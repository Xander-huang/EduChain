package top.zy68.personservice.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.personservice.service.UserService;
import top.zy68.personservice.utils.JwtUtil;

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
        // 只要token是合法生成的，即使过期也可以得到personId
        String personId = JwtUtil.getPersonId(token);
        if (personId == null) {
            throw new AuthenticationException("token非法，请重新登录.");
        }
        PersonUser currUser = userService.queryUserByPersonId(personId);
        if (currUser == null) {
            throw new AuthenticationException("不存在该用户，请重新登录.");
        }
        String salt = userService.getTokenSaltByPersonId(personId);

        // JwtUtil.sign用personId和salt生成签名，这里再把这两个参数给Shiro做校验（也就是使用自定义的TokenCredentialsMatcher）
        return new SimpleAuthenticationInfo(currUser, salt, "tokenAuthRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }
}