import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";


import {
    ProgressBar
} from "./progressBar.js";

export class Ticket{
    constructor(tab) {
        this.checkList = document.getElementById("check");
        this.checkedList = document.getElementById("checked");
        this.readyToCheckList = document.getElementById("ready");
        this.cancelList = document.getElementById("cancel");
        this.tab = tab;

        this.pushTicketBtn = document.getElementById("pushTicket");
        this.getTicketBtn = document.getElementById("getTicket");
        this.repushTicketBtn = document.getElementById("repushTicket");
        this.checkCards = Array.from(this.checkList.children);

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
    
    
    requestTickesOfToday() {
        ajax({
            url : "/parking/api/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8"
        }, this.attachCards.bind(this));
        
    }
    
    requestTicketsOfSearchWord(word) {
    	ajax({
            url : `/parking/api/search?word=${word}`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
            data : word
        },(data) => {
        	this.deleteAllTicketList();
        	this.attachCards(data);
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
    
    attachCards(data) {
        let carInfos = data;
        let resultHTML = templateParser.getResultHTML(template.cardTemplate, carInfos);
        let cards = templateParser.stringToElement(resultHTML);
        
        this.makeCards(cards);
        
        this.checkCards = Array.from(this.checkList.children);
    }

    makeCards(cards) {
        cards.childNodes.forEach(card => {
            let childNodeOfCard = Array.from(card.children);
            let cardStatus = this.makeCard(childNodeOfCard, null)
            this.moveCardFromTabToTab(card, cardStatus);
        });
    }

    changeCardsFromParkingInfos(cardsObject, parkingInfos) {
        parkingInfos.forEach(parkingInfo => {
            let card = cardsObject[parkingInfo.parkingInfoId];
            this.changeCardFromParkingInfo(card, parkingInfo);
        });
    }

    changeCardFromParkingInfo(card, parkingInfo) {
        let childNodeOfCard = Array.from(card.children);
        let cardStatus = this.makeCard(childNodeOfCard, parkingInfo.code);
        this.moveCardFromTabToTab(card, cardStatus);
    }

    moveCardFromTabToTab(card, cardStatus) {
        let ticketCnt = {
            check: 0,
            checked: 0,
            ready: 0,
            cancel: 0
        };

        this.moveCardAboutCode(card , cardStatus,  ticketCnt);
        this.tab.updateTicketCnt(ticketCnt);
    }

    makeCard(childNodeOfCard, statusData) {
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
            let card = stateNode.parentElement.parentElement;
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
            this.readyToCheckList.prepend(card);
            return ;
        }  else if(status == this.ticketStatusCode.CANCEL) {
            ticketCnt.cancel += 1;
            this.cancelList.prepend(card);
            return ;
        }
        if(!card.parentElement) {
            ticketCnt.check += 1;
            this.checkList.prepend(card);
        }
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

    	let ticketCnt = {};
    	let tabId = card.parentNode.getAttribute("id");
    	ticketCnt[tabId] = -1;
    	this.tab.updateTicketCnt(ticketCnt);

    	card.parentNode.removeChild(card);
    	let appFlag = changeStatus;
    	
    	ajax({
            url : `/parking/api/ticket/${parkingInfoId}`,
            method : "PUT",
            contentType : "application/json; charset=utf-8",
            data : appFlag
        }, (parkingInfo) => {
            this.changeCardFromParkingInfo(card, parkingInfo);
    	});
    }
   

    addClickEventToGetNewTickectBtn() {
    	this.getTicketBtn.addEventListener("click",
            this.getNewTicket.bind(this));
    }

    getNewTicket(){
    	ajax({
            url : `/parking/api/new/cars`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, this.attachCards.bind(this));
    }

    
    addClickEventToPushTicketBtn(){
        this.pushTicketBtn.addEventListener("click", 
        this.clickEventHandlerAboutPushTicket.bind(this));
    
    }

    clickEventHandlerAboutPushTicket(event) {
        let parkingLotDict = this.getParkingLotOfTicket(this.checkCards);
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

        ajax({
            url : `/parking/api/apply/parkingLot/${nextPushParkingLot.name}`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, (parkingInfos) => {
            this.progressBar.totalProgressBar.value += nextPushParkingLot.count;
            this.changeCardsFromParkingInfos(nextPushParkingLot.elmts, parkingInfos);
            this.requestPushTicketByParkingLot();
        });

    }
    getParkingLotOfTicket(cards) {
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
    getSeleniumErrorCardObjects(parkingLotName = null) {
        let seleniumErrorCards = document.getElementsByClassName("serror");
        let cardObjects = [];

        Array.from(seleniumErrorCards).forEach( card => {
            let parkingLotNameOfCurCard = Array.from(card.children)[4].innerText;
            if(parkingLotName === null && parkingLotName === parkingLotNameOfCurCard) {
                let cardObject = {}
                cardObject[card.dataset.id] = card;
                cardObjects.push(cardObject);
            }
        })

        return cardObjects;
    }

    addClickEventToRepushTicketBtn() {
        let seleniumErrorCardsObject = this.getSeleniumErrorCardObjects();
        if(seleniumErrorCardsObject.length <= 0) {
            return;
        }

        this.repushTicketBtn.addEventListener("click", () => {
            ajax({
                url : "/parking/api/apply/error/car",
                method : "GET",
                contentType : "application/json; charset=utf-8"
            }, (parkingInfos) => {
                this.changeCardsFromParkingInfos(seleniumErrorCardsObject, parkingInfos);
            });
        });
    }
}