package top.zy68.personservice.config;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.zy68.personservice.auth.AccountAuthRealm;
import top.zy68.personservice.auth.JwtAuthFilter;
import top.zy68.personservice.auth.TokenAuthRealm;
import top.zy68.personservice.auth.TokenCredentialsMatcher;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Map;

/**
 * 向Spring中注入Shiro相关的bean
 *
 * @ClassName ShiroConfig
 * @Description Shiro配置
 * @Author Sustart
 * @Date2022/1/16 17:06
 * @Version 1.0
 **/
@Configuration
public class ShiroConfig {

    /**
     * 设置过滤器，将自定义的Filter加入
     *
     * @return ShiroFilterFactoryBean shiro过滤器工厂bean，用与配置 securityManager、过滤器等
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(injectSecurityManager());

        // 配置两个filter，分别为：登录认证filter和角色授权filter
        Map<String, Filter> filterMap = factoryBean.getFilters();
        // JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(userService);
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter();
        jwtAuthFilter.setLoginUrl("/login");
        filterMap.put("authcToken", jwtAuthFilter);
        factoryBean.setFilters(filterMap);

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        // swagger-ui开放
        chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
        chainDefinition.addPathDefinition("/webjars/**", "anon");
        chainDefinition.addPathDefinition("/v2/**", "anon");
        chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
        // 业务接口开放
        // noSessionCreation：用户在操作session时会抛异常
        chainDefinition.addPathDefinition("/register", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/getCaptchaImg", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/login", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/sendEmailCaptcha/*", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/resetPwd", "noSessionCreation,anon");
        //permissive：当token无效时也允许请求访问，不会返回鉴权未通过的错误
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/authorize", "noSessionCreation,authcToken[permissive]");
        // 默认进行用户鉴权
        chainDefinition.addPathDefinition("/**", "noSessionCreation,authcToken");
        factoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());

        return factoryBean;
    }

    /**
     * 注入 securityManager
     *
     * @return DefaultWebSecurityManager
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager injectSecurityManager() {
        // 配置自定义的 Realm 认证器
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setAuthenticator(authenticator());

        // 关闭 Shiro session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    /**
     * 初始化Authenticator
     *
     * @return 含realm认证授权策略的信息
     */
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 设置两个Realm，一个用于jwt token的认证；一个用于用户登录验证和访问权限获取
        authenticator.setRealms(Arrays.asList(injectAccountAuthRealm(), injectTokenAuthRealm()));
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 注入AccountAuthRealm，用于在客户端通过账户登录时校验
     *
     * @return Realm
     */
    @Bean("accountAuthRealm")
    public AccountAuthRealm injectAccountAuthRealm() {
        AccountAuthRealm accountAuthRealm = new AccountAuthRealm();
        // 配置校验密码的匹配器
        accountAuthRealm.setCredentialsMatcher(new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME));
        return accountAuthRealm;
    }

    /**
     * 注入TokenAuthRealm，用于校验客户端请求接口时附带的token
     *
     * @return Realm
     */
    @Bean("tokenAuthRealm")
    public TokenAuthRealm injectTokenAuthRealm() {
        TokenAuthRealm tokenAuthRealm = new TokenAuthRealm();
        // 配置自定义校验token的匹配器
        tokenAuthRealm.setCredentialsMatcher(new TokenCredentialsMatcher());
        return tokenAuthRealm;
    }
}
