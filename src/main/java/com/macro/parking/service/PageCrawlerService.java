package com.macro.parking.service;

import java.util.*;

import com.macro.parking.crawler.*;
import com.macro.parking.enums.WebSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.domain.Car;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;


/**
 * 페이지 크롤링 service
 */
@Service
public class PageCrawlerService {
	
	@Autowired
	ModuPageCrawler moduPageCrawler;
	
	@Autowired
	IParkingPageCralwer iparkPageCrawler;

	@Autowired
	IptimePageCrawler iptimePageCrawler;
	
	@Autowired
	ParkingLotService parkingLotService;
	
	@Autowired
	ParkingInfoService parkingInfoService;
	
	@Autowired
	ParkingTicketService parkingTicketService;

	@Autowired
	AminoPageCrawler aminoPageCrawler;
	
	@Autowired
	CarService carService;

	/**
	 * @param lastParkingInfo 현재까지 최신 주차 예약 정보
	 * @return List<ParkingInfo>
	 *  현재까지 최신 주차 예약 정보부터 실시간 주차 예약 정보 크롤링해 갱신
	 */
   	public List<ParkingInfo> getParkingTicketReservation(ParkingInfo lastParkingInfo) {
   			moduPageCrawler.setupChromeDriver();
			moduPageCrawler.load();
   			moduPageCrawler.login();
			List<ParkingInfo> parkingInfos = moduPageCrawler.getParkingTicketData(lastParkingInfo);
			moduPageCrawler.quit();

			List<ParkingInfo> resultParkingInfos = new LinkedList<>();

			parkingInfos.forEach((parkingInfo) -> {
				if(this.getParkingInfo(parkingInfo)) {
					resultParkingInfos.add(parkingInfo);
				}
			});

			return resultParkingInfos;
	}

	/**
	 * @param parkingInfo 주차 정보 객체
	 * @return boolean
	 *  주차 정보 객체로 관계 테이블 정보 얻기
	 */
   	public boolean getParkingInfo(ParkingInfo parkingInfo) {
   		String carNum = parkingInfo.getCar().getNumber();
   		Car car = carService.findByNumber(carNum);

   		if(parkingInfo.getParkingTicket().getAppName().equals("월정기권")) {
   			return false;
		}

		if(car == null) {
			car = carService.addCar(parkingInfo.getCar());
		}
		ParkingLot parkingLot = parkingLotService.findByName(parkingInfo.getParkingTicket().getParkingLot().getName());

		ParkingTicket parkingTicket = parkingTicketService.findByAppNameAndParkingLot(parkingInfo.getParkingTicket().getAppName(),  parkingLot);
		parkingInfo.setCar(car);
		parkingInfo.setParkingTicket(parkingTicket);
		return true;
   	}


	/**
	 * @param parkingInfos 주차 예약 정보
	 *  주차권 발권 하기
	 */
	public void applyParkingTickets(List<ParkingInfo> parkingInfos){
		List<ParkingInfo> sortedParkingInfo = this.sortByParkingLotName(parkingInfos);
		
		ParkingInfo pre = sortedParkingInfo.get(0);
		ParkingInfo cur = null;
		ParkingLot parkingLot;
		List<ParkingInfo> subList = new ArrayList<>();
		subList.add(pre);
		
		
		for(int idx = 1, fin=sortedParkingInfo.size(); idx < fin; idx++) {
			cur = sortedParkingInfo.get(idx);
			if(pre.getParkingTicket().getParkingLot().getName().equals(cur.getParkingTicket().getParkingLot().getName())) {
				subList.add(cur);
				
			} else {
				this.addTicketByParkingLot(subList);
				subList.clear();
				pre = cur;
				subList.add(pre);
			}
		}
		
		this.addTicketByParkingLot(subList);
		
	}

	/**
	 * @param parkingInfos 주차 정보 객체들
	 * @return List<ParkingInfo>
	 *   주차장 이름으로 정렬
	 */
	public List<ParkingInfo> sortByParkingLotName(List<ParkingInfo> parkingInfos) {
		List<ParkingInfo> sortedParkingInfo = new ArrayList<>(parkingInfos);
		Collections.sort(sortedParkingInfo, new Comparator<ParkingInfo>() {

			@Override
			public int compare(ParkingInfo o1, ParkingInfo o2) {
				return o1.getParkingTicket().getParkingLot().getName().
						compareTo(o2.getParkingTicket().getParkingLot().getName());
			}
		});
		return sortedParkingInfo;
	}

	/**
	 * @param parkingInfos 주차 정보
	 *   주차 정보 내 주차장 이름을 기준으로 주차권 발권 기능
	 */
	private void addTicketByParkingLot(List<ParkingInfo> parkingInfos) {
		ParkingInfo parkingInfo = parkingInfos.get(0);
		ParkingLot parkingLot = parkingInfo.getParkingTicket().getParkingLot();
		String url = parkingLot.getWebsite();

		PageCrawler pageCrawler = null;
		if(WebSite.IPARK.isEqual(url)) {
			pageCrawler = this.applyTicketToIpark(parkingInfos, parkingLot);
		} else if(WebSite.IPTIME.isEqual(url)) {
			pageCrawler = this.applyTicketToIptime(parkingInfos, parkingLot);
		} else if(WebSite.아미노.isEqual(url)){
			pageCrawler = this.applyTicketToAmino(parkingInfos, parkingLot);
		} else {
			return;
		}

		pageCrawler.quit();
		parkingInfoService.updateAllParkingInfo(parkingInfos);

	}

	/**
	 * @param parkingInfos 주차 정보
	 * @param parkingLot 주차장
	 * @return PageCrawler
	 *
	 *  iptime 관리 사이트에서 주차권 발권 기능
	 */
	private PageCrawler applyTicketToIptime(List<ParkingInfo> parkingInfos, ParkingLot parkingLot) {
		iptimePageCrawler.setupChromeDriver();
		iptimePageCrawler.load(parkingLot.getWebsite());
		iptimePageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		iptimePageCrawler.goToApplyTab();
		iptimePageCrawler.applyTickets(parkingInfos);
		return iptimePageCrawler;
   	}

	/**
	 * @param parkingInfos 주차 정보
	 * @param parkingLot 주차장
	 * @return PageCrawler
	 *
	 *  ipark 관리 사이트에서 주차권 발권 기능
	 */
	private PageCrawler applyTicketToIpark(List<ParkingInfo> parkingInfos, ParkingLot parkingLot) {
		iparkPageCrawler.setupChromeDriver();
		iparkPageCrawler.load(parkingLot.getWebsite());
		iparkPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		iparkPageCrawler.applyParkingTicket(parkingInfos);
		return iparkPageCrawler;
	}

	/**
	 * @param parkingInfos 주차 정보
	 * @param parkingLot 주차장
	 * @return PageCrawler
	 *
	 *  amino 관리 사이트에서 주차권 발권 기능
	 */
	private PageCrawler applyTicketToAmino(List<ParkingInfo> parkingInfos, ParkingLot parkingLot) {
		aminoPageCrawler.setupChromeDriver();
		aminoPageCrawler.load(parkingLot.getWebsite());
		aminoPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		aminoPageCrawler.applyParkingTicket(parkingInfos);
		return aminoPageCrawler;
	}
	
}
