package com.example.modulecommon.jwtutil;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * 해당 클래스는 의존성 주입이 필요한 클래스
 * 컴포넌트로 IOC에 등록 해줘야함
 * */
@Log4j2
@Getter
@Component
public class JWTUtil {

    @Value("${googlelogin.googleClientId}")
    private String googleClientId;

    @Value("${googlelogin.googlekey}")
    private String googlekey;

    @Value("{myToken.userSecretKey}")
    private String userSecretKey;

    // @Value 는 정적 변수로는 담지 못함
    // 토큰 검증에 필요한 키
    @Value("${myToken.myKey}")
    private String myKey;

    @Value("${tokenVerifyTime.accesstimeSet}")
    private long AUTH_TIME;

    @Value("${tokenVerifyTime.refreshTimeSet}")
    private long REFRESH_TIME;

    @Value("${serverTokenKey}")
    private String serverTokenKey;

    @Value("${serverTokenTime}")
    private long serverTokenTime;

    /**
     * 토큰 제작 메소드
     *
     * */
    public String makeAuthToken(String username, int userid){
        log.info("now New make Token : " + username);
        return JWT.create()
                .withIssuer("nowAuction")
                .withClaim("userId", userid)
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(Algorithm.HMAC256(myKey+userid));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
    }

    public String makeAuthToken(int userid){
        log.info("now New make Token, userID : " + userid);
        return JWT.create()
                .withIssuer("nowAuction")
                .withClaim("userId", userid)
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(Algorithm.HMAC256(myKey+userid));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
    }

    /**
     * 유저네임을 넣은 Refresh  Token
     *
     * */
    public String makeRfreshToken(String username){
        log.info("now New make refresh Token : " + username);
        return JWT.create()
                .withIssuer("nowAuction")
                .withClaim("refresh","refresh")
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(Algorithm.HMAC256(myKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
        // 만료시간은 리프레쉬 토큰 시간에 맞춰서 넣는다
    }


    /**
     * 구글에서 성공적으로 받아온 토큰을 검증해주는 메소드
     * @param token 구글 로그인 성공 토큰을 넣어주세요
     * */
    public GoogleIdToken.Payload googleVerify(String token) throws GeneralSecurityException, IOException {

        if (googlekey ==null){
            log.info("googlekey null");
        }

        GoogleIdTokenVerifier googleIdTokenVerifier =
                new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(googleClientId))
                        .setIssuer("https://accounts.google.com")
                        .build();

        GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(token);

        // 값이 나오면 인증 성공
        // 설명이 조금 부실하긴함 인증 하지만 암호화 검증이 제대로 이루어졌는지에 대한 궁금증
        if(googleIdToken != null){
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            log.info("Verification google token success");
            //log.info(payload.getSubject());
            //log.info(payload.getEmail());
            String name = (String) payload.get("name");
            log.info(name);

            return payload;

        }else{
            log.info("JWTUtil google Token Verification failed");
            return null;
        }

    }


    /**
     * 키와 밸류 형식으로 한번에 해주기
     *  1이면 검증완료, -2 이면 만료된 토큰, -1 이면 검증실패, 그냥 토큰이 검증실패
     * */
    public Map<Integer, DecodedJWT> returnMapMyTokenVerify(String token){
        Map<Integer,DecodedJWT> returnMap = new HashMap<>();

        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(myKey)).build().verify(token);
            log.info("success myToken verify");
            returnMap.put(1, verify);
            return returnMap;
        }catch (TokenExpiredException e){
            log.info("The myToken has expired"); // 토큰 유효시간이 지남

            DecodedJWT decodeJWT = JWT.decode(token);
            returnMap.put(-2, decodeJWT);

            // 재발급이 필요, 리프레시 토큰이 있나 체크해야함
            return returnMap;
        }

        catch (Exception e){
            //e.printStackTrace();
            DecodedJWT decodeJWT = JWT.decode(token);

            log.info("myToken fail verify : " + decodeJWT);
            // 실패시
            returnMap.put(-1, decodeJWT);
            return returnMap;

        }
    }

    /**
     * 키와 밸류 형식으로 한번에 해주기
     *  1이면 검증완료, -2 이면 만료된 토큰, -1 이면 검증실패, 그냥 토큰이 검증실패
     * */
    public Map<Integer, DecodedJWT> returnMapMyTokenVerify(String token,int id){
        Map<Integer,DecodedJWT> returnMap = new HashMap<>();

        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(myKey+id)).build().verify(token);
            log.info("success myToken verify");
            returnMap.put(1, verify);
            return returnMap;
        }catch (TokenExpiredException e){
            log.info("The myToken has expired"); // 토큰 유효시간이 지남

            DecodedJWT decodeJWT = JWT.decode(token);
            returnMap.put(-2, decodeJWT);

            // 재발급이 필요, 리프레시 토큰이 있나 체크해야함
            return returnMap;
        }

        catch (Exception e){
            //e.printStackTrace();
            DecodedJWT decodeJWT = JWT.decode(token);

            log.info("myToken fail verify : " + decodeJWT);
            // 실패시
            returnMap.put(-1, decodeJWT);
            return returnMap;

        }
    }

    /**
     * 단순 토큰값을 디코드 해줌 필터가 이미 다 끝마쳤기 때문에
     *
     * */
    public DecodedJWT getTokenData(String token){
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(myKey)).build().verify(token);
            return verify;
        }catch (TokenExpiredException e){
            log.info("The myToken has expired"); // 토큰 유효시간이 지남
        }
        catch (Exception e){
            //e.printStackTrace();
            log.info("This myToken error");
            return null;

        }
        return null;
    }

    /**
     * 토큰 안에 담겨있는 정보를
     * 인증함과 동시에 반환해주는 기본적인 녀석
     * 만약 기한이 지났거나 검증이 실패할경우 반환값 null
     * */
    public DecodedJWT getTokenAndVerify(String token){
        try{
            return JWT.require(Algorithm.HMAC256(myKey)).build().verify(token);
        }catch (TokenExpiredException e){
            return null;
        }

    }


    public String makeServerAuthToken(String servername){
        log.info("new servertoken : " + servername);
        return JWT.create()
                .withSubject(servername)
                .withIssuer("nowAuction")
                .withClaim("username", servername) // 서버이름
                .withClaim("exp", Instant.now().getEpochSecond()+serverTokenTime)
                .sign(Algorithm.HMAC256(serverTokenKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
    }

    /** */
    public boolean serverJWTTokenVerify(String token){
        try {
            // 만약 아무 문제없이 잘된다면
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(serverTokenKey)).build().verify(token);
            return true;
        }catch (TokenExpiredException e){
            log.info("The server Token has expired"); // 토큰 유효시간이 지남
        }
        catch (Exception e){
            //e.printStackTrace();
            log.info("This serverToken error");
            return false;
        }
        return false;
    }


}
