package com.example.oauth2.service;

import com.example.oauth2.entity.UserEntity;
import com.example.oauth2.exception.RecordNotFoundException;
import com.example.oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;



    public void checkUserName(String username, String password) {
     UserEntity user=userRepository.findByEmail(username).orElseThrow(()->{
         throw new RecordNotFoundException("username or password incorrect");
     });
        boolean isSuccess = passwordEncoder.matches(password, user.getPassword());
        if (!isSuccess){
            throw new RecordNotFoundException("username or password incorrect");
        }
        setAuthentication(username);
    }


    private void setAuthentication(String email) {
        Authentication authentication=
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("OIDC_USER"))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void processOAuthPostLogin(DefaultOAuth2User principal) {
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");
        Optional<UserEntity> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            UserEntity user = UserEntity.of(name, email);
            String userPassword = UUID.randomUUID().toString().substring(0, 5);
           emailService.sendMessage(email,userPassword);
            user.setPassword(passwordEncoder.encode(userPassword));
            userRepository.save(user);

        }
    }

}
