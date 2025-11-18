package com.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    @Column(name = "num")
    private Long num;

    @Column(unique = true)
    private String memberId;

    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Transient
    private String id;


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setMemberId(memberFormDto.getMemberId());
        member.setName(memberFormDto.getName());
        member.setPhone(memberFormDto.getPhone());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setId(memberFormDto.getId());

        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        member.setRole(Role.USER);
        return member;
    }
}
