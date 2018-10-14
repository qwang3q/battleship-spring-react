package com.example.battleship.repository;

import com.example.battleship.model.Cell;
import org.springframework.data.repository.CrudRepository;

public interface CellRepository extends CrudRepository<Cell, Integer> {
}
