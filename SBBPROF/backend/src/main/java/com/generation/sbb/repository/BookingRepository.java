package com.generation.sbb.repository;

import com.generation.sbb.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.from = ?1 AND b.room.category.hotel.id = ?2")
    List<Booking> findByFromAndHotelId(LocalDate from, int hotelId);

}