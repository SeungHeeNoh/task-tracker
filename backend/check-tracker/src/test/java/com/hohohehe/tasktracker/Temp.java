package com.hohohehe.tasktracker;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Temp {

    @Test
    void name() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode("1234");
        System.out.println(pw);
    }
}
