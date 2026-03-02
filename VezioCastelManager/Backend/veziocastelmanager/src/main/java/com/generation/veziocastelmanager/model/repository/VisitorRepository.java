package com.generation.veziocastelmanager.model.repository;

import com.generation.veziocastelmanager.model.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer>
{

}
