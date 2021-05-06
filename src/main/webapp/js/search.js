import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

export class Search{
    constructor(ticket){
        this.searchInput = document.getElementById("searchInput");
        this.searchHistory = document.getElementById("searchHistory");
        this.searchContainer = document.getElementById("searchContainer");
        this.searchStopBtn = document.getElementById("searchStopBtn");
        this.searchBtn = document.getElementById("searchBtn");
        this.ticket = ticket;
        this.addSearchEvent();
        this.showWords();
        this.addKeydownEvent();
        this.addStopSearchEvent();

    }
    showWords() {
        let words = this.getWordArray();
        if(words===null) {
            return;
        }
        this.addWordElmts(words);
    }
    
    addWordElmts(words) {
        let data = [];
        words.forEach(word => {
            let wordData = {
                word: word
            };
            data.push(wordData);
        });
        let resultHTML = templateParser.getResultHTML(template.wordTemplate, data);
        let wordElmts = templateParser.stringToElement(resultHTML);
        wordElmts.childNodes.forEach(wordElmt => {
            this.showWord(wordElmt);
        });

    }
    showWord(wordElmt) {
        let wordCancelBtn = wordElmt.children[1];
        let searchWordElmt = wordElmt.children[0];
        this.addClickSearchWordEvent(searchWordElmt);
        this.addClickDeleteBtnEvent(wordCancelBtn);
        this.searchHistory.appendChild(wordElmt);
    }
    
    addClickSearchWordEvent(searchWordElmt){
        searchWordElmt.addEventListener("click", (event) => {
            this.searchInput.value = searchWordElmt.innerHTML;
        });
    }
    
    addClickDeleteBtnEvent(deleteBtn){
        deleteBtn.addEventListener("click", (event) => {

            let historyWord = event.currentTarget.parentElement;
            this.searchHistory.removeChild(historyWord);
            let wordText = historyWord.children[0].innerHTML;
            wordText += " ";
            this.deleteWord(wordText);
        });
    }

    deleteWord(word){
        let wordsStr = localStorage.getItem("words");
        wordsStr = wordsStr.replace(word, "");
        localStorage.setItem("words", wordsStr);
    }
    addKeydownEvent(){
        this.searchInput.addEventListener("keydown", (event) => {
            // Enter를 입력하면
            if(event.keyCode == 13) {
                this.searchBtn.click();
            }
        });
    }
    addSearchEvent(){
        this.searchBtn.addEventListener("click", () => {
            let wordData = this.searchInput.value;
            let wordsStr = localStorage.getItem("words");

            if(wordsStr === null) {
                wordsStr = "";
            }
            
            this.ticket.requestTicketsOfSearchWord(wordData);
        	this.searchStopBtn.style.display="block";

            if(wordsStr.indexOf(wordData) >= 0) {
            	return;
            }
            wordsStr += `${wordData} `;
            localStorage.setItem("words", wordsStr);

            let addData = [];
            addData.push(wordData);
            this.addWordElmts(addData);
            
        });
    }
    
    getWordArray(){
        let wordsStr = localStorage.getItem("words");
        if(wordsStr === null) {
            return null;
        }
        let words = wordsStr.split(" ");
        words.pop();
        return words;
    }
    
    addStopSearchEvent() {
    	this.searchStopBtn.addEventListener("click", () => {
    		this.searchInput.value = "";
    		this.ticket.deleteAllTicketList();
            this.ticket.requestTickesOfToday();   
    	});
    }
    
}