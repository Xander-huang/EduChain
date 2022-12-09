package top.zy68.personservice.auth;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;
import top.zy68.personservice.utils.JwtUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @ClassName JwtAuthFilter
 * @Description 登录认证过滤器
 * @Author Sustart
 * @Date2022/3/10 11:14
 * @Version 1.0
 **/
@Slf4j
public class JwtAuthFilter extends AuthenticatingFilter {
    /**
     * 刷新token时间
     */
    private static final int TOKEN_REFRESH_INTERVAL = 30000;

    // private UserService userService;
    //
    // /**
    //  * 构造方法
    //  */
    // public JwtAuthFilter() {
    // }
    //
    // /**
    //  * 需要在ShiroConfig中注入userService
    //  *
    //  * @param userService userService
    //  */
    // public JwtAuthFilter(UserService userService) {
    //     this.userService = userService;
    // }

    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。
     * 这个方法返回null的话会直接抛出异常，进入isAccessAllowed()的异常处理逻辑。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwtToken = httpServletRequest.getHeader("Authorization");
        try {
            if (jwtToken != null && !JwtUtil.isTokenExpired(jwtToken)) {
                return new AccountToken(jwtToken);
            }
        } catch (JWTDecodeException e) {
            log.error("token解码失败，token格式非法.");
        }
        return null;
    }

    /**
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。
     * 这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) { //not found any token
            log.error("Not found any token");
        } catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 如果这个Filter在之前isAccessAllowed()方法中返回false,则会进入这个方法。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        // 输出responseBean的Json格式
        PrintWriter out = httpResponse.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseBean responseBean = new ResponseBean(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), null);
        out.write(objectMapper.writeValueAsString(responseBean));
        out.flush();
        out.close();
        return false;
    }

    /**
     * 如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功.
     * 我们这里还判断了是否要刷新Token
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        return true;
        // 暂时不动态刷新token，一token用到底
        // HttpServletResponse httpResponse = WebUtils.toHttp(response);
        // String newToken = null;
        // if (token instanceof AccountToken) {
        //     AccountToken jwtToken = (AccountToken) token;
        //     PersonUser user = (PersonUser) subject.getPrincipal();
        //     boolean shouldRefresh = shouldTokenRefresh(Objects.requireNonNull(JwtUtil.getIssuedAt((String) jwtToken.getCredentials())));
        //     if (shouldRefresh) {
        //         newToken = userService.generateAccountToken(user.getIdNumber());
        //     }
        // }
        // if (newToken != null) {
        //     log.info("refreshed token {}", newToken);
        //     httpResponse.setHeader("Authorization", newToken);
        // }
    }

    /**
     * 判断是否需要刷新token
     *
     * @param issueAt Token生成时间
     * @return 是否刷新
     */
    private boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(TOKEN_REFRESH_INTERVAL).isAfter(issueTime);
    }

    /**
     * 如果调用shiro的login认证失败，会回调这个方法，这里什么都不做，留到onAccessDenied()中处理。
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.warn("token验证失败，Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }
}