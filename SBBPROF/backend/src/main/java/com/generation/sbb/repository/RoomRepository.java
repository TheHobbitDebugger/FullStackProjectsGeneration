package com.generation.sbb.repository;

import com.generation.sbb.model.Room;
import com.generation.sbb.model.RoomCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r WHERE r.category.hotel.id = ?1 AND r NOT IN (SELECT b.room FROM Booking b WHERE b.from <= ?3 AND b.to >= ?2  order by price)")
    List<Room> findFreeRoomsInHotel(int hotelId, LocalDate from, LocalDate to);


    default Map<RoomCategory, List<String>> getFreeRoomsInHotelByCategory(int hotelId, LocalDate from, LocalDate to)
    {
        Map<RoomCategory, List<String>> result = new LinkedHashMap<RoomCategory, List<String>>();
        List<Room> rooms = findFreeRoomsInHotel(hotelId, from, to);
        
        for(Room r : rooms){
            if(!result.containsKey(r.getCategory())){
                result.put(r.getCategory(), new ArrayList<String>());
                result.get(r.getCategory()).add(r.getName());
            }
            else{
                result.get(r.getCategory()).add(r.getName());
            }
        }
        return result;
    }

}