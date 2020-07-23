package io.github.fengyueqiao.marsnode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MarsNodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsNodeApplication.class, args);
    }

}
