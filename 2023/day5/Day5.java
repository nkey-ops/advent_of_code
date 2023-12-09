package day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * <pre>
 * --- Day 5: If You Give A Seed A Fertilizer ---
 * https://adventofcode.com/2023/day/5
 *
 * --- First Star ---
 *
 * The almanac (your puzzle input) lists all of the seeds that need to be planted.
 * It also lists what type of soil to use with each kind of seed, what type of
 * fertilizer to use with each kind of soil, what type of water to use with
 * each kind of fertilizer, and so on. Every type of seed, soil, fertilizer
 * and so on is identified with a number, but numbers are reused by each
 * category - that is, soil 123 and fertilizer 123 aren't necessarily related to each other.
 *
 * For example:
 *  seeds: 79 14 55 13
 *
 *  seed-to-soil map:
 *  50 98 2
 *  52 50 48
 *
 *  soil-to-fertilizer map:
 *  0 15 37
 *  37 52 2
 *  39 0 15
 *
 *  fertilizer-to-water map:
 *  49 53 8
 *  0 11 42
 *  42 0 7
 *  57 7 4
 *
 *  water-to-light map:
 *  88 18 7
 *  18 25 70
 *
 *  light-to-temperature map:
 *  45 77 23
 *  81 45 19
 *  68 64 13
 *
 *  temperature-to-humidity map:
 *  0 69 1
 *  1 0 69
 *
 *  humidity-to-location map:
 *  60 56 37
 *  56 93 4
 *
 * The gardener and his team want to get started as soon as possible,
 * so they'd like to know the closest location that needs a seed.
 * Using these maps, find the lowest location number that corresponds
 * to any of the initial seeds. To do this, you'll need to convert each seed
 * number through other categories until you can find its corresponding location number.
 * In this example, the corresponding types are:
 *
 * - Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
 * - Seed 14, soil 14, fertilizer 53, water 49, light 42, temperature 42, humidity 43, location 43.
 * - Seed 55, soil 57, fertilizer 57, water 53, light 46, temperature 82, humidity 82, location 86.
 * - Seed 13, soil 13, fertilizer 52, water 41, light 34, temperature 34, humidity 35, location 35.
 *
 * So, the lowest location number in this example is 35.
 *
 * Your puzzle answer was 111627841.
 *
 * --- Part Two ---
 *
 * The values on the initial seeds: line come in pairs. Within each pair,
 * the first value is the start of the range and the second value is the length of the range.
 * So, in the first line of the example above:
 *
 * seeds: 79 14 55 13
 * This line describes two ranges of seed numbers to be planted in the garden. 
 * The first range starts with seed number 79 and contains 14 values: 79, 80, ..., 91, 92. 
 * The second range starts with seed number 55 and contains 13 values: 55, 56, ..., 66, 67.
 *
 * Now, rather than considering four seed numbers, you need to consider a total of 27 seed numbers.
 *
 * In the above example, the lowest location number can be obtained from seed number 82, 
 * which corresponds to soil 84, fertilizer 84, water 84, light 77, temperature 45, 
 * humidity 46, and location 46. So, the lowest location number is 46.
 *
 * Consider all of the initial seed numbers listed in the ranges on the 
 * first line of the almanac. What is the lowest location number that 
 * corresponds to any of the initial seed numbers?
 *
 * Your puzzle answer was 69323688.
 *
 * </pre>
 */
public class Day5 {

  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day5/";

    System.out.println(getFirstStar(path + "/test1.txt"));
    System.out.println(getFirstStar(path + "/input1.txt"));
    System.out.println(getSecondStar(path + "/test1.txt"));
    System.out.println(getSecondStar(path + "/input1.txt"));
  }

  /**
   *
   *
   * <pre>
   *  Goes through each line of the file.
   *  If it's the fist line (will look like "seeds:{\\s\\d+}+")
   *    split it into an array of long "source values" skipping "seeds:" word
   *
   *  If it's a mapping (".+?map:")
   *    extract all the ranges that look like ("destination start index,
   *                                            convertion range start index,
   *                                            length of the convertion range")
   *    to a "ranges" list up to the blank line;
   *    Use ranges to update the list of the "source values"
   * </pre>
   *
   * @return the smallest value inside of srcValues array
   */
  private static long getFirstStar(String inputName) throws IOException {
    long[] srcValues = null;
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      boolean isFirstLineRead = false;
      while (bf.ready()) {
        String line = bf.readLine();
        if (!isFirstLineRead) {
          srcValues =
              Arrays.stream(line.split("\\s+"))
                  .skip(1)
                  .mapToLong(i -> Long.parseLong((i)))
                  .toArray();
          isFirstLineRead = true;

        } else if (line.matches(".+?map:")) {

          List<Long[]> ranges = new ArrayList<>();
          while (bf.ready() && !(line = bf.readLine()).isEmpty()) {
            ranges.add(getRange(line));
          }

          updateDestinations(srcValues, ranges);
        }
      }
    }

    return srcValues[0];
  }

  /**
   *
   *
   * <pre>
   *  Goes through each line of the file.
   *  If it's the fist line (will look like "seeds:{\\s\\d+\\s\\d+}+")
   *    split it into a {@link List} of {@link Long} "source ranges of values"
   *    skipping "seeds:" word
   *
   *  If it's a mapping (".+?map:")
   *    extract all the ranges that look like ("destination start index,
   *                                            convertion range start index,
   *                                            length of the convertion range")
   *    to a "ranges" list up to the blank line;
   *    Use ranges to update the list of the "source range of values"
   * </pre>
   */
  private static Long getSecondStar(String inputName) throws IOException {
    List<Long[]> srcRangesOfValues = null;
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      boolean isFirstLineRead = false;
      while (bf.ready()) {
        String line = bf.readLine();

        if (!isFirstLineRead) {
          srcRangesOfValues = getRanges(line);
          isFirstLineRead = true;

        } else if (line.matches(".+?map:")) {
          List<Long[]> ranges = new ArrayList<>();
          while (bf.ready() && !(line = bf.readLine()).isEmpty()) {
            ranges.add(getRange(line));
          }

          updateDestinations(srcRangesOfValues, ranges);
        }
      }
    }

    return srcRangesOfValues.get(0)[0];
  }

  private static List<Long[]> getRanges(String line) {
    Objects.requireNonNull(line);

    String[] strRanges = line.split("\\s+");

    List<Long[]> result = new ArrayList<>();
    for (int i = 1; i < strRanges.length; i += 2) {
      result.add(new Long[] {Long.parseLong(strRanges[i]), Long.parseLong(strRanges[i + 1])});
    }
    return result;
  }

  private static void updateDestinations(long[] srcValues, List<Long[]> ranges) {
    Objects.requireNonNull(ranges);

    int smInd = 0;
    for (int i = 0; i < srcValues.length; i++) {
      if (srcValues[i] < 0) {
        throw new IllegalArgumentException();
      }

      srcValues[i] = getDestination(ranges, srcValues[i]);

      if (srcValues[i] < srcValues[smInd]) {
        smInd = i;
      }
    }

    long tmp = srcValues[0];
    srcValues[0] = srcValues[smInd];
    srcValues[smInd] = tmp;
  }

  private static void updateDestinations(List<Long[]> rangesOfValues, List<Long[]> ranges) {
    Objects.requireNonNull(rangesOfValues);
    Objects.requireNonNull(ranges);

    List<Long[]> updatedRangesOfValues = new ArrayList<>();
    int smInd = 0;

    for (int i = 0; i < rangesOfValues.size(); i++) {
      Long[] valueRange = rangesOfValues.get(i);
      List<Long[]> destinations = getDestination(valueRange[0], valueRange[1], ranges);

      for (Long[] destinationRange : destinations) {
        updatedRangesOfValues.add(destinationRange);

        if (destinationRange[0] < updatedRangesOfValues.get(0)[0]) {
          smInd = updatedRangesOfValues.size() - 1;
        }
      }
    }

    rangesOfValues.clear();
    rangesOfValues.addAll(updatedRangesOfValues);

    Long[] tmp = rangesOfValues.get(0);
    rangesOfValues.set(0, rangesOfValues.get(smInd));
    rangesOfValues.set(smInd, tmp);
  }

  private static long getDestination(List<Long[]> ranges, long val) {
    for (Long[] range : ranges) {
      if (val >= range[1] && val < range[1] + range[2]) {
        return val - range[1] + range[0];
      }
    }

    return val;
  }

  /**
   *
   *
   * <pre>
   *  Returns a list of ranges based on 5 possible cases.
   *
   * Goes through all the conversion ranges.
   * (where the conversion range array looks like
   * {destination start index, conversion range start index, length})
   * and checks whether the source range partially or completely overlaps it.
   * If so,
   *  a new part of the source range that overlaps will be converted.
   *  based on the destination start index and added as a return value with its length.
   *
   * The other part that doesn't overlap with the conversion range (if present) will
   * be passed to a recursive call to see if it falls into other conversion ranges.
   *
   * vs - source range start index
   * ve - source range end index
   * rs - convertion range start index
   * re - convertion range end index
   *
   * ds - destination start index
   *
   * Where
   *  1: rs vs ve re
   *    source range is inside a destination range.
   *  Result:
   *    {ds + vs - rs, ve - vs} vs to ve - source range will be converted
   *  2: vs rs re ve
   *    destination range is inside the source range.
   *  Result:
   *    {vs, rs - vs},           vs to rs - source range isn't changed
   *                                        pass to a recusive call
   *    {ds, re - rs},           rs to re - convertion range
   *    {re, ve - re}            re to ve - source range isn't changed
   *                                        pass to a recusive call
   *  3: rs vs re ve
   *    left side of the source range will be converted.
   *  Result:
   *    {ds + vs - rs, re - vs}, vs to re - conversion range
   *    {re, ve - re}            re to ve - source range isn't changed
   *                                        pass to a recusive call
   *  4: vs rs ve re
   *    right side of the source range will be converted.
   *  Result:
   *    {vs, rs - vs},           vs to rs - source range isn't changed
   *                                        pass to a recusive call
   *    {ds, ve - rs}            rs to ve - convertion range
   *  5: vs ve rs re | rs re vs ve
   *  Result:
   *    source and destination ranges don't overlap
   *    {vs, ve - vs}            vs to ve - source range isn't changed
   *
   * </pre>
   *
   * @param vs source range start index
   * @param length of the source range
   * @param convertionRanges list of destination ranges with a pattern {destination start index,
   *     convertion range start index, length of the convertion range}
   * @return {@link List} that contains an array of destination ranges that include start index and
   *     length
   */
  private static List<Long[]> getDestination(long vs, long length, List<Long[]> convertionRanges) {
    if (vs < 0 || length < 0 || convertionRanges == null) {
      throw new IllegalArgumentException();
    }

    long ve = vs + length;
    for (Long[] range : convertionRanges) {
      if (range.length != 3 || range[0] < 0 || range[1] < 0 || range[2] < 0) {
        throw new IllegalArgumentException();
      }

      long rs = range[1];
      long re = rs + range[2];

      if (rs <= vs && ve <= re) { // rs VS VE re
        return Arrays.asList(new Long[][] {{range[0] + vs - rs, length}}); // VS VE

      } else if (vs <= rs && re <= ve) { // VS rs re VE
        List<Long[]> l = new ArrayList<>();
        l.addAll(getDestination(vs, rs - vs, convertionRanges)); // VS rs
        l.add(new Long[] {range[0], range[2]}); // rs re
        l.addAll(getDestination(re, ve - re, convertionRanges)); // re VE

        return l;
      } else if (rs < vs && vs < re && re < ve) { // rs VS re VE
        List<Long[]> l = new ArrayList<>();
        l.add(new Long[] {range[0] + (vs - rs), re - vs}); // VS re
        l.addAll(getDestination(re, ve - re, convertionRanges)); // re VE

        return l;
      } else if (vs < rs && ve < re && rs < ve) { // VS rs VE re
        List<Long[]> l = new ArrayList<>();

        l.addAll(getDestination(vs, rs - vs, convertionRanges)); // VS rs
        l.add(new Long[] {range[0], ve - rs}); // rs VE
        return l;
      }
    }

    return Arrays.asList(new Long[][] {{vs, length}}); // VS VE rs re | rs re VS VE
  }

  private static Long[] getRange(String line) {
    String[] strRange = line.split("\\s+");
    Long[] range = new Long[strRange.length];
    for (int i = 0; i < range.length; i++) {
      range[i] = Long.parseLong(strRange[i]);
    }

    return range;
  }
}
