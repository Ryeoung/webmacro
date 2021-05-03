const template = {
    cardTemplate :  `{{#each .}}
                                <div class = "card" data-Id="{{parkingInfoId}}">
                                        <div class="date">{{date}}</div>
                                        <div class="parkingLotName">{{parkingLotName}}</div>
                                        <div class="carNum">{{carNum}}</div>
                                        <div class="ticket">{{appTicketName}}</div>
                                        <div class="state">{{code}}</div>
                                        <div class="btnArea">
                                            <button class="ticketBtn ticketCheck">주차확인</button>
                                            <button class="ticketBtn ticketDelte">취소</button>
                                        </div>
                                    </div>
                            {{/each}}`
}
Object.freeze(template);

export default template;