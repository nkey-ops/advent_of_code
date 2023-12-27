package day10;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * <pre>
 * --- Day 10: Pipe Maze ---
 * https://adventofcode.com/2023/day/10
 * The pipes are arranged in a two-dimensional grid of tiles:
 *
 *  - | is a vertical pipe connecting north and south.
 *  - - is a horizontal pipe connecting east and west.
 *  - L is a 90-degree bend connecting north and east.
 *  - J is a 90-degree bend connecting north and west.
 *  - 7 is a 90-degree bend connecting south and west.
 *  - F is a 90-degree bend connecting south and east.
 *  - . is ground; there is no pipe in this tile.
 *  - S is the starting position of the animal; there is a pipe on this tile,
 *  but your sketch doesn't show what shape the pipe has.
 *
 *
 *
 * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains
 * the animal is one large, continuous loop.
 *
 * For example, here is a square loop of pipe:
 *     .....
 *     .F-7.
 *     .|.|.
 *     .L-J.
 *     .....
 *
 * If the animal had entered this loop in the northwest corner,
 * the sketch would instead look like this:
 *     .....
 *     .S-7.
 *     .|.|.
 *     .L-J.
 *     .....
 *
 *
 * In the above diagram, the S tile is still a 90-degree F bend: you can tell because of
 * how the adjacent pipes connect to it.
 *
 * Unfortunately, there are also many pipes that aren't connected to the loop!
 * This sketch shows the same loop as above:
 *
 *    -L|F7
 *    7S-7|
 *    L|7||
 *    -L-J|
 *    L|-JF
 *
 * In the above diagram, you can still figure out which pipes form the main loop:
 * they're the ones connected to S, pipes those pipes connect to, pipes those pipes connect to,
 * and so on. Every pipe in the main loop connects to its two neighbors
 * (including S, which will have exactly two pipes connecting to it, and which is assumed
 * to connect back to those two pipes).
 *
 * If you want to get out ahead of the animal, you should find the tile in the loop that
 * is farthest from the starting position. Because the animal is in the pipe, it doesn't
 * make sense to measure this by direct distance. Instead, you need to find the tile that
 * would take the longest number of steps along the loop to reach from the starting point -
 * regardless of which way around the loop the animal went.
 *
 * In the first example with the square loop:
 *    .....
 *    .012.
 *    .1.3.
 *    .234.
 *    .....
 * In this example, the farthest point from the start is 4 steps away.
 *
 * Here's the more complex loop again:
 *    ..45.
 *    .236.
 *    01.78
 *    14567
 *    23...
 *
 * Find the single giant loop starting at S.
 * How many steps along the loop does it take to get from the starting position to the point
 * farthest from the starting position?
 *
 * Your puzzle answer was 7097.
 *
 * </pre>
 */
public class Day10 {

  public static void main(String[] args) throws IOException {
    String path = "/home/deuru/table/space/advent_of_code/2023/day10";

    int firstStar = getFirstStar(path + "/test1.txt");
    System.out.println(firstStar);
    assert firstStar == 4;

    int firstStar2 = getFirstStar(path + "/test1_hard.txt");
    System.out.println(firstStar2);
    assert firstStar2 == 4;

    int firstStar3 = getFirstStar(path + "/test2.txt");
    System.out.println(firstStar3);
    assert firstStar3 == 8;

    int firstStar4 = getFirstStar(path + "/test2_hard.txt");
    System.out.println(firstStar4);
    assert firstStar4 == 8;

    System.out.println(getFirstStar(path + "/input1.txt"));
  }

  private static int getFirstStar(String input) throws FileNotFoundException, IOException {

    int sy = 0, sx = 0;
    char[][] field;
    try (BufferedReader bf = new BufferedReader(new FileReader(input))) {
      List<char[]> l = new ArrayList<>();

      while (bf.ready()) {
        String line = bf.readLine();

        int ch;
        if ((ch = line.indexOf("S")) != -1) {
          assert sy == 0 && sx == 0;
          sy = l.size();
          sx = ch;
        }

        l.add(line.toCharArray());
      }

      field = new char[l.size()][];
      for (int i = 0; i < l.size(); i++) {
        field[i] = l.get(i);
      }
    }

    assert field != null;
    assert field.length != 0;

    int furthestPipe = getFurthestPipe(field, new Pipe(sx, sy));
    return furthestPipe;
  }

  /**
   * Calcultes the number of pipes to the furthest one starting from the {@code startPipe} by
   * finding two adjacent pipes to the {@code startPipe} and moving along them until they meet at
   * the same one.
   *
   * @param field to find adjacent pipes
   * @param startPipe to start from
   * @return the number of pipes to the furthest one starting from the {@code startPipe}
   */
  private static int getFurthestPipe(char[][] field, Pipe startPipe) {
    validatePipe(field, startPipe);
    validateStartPipe(field, startPipe);

    Pipe pipe1 = getAdjacentPipeToStartPipe(field, startPipe, new Pipe(startPipe.x, startPipe.y));
    Pipe pipe2 = getAdjacentPipeToStartPipe(field, startPipe, pipe1);

    Pipe prevPipe1 = startPipe;
    Pipe prevPipe2 = startPipe;

    int countPipes = 1;
    while (pipe1.x != pipe2.x || pipe1.y != pipe2.y) {
      Pipe newPipe1 = getAdjacentPipe(field, pipe1, prevPipe1);
      Pipe newPipe2 = getAdjacentPipe(field, pipe2, prevPipe2);

      prevPipe1 = pipe1;
      prevPipe2 = pipe2;
      pipe1 = newPipe1;
      pipe2 = newPipe2;

      countPipes++;

      assert countPipes > 0;
    }

    return countPipes;
  }

  /**
   * Returs the adjancent pipe to wich {@code pipe} is linked to but it shouldn't be an {@code
   * excludePipe}. Each pipe can have up to 2 pipes connected.
   *
   * @param field to find the adjacent pipe on
   * @param pipe to dermine the direction where the next pipe is
   * @param excludePipe the pipe to be exclude from the
   * @return the adjacent pipe that is not an {@code excludePipe}
   */
  private static Pipe getAdjacentPipe(char[][] field, Pipe pipe, Pipe excludePipe) {
    Objects.requireNonNull(field);
    Objects.requireNonNull(pipe);
    Objects.requireNonNull(excludePipe);

    validatePipe(field, pipe);
    validatePipe(field, excludePipe);

    return switch (field[pipe.y][pipe.x]) {
      case '|' -> new Pipe(pipe.x, pipe.y + (excludePipe.y == pipe.y - 1 ? 1 : -1));
      case '-' -> new Pipe(pipe.x + (excludePipe.x == pipe.x - 1 ? 1 : -1), pipe.y);
      case 'L' -> excludePipe.y == pipe.y - 1
          ? new Pipe(pipe.x + 1, pipe.y)
          : new Pipe(pipe.x, pipe.y - 1);
      case 'J' -> excludePipe.y == pipe.y - 1
          ? new Pipe(pipe.x - 1, pipe.y)
          : new Pipe(pipe.x, pipe.y - 1);
      case '7' -> excludePipe.y == pipe.y + 1
          ? new Pipe(pipe.x - 1, pipe.y)
          : new Pipe(pipe.x, pipe.y + 1);
      case 'F' -> excludePipe.y == pipe.y + 1
          ? new Pipe(pipe.x + 1, pipe.y)
          : new Pipe(pipe.x, pipe.y + 1);
      case 'S' -> getAdjacentPipeToStartPipe(field, pipe, excludePipe);
      default -> throw new IllegalArgumentException();
    };
  }

  /**
   * Returns adjacent pipe to a {@code startPipe} excluding {@code excludePipe}.
   *
   * @param field to find adjacent pipes
   * @param startPipe to get adjacent pipe
   * @param excludePipe to exclude pipe if it's adjacent
   * @return adjacent {@link Pipe} to a {@code startPipe}
   */
  private static Pipe getAdjacentPipeToStartPipe(char[][] field, Pipe startPipe, Pipe excludePipe) {
    validateStartPipe(field, startPipe);
    validatePipe(field, excludePipe);

    char dir;
    int x = startPipe.x;
    int y = startPipe.y - 1;

    // North
    if (y >= 0
        && !(x == excludePipe.x && y == excludePipe.y)
        && ((dir = field[y][x]) == '7' || dir == '|' || dir == 'F')) {
      return new Pipe(x, y);
    }

    x = startPipe.x + 1;
    y = startPipe.y;

    // East
    if (x < field[startPipe.y].length
        && !(x == excludePipe.x && y == excludePipe.y)
        && ((dir = field[y][x]) == 'J' || dir == '-' || dir == '7')) {
      return new Pipe(x, y);
    }

    x = startPipe.x;
    y = startPipe.y + 1;

    // South
    if (y < field.length
        && !(x == excludePipe.x && y == excludePipe.y)
        && ((dir = field[y][x]) == 'J' || dir == '|' || dir == 'L')) {
      return new Pipe(x, y);
    }

    x = startPipe.x - 1;
    y = startPipe.y;

    // West
    if (x < 0
        && !(x == excludePipe.x && y == excludePipe.y)
        && ((dir = field[y][x]) == 'L' || dir == '-' || dir == 'F')) {
      return new Pipe(x, y);
    }

    throw new IllegalArgumentException("No pipe were found");
  }

  private static class Pipe {
    final int x, y;

    public Pipe(int x, int y) {
      if (x < 0 || y < 0) {
        throw new IllegalArgumentException("Cordinats can't be belows zero");
      }

      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return "Pipe [x=" + x + ", y=" + y + "]";
    }
  }

  /**
   * Throws {@link IllegalArgumentException} if the {@code pipe} isn't in bounds of the {@code
   * field} or field, pipe is a null.
   *
   * @param field to define the bounds
   * @param pipe 's bounds to check
   */
  private static void validatePipe(char[][] field, Pipe pipe) {
    Objects.requireNonNull(field);
    Objects.requireNonNull(pipe);

    if (field.length == 0 || pipe.x >= field[0].length || pipe.y >= field.length) {
      throw new IllegalArgumentException("Pipe is out of bouns for the field");
    }
  }

  /**
   * Throws {@link IllegalArgumentException} if the {@code pipe} doesn't pass {@link
   * Day10#validatePipe(char[][], Pipe)} or isn't a start pipe (i.e != 'S')
   *
   * @param field to define the bounds
   * @param pipe to check wheather is a start pipe
   */
  private static void validateStartPipe(char[][] field, Pipe pipe) {
    validatePipe(field, pipe);
    if (field[pipe.y][pipe.x] != 'S') {
      throw new IllegalArgumentException("The pipe isn't a start pipe");
    }
  }
}
