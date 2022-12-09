package top.zy68.allianceservice.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;
import top.zy68.allianceservice.service.UserService;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.fbChainService.entity.NodeUser;

import javax.annotation.Resource;

/**
 * @ClassName TokenAuthRealm
 * @Description 通过Token验证的登录认证方式
 * @Author Sustart
 * @Date2022/3/10 11:29
 * @Version 1.0
 **/
public class TokenAuthRealm extends AuthorizingRealm {
    @Lazy
    @Resource
    UserService userService;

    /**
     * 限定这个Realm只支持我们自定义的JWT Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AccountToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        AccountToken accountToken = (AccountToken) authenticationToken;
        String token = (String) accountToken.getPrincipal();

        // Token非法：只要token是合法生成的，即使过期也可以得到account
        String account = JwtUtil.getAccount(token);
        if (account == null) {
            throw new IncorrectCredentialsException("token非法，请重新登录.");
        }

        // 不存在Token内的account用户
        NodeUser currUser = userService.queryUserByAccount(account);
        if (currUser == null) {
            throw new ExpiredCredentialsException("Token已过期，请重新登录.");
        }

        // 登录的salt不存在
        String salt = userService.getTokenSaltByAccount(account);
        if (salt == null) {
            throw new ExpiredCredentialsException("登录状态已超时，请重新登录.");
        }

        // JwtUtil.sign用account和salt生成签名，这里再把这两个参数给Shiro做校验（也就是使用自定义的TokenCredentialsMatcher）
        return new SimpleAuthenticationInfo(currUser, salt, "tokenAuthRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        NodeUser user = (NodeUser) principalCollection.getPrimaryPrincipal();
        String role = user.getRole();
        simpleAuthorizationInfo.addRole(role);
        return simpleAuthorizationInfo;
    }
}