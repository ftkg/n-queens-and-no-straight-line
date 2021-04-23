package ftkg.nqueens.internal;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class Utils {
    private Utils() {
    }

    /**
     * If the Throwable is an Error, throw it; if it is a RuntimeException return it, otherwise throw
     * IllegalStateException.
     *
     * @param t the Throwable
     * @return the RuntimeException, if it is one
     */
    public static RuntimeException launderThrowable(@Nullable Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }

    /**
     * Prints the chess board and queens to the console.
     * @param board the chess board
     * @param n the size of the board
     */
    public static void printSolution(boolean[][] board, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.printf(" %s ", (board[i][j] ? "Q" : "+"));
            System.out.println();
        }
    }
}
