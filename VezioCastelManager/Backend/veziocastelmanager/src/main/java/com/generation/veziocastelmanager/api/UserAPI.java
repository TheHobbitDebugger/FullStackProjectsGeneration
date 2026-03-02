package com.generation.veziocastelmanager.api;

import com.generation.veziocastelmanager.dto.LoginDTO;
import com.generation.veziocastelmanager.dto.TokenDTO;
import com.generation.veziocastelmanager.dto.UserDTO;
import com.generation.veziocastelmanager.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vcm/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserAPI {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDTO dto) {
        try {
            dto = service.register(dto);
            return ResponseEntity.status(201).body(dto);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) {
        return service.login(loginDTO);
    }
}
