package com.shopme.admin.user;

import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword(){

        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

        String rawPassword="1234567890";
        String encodedPassword= passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);

        boolean matches=passwordEncoder.matches(rawPassword,encodedPassword);

        System.out.println(matches);
        assertThat(matches).isTrue();


    }



}
