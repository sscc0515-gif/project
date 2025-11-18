package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;

import com.springboot.domain.Member;
import com.springboot.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/")
    public String welcome(Model model, Authentication authentication, HttpServletRequest httpServletRequest) {
        if (authentication == null) {
            return "welcome";
        }

        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();

        if (userId == null) {
            return "redirect:/login";
        }

        Member member = memberService.getMemberById(userId);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userLoginInfo", member);

        return "welcome";
    }
}
