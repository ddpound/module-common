package com.example.modulecommon.jwtutil;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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

    public String requestListCookieGetString(String cookieName, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies
        ) {
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * 문자열을 쿠키 형식으로 전달해줌
     * ex) "cookieName = cookieValue"
     * */
    public String feignClientReturnCookie(String cookieName, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies
        ) {
            if(cookie.getName().equals(cookieName)){
                return  JWT_COOKIE_NAME+"="+cookie.getValue();
            }
        }

        return null;
    }


    public String listCookieGetString(String cookieName, Cookie[] cookies){

        for (Cookie cookie : cookies
             ) {
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }

        return null;
    }


}
