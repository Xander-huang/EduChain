package top.zy68.allianceservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Description JWT工具类：使用 用户账号 + 联盟点编号 和 salt 生成 指定 有效期的 token。
 * @Author Sustart
 * @Date2022/03/16 11:35
 * @Version 1.0
 **/
@Slf4j
public class JwtUtil {
    /**
     * 生成签名
     *
     * @param account    用户账号
     * @param salt       盐
     * @param expireTime 过期时间，单位s
     * @return 生成的token
     */
    public static String sign(String account, String salt, long expireTime) {
        Date expiredDate = new Date(System.currentTimeMillis() + expireTime * 1000);
        Date issuedDate = new Date();
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            return JWT.create()
                    .withClaim("account", account)
                    .withExpiresAt(expiredDate)
                    .withIssuedAt(issuedDate)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验token是否正确
     * token过期则不正常，返回false
     *
     * @param token   密钥
     * @param account 用户账号
     * @param salt    盐
     * @return 是否正确
     */
    public static boolean verify(String token, String account, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("account", account)
                    .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException | JWTVerificationException e) {
            log.info("Token verified error:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 获得token中的账户
     *
     * @param token token
     * @return 联盟点编号
     */
    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 无需salt获得token中包含的签发时间
     *
     * @param token token
     * @return token中包含的签发时间
     */
    public static Date getIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token是否过期
     *
     * @param token token
     * @return true：过期
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * 生成随机盐,长度32位
     *
     * @return 16进制的盐
     */
    public static String generateSalt() {
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(16).toHex();
    }
}
