package top.zy68.allianceservice.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Lazy;
import top.zy68.allianceservice.service.UserService;
import top.zy68.allianceservice.util.ValidatorUtil;
import top.zy68.fbChainService.entity.NodeUser;

import javax.annotation.Resource;

/**
 * @ClassName AccountAuthRealm
 * @Description 通过账号密码的登录认证方式
 * @Author Sustart
 * @Date2022/3/29 10:58
 * @Version 1.0
 **/
public class AccountAuthRealm extends AuthorizingRealm {
    @Lazy
    @Resource
    UserService userService;

    /**
     * 保证Controller传入的token是继承自UsernamePasswordToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 登录认证
     * 根据token给的用户名，去数据库查出加密过用户密码，然后把加密后的密码和盐值一起发给shiro，让它做比对。
     *
     * @param token 来自LoginController/login接口的token
     * @return AuthenticationInfo
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userPasswordToken = (UsernamePasswordToken) token;
        // username 可能为账户account，可能为邮箱email
        String username = userPasswordToken.getUsername();
        NodeUser currUser;
        if (ValidatorUtil.isLegalAccount(username)) {
            currUser = userService.queryUserByAccount(username);
        } else if (ValidatorUtil.isLegalMail(username)) {
            currUser = userService.queryUserByMail(username);
            if (currUser == null) {
                throw new UnknownAccountException();
            }
        } else {
            throw new UnknownAccountException();
        }

        // 存在该用户，将密码和加密密码的盐交给shiro匹配（对应注册时DB保存加密过的密码）
        return new SimpleAuthenticationInfo(currUser, currUser.getPassword(), ByteSource.Util.bytes(currUser.getSalt()), "accountAuthRealm");
    }

    /**
     * 登录用户设置角色
     *
     * @param principalCollection 用户主要的信息集合，由上面认证方法进行认证时得到(SimpleAuthenticationInfo.user)
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        NodeUser user = (NodeUser) principalCollection.getPrimaryPrincipal();
        String role = user.getRole();
        simpleAuthorizationInfo.addRole(role);
        return simpleAuthorizationInfo;
    }
}