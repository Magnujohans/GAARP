import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Magnu on 29.08.2016.
 */
public class Tabu {
    private int size;
    int[] currentBoard;
    public Random rng;

    public int cost;


    public TreeMap<Integer, Move> neighborhood;
    public ArrayList<Move> tabuList;
    public int bestSolution;
    public ArrayList<int[]> solutions;
    public int maxTabuList;


    public Tabu(int[] board) {
        this.currentBoard = board;
        distribute();
        this.size = board.length;
        this.bestSolution = size*size;
        this.neighborhood = new TreeMap<Integer, Move>();
        createNeighborhood(currentBoard);
        tabuList = new ArrayList<Move>();
        solutions = new ArrayList<int[]>();
        rng = new Random();
        cost = calculateCost(currentBoard);
        maxTabuList = 20;


    }

    /**
     Used when the input is not on a format with one queen for every row and every column.
     */
    public void distribute() {
        ArrayList<Integer> allNumbers = new ArrayList<Integer>();
        ArrayList<Integer> doubleNumbers = new ArrayList<Integer>();
        for(int i = 0; i < currentBoard.length; i++){
            allNumbers.add(i);
        }
        for(int i = 0; i < currentBoard.length; i++){
            if(allNumbers.contains(currentBoard[i])){
                allNumbers.remove(allNumbers.indexOf(currentBoard[i]));
            }
        }
        for(int i = 0; i < currentBoard.length; i++){
            if(doubleNumbers.contains(currentBoard[i])){
                currentBoard[i] = allNumbers.remove(0);
            }
            else{
                doubleNumbers.add(currentBoard[i]);
            }
        }
        printQueens2(currentBoard);
    }

    /**
     * Makes a move on a board, and returns it.
     */
    public int[] makeMove(int[] board, Move exchange) {
        int[] temp = board.clone();

        int a, b;
        a = temp[exchange.getQueen1()];
        b = temp[exchange.getQueen2()];
        temp[exchange.getQueen1()] = b;
        temp[exchange.getQueen2()] = a;

        return temp;
    }

    /**
     * Checks tabulist for duplicates
     */
    public boolean tabuListContains(Move move){
        for(Move x: tabuList){
            if ((x.getQueen1() == move.getQueen1() && x.getQueen2() == move.getQueen2())
                    || (x.getQueen2() == move.getQueen1() && x.getQueen1() == move.getQueen2())){
                return true;
            }
        }
        return false;
    }


    /**
     * Make a duplicate of the board, and do a move.
     * Does this many times
     */
    public void createNeighborhood(int[] board) {
        int i, j;
        Move temp;
        for (i = 0; i < size; i++) {
            for (j = i+1; j < size; j++) {
                if(j >= size){
                    break;
                }
                temp = new Move(i, j);
                neighborhood.put(calculateCost(makeMove(currentBoard,temp)), temp);
                }
            }
        }

    /**
     * The TreeMap is always sorted on Key (fitness) with the best fitness at the top.
     * Returns the best move, if it's better than everything we have seen.
     * Else, check the tabulist
     */
    public Move getBestMove(){
        if (neighborhood.firstKey() > bestSolution){
            bestSolution = neighborhood.firstKey();
            return neighborhood.firstEntry().getValue();
        }
        for(Move move: neighborhood.values()){
            if (!tabuListContains(move)){
                return move;
            }
        }
        return new Move(0,0);
    }

    /**
     * Calculates the number of conflicts
     */
    public int calculateCost(int[] board) {
        int i, j;
        int boardCost = 0;

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i == j) continue;
                if (board[i] == board[j] // same row
                        || (i - j == board[i] - board[j]) // same diagonal
                        || (i - j == board[j] - board[i])) { //same counter diagonal
                    boardCost++;
                }
            }
        }

        return boardCost / 2;

    }

    public boolean checkUniqueSolution(int[] newSolution){
        for(int x = 0; x < solutions.size(); x++){
            boolean unique = false;
            for(int z = 0; z < size; z++){
                if (solutions.get(x)[z] != newSolution[z]){
                    unique = true;
                }
            }
            if (unique == false){
                return false;
            }

        }
        return true;
    }

    /**
     * If We have reached max tabulist size, we remove the move that was taken farthest awey in time
     */
    public void modifyTabuList(Move move){
        if(tabuList.size() >= maxTabuList){
            tabuList.remove(0);
        }
        tabuList.add(move);
        //System.out.println(tabuList.get(0).getQueen1() + " " + tabuList.get(0).getQueen2() );
    }


    public void solve() {
        Move bestMove;
        cost = size*size;
        while (cost != 0) {
            bestMove = getBestMove();
            modifyTabuList(bestMove);
            currentBoard = makeMove(currentBoard,bestMove);
            createNeighborhood(currentBoard);
            cost = calculateCost(currentBoard);
        }
        if (cost == 0) {
            //System.out.println("Fant en løsning");
            if(checkUniqueSolution(currentBoard)){
                solutions.add(currentBoard);
                //printQueens2(currentBoard);
                //System.out.println(solutions.size());
            }
        }

    }

    /**
     * This is the visual way of printing
     */
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
    /**
     * This is the standard way of printing
     */
    public void printQueens2(int[] x) {
        int N = x.length;
        String printed = " ";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (x[i] == j) {
                    printed = printed + " " + (j + 1);
                }
            }
        }
        System.out.println(printed);
    }

    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a number: ");
        String input = reader.nextLine();


        final Pattern pattern = Pattern.compile("[0-9 ]+");
        if (!pattern.matcher(input).matches()) {
            throw new IllegalArgumentException("Invalid String");
        }
        String[] pos = input.split("\\s+");
        int[] posInt = new int[pos.length];

        for (int x = 0; x < pos.length; x++) {
            posInt[x] = (Integer.parseInt(pos[x]) - 1);

        }
        Tabu Q = new Tabu(posInt);
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        /*
        while(Q.solutions.size() < 10000){
            Q.solve();
        }
        endTime = System.currentTimeMillis();
        System.out.println((endTime-startTime));*/

        while (System.currentTimeMillis() - startTime < 0.5*60*1000){
            Q.solve();
        }
        System.out.println(Q.solutions.size());
    }
}
