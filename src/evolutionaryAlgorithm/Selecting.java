package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Magnu on 27.02.2017.
 */
public class Selecting {

    public static Random rng = new Random();
    public static final int tournamentSize = 5;
    public static final double selectionProb = 0.5;
    public static final double offSpringPerEpoch = 10;

    public static Genotype tournamentSelection(ArrayList<Genotype> population){
        ArrayList<Integer> chosenIndices = new ArrayList<>();
        ArrayList<Genotype> chosenChromosomes = new ArrayList<>();
        double prob;
        int temp;
        while(chosenChromosomes.size() < tournamentSize){
            temp = rng.nextInt(population.size());
            if(!chosenChromosomes.contains(population.get(temp))){
                chosenChromosomes.add(population.get(temp));
            }

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

    public static ArrayList<Genotype> holdTournament(ArrayList<Genotype> population){
        ArrayList<Genotype> chromosomes = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        Genotype selected;
        while (indices.size() < offSpringPerEpoch){
            selected = tournamentSelection(population);
            if(!indices.contains(population.indexOf(selected))) {
                indices.add(population.indexOf(selected));
            }
        }
        for(int i = 0; i < indices.size(); i++){
            chromosomes.add(population.get(indices.get(i)));
        }
        return chromosomes;

    }
}
