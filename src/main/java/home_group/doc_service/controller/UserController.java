package home_group.doc_service.controller;

import home_group.doc_service.dto.Login;
import home_group.doc_service.dto.Register;
import home_group.doc_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> regiser(@RequestBody Register register) {
        return ResponseEntity.ok(authService.register(register));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        return ResponseEntity.ok(authService.login(login));
    }
}
