import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Magnu on 29.08.2016.
 */
public class GenAlg {
    private int size;
    int[] currentBoard;
    public int nrSolutions;
    public Random rng;

    public int cost;

    public ArrayList<Move> neighborhood;
    public int bestSolution;
    public ArrayList<int[]> solutions;

    public ArrayList<Chromosome> population;
    public int tournamentSize;
    public double selectionProb;
    public int maxPop;
    public int offSpringPerEpoch;
    public double mutationRate;
    public double SurvivalRate;


    public GenAlg(int[] board) {
        this.currentBoard = board;
        distribute();
        this.size = board.length;
        this.bestSolution = size*size;
        this.neighborhood = new ArrayList<>();
        createNeighborhood(currentBoard);
        nrSolutions = 0;
        solutions = new ArrayList<int[]>();
        rng = new Random();
        cost = calculateCost(currentBoard);
        this.population = new ArrayList<>();
        tournamentSize = 4;
        selectionProb = 0.7;
        mutationRate = 0.2;
        maxPop = 25;
        offSpringPerEpoch = 8;
        SurvivalRate = 0.7;
        initialPopulation(size);


    }

    /**
     * As in Tabu search, we need a population where we have no more or less than one queen per row and column.
     * implements the distribute here for use in initialPopulation
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
    }

    /**
     * Creating the initial population, using the current board and the exchange operator
     */
    public void initialPopulation(int size){
        for(int x = 0; x< 2*size; x++){
            population.add(new Chromosome(makeMove(currentBoard,neighborhood.get(x)), calculateCost(makeMove(currentBoard,neighborhood.get(x)))));
        }
    }

    /**
     *This is where the actual tournament is held. We select a number of contestants, and assign them a given probability
     * where the fittest has a higher probability of being chosen (p) and the others lower(p*(1-p)^n)
     */
    public Chromosome tournamentSelection(){
        ArrayList<Integer> chosenIndices = new ArrayList<>();
        ArrayList<Chromosome> chosenChromosomes = new ArrayList<>();
        double prob;
        int temp;
        while(chosenChromosomes.size() < tournamentSize){
            temp = rng.nextInt(population.size());
            if(!chosenChromosomes.contains(population.get(temp))){
                chosenChromosomes.add(population.get(temp));
            }

            //chosenChromosomes.add(population.get(temp));
        }

        Collections.sort(chosenChromosomes);
        prob = rng.nextDouble();
        for(int i = 0; i < tournamentSize; i++){
            if (prob > selectionProb){
                prob = prob + (prob*(1-prob));
            }
            else{
                return chosenChromosomes.get(i);
            }
        }
        return chosenChromosomes.get(chosenChromosomes.size()-1);



    }

    /**
     * This is where we hold a tournament several times, and return a list of chosen parents.
     */
    public ArrayList<Chromosome> holdTournament(){
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        Chromosome selected;
        while (indices.size() < offSpringPerEpoch){
            selected = tournamentSelection();
            if(!indices.contains(population.indexOf(selected))) {
                indices.add(population.indexOf(selected));
            }
        }
        for(int i = 0; i < indices.size(); i++){
            chromosomes.add(population.get(indices.get(i)));
        }
        return chromosomes;

    }

    /**
     * Mutation is just an exchange operator, like in Tabu Search
     */
    public Chromosome Mutation(Chromosome chromosome){
        int[] temp;
        int random1 = rng.nextInt(chromosome.board.length);
        int random2 = rng.nextInt(chromosome.board.length);
        while(random1 == random2){
            random2 = rng.nextInt(chromosome.board.length);
        }
        temp = makeMove(chromosome.board, new Move(random1,random2));
        return new Chromosome(temp,calculateCost(temp));
    }

    /**
     * The mating process takes the outputs from several tournaments, mates them, and might also mutate them
     */
    public ArrayList<Chromosome> Mating(ArrayList<Chromosome> parents){
        ArrayList<Chromosome> offspring = new ArrayList<>();
        CrossOver temp;
        Chromosome tempOffspring1;
        Chromosome tempOffspring2;
        for(int i = 0; i < (parents.size()-1); i = i+2){
            temp = new CrossOver(parents.get(i).board,parents.get(i+1).board);
            tempOffspring1 = new Chromosome(temp.getOffspring1(), calculateCost(temp.getOffspring1()));
            if(rng.nextDouble() <= mutationRate){
                tempOffspring1 = Mutation(tempOffspring1);
            }
            tempOffspring2 = new Chromosome(temp.getOffspring2(), calculateCost(temp.getOffspring2()));
            if(rng.nextDouble() <= mutationRate){
                tempOffspring2 = Mutation(tempOffspring2);
            }
            offspring.add(tempOffspring1);
            offspring.add(tempOffspring2);
        }
        return offspring;
    }

    /**
     *here, we add all new offspring, and remove inhabitants given the survivalrate.
     */
    public void updatePopulation(ArrayList<Chromosome> newMembers) {
        for(int x = 0; x < newMembers.size(); x++){
            if(checkUniqueInhabitant(newMembers.get(x))){
                population.add(newMembers.get(x));
            }
        }
        Collections.sort(population);
        int remove = 1;
        while(population.size() > maxPop){
            if(rng.nextDouble() >= SurvivalRate){
                if(remove >= population.size()){
                    remove = 1;
                }
                population.remove(population.size()-remove);
            }
            remove++;
        }
    }


    /**
     * Same as in Tabu Search, finds best solution
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


    //
    public void createNeighborhood(int[] board) {
        int i, j;
        Move temp;

        for (i = 0; i < size; i++) {
            for (j = i+1; j < size; j++) {
                if(j >= size){
                    break;
                }
                temp = new Move(i, j);
                neighborhood.add(temp);
                }
            }
        }

    /**
     *Get the fitness of the best inhabitant
     */
    public int bestInhabitant(){
        Collections.sort(population);
        return population.get(0).cost;
    }

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
     *Checks the population for Unique Inhabitants
     */
    public boolean checkUniqueInhabitant(Chromosome newInhabitant){
        for(int x = 0; x < population.size(); x++){
            boolean unique = false;
            for(int z = 0; z < size; z++){
                if (population.get(x).board[z] != newInhabitant.board[z]){
                    unique = true;
                }
            }
            if (unique == false){
                return false;
            }

        }
        return true;
    }


    public void solve() {
        ArrayList<Chromosome> parents;
        ArrayList<Chromosome> offspring;
        Chromosome bestInhabitant;
        while (bestInhabitant() != 0) {
            parents = holdTournament();
            offspring = Mating(parents);
            updatePopulation(offspring);
            /*for(int z = 0; z < population.size(); z++){
                printQueens2(population.get(z).board);
                System.out.println(population.get(z).cost);
            }
            System.out.println(" - - - - - - - - - ");
            printQueens2(population.get(0).board);
            System.out.println(population.get(0).cost);*/
        }
        while(bestInhabitant() == 0) {
            bestInhabitant = population.remove(0);
            if(checkUniqueSolution(bestInhabitant.board)){
                solutions.add(bestInhabitant.board);
                //System.out.println(solutions.size());
                //printQueens2(bestInhabitant.board);
            }
            bestInhabitant = null;

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
        GenAlg Q = new GenAlg(posInt);
        long startTime;
        //long endTime;
        startTime = System.currentTimeMillis();/*
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
