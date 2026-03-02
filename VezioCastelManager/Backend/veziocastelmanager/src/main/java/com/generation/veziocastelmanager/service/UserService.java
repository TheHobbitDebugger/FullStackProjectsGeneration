package com.generation.veziocastelmanager.service;

import com.generation.veziocastelmanager.dto.LoginDTO;
import com.generation.veziocastelmanager.dto.TokenDTO;
import com.generation.veziocastelmanager.dto.UserDTO;
import com.generation.veziocastelmanager.mapper.UserMapper;
import com.generation.veziocastelmanager.model.entities.User;
import com.generation.veziocastelmanager.model.repository.UserRepository;
import com.generation.veziocastelmanager.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private UserMapper      userMapper;

    @Autowired
    private PasswordHasher  passwordHasher;

    @Autowired
    private JwtService      jwtService;

    // usato da GET /vcm/api/users
    // prendo tutti gli utenti dal db e li converto in DTO prima di restituirli
    public List<UserDTO> findAll()
    {
        return userMapper.toDTOs(userRepository.findAll());
    }

    // usato da GET /vcm/api/users/{id}
    // cerco l'utente per id, se non esiste lancio un'eccezione che Spring traduce in 404
    public UserDTO findById(int id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    // usato da POST /vcm/api/users e PUT /vcm/api/users/{id}
    // prima di salvare faccio l'hash della password, non la salvo mai in chiaro nel db
    // il mapper converte il DTO in entità, poi salvo e riconverto in DTO per la risposta
    public UserDTO save(UserDTO userDTO)
    {
        userDTO.setPassword(passwordHasher.toHash(userDTO.getPassword()));
        User user = userMapper.toEntity(userDTO);
        user      = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    // usato da DELETE /vcm/api/users/{id}
    public void deleteById(int id)
    {
        userRepository.deleteById(id);
    }

    // usato da POST /vcm/api/users/login
    // cerco l'utente per username, se non esiste lancio eccezione
    // poi confronto la password ricevuta (hashata) con quella salvata nel db
    // se coincidono genero il token JWT e lo restituisco al client
    public TokenDTO login(LoginDTO loginDTO)
    {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!user.getPassword().equals(passwordHasher.toHash(loginDTO.getPassword())))
            throw new RuntimeException("Invalid password");
        return new TokenDTO(jwtService.generateToken(user));
    }
}
