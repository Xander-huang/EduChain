package top.zy68.allianceservice.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import top.zy68.allianceservice.auth.*;

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
    public ShiroFilterFactoryBean shiroFilter(@Lazy DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

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
        chainDefinition.addPathDefinition("/getCaptchaImg", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/login", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/alliance/doAudit/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/alliance/register", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/sendEmailCaptcha/*", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/resetPwd", "noSessionCreation,anon");

        //permissive：当token无效时也允许请求访问，不会返回鉴权未通过的错误
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]");
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
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setAuthenticator(modularRealmAuthenticator());
        // 配置Realm
        manager.setRealms(Arrays.asList(injectAccountAuthRealm(), injectTokenAuthRealm()));
        // 关闭 Shiro session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
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
     * 系统自带的Realm管理，针对多realm 认证
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        //自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
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

    // 以下为注解支持
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
