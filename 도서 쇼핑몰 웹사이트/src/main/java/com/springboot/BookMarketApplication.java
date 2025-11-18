package com.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.domain.Member;
import com.springboot.domain.Role;
import com.springboot.service.MemberService;

@SpringBootApplication
public class BookMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMarketApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(MemberService memberService) {
        return args -> {
            if (memberService.getMemberById("Admin") == null) {
                Member member = new Member();
                member.setMemberId("Admin");
                member.setName("관리자");
                member.setPhone("");
                member.setEmail("");
                member.setAddress("");

                String password = new BCryptPasswordEncoder().encode("Admin1234");
                member.setPassword(password);
                member.setRole(Role.ADMIN);

                memberService.saveMember(member);
            }
        };
    }

}
