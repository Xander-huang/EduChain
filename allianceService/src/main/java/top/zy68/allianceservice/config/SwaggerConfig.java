/*
package top.zy68.allianceservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


*/
/**
 * @ClassName SwaggerConfig
 * @Description SwaggerConfig
 * @Author Sustart
 * @Date2022/3/6 23:13
 * @Version 1.0
 **//*

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    */
/**
     * 配置swagger2
     *
     * @return Docket
     *//*

    @Bean
    public Docket docket() {
        Contact contact = new Contact("DevTeam", "https://baidu.com", "zymail68@foxmail.com");
        ApiInfo info = new ApiInfo(
                "FBTeachingService's API",
                "PersonService个人端接口",
                "V1.0",
                "urn:toVs",
                contact,
                "Apache 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(info)
                .groupName("PersonService")
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.zy68.personservice.controller"))
                .build();
    }
}
*/
