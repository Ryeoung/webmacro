package com.macro.parking.enums;

public enum WebSite {
    IPARK("iparking"),
    IPTIME("iptime"),
    아미노("211.218.0.250");

    String url;

    WebSite(String url) {
        this.url = url;
    }

    public boolean isEqual(String url) {
        if(url.indexOf(this.url) > 0) {
            return true;
        }
        return false;
    }
}
