package day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 *
 * <pre>
 * --- Day 7: Camel Cards ---
 * https://adventofcode.com/2023/day/7
 *
 * --- First Star --
 * In Camel Cards, you get a list of hands, and your goal is to order them based on the
 * strength of each hand.
 * A hand consists of five cards labeled one of A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2.
 * The relative strength of each card follows this order, where A is the highest and 2
 * is the lowest.
 *
 * Every hand is exactly one type. From strongest to weakest, they are:
 *
 * - Five of a kind, where all five cards have the same label: AAAAA
 * - Four of a kind, where four cards have the same label and one card has a different label: AA8AA
 * - Full house, where three cards have the same label, and the remaining two cards share
 *   a different label: 23332
 * - Three of a kind, where three cards have the same label, and the remaining two cards are
 *   each different from any other card in the hand: TTT98
 * - Two pair, where two cards share one label, two other cards share a second label, and the
 *   remaining card has a third label: 23432
 * - One pair, where two cards share one label, and the other three cards have a different label
 *   from the pair and each other: A23A4
 * - High card, where all cards' labels are distinct: 23456
 *
 * - If two hands have the same type, a second ordering rule takes effect.
 *   Start by comparing the first card in each hand.
 * - If these cards are different, the hand with the stronger first card is considered stronger.
 * - If the first card in each hand have the same label, however, then move on to considering
 *   the second card in each hand.
 * - If they differ, the hand with the higher second card wins; otherwise,
 *   continue with the third card in each hand, then the fourth, then the fifth.
 *
 * To play Camel Cards, you are given a list of hands and their corresponding bid
 * (your puzzle input).
 *
 * For example:
 *
 *   32T3K 765
 *   T55J5 684
 *   KK677 28
 *   KTJJT 220
 *   QQQJA 483
 *
 * This example shows five hands; each hand is followed by its bid amount.
 * Each hand wins an amount equal to its bid multiplied by its rank, where the weakest hand
 * gets rank 1, the second-weakest hand gets rank 2, and so on up to the strongest hand.
 * Because there are five hands in this example, the strongest hand will have rank 5 and its
 * bid will be multiplied by 5.
 *
 * So, the first step is to put the hands in order of strength:
 *
 * - 32T3K is the only one pair and the other hands are all a stronger type, so it gets rank 1.
 * - KK677 and KTJJT are both two pair. Their first cards both have the same label,
 *   but the second card of KK677 is stronger (K vs T), so KTJJT gets rank 2 and KK677 gets rank 3.
 * - T55J5 and QQQJA are both three of a kind. QQQJA has a stronger first card, so it gets
 *   rank 5 and T55J5 gets rank 4.
 *
 * Now, you can determine the total winnings of this set of hands by adding up the result
 * of multiplying each hand's bid with its rank (765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5).
 * So the total winnings in this example are 6440.
 *
 * Your puzzle answer was 253866470.
 *
 * --- Part Two ---
 *
 * To make things a little more interesting, the Elf introduces one additional rule.
 * Now, J cards are jokers - wildcards that can act like whatever card would make the hand the
 * strongest type possible.
 *
 * To balance this, J cards are now the weakest individual cards, weaker even than 2.
 * The other cards stay in the same order: A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J.
 *
 * J cards can pretend to be whatever card is best for the purpose of determining hand type;
 * for example, QJJQ2 is now considered four of a kind.
 * However, for the purpose of breaking ties between two hands of the same type,
 * J is always treated as J, not the card it's pretending to be:
 * JKKK2 is weaker than QQQQ2 because J is weaker than Q.
 *
 * Now, the above example goes very differently:
 *
 *   32T3K 765
 *   T55J5 684
 *   KK677 28
 *   KTJJT 220
 *   QQQJA 483
 *
 * - 32T3K is still the only one pair; it doesn't contain any jokers,
 *   so its strength doesn't increase.
 * - KK677 is now the only two pair, making it the second-weakest hand.
 * - T55J5, KTJJT, and QQQJA are now all four of a kind! T55J5 gets rank 3, QQQJA gets rank 4,
 *   and KTJJT gets rank 5.
 *
 * With the new joker rule, the total winnings in this example are 5905.
 *
 * Your puzzle answer was 254494947.
 *
 * </pre>
 */
public class Day7 {

  public static void main(String[] args) throws IOException {
    String path = "/advent_of_code/2023/day7";

    System.out.println(getFirstStarOrSecondStart(path + "/test1.txt", true));
    System.out.println(getFirstStarOrSecondStart(path + "/input1.txt", true));
    System.out.println(getFirstStarOrSecondStart(path + "/test1.txt", false));
    System.out.println(getFirstStarOrSecondStart(path + "/input1.txt", false));
  }

  private static int getFirstStarOrSecondStart(String inputName, boolean isFirstStar)
      throws IOException {
    try (BufferedReader bf = new BufferedReader(new FileReader(inputName))) {

      Set<Hand> hands = new TreeSet<>();
      while (bf.ready()) {
        String[] handAndBid = bf.readLine().split("\\s");

        assert handAndBid.length == 2;
        assert handAndBid[0].length() == 5;

        int bid = Integer.parseInt(handAndBid[1]);
        assert bid > 0;

        char[] cards = handAndBid[0].toCharArray();
        Hand e = new Hand(cards, bid, !isFirstStar);
        hands.add(e);
      }

      return getSum(hands);
    }
  }

  private static int getSum(Set<Hand> hands) {
    int sum = 0;
    int rank = 1;
    for (Hand hand : hands) {
      sum += hand.bid * rank++;
    }

    return sum;
  }

  private static class Hand implements Comparable<Hand> {
    private static enum HandType {
      HIGH_CARD,
      ONE_PAIR,
      TWO_PAIR,
      THREE_OF_KIND,
      FULL_HOUSE,
      FOUR_OF_KIND,
      FIVE_OF_KIND;
    }

    final char[] hand;
    final int bid;
    final HandType type;
    final boolean isJockerEnabled;

    public Hand(char[] hand, int bid, boolean isJockerEnabled) {
      if (hand.length != 5) {
        throw new IllegalArgumentException();
      }

      this.hand = Arrays.copyOf(hand, 5);
      this.bid = bid;
      this.isJockerEnabled = isJockerEnabled;
      this.type = isJockerEnabled ? getHandTypeWithJocker(hand) : getHandType(hand);
    }

    private static HandType getHandType(char[] hand) {
      if (hand.length != 5) {
        throw new IllegalArgumentException();
      }

      hand = sort(hand[0], hand[1], hand[2], hand[3], hand[4]);

      int pair1 = 0;
      int pair2 = 0;
      int pairCounter = 1;
      char prev = hand[0];

      // fids the longest pairs: where pair1 >= pair2
      for (int i = 1; i <= hand.length; i++) {
        if (i != hand.length && prev == hand[i]) {
          pairCounter++;
        } else {
          if (pairCounter > pair1) {
            pair2 = pair1;
            pair1 = pairCounter;
          } else if (pairCounter > pair2) {
            pair2 = pairCounter;
          }

          if (i == hand.length) {
            break;
          }
          pairCounter = 1;
          prev = hand[i];
        }
      }

      assert pair1 >= pair2;

      return getHandType(pair1, pair2);
    }

    private static HandType getHandTypeWithJocker(char[] hand) {
      if (hand.length != 5) throw new IllegalArgumentException();
      hand = sort(hand[0], hand[1], hand[2], hand[3], hand[4]);

      int pair1 = 0;
      int pair2 = 0;
      int pairCounter = 1;
      int jockerCounter = 0;
      char prev = hand[0];

      // fids the longest pairs: where pair1 >= pair2
      for (int i = 1; i <= hand.length; i++) {
        if (prev == 'J') jockerCounter++;

        if (i != hand.length && prev == hand[i]) {
          pairCounter++;
        } else {
          if (pairCounter > pair1) {
            pair2 = pair1;
            pair1 = pairCounter;
          } else if (pairCounter > pair2) {
            pair2 = pairCounter;
          }

          if (i == hand.length) {
            break;
          }
          pairCounter = 1;
          prev = hand[i];
        }
      }

      assert pair1 >= pair2;

      // JJJJJ 5 0
      // JJJJK 5 0
      // JJJKK 5 0
      // JJJKT 4 1

      // JJQQQ 5 0
      // JJQQK 4 1
      // JJQKA 3 1

      // JQQQQ 5 0
      // JTQQQ 4 1
      // JTTQQ 3 2
      // JTAQQ 3 1
      // JTABQ 2 1
      if (jockerCounter >= 1) {
        pair1 += pair2;

        if (pair1 == 4 && pair2 == 2) {
          if (jockerCounter == 1) pair1--; // JTTQQ 3 2
          if (jockerCounter == 2) pair2--; // JJQQK 4 1
        }
      }

      if (pair1 == 5) pair2 = 0;
      return getHandType(pair1, pair2);
    }

    private static HandType getHandType(int pair1, int pair2) {
      if (pair1 + pair2 > 5 || pair1 > 5 || pair1 < pair2 || pair1 < 1 || pair2 < 0)
        throw new IllegalArgumentException(pair1 + " " + pair2);

      if (pair1 == 5) {
        return HandType.FIVE_OF_KIND;
      } else if (pair1 == 4) {
        return HandType.FOUR_OF_KIND;
      } else if (pair1 == 3 && pair2 == 2) {
        return HandType.FULL_HOUSE;
      } else if (pair1 == 3) {
        return HandType.THREE_OF_KIND;
      } else if (pair1 == 2 && pair2 == 2) {
        return HandType.TWO_PAIR;
      } else if (pair1 == 2) {
        return HandType.ONE_PAIR;
      } else {
        return HandType.HIGH_CARD;
      }
    }

    @Override
    public int compareTo(Hand that) {
      int cmp = this.type.compareTo(that.type);
      if (cmp != 0) {
        return cmp;
      }

      for (int i = 0; i < 5; i++) {
        if (this.hand[i] != that.hand[i]) {
          return compareCards(this.hand[i], that.hand[i]);
        }
      }

      return 0;
    }

    private int compareCards(char a, char b) {
      char[] labels = new char[] {'T', 'J', 'Q', 'K', 'A'};

      int aRate = 0;
      int bRate = 0;

      for (int i = 0; i < labels.length; i++) {
        if (a == labels[i]) aRate = i + 1 + '9';
        if (b == labels[i]) bRate = i + 1 + '9';
      }

      if ((a < '2' && a > '9' && aRate == 0) || (b < '2' && b > '9' && bRate == 0))
        throw new IllegalArgumentException();

      if (aRate == 0) aRate = a;
      if (bRate == 0) bRate = b;

      if (isJockerEnabled) {
        if (a == 'J') aRate = 1;
        if (b == 'J') bRate = 1;
      }

      return Integer.compare(aRate, bRate);
    }

    @Override
    public String toString() {
      return String.format("%s bid: %s type: %s", Arrays.toString(hand), bid, type);
    }

    //Horsing around
    public static char[] sort(char A, char B, char C, char D, char E) {
      char t;

      if (A > B) { t = A; A = B; B = t; }
      if (D > E) { t = D; D = E; E = t; }
      if (A > C) { t = C; C = A; A = t; }
      if (C > E) { t = C; C = E; E = t; }
      if (A > D) { t = A; A = D; D = t; }
      if (B > E) { t = B; B = E; E = t; }
      if (C > D) { t = C; C = D; D = t; }
      if (B > D) { t = B; B = D; D = t; }
      if (B > C) { t = B; B = C; C = t; }
      

      assert A <= B && B <= C && C <= D && D <= E;
      return new char[] {A, B, C, D, E};
    }
  }
}
