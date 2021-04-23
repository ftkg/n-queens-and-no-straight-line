package ftkg.nqueens.implementation.backtrack;

import org.apache.commons.math3.fraction.Fraction;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a queen placed in the chess board and its connection vectors.
 */
class Queen {
    /**
     * Row this queen was placed on the board.
     */
    final int row;
    /**
     * Column this queen was placed on the board.
     */
    final int col;
    /**
     * Set of vectors this queen forms with other left-sided queens.
     * <p>As we are exclusively interested in straight lines, a vector of 2 points (p1,p2) can be defined by its slope
     * only: <tt>(p2.col-p1.col/p2.row-p1.row)</tt>
     */
    private final Set<Fraction> vectors = new HashSet<>();

    Queen(int row, int col) {
        this.row = row;
        this.col = col;
    }

    void addVector(Fraction vector) {
        this.vectors.add(vector);
    }

    /**
     * Checks if this queen is located at the edge of the board.
     * @param n the size of the board
     * @return <tt>true</tt> if it is at the edge
     */
    boolean isAtEdge(int n) {
        return row == 0 || row == n - 1 || col == 0 || col == n - 1;
    }

    /**
     * Checks if this vector is a straight continuation from a previous one at this node.
     * @param vector The new vector to check
     * @return <tt>true</tt> if this queen is in the middle of a 3-node straight line
     */
    boolean formsStraightLine(Fraction vector) {
        return this.vectors.contains(vector);
    }

    /**
     * Calculates the vector slope this queen forms with a previous (left-sided) queen.
     * @param prevQueen the previous queen
     * @return the vector slope fraction
     */
    Fraction getVector(Queen prevQueen) {
        return new Fraction(this.col - prevQueen.col, this.row - prevQueen.row);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queen queen = (Queen) o;
        return row == queen.row && col == queen.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
