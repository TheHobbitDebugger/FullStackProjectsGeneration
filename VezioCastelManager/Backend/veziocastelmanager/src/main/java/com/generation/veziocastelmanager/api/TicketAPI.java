package com.generation.veziocastelmanager.api;

import com.generation.veziocastelmanager.dto.TicketDTO;
import com.generation.veziocastelmanager.service.TicketService;
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
@RequestMapping("/vcm/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketAPI
{
    @Autowired
    private TicketService service;

    // GET /vcm/api/tickets — ritorna tutti i biglietti
    @GetMapping
    public List<TicketDTO> findAll()
    {
        return service.findAll();
    }

    // GET /vcm/api/tickets/{id} — ritorna un biglietto per id
    @GetMapping("/{id}")
    public TicketDTO findById(@PathVariable int id)
    {
        return service.findById(id);
    }

    // POST /vcm/api/tickets — crea un nuovo biglietto
    // il prezzo viene calcolato automaticamente nel service in base all'età del visitatore
    // ritorna 201 se va bene o 400 se i dati non sono validi
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody TicketDTO dto)
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

    // PUT /vcm/api/tickets/{id} — aggiorna un biglietto esistente
    // prendo l'id dal path e lo setto nel dto prima di passarlo al service
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody TicketDTO dto)
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

    // DELETE /vcm/api/tickets/{id} — elimina un biglietto, ritorna 204 senza corpo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id)
    {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
