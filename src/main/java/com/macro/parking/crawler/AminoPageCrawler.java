package com.macro.parking.crawler;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.page.amino.AminoLoginPage;

import com.macro.parking.page.amino.TicketApplyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AminoPageCrawler extends PageCrawler{
    private final AminoLoginPage loginPage;
    private final TicketApplyPage ticketApplyPage;
    @Autowired
    public AminoPageCrawler(AminoLoginPage loginPage,
                            @Qualifier("aminoTicketApplyPage") TicketApplyPage ticketApplyPage) {
        super();
        this.loginPage = loginPage;
        this.ticketApplyPage = ticketApplyPage;
    }

    @Override
    public void setupChromeDriver() {
        super.setupChromeDriver();
        this.loginPage.init(this.driver);
        this.ticketApplyPage.init(this.driver);
    }

    public void load(String url) {
        loginPage.load(url);
    }

    public void login(String id, String pwd) {
        loginPage.fillLogin(id, pwd);
        loginPage.clickLoginBtn();
    }


    public void applyParkingTicket(List<ParkingInfo> parkingInfos) {
        int idx = 0;

        String parkingLotName = parkingInfos.get(0).getParkingTicket().getParkingLot().getName();

        ParkingInfo parkingInfo = null;
        this.ticketApplyPage.load();

        try {
            for(idx = 0; idx < parkingInfos.size(); idx++) {
                parkingInfo  = parkingInfos.get(idx);
                String carNumber = parkingInfo.getCar().getNumber();
                System.out.println(carNumber);


                this.ticketApplyPage.searchCar(carNumber);
                Thread.sleep(500);
                if(this.ticketApplyPage.isExistCar(carNumber)) {
                    //차 있는 경우
                    //해당 차량 주차권 정보로 가기
                    if(this.ticketApplyPage.isHavingTicket(parkingInfo.getParkingTicket().getWebName())) {
                        //해당 차량의 주차권이 이미 존재하는 경우
                        parkingInfo.setAppFlag(StatusCodeType.TICKET_EXIST_ERROR);
                    } else {
                        if(this.ticketApplyPage.applyTicket(parkingInfo.getParkingTicket().getWebName())) {
                            //해당 차량이 구매한 주차권을 구매한 경우
                            parkingInfo.setAppFlag(StatusCodeType.SUCCESS);
                        }
                    }
                } else {
                    //치 없는 경우
                    parkingInfo.setAppFlag(StatusCodeType.NO_CAR_ERROR);
                }
            }
        }  catch(Exception e) {
            int curIdx = 0;
            ParkingInfo info = null;

            if(parkingInfo != null) {
                curIdx = idx;
            }
            for( ;curIdx < parkingInfos.size(); curIdx++) {
                info = parkingInfos.get(curIdx);
                info.setAppFlag(StatusCodeType.SELENIUM_ERROR);
            }
            e.printStackTrace();
        }

    }
}
