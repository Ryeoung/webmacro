import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

class App {
    constructor() {
        this.cardList = document.getElementsByClassName("cardList");
        this.requestAllCarInfo();
    }
    requestAllCarInfo() {
        ajax({
            url : "https://localhost:8080/api/cars",
            method : "GET",
            contentType : "application/json; charset=utf-8",
        }, this.makeCardOfCar.bind(this));
        
    }

    makeCardOfCar(data) {
        let carInfos = data;
        let resultHTML = templateParser.getResultHTML(template.categoryListTemplate, categories);
        let cards = document.createRange().createContextualFragment(resultHTML);
        this.cardList.appendChild(cards);


    }
    getCategoriesSuccess(data) {
		let categories = data.categories;
		let resultHTML = templateParser.getResultHTML(template.categoryListTemplate, categories);

		let categoryTabElmtsDocumentFragments =  document.createRange().createContextualFragment(resultHTML);
		this.categoryContainerElmts.appendChild(categoryTabElmtsDocumentFragments);
		this.categoryTabElmts =  document.querySelectorAll("[data-category]");
		this.addTabEventListener();
	}
	
	/* 
	 *  탭 클릭 이벤트 리스너
	 */
	addTabEventListener() {
		this.categoryTabElmts.forEach(tab => {
			tab.addEventListener("click", this.tabEventHandler.bind(this));
		});
	}
}
window.onload = () => {
    new App();
}