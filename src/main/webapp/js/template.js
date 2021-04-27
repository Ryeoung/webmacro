const template = {
    cardTemplate :  `{{#each .}}
                                <div class = "card" data-Id="{{parkingTicketId}}">
                                        <div class="date">{{date}}</div>
                                        <div class="parkingLotName">{{parkingLotName}}</div>
                                        <div class="carNum">{{carNum}}</div>
                                        <div class="ticket">{{appTicketName}}</div>
                                        <div class="state">{{code}}</div>
                                        <div class="btnArea">
                                            <button class="ticketCheck">주차확인</button>
                                        </div>
                                    </div>
                            {{/each}}`
}
Object.freeze(template);

export default template;