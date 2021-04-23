package ftkg.nqueens.implementation.backtrack;

import com.google.common.base.Preconditions;
import ftkg.nqueens.NQueenSolver;
import org.apache.commons.math3.fraction.Fraction;
import org.checkerframework.checker.nullness.qual.Nullable;
import ftkg.nqueens.internal.DequeStack;
import ftkg.nqueens.internal.Stack;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Implements the N-Queen problem using backtracking, avoiding 3-queen straight lines at any angle. This object is
 * stateful and therefore will block on reuse while processing.
 * <p> <b>Design notes:</b>
 * <p> 1. A Context object holding all the shared fields could be used to remove this object state and allow
 * concurrent usage, make the methods pure (side-effect free) and improve testing granularity. The current code was
 * kept for its simplicity and readability, avoiding the passing/receiving of an extra object everywhere.
 * <p> 2. Incrementing attacked squares at queen placement proved to be a large performance gain against checking every
 * square safety by searching the board for queens (frequency upper bound of n! vs n^n).
 */
@ThreadSafe
public final class NQueenSolverBacktrack implements NQueenSolver {

    /**
     * Number of queens.
     */
    @GuardedBy("this") private volatile int nQueens;
    /**
     * Chess board with the queen pieces.
     */
    @GuardedBy("this") private volatile boolean[][] queensBoard = new boolean[0][0];
    /**
     * Chess board with attacked/occupied squares.
     */
    @GuardedBy("this") private volatile int[][] attackedBoard = new int[0][0];
    /**
     * Stack of placed queens, the rightmost being at the top.
     */
    @GuardedBy("this") private volatile Stack<Queen> placedQueens = new DequeStack<>(0);

    @Override
    public synchronized boolean[][] solve(int n) {
        Preconditions.checkArgument(n >= 0, "n must be a positive integer");
        initializeState(n);
        if (solveUtil(0)) return queensBoard;
        else return new boolean[n][n];
    }

    private void initializeState(int n) {
        this.nQueens = n;
        this.queensBoard = new boolean[n][n];
        this.attackedBoard = new int[n][n];
        this.placedQueens = new DequeStack<>(n);
    }

    private boolean solveUtil(int col) {
        if (col == nQueens) return true;
        for (int row = 0; row < nQueens; row++) {
            Queen queen = tryToPlace(row, col);
            if (queen != null) {
                placeQueen(queen);
                if (solveUtil(col + 1)) return true;
                else removeQueen(queen);
            }
        }
        return false;
    }

    /**
     * Tries to place a new queen. A queen can be placed if:
     *
     * <p>1. Its square is safe (i.e., not attacked),
     * <p>2. It doesn't form a straight line with previous queens.
     *
     * @param row The row to place the new queen
     * @param col The column to place the new queen
     * @return The new placed queen, or null
     */
    private @Nullable Queen tryToPlace(int row, int col) {
        if (attackedBoard[row][col] == 0) {
            Queen newQueen = new Queen(row, col);
            for (Queen pQueen : placedQueens) {
                Fraction vector = newQueen.getVector(pQueen);
                // check if it forms a 3-queen straight line
                if (pQueen.formsStraightLine(vector)) return null;
                // edge queens do not need to keep vectors because they are a dead-end
                else if (!newQueen.isAtEdge(nQueens)) newQueen.addVector(vector);
            }
            return newQueen;
        } else return null;
    }

    /**
     * Places one queen and marks every square it attacks. Note that it only needs to mark forward squares (right row
     * and right diagonals).
     * @param queen the new queen to place
     */
    private void placeQueen(Queen queen) {
        int row = queen.row;
        int col = queen.col;
        queensBoard[row][col] = true;
        //entire row
        for (int c = col; c < nQueens; c++) {
            attackedBoard[row][c]++;
        }
        //upper right diagonal
        for (int r = row - 1, c = col + 1; r >= 0 && c < nQueens; r--, c++) {
            attackedBoard[r][c]++;
        }
        //bottom right diagonal
        for (int r = row + 1, c = col + 1; r < nQueens && c < nQueens; r++, c++) {
            attackedBoard[r][c]++;
        }
        placedQueens.push(queen);
    }

    /**
     * Removes one queen and every square it attacks.
     * @param queen the queen to remove
     */
    private void removeQueen(Queen queen) {
        int row = queen.row;
        int col = queen.col;
        queensBoard[row][col] = false;
        //entire row
        for (int c = col; c < nQueens; c++) {
            attackedBoard[row][c]--;
        }
        //upper right diagonal
        for (int r = row - 1, c = col + 1; r >= 0 && c < nQueens; r--, c++) {
            attackedBoard[r][c]--;
        }
        //bottom right diagonal
        for (int r = row + 1, c = col + 1; r < nQueens && c < nQueens; r++, c++) {
            attackedBoard[r][c]--;
        }
        placedQueens.pop();
    }
}
