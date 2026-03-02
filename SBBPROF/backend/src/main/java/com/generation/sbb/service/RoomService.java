package com.generation.sbb.service;

import com.generation.sbb.dto.RoomCategoryDTO;
import com.generation.sbb.dto.RoomDTO;
import com.generation.sbb.mapper.RoomCategoryMapper;
import com.generation.sbb.mapper.RoomMapper;
import com.generation.sbb.model.Room;
import com.generation.sbb.model.RoomCategory;
import com.generation.sbb.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomCategoryMapper roomCategoryMapper;

    public List<RoomDTO> findAll() {
        return roomMapper.toDTOs(roomRepository.findAll());
    }

    public RoomDTO findById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
        return roomMapper.toDTO(room);
    }

    public RoomDTO save(@Valid RoomDTO roomDTO) {
        Room room = roomMapper.toEntity(roomDTO);
        room = roomRepository.save(room);
        return roomMapper.toDTO(room);
    }

    public void deleteById(Integer id) {
        roomRepository.deleteById(id);
    }

    public List<RoomCategoryDTO> findFreeRoomsInHotel(int hotelId, LocalDate from, LocalDate to) {
        Map<RoomCategory, List<String>> result = roomRepository.getFreeRoomsInHotelByCategory(hotelId, from, to);
        List<RoomCategoryDTO> roomCategories = new ArrayList<RoomCategoryDTO>();
        int minPrice = 10000;
        for(RoomCategory category : result.keySet()) {
            RoomCategoryDTO dto = roomCategoryMapper.toDTO(category);
            // aggiungo al RoomCategoryDTO il calcolo delle stanze libere di quel dato tipo per l'hotel hotelID
            dto.setFree(result.get(category).size());   // il numero delle stanze
            dto.setRoomNames(result.get(category));     // i nomi delle stanze
            int price = category.getPrice();
            if(price<minPrice){
                minPrice = price;
                dto.setDefaultRoom(dto.getRoomNames().get(0));
            }
            roomCategories.add(dto);
        }            
        return roomCategories;
    }
        

}