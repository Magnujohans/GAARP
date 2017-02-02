/**
 * Created by Magnu on 25.09.2016.
 */

/**
 * The Chromosome is represented as a int[], but we have added the fitness, along with the comparable interface
 */
public class Chromosome implements Comparable{
    public int[] board;
    public int cost;

    public Chromosome(int[] board, int cost) {
        this.board = board;
        this.cost = cost;
    }

    @Override
    public int compareTo(Object other) {
        return this.cost - ((Chromosome) other).cost;
    }
}
