package com.example.battleship.controller;

import com.example.battleship.DatabaseLoader;
import com.example.battleship.model.Board;
import com.example.battleship.model.Cell;
import com.example.battleship.repository.CellRepository;
import com.example.battleship.repository.MapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController    // This means that this class is a Controller
public class DataController {
    Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private CellRepository cellRepository;

    @RequestMapping(path="board") // This means URL's start with /board (after Application path)
    public Board getBoardByType(@RequestParam(value="type")String type) {
        Iterable<Board> boards = mapRepository.findAll();
        Board board = null;
        for(Board b : boards) {
            if(b.getType().equals(type)) {
                board = b;
            }
        }

        return board;
    }

    @RequestMapping(path="hitcell") // This means URL's start with /hitcell (after Application path)
    public String updateCell(@RequestParam(value="id") Integer id) {
        Cell cell = cellRepository.findById(id).get();
        cell.getMap().attackCell(cell);
        cellRepository.save(cell);
        return "{}";
    }

    @RequestMapping(path="isdefeated") // This means URL's start with /isdefeated (after Application path)
    public int checkDefeated(@RequestParam(value="href")String href) {
        logger.info(href);
        String[] parts = href.split("/");
        Integer id = Integer.parseInt(parts[parts.length - 1]);
        logger.info(id.toString());
        Board board = mapRepository.findById(id).get();
        if(board.isDefeated()) return 1;
        return 0;
    }
}
