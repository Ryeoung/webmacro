
export class Tab{
    constructor(){
        this.tabLinks = document.getElementsByClassName("tabLink");
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
}