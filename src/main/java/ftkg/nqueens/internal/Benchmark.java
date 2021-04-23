package ftkg.nqueens.internal;

import ftkg.nqueens.NQueenSolver;
import ftkg.nqueens.implementation.backtrack.NQueenSolverBacktrack;
import ftkg.nqueens.implementation.backtrack.NQueenSolverParallelBacktrack;

final class Benchmark {
    private Benchmark() {
    }

    private static final int START_N = 15;
    private static final int END_N = 25;
    private static final double NANO_TO_MILLIS = 10e6;

    public static void main(String[] args) {
        NQueenSolver solver1 = new NQueenSolverBacktrack();
        NQueenSolver solver2 = new NQueenSolverParallelBacktrack();

        for (int n = START_N; n <= END_N; n++) {
            long l1 = System.nanoTime();
            Utils.printSolution(solver1.solve(n), n);
            System.out.printf("%s n=%d time=%f ms %n", solver1.getClass().getSimpleName(), n,
                    (System.nanoTime() - l1) / NANO_TO_MILLIS);

            long l2 = System.nanoTime();
            Utils.printSolution(solver2.solve(n), n);
            System.out.printf("%s n=%d time=%f ms %n", solver2.getClass().getSimpleName(), n,
                    (System.nanoTime() - l2) / NANO_TO_MILLIS);

            System.out.println("***********************************************************");
        }
    }
}
