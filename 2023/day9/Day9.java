package day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * <pre>
 * --- Day 9: Mirage Maintenance ---
 * https://adventofcode.com/2023/day/9
 *
 * -- First Star --
 * You pull out your handy Oasis And Sand Instability Sensor and analyze your surroundings.
 * The OASIS produces a report of many values and how they are changing over time
 * (your puzzle input). Each line in the report contains the history of a single value.
 *
 * For example:
 *
 *    0 3 6 9 12 15
 *    1 3 6 10 15 21
 *    10 13 16 21 30 45
 *
 *
 * In the above dataset, the first history is 0 3 6 9 12 15.
 * Because the values increase by 3 each step, the first sequence of differences that
 * you generate will be 3 3 3 3 3. Note that this sequence has one fewer value than the
 * input sequence because at each step it considers two numbers from the input.
 * Since these values aren't all zero, repeat the process: the values differ by 0 at each step,
 * so the next sequence is 0 0 0 0. This means you have enough information to extrapolate
 * the history!
 * Visually, these sequences can be arranged like this:
 *
 *    0   3   6   9  12  15
 *      3   3   3   3   3
 *       0   0   0   0
 *
 * To extrapolate, start by adding a new zero to the end of your list of zeroes;
 * because the zeroes represent differences between the two values above them,
 * this also means there is now a placeholder in every sequence above it:
 *
 *     0   3   6   9  12  15   B
 *       3   3   3   3   3   A
 *         0   0   0   0   0
 *
 * You can then start filling in placeholders from the bottom up.
 * A needs to be the result of increasing 3 (the value to its left) by 0 (the value below it);
 * this means A must be 3:
 *
 *     0   3   6   9  12  15   B
 *       3   3   3   3   3   3
 *         0   0   0   0   0
 *
 * Finally, you can fill in B, which needs to be the result of increasing 15
 * (the value to its left) by 3 (the value below it), or 18:
 *
 *    0   3   6   9  12  15
 *      3   3   3   3   3
 *        0   0   0   0
 *
 * So, the next value of the first history is 18.
 *
 *    1   3   6  10  15  21  28
 *      2   3   4   5   6   7
 *        1   1   1   1   1
 *          0   0   0   0
 *
 * So, the next value of the second history is 28.
 *
 *    10  13  16  21  30  45  68
 *      3   3   5   9  15  23
 *        0   2   4   6   8
 *          2   2   2   2
 *            0   0   0
 *
 * So, the next value of the third history is 68
 * If you find the next value for each history in this example and add them together, you get 114.
 *
 * Analyze your OASIS report and extrapolate the next value for each history.
 * What is the sum of these extrapolated values?
 *
 * Your puzzle answer was 2174807968.
 *
 * --- Part Two ---
 *
 * For each history, repeat the process of finding differences until the sequence
 * of differences is entirely zero. Then, rather than adding a zero to the end and filling
 * in the next values of each previous sequence, you should instead add a zero to
 * the beginning of your sequence of zeroes, then fill in new first values for each
 * previous sequence.
 *
 * In particular, here is what the third example history looks like when extrapolating back in time:
 *
 *   5  10  13  16  21  30  45
 *     5   3   3   5   9  15
 *      -2   0   2   4   6
 *         2   2   2   2
 *           0   0   0
 * Adding the new values on the left side of each sequence from bottom to top eventually
 * reveals the new left-most history value: 5.
 *
 * Doing this for the remaining example data above results in previous values of -3 for
 * the first history and 0 for the second history. Adding all three new values together produces 2.
 *
 * Analyze your OASIS report again, this time extrapolating the previous value for each history.
 * What is the sum of these extrapolated values?
 *
 * Your puzzle answer was 1208.
 *
 *
 * </pre>
 */
public class Day9 {
  static boolean isLogged = false;

  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day9";

    System.out.println(getFirstOrSecondStar(path + "/test1.txt", true));
    System.out.println(getFirstOrSecondStar(path + "/input1.txt", true));
    System.out.println(getFirstOrSecondStar(path + "/test1.txt", false));
    System.out.println(getFirstOrSecondStar(path + "/input1.txt", false));
  }

  /**
   *
   * <pre>
   * ** Pseudo Code **
   *
   * int[][] histories
   *
   * iterate over the file
   * String line = file.getLine()
   * histories[] = splitIntoHistory(inputLine);
   *
   * int sumOfPredictionValues = 0
   * for history : histories
   * sumOfPredictionValues += getPrediciontOfTheNextValue(history)
   *
   *
   * int[] splitIntoHistory(inputLine)
   *  String[] sHistoryValues = inputLine.split("\s");
   *  int[] historyValues = new int[sHistoryValues.length];
   *
   *  int i = 0;
   *  for sHistoryValue : sHistoryValues
   *  historyValues[i++] = parseInt(sHistoryValue)
   *
   * return historyValues
   *
   * getPredictionOfTheNextValue(int[] h)
   *  int[] diff = new int[h.length]
   *  boolea isAllZeros = true
   *
   *  for(int i = 1; i < h.length; i++)
   *  diff[i - 1] = h[i] - [i - 1]
   *  if(diff[i - 1] != 0) isAllZeros = false
   *
   *  if(isAllZeros) return h[h.length - 1]
   *
   * return h[h.length -1] + getPredictionOfTheNextValue(diff)
   *
   * </pre>
   */
  private static long getFirstOrSecondStar(String inputName, boolean isFirstStar)
      throws IOException {
    List<String> lines = null;

    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      lines = bf.lines().toList();
    }

    long sumOfPredictionValues = 0;
    for (String line : lines) {
      int[] history = splitIntoHistory(line);
      long prediction = getPrediction(history, !isFirstStar);

      logRow(!isFirstStar, history, prediction);
      if (isLogged) { System.out.println(); }

      sumOfPredictionValues += prediction;
    }

    return sumOfPredictionValues;
  }

  /**
   * Parses the line into array of digints.
   *
   * @param line to be parse
   * @return array of digits parsed from the line
   */
  private static int[] splitIntoHistory(String line) {
    Objects.requireNonNull(line);

    String[] strHistoryValues = line.split("\s");
    int[] historyValues = new int[strHistoryValues.length];

    for (int i = 0; i < strHistoryValues.length; i++) {
      historyValues[i] = Integer.parseInt(strHistoryValues[i]);
    }

    return historyValues;
  }

  /**
   *
   *
   * <pre>
   * Calculates the prediction of the next value from the
   * start or end depending on the {@code isAtStart} parameter by
   *
   * finding the differences between two close values and
   * passing them  as an array in recursive call
   * until the differences between the values of the row are the same
   * and returning any digiit from the current row  (etc. {@code history})i - they are the same;
   *
   * on the way back from recurcsive calls the following happens ;
   *
   *  Depending on {@code isAtStart} whether it is
   *  true - from the first value of the current row (etc. {@code history}) returned prediction
   *    will be subtracted and the result returned.
   *  false - the last value of the current row (etc. {@code history}) added o returned prediction
   *    and the result returned.
   *
   * </pre>
   *
   * @param history calculate the prediction from
   * @param isAtStart choose the side of prediction if true it is a start of the row, if false the
   *     end of it
   * @note you never know when an integer overflow can occur
   */
  private static long getPrediction(int[] history, boolean isAtStart) {
    Objects.requireNonNull(history);
    if (history.length == 0) {
      throw new IllegalArgumentException("History is empty");
    }

    int[] differences = new int[history.length - 1];
    boolean isAllZeros = true;

    for (int i = 1; i < history.length; i++) {
      differences[i - 1] = history[i] - history[i - 1];
      if (differences[i - 1] != 0) {
        isAllZeros = false;
      }
    }

    assert history.length != 1;

    if (isAllZeros) {
      return history[0]; // doesn't matter wheather first value or last they're the same
    }

    long prediction = getPrediction(differences, isAtStart);
    logRow(isAtStart, differences, prediction);

    return isAtStart ? history[0] - prediction : history[history.length - 1] + prediction;
  }

  private static void logRow(boolean isAtStart, int[] differences, long prediction) {
    if (!isLogged) {
      return;
    }

    System.out.println(
        isAtStart
            ? prediction + " <| " + Arrays.toString(differences)
            : Arrays.toString(differences) + " |> " + prediction);
  }
}
