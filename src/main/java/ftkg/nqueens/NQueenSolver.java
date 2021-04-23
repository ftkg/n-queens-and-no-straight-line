package ftkg.nqueens;

/**
 * Algorithm which finds a solution to placing N queens on an NxN chess board, so that none of them attack each other.
 * It also avoids 3 queens in a straight line (at any angle).
 */
public interface NQueenSolver {

    /**
     * @param n the number of queens to be placed into an nxn chess board
     * @return an nxn matrix representing the chess board with placed queens; if no solution was found, returns an
     * empty board
     */
    boolean[][] solve(int n);
}


