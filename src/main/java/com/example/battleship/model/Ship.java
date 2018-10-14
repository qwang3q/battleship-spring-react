package com.example.battleship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the comman properties of ship in battleship game.
 */
@Entity
public class Ship{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board map;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "ship")
    @JsonManagedReference
    private List<Cell> cells = new ArrayList<Cell>();

    private Integer size;
    private Integer numOfHitCells;

    /**
     * Creates a ship.
     * @param size the number of cells a ship has, which is greater than 0 and less or equal to 4
     * @param numOfHitCells the number of cells that were hit, which is greater than 0 and less or
     *                      equal to ship size
     */
    public Ship(Integer size, Integer numOfHitCells) {
        this.size = size;
        this.numOfHitCells = numOfHitCells;
    }

    public Ship() {
        this(1, 0);
    }


    /**
     * Returns true is the ship has been sunk and false otherwise.
     *
     * @return true is the ship has been sunk and false otherwise
     */
    public Boolean isSunk() {
        for(Cell cell : this.cells) {
            if (cell.getHit() != true) {
                return false;
            }
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Board getMap() {
        return map;
    }

    public void setMap(Board map) {
        this.map = map;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumOfHitCells() {
        return numOfHitCells;
    }

    public void setNumOfHitCells(Integer numOfHitCells) {
        this.numOfHitCells = numOfHitCells;
    }

    public void incNumOdHitCells() {
        this.numOfHitCells++;
    }
}
