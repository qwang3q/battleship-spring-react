package com.example.battleship.controller;


import com.example.battleship.model.*;

import java.util.ArrayList;
import java.util.List;

public class SmartStrategy {

    /**
     * Return a position on the board to be attacked.
     *
     * @param board represents the battle board.
     * @return a position on the board to be attacked.
     */

    public Position chooseCellToAttack(Board board) {
        Position posnToAttack;
        posnToAttack = findPotentialShipCell(board);
        if (posnToAttack == null) {
            do {
                posnToAttack = chooseRandomCellToAttack(board);
            } while (checkIsGapCell(board, posnToAttack));
        }
        return posnToAttack;
    }

    public Position chooseRandomCellToAttack(Board board) {
        Position posn;
        do {
            posn = RandomInput.generateRandomPosition();
        } while (board.getCell(posn).getHit());
        return posn;
    }


    /**
     * Search the board to find a potential ship cell.
     *
     * @param board represents a battle board
     * @return the position of potential ship cell.
     */
    public Position findPotentialShipCell(Board board) {
        Position posnToAttack = null;
        List<Cell> cells = board.getCells();
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = board.getCell(i);
            Position cellPosition = board.getPositionFromIndex(i);
            if (cell.isShipCell() && !cell.isSunk()) {
                posnToAttack = findSurroundingShipCell(board, cellPosition);
                if (posnToAttack != null) {
                    return posnToAttack;
                }
            }
        }
        return posnToAttack;
    }


    /**
     * Find the position of a potential ship cell surrounding the given ship cell.
     *
     * @param board          represents the battle ship.
     * @param cellPosition represents the given ship cell.
     * @return the position of potential ship cell.
     */
    public Position findSurroundingShipCell(Board board, Position cellPosition) {

        Position cellToAttack = null;
        List<Position> surroundingCells = getSurroundingCells(cellPosition);
        for (Position surroundingCellPosition : surroundingCells) {
            Cell surroundingCell = board.getCell(surroundingCellPosition);
            if (surroundingCell.isShipCell()) {
                cellToAttack = calculateShipCellPosition(cellPosition, surroundingCellPosition);
                if (cellToAttack != null && board.getCell(cellToAttack).getHit()) {
                    cellToAttack = null;
                }
                break;
            }
            if (!surroundingCell.getHit()) {
                cellToAttack = surroundingCellPosition;
            }
        }
        return cellToAttack;
    }


    /**
     * Get the list of positions of cells surrounding the given cell.
     *
     * @param currentCell represents the given cell.
     * @return the list of positions of cells surrounding the given cell.
     */
    public List<Position> getSurroundingCells(Position currentCell) {
        List<Position> result = new ArrayList<>();
        int[][] coordinates = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < 4; i++) {
            try {
                int newRow = currentCell.getRowIndex() + coordinates[i][0];
                int newColumn = currentCell.getColumnIndex() + coordinates[i][1];
                result.add(new Position(newRow, newColumn));
            } catch (Exception e) {
                continue;
            }
        }
        return result;
    }

    /**
     * Calculate the mirror position of current cell's surrounding cell.
     *
     * @param currentCell     represents current cell which is in the middle.
     * @param surroundingCell represents a cell surrounding current cell.
     * @return the mirror positon of surrounding cell.
     */
    public Position calculateShipCellPosition(Position currentCell, Position surroundingCell) {
        int row;
        int column;
        Position result;
        if (currentCell.getRowIndex() == surroundingCell.getRowIndex()) {
            column = currentCell.getColumnIndex()
                    - (surroundingCell.getColumnIndex() - currentCell.getColumnIndex());
            row = currentCell.getRowIndex();
        } else {
            row = currentCell.getRowIndex()
                    - (surroundingCell.getRowIndex() - currentCell.getRowIndex());
            column = currentCell.getColumnIndex();
        }
        try {
            result = new Position(row, column);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if the cell at given position is a gap cell.
     *
     * @param board          represents the battle board.
     * @param cellPosition represents the given position.
     * @return true if given cell is a gap cell, false otherwise.
     */
    public boolean checkIsGapCell(Board board, Position cellPosition) {
        List<Position> neighbors = board.getNeighborCells(cellPosition);
        for (Position neighbor : neighbors) {
            if (board.getCell(neighbor).isShipCell()) {
                return true;
            }
        }
        return false;
    }
}
