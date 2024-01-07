package day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Day13
 *
 * <pre>
 *
 * --- Day 13: Point of Incidence ---
 * https://adventofcode.com/2023/day/13
 *
 * You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input);
 * perhaps by carefully analyzing these patterns, you can figure out where the mirrors are!
 *
 * For example:
 *
 *  #.##..##.
 *  ..#.##.#.
 *  ##......#
 *  ##......#
 *  ..#.##.#.
 *  ..##..##.
 *  #.#.##.#.
 *
 *  #...##..#
 *  #....#..#
 *  ..##..###
 *  #####.##.
 *  #####.##.
 *  ..##..###
 *  #....#..#
 *
 * To find the reflection in each pattern, you need to find a perfect reflection across either
 * a horizontal line between two rows or across a vertical line between two columns.
 *
 * In the first pattern, the reflection is across a vertical line between two columns;
 * arrows on each of the two columns point at the line between the columns:
 *
 *  123456789
 *      ><
 *  #.##..##.
 *  ..#.##.#.
 *  ##......#
 *  ##......#
 *  ..#.##.#.
 *  ..##..##.
 *  #.#.##.#.
 *      ><
 *  123456789
 *
 * In this pattern, the line of reflection is the vertical line between columns 5 and 6.
 * Because the vertical line is not perfectly in the middle of the pattern,
 * part of the pattern (column 1) has nowhere to reflect onto and can be ignored; every other
 * column has a reflected column within the pattern and must match exactly:
 * column 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.
 *
 * The second pattern reflects across a horizontal line instead:
 *
 *  1 #...##..# 1
 *  2 #....#..# 2
 *  3 ..##..### 3
 *  4v#####.##.v4
 *  5^#####.##.^5
 *  6 ..##..### 6
 *  7 #....#..# 7
 *
 * This pattern reflects across the horizontal line between rows 4 and 5.
 * Row 1 would reflect with a hypothetical row 8, but since that's not in the pattern,
 * row 1 doesn't need to match anything. The remaining rows match: row 2 matches row 7,
 * row 3 matches row 6, and row 4 matches row 5.
 *
 * To summarize your pattern notes, add up the number of columns to the left of each
 * vertical line of reflection; to that, also add 100 multiplied by the number of rows above
 * each horizontal line of reflection. In the above example, the first pattern's vertical line
 * has 5 columns to its left and the second pattern's horizontal line has 4 rows above it,
 * a total of 405.
 *
 * Find the line of reflection in each of the patterns in your notes.
 * What number do you get after summarizing all of your notes?
 *
 * Your puzzle answer was 34993.
 *
 *
 * --- Part Two ---
 *
 * Upon closer inspection, you discover that every mirror has exactly one smudge:
 * exactly one . or # should be the opposite type.
 *
 * In each pattern, you'll need to locate and fix the smudge that causes a different
 * reflection line to be valid. (The old reflection line won't necessarily continue
 * being valid after the smudge is fixed.)
 *
 * Here's the above example again:
 *
 *    #.##..##.
 *    ..#.##.#.
 *    ##......#
 *    ##......#
 *    ..#.##.#.
 *    ..##..##.
 *    #.#.##.#.
 *
 *    #...##..#
 *    #....#..#
 *    ..##..###
 *    #####.##.
 *    #####.##.
 *    ..##..###
 *    #....#..#
 *
 * The first pattern's smudge is in the top-left corner. If the top-left # were instead .,
 * it would have a different, horizontal line of reflection:
 *
 *    1 ..##..##. 1
 *    2 ..#.##.#. 2
 *    3v##......#v3
 *    4^##......#^4
 *    5 ..#.##.#. 5
 *    6 ..##..##. 6
 *    7 #.#.##.#. 7
 *
 * With the smudge in the top-left corner repaired, a new horizontal line of reflection
 * between rows 3 and 4 now exists. Row 7 has no corresponding reflected row and can be ignored,
 * but every other row matches exactly: row 1 matches row 6, row 2 matches row 5,
 * and row 3 matches row 4.
 *
 * In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:
 *
 *    1v#...##..#v1
 *    2^#...##..#^2
 *    3 ..##..### 3
 *    4 #####.##. 4
 *    5 #####.##. 5
 *    6 ..##..### 6
 *    7 #....#..# 7
 *
 * Now, the pattern has a different horizontal line of reflection between rows 1 and 2.
 *
 * Summarize your notes as before, but instead use the new different reflection lines.
 * In this example, the first pattern's new horizontal line has 3 rows above it and the
 * second pattern's new horizontal line has 1 row above it, summarizing to the value 400.
 *
 * In each pattern, fix the smudge and find the different line of reflection.
 * What number do you get after summarizing the new reflection line in each pattern in your notes?
 *
 * Your puzzle answer was 29341.
 *
 * </pre>
 */
public class Day13 {
  static boolean[] hasMirrow;

  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day13";

    System.out.println(getFirstStar(path + "/test1.txt"));
    System.out.println(getFirstStar(path + "/input1.txt"));

    System.out.println(getSecondStar(path + "/test1.txt"));
    System.out.println(getSecondStar(path + "/input1.txt"));
  }

  /**
   * @param input file path to read patterns from
   * @returns the sum of the horizontal mirrored lines above the mirror multiplied by 100 plus the
   *     sum of vertical mirrored lines to the left of the mirror.
   */
  private static int getFirstStar(String input) {
    String[][] patterns = getPatterns(input);

    return Math.addExact(
        Math.multiplyExact(100, countHorizontalMirrowedLines(patterns, false)),
        countVerticalMirrowedLines(patterns, false));
  }

  /**
   * @param input file path to read patterns from
   * @returns the sum of the horizontal mirrored lines above the mirror multiplied by 100 plus the
   *     sum of vertical mirrored lines to the left of the mirror.
   */
  private static long getSecondStar(String input) {
    String[][] patterns = getPatterns(input);

    return Math.addExact(
        Math.multiplyExact(100, countHorizontalMirrowedLines(patterns, true)),
        countVerticalMirrowedLines(patterns, true));
  }

  /**
   * Returns an array of patterns.
   *
   * @param input file path to read patterns from
   * @return returns an array of patterns.
   */
  private static String[][] getPatterns(String input) {
    Objects.requireNonNull(input);
    List<String[]> patterns = new ArrayList<>();

    try (BufferedReader bf = new BufferedReader(new FileReader(input))) {

      List<String> pattern = new ArrayList<>();
      while (bf.ready()) {
        String line = bf.readLine();

        if (line.isBlank()) {
          patterns.add(pattern.toArray(new String[pattern.size()]));
          pattern = new ArrayList<>();
        } else {
          pattern.add(line);
        }
      }

      patterns.add(pattern.toArray(new String[pattern.size()]));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return patterns.toArray(new String[patterns.size()][]);
  }

  /**
   * Counts and sums all the mirrored horizontal lines above the mirror.
   *
   * @param isBroken if the mirror has a smudge
   * @param patterns an array of patterns to count the mirrored lines from.
   * @return the sum of all the mirrored horizontal lines above the mirror.
   */
  private static int countHorizontalMirrowedLines(String[][] patterns, boolean isBroken) {
    Objects.requireNonNull(patterns);

    int sum = 0;
    for (String[] pattern : patterns) {
      Objects.requireNonNull(pattern);
      sum =
          Math.addExact(
              sum,
              isBroken
                  ? countHorizontalMirrowedLines(pattern, 0)
                  : countHorizontalMirrowedLines(pattern));
    }

    return sum;
  }

  /**
   * Counts and sums all the mirrored vertical lines to the left of the mirror.
   *
   * @param isBroken if the mirror has a smudge
   * @param patterns an array of patterns to count the mirrored lines from.
   * @return the sum of all the mirrored vertical lines to the left of the mirror.
   */
  private static int countVerticalMirrowedLines(String[][] patterns, boolean isBroken) {
    Objects.requireNonNull(patterns);

    int sum = 0;
    for (String[] pattern : patterns) {
      Objects.requireNonNull(pattern);

      sum =
          Math.addExact(
              sum,
              isBroken
                  ? countVerticalMirrowedLines4(pattern, 0)
                  : countVerticalMirrowedLines(pattern));
    }

    return sum;
  }

  /**
   * Counts mirrored horizontal lines above the mirror if present.
   *
   * @param pattern to count lines from.
   * @return number of horizontal lines above the mirror if present if not returns 0.
   */
  private static int countHorizontalMirrowedLines(String[] pattern) {
    Objects.requireNonNull(pattern);
    if (pattern.length < 2) throw new IllegalArgumentException();

    int mirrowId = -1;
    for (int i = 1; i < pattern.length; i++) {
      String line = pattern[i];

      if (line == null || line.isBlank() || line.length() != pattern[0].length()) {
        throw new IllegalArgumentException();
      }

      if (isMirrorFound(mirrowId)) { // check the previous line
        if (line.equals(pattern[i - 1])) {
          mirrowId = i - 1;
        }
      } else { // check the mirrored line
        int mirrowedLineInd = mirrowId - (i - mirrowId - 1);

        if (mirrowedLineInd < 0) break;
        else if (!line.equals(pattern[mirrowedLineInd])) {
          mirrowId = -1;
        }
      }
    }

    return mirrowId + 1;
  }

  /**
   * Counts mirrored horizontal lines above the mirror (if present and if the mirror has a 1
   * smudge).
   *
   * @param pattern to count lines from.
   * @param startAt zero based position to start parsing pattern from.
   * @return number of horizontal lines above the mirror if present, if not returns 0.
   */
  private static int countHorizontalMirrowedLines(String[] pattern, int startAt) {
    Objects.requireNonNull(pattern);

    if (pattern.length < 2 || startAt < 0 || startAt >= pattern.length)
      throw new IllegalArgumentException();

    int mirrowId = -1;
    boolean isSmudgeFixed = false;

    for (int i = startAt + 1; i < pattern.length; i++) {
      String line = pattern[i];

      if (line == null || line.isBlank() || line.length() != pattern[0].length()) {
        throw new IllegalArgumentException();
      }

      if (isMirrorFound(mirrowId)) { // check previous line
        int difference = differentBy(line, pattern[i - 1]);
        if (difference > 1) continue;

        if (difference == 1) isSmudgeFixed = true;
        mirrowId = i - 1;

      } else { // check the mirrored line

        int mirrowedLineInd = mirrowId - (i - mirrowId - 1);
        if (mirrowedLineInd < 0) break;

        int difference = differentBy(line, pattern[mirrowedLineInd]);

        if (difference == 1 && !isSmudgeFixed) {
          isSmudgeFixed = true;

        } else if (difference != 0) {
          isSmudgeFixed = false;
          mirrowId = -1;
        }
      }
    }

    if (mirrowId != -1 && mirrowId < pattern.length && !isSmudgeFixed) {
      return countHorizontalMirrowedLines(pattern, mirrowId + 1);
    } else {
      return isSmudgeFixed ? mirrowId + 1 : 0;
    }
  }

  /**
   * Counts mirrored vertical lines to the left of the mirror if present.
   *
   * @param pattern to count lines from.
   * @return number of vertical lines to the left of the mirror if present, if not returns 0.
   */
  private static int countVerticalMirrowedLines(String[] pattern) {
    Objects.requireNonNull(pattern);
    if (pattern.length < 2) throw new IllegalArgumentException();

    int height = pattern.length;
    int width = pattern[0].length();

    int mirrowId = -1;

    for (int w = 1; w < width; w++) {

      boolean isTheSame = true;
      for (int h = 0; h < height; h++) {
        char ch = pattern[h].charAt(w);

        if (isMirrorFound(mirrowId)) {
          if (pattern[h].charAt(w - 1) != ch) {
            isTheSame = false;
          }
        } else {
          int mirrowedLine = mirrowId - (w - mirrowId - 1);

          if (mirrowedLine < 0) break;
          else if (pattern[h].charAt(mirrowedLine) != ch) {
            mirrowId = -1;
            isTheSame = false;
          }
        }
      }

      if (isMirrorFound(mirrowId) && isTheSame && w != 0) {
        mirrowId = w - 1;
      }
    }

    return mirrowId + 1;
  }

  /**
   * Counts mirrored vertical lines to the left of the mirror (if present and if the mirror has a 1
   * smudge).
   *
   * @param pattern to count lines from.
   * @param startAt zero based position to start parsing pattern from.
   * @return number of vertical lines to the left of the mirror if present, if not returns 0.
   */
  private static int countVerticalMirrowedLines4(String[] pattern, int startAt) {
    Objects.requireNonNull(pattern);

    if (pattern.length < 2 || startAt < 0 || startAt >= pattern[0].length()) {
      throw new IllegalArgumentException();
    }

    int height = pattern.length;
    int width = pattern[0].length();

    int mirrowId = -1;
    boolean isSmudgeFixed = false;
    WLable:
    for (int w = startAt + 1; w < width; w++) {

      for (int h = 0; h < height; h++) {
        char ch = pattern[h].charAt(w);

        if (isMirrorFound(mirrowId)) {

          if (pattern[h].charAt(w - 1) != ch) {
            if (!isSmudgeFixed) isSmudgeFixed = true;
            else {
              isSmudgeFixed = false;
              continue WLable;
            }
          }

        } else { // check mirrow line
          int mirrowedLine = mirrowId - (w - mirrowId - 1);

          if (mirrowedLine < 0) break;

          if (pattern[h].charAt(mirrowedLine) != ch) {

            if (isSmudgeFixed) {
              mirrowId = -1;
              isSmudgeFixed = false;
              continue WLable;
            } else {
              isSmudgeFixed = true;
            }
          }
        }
      }

      if (isMirrorFound(mirrowId)) {
        mirrowId = w - 1;
      }
    }

    if (mirrowId != -1 && mirrowId != width && !isSmudgeFixed) {
      return countVerticalMirrowedLines4(pattern, mirrowId + 1);
    } else {
      return isSmudgeFixed ? mirrowId + 1 : 0;
    }
  }

  private static boolean isMirrorFound(int mirrowId) {
    return mirrowId == -1;
  }

  /**
   * Return the number of different characters between two EQUAL-length strings.
   *
   * @param a string
   * @param b string
   * @return the number of different characters between two strings.
   */
  public static int differentBy(String a, String b) {
    Objects.requireNonNull(a);
    Objects.requireNonNull(b);

    if (a.length() != b.length()) throw new IllegalArgumentException();

    int difference = 0;
    for (int i = 0; i < a.length(); i++) {
      if (a.charAt(i) != b.charAt(i)) difference++;
    }

    return difference;
  }
}
