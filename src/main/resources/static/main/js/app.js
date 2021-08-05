import {
    Search
} from "./search.js";

import {
    Tab
} from "./tab.js";

import {
    Sort
} from "./sort.js";

import {
    Ticket
} from "./ticket.js";

import ajax from "./ajax.js";
import templateParser from "./templateParser.js";

class App {
    constructor() {
    	this.tab = new Tab();
        this.ticket = new Ticket(this.tab, ajax, templateParser);
        this.sort = new Sort();
                
        this.search = new Search(this.ticket);
        this.ticket.requestTickesOfToday();
        this.addLoadSheepHtml();
    }

    addLoadSheepHtml() {
        let sheepContainer = document.getElementById("sheepContainer");
        let includePath = sheepContainer.dataset.includePath;
        if(includePath) {
            ajax({
                url : includePath,
                method : "GET",
                contentType : "text/html; charset=utf-8"
            }, (htmlString) => {
                let html = templateParser.stringToElement(htmlString);
                sheepContainer.append(html);
            });
        }

    }
}
window.onload = () => {
    new App();

}