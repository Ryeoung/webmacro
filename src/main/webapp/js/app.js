import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

class App {
    constructor() {
        this.checkList = document.getElementById("check");
        this.checkedList = document.getElementById("checked");
        this.readyToCheckList = document.getElementById("ready");
        this.pushTicketBtn = document.getElementById("pushTicket");
        this.getTicketBtn = document.getElementById("getTicket");
        this.checkCards = Array.from(this.checkList.children);
        this.requestAllCarInfoToday();
        this.addClickEventToGetNewTickectBtn();
        this.addClickEventToPushTicketBtn();

    }

    requestAllCarInfoToday() {
        ajax({
            url : "/parking/api/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, this.makeCardOfCar.bind(this));
        
    }

    makeCardOfCar(data) {
        let carInfos = data;
        let resultHTML = templateParser.getResultHTML(template.cardTemplate, carInfos);
        let cards = this.stringToElement(resultHTML);
        cards.childNodes.forEach(card => {
        	this.moveCardAboutCode(card);
        });
        this.checkCards = Array.from(this.checkList.children);
    }
    
    stringToElement(str){
    	return  document.createRange().createContextualFragment(str);
    }

    
    moveCardAboutCode(card){
    	const OK = "ok";
        const SELENIUM_ERROR = "fail01";
        const NO_CAR_ERROR = "fail02";
        const TICKET_EXIST_ERROR = "fail03";
        const NOT_WORKING = "noting"
        const CHECK_TICKET = "check";

    	let cardData = {};
        
        let childNode = Array.from(card.children);
        
        let stateNode = childNode[4];

        let status = stateNode.innerHTML;
        if(status === TICKET_EXIST_ERROR) {
            stateNode.innerHTML = "주차권이 이미 존재";
            this.checkedList.prepend(card);
            return;
        } else if(status == CHECK_TICKET) {
        	stateNode.innerHTML = "주차 완료";
        	this.checkedList.prepend(card);
            return;
        } else if(status === SELENIUM_ERROR) {
            stateNode.innerHTML = "해당 티켓에 관한 시스템 에러가 발생";
        } else if(status === OK) {
            stateNode.innerHTML = "주차확인 필요";
            this.readyToCheckList.prepend(card);
            return;
        } else if(status === NO_CAR_ERROR) {
        	stateNode.innerHTML = "차가 아직 안 왔습니다.";
        } else if(status == NOT_WORKING) {
        	stateNode.innerHTML = "";
        }
        
        this.checkList.prepend(card);
    }
        
    addClickEventToGetNewTickectBtn() {
    	this.getTicketBtn.addEventListener("click", 
    			this.clickEventhandlerAboutGetNewTicket.bind(this));
    }
    
    clickEventhandlerAboutGetNewTicket() {
    	this.getNewTicket();
    }
        
    getNewTicket(){
    	let id = -200;
    	if(this.checkList.childElementCount > 0) {
    		let card = this.checkList.firstElementChild;
        	id = card.dataset.id;	
    	}
    	
    	
    	ajax({
            url : `/parking/api/newcars?id=${id}`,
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
            url : "/parking/api/register",
            method : "POST",
            contentType : "application/json; charset=utf-8",
        }, this.clickEventOfPushTicketSuccess.bind(this));
    }

    clickEventOfPushTicketSuccess(data) {
        this.checkList.innerHTML = "";
        this.makeCardOfCar(data);
    }
}
window.onload = () => {
    new App();
}