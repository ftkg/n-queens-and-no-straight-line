package ftkg.nqueens.implementation.backtrack;


import ftkg.nqueens.NQueenSolver;
import ftkg.nqueens.internal.DequeStack;
import ftkg.nqueens.internal.Stack;
import ftkg.nqueens.test.TestUtils;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public interface NQueenSolverTest<T extends NQueenSolver> {

    T createSolver();

    @ParameterizedTest
    @ValueSource(ints = { 1, 4, 8, 9, 10 })
    default void testBasicSolutions(int n) {
        Stack<Queen> queens = new DequeStack<>(n);
        boolean[][] result = createSolver().solve(n);
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                if (result[row][col]) {
                    Assertions.assertFalse(TestUtils.checkAnyQueenAttacked(n, row, col, result));
                    Queen newQueen = new Queen(row, col);
                    for (Queen q : queens) {
                        Fraction vector = newQueen.getVector(q);
                        assertFalse(q.formsStraightLine(vector));
                        newQueen.addVector(vector);
                    }
                    queens.push(newQueen);
                    break;
                }
            }
        }
        assertEquals(n, queens.size());
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 5, 6, 7 })
    default void testValuesWithNoSolution(int n) {
        int queens = 0;
        boolean[][] result = createSolver().solve(n);
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                assertFalse(result[row][col]);
            }
        }
        assertEquals(0, queens);
    }
}
