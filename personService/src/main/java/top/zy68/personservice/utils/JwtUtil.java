package top.zy68.personservice.utils;

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
 * @Description JWT工具类：使用 PersonUser 用的 personId 身份证号和 secret 密码生成 5 min 有效期的 token。
 * @Author Sustart
 * @Date2022/01/08 20:35
 * @Version 1.0
 **/
@Slf4j
public class JwtUtil {
    /**
     * 通过 personId + salt 通过算法哈希生成token
     *
     * @param personId   用户身份证号
     * @param salt       盐
     * @param expireTime 过期时间，单位s
     * @return 生成的token
     */
    public static String sign(String personId, String salt, long expireTime) {
        // 过期时间
        Date expiredDate = new Date(System.currentTimeMillis() + expireTime * 1000);
        // 签发时间
        Date issuedDate = new Date();
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            // 附带personId信息，附加的key-value越多，token越长
            return JWT.create()
                    .withClaim("personId", personId)
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
     * @param token    密钥
     * @param personId 用户名
     * @param salt     盐
     * @return 是否正确
     */
    public static boolean verify(String token, String personId, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("personId", personId)
                    .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException | JWTVerificationException e) {
            log.warn("Token verified error:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 无需salt获得token中的负载信息（personId身份证号）
     * token过期后仍然可以获取
     *
     * @param token token
     * @return 用户身份证号
     */
    public static String getPersonId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("personId").asString();
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
