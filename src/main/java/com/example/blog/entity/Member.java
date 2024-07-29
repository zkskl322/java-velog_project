package com.example.blog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

//    @Getter
//    @Setter
//    public class MemberForm {
//        @NotEmpty(message = "아이디는 필수항목입니다.")
//        private String username;
//
//        @NotEmpty(message = "비밀번호는 필수항목입니다.")
//        private String password;
//
//        @NotEmpty(message = "이메일은 필수항목입니다.")
//        @Email
//        private String email;
//    }
}