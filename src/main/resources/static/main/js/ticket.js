import template from "./template.js";

import {
    ProgressBar
} from "./progressBar.js";

export class Ticket{
    constructor(tab, ajax, templateParser) {
        this.checkList = document.getElementById("check");
        this.checkedList = document.getElementById("checked");
        this.readyToCheckList = document.getElementById("ready");
        this.cancelList = document.getElementById("cancel");
        this.tab = tab;

        this.pushTicketBtn = document.getElementById("pushTicket");
        this.getTicketBtn = document.getElementById("getTicket");
        this.repushTicketBtn = document.getElementById("repushTicket");
        this.checkCards = Array.from(this.checkList.children);

        this.ajax = ajax;
        this.templateParser = templateParser;

        this.progressBar = new ProgressBar(template, templateParser);

        this.addClickEventToGetNewTickectBtn();
        this.addClickEventToPushTicketBtn();
        this.addClickEventToRepushTicketBtn();

        this.ticketStatusCode = {
            OK : "ok",
            SELENIUM_ERROR : "fail01",
            NO_CAR_ERROR : "fail02",
            TICKET_EXIST_ERROR : "fail03",
            NOT_WORKING : "noting",
            CHECK_TICKET : "check",
            CANCEL : "cancel"

        }
    }
    
    
    requestTicketsOfToday() {
        this.ajax({
            url : "/parking/api/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8"
        }, this.makeCards.bind(this));
        
    }
    
    requestTicketsOfSearchWord(word) {
    	this.ajax({
            url : `/parking/api/search?word=${word}`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
            data : word
        },(data) => {
        	this.deleteAllTicketList();
        	this.makeCards(data);
        });
    }
    
    deleteAllTicketList(){
    	this.checkList.innerHTML = "";
    	this.checkedList.innerHTML = "";
    	this.readyToCheckList.innerHTML = "";
    	this.cancelList.innerHTML = "";
    	
    	 this.tab.checkCntElmt.innerHTML = 0;
         this.tab.checkedCntElmt.innerHTML = 0;
         this.tab.readyCntElmt.innerHTML = 0;
         this.tab.cancelCntElmt.innerHTML = 0;
    }
    
    makeCards(data) {
        let carInfos = data;
        let resultHTML = this.templateParser.getResultHTML(template.cardTemplate, carInfos);
        let cards = this.templateParser.stringToElement(resultHTML);
        
        this.makeCardElements(cards);
        
        this.checkCards = Array.from(this.checkList.children);
    }

    makeCardElements(cards) {
        cards.childNodes.forEach(card => {
            this.updateCardElement(card);
        });
    }

    updateCardElement(card, statusData = null) {
        let cardStatus = this.makeChildrenElementsOfCard(card, statusData);
        let ticketCnt = {
            check: 0,
            checked: 0,
            ready: 0,
            cancel: 0
        };

        if(card.parentElement) {
            let cardListId = card.parentElement.id;
            ticketCnt[cardListId] = -1;
        }

        this.moveCardFromTabToTab(card, cardStatus, ticketCnt);
    }

    changeCardsElementsByParkingInfos(cardsObject, parkingInfos) {
        parkingInfos.forEach(parkingInfo => {
            let card = cardsObject[parkingInfo.parkingInfoId];
            this.updateCardElement(card, parkingInfo.code);
        });
    }

    moveCardFromTabToTab(card, cardStatus, ticketCnt = {   check: 0,
                                                                checked: 0,
                                                                ready: 0,
                                                                cancel: 0 }) {
        this.moveCardAboutCode(card , cardStatus,  ticketCnt);
        this.tab.updateTicketCnt(ticketCnt);
    }

    makeChildrenElementsOfCard(card, statusData = null) {
        let childNodeOfCard = Array.from(card.children);

        let checkBtn = childNodeOfCard[5].children[0];
        let deleteBtn = childNodeOfCard[5].children[1];

        checkBtn.addEventListener("click", (event) => {
            this.clickEventHandlerAboutCheckTicket(event, {appFlag : 'CHECK_TICKET'});
        });
        deleteBtn.addEventListener("click", (event) => {
            this.clickEventHandlerAboutCheckTicket(event, {appFlag : 'CANCEL'});
        });
        let status = ""
        let stateNode = childNodeOfCard[4];

        if(statusData) {
            status = statusData;
        } else {
            status = stateNode.innerHTML;
        }

        if(status === this.ticketStatusCode.TICKET_EXIST_ERROR) {
            stateNode.innerHTML = "주차권이 이미 존재";
            this.hideBtn(deleteBtn);
            return status;
        } else if(status == this.ticketStatusCode.CHECK_TICKET) {
            stateNode.innerHTML = "주차 완료";
            this.hideBtn(checkBtn, deleteBtn);
            return status;
        } else if(status === this.ticketStatusCode.SELENIUM_ERROR) {
            stateNode.innerHTML = "해당 티켓에 관한 시스템 에러가 발생";
            let card = stateNode.parentElement;
            card.className += " serror";

        } else if(status === this.ticketStatusCode.OK) {
            stateNode.innerHTML = "주차확인 필요";
            return status;
        } else if(status === this.ticketStatusCode.NO_CAR_ERROR) {
            stateNode.innerHTML = "차가 아직 안 왔습니다.";
        } else if(status == this.ticketStatusCode.NOT_WORKING) {
            stateNode.innerHTML = "";
        } else if(status == this.ticketStatusCode.CANCEL) {
            stateNode.innerHTML = "취소";
            this.hideBtn(deleteBtn, checkBtn);
            return status;
        }

        return status;
    }

    moveCardAboutCode(card, status, ticketCnt){
        if(status === this.ticketStatusCode.TICKET_EXIST_ERROR) {
            ticketCnt.checked += 1;
            this.checkedList.prepend(card);
            return ;
        } else if(status == this.ticketStatusCode.CHECK_TICKET) {
        	ticketCnt.checked += 1;
        	this.checkedList.prepend(card);
            return ;
        } else if(status === this.ticketStatusCode.OK) {
            ticketCnt.ready += 1;
            this.readyToCheckList.append(card);
            return ;
        }  else if(status == this.ticketStatusCode.CANCEL) {
            ticketCnt.cancel += 1;
            this.cancelList.prepend(card);
            return ;
        }
        if(!card.parentElement) {
            this.checkList.prepend(card);
        }
        ticketCnt.check += 1;
    }
    
    hideBtn(...btns){
    	for(let idx = 0; idx < btns.length; idx++) {
    		let btn =btns[idx];
    		btn.style.display = "none";
    	}
    }
 
    
    clickEventHandlerAboutCheckTicket(event, changeStatus){
    	let btn = event.currentTarget;
    	let card = btn.parentElement.parentElement;
    	let parkingInfoId = Number(card.dataset.id);

    	// card.parentNode.removeChild(card);
    	let appFlag = changeStatus;
    	
    	this.ajax({
            url : `/parking/api/ticket/${parkingInfoId}`,
            method : "PUT",
            contentType : "application/json; charset=utf-8",
            data : appFlag
        }, (parkingInfo) => {
            this.updateCardElement(card, parkingInfo.code);
    	});
    }
   

    addClickEventToGetNewTickectBtn() {
    	this.getTicketBtn.addEventListener("click",
            this.getNewTicket.bind(this));
    }

    getNewTicket(){
        let loading = document.getElementById("loading");
        loading.style.display = "flex";

    	this.ajax({
            url : `/parking/api/new/cars`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, (data) => {
    	    this.makeCards(data);
            loading.style.display = "none";
        });
    }

    
    addClickEventToPushTicketBtn(){
        this.pushTicketBtn.addEventListener("click", () => {
                let parkingLotDict = this.getParkingLotOfTicket(this.checkCards);
                this.pushTicketsByParkingLotDict(parkingLotDict);
            }
        );
    
    }

    pushTicketsByParkingLotDict(parkingLotDict = {}) {
        let parkingLotCnt = Object.keys(parkingLotDict).length;
        if(parkingLotCnt === 0) {
            return;
        }
        this.progressBar.setParkingLotObject(parkingLotDict);
        this.progressBar.totalProgressBar.max =  this.progressBar.totalTicketCnt;
        this.progressBar.totalProgressBar.value = 0;
        this.progressBar.nextPushTicketIdx = 0;

        this.progressBar.popupProgressModal();
        this.requestPushTicketByParkingLot();
    }

    requestPushTicketByParkingLot() {
        let nextPushParkingLot = this.progressBar.getNextPushParkingLot();
        if(nextPushParkingLot === null) {
            this.progressBar.changeLabelText(`주차권 발권 완료 `);
            return;
        }
        this.progressBar.changeLabelText(`${nextPushParkingLot.name} 주차권 발권 중 .... `);

        this.ajax({
            url : `/parking/api/apply/parkingLot/${nextPushParkingLot.name}`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, (parkingInfos) => {
            this.progressBar.totalProgressBar.value += nextPushParkingLot.count;
            this.changeCardsElementsByParkingInfos(nextPushParkingLot.elmts, parkingInfos);
            this.requestPushTicketByParkingLot();
        });

    }
    getParkingLotOfTicket(cards = []) {
        let parkingLotDict = {};
        cards.forEach((card) => {
            let childNode = Array.from(card.children);
            let parkingLotName = childNode[1].innerText;


            let cardId = card.dataset.id;
            if(!parkingLotDict[parkingLotName]) {
                parkingLotDict[parkingLotName] = {
                    name : parkingLotName,
                    count : 1,
                    elmts : {}
                };
            } else {
                parkingLotDict[parkingLotName].count += 1;
            }
            parkingLotDict[parkingLotName].elmts[cardId] = card;

        });

        return parkingLotDict;
    }
    getSeleniumErrorCards(parkingLotName = null) {
        let seleniumErrorCards = Array.from(document.getElementsByClassName("serror"));
        if( parkingLotName === null) {
            return seleniumErrorCards;
        }

        let cards = [];
        Array.from(seleniumErrorCards).forEach( card => {
            let parkingLotNameOfCurCard = Array.from(card.children)[4].innerText;
            if(parkingLotName === parkingLotNameOfCurCard) {
                cards.push(card);
            }
        });

        return cards;
    }

    addClickEventToRepushTicketBtn() {
        this.repushTicketBtn.addEventListener("click", () => {
            let seleniumErrorCards = this.getSeleniumErrorCards();
            let parkingLotDict = this.getParkingLotOfTicket(seleniumErrorCards);
            this.pushTicketsByParkingLotDict(parkingLotDict);
        });
    }
}