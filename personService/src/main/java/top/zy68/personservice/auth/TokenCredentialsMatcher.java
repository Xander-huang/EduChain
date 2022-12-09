package top.zy68.personservice.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import top.zy68.personservice.utils.JwtUtil;

/**
 * @ClassName TokenCredentialsMatcher
 * @Description 用户通过token登录时，验证token合法性
 * @Author Sustart
 * @Date2022/3/10 11:30
 * @Version 1.0
 **/

public class TokenCredentialsMatcher implements CredentialsMatcher {
    /**
     * Matcher中直接调用JwtUtil工具包中的verify方法。token合法则登录认证通过。
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String token = (String) authenticationToken.getCredentials();
        String salt = authenticationInfo.getCredentials().toString();
        String personId = JwtUtil.getPersonId(token);
        return JwtUtil.verify(token, personId, salt);
    }
}
