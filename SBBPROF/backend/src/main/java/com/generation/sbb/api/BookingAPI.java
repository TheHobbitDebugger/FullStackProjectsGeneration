package com.generation.sbb.api;

import com.generation.sbb.dto.BookingDTO;
import com.generation.sbb.service.BookingService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sbb/api/bookings")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingAPI {

    @Autowired
    private BookingService service;
  
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody BookingDTO dto) {
        try {
            dto = service.save(dto);
            return ResponseEntity.status(201).body(dto);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody BookingDTO dto) {
        try {
            dto.setId(id);
            dto = service.save(dto);
            return ResponseEntity.ok(dto);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public BookingDTO findById(@PathVariable int id) {
        return service.findById(id);
    }
    
    @GetMapping("/todaysarrivals/{hotelId}")
    public List<BookingDTO> findAll(@PathVariable("hotelId") int hotelId) {
        return service.findTodaysArrivalForHotel(hotelId);
    }

    @PatchMapping("/{id}/{status}") 
    public ResponseEntity<Void> changeStatus(@PathVariable("id") int id, @PathVariable("status") String status) {
        try{
            service.changeStatus(id, status);
            return ResponseEntity.status(204).build();
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(404).build();
        }
    }




}