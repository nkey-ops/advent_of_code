package day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * --- Part One ---
 * The newly-improved calibration document consists of lines of text; 
 * each line originally contained a specific calibration value that the 
 * Elves now need to recover. 
 * On each line, the calibration value can be found by combining the first 
 * digit and the last digit (in that order) to form a single two-digit number.
 *
 * For example:
 * 1abc2
 * pqr3stu8vwx
 * a1b2c3d4e5f
 * treb7uchet
 *
 * In this example, the calibration values of these four lines are 12, 38, 15, and 77. 
 * Adding these together produces 142.
 *
 * Consider your entire calibration document. What is the sum of all of the calibration values?
 * Your puzzle answer was 54573.
 *
 * --- Part Two ---
 *
 * Your calculation isn't quite right. It looks like some of the digits 
 * are actually spelled out with letters: 
 * one, two, three, four, five, six, seven, eight, and nine 
 * also count as valid "digits".
 *
 * Equipped with this new information, you now need to find 
 * the real first and last digit on each line. 
 *
 * For example:
 * two1nine 
 * eightwothree
 * abcone2threexyz
 * xtwone3four
 * 4nineeightseven2
 * zoneight234
 * 7pqrstsixteen
 *
 * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. 
 * Adding these together produces 281.
 *
 * What is the sum of all of the calibration values?
 * Your puzzle answer was 54591.
 */
public class Day1 {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    String inputName =
        "/input1.txt";

    int v1 = getSumFirstStar(inputName);
    int v2 = getSumSecondStar(inputName);

    System.out.println("RESULTS!!! " + v1 + " " + v2 );
  }

  private static int getSumFirstStar(String inputName) throws IOException, FileNotFoundException {
    int c = 0;
    try (BufferedReader bf = new BufferedReader(new FileReader(new File(inputName)))) {
      while (bf.ready()) {
        char[] chars = bf.readLine().toCharArray();
        char a = 'n', b = 'n';

        for (int i = 0; i < chars.length; i++) {
          if (chars[i] >= 48 && chars[i] <= 57) {
            if (a == 'n') {
              a = b = chars[i];
            } else {
              b = chars[i];
            }
          }
        }

        c += Integer.valueOf(a + "" + b);
      }
    }
    return c;
  }

  private static int getSumSecondStar(String inputName) throws IOException, FileNotFoundException {
    Map<String, Integer> wtod =
        Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9);

    int c = 0;
    try (BufferedReader bf = new BufferedReader(new FileReader(new File(inputName)))) {
      while (bf.ready()) {
        String line = bf.readLine();
        int sStart = -1, sEnd = -1;
        int ssNum = -1, seNum = -1;

        int sInd, eInd;
        for (Map.Entry<String, Integer> entry : wtod.entrySet()) {
          sInd = line.indexOf(entry.getKey());
          eInd = line.lastIndexOf(entry.getKey());

          if ((sStart > sInd || sStart == -1) && sInd != -1) {
            sStart = sInd;
            ssNum = entry.getValue();
          }
          if (sEnd < eInd) {

            sEnd = eInd;
            seNum = entry.getValue();
          }
        }

        char[] chars = line.toCharArray();
        int chStart = -1, chEnd = -1;

        for (int i = 0; i < chars.length; i++) {
          if (chars[i] >= 48 && chars[i] <= 57) {
            if (chStart == -1) {
              chStart = chEnd = i;
            } else {
              chEnd = i;
            }
          }
        }

        int sNum = ssNum;
        if (sStart == -1 || (sStart != -1 && chStart != -1 && sStart > chStart)) {
          sNum = Character.getNumericValue(chars[chStart]);
        }

        int eNum = seNum;
        if (sEnd == -1 || (eNum != -1 && chEnd != -1 && sEnd < chEnd)) {
          eNum = Character.getNumericValue(chars[chEnd]);
        }

        c += Integer.valueOf(sNum + "" + eNum);
      }
    }
    return c;
  }
}
