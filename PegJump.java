package student;

/*
 * This Student class is meant to contain your algorithm.
 * You should implement the static methods:
 *
 *   solvePegJump - finds a solution and the number of nodes examined in the search
 *                  it should fill in the jumpList argument with the jumps that form
 *                  your solution
 *
 * The input is a PegJumpPuzzle object, which has methods:
 *   numHoles()       returns the number of holes, numbered starting at 0
 *   getStartHole()   returns the starting hole that is initially empty
 *   jumpIterator()   returns an iterator that iterates through the possible jumps
 *   numJumps()       returns the number of possible jump moves in the puzzle board
 *                    (I never used this.)
 *
 * The jumpIterator() returns Jump objects which each represents a valid jump with three
 * fields: 'from', 'over', and 'dest'
 * There are getter methods to get these fields.
 *   getFrom()         Returns the hole that jump is "from".
 *   getOver()         Returns the hole that the jump is "over".
 *   getDest()         Returns the hole that is the "dest"-ination.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import tester.*;

public class PegJump {

  // returns:
  //   the number of jumps tried
  //   and as a modifiable argument, it fills in the jumpList
  public static long solvePegJump(
    PegJumpPuzzle puzzle,
    ArrayList<Jump> jumpList
  ) {
    int jumpCnt = 0;
    boolean foundAjump;
    ArrayList<ArrayList<Jump>> triedJumps = new ArrayList<>(puzzle.numHoles());
    Stack<Jump> jumps = new Stack<Jump>();
    // initialize the puzzle
    // make an array to keep tracks of where the pegs are
    // and put pegs in all holes except the starting hole
    boolean pegs[] = new boolean[puzzle.numHoles()]; // hole numbers start at 0
    for (int i = 0; i < puzzle.numHoles(); i++) pegs[i] = true; // fill all holes
    pegs[puzzle.getStartHole()] = false; // clear starting hole

    for (int i = 0; i < puzzle.numJumps(); i++) {
      triedJumps.add(new ArrayList<Jump>());
    }

    // start doing jumps
    do {
      Iterator<Jump> jitr = puzzle.jumpIterator();
      foundAjump = false;
      while (jitr.hasNext()) {
        Jump j = jitr.next();
        int from = j.getFrom();
        int over = j.getOver();
        int dest = j.getDest();
        if (
          pegs[from] &&
          pegs[over] &&
          !pegs[dest] &&
          !triedJumps.get(jumpCnt).contains(j)
        ) {
          // found a valid jump
          jumps.push(j); // add to the result list
          pegs[from] = false; // do the jump
          pegs[over] = false;
          pegs[dest] = true;
          jumpCnt++;
          foundAjump = true;
          break;
        }
      }
      //all valid jumps exhausted, if not done go back a jump and try next jump
      System.out.println(jumps);
      if (!foundAjump) {
        Jump lastJump;
        int count = 0;
        for (boolean peg : pegs) {
          if (peg) {
            count++;
          }
        }
        if (count > 1) {
          lastJump = jumps.pop();
          pegs[lastJump.getFrom()] = true;
          pegs[lastJump.getOver()] = true;
          pegs[lastJump.getDest()] = false;
          foundAjump = true;
          jumpCnt--;
          triedJumps.get(jumpCnt).add(lastJump);
          triedJumps.get(jumpCnt + 1).clear();
        }
      }
    } while (foundAjump);

    for (Jump jump : jumps) {
      jumpList.add(jump);
    }
    return jumpCnt;
  }
}
