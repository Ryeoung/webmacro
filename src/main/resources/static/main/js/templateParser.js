/*
 * 탬플릿을 파싱해서 값을 집어 넣는 객체
 */
class TemplateParser {
	/*
	 * 바인딩할 데이터가 객체 하나일 경우 템플릿 작업을 통해 resultHTML을 반환
	 */
	getResultHTML(template, data){
		let bindTemplate = Handlebars.compile(template);
		let resultHTML = bindTemplate(data);
		return resultHTML.trim();
	}
	/*
	 * 바인딩할 데이터가 배열일 경우 템플릿 작업을 통해 resultHTML을 반환
	 * limit에 값이 들어 갈 경우 해당 갯수만큼만 작업을 진행하고 반환
	 */
	getResultHTMLOfList(template, data, limit) {
		let bindTemplate = Handlebars.compile(template);
		let resultHTML = "";
		
		data.every((item, index)=> {
			if(typeof limit === "number" && index === limit) {
				return false;
			}
			resultHTML += bindTemplate(item);
			return true;
		});
		return resultHTML;
	}
	/*
	 * string data -> HTML DOM
	 */
	stringToElement(str){
    	return  document.createRange().createContextualFragment(str);
    }
}
const templateParser = new TemplateParser();
export default templateParser;