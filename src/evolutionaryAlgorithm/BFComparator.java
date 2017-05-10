package evolutionaryAlgorithm;


import java.util.Comparator;

/**
 * Created by Magnu on 10.12.2016.
 */

//This is just a comparator, so we can sort the Genotypes from best(lowest) to worst fitness.
public class BFComparator implements Comparator<Genotype> {
    int nElite;
    int population;

    public BFComparator(int nElite, int population){
        this.nElite = nElite;
        this.population = population;
    }

    @Override
    public int compare(Genotype a, Genotype b) {
        double rankA = a.fitnessRank + (1-(nElite/population))*a.diversityRank;
        double rankB = b.fitnessRank + (1-(nElite/population))*b.diversityRank;


        if (rankA > rankB){
            return 1;
        }

        if(rankA < rankB){
            return -1;
        }
        else{
            return a.compareTo(b);
        }
    }
}

