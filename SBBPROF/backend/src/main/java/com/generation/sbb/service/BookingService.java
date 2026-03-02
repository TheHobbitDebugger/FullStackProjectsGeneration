package com.generation.sbb.service;

import com.generation.sbb.dto.BookingDTO;
import com.generation.sbb.mapper.BookingMapper;
import com.generation.sbb.model.Booking;
import com.generation.sbb.model.BookingStatus;
import com.generation.sbb.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    public List<BookingDTO> findTodaysArrivalForHotel(int hotelId){
        return bookingMapper.toDTOs(bookingRepository.findByFromAndHotelId(LocalDate.now(), hotelId));
    }

    public List<BookingDTO> findAll() {
        return bookingMapper.toDTOs(bookingRepository.findAll());
    }

    public BookingDTO findById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        return bookingMapper.toDTO(booking);
    }

    public BookingDTO save(@Valid BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDTO(booking);
    }

    public void deleteById(Integer id) {
        bookingRepository.deleteById(id);
    }

    public void changeStatus(int id, String status) {   
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        b.setStatus(BookingStatus.valueOf(status));
        bookingRepository.save(b);
    }

}
