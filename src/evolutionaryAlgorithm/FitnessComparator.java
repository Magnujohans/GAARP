package evolutionaryAlgorithm;


import java.util.Comparator;

/**
 * Created by Magnu on 10.12.2016.
 */

//This is just a comparator, so we can sort the Genotypes from best(lowest) to worst fitness.
public class FitnessComparator implements Comparator<Genotype> {
    @Override
    public int compare(Genotype a, Genotype b) {
        if (a.fitness > b.fitness){
            return 1;
        }
        if(a.fitness < b.fitness){
            return -1;
        }
        else{
            return 0;
        }
    }
}

