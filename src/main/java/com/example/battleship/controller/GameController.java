package com.example.battleship.controller;

import com.example.battleship.DatabaseLoader;
import com.example.battleship.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represents an abstract battleship game.
 */
public class GameController {

    Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    private Board userFleetBoard;
    private Board computerFleetBoard;

    private HashMap<Integer, Integer> shipDict = new HashMap<Integer, Integer>(){{
        // put(1, 4);
        put(2, 3);
        put(3, 2);
        put(4, 1);
    }};

    public Board getUserFleetBoard() {
        return userFleetBoard;
    }
    public Board getComputerFleetBoard() {
        return computerFleetBoard;
    }


    /**
     * Create an abstract battleship game.
     */
    public GameController() {
        this.userFleetBoard = new Board("userFleetBoard");
        this.computerFleetBoard = new Board("computerFleetBoard");
    }

    public Integer findRandomCellForAttack(String type) {
        Position posn = null;
        Board board = type == "userFleetBoard" ? this.getUserFleetBoard() : this.getComputerFleetBoard();
        while(posn == null || board.getCell(posn).getHit()) {
            posn = RandomInput.generateRandomPosition();
        }

        return board.getIndexFromPosition(posn);
    }

    /**
     * Randomly choose a position and direction to place the given ship on board.
     *
     * @param ship represents the given ship.
     * @param board  represents the given board.
     */
    protected void randomPlaceOneShip(Ship ship, Board board) {
        Position posn = null;
        Direction direction = null;
        while(!board.goodForPlaceMent(posn, direction, ship)) {
            posn = RandomInput.generateRandomPosition();
            direction = RandomInput.generateRandomDirection();
        }

        // try {
            logger.info("place ship at: " + posn + direction);
            board.placeShipOnMap(posn, direction, ship);
            ship.setMap(board);
        // } catch (Exception e) {
        //     randomPlaceOneShip(ship, board);
        // }
    }

    /**
     * Use random position and direction to place all of computer's or player's ships on the board.
     *
     * @param board   the board to place ships on.
     * @param ships represents the ships to be placed on the board.
     */
    protected void randomPlacement(Board board, List<Ship> ships) {
        for (Ship ship : ships) {
            randomPlaceOneShip(ship, board);
        }
    }


    protected void placeShipsOnMap(Board board) {
        List<Ship> ships = new ArrayList<Ship>();
        for (Integer count : this.shipDict.keySet()) {
            for(Integer shipNum = 0; shipNum < this.shipDict.get(count); shipNum++)
            {
                ships.add(new Ship(count, 0));
            }
        }
        randomPlacement(board, ships);
    }

    /**
     * Set up the game, complete ship configuration.
     */
    public void setUpGame() {
        placeShipsOnMap(userFleetBoard);
        placeShipsOnMap(computerFleetBoard);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        GameController that = (GameController) object;
        return Objects.equals(getUserFleetBoard(), that.getUserFleetBoard())
                && Objects.equals(getComputerFleetBoard(), that.getComputerFleetBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserFleetBoard(), getComputerFleetBoard());
    }
}
