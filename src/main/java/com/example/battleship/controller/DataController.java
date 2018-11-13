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

    @RequestMapping(path="newgame") // This means URL's start with /newgame (after Application path)
    public String newGame() {
        GameController gameController = new GameController();
        this.mapRepository.deleteAll();
        gameController.setUpGame();

        this.mapRepository.save(gameController.getUserFleetBoard());
        this.mapRepository.save(gameController.getComputerFleetBoard());

        logger.info("An INFO Message: Game started");
        return "";
    }

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

        GameController gameController = new GameController();
        Cell userCell = null;
        Integer userCellIdOnBoard = gameController.findRandomCellForAttack("userFleetBoard");
        Iterable<Cell> cells = cellRepository.findAll();

        for(Cell c : cells) {
            if( (c.getMap().getType().equals("userFleetBoard")) &&    
                (c.getIdOnBoard() == userCellIdOnBoard)) {
                userCell = c;
            }
        }

        userCell.getMap().attackCell(userCell);
        cellRepository.save(userCell);

        return "{}";
    }

    @RequestMapping(path="isdefeated") // This means URL's start with /isdefeated (after Application path)
    public String checkDefeated(@RequestParam(value="name")String name) {
        logger.info(name);
        Iterable<Board> boards = mapRepository.findAll();

        for(Board b: boards) {
            if(b.getType().equals(name) && b.isDefeated()) return "true";
        }
        
        return "false";
    }
}
