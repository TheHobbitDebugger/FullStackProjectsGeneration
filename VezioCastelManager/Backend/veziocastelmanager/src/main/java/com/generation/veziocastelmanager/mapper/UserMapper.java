package com.generation.veziocastelmanager.mapper;

import com.generation.veziocastelmanager.dto.UserDTO;
import com.generation.veziocastelmanager.model.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserDTO         toDTO(User user);
    List<UserDTO>   toDTOs(List<User> users);
    User            toEntity(UserDTO userDTO);
    List<User>      toEntities(List<UserDTO> userDTOs);
}
