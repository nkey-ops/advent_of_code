package day6;

import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 *
 * <pre>
 * --- Day 6: Wait For It ---
 * https://adventofcode.com/2023/day/6
 *
 * --- First Star --
 * Holding down the button charges the boat, and releasing the button allows
 * the boat to move. Boats move faster if their button was held longer,
 * but time spent holding the button counts against the total race time.
 * You can only hold the button at the start of the race, and boats don't move
 * until the button is released.
 *
 * For example:
 *
 *  Time:      7  15   30
 *  Distance:  9  40  200
 *  This document describes three races:
 *
 * - The first race lasts 7 milliseconds. The record distance in this race is 9 millimeters.
 * - The second race lasts 15 milliseconds. The record distance in this race is 40 millimeters.
 * - The third race lasts 30 milliseconds. The record distance in this race is 200 millimeters.
 *
 * Your toy boat has a starting speed of zero millimeters per millisecond.
 * For each whole millisecond you spend at the beginning of the race holding
 * down the button, the boat's speed increases by one millimeter per millisecond.
 *
 * Since the current record for this race is 9 millimeters, there are actually 4
 * different ways you could win: you could hold the button for 2, 3, 4, or 5 milliseconds
 * at the start of the race.
 *
 * In the second race, you could hold the button for at least 4 milliseconds
 * and at most 11 milliseconds and beat the record, a total of 8 different ways to win.
 *
 * In the third race, you could hold the button for at least 11 milliseconds
 * and no more than 19 milliseconds and still beat the record, a total of 9 ways you could win.
 *
 * To see how much margin of error you have, determine the number of ways you
 * can beat the record in each race; in this example, if you multiply these
 * values together, you get 288 (4 * 8 * 9).
 *
 * Determine the number of ways you could beat the record in each race.
 * What do you get if you multiply these numbers together?
 *
 * Your puzzle answer was 219849.
 *
 * --- Part Two ---
 *
 * As the race is about to start, you realize the piece of paper with race 
 * times and record distances you got earlier actually just has very bad kerning. 
 * There's really only one race - ignore the spaces between the numbers on each line.
 *
 * So, the example from before:
 *
 *  Time:      7  15   30
 *  Distance:  9  40  200
 *
 * ...now instead means this:
 *
 *  Time:      71530
 *  Distance:  940200
 *
 * Now, you have to figure out how many ways there are to win this single race. 
 * In this example, the race lasts for 71530 milliseconds and the record distance 
 * you need to beat is 940200 millimeters. You could hold the button anywhere 
 * from 14 to 71516 milliseconds and beat the record, a total of 71503 ways!
 *
 * Your puzzle answer was 29432455.
 *
 * </pre>
 */
public class Day6 {
  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day6/";

    System.out.println(getFirstStar(path + "/test1.txt"));
    System.out.println(getFirstStar(path + "/input1.txt"));
    System.out.println(getSecondStar(path + "/test1.txt"));
    System.out.println(getSecondStar(path + "/input1.txt"));
  }

  private static int getFirstStar(String inputName) throws IOException {
    int winCounter = 0;
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      String[] times = bf.readLine().split("\\s+");
      String[] distances = bf.readLine().split("\\s+");

      if (times.length != distances.length || times.length < 1) {
        throw new IllegalArgumentException();
      }

      for (int i = 1; i < distances.length; i++) {
        int wins = getNumOfWaysToWin(parseInt(times[i]), parseInt(distances[i]));
        winCounter = winCounter == 0 ? wins : winCounter * wins;
      }
    }

    return winCounter;
  }

  private static int getSecondStar(String inputName) throws FileNotFoundException, IOException {
    int winCounter = 0;
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      String[] times = bf.readLine().replaceAll("\\s+", "").split("Time:");
      String[] distances = bf.readLine().replaceAll("\\s+", "").split("Distance:");

      System.out.println(Arrays.toString(times));
      System.out.println(Arrays.toString(distances));

      if (times.length != distances.length || times.length < 1) {
        throw new IllegalArgumentException();
      }

      for (int i = 1; i < distances.length; i++) {
        int wins = getNumOfWaysToWin(Long.parseLong(times[i]), Long.parseLong(distances[i]));
        winCounter = winCounter == 0 ? wins : winCounter * wins;
      }
    }

    return winCounter;
  }

  /**
   *
   *
   * <pre>
   *
   * Finds the number of longest distances comparing to the provided one.
   *
   * Where:
   *  First value is distance,
   *  Second value is press time
   *
   * t = 7, d = 9
   *
   * 0 0-0
   * 1 ...... 6-1
   * 2 .......... 10-2
   * 3 ............ 12-3
   * 4 ............ 12-4
   * 5 .......... 10-5
   * 6 ...... 6-6
   * 7 0-7
   *
   * 2, 3, 4, 5 rows have a longer distance the 9
   * Total longest distances is 4
   *
   * </pre>
   *
   * @param t race time
   * @param d distance to compare with
   * @return the number of longest distances
   */
  private static int getNumOfWaysToWin(long t, long d) {
    if (t < 0 || d < 0) {
      throw new IllegalArgumentException();
    }

    int wins = 0;
    for (long i = 0; i <= t; i++) {
      long calc = calcDistance(i, t);
      if (calc > d) {
        wins++;
      }
    }

    return wins;
  }

  /**
   * Calculates the distance based on a press time and race time by the formula {@literal (t - p) *
   * p}.
   *
   * @param p press time
   * @param t race time
   * @return distance
   */
  public static long calcDistance(long p, long t) {
    if (p < 0 || t < 0 || t < p) {
      throw new IllegalArgumentException();
    }

    return (t - p) * p;
  }
}
