import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Magnu on 29.08.2016.
 */

/**
 * This is the assignment for Task 2
 */
public class Chessboard2 {
    private int size;
    private int predet;
    int[] board;
    public int solutions;


    public Chessboard2(int[] board){
        this.board = board;
        this.size = board.length;
        howManyDetermined();
        if(!this.checkExistingQueens()){
            throw new IllegalArgumentException("The queens you entered are threatening eachother");
        }
        solutions = 0;
    }

    public void howManyDetermined(){
        for (int i = 0; i < size; i++) {
            if (board[i] != -1) {
                predet +=1;
            }
        }
    }

    public boolean canPlaceQueen(int row, int column) {
        /**
         * Returns TRUE if a queen can be placed in row r and column c.
         * Otherwise it returns FALSE. x[] is a global array whose first (r-1)
         * values have been set.
         */
        for (int i = 0; i < row; i++) {
            if (board[i] == column || (i - row) == (board[i] - column) || (i - row) == (column - board[i])) {
                return false;
            }
        }
        return true;
    }


    public boolean checkExistingQueens(){
        for(int i = 0; i < board.length; i++){
            if(board[i] != -1){
                if(canPlaceQueen(i, board[i]) == false) {
                    System.out.println("There is a threat between your queens");
                    return false;
                }
            }
        }
        System.out.println("Her stemmer alt");
        return true;
    }

    public void placeNqueens(int row, int n) {
        /**
         * Using backtracking this method prints all possible placements of n
         * queens on an n x n chessboard so that they are non-attacking.
         */
        for (int column = 0; column < n; column++) {
            if (canPlaceQueen(row, column)) {
                board[row] = column;
                if (row == n - 1) {
                    //printQueens2(board);
                    solutions += 1;
                } else {
                    placeNqueens(row + 1, n);
                }
            }

        }
    }

    public void printQueens(int[] x) {
        int N = x.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (x[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public void printQueens2(int[] x){
        int N = x.length;
        String printed = " ";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (x[i] == j) {
                    printed = printed + " " + (j+1);
                }
            }
        }
        System.out.println(printed);
    }

    public void callplaceNqueens() {
        placeNqueens(predet, board.length);
        System.out.println(solutions);
    }

    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the size: ");
        String size = reader.nextLine();
        System.out.println("Enter the board: ");
        String input = reader.nextLine();


        final Pattern inputPattern = Pattern.compile("[0-9 ]+");
        //final Pattern sizePattern = Pattern.compile("[0-9 ]");
        if (!inputPattern.matcher(input).matches()) {
            throw new IllegalArgumentException("Invalid String");
        }
        /*
        if (!sizePattern.matcher(size).matches()) {
            throw new IllegalArgumentException("Invalid int");
        }
        */
        String[] pos = input.split("\\s+");
        int[] posInt = new int[pos.length];
        int sizeInt = Integer.parseInt(size);

        if(sizeInt != posInt.length){
            throw new IllegalArgumentException("Mismatch between selected size and input");
        }

        for(int x = 0; x < pos.length; x++){
            posInt[x] = (Integer.parseInt(pos[x]) - 1);

        }
        Chessboard2 Q = new Chessboard2(posInt);
        long startTime = System.currentTimeMillis();
        Q.callplaceNqueens();
        long endTime = System.currentTimeMillis();
        System.out.println((endTime-startTime));
    }
}
