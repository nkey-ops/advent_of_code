package day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
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
 *
 * --- Part Two ---
 *
 * Each cycle tilts the platform four times so that the rounded rocks roll north, then west,
 * then south, then east. After each tilt, the rounded rocks roll as far as they can before
 * othe platform tilts in the next direction. After one cycle, the platform will have finished
 * rolling the rounded rocks in those four directions in that order.
 *
 * Here's what happens in the example above after each of the first few cycles:
 *
 * After 1 cycle:  After 2 cycles:  After 3 cycles:
 * .....#....      .....#....       .....#....
 * ....#...O#      ....#...O#       ....#...O#
 * ...OO##...      .....##...       .....##...
 * .OO#......      ..O#......       ..O#......
 * .....OOO#.      .....OOO#.       .....OOO#.
 * .O#...O#.#      .O#...O#.#       .O#...O#.#
 * ....O#....      ....O#...O       ....O#...O
 * ......OOOO      .......OOO       .......OOO
 * #...O###..      #..OO###..       #...O###.O
 * #..OO#....      #.OOO#...O       #.OOO#...O
 *
 * This process should work if you leave it running long enough, but you're still worried about
 * the north support beams. To make sure they'll survive for a while, you need to calculate
 * the total load on the north support beams after 1000000000 cycles.
 *
 * In the above example, after 1000000000 cycles, the total load on the north support beams is 64.
 *
 * Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the north
 * support beams?
 *
 * --- Part Two ---
 * Each cycle tilts the platform four times so that the rounded rocks roll north,
 * then west, then south, then east. After each tilt, the rounded rocks roll as far as
 * they can before the platform tilts in the next direction. After one cycle,
 * the platform will have finished rolling the rounded rocks in those four directions
 * in that order.
 *
 * This process should work if you leave it running long enough, but you're still worried
 * about the north support beams. To make sure they'll survive for a while, you need to
 * calculate the total load on the north support beams after 1000000000 cycles.
 *
 * In the above example, after 1000000000 cycles, the total load on the north support beams is 64.
 *
 * Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the north
 * support beams?
 *
 * Your puzzle answer was 87700.
 * </pre>
 */
public class Day14 {

  public static void main(String[] args) {
    String path = "/advent_of_code/2023/day14";

    int firstStar = getFirstStar(path + "/test1.txt");
    System.out.println(firstStar);
    assert firstStar == 136;

    int firstStar2 = getFirstStar(path + "/input1.txt");
    System.out.println(firstStar2);
    assert firstStar2 == 106648;

    var secondStar = getSecondStar(path + "/test1.txt");
    System.out.println(secondStar);
    assert secondStar == 64;

    var secondStar2 = getSecondStar(path + "/input1.txt");
    System.out.println(secondStar2);
    assert secondStar2 == 87700;
  }

  private static int getFirstStar(String input) {
    char[][] platform = getPlatform(input);

    return calculateWithNorthTilt(platform);
  }

  private static int getSecondStar(String input) {
    char[][] charP = getPlatform(input);

    return getPlatformWeigthOnTheLastCycle(charP, 1_000_000_000);
  }

  /**
   * Returns data splited into lines from the {@code input}
   *
   * @param input file path to read data from
   * @return data splited into lines from the {@code input}
   */
  private static char[][] getPlatform(String input) {
    Objects.requireNonNull(input);

    try (BufferedReader bf = new BufferedReader(new FileReader(input))) {
      var lines = bf.lines().toArray(String[]::new);
      char[][] platform = new char[lines.length][];

      for (int i = 0; i < platform.length; i++) {
        platform[i] = lines[i].toCharArray();
      }
      return platform;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Calculates the weight of the {@code platform} after tilting it north side
   *
   * @param platform to tilt and calculate the weight of
   * @return the weight of the platform
   */
  private static int calculateWithNorthTilt(char[][] platform) {
    Objects.requireNonNull(platform);
    if (platform.length == 0) throw new IllegalArgumentException();

    int width = platform[0].length;
    int[] spaces = new int[width];

    int weight = 0;
    for (int i = 0; i < platform.length; i++) {
      char[] line = platform[i];

      Objects.requireNonNull(line);
      if (line.length != width) {
        throw new IllegalArgumentException();
      }

      for (int j = 0; j < width; j++) {
        char ch = line[j];

        switch (ch) {
          case 'O' -> {
            // Calculationg the rocks's weight
            weight = Math.addExact(weight, platform.length - (i - spaces[j]));
          }
          case '#' -> {
            spaces[j] = 0;
          }
          case '.' -> {
            spaces[j]++;
          }
          default -> throw new IllegalArgumentException();
        }
      }
    }

    return weight;
  }

  /**
   *
   *
   * <pre>
   * Calculates the weight of the platform after {@code cycles} of 4 north, west, south, east
   * tilts.
   * It's done by repeatedly performing the cycle of 4 tilts then storing the state of the
   * platform.
   * After the platform wast tilted 4 times using cashed history of all the cycles starting from
   * the most recent one, compare the current platform with others.
   *  If match was found:
   *
   *     If platform before didn't have a match then:
   *        This platforms is a starting point of potential repeated cycle, store its index as
   *        platform that starts a repeated cycle, and store index of the current platform that
   *        ends the repeated cycle, and add the current platform to the cache list,
   *        increment the platform match count.
   *
   *     Else the match is the next platform after the platform that was a match of the platform
   *     before the current one:
   *        add this platform to the cache list and increment the match count.
   *
   *  Else match wasn't found:
   *    Wipe the stored indexes of the first platform that forms a repeated cycle, and the platform
   *    that ends it, add the platform to the cache list and wipe the match count.
   *
   * After a certain number of cycles a matched platform will be the before the platform that ends
   * the repeated cycle. Use index of the platform the starts the cycle + 1 and the match count to
   * calculate the weight of the platform after {@code cycles}
   *
   * </pre>
   *
   * @param platform to calculate the weight of after {@code cycles}
   * @return weight of the platform after {@code cycles}
   */
  private static int getPlatformWeigthOnTheLastCycle(char[][] platform, int cycles) {
    Objects.requireNonNull(platform);
    if (platform.length == 0 || cycles < 1) {
      throw new IllegalArgumentException();
    }

    boolean isFirstMatch = true;
    int cycleStartIndex = -1;
    int cyclEndIndex = -1;
    int matchCounter = 0;

    char[][][] cachedPlatforms = new char[1000][][];
    int cachedPlatformsSize = 0;

    for (int i = 0; i < 1_000; i++) {
      tiltVertically(platform, true);
      tiltHorizontally(platform, false);
      tiltVertically(platform, false);
      tiltHorizontally(platform, true);

      if (isFirstMatch) {
        int matchIndex = getNearestMatch(cachedPlatforms, cachedPlatformsSize, platform);

        if (matchIndex != -1) {
          isFirstMatch = false;
          cycleStartIndex = matchIndex;
          cyclEndIndex = cachedPlatformsSize;
          matchCounter++;
        }
      } else if (matches(platform, cachedPlatforms[cycleStartIndex + matchCounter - 1])) {
        // if current platform matches the platform that is before the index of the first
        // platform of the second repeat cycle --> we fount repeated cycle
        if (cycleStartIndex + matchCounter == cyclEndIndex) {
          break;
        }

        matchCounter++;
      } else {
        int matchIndex = getNearestMatch(cachedPlatforms, cachedPlatformsSize, platform);

        if (matchIndex == -1) {
          isFirstMatch = true;
          cycleStartIndex = -1;
          cyclEndIndex = -1;
          matchCounter = 0;
        } else {
          cycleStartIndex = matchIndex;
          cyclEndIndex = cachedPlatformsSize;
          matchCounter++;
        }
      }

      cachedPlatforms[cachedPlatformsSize] = copy(platform);
      cachedPlatformsSize++;
    }

    if (matchCounter == 0) return -1;

    int lastPlatformIndex = getLastPlatformIndex(cycleStartIndex + 1, matchCounter);
    return calculateLoad(cachedPlatforms[lastPlatformIndex]);
  }

  /**
   * Compares an array of {@code cachedPlatforms} to find a match and return its index Comparison is
   * started from {@code cachedPlatformSize} and till the begging.
   *
   * @param cachedPlatforms find find the match
   * @param cachedPlatformsSize starting point to start match at
   * @param platform to find match of it
   * @return index of the match
   */
  private static int getNearestMatch(
      char[][][] cachedPlatforms, int cachedPlatformsSize, char[][] platform) {
    Objects.requireNonNull(cachedPlatforms);
    Objects.requireNonNull(platform);

    if (cachedPlatformsSize < 0 || cachedPlatformsSize > cachedPlatforms.length) {
      throw new IllegalArgumentException();
    }

    for (int i = cachedPlatformsSize - 1; i >= 0; i--) {
      if (matches(cachedPlatforms[i], platform)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Compares to matrix using {@link Arrays#compare(char[], char[])}
   *
   * @param p1 matrix to compare
   * @param p2 matrix to compare
   * @return whether to matrixes are equal
   */
  private static boolean matches(char[][] p1, char[][] p2) {
    Objects.requireNonNull(p1);
    Objects.requireNonNull(p2);

    if (p1.length != p2.length) {
      throw new IllegalArgumentException();
    }

    for (int i = 0; i < p2.length; i++) {
      if (Arrays.compare(p1[i], p2[i]) != 0) {
        return false;
      }
    }
    return true;
  }

  private static void tiltHorizontally(char[][] platform, boolean isRight) {
    var width = platform[0].length;
    int[] spaces = new int[platform.length];

    int start = isRight ? width - 1 : 0;
    int end = isRight ? -1 : width;
    int inc = isRight ? -1 : 1;

    for (; isRight ? start > end : start < end; start += inc) {

      for (int i = 0; i < platform.length; i++) {
        char ch = platform[i][start];

        switch (ch) {
          case 'O' -> {
            if (spaces[i] != 0) {
              platform[i][isRight ? start + spaces[i] : start - spaces[i]] = 'O';
              platform[i][start] = '.';
            }
          }
          case '#' -> {
            spaces[i] = 0;
          }
          case '.' -> {
            spaces[i]++;
          }
          default -> throw new IllegalArgumentException();
        }
      }
    }
  }

  private static void tiltVertically(char[][] platform, boolean isUp) {
    var width = platform[0].length;
    int[] spaces = new int[width];

    int start = isUp ? 0 : platform.length - 1;
    int end = isUp ? platform.length : -1;
    int inc = isUp ? 1 : -1;

    for (; isUp ? start < end : start > end; start += inc) {
      char[] line = platform[start];

      if (line == null || line.length != width) {
        throw new IllegalArgumentException();
      }

      for (int j = 0; j < line.length; j++) {
        char ch = line[j];

        switch (ch) {
          case 'O' -> {
            if (spaces[j] != 0) {

              platform[isUp ? start - spaces[j] : start + spaces[j]][j] = 'O';

              line[j] = '.';
            }
          }
          case '#' -> {
            spaces[j] = 0;
          }
          case '.' -> {
            spaces[j]++;
          }
          default -> throw new IllegalArgumentException();
        }
      }
    }
  }

  private static int getLastPlatformIndex(int start, int length) {
    int iterations = 1_000_000_000;
    if (start < 0 || length <= 0 || start > iterations) {
      throw new IllegalArgumentException();
    }

    iterations -= start - 1;

    int reminder = iterations % length;
    return reminder + start - 2;
  }

  private static char[][] copy(char[][] platform) {
    char[][] copy = new char[platform.length][];

    for (int i = 0; i < copy.length; i++) {
      copy[i] = platform[i].clone();
    }

    return copy;
  }

  private static int calculateLoad(char[][] platform) {
    Objects.requireNonNull(platform);
    if (platform.length == 0) {
      return 0;
    }
    Objects.requireNonNull(platform[0]);

    int width = platform[0].length;
    int sum = 0;
    for (int i = 0; i < platform.length; i++) {
      var line = platform[i];
      Objects.requireNonNull(line);

      if (line.length != width) {
        throw new IllegalArgumentException();
      }

      for (int j = 0; j < width; j++) {
        if (line[j] == 'O') {
          sum = Math.addExact(sum, platform.length - i);
        }
      }
    }

    return sum;
  }
}
