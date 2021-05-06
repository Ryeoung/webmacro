import ajax from "./ajax.js";

import template from "./template.js";

import templateParser from "./templateParser.js";

export class Search{
    constructor(){
        this.searchInput = document.getElementById("searchInput");
        this.searchHistory = document.getElementById("searchHistory");
        this.searchContainer = document.getElementById("searchContainer");
        this.searchBtn = document.getElementById("searchBtn");
        this.showWords();
        this.addKeydownEvent();
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
            // let idx = words.indexOf(wordText);
            // if(idx >= 0 ){
            //    words.splice(idx, 1);
            // }
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
                let wordData = this.searchInput.value;
                let wordsStr = localStorage.getItem("words");

                if(wordsStr === null) {
                    wordsStr = "";
                }

                wordsStr += `${wordData} `;
                localStorage.setItem("words", wordsStr);

                let addData = [];
                addData.push(wordData);
                this.addWordElmts(addData);
            }
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
    
}