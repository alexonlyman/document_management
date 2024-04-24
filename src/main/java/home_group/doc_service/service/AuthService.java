package home_group.doc_service.service;

import home_group.doc_service.dto.Login;
import home_group.doc_service.dto.Register;
import home_group.doc_service.dto.Role;
import home_group.doc_service.entity.User;
import home_group.doc_service.exeptions.EmailAllreadyExist;
import home_group.doc_service.repository.UserRepo;
import home_group.doc_service.utils.AuthenticationResponse;
import home_group.doc_service.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService  {

    private final UserRepo userRepo;
    private final JwtTokenService jwtTokenService;
    Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;



    public AuthenticationResponse register (Register register) {
        try {
            User newUser = new User();
            logger.info("userd data " + register);
            newUser.setRole(Role.USER);
            if (userRepo.existsByEmail(register.getEmail())) {
                throw new EmailAllreadyExist("email used");
            }
            newUser.setEmail(register.getEmail());

            newUser.setPassword(passwordEncoder.encode(register.getPassword()));
            User user = userRepo.save(newUser);
            String token = jwtTokenService.generateToken(user);
            return AuthenticationResponse.builder().
                    token(token)
                    .build();

        } catch (DataIntegrityViolationException ex) {
            throw new EmailAllreadyExist("email used");
        }

    }

    public AuthenticationResponse login(Login login) {
        logger.info("login data " + login);
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
        );
        User user = userRepo.findUserByEmail(login.getEmail());
        logger.info("email data " + login.getEmail());
        String token = jwtTokenService.generateToken(user);
        return AuthenticationResponse.builder().
                token(token)
                .build();

    }


}
