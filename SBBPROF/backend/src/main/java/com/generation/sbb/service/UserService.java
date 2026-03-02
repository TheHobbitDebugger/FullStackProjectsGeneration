package com.generation.sbb.service;

import com.generation.sbb.dto.LoginDTO;
import com.generation.sbb.dto.TokenDTO;
import com.generation.sbb.dto.UserDTO;
import com.generation.sbb.mapper.UserMapper;
import com.generation.sbb.model.User;
import com.generation.sbb.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.generation.sbb.security.JwtService;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    private JwtService jwtService;


    public List<UserDTO> findAll() {
        return userMapper.toDTOs(userRepository.findAll());
    }

    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    public UserDTO save(@Valid UserDTO userDTO) {
        
        userDTO.setPassword(passwordHasher.toHash(userDTO.getPassword()));
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public TokenDTO login(LoginDTO loginDTO) {

        // cerco un utente con lo stesso user name. Se non c'è, restituisco eccezione
        // e si tradurrà in 403 Forbidden
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // verifico: le password corrispondono? Se non corrispondono, stessa storia
        // cambia il messaggio di errore                
        if (!user.getPassword().equals(passwordHasher.toHash(loginDTO.getPassword()))) {
            throw new RuntimeException("Invalid password");
        }

        // jwtService che è un servizio che non tocca il db, genera il token a partire dallo user
        // e lo rimanda all'utente sotto forma di TokenDTO
        // il token viene inviato al client, il client ce lo rimanderà
        return new TokenDTO(jwtService.generateToken(user));
    }
}