package ftkg.nqueens.implementation.backtrack;

public class NQueenSolverParallelBacktrackTest implements NQueenSolverTest<NQueenSolverParallelBacktrack> {
    @Override
    public NQueenSolverParallelBacktrack createSolver() {
        return new NQueenSolverParallelBacktrack();
    }
}