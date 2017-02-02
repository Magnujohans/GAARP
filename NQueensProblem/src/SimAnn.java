import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Magnu on 29.08.2016.
 */
public class SimAnn {
    private int size;
    int[] currentBoard;
    int[] newBoard;
    public ArrayList<int[]> solutions;
    public Random rng;

    public int cost;


    public int temperature;
    public double dropRate;


    public SimAnn(int[] board) {
        this.currentBoard = board;
        this.size = board.length;
        rng = new Random();
        cost = calculateCost(currentBoard);
        this.temperature = 100;
        this.dropRate = 0.9;
        solutions = new ArrayList<>();
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
     *Get next board by randomizing a queen in a position
     */
    public int[] getNextBoard(int rounds) {
        int[] temp = currentBoard.clone();
        for(int x = 0; x < rounds; x++){
            int queenToChange = rng.nextInt(size);
            int up = rng.nextInt(size);
            temp[queenToChange] = up;
        }

        return temp;

    }

    public void solve() {
        while (cost != 0) {
            double temperature;
            double delta;
            double probability;
            double rand;
            int counter = 0;


            for (temperature = this.temperature; (temperature > 0) && (calculateCost(currentBoard) != 0); temperature = temperature * dropRate) {
                newBoard = this.getNextBoard(1);
                delta = calculateCost(currentBoard) - calculateCost(newBoard);
                probability = Math.exp(delta / temperature);
                rand = Math.random();

                if (delta > 0) {
                    currentBoard = newBoard;
                    cost = calculateCost(newBoard);
                    counter = 0;
                } else if (rand <= probability) {
                    currentBoard = newBoard;
                    cost = calculateCost(newBoard);
                    counter = 0;
                } else{
                    cost = calculateCost(currentBoard);
                    counter++;
                }
                if (counter >= 3*size){
                    currentBoard = getNextBoard(size);
                    counter = 0;
                    temperature = this.temperature;
                }
            }
        }
        if (cost == 0) {
            //temperature = 100;
            cost = 100;
            if(checkUniqueSolution(currentBoard)){
                solutions.add(currentBoard);
                //printQueens2(currentBoard);
                //System.out.println(solutions.size());
            }
            currentBoard = getNextBoard(size);
            //printQueens2(currentBoard);
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
        SimAnn Q = new SimAnn(posInt);
        long startTime;
        //long endTime;

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
