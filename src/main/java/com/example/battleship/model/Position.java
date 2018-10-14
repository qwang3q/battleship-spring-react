package com.example.battleship.model;


import javax.persistence.Entity;
import java.util.Objects;

/**
 * Represents the position of a cell in the map.
 */
public class Position {

  private static final char FIRST_COLUMN = 'A';
  private static final char LAST_COLUMN = 'J';
  private static final int FIRST_ROW = 1;
  private static final int LAST_ROW = 10;

  private int row;
  private char column;


  /**
   * Creates a new position on the map.
   *
   * @param posn a string represents the position on the map. The string consists of column and row
   *             seperated by space，eg: "A 1". Column is from A to J. Row is from 1 to 10。
   * @throws InvalidArgumentException when invalid input is given.
   */
  public Position(String posn) {

    String[] pos = posn.split(" ");

    this.column = Character.toUpperCase(pos[0].charAt(0));

    this.row = Integer.parseInt(pos[1]);
  }


  /**
   * Creates a new position.
   *
   * @param row represent the row on the map
   * @param column represent the column on the map
   */
  public Position(int row, char column) {

    this.row = row;

    this.column = column;

  }

  /**
   * Creates a new position.
   *
   * @param row represent the row on the map
   * @param column represent the column on the map
   */
  public Position(int row, int column) {

      this.row = row + 1;
      this.column = Character.toChars(column + FIRST_COLUMN)[0];

  }

  /**
   * Getter for property row.
   *
   * @return value for property row
   */
  public int getRow() {
    return row;
  }

  /**
   * Getter for property column.
   *
   * @return value for property column.
   */
  public char getColumn() {
    return column;
  }

  /**
   * Get the row index on the map.
   *
   * @return the row index on the map
   */
  public int getRowIndex() {
    return row - FIRST_ROW;
  }

  /**
   * Get the column index on the map.
   *
   * @return the row index on the map
   */
  public int getColumnIndex() {
    return column - FIRST_COLUMN;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Position position = (Position) object;
    return row == position.row
        && column == position.column;
  }

  @Override
  public int hashCode() {

    return Objects.hash(row, column);
  }

  @Override
  public String toString() {
    return "Position{"
        + "row=" + row
        + ", column=" + column
        + '}';
  }
}


