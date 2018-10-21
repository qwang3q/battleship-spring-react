package com.example.battleship.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//import lombok.Data;

/**
 * Represents a 10 x 10 map in the battleship game.
 */
//@Data
@Entity
public class Board {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    public int getMapWidth() {
        return MAP_WIDTH;
    }

    public int getCellCount() {
        return CELL_COUNT;
    }

    private final int MAP_HEIGHT = 10;
    private final int MAP_WIDTH = 10;
    private final int CELL_COUNT = 100;

    public List<Cell> cells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "map")
    @JsonManagedReference
    private List<Cell> cells = new ArrayList<Cell>();

    public List<Ship> getShips() { return this.ships; }
    public void setShips(List<Ship> las) { this.ships = las; }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "map")
    @JsonManagedReference
    private List<Ship> ships = new ArrayList<Ship>();

    public void addShip(Ship ship) { this.ships.add(ship); }

    /**
     * Creates a new map with only water cells.
     */
    public Board(String type) {
        this.type = type;
        this.initialize();
    }

    public Board() {
        this("");
    }


    /**
     * Getter for property cells.
     *
     * @return value for property cells.
     */
    public List<Cell> getCells() {
        return cells;
    }

    public void initialize() {
        for (int i = 0; i < getCellCount(); i++) {
            Cell newCell = new Cell();
            addCell(newCell, i);
            newCell.setMap(this);
        }
    }

    /**
     * Update the fleet map after attack.
     *
     * @param posn         represents the position to be attacked
     */
    public void attackCell(Position posn) {
        attackCell(getIndexFromPosition(posn));
    }

    public void attackCell(Integer i) {
        Cell cell = getCell(i);
        attackCell(cell);
    }

    public void attackCell(Cell cell) {
        Ship ship = cell.getShip();

        if(ship != null && !cell.getHit()) {
            ship.incNumOdHitCells();
        }

        cell.setHit(true);

        if(ship != null && ship.isSunk()) {
            for(Cell shipCell : ship.getCells()) {
                shipCell.setSunk(true);
            }
        }
    }

    /**
     * Get a cell from the map at the specific position.
     *
     * @param posn represents the position on the column
     * @return the cell at specific position.
     */
    public Cell getCell(Position posn) {
        Integer i = getIndexFromPosition(posn);
        return getCell(i);
    }

    public Cell getCell(Integer i) {
        return this.getCells().get(i);
    }

    public void addCell(Cell cell, int id) {
        this.cells.add(cell);
        cell.setIdOnBoard(id);
    }

    public Integer getIndexFromPosition(Position posn) {
        Integer row = posn.getRowIndex() - 1;
        Integer col = posn.getColumnIndex() - 1;
        return row * getMapWidth() + col;
    }

    public Position getPositionFromIndex(Integer i) {
        Integer row = i / getMapHeight();
        Integer col = i % getMapWidth();
        return new Position(row + 1, col + 1);
    }

    public boolean goodForPlaceMent(Position posn, Direction direction, Ship ship) {
        if(posn == null || direction == null) return false;

        int numOfCells = ship.getSize();
        List<Position> visitedPosn = new ArrayList();

        for (int i = 0; i < numOfCells; i++) {
            // First make sure neighbors are not ship cells
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    Position newPosn = new Position(posn.getRow() + j, Character.toChars(posn.getColumn() + k)[0]);
                    if(visitedPosn.contains(newPosn)) continue;

                    if(newPosn.getColumnIndex() < 1 || newPosn.getRowIndex() < 1) continue;
                    if(newPosn.getColumnIndex() > 10 || newPosn.getRowIndex() > 10) continue;

                    Cell newCell = getCell(newPosn);
                    if(newCell.getShip() != null) return false;
                }
            }

            if(posn.getColumnIndex() < 1 || posn.getRowIndex() < 1) return false;
            if(posn.getColumnIndex() > 10 || posn.getRowIndex() > 10) return false;

            Cell cell = getCell(posn);
            if(cell.getShip() != null) return false;

            visitedPosn.add(posn);

            posn = getNextPosition(posn, direction);
        }
        return true;
    }

    public void placeShipOnMap(Position posn, Direction direction, Ship ship) {
        int numOfCells = ship.getSize();

        for (int i = 0; i < numOfCells; i++) {
            Cell shipCell = getCell(posn);
            ship.addCell(shipCell);
            shipCell.setShip(ship);
            shipCell.setShipCell(true);
            posn = getNextPosition(posn, direction);
        }

        this.addShip(ship);
    }

    /**
     * Get given position's next position in the given direction.
     * @param posn the given position
     * @param direction the given direction
     * @return given position's next position in the given direction.
     */
    protected Position getNextPosition(Position posn, Direction direction) {
        int row;
        char column;

        if (direction == Direction.HORIZONTAL) {
            row = posn.getRow();
            column = Character.toChars(posn.getColumn() + 1)[0];
        } else {
            row = posn.getRow() + 1;
            column = posn.getColumn();
        }
        return new Position(row, column);
    }

    /**
     * Get the positions of neighbor cells of the given cell.
     * @param posn represents the given cell.
     * @return the positions of given cell's neighbors.
     */
    public List<Position> getNeighborCells(Position posn) {
        int row = posn.getRow();
        char column = posn.getColumn();
        List<Position> neighbors = new ArrayList<>();
        Position newPosn;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    newPosn = new Position(row + i, Character.toChars(column + j)[0]);
                    if (newPosn.equals(posn)) {
                        continue;
                    }
                    neighbors.add(newPosn);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return neighbors;
    }

    public Boolean isDefeated() {
        for(Ship ship : this.ships) {
           if(!ship.isSunk()) {
               return false;
           }
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Board board1 = (Board) object;
        for (int i = 0; i < CELL_COUNT; i++) {
            if (!this.cells.get(i).equals(board1.getCells().get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < CELL_COUNT; i++) {
            result = result * 31 + this.cells.get(i).hashCode();
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder cellString = new StringBuilder();
        for (int i = 0; i < CELL_COUNT; i++) {
            cellString = cellString.append(this.cells.get(i).toString()).append("\n");
        }
        return "Board{"
                + "map=" + cellString
                + '}';
    }
}
