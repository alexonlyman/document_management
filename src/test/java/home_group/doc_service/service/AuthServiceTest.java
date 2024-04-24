package home_group.doc_service.service;

import home_group.doc_service.dto.Login;
import home_group.doc_service.dto.Register;
import home_group.doc_service.dto.Role;
import home_group.doc_service.entity.DocSignature;
import home_group.doc_service.entity.Document;
import home_group.doc_service.entity.User;
import home_group.doc_service.repository.UserRepo;
import home_group.doc_service.utils.AuthenticationResponse;
import home_group.doc_service.utils.JwtTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    UserRepo userRepo;
    @Mock
    AuthenticationManager manager;
    @Mock
    Authentication authentication;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    JwtTokenService jwtTokenService;
    @InjectMocks
    AuthService authService;
    User user = new User();
    List<Document> documentList = new ArrayList<>();
    DocSignature docSignature = new DocSignature();
    Register register = new Register("aasd@mail.ru","password", Role.USER);
    Login login = new Login("aasd@mail.ru","password");
    @Test
    void register() {
        user.setDocuments(documentList);
        user.setId(1);
        user.setRole();
        user.setPassword(register.getPassword());
        user.setEmail(register.getEmail());

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(jwtTokenService.generateToken(user)).thenReturn("testToken");

        AuthenticationResponse response = authService.register(register);

        Assertions.assertNotNull(register);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("encodedPassword",passwordEncoder.encode(anyString()));
        Assertions.assertEquals("testToken", response.getToken());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepo, times(1)).save(any(User.class));
        verify(jwtTokenService, times(1)).generateToken(any(User.class));
    }

    @Test
    void login() {
        when(userRepo.findUserByEmail(login.getEmail())).thenReturn(user);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenService.generateToken(any(User.class))).thenReturn("testToken");

        AuthenticationResponse response = authService.login(login);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals("aasd@mail.ru",login.getEmail());
        Assertions.assertEquals("testToken",response.getToken());

        verify(userRepo, times(1)).findUserByEmail(login.getEmail());
        verify(jwtTokenService, times(1)).generateToken(any(User.class));
    }
}