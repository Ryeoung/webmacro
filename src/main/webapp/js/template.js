const template = {
    categoryListTemplate : `{{#each .}}
                                <div class = "card">
                                    {{date}}
                                    {{carNum}}
                                    {{parkingLot}}
                                    {{ticket}}
                                </div>
                            {{/each}}`
}
Object.freeze(template);

export default template;