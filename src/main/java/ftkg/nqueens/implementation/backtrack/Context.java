package ftkg.nqueens.implementation.backtrack;

import ftkg.nqueens.internal.DequeStack;
import ftkg.nqueens.internal.Stack;

/**
 * A context object for the {@link NQueenSolverParallelBacktrack} implementation.
 */
class Context {
    /**
     * Number of queens.
     */
    final int n;
    /**
     * Chess board with the queen pieces.
     */
    final boolean[][] queensBoard;
    /**
     * Chess board with attacked/occupied squares.
     */
    final int[][] attackedBoard;
    /**
     * Stack of placed queens, the rightmost being at the top.
     */
    final Stack<Queen> placedQueens;

    /**
     * Whether a solution was a found in this context.
     */
    private boolean isSolutionFound;

    public boolean isSolutionFound() {
        return isSolutionFound;
    }

    Context(int n) {
        this.n = n;
        this.queensBoard = new boolean[n][n];
        this.attackedBoard = new int[n][n];
        this.placedQueens = new DequeStack<>(n);
    }

    Context success() {
        this.isSolutionFound = true;
        return this;
    }

    Context failure() {
        this.isSolutionFound = false;
        return this;
    }

}
