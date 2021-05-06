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
        this.ticket = new Ticket();
        this.sort = new Sort();
        this.tab = new Tab();
        this.search = new Search(this.ticket);
        this.ticket.requestTickesOfToday();   
    }    
}
window.onload = () => {
    new App();
}