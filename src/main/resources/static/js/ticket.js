import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

export class Ticket{
    constructor(tab) {
        this.checkList = document.getElementById("check");
        this.checkedList = document.getElementById("checked");
        this.readyToCheckList = document.getElementById("ready");
        this.cancelList = document.getElementById("cancel");
        this.tab = tab;

        this.pushTicketBtn = document.getElementById("pushTicket");
        this.getTicketBtn = document.getElementById("getTicket");
        this.checkCards = Array.from(this.checkList.children);

        this.addClickEventToGetNewTickectBtn();
        this.addClickEventToPushTicketBtn();
        

    }
    
    
    requestTickesOfToday() {
        ajax({
            url : "/parking/api/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8"
        }, this.makeCardOfCar.bind(this));
        
    }
    
    requestTicketsOfSearchWord(word) {
    	ajax({
            url : `/parking/api/search?word=${word}`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
            data : word
        },(data) => {
        	this.deleteAllTicketList();
        	this.makeCardOfCar(data);
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
    
    makeCardOfCar(data) {
        let carInfos = data;
        let resultHTML = templateParser.getResultHTML(template.cardTemplate, carInfos);
        let cards = templateParser.stringToElement(resultHTML);
        let ticketCnt = {
    			check: 0,
    			checked: 0,
    			ready: 0,
    			cancel: 0
        };
        
        cards.childNodes.forEach(card => {
        	this.moveCardAboutCode(card , ticketCnt);
        });
        
        this.tab.updateTicketCnt(ticketCnt);
        
        this.checkCards = Array.from(this.checkList.children);
    }

    
    moveCardAboutCode(card, ticketCnt){
    	const OK = "ok";
        const SELENIUM_ERROR = "fail01";
        const NO_CAR_ERROR = "fail02";
        const TICKET_EXIST_ERROR = "fail03";
        const NOT_WORKING = "noting"
        const CHECK_TICKET = "check";
        const CANCEL = "cancel";

    	let cardData = {};
        
        let childNode = Array.from(card.children);
        let checkBtn = childNode[5].children[0];
        let deleteBtn = childNode[5].children[1];
        
        checkBtn.addEventListener("click", (event) => {
        	this.clickEventHandlerAboutCheckTicket(event, {appFlag : 'CHECK_TICKET'});
        });
        deleteBtn.addEventListener("click", (event) => {
        	this.clickEventHandlerAboutCheckTicket(event, {appFlag : 'CANCEL'});
        });
        
        let stateNode = childNode[4];

        let status = stateNode.innerHTML;
        if(status === TICKET_EXIST_ERROR) {
            stateNode.innerHTML = "주차권이 이미 존재";
            this.hideBtn(deleteBtn);
            ticketCnt.checked += 1;
            this.checkedList.prepend(card);
            return;
        } else if(status == CHECK_TICKET) {
        	stateNode.innerHTML = "주차 완료";
        	this.hideBtn(checkBtn, deleteBtn);
        	ticketCnt.checked += 1;
        	this.checkedList.prepend(card);
            return;
        } else if(status === SELENIUM_ERROR) {
            stateNode.innerHTML = "해당 티켓에 관한 시스템 에러가 발생";
        } else if(status === OK) {
            stateNode.innerHTML = "주차확인 필요";
            ticketCnt.ready += 1;
            this.readyToCheckList.prepend(card);
            return;
        } else if(status === NO_CAR_ERROR) {
        	stateNode.innerHTML = "차가 아직 안 왔습니다.";
        } else if(status == NOT_WORKING) {
        	stateNode.innerHTML = "";
        } else if(status == CANCEL) {
        	stateNode.innerHTML = "취소";
            this.hideBtn(deleteBtn, checkBtn);
            ticketCnt.cancel += 1;
            this.cancelList.prepend(card);
            return;
        }
        
       
        ticketCnt.check += 1;
        this.checkList.prepend(card);
    }
    
    hideBtn(...btns){
    	for(let idx = 0; idx < btns.length; idx++) {
    		let btn =btns[idx];
    		btn.style.display = "none";
    	}
    }
 
    
    clickEventHandlerAboutCheckTicket(event, changed){
    	let btn = event.currentTarget;
    	let card = btn.parentElement.parentElement;
    	let parkingInfoId = Number(card.dataset.id);

    	let ticketCnt = {};
    	ticketCnt[card.parentNode.getAttribute("id")] = -1;
    	
    	this.tab.updateTicketCnt(ticketCnt);

    	card.parentNode.removeChild(card);
    	let appFlag = changed;
    	
    	ajax({
            url : `/parking/api/ticket/${parkingInfoId}`,
            method : "PUT",
            contentType : "application/json; charset=utf-8",
            data : appFlag
        }, this.makeCardOfCar.bind(this));
        
    }
   

    addClickEventToGetNewTickectBtn() {
    	this.getTicketBtn.addEventListener("click", 
    			this.clickEventhandlerAboutGetNewTicket.bind(this));
    }
    
    clickEventhandlerAboutGetNewTicket() {
    	this.getNewTicket();
    }
        
    getNewTicket(){
    	ajax({
            url : `/parking/api/new/cars`,
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, this.makeCardOfCar.bind(this));
    }

    
    addClickEventToPushTicketBtn(){
        this.pushTicketBtn.addEventListener("click", 
        this.clickEventHandlerAboutPushTicket.bind(this));
    
    }

    clickEventHandlerAboutPushTicket(event) {
        ajax({
            url : "/parking/api/apply/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, this.clickEventOfPushTicketSuccess.bind(this));
    }

    clickEventOfPushTicketSuccess(data) {
        this.checkList.innerHTML = "";
        this.tab.checkCntElmt.innerHTML = 0;
        this.makeCardOfCar(data);
    }
}