package com.example.modulecommon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.modulecommon"})
public class ModuleCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleCommonApplication.class, args);
    }

}
