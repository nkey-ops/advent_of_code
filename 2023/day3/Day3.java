package day3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/3
 *
 * <p>--- Day 3: Gear Ratios ---
 *
 * <p>--- Part One ---
 *
 * <p>The engine schematic (your puzzle input) consists of a visual representation of the engine.
 *
 * <p>There are lots of numbers and symbols you don't really understand, but apparently any number
 * adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum.
 * (Periods (.) do not count as a symbol.)
 *
 * <p>Here is an example engine schematic:
 *
 * <p><code>
 *   467..114..
 *   ...*......
 *   ..35..633.
 *   ......#...
 *   617*......
 *   .....+.58.
 *   ..592.....
 *   ......755.
 *   ...$.*....
 *   .664.598..
 * </code>
 *
 * <p>In this schematic, two numbers are not part numbers because they are not adjacent to a symbol:
 * 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a
 * part number; their sum is 4361.
 *
 * <p>Your puzzle answer was 531561.
 *
 * <p>--- Part Two ---
 *
 * <p>The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is
 * any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of
 * multiplying those two numbers together.
 *
 * <p>Consider the same engine schematic again:
 *
 * <p><code>
 *   467..114..
 *   ...*......
 *   ..35..633.
 *   ......#...
 *   617*......
 *   .....+.58.
 *   ..592.....
 *   ......755.
 *   ...$.*....
 *   .664.598..
 * </code>
 *
 * <p>In this schematic, there are two gears. The first is in the top left; it has part numbers 467
 * and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is
 * 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.)
 * Adding up all of the gear ratios produces 467835.
 *
 * <p>Your puzzle answer was 83279367.
 */
public class Day3 {

  public static void main(String[] args) throws IOException {
    String path = "/";

    System.out.println(getFirstStar(path + "/test1.txt"));
    System.out.println(getFirstStar(path + "/input1.txt"));
    System.out.println(getSecondStar(path + "/test1.txt"));
    System.out.println(getSecondStar(path + "/input1.txt"));
  }

  private static int getFirstStar(String inputName) throws FileNotFoundException, IOException {
    Set<Integer> nums = new HashSet<>();
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      char[] prevLine = null;
      char[] nextLine = bf.ready() ? bf.readLine().toCharArray() : null;

      List<Integer> r = new ArrayList<>();
      int y = 0;

      while (nextLine != null) {
        char[] currLine = nextLine;
        nextLine = bf.ready() ? bf.readLine().toCharArray() : null;

        for (int i = 0; i < currLine.length; i++) {
          char ch = currLine[i];

          if (ch != '.' && (ch < 48 || ch > 57)) {
            getAdjacentNums(y, i, prevLine, currLine, nextLine, nums).forEach(r::add);
          }
        }

        prevLine = currLine;
        y++;
      }

      int v = 0;
      for (Integer integer : r) {
        v += integer;
      }

      return v;
    }
  }

  private static int getSecondStar(String inputName) throws FileNotFoundException, IOException {
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      char[] prevLine = null;
      char[] nextLine = bf.ready() ? bf.readLine().toCharArray() : null;

      int counter = 0;
      while (nextLine != null) {
        char[] currLine = nextLine;
        nextLine = bf.ready() ? bf.readLine().toCharArray() : null;

        for (int i = 0; i < currLine.length; i++) {
          char ch = currLine[i];

          if (ch != '.' && ch == '*') {
            int r = getGearsMyltiplicaton(i, prevLine, currLine, nextLine);
            if (r != -1) {
              counter += r;
            }
          }
        }

        prevLine = currLine;
      }

      return counter;
    }
  }

  private static int getGearsMyltiplicaton(
      int x, char[] prevLine, char[] currLine, char[] nextLine) {
    if (currLine == null) {
      throw new IllegalArgumentException();
    }

    char[][] lines = {prevLine, currLine, nextLine};
    int[] axis = {-1, 0, 1};

    int firstNum = -1;
    int result = -1;

    ForY:
    for (int yDir : axis) {
      if ((prevLine == null && yDir == -1) || (nextLine == null && yDir == 1)) {
        continue;
      }

      for (int xDir : axis) {
        int adY = yDir + 1;
        int adX = xDir + x;

        if (isOutOfBounds(currLine, yDir, xDir, adY, adX)) {
          continue;
        }

        char ch = lines[adY][adX];
        if (!isDigit(ch)) {
          continue;
        }

        if (result != -1) {
          return -1;
        }

        int start = getStartOfNum(adX, lines[adY]);
        int end = getEndOfNum(adX, lines[adY]);

        int v = Integer.valueOf(String.valueOf(lines[adY], start, end - start));

        if (firstNum == -1) {
          firstNum = v;
        } else {
          result = firstNum * v;
        }

        // prevent checking the same number on the first and last lines on next loop.
        if (end - 1 > adX && (adY == 0 || adY == 2)) {
          continue ForY;
        }
      }
    }

    return result;
  }

  private static List<Integer> getAdjacentNums(
      int y, int x, char[] prevLine, char[] currLine, char[] nextLine, Set<Integer> savedNums) {

    if (currLine == null || savedNums == null) {
      throw new IllegalArgumentException();
    }

    char[][] lines = {prevLine, currLine, nextLine};
    int[] axy = {-1, 0, 1};

    List<Integer> res = new ArrayList<>();

    ForY:
    for (int yDir : axy) {
      if ((prevLine == null && yDir == -1) || (nextLine == null && yDir == 1)) {
        continue;
      }
      for (int xDir : axy) {
        int adY = yDir + 1;
        int adX = xDir + x;

        if (isOutOfBounds(currLine, yDir, xDir, adY, adX)) {
          continue;
        }

        char c = lines[adY][adX];
        if (!isDigit(c)) {
          continue;
        }

        // OPTTIP while trying to get a start and end save passed
        // chars as a string to immediattely parce in into a an integer.
        int start = getStartOfNum(adX, lines[adY]);
        int numId = (y + yDir) * currLine.length + start;
        if (savedNums.contains(numId)) {
          continue;
        } else {
          savedNums.add(numId);
        }

        int end = getEndOfNum(adX, lines[adY]);

        res.add(Integer.valueOf(String.valueOf(lines[adY], start, end - start)));

        if (end - 1 > adX && (adY == 0 || adY == 2)) {
          continue ForY;
        }
      }
    }

    return res;
  }

  private static boolean isOutOfBounds(char[] currLine, int yDir, int xDir, int adY, int adX) {
    return (yDir == 0 && xDir == 0)
        || adY < 0
        || adX < 0
        || adY >= currLine.length
        || adX >= currLine.length;
  }

  private static boolean isDigit(char c) {
    return c > 47 && c < 58;
  }

  private static int getStartOfNum(int x, char[] chars) {
    if (x < 0 || x >= chars.length || chars == null) {
      throw new IllegalArgumentException();
    }

    for (int i = x - 1; i >= 0; i--) {
      if (!isDigit(chars[i])) {
        return i + 1;
      }
    }

    return 0;
  }

  private static int getEndOfNum(int x, char[] chars) {
    if (x < 0 || x >= chars.length || chars == null) {
      throw new IllegalArgumentException();
    }

    for (int i = x + 1; i < chars.length; i++) {
      if (!isDigit(chars[i])) {
        return i;
      }
    }

    return chars.length;
  }
}
