package day2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** 
 * https://adventofcode.com/2023/day/2 
 * --- Day 2: Cube Conundrum ---
 *
 * --- Part One ---
 * You play several games and record the information from each game (your puzzle input). 
 * Each game is listed with its ID number (like the 11 in Game 11: ...) 
 * followed by a semicolon-separated list of subsets of cubes that 
 * were revealed from the bag (like 3 red, 5 green, 4 blue).
 *
 * For example, the record of a few games might look like this:
 *  Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 *  Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 *  Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 *  Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 *  Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 *
 * The Elf would first like to know which games would have been possible 
 * if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?
 *
 * Your puzzle answer was 2406.
 *
 * --- Part Two ---
 * As you continue your walk, the Elf poses a second question: 
 * in each game you played, 
 * what is the fewest number of cubes of each color that could have been 
 * in the bag to make the game possible?
 * 
 * Again consider the example games from earlier:
 *  Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 *  Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 *  Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 *  Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 *  Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 *
 * The power of a set of cubes is equal to the numbers of red, green, and blue 
 * cubes multiplied together. 
 * The power of the minimum set of cubes in game 1 is 48. In games 2-5 it 
 * was 12, 1560, 630, and 36, respectively. Adding up these five powers 
 * produces the sum 2286.
 *
 * For each game, find the minimum set of cubes that must have been present.
 * What is the sum of the power of these sets?
 *
 *  Your puzzle answer was 78375.
 **/
public class Day2 {

  /*read line
    FIRST START
    get total num of cubes per color () {
      set max vars for blue, red, green
      split into \d{1,}\s\(blue|red|green) pairs

      if(value of the pair > maxVar) add id to counter
      else continue
    repeat
  */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    String testName =  "/test1.txt";
    String inputName = "/input1.txt";

    System.out.println(getSumSecondStar(inputName));
  }

  /**
   * FIRST STAR.
   *
   *  set max vars for blue, red, green
   *
   *  read line 
   *  split into \d{1,}\s\(blue|red|green) pairs
   *  if(value of the pair > maxVar) 
   *    add id to counter 
   *  else continue
   *  repeate
   */
  private static int getSumFirstStar(String inputName) throws IOException, FileNotFoundException {
    int counter = 0;
    int bMax = 14, rMax = 12, gMax = 13;
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      While:
      while (bf.ready()) {
        String line = bf.readLine();
        String[] pairs = line.split(":\\s|,\\s|;\\s|(\\s\\d+\\[b,r,w])");

        for (int i = 1; i < pairs.length; i++) {
          String[] pair = pairs[i].split("\\s");
          int num = Integer.parseInt(pair[0]);

          if (pair[1].equals("blue") && num > bMax) {
            continue While;
          } else if (pair[1].equals("red") && num > rMax) {
            continue While;
          } else if (pair[1].equals("green") && num > gMax) {
            continue While;
          }
        }

        counter += Integer.valueOf(pairs[0].split("\\s")[1]);
      }
    }
    return counter;
  }

  /**
   * SECOND STAR.
   *
   * set max vars for blue, red, green
   *
   * read line 
   * split into \d{1,}\s\(blue|red|green) pairs
   * if(value of the pair > maxVar) 
   *  set val as a maxVar
   * 
   *
   * repeate
   *
   */
  private static int getSumSecondStar(String inputName) throws IOException, FileNotFoundException {
    int counter = 0;
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      while (bf.ready()) {
        String line = bf.readLine();
        System.out.println(line);

        String[] pairs = line.split(":\\s|,\\s|;\\s|(\\s\\d+\\[b,r,w])");

        int bMax = 0, rMax = 0, gMax = 0;
        for (int i = 1; i < pairs.length; i++) {
          String[] pair = pairs[i].split("\\s");
          int num = Integer.parseInt(pair[0]);

          if (pair[1].equals("blue") && num > bMax) {
            bMax = num;
          } else if (pair[1].equals("red") && num > rMax) {
            rMax = num;
          } else if (pair[1].equals("green") && num > gMax) {
            gMax = num;
          }
        }

        counter += bMax * rMax * gMax;
      }
    }
    return counter;
  }
}
