package top.zy68.allianceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName AllianceServiceApplication
 * @Description 联盟端启动类.
 * @Author Sustart
 * @Date2022/02/16 20:51
 * @Version 1.0
 **/
@SpringBootApplication
@ComponentScan("top.zy68.*")
public class AllianceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllianceServiceApplication.class, args);
    }

}
