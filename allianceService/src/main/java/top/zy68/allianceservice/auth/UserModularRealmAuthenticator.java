package top.zy68.allianceservice.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * @ClassName UserModularRealmAuthenticator
 * @Description TODO
 * @Author Sustart
 * @Date2022/4/19 21:26
 * @Version 1.0
 **/
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 通过supports()方法，匹配对应的Realm
        Realm uniqueRealm = null;
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                uniqueRealm = realm;
                break;
            }
        }
        if (uniqueRealm == null) {
            throw new UnsupportedTokenException();
        }
        return uniqueRealm.getAuthenticationInfo(token);
    }
}
