package com.generation.sbb.repository;

import com.generation.sbb.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {

    List<Guest> findByFirstNameOrSsnContaining(String a, String b);


}