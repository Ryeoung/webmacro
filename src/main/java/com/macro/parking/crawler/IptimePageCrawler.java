package com.macro.parking.crawler;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.page.iptime.IptimeLoginPage;
import com.macro.parking.page.iptime.MainPage;
import com.macro.parking.page.iptime.TicketApplyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IptimePageCrawler extends PageCrawler{
    private final IptimeLoginPage iptimeLoginPage;
    private final MainPage mainPage;
    private final TicketApplyPage ticketApplyPage;

    @Autowired
    public IptimePageCrawler(IptimeLoginPage iptimeLoginPage, @Qualifier("iptimeMainPage") MainPage mainPage,
                             @Qualifier("iptimeApplyPage")  TicketApplyPage ticketApplyPage) {
        this.iptimeLoginPage = iptimeLoginPage;
        this.mainPage = mainPage;
        this.ticketApplyPage = ticketApplyPage;
    }


    @Override
    public void setupChromeDriver() {
        super.setupChromeDriver();
        this.iptimeLoginPage.init(this.driver);
        this.mainPage.init(this.driver);
        this.ticketApplyPage.init(this.driver);
    }

    public void load(String url) {
        this.iptimeLoginPage.navigate(url);
    }

    public void login(String id, String password) {
        this.iptimeLoginPage.fillLoginInfo(id, password);
        this.iptimeLoginPage.clickLoginBtn();
    }

    public void goToApplyTab() {
        this.mainPage.clickLinkToApplyTabBtn();
    }

    public void applyTickets(List<ParkingInfo> parkingInfos) {

        int idx = 0;

        String parkingLotName = parkingInfos.get(0).getParkingTicket().getParkingLot().getName();

        ParkingInfo parkingInfo = null;

        try {
            for(idx = 0; idx < parkingInfos.size(); idx++) {
                parkingInfo  = parkingInfos.get(idx);


                if(this.ticketApplyPage.searchCarByCarNum(parkingInfo.getCar().getNumber())) {
                   if(this.ticketApplyPage.isExitAppliedTicket()) {
                       parkingInfo.setAppFlag(StatusCodeType.TICKET_EXIST_ERROR);
                   } else {
                       this.ticketApplyPage.applyTicket(parkingInfo.getParkingTicket().getWebName());
                       parkingInfo.setAppFlag(StatusCodeType.SUCCESS);
                   }
               } else {
                    parkingInfo.setAppFlag(StatusCodeType.NO_CAR_ERROR);
               }

                Thread.sleep(500);
               this.ticketApplyPage.clearCarSearchInput();



            }
        }  catch(Exception e ) {
            ParkingInfo info = null;

            for( ;idx < parkingInfos.size(); idx++) {
                info = parkingInfos.get(idx);
                info.setAppFlag(StatusCodeType.SELENIUM_ERROR);
            }
            e.printStackTrace();
        }
    }
}
