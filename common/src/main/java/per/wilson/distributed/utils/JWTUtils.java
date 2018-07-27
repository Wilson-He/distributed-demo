package per.wilson.distributed.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.lang3.time.DateUtils;

import java.security.Key;
import java.util.*;

/**
 * JWTUtils
 *
 * @author Wilson
 * @date 18-7-19
 */
public class JWTUtils {
    public final static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    public final static Key key = MacProvider.generateKey(SignatureAlgorithm.HS512);
    public final static String STR_KEY = "abcdef";

    public final static Integer EXPIRED_TIME = 1000 * 60 * 30;

    public static String createJWT(String account) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(account)
                .setIssuedAt(now)
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setExpiration(Optional.ofNullable(EXPIRED_TIME)
                        .map(e -> DateUtils.addMilliseconds(now, EXPIRED_TIME))
                        .orElse(null))
                .signWith(signatureAlgorithm, STR_KEY)
                .compact();
    }

    public static String createToken(String username, String password) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("username", username);
        claims.put("password", password);
        return Jwts.builder()
                .signWith(signatureAlgorithm, STR_KEY)
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .addClaims(claims)
                .compact();
    }

    public static DecodedJWT parseJWT(String token) {
        return JWT.decode(token);
    }

    public static Jwt parseToJwt(String token) {
        return Jwts.parser()
                .setSigningKey(STR_KEY)
                .parse(token);
    }

    public static DefaultClaims parseToBody(String token) {
        return (DefaultClaims) Jwts.parser()
                .setSigningKey(STR_KEY)
                .parse(token)
                .getBody();
    }

    public static <T> T getClaim(String token, String key, Class<T> valClass) {
        return JWT.decode(token)
                .getClaim(key)
                .as(valClass);
    }

    public static void main(String[] args) {
        List<String> roles = Lists.newArrayList("role");
        List<String> permissions = Lists.newArrayList("permission");
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("username", "wilson");
        claims.put("password", "123456");
        claims.put("roles", roles);
        claims.put("permissions", permissions);
        Date past = DateUtils.addDays(new Date(), -1);
        String pastToken = Jwts.builder()
                .setSubject("321321")
                .addClaims(claims)
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .signWith(signatureAlgorithm,STR_KEY)
                .setExpiration(past)
                .compact();
        System.out.println("pastToken:" + pastToken);
        System.out.println("-----------");
        System.out.println(parseToJwt(pastToken));



    }
}
