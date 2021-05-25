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

class App {
    constructor() {
    	this.tab = new Tab();
        this.ticket = new Ticket(this.tab);
        this.sort = new Sort();
                
        this.search = new Search(this.ticket);
        this.ticket.requestTickesOfToday();   
    }    
}
window.onload = () => {
    new App();
}