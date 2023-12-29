package day11;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * <pre>
 * --- Day 10: Pipe Maze ---
 * https://adventofcode.com/2023/day/10
 *
 *
 * The researcher has collected a bunch of data and compiled the data into a single giant
 * image (your puzzle input). The image includes empty space (.) and galaxies (#).
 * For example:
 *
 *    ...#......
 *    .......#..
 *    #.........
 *    ..........
 *    ......#...
 *    .#........
 *    .........#
 *    ..........
 *    .......#..
 *    #...#.....
 *
 * The researcher is trying to figure out the sum of the lengths of the shortest path
 * between every pair of galaxies. However, there's a catch: the universe expanded in the
 * time it took the light from those galaxies to reach the observatory.
 *
 * Due to something involving gravitational effects, only some space expands.
 * In fact, the result is that any rows or columns that contain no galaxies should all
 * actually be twice as big.
 *
 * In the above example, three columns and two rows contain no galaxies:
 *
 *    v  v  v
 *    ...#......
 *    .......#..
 *    #.........
 *    >..........<
 *    ......#...
 *    .#........
 *    .........#
 *    >..........<
 *    .......#..
 *    #...#.....
 *    ^  ^  ^
 *
 * These rows and columns need to be twice as big; the result of cosmic expansion therefore
 * looks like this:
 *
 *    ....#........
 *    .........#...
 *    #............
 *    .............
 *    .............
 *    ........#....
 *    .#...........
 *    ............#
 *    .............
 *    .............
 *    .........#...
 *    #....#.......
 *
 * Equipped with this expanded universe, the shortest path between every pair of galaxies
 * can be found. It can help to assign every galaxy a unique number:
 *
 *    ....1........
 *    .........2...
 *    3............
 *    .............
 *    .............
 *    ........4....
 *    .5...........
 *    ............6
 *    .............
 *    .............
 *    .........7...
 *    8....9.......
 *
 * In these 9 galaxies, there are 36 pairs. Only count each pair once; order within the
 * pair doesn't matter. For each pair, find any shortest path between the two galaxies
 * using only steps that move up, down, left, or right exactly one . or # at a time.
 * (The shortest path between two galaxies is allowed to pass through another galaxy.)
 *
 * For example, here is one of the shortest paths between galaxies 5 and 9:
 *
 *    ....1........
 *    .........2...
 *    3............
 *    .............
 *    .............
 *    ........4....
 *    .5...........
 *    .##.........6
 *    ..##.........
 *    ...##........
 *    ....##...7...
 *    8....9.......
 *
 * This path has length 9 because it takes a minimum of nine steps to get from galaxy 5 to
 * galaxy 9 (the eight locations marked # plus the step onto galaxy 9 itself).
 * Here are some other example shortest path lengths:
 *
 *  - Between galaxy 1 and galaxy 7: 15
 *  - Between galaxy 3 and galaxy 6: 17
 *  - Between galaxy 8 and galaxy 9: 5
 *
 * In this example, after expanding the universe, the sum of the shortest path between
 * all 36 pairs of galaxies is 374.
 *
 * Expand the universe, then find the length of the shortest path between every pair of
 * galaxies. What is the sum of these lengths?
 *
 * Your puzzle answer was 9543156.
 *
 *
 * --- Part Two ---
 *The galaxies are much older (and thus much farther apart) than the researcher initially estimated.
 *
 * Now, instead of the expansion you did before, make each empty row or column one million
 * times larger. That is, each empty row should be replaced with 1000000 empty rows,
 * and each empty column should be replaced with 1000000 empty columns.
 *
 * (In the example above, if each empty row or column were merely 10 times larger,
 * the sum of the shortest paths between every pair of galaxies would be 1030.
 * If each empty row or column were merely 100 times larger, the sum of the shortest paths
 * between every pair of galaxies would be 8410. However, your universe will need to expand
 * far beyond these values.)
 *
 * Starting with the same initial image, expand the universe according to these new rules,
 * then find the length of the shortest path between every pair of galaxies.
 * What is the sum of these lengths?
 *
 * Your puzzle answer was 625243292686.
 *
 * */
public class Day11 {

  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day11";

    long firstStar1 = getFirstStar(path + "/test1.txt");
    System.out.println(firstStar1);
    assert firstStar1 == 374;

    long firstStar2 = getFirstStar(path + "/input1.txt");
    System.out.println(firstStar2);

    long secondStar1 = getSecondtStar(path + "/test1.txt");
    System.out.println(secondStar1);

    long secondStar2 = getSecondtStar(path + "/input1.txt");
    System.out.println(secondStar2);
  }

  private static long getFirstStar(String input) throws FileNotFoundException, IOException {
    List<Galaxy> galaxies = getGalaxies(input, 2);
    return getSumOfShortestPaths(galaxies);
  }

  private static long getSecondtStar(String input) throws FileNotFoundException, IOException {
    List<Galaxy> galaxies = getGalaxies(input, 1_000_000);
    return getSumOfShortestPaths(galaxies);
  }

  /**
   * Return a list of {@link Galaxy} with cordinates adjusted according to the extension of the
   * universe.
   *
   * @param inputPath the path of the file to extract the data from
   * @return a list of {@link Galaxy}
   */
  private static List<Galaxy> getGalaxies(String inputPath, int extendVoidBy)
      throws FileNotFoundException, IOException {
    Objects.requireNonNull(inputPath);

    List<Galaxy> galaxies = new ArrayList<>();
    try (BufferedReader bf = new BufferedReader(new FileReader(inputPath))) {

      boolean[] hasVerLineAGalaxy = null;
      int countHorLines = 1;
      while (bf.ready()) {
        String line = bf.readLine();
        if (hasVerLineAGalaxy == null) {
          hasVerLineAGalaxy = new boolean[line.length()];
        }

        int galaxyIndex = -1;
        boolean isGalaxyFound = false;
        while ((galaxyIndex = line.indexOf("#", galaxyIndex + 1)) != -1) {
          galaxies.add(new Galaxy(galaxyIndex, countHorLines));
          hasVerLineAGalaxy[galaxyIndex] = true;
          isGalaxyFound = true;
        }

        countHorLines = Math.addExact(countHorLines, isGalaxyFound ? 1 : extendVoidBy);
        assert countHorLines > 0;
      }

      int countVerLines = 0;
      int[] varLines = new int[hasVerLineAGalaxy.length];
      for (int i = 0; i < hasVerLineAGalaxy.length; i++) {
        varLines[i] =
            countVerLines = Math.addExact(countVerLines, hasVerLineAGalaxy[i] ? 1 : extendVoidBy);
      }

      for (Galaxy galaxy : galaxies) {
        galaxy.x = varLines[galaxy.x];
      }
    }

    return galaxies;
  }

  /** Return the sum of short paths between each pair of galxies. */
  private static long getSumOfShortestPaths(List<Galaxy> galaxies) {
    long sum = 0;

    for (int i = 0; i < galaxies.size(); i++) {
      for (int j = i + 1; j < galaxies.size(); j++) {
        int shortesPath = getShortesPath(galaxies.get(i), galaxies.get(j));
        sum = Math.addExact(sum, shortesPath);
      }
    }
    return sum;
  }

  private static int getShortesPath(Galaxy galaxy1, Galaxy galaxy2) {
    Objects.requireNonNull(galaxy1);
    Objects.requireNonNull(galaxy2);

    return (2 * Math.abs(galaxy2.x - galaxy1.x) + 2 * Math.abs(galaxy2.y - galaxy1.y)) / 2;
  }

  /** Represents a Galaxy on the cordinat plane with x and y cordinates. */
  private static class Galaxy {
    int x, y;

    public Galaxy(int x, int y) {
      if (x < 0 || y < 0) {
        throw new IllegalArgumentException("Cordinats can't be below zero");
      }
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return "Galaxy [x=" + x + ", y=" + y + "]";
    }
  }
}
