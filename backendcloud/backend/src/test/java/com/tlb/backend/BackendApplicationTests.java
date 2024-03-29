package com.tlb.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.matches("123456","$2a$10$A.My.0vE81sCvrTifUps0uUNdJ6UU0VwVd1Zt/HXDEH7kGH3bWROC"));
        System.out.println(passwordEncoder.encode("123456"));
    }

}
