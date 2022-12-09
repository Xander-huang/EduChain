package top.zy68.personservice.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName AccountToken
 * @Description Shiro用户token
 * @Author Sustart
 * @Date2022/1/16 17:41
 * @Version 1.0
 **/
public class AccountToken implements AuthenticationToken {

    private final String token;

    /**
     * @param token token令牌
     */
    public AccountToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}