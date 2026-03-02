package com.generation.veziocastelmanager.api;

import com.generation.veziocastelmanager.dto.LoginDTO;
import com.generation.veziocastelmanager.dto.TokenDTO;
import com.generation.veziocastelmanager.dto.UserDTO;
import com.generation.veziocastelmanager.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vcm/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserAPI
{
    @Autowired
    private UserService service;

    // GET /vcm/api/users — ritorna tutti gli utenti
    @GetMapping
    public List<UserDTO> findAll()
    {
        return service.findAll();
    }

    // GET /vcm/api/users/{id} — ritorna un utente per id
    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable int id)
    {
        return service.findById(id);
    }

    // POST /vcm/api/users — crea un nuovo utente, ritorna 201 se va bene o 400 se i dati non sono validi
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserDTO dto)
    {
        try
        {
            dto = service.save(dto);
            return ResponseEntity.status(201).body(dto);
        }
        catch (ConstraintViolationException e)
        {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // PUT /vcm/api/users/{id} — aggiorna un utente esistente
    // prendo l'id dal path e lo setto nel dto prima di passarlo al service
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody UserDTO dto)
    {
        try
        {
            dto.setId(id);
            dto = service.save(dto);
            return ResponseEntity.ok(dto);
        }
        catch (ConstraintViolationException e)
        {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // DELETE /vcm/api/users/{id} — elimina un utente, ritorna 204 senza corpo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id)
    {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // POST /vcm/api/users/login — riceve username e password, ritorna il token JWT se le credenziali sono corrette
    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO)
    {
        return service.login(loginDTO);
    }
}
