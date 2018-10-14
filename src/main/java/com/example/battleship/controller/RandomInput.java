package com.example.battleship.controller;

import com.example.battleship.model.Direction;
import com.example.battleship.model.Position;

import java.util.Random;



/**
 * Generate random position and direction for other class.
 */
public class RandomInput {

  /**
   * Generate a random position.
   *
   * @return a random position
   */
  public static Position generateRandomPosition() {
    Random rand = new Random();
    int row = rand.nextInt(10) + 1;
    char column = Character.toChars('A' + rand.nextInt(10))[0];
    return new Position(row, column);
  }

  /**
   * Generate a random direction.
   *
   * @return a random direction.
   */
  public static Direction generateRandomDirection() {
    Random rand = new Random();
    int number = rand.nextInt(2);
    if (number == 0) {
      return Direction.HORIZONTAL;
    } else {
      return Direction.VERTICAL;
    }
  }


}
