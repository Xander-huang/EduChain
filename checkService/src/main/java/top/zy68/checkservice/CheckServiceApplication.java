package top.zy68.checkservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("top.zy68.*")
public class CheckServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckServiceApplication.class, args);
    }

}
