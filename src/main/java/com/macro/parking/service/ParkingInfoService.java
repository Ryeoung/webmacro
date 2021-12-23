package com.macro.parking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.ParkingInfoDao;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.enums.StatusCodeType;

@Service
public class ParkingInfoService {
	@Autowired
	ParkingInfoDao parkingInfoDao;
	private final Logger logger = LoggerFactory.getLogger(ParkingInfoService.class);

	/**
	 * @param parkingInfos 주차정보
	 * @return List<ParkingInfo>
	 *     주차정보 삽입
	 */
	public List<ParkingInfo> addAllTicket(List<ParkingInfo> parkingInfos) {
		return parkingInfoDao.saveAll(parkingInfos);
	}

	/**
	 * @param parkingInfos 주차정보 리스트
	 * 주차 정보 리스트 업데이트
	 */
	public void updateAllParkingInfo(List<ParkingInfo> parkingInfos) {
		for(ParkingInfo info :parkingInfos) {
			updateParkingInfo(info);
		}
	}

	/**
	 * @param parkingInfo 주차정보
	 *  주차정보를 업데이트 하지만
	 *  실패시 로그 찍어 주기
	 *
	 */
	public void updateParkingInfo(ParkingInfo parkingInfo) {
		ParkingInfo oldParkingInfo = parkingInfoDao.findByParkingInfoId(parkingInfo.getParkingInfoId());
		if(oldParkingInfo != null) {
			try{
				parkingInfoDao.save(parkingInfo);
			} catch (Exception e) {
				logger.warn("예약시간 : " + oldParkingInfo.getOrderTime() + " "
						+ oldParkingInfo.getParkingTicket().getAppName()
						+ " 발권 업데이트가 예기치 않게 종료되었습니다.");
			}

		}

	}

	/**
	 * @param codeType 상태정보
	 * @return @return List<ParkingInfo>
	 *     상태 정보를 가지고 주차권 리스트 가져오기
	 */
	public List<ParkingInfo> findAllWillCrawling(StatusCodeType codeType) {
		List<StatusCodeType> appFlags = this.getStatusCodeTypeOfWillCrawling(codeType);
		return parkingInfoDao.findByAppFlagInAndOrderTimeGreaterThanEqual(appFlags, getToday());
	}

	/**
	 * @param codeType 상태 정보
	 * @param parkingTickets 주차권
	 * @return List<ParkingInfo>
	 *     상태정보와 주차권 리스트를 가지고 주차 정보를 가졍괴
	 */
	public List<ParkingInfo> findAllWillCrawling(StatusCodeType codeType, List<ParkingTicket> parkingTickets) {
		List<StatusCodeType> appFlags = this.getStatusCodeTypeOfWillCrawling(codeType);

		return parkingInfoDao.findByAppFlagInAndParkingTicketInAndOrderTimeGreaterThanEqual(appFlags,parkingTickets, getToday());
	}

	/**
	 * @param codeType 상태 정보
	 * @return List<StatusCodeType>
	 *     상태 정보가 Null일 경우 크롤링 상태의 상태정보를 list로 반환
	 *     상태 정보가 isNotNull일 경우 해상 상태를 반환
	 */
	public List<StatusCodeType> getStatusCodeTypeOfWillCrawling(StatusCodeType codeType) {
		List<StatusCodeType> appFlags = new ArrayList<>();
		if (codeType == null) {
			appFlags.add(StatusCodeType.NO_CAR_ERROR);
			appFlags.add(StatusCodeType.NOT_WORKING);
			appFlags.add(StatusCodeType.SELENIUM_ERROR);
		} else {
			appFlags.add(codeType);
		}

		return appFlags;
	}

	/**
	 * @return ParkingInfo
	 *  데이터베이스에서 오늘 날짜로 저장된 주차정보 중 최신 데이터 가져오기
	 */
	public ParkingInfo findlatelyParkingInfoByToday() {
		return parkingInfoDao.findTopByOrderTimeGreaterThanEqualOrderByOrderTimeDescParkingInfoIdDesc(getToday());

	}

	/**
	 * @return List<ParkingInfo>
	 *  오늘 날짜 주차 정보 찾기
	 */
	public List<ParkingInfo> findAllByToday() {
		return parkingInfoDao.findByOrderTimeGreaterThanEqual(getToday());
	}

	/**
	 * @param parkingInfoId 주차정보 기본키
	 * @return ParkingInfo
	 *
	 *  주차 정보 기본키로 찾기
	 */
	public ParkingInfo findByParkingInfoId(int parkingInfoId) {
		return parkingInfoDao.findByParkingInfoId(parkingInfoId);
	}

	/**
	 * @param carNum 차량번호
	 * @param parkingTickets 주차권
	 * @return List<ParkingInfo>
	 *
	 *    차량 번호와 주차권으로 주차정보 가져오기
	 */
	public List<ParkingInfo> findByParkingTicketAndCar(String carNum, List<ParkingTicket> parkingTickets) {
		String likeQuery = "%" + carNum + "%";
		return parkingInfoDao.findByOrderTimeGreaterThanEqualAndParkingTicketInOrCar_NumberLike(getToday(), parkingTickets, likeQuery);
	}

	/**
	 * @return 오늘 날짜 데이터 반환
	 */
	private LocalDateTime getToday() {
		LocalDate today = LocalDate.now();
		return today.atStartOfDay();
	}
}
