/*
 *  XMLHttpRequest를 통해 서버와 통신 
 */
export default function ajax(request, callback) {
	const DONE = 4;
	const OK = 200;
	const NOT_FOUND = 404;
	const INTERNAL_SERVER_ERROR = 500;
	const BAD_REQUEST =400;
	const UNAUTHORIZED_ERROR = 401;
	
	if (typeof callback !== "function") {
		alert("callback(이/가) 함수가 아닙니다.");
		return;
	}

	let xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState === DONE) {
			let data = JSON.parse(this.responseText);
			if (xhttp.status === OK) {
				callback(data);
			} else if (xhttp.status === NOT_FOUND || xhttp.status === INTERNAL_SERVER_ERROR || xhttp.status === BAD_REQUEST){
				alert(data.message);
			} else if(xhttp.status === UNAUTHORIZED_ERROR) {
				alert(data.message);
			}
		}
	}
	
	if (typeof request.errorMessage === "string" && request.errorMessage.trim().length > 0) {
		xhttp.addEventListener("error", ()=> {
			alert(request.errorMessage);
		});
	}
	
	xhttp.open(request.method, request.url, true);
	if(request.contentType) {
		xhttp.setRequestHeader("Content-type", request.contentType);
	} 
	
	
	if(request.data){
		let data = request.data;
		if(request.contentType && request.contentType.indexOf("application/json") > -1) {
			xhttp.send(JSON.stringify(data));
		} else {
			xhttp.send(data);
		}
	} else {
		xhttp.send();
	}
}