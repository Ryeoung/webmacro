import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

class App {
    constructor() {
        const OK = "ok";
        const LOGIN_ERROR = "fail01";
        const NO_CAR_ERROR = "fail02";
        const TICKET_EXIST_ERROR = "fail03";

        this.checkList = document.getElementById("check");
        this.checkedList = document.getElementById("checked");
        this.pushTicketBtn = document.getElementById("pushTicket");
        this.checkCards = Array.from(this.checkList.children);
//        this.requestAllCarInfo();
        this.addClickEventToPushTicketBtn();

    }

    requestAllCarInfo() {
        ajax({
            url : "/parking/api/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, this.makeCardOfCar.bind(this));
        
    }

    makeCardOfCar(data) {
        let carInfos = data;
        let resultHTML = templateParser.getResultHTML(template.cardTemplate, carInfos);
        let cards = document.createRange().createContextualFragment(resultHTML);
        this.checkList.appendChild(cards);
        this.checkCards = Array.from(this.checkList.children);
    }
    
    addClickEventToPushTicketBtn(){
        this.pushTicketBtn.addEventListener("click", 
        this.clickEventHandlerAboutPushTicket.bind(this));
    
    }

    clickEventHandlerAboutPushTicket(event) {
        let reqData = [];
        this.checkCards.forEach(card => {
            let cardData = {};
            
            Array.from(card.children).forEach(divElmt => {
                let className = divElmt.className;
                if (className === "carNum") {
                    cardData.carNum = divElmt.innerText;
                } else if(className === "ticket") {
                    cardData.ticket = divElmt.innerText;
                } else if(className === "parkingLotName") {
                    cardData.parkingLotName = divElmt.innerText;
                }
            });
            reqData.push(cardData);
        });

        ajax({
            url : "/parking/api/register",
            method : "POST",
            data : reqData,
            contentType : "application/json; charset=utf-8",
        }, this.clickEventOfPushTicketSuccess.bind(this));
    }

    clickEventOfPushTicketSuccess(data) {
        this.checkList.innerHTML = "";
        let carInfos = data;
        let resultHTML = templateParser.getResultHTML(template.cardTemplate, carInfos);
        let cards = document.createRange().createContextualFragment(resultHTML);
        let div = document.createElement("div");
        div.appendChild(cards);

        Array.from(div.children).forEach(card => {
            let cardData = {};
            
            let childNode = Array.from(card.children);
            
            let stateNode = childNode[4];

            let status = stateNode.innerHTML;
            if(status === OK) {
                stateNode.innerHTML = "성공";
            } else if(status === LOGIN_ERROR) {
                stateNode.innerHTML = "로그인 에러 발생";
            } else if(status === TICKET_EXIST_ERROR) {
                stateNode.innerHTML = "이미 주차권이 있습니다.";
                this.checkedList.appendChild(card);
            }
        });
    }
}
window.onload = () => {
    new App();
}