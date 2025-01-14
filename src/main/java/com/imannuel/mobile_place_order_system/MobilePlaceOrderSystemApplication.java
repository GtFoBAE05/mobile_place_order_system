package com.imannuel.mobile_place_order_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MobilePlaceOrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobilePlaceOrderSystemApplication.class, args);
    }

}
