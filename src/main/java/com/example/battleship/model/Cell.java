package com.example.battleship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Represents the common properties of a cell in the map of the battleship game.
 */
@Entity
public class Cell implements Serializable {
    private boolean isShipCell;
    private boolean isHit;
    private boolean isSunk;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board map;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ship_id")
    @JsonBackReference
    private Ship ship;

    /**
     * Creates a cell.
     * @param isHit represents whether the cell is hit, true means the cell was hit, false otherwise
     */
    public Cell(Boolean isShipCell, Boolean isHit) {
        this.isHit = isHit;
        this.isShipCell = isShipCell;
    }

    public Cell() {
        this(false, false);
    }

    public boolean isShipCell() {
        return isShipCell;
    }

    public void setShipCell(boolean shipCell) {
        isShipCell = shipCell;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHit() {
        return isHit;
    }

    public void setHit(Boolean hit) {
        isHit = hit;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }


    public Board getMap() {
        return map;
    }

    public void setMap(Board map) {
        this.map = map;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}
