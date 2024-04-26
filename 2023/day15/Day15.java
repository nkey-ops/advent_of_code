package day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Day15
 *
 * <pre>
 * The HASH algorithm is a way to turn any string of characters into a single number in the
 * range 0 to 255. To run the HASH algorithm on a string, start with a current value of 0.
 * Then, for each character in the string starting from the beginning:
 *
 * Determine the ASCII code for the current character of the string.
 * Increase the current value by the ASCII code you just determined.
 * Set the current value to itself multiplied by 17.
 * Set the current value to the remainder of dividing itself by 256.
 * After following these steps for each character in the string in order, the current value is the
 * output of the HASH algorithm.
 *
 * So, to find the result of running the HASH algorithm on the string HASH:
 *
 * The current value starts at 0.
 * The first character is H; its ASCII code is 72.
 * The current value increases to 72.
 * The current value is multiplied by 17 to become 1224.
 * The current value becomes 200 (the remainder of 1224 divided by 256).
 * The next character is A; its ASCII code is 65.
 * The current value increases to 265.
 * The current value is multiplied by 17 to become 4505.
 * The current value becomes 153 (the remainder of 4505 divided by 256).
 * The next character is S; its ASCII code is 83.
 * The current value increases to 236.
 * The current value is multiplied by 17 to become 4012.
 * The current value becomes 172 (the remainder of 4012 divided by 256).
 * The next character is H; its ASCII code is 72.
 * The current value increases to 244.
 * The current value is multiplied by 17 to become 4148.
 * The current value becomes 52 (the remainder of 4148 divided by 256).
 * So, the result of running the HASH algorithm on the string HASH is 52.
 *
 * The initialization sequence (your puzzle input) is a comma-separated list of steps to start the
 * Lava Production Facility. Ignore newline characters when parsing the initialization sequence.
 * To verify that your HASH algorithm is working, the book offers the sum of the result of running
 * the HASH algorithm on each step in the initialization sequence.
 *
 * For example:
 *
 * rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
 * This initialization sequence specifies 11 individual steps; the result of running the HASH
 * algorithm on each of the steps is as follows:
 *
 * rn=1 becomes 30.
 * cm- becomes 253.
 * qp=3 becomes 97.
 * cm=2 becomes 47.
 * qp- becomes 14.
 * pc=4 becomes 180.
 * ot=9 becomes 9.
 * ab=5 becomes 197.
 * pc- becomes 48.
 * pc=6 becomes 214.
 * ot=7 becomes 231.
 *
 * In this example, the sum of these results is 1320. Unfortunately, the reindeer has stolen the
 * page containing the expected verification number and is currently running around the facility
 * with it excitedly.
 *
 * Run the HASH algorithm on each step in the initialization sequence. What is the sum of the
 * results? (The initialization sequence is one long line; be careful when copy-pasting it.)
 *
 * Your puzzle answer was 522547.
 * Each step begins with a sequence of letters that indicate the label of the lens on which the
 * step operates. The result of running the HASH algorithm on the label indicates the correct box
 * for that step.
 *
 * The label will be immediately followed by a character that indicates the operation to perform:
 * either an equals sign (=) or a dash (-).
 *
 * If the operation character is a dash (-), go to the relevant box and remove the lens with the
 * given label if it is present in the box. Then, move any remaining lenses as far forward in the
 * box as they can go without changing their order, filling any space made by removing the indicated
 * lens. (If no lens in that box has the given label, nothing happens.)
 *
 * If the operation character is an equals sign (=), it will be followed by a number indicating
 * the focal length of the lens that needs to go into the relevant box; be sure to use the label
 * maker to mark the lens with the label given in the beginning of the step so you can find it
 * later. There are two possible situations:
 *
 * If there is already a lens in the box with the same label, replace the old lens with the new
 * lens: remove the old lens and put the new lens in its place, not moving any other lenses in
 * the box.
 * If there is not already a lens in the box with the same label, add the lens to the box
 * immediately behind any lenses already in the box. Don't move any of the other lenses when you
 * do this. If there aren't any lenses in the box, the new lens goes all the way to the front
 * of the box.
 *
 * Here is the contents of every box after each step in the example initialization sequence above:
 *
 * After "rn=1":
 * Box 0: [rn 1]
 *
 * After "cm-":
 * Box 0: [rn 1]
 *
 * After "qp=3":
 * Box 0: [rn 1]
 * Box 1: [qp 3]
 *
 * After "cm=2":
 * Box 0: [rn 1] [cm 2]
 * Box 1: [qp 3]
 *
 * After "qp-":
 * Box 0: [rn 1] [cm 2]
 *
 * After "pc=4":
 * Box 0: [rn 1] [cm 2]
 * Box 3: [pc 4]
 *
 * After "ot=9":
 * Box 0: [rn 1] [cm 2]
 * Box 3: [pc 4] [ot 9]
 *
 * After "ab=5":
 * Box 0: [rn 1] [cm 2]
 * Box 3: [pc 4] [ot 9] [ab 5]
 *
 * After "pc-":
 * Box 0: [rn 1] [cm 2]
 * Box 3: [ot 9] [ab 5]
 *
 * After "pc=6":
 * Box 0: [rn 1] [cm 2]
 * Box 3: [ot 9] [ab 5] [pc 6]
 *
 * After "ot=7":
 * Box 0: [rn 1] [cm 2]
 * Box 3: [ot 7] [ab 5] [pc 6]
 * All 256 boxes are always present; only the boxes that contain any lenses are shown here. Within each box, lenses are listed from front to back; each lens is shown as its label and focal length in square brackets.
 *
 * To confirm that all of the lenses are installed correctly, add up the focusing power of all of the lenses. The focusing power of a single lens is the result of multiplying together:
 *
 * One plus the box number of the lens in question.
 * The slot number of the lens within the box: 1 for the first lens, 2 for the second lens, and so on.
 * The focal length of the lens.
 * At the end of the above example, the focusing power of each lens is as follows:
 *
 * rn: 1 (box 0) * 1 (first slot) * 1 (focal length) = 1
 * cm: 1 (box 0) * 2 (second slot) * 2 (focal length) = 4
 * ot: 4 (box 3) * 1 (first slot) * 7 (focal length) = 28
 * ab: 4 (box 3) * 2 (second slot) * 5 (focal length) = 40
 * pc: 4 (box 3) * 3 (third slot) * 6 (focal length) = 72
 * So, the above example ends up with a total focusing power of 145.
 *
 * With the help of an over-enthusiastic reindeer in a hard hat, follow the initialization sequence. What is the focusing power of the resulting lens configuration?
 * </pre>
 */
public class Day15 {

  public static void main(String[] args) {
    String path = "/advent_of_code/2023/day15";

    int firstStar = getFirstStar(path + "/test1.txt");
    System.out.println(firstStar);

    int firstStar2 = getFirstStar(path + "/input.txt");
    System.out.println(firstStar2);

    int secondStar = getSecondStar(path + "/test1.txt");
    System.out.println(secondStar);

    int secondStar2 = getSecondStar(path + "/input.txt");
    System.out.println(secondStar2);
  }

  private static int getFirstStar(String input) {
    Objects.requireNonNull(input);
    var initSeq = parseFile(input);

    int sum = 0;
    for (char[] cs : initSeq) {
      sum = Math.addExact(sum, hash(cs));
    }

    return sum;
  }

  private static int getSecondStar(String input) {
    Objects.requireNonNull(input);

    var initSeq = parseFile(input);

    @SuppressWarnings("unchecked")
    ArrayList<Lens>[] boxes = (ArrayList<Lens>[]) new ArrayList[256];
    for (char[] code : initSeq) {

      char[] hash = null;
      int focalLength = -1;
      for (int i = code.length - 1; i >= 0; i--) {
        char ch = code[i];

        if (ch >= '0' && ch <= '9') {
          focalLength = ch - 48;
          assert focalLength >= 0 && focalLength <= 9;
        } else if (ch == '=' || ch == '-') {
          hash = new char[i];
        } else {
          if (hash == null) {
            throw new IllegalArgumentException();
          }
          hash[i] = ch;
        }
      }

      if (focalLength == -1) {
        removeLens(hash, boxes);
      } else {
        addOrChangeLens(hash, focalLength, boxes);
      }
    }

    int sum = 0;
    for (int i = 0; i < boxes.length; i++) {
      if (boxes[i] == null) continue;
      for (int j = 0; j < boxes[i].size(); j++) {
        Lens lens = boxes[i].get(j);

        sum = Math.addExact(sum, (i + 1) * (j + 1) * lens.focalLength);
      }
    }

    return sum;
  }

  private static void removeLens(char[] lable, ArrayList<Lens>[] boxes) {
    Objects.requireNonNull(lable);
    Objects.requireNonNull(boxes);

    int hash = hash(lable);
    ArrayList<Lens> box = boxes[hash];

    if (box == null) {
      boxes[hash] = box = new ArrayList<Lens>();
    }

    for (int i = 0; i < box.size(); i++) {
      if (Arrays.compare(box.get(i).lable, lable) == 0) {
        box.remove(i);
      }
    }
  }

  private static void addOrChangeLens(char[] lable, int focalLength, ArrayList<Lens>[] boxes) {
    Objects.requireNonNull(lable);
    Objects.requireNonNull(boxes);

    if (focalLength < 0 || focalLength > 9) {
      throw new IllegalArgumentException();
    }

    int hash = hash(lable);
    ArrayList<Lens> box = boxes[hash];

    if (box == null) {
      boxes[hash] = box = new ArrayList<Lens>();
    }

    for (int i = 0; i < box.size(); i++) {
      if (Arrays.compare(box.get(i).lable, lable) == 0) {
        box.set(i, new Lens(lable, focalLength));
        return;
      }
    }

    box.add(new Lens(lable, focalLength));
  }

  private static char[][] parseFile(String input) {
    Objects.requireNonNull(input);

    char[][] list = new char[10][];
    int listIndex = 0;
    char[] buffer = new char[2048];
    try (var buf = new BufferedReader(new FileReader(input))) {
      var code = new char[10];
      int codeIndex = 0;
      while (buf.ready()) {
        int read = buf.read(buffer);

        for (int i = 0; i < read; i++) {
          var ch = buffer[i];

          if (ch == ',' || (i == read - 1 && !buf.ready())) {
            if (listIndex >= list.length) list = expand(list);

            list[listIndex++] = Arrays.copyOf(code, codeIndex);
            codeIndex = 0;
          } else {
            // TODO potential out of bounds
            code[codeIndex++] = ch;
          }
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    return Arrays.copyOf(list, listIndex);
  }

  private static char[][] expand(char[][] list) {
    Objects.requireNonNull(list);

    char[][] res = new char[(int) (list.length + list.length * 0.5)][];

    for (int i = 0; i < list.length; i++) {
      res[i] = list[i];
    }

    return res;
  }

  private static int hash(char[] cs) {
    Objects.requireNonNull(cs);

    int hash = 0;
    for (char c : cs) {
      hash = Math.addExact(hash, (int) c);
      hash = Math.multiplyExact(hash, 17);
      hash = hash % 256;
    }

    return hash;
  }

  private static class Lens {
    public final char[] lable;
    public final int focalLength;

    public Lens(char[] lable, int focalLength) {
      this.lable = lable;
      this.focalLength = focalLength;
    }
  }
}
