package com.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.springboot.domain.Member;
import com.springboot.domain.MemberFormDto;
import com.springboot.service.MemberService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/members")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value = "/add")
    public String requestAddMemberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/addMember";
    }

    @PostMapping(value = "/add")
    public String submitAddNewMember(@Valid MemberFormDto memberFormDto,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/addMember";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMember";
        }

        return "redirect:/members";
    }

    @GetMapping(value = "/update/{memberId}")
    public String requestUpdateMemberForm(@PathVariable(name = "memberId") String memberId,
                                          Model model) {
        Member member = memberService.getMemberById(memberId);
        model.addAttribute("memberFormDto", member);
        return "member/updateMember";
    }

    @PostMapping(value = "/update")
    public String submitUpdateMember(@Valid MemberFormDto memberFormDto,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/updateMember";
        }

        try {
            Member existingMember = memberService.getMemberById(memberFormDto.getMemberId());
            if (existingMember == null) {
                model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
                return "member/updateMember";
            }

            existingMember.setName(memberFormDto.getName());
            existingMember.setPhone(memberFormDto.getPhone());
            existingMember.setEmail(memberFormDto.getEmail());
            existingMember.setAddress(memberFormDto.getAddress());

            if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isBlank()) {
                existingMember.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
            }

            memberService.updateMember(existingMember); 

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/updateMember";
        }

        return "redirect:/members";
    }

    @GetMapping("/delete/{memberId}")
    public String deleteMember(@PathVariable(name = "memberId") String memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/logout";
    }

    @GetMapping
    public String requestMain() {
        return "redirect:/";
    }
}
