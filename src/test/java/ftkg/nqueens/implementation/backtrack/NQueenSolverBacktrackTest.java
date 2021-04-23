package ftkg.nqueens.implementation.backtrack;

public class NQueenSolverBacktrackTest implements NQueenSolverTest<NQueenSolverBacktrack> {
    @Override
    public NQueenSolverBacktrack createSolver() {
        return new NQueenSolverBacktrack();
    }
}