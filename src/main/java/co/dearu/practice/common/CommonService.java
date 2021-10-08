package co.dearu.practice.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입(패스워드 암호화 SHA-256)
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // 로그인(패스워드 확인)
    public boolean matches(CharSequence rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}


