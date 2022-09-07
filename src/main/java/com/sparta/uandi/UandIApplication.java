package com.sparta.uandi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스프링 부트에서 스케줄러가 작동
@EnableJpaAuditing
@SpringBootApplication
public class UandIApplication {

    public static void main(String[] args) {
        SpringApplication.run(UandIApplication.class, args);
    }

}
