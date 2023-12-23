package day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 *
 * <pre>
 * --- Day 8: Haunted Wasteland ---
 * https://adventofcode.com/2023/day/8
 *
 * --- First Star --
 * It seems like you're meant to use the left/right instructions to navigate the network.
 * Perhaps if you have the camel follow the same instructions, you can escape the haunted wasteland!
 *
 * After examining the maps for a bit, two nodes stick out: AAA and ZZZ.
 * You feel like AAA is where you are now, and you have to follow the left/right
 * instructions until you reach ZZZ.
 *
 * This format defines each node of the network individually.
 *
 * For example:
 *
 *    RL
 *
 *    AAA = (BBB, CCC)
 *    BBB = (DDD, EEE)
 *    CCC = (ZZZ, GGG)
 *    DDD = (DDD, DDD)
 *    EEE = (EEE, EEE)
 *    GGG = (GGG, GGG)
 *    ZZZ = (ZZZ, ZZZ)
 *
 * Starting with AAA, you need to look up the next element based on the next left/right
 * instruction in your input.
 * In this example, start with AAA and go right (R) by choosing the right element of AAA, CCC.
 * Then, L means to choose the left element of CCC, ZZZ. By following the left/right
 * instructions, you reach ZZZ in 2 steps.
 *
 * Of course, you might not find ZZZ right away.
 * If you run out of left/right instructions, repeat the whole sequence of instructions
 * as necessary: RL really means RLRLRLRLRLRLRLRL... and so on.
 *
 * For example, here is a situation that takes 6 steps to reach ZZZ:
 *
 *    LLR
 *
 *    AAA = (BBB, BBB)
 *    BBB = (AAA, ZZZ)
 *    ZZZ = (ZZZ, ZZZ)
 *
 * Starting at AAA, follow the left/right instructions.
 * How many steps are required to reach ZZZ?
 *
 * Your puzzle answer was 13019.
 *
 * -- Part Two ---
 *
 *
 * After examining the maps a bit longer, your attention is drawn to a curious fact:
 * the number of nodes with names ending in A is equal to the number ending in Z!
 * If you were a ghost, you'd probably just start at every node that ends with A and follow all
 * of the paths at the same time until they all simultaneously end up at nodes that end with Z.
 *
 * For example:
 *
 *    LR
 *
 *    11A = (11B, XXX)
 *    11B = (XXX, 11Z)
 *    11Z = (11B, XXX)
 *    22A = (22B, XXX)
 *    22B = (22C, 22C)
 *    22C = (22Z, 22Z)
 *    22Z = (22B, 22B)
 *    XXX = (XXX, XXX)
 *
 * Here, there are two starting nodes, 11A and 22A (because they both end with A).
 * As you follow each left/right instruction, use that instruction to simultaneously
 * navigate away from both nodes you're currently on.
 * Repeat this process until all of the nodes you're currently on end with Z.
 * (If only some of the nodes you're on end with Z, they act like any other node
 * and you continue as normal.)
 *
 * In this example, you would proceed as follows:
 *
 * - Step 0: You are at 11A and 22A.
 * - Step 1: You choose all of the left paths, leading you to 11B and 22B.
 * - Step 2: You choose all of the right paths, leading you to 11Z and 22C.
 * - Step 3: You choose all of the left paths, leading you to 11B and 22Z.
 * - Step 4: You choose all of the right paths, leading you to 11Z and 22B.
 * - Step 5: You choose all of the left paths, leading you to 11B and 22C.
 * - Step 6: You choose all of the right paths, leading you to 11Z and 22Z.
 *
 * So, in this example, you end up entirely on nodes that end in Z after 6 steps.
 *
 * Simultaneously start on every node that ends with A. How many steps does it take
 * before you're only on nodes that end with Z?
 *
 * Your puzzle answer was 13524038372771.
 *
 * </pre>
 */
public class Day8 {
  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day8";

    System.out.println(getFirstStar(path + "/test1.txt"));
    System.out.println(getFirstStar(path + "/test2.txt"));
    System.out.println(getFirstStar(path + "/input1.txt"));

    System.out.println(getSecondStar(path + "/test1.txt"));
    System.out.println(getSecondStar(path + "/test2.txt"));
    System.out.println(getSecondStar(path + "/test3.txt"));
    System.out.println(getSecondStar(path + "/input1.txt"));
  }

  private static int getFirstStar(String inputName) throws IOException {

    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      Map<String, String[]> nodes = new HashMap<>();
      char[] steps = null;

      boolean isFirstLine = true;
      boolean isFirstNode = true;
      String firstNode = null;

      while (bf.ready()) {
        if (isFirstLine) {
          steps = bf.readLine().toCharArray();
          isFirstLine = false;
          continue;
        }

        String[] node = bf.readLine().split("\\s=\\s\\(|,\\s|\\)");
        if (node.length == 1) {
          continue;
        }

        if (isFirstNode) {
          firstNode = node[0];
          isFirstNode = false;
        }

        nodes.put(node[0], new String[] {node[1], node[2]});
      }

      int i = 0;
      int stepCounter = 0;
      String currNode = firstNode;
      while (!currNode.equals("ZZZ")) {
        byte side = (byte) (steps[i++] == 'L' ? 0 : 1);
        currNode = nodes.get(currNode)[side];

        stepCounter++;
        if (i == steps.length) {
          i = 0;
        }
      }

      return stepCounter;
    }
  }

  private static long getSecondStar(String inputName) throws IOException {

    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      Map<String, String[]> nodes = new HashMap<>();
      char[] steps = null;

      boolean isFirstLine = true;

      List<String> endNodes = new ArrayList<>();
      while (bf.ready()) {
        if (isFirstLine) {
          steps = bf.readLine().toCharArray();
          isFirstLine = false;
          continue;
        }

        String[] node = bf.readLine().split("\\s=\\s\\(|,\\s|\\)");
        if (node.length == 1) {
          continue;
        }

        if (node[0].charAt(2) == 'A') {
          endNodes.add(node[0]);
        }

        nodes.put(node[0], new String[] {node[1], node[2]});
      }

      return getStopIndex(nodes, steps, endNodes);
    }
  }

  private static long getStopIndex(
      Map<String, String[]> nodes, char[] steps, List<String> startNodes) {
    int[][] distances = new int[startNodes.size()][];

    for (int i = 0; i < startNodes.size(); i++) {
      distances[i] = getDistancesBeweenEndNodes(startNodes.get(i), nodes, steps);
    }

    int[] startIndexes = new int[startNodes.size()];
    long[] numOfPaths = new long[startNodes.size()];
    long stopIndex = distances[0][0];

    for (int i = 0; i < startNodes.size(); i++) {

      long stopCurrIndex = numOfPaths[i];
      int startIndex;
      for (startIndex = startIndexes[i]; stopCurrIndex < stopIndex; ) {
        stopCurrIndex += distances[i][startIndex++];
        if (startIndex == distances[i].length) {
          startIndex = 1;
        }
      }

      assert startIndex < distances[i].length;

      startIndexes[i] = startIndex;
      numOfPaths[i] = stopCurrIndex;

      if (stopCurrIndex > stopIndex) {
        stopIndex = stopCurrIndex;
        i = -1;
      }
    }

    return stopIndex * steps.length;
  }

  /**
   * Returns an integer array of distances Where the first value of the array is the distance to the
   * first end node starting from the start node,
   *
   * <p>all the next ones will have it's distance counted from the previous end node
   *
   * <p>All the distance added
   *
   * <p>distance - the number of times an array of steps should be passed to achieve an end node.
   *
   * @param startNode the node that is linked to the end nodes
   * @param nodes where the end nodes can be found
   * @param steps to follow to find the end nodes
   * @return an array of distance beween the end nodes linked to the start node
   */
  private static int[] getDistancesBeweenEndNodes(
      String startNode, Map<String, String[]> nodes, char[] steps) {
    Objects.requireNonNull(startNode);
    Objects.requireNonNull(nodes);
    Objects.requireNonNull(steps);

    if (startNode.length() != 3 || nodes.size() == 0 || steps.length == 0) {
      throw new IllegalArgumentException();
    }

    List<Integer> r = new ArrayList<>();
    String firstEndNode = null;
    int numberOfPathsPassed = 0;
    while (true) {
      for (int i = 0; i < steps.length; i++) {
        startNode = nodes.get(startNode)[steps[i] == 'L' ? 0 : 1];
      }

      numberOfPathsPassed++;

      if (isEndNode(startNode)) {
        r.add(numberOfPathsPassed);
        numberOfPathsPassed = 0;

        if (firstEndNode == null) {
          firstEndNode = startNode;
        } else if (startNode.equals(firstEndNode)) {
          break;
        }
      }
    }

    return r.stream().mapToInt(i -> i).toArray();
  }

  /**
   * Checks wheather node is an end node To be an end node its length should be equal to 3 and it
   * should have 'Z' as a last character.
   *
   * @param node name of node
   * @return whether node is an End Node
   */
  private static boolean isEndNode(String node) {
    Objects.requireNonNull(node);
    if (node.length() != 3) {
      throw new IllegalArgumentException("Node name length should be equal to 3");
    }

    return node.charAt(2) == 'Z';
  }
}
