const template = {
    categoryListTemplate : `{{#each .}}
						    	<div class = "card">
						                <div class="date">{{date}}</div>
						                <div class="parkingLotName">{{parkingLotName}}</div>
						                <div class="carNum">{{carNum}}</div>
						                <div class="ticket">{{ticket}}</div>
						                <div class="state">{{message}}</div>
						                <button class="ticketCheck">주차확인</button>
						            </div>
                            {{/each}}`
}
Object.freeze(template);

export default template;