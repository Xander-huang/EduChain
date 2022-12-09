package top.zy68.personservice;

import org.mapstruct.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName PersonServiceApplication
 * @Description 个人端启动类.
 * @Author Sustart
 * @Date2021/12/13 20:51
 * @Version 1.0
 **/
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan("top.zy68.*")
public class PersonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonServiceApplication.class, args);
    }

}
