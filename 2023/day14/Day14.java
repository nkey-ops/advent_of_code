package day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

/**
 * 4
 *
 * <pre>
 *
 * --- Day 14: Parabolic Reflector Dish ---
 *
 * https://adventofcode.com/2023/day/14
 *
 *
 * In short: if you move the rocks, you can focus the dish.
 * The platform even has a control panel on the side that lets you tilt it in one of 
 * four directions! The rounded rocks (O) will roll when the platform is tilted,
 * while the cube-shaped rocks (#) will stay in place. You note the positions of all of the
 * empty spaces (.) and rocks (your puzzle input).
 *
 * For example:
 *
 *    O....#....
 *    O.OO#....#
 *    .....##...
 *    OO.#O....O
 *    .O.....O#.
 *    O.#..O.#.#
 *    ..O..#O..O
 *    .......O..
 *    #....###..
 *    #OO..#....
 *
 * Start by tilting the lever so all of the rocks will slide north as far as they will go:
 *
 *    OOOO.#.O..
 *    OO..#....#
 *    OO..O##..O
 *    O..#.OO...
 *    ........#.
 *    ..#....#.#
 *    ..O..#.O.O
 *    ..O.......
 *    #....###..
 *    #....#....
 *
 * You notice that the support beams along the north side of the platform are damaged;
 * to ensure the platform doesn't collapse, you should calculate the total load on the 
 * north support beams.
 *
 * The amount of load caused by a single rounded rock (O) is equal to the number of rows from 
 * the rock to the south edge of the platform, including the row the rock is on. 
 * (Cube-shaped rocks (#) don't contribute to load.) So, the amount of load caused by each 
 * rock in each row is as follows:
 *
 *    OOOO.#.O.. 10
 *    OO..#....#  9
 *    OO..O##..O  8
 *    O..#.OO...  7
 *    ........#.  6
 *    ..#....#.#  5
 *    ..O..#.O.O  4
 *    ..O.......  3
 *    #....###..  2
 *    #....#....  1
 *
 * The total load is the sum of the load caused by all of the rounded rocks.
 * In this example, the total load is 136.
 *
 * Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total 
 * load on the north support beams?
 *
 * Your puzzle answer was 106648.
 *
 * </pre>
 */
public class Day14 {

  public static void main(String[] args) {
    String path = "/home/deuru/table/space/advent_of_code/2023/day14";

    int firstStar = getFirstStar(path + "/test1.txt");
    System.out.println(firstStar);
    assert firstStar == 136;

    int firstStar2 = getFirstStar(path + "/input1.txt");
    System.out.println(firstStar2);
    assert firstStar2 == 106648;
  }

  private static int getFirstStar(String input) {
    String[] platform = getPlatform(input);

    return calcucalteTotalLoad(platform);
  }

  /**
   * Returns data splited into lines from the {@code input} 
   *
   * @param input file path to read data from
   * @return data splited into lines from the {@code input} 
   */
  private static String[] getPlatform(String input) {
    Objects.requireNonNull(input);

    try (BufferedReader bf = new BufferedReader(new FileReader(input))) {
      return bf.lines().toArray(String[]::new);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static int calcucalteTotalLoad(String[] platform) {
    Objects.requireNonNull(platform);
    if(platform.length == 0 ) 
      throw new IllegalArgumentException();

    int sum = 0;

    int width = platform[0].length();
    int[] spaces = new int[width];

    for (int i = 0; i < platform.length; i++) {
      String line = platform[i];

       if (line == null || line.isBlank() || line.length() != width) {
         throw new IllegalArgumentException(); 
       };


      for (int j = 0; j < width; j++) {
        char ch = line.charAt(j);

        switch (ch) {
          case 'O' -> {
            sum = Math.addExact(sum, platform.length - (i - spaces[j]));
          }
          case '#' -> {
            spaces[j] = 0;
          }
          case '.' -> {
            spaces[j] = spaces[j] + 1;
          }
          default -> throw new IllegalArgumentException();
        }
      }
    }

    return sum;
  }
}
