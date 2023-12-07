package day4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *
 * --- Day 4: Scratchcards ---
 * https://adventofcode.com/2023/day/4
 *
 * --- First Star ---
 *
 * The Elf leads you over to the pile of colorful cards.
 * There, you discover dozens of scratchcards, all with their opaque covering
 * already scratched off.
 * Picking one up, it looks like each card has two lists of numbers separated
 * by a vertical bar (|): a list of winning numbers and then a list of numbers you have.
 * You organize the information into a table (your puzzle input).
 *
 * As far as the Elf has been able to figure out, you have to figure out
 * which of the numbers you have appear in the list of winning numbers.
 * The first match makes the card worth one point and each match after the
 * first doubles the point value of that card.
 *
 * For example:
 *  Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
 *  Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
 *  Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
 *  Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
 *  Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
 *  Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
 *
 * In the above example, card 1 has five winning numbers (41, 48, 83, 86, and 17)
 * and eight numbers you have (83, 86, 6, 31, 17, 9, 48, and 53).
 * Of the numbers you have, four of them (48, 83, 17, and 86) are winning numbers!
 * That means card 1 is worth 8 points (1 for the first match, then doubled three
 * times for each of the three matches after the first).
 *
 * - Card 2 has two winning numbers (32 and 61), so it is worth 2 points.
 * - Card 3 has two winning numbers (1 and 21), so it is worth 2 points.
 * - Card 4 has one winning number (84), so it is worth 1 point.
 * - Card 5 has no winning numbers, so it is worth no points.
 * - Card 6 has no winning numbers, so it is worth no points.
 *
 * So, in this example, the Elf's pile of scratchcards is worth 13 points.
 *
 * Your puzzle answer was 25231.
 *
 * --- Part Two ---
 *
 * Copies of scratchcards are scored like normal scratchcards and have the
 * same card number as the card they copied.
 * So, if you win a copy of card 10 and it has 5 matching numbers, it would
 * then win a copy of the same cards that the original card 10 won:
 * cards 11, 12, 13, 14, and 15. This process repeats until none of the copies
 * cause you to win any more cards. (Cards will never make you copy a card
 * past the end of the table.)
 *
 * This time, the above example goes differently:
 *
 *  Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
 *  Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
 *  Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
 *  Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
 *  Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
 *  Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
 *
 *  - Card 1 has four matching numbers, so you win one copy each of the next
 *    four cards: cards 2, 3, 4, and 5.
 *  - Your original card 2 has two matching numbers, so you win one copy each
 *     of cards 3 and 4.
 *  - Your copy of card 2 also wins one copy each of cards 3 and 4.
 *  - Your four instances of card 3 (one original and three copies) have two matching numbers,
 *    so you win four copies each of cards 4 and 5.
 *  - Your eight instances of card 4 (one original and seven copies) have one matching number,
 *    so you win eight copies of card 5.
 *  - Your fourteen instances of card 5 (one original and thirteen copies)
 *    have no matching numbers and win no more cards.
 *  - Your one instance of card 6 (one original) has no matching numbers and
 *    wins no more cards.
 *
 * Once all of the originals and copies have been processed, you end up with 1
 * instance of card 1, 2 instances of card 2, 4 instances of card 3, 8
 * instances of card 4, 14 instances of card 5, and 1 instance of card 6.
 * In total, this example pile of scratchcards causes you to ultimately have 30 scratchcards!
 *
 *
 * Your puzzle answer was 9721255.
 * </pre>
 */
public class Day4 {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    String path = "/advent_of_code/2023/day4/";

    System.out.println(getFirstStar(path + "/test1.txt"));
    System.out.println(getFirstStar(path + "/input1.txt"));
    System.out.println(getSecondStar(path + "/test1.txt"));
    System.out.println(getSecondStar(path + "/input1.txt"));
  }

  /*
   *
   * create a point counter
   * read a line
   *    save win nums to the hashset
   *    while iterating over own numbers
   *    lookup each number in the hash set
   *    if present
   *      multiply current point by 2
   * add card point to point counter
   * repeate
   *
   */
  private static int getFirstStar(String inputName) throws IOException, FileNotFoundException {
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      int totalPoints = 0;
      while (bf.ready()) {
        String[] line = bf.readLine().split("\\s+");

        Set<Integer> winNums = new HashSet<>();
        int stopPoint = 0;
        // read win nums
        for (int i = 2; i < line.length; i++) {
          String val = line[i];
          if (val.equals("|")) {
            stopPoint = i;
            break;
          }
          winNums.add(Integer.parseInt(val));
        }

        int cardPoints = 0;
        // read own numbers
        for (int i = stopPoint + 1; i < line.length; i++) {
          int num = Integer.parseInt(line[i]);
          if (!winNums.contains(num)) {
            continue;
          }

          cardPoints += cardPoints == 0 ? 1 : cardPoints;
        }

        totalPoints += cardPoints;
      }

      return totalPoints;
    }
  }

  /*
   * create cardCounter ArrayList
   * create a tatalCardsCounter
   * read a line
   * int numOfMatches = getNumOfMatches(line) {
   *    create matchCounter
   *
   *    save win nums to hashset
   *    while iterating over own numbers
   *    lookup each number in  a hash set
   *    if present
   *      increas matchCounter
   * }
   *
   * increase Each Card Counter by cardCounter.getCurrentCardCounter
   * starting from the next card upto numfOfMatches
   *
   * totalCardsCounter += cardCounter.getCurrentCardCounter
   * repeate
   */
  private static int getSecondStar(String inputName) throws IOException, FileNotFoundException {
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {
      List<Integer> cardCounter = new ArrayList<>(2);
      int totalCardCounter = 0;
      int lineCounter = 0;

      while (bf.ready()) {
        String[] line = bf.readLine().split("\\s+");

        // add card to the cardCounter if it wasn't added
        if (cardCounter.size() <= lineCounter) {
          cardCounter.add(1);
        }

        int cardMatches = getCardMatches(line);
        int currCardCounter = cardCounter.get(lineCounter);

        for (int i = lineCounter + 1; i <= lineCounter + cardMatches; i++) {
          if (cardCounter.size() <= i) {
            cardCounter.add(1);
          }
          cardCounter.set(i, cardCounter.get(i) + currCardCounter * 1);
        }

        totalCardCounter += cardCounter.get(lineCounter);
        lineCounter++;
      }
      return totalCardCounter;
    }
  }

  private static int getCardMatches(String[] line) {
    Set<Integer> winNums = new HashSet<>();
    int stopPoint = 0;
    // read win nums
    for (int i = 2; i < line.length; i++) {
      String val = line[i];
      if (val.equals("|")) {
        stopPoint = i;
        break;
      }
      winNums.add(Integer.parseInt(val));
    }

    int cardMatches = 0;
    // read own numbers
    for (int i = stopPoint + 1; i < line.length; i++) {
      int num = Integer.parseInt(line[i]);
      if (!winNums.contains(num)) {
        continue;
      }

      cardMatches++;
    }
    return cardMatches;
  }
}
