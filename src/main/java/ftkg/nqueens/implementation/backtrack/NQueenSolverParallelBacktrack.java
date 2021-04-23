package ftkg.nqueens.implementation.backtrack;

import com.google.common.base.Preconditions;
import ftkg.nqueens.NQueenSolver;
import org.apache.commons.math3.fraction.Fraction;
import org.checkerframework.checker.nullness.qual.Nullable;
import ftkg.nqueens.internal.Utils;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.*;

/**
 * Implements the N-Queen problem using parallel backtracking, also avoiding 3-queen straight lines at any angle.
 * It decomposes the problem space by rows of the initial column and cancels the remaining tasks when a solution is
 * found. This solver will attempt to use all available cores in the virtual machine.
 * <p> <b>Design notes:</b>
 * <p> 1. For a larger value of N and number of cores this is not a very efficient implementation - even though it
 * can find faster solutions by looking at multiple beginnings concurrently, it is only eliminating the first column,
 * thus any performance gains quickly plateau in comparison to the total execution time. A more depth-focused approach
 * would be better, but require more work.
 * <p> 2. It is using a fixed thread pool with unbounded queue, since the value of N is well-known and "small"
 * . For <b>very</b> large numbers it could be changed into a bounded blocking queue to preserve memory.
 * <p> 3. Usage of recursion is ok for the reasons above, but for a very large N it could be changed to an iterative
 * version using a stack.
 */
@ThreadSafe
public final class NQueenSolverParallelBacktrack implements NQueenSolver {

    @Override
    public boolean[][] solve(int n) {
        Preconditions.checkArgument(n >= 0, "n must be a positive integer");
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.printf("Executing with %d threads...%n", cores);
        ExecutorService exec = Executors.newFixedThreadPool(cores);
        CompletionService<Context> completionService = new ExecutorCompletionService<>(exec);

        for (int row = 0; row < n; row++) {
            Context ctx = new Context(n);
            placeQueen(new Queen(row, 0), ctx);
            completionService.submit(() -> solveUtil(1, ctx));
        }
        // wait for each result from the output blocking queue
        for (int row = 0; row < n; row++) {
            try {
                Context result = completionService.take().get();
                if (result.isSolutionFound()) {
                    exec.shutdownNow();
                    return result.queensBoard;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throw Utils.launderThrowable(e.getCause());
            }
        }
        exec.shutdown();
        return new boolean[n][n];
    }

    /**
     * Solves the N-Queen problem recursively. Used as a {@link java.util.concurrent.Callable}, may be interrupted.
     * @param col The column to advance the search
     * @param ctx The current state
     * @return The result for the passed column
     */
    private Context solveUtil(int col, Context ctx) {
        // quit the worker gracefully
        if (col == ctx.n || Thread.interrupted()) return ctx.success();
        for (int row = 0; row < ctx.n; row++) {
            Queen queen = tryToPlace(row, col, ctx);
            if (queen != null) {
                placeQueen(queen, ctx);
                if (solveUtil(col + 1, ctx).isSolutionFound()) return ctx.success();
                else removeQueen(queen, ctx);
            }
        }
        return ctx.failure();
    }

    /**
     * Tries to place a new queen. A queen can be placed if:
     *
     * <p>1. Its square is safe (i.e., not attacked),
     * <p>2. It doesn't form a straight line with previous queens.
     *
     * @param row The row to place the new queen
     * @param col The column to place the new queen
     * @param ctx The current context
     * @return The new placed queen, or null
     */
    private @Nullable Queen tryToPlace(int row, int col, Context ctx) {
        if (ctx.attackedBoard[row][col] == 0) {
            Queen newQueen = new Queen(row, col);
            for (Queen pQueen : ctx.placedQueens) {
                Fraction vector = newQueen.getVector(pQueen);
                // check if it forms a 3-queen straight line
                if (pQueen.formsStraightLine(vector)) return null;
                // edge queens do not need to keep vectors because they are a dead-end
                else if (!newQueen.isAtEdge(ctx.n)) newQueen.addVector(vector);
            }
            return newQueen;
        } else return null;
    }

    /**
     * Places one queen and marks every square it attacks. Note that it only needs to mark forward squares (right row
     * and right diagonals).
     * @param queen the new queen to place
     * @param ctx The current context
     */
    private void placeQueen(Queen queen, Context ctx) {
        int row = queen.row;
        int col = queen.col;
        ctx.queensBoard[row][col] = true;
        //entire row
        for (int c = col; c < ctx.n; c++) {
            ctx.attackedBoard[row][c]++;
        }
        //upper right diagonal
        for (int r = row - 1, c = col + 1; r >= 0 && c < ctx.n; r--, c++) {
            ctx.attackedBoard[r][c]++;
        }
        //bottom right diagonal
        for (int r = row + 1, c = col + 1; r < ctx.n && c < ctx.n; r++, c++) {
            ctx.attackedBoard[r][c]++;
        }
        ctx.placedQueens.push(queen);
    }

    /**
     * Removes one queen and every square it attacks.
     * @param queen the queen to remove
     * @param ctx The current context
     */
    private void removeQueen(Queen queen, Context ctx) {
        int row = queen.row;
        int col = queen.col;
        ctx.queensBoard[row][col] = false;
        //entire row
        for (int c = col; c < ctx.n; c++) {
            ctx.attackedBoard[row][c]--;
        }
        //upper right diagonal
        for (int r = row - 1, c = col + 1; r >= 0 && c < ctx.n; r--, c++) {
            ctx.attackedBoard[r][c]--;
        }
        //bottom right diagonal
        for (int r = row + 1, c = col + 1; r < ctx.n && c < ctx.n; r++, c++) {
            ctx.attackedBoard[r][c]--;
        }
        ctx.placedQueens.pop();
    }
}
