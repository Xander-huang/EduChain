package top.zy68.allianceservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfig
 * @Description 跨域
 * @Author Sustart
 * @Date2022/3/15 12:52
 * @Version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 项目中的所有接口都支持跨域
        registry.addMapping("/**")
                // 所有地址都可以访问/具体地址
                .allowedOrigins("*")
                // "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600)
                // 支持的域
                .allowedOrigins("*");
    }
}
