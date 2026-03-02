package com.generation.veziocastelmanager.service;

import com.generation.veziocastelmanager.dto.VisitorDTO;
import com.generation.veziocastelmanager.mapper.VisitorMapper;
import com.generation.veziocastelmanager.model.entities.Visitor;
import com.generation.veziocastelmanager.model.repository.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class VisitorService
{
    @Autowired
    private VisitorRepository   visitorRepository;

    @Autowired
    private VisitorMapper       visitorMapper;

    // usato da GET /vcm/api/visitors
    // prendo tutti i visitatori dal db e li converto in DTO
    public List<VisitorDTO> findAll()
    {
        return visitorMapper.toDTOs(visitorRepository.findAll());
    }

    // usato da GET /vcm/api/visitors/{id}
    // cerco il visitatore per id, se non esiste lancio un'eccezione che Spring traduce in 404
    public VisitorDTO findById(int id)
    {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found with id: " + id));
        return (visitorMapper.toDTO(visitor));
    }

    // usato da POST /vcm/api/visitors e PUT /vcm/api/visitors/{id}
    // @Valid fa scattare le validazioni definite nel DTO prima di procedere
    // il mapper converte il DTO in entità, salvo e riconverto in DTO per la risposta
    public VisitorDTO       save(@Valid VisitorDTO visitorDTO)
    {
        Visitor visitor = visitorMapper.toEntity(visitorDTO);
        visitor         = visitorRepository.save(visitor);
        return (visitorMapper.toDTO(visitor));
    }

    // usato da DELETE /vcm/api/visitors/{id}
    public void deleteById(int id)
    {
        visitorRepository.deleteById(id);
    }
}
