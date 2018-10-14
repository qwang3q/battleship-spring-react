package com.example.battleship.repository;

import com.example.battleship.model.Ship;
import org.springframework.data.repository.CrudRepository;

public interface ShipRepository extends CrudRepository<Ship, Integer> {
}
