package com.example.blog.controller;

import com.example.blog.dto.MemberDto;
import com.example.blog.repository.MemberRepository;
import com.example.blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "signup_form";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @PostMapping("/signup")
    public String signupMember(@ModelAttribute MemberDto memberDto, Model model) {
        String result = memberService.register(memberDto);
        if (result == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", result);
            return "signup";
        }
    }

    @GetMapping("/find-username")
    public String findUsernameForm() {
        return "find_username_form";
    }

    @PostMapping("/find-username")
    public String findUsername(@RequestParam String email, Model model) {
        String username = memberService.findUsernameByEmail(email);
        if(username != null) {
            model.addAttribute("username", username);
            return "find_username_form";
        } else {
            model.addAttribute("errorMessage", "이 이메일에 해당하는 아이디가 없습니다.");
            return "find_username_form";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "reset_password_form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, Model model) {
        boolean success = memberService.resetPassword(email);
        if(success) {
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", "비밀번호 초기화에 실패했습니다.");
            return "reset_password_form";
        }
    }
}