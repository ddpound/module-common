package com.example.modulecommon.allstatic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Component
public class MyTokenProperties {

    @Value("${myToken.cookieJWTName}")
    private String JWT_COOKIE_NAME;

    @Value("${myToken.refreshJWTCookieName}")
    private String REFRESH_COOKIE_NAME;

    @Value("${myToken.userId}")
    private String JWT_COOKIE_ID;


}
