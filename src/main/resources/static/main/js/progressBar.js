

export class ProgressBar {
    constructor(template, templateParser) {
        this.modal = document.getElementsByClassName("modal")[0];
        this.closeBtn = document.getElementsByClassName("modalCloseBtn")[0];
        this.labels =document.querySelectorAll(".totalProgressLabel");
        this.alertMessageBar = document.getElementById("alertMessageContainer");
        this.template = template;
        this.templateParser = templateParser;


        this.totalProgressBar = document.getElementsByClassName("totalProgress")[0];
        this.addClickEventAboutCloseBtn();
        this.addClickEventAboutAlertMessage();

        this.willPushParkingLotObject = {}
        this.willPushParkingLotKeys = [];
        this.totalTicketCnt = 0;

        this.nextPushTicketIdx = 0;
    }

    popupProgressModal() {
        this.modal.style.display = "block";

    }

    addClickEventAboutCloseBtn() {
        this.closeBtn.addEventListener("click", () => {
            this.modal.style.display = "none";
            if(!this.isFinishOfProgress()) {
                this.alertMessageBar.style.display = "flex";
            } else {
                this.alertMessageBar.style.display = "none";
            }
        });
    }

    addClickEventAboutAlertMessage(){
        this.alertMessageBar.addEventListener("click", () => {
            this.alertMessageBar.style.display = "none";
            this.popupProgressModal();
        });
    }

    isFinishOfProgress() {
        if(this.nextPushTicketIdx >= this.willPushParkingLotKeys.length) {
            return true;
        }
    }

    setParkingLotObject (parkingLotDict = {}){
        this.willPushParkingLotObject = parkingLotDict;
        this.willPushParkingLotKeys = Object.keys(parkingLotDict);
        this.totalTicketCnt = this.getTotalTicketCnt(parkingLotDict);
    }

    getTotalTicketCnt(parkingLotDict = {}) {
        let totalTicketCnt = 0;
        for(const key in parkingLotDict) {
            totalTicketCnt  += parkingLotDict[key].count;
        }
        return totalTicketCnt;
    }

    getNextPushParkingLot() {
        if(this.isFinishOfProgress()) {
            return null;
        }
        let key = this.willPushParkingLotKeys[this.nextPushTicketIdx++];
        return this.willPushParkingLotObject[key];
    }


    changeLabelText(comment = "") {
        this.labels.forEach(label => {
            label.textContent = comment;
        });
    }
}