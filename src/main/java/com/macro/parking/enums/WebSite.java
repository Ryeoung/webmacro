package com.macro.parking.enums;

public enum WebSite {
    IPARK("iparking"),
    IPTIME("iptime"),
    아미노("211.218.0.250");

    String url;

    WebSite(String url) {
        this.url = url;
    }

    /**
     * @param url 입력받은 url
     * @return boolean
     *  입력받은 url(www.~.~.com)에 enum의 url이 들어가는 지 확인하여 동일하지 반환한다.
     */
    public boolean isEqual(String url) {
        if(url.indexOf(this.url) > 0) {
            return true;
        }
        return false;
    }
}
