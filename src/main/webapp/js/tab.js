
export class Tab{
    constructor(){
        this.tabLinks = document.getElementsByClassName("tabLink");
        this.checkCntElmt = this.tabLinks[1].children[0];
        this.checkedCntElmt = this.tabLinks[2].children[0];
        this.readyCntElmt = this.tabLinks[0].children[0];
        this.cancelCntElmt = this.tabLinks[3].children[0];
        
        this.addOpenTab();
        document.getElementById("defaultTab").click();
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
    
    updateTicketCnt(data) {
    	let cnt = -1;
    	Object.keys(data).forEach((key) => {
        	let targetElmt = null;
        	
    		if(key === "check") {
    			targetElmt = this.checkCntElmt; 
    			
    		} else if(key === "checked") {
    			targetElmt = this.checkedCntElmt;
    		} else if(key === "ready") {
    			targetElmt = this.readyCntElmt;
    		} else if(key == "cancel") {
    			targetElmt = this.cancelCntElmt;
      		}
    		
    		if(targetElmt != null) {
    			cnt = Number(targetElmt.innerHTML) + data[key];
    			targetElmt.innerHTML = cnt;
    		}
    		
    	});
    }
    
}