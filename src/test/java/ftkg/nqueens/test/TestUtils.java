package ftkg.nqueens.test;

public class TestUtils {

    public static boolean checkAnyQueenAttacked(int n, final int row, final int col, boolean[][] board) {
        //entire row
        for (int c = col + 1; c < n; c++) {
            if (board[row][c]) return true;
        }
        //upper right diagonal
        for (int r = row - 1, c = col + 1; r >= 0 && c < n; r--, c++) {
            if (board[r][c]) return true;
        }
        //bottom right diagonal
        for (int r = row + 1, c = col + 1; r < n && c < n; r++, c++) {
            if (board[r][c]) return true;
        }
        return false;
    }
}
