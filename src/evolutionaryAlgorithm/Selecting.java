package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Magnu on 27.02.2017.
 */
public class Selecting {

    public Random rng = new Random();
    public int tournamentSize = 2;
    public double selectionProb = 1;
    public double offSpringPerEpoch;
    public int nElite;
    public int nPopulation;


    //This is the class holding selecting parents, and binary tournament
    public Selecting(double offSpringPerEpoch, int nElite, int nPopulation){
        this.offSpringPerEpoch = offSpringPerEpoch;
        this.nElite = nElite;
        this.nPopulation = nPopulation;
    }
    //Updates the Elite factor, increasing the importance of makespan. This is for the adaptive version
    public void updateElite(int nElite){
        this.nElite = nElite;
    }

    //Selects the parents by using the functions further down
    public Genotype tournamentSelection(ArrayList<Genotype> population){
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

        Collections.sort(chosenChromosomes, new BFComparator(nElite, nPopulation));
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

    //Holds one tournament to return one parent
    public ArrayList<Genotype> holdTournament(ArrayList<Genotype> population){
        ArrayList<Genotype> chromosomes = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        Genotype selected;
        while (indices.size() < offSpringPerEpoch){
            selected = tournamentSelection(population);
            indices.add(population.indexOf(selected));

            /*if(!indices.contains(population.indexOf(selected))) {
                indices.add(population.indexOf(selected));
            }*/
        }
        for(int i = 0; i < indices.size(); i++){
            chromosomes.add(population.get(indices.get(i)));
        }
        return chromosomes;

    }

    //Uses the PMX to create two offspring
    public ArrayList<Genotype> Mating(ArrayList<Genotype> population, Fenotype fenotype){
        ArrayList<Genotype> parents = holdTournament(population);
        ArrayList<Genotype> offspring = new ArrayList<>();
        FMXCrossover tempLane;
        FMXCrossover tempSidewalk;
        Genotype tempOffspring1;
        Genotype tempOffspring2;
        for(int i = 0; i < (parents.size()-1); i = i+2){
            tempLane = new FMXCrossover(parents.get(i).getLaneGenome(),parents.get(i+1).getLaneGenome());
            tempSidewalk = new FMXCrossover(parents.get(i).getSidewalkGenome(),parents.get(i+1).getSidewalkGenome());
            tempOffspring1 = new Genotype(tempLane.getOffspring1(), tempSidewalk.getOffspring1(), fenotype.calculateFitness(tempLane.getOffspring1(), tempSidewalk.getOffspring1()));
            tempOffspring2 = new Genotype(tempLane.getOffspring2(), tempSidewalk.getOffspring2(), fenotype.calculateFitness(tempLane.getOffspring2(), tempSidewalk.getOffspring2()));
            /*if(rng.nextDouble() <= mutationRate){
                tempOffspring1 = Mutation(tempOffspring1);
            }*/
            offspring.add(tempOffspring1);
            offspring.add(tempOffspring2);
        }
        return offspring;
    }

}
