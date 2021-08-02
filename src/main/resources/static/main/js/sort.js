export class Sort {
    constructor() {
        this.titles = document.getElementsByClassName("title");
        this.addClickEventAboutSortByTitle();
        
    }
    addClickEventAboutSortByTitle(){
    	Array.from(this.titles).forEach(titleElmt => {
    		let title = titleElmt.classList[0];
    		if(title === "btnArea" || title === "state" || title === "ticket"){
    			return;
    		}
    		

    		titleElmt.addEventListener("click", (event) => {
        		let orderBy = titleElmt.dataset.orderBy;

        		let tabId = document.getElementsByClassName("tabActive")[0].dataset.tabId;
        		let sortList = document.getElementById(tabId);
        		let cards = Array.from(sortList.children);

        		cards.sort((c1,c2) => {
        			if(orderBy === "desc") {
        				titleElmt.dataset.orderBy = "asc";
        				return this.sortByTitleDesc(c1, c2, title);
        			} else {
        				titleElmt.dataset.orderBy = "desc";
        				return this.sortByTitle(c1, c2, title);
        			}
        			
        		});
        		sortList.innerHTML = "";
        		cards.forEach(card => {
        			sortList.append(card);
        		});
        	});
    	});
    }
    
    sortByTitle(c1, c2, title){
    	let fields1 = Array.from(c1.children);
    	let fields2 = Array.from(c2.children);
    	let field1, field2;
    	if(title === "date") {
    		field1 = fields1[0].innerHTML;
    		field2 = fields2[0].innerHTML;
    	} else if(title === "parkingLotName") {
    		field1 = fields1[1].innerHTML;
    		field2 = fields2[1].innerHTML;
    	} else if(title === "carNum") {
    		field1 = fields1[2].innerHTML;
    		field2 = fields2[2].innerHTML;
    	} 
    	
        return field1 < field2 ? -1 : field1 > field2 ? 1 : 0;
    }
    
    sortByTitleDesc(c1, c2, title){
    	let fields1 = Array.from(c1.children);
    	let fields2 = Array.from(c2.children);
    	let field1, field2;
    	if(title === "date") {
    		field1 = fields1[0].innerHTML;
    		field2 = fields2[0].innerHTML;
    	} else if(title === "parkingLotName") {
    		field1 = fields1[1].innerHTML;
    		field2 = fields2[1].innerHTML;
    	} else if(title === "carNum") {
    		field1 = fields1[2].innerHTML;
    		field2 = fields2[2].innerHTML;
    	} 
    	
        return field1 > field2 ? -1 : field1 < field2 ? 1 : 0;
    }
}