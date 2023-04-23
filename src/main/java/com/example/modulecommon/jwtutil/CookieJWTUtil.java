package com.example.modulecommon.jwtutil;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Log4j2
@Getter
@Component
public class CookieJWTUtil {

    @Value("${myToken.cookieJWTName}")
    private String JWT_COOKIE_NAME;

    @Value("${myToken.refreshJWTCookieName}")
    private String REFRESH_COOKIE_NAME;

    @Value("${myToken.userId}")
    private String REFRESH_COOKIE_ID;


    public String listCookieGetString(String cookieName, Cookie[] cookies){



        return "";
    }


}
