package com.example.blog.service;

import com.example.blog.dto.MemberDto;
import com.example.blog.entity.Member;
import com.example.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public String register(MemberDto memberDto) {
        if(memberRepository.existsByUsername(memberDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        if(memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member();
        member.setUsername(memberDto.getUsername());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        member.setEmail(memberDto.getEmail());

        memberRepository.save(member);
        return null;
    }

    public String findUsernameByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            return member.get().getUsername();
        } else {
            return null;
        }
    }
    public boolean resetPassword(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String tempPassword = UUID.randomUUID().toString().substring(0, 8);
            member.setPassword(passwordEncoder.encode(tempPassword));
            memberRepository.save(member);

            // 사용자에게 임시 비밀번호 발송
            String subject = "임시 비밀번호 안내";
            String text = "임시 비밀번호는 " + tempPassword + " 입니다. 로그인 후 비밀번호를 변경하세요.";

            return true;
        }
        return false;
    }
}