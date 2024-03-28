package com.zb.deuggeun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DeugGeunApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeugGeunApplication.class, args);
  }

}
