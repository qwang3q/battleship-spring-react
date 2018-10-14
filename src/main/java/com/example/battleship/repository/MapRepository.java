package com.example.battleship.repository;

import com.example.battleship.model.Board;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MapRepository extends CrudRepository<Board, Integer> {
}
