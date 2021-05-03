import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

class App {
    constructor() {
        this.checkList = document.getElementById("check");
        this.checkedList = document.getElementById("checked");
        this.readyToCheckList = document.getElementById("ready");
        this.cancelList = document.getElementById("cancel");
        
        this.pushTicketBtn = document.getElementById("pushTicket");
        this.getTicketBtn = document.getElementById("getTicket");
        this.tabLinks = document.getElementsByClassName("tabLink");
        this.addOpenTab();
        document.getElementById("defaultTab").click();
        this.checkCards = Array.from(this.checkList.children);
        this.requestAllCarInfoToday();
        this.addClickEventToGetNewTickectBtn();
        this.addClickEventToPushTicketBtn();

    }
    addOpenTab() {
    	Array.from(this.tabLinks).forEach(tabLink => {
    		tabLink.addEventListener("click", (event) => {
				let tabLink = event.currentTarget;
				let tabId = tabLink.dataset.tabId;

				let tabcontent = document.getElementsByClassName("tabContent");
				for (let i = 0; i < tabcontent.length; i++) {
				    tabcontent[i].style.display = "none";
				}
				
				let tablinks = this.tabLinks;
				for (let i = 0; i < tablinks.length; i++) {
				    tablinks[i].className = tablinks[i].className.replace(" tabActive", "");
				}
				
				document.getElementById(tabId).style.display = "flex";
				tabLink.className += " tabActive";
				
    		});
    	});
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
            this.checkedList.prepend(card);
            return;
        } else if(status == CHECK_TICKET) {
        	stateNode.innerHTML = "주차 완료";
        	this.hideBtn(checkBtn, deleteBtn);
        	this.checkedList.prepend(card);
            return;
        } else if(status === SELENIUM_ERROR) {
            stateNode.innerHTML = "해당 티켓에 관한 시스템 에러가 발생";
        } else if(status === OK) {
            stateNode.innerHTML = "주차확인 필요";
            this.hideBtn(deleteBtn);
            this.readyToCheckList.prepend(card);
            return;
        } else if(status === NO_CAR_ERROR) {
        	stateNode.innerHTML = "차가 아직 안 왔습니다.";
        } else if(status == NOT_WORKING) {
        	stateNode.innerHTML = "";
        } else if(status == CANCEL) {
        	stateNode.innerHTML = "취소";
            this.hideBtn(deleteBtn, checkBtn);
            this.cancelList.prepend(card);
            return;
        }
        
        this.hideBtn(checkBtn);
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
    
    	card.parentNode.removeChild(card);
    	let data =changed;
    	ajax({
            url : `/parking/api/ticket/${parkingInfoId}`,
            method : "PUT",
            contentType : "application/json; charset=utf-8",
            data : data
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