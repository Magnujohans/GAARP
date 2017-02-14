package evolutionaryAlgorithm;

import java.util.ArrayList;

import parameterFiles.EvolutionaryAlgorithmParams;
import graph.FloydWarshall;
import graph.Graph;
import InitialSolution.Algorithm;
import InitialSolution.Vehicle;
import InitialSolution.Arc;
import InitialSolution.ArcNodeIdentifier;
import InitialSolution.Node;

public class Genotype implements Comparable<Genotype>{
	int[] laneGenome;
	int[] sidewalkGenome;
	double fitness = -1.0;
	ArrayList<Arc> arcs;

	
	public Genotype(){
		initializeRandomly();
	}

	public Genotype(ArrayList<Vehicle> initialVehicles, ArrayList<Arc> arcs){
		this.arcs = arcs;
	}

	public Genotype(ArrayList<Arc> arcs){
		this.arcs = arcs;
	}

	public Genotype(Genotype genotype){
		this.laneGenome = genotype.getLaneGenome().clone();
		this.sidewalkGenome = genotype.getSidewalkGenome().clone();
		this.calculateFitness();
	}
	public Genotype(int[] laneGenome, int[] sidewalkGenome) {
		this.laneGenome = laneGenome.clone();
		this.sidewalkGenome = sidewalkGenome.clone();
	}
	
	/**The indexes of the (required) graph-elements of the trip this genome encodes in the order they are in the tour.*/

	
	/**Returns the fitness of this genome. If it hasn't
	 * been calculated already it calculates and sets it
	 * before it returns it.*/
	public double getFitness(){
		if(fitness == -1.0){
			calculateFitness();
		}
		return fitness;
	}
	
	/**Calculates the fitness of this genome. Chooses how
	 * to calculate it based on the FITNESS_TYPE variable
	 * in EA-params*/
	public void calculateFitness(){
		if(EvolutionaryAlgorithmParams.FINTESS_TYPE == EvolutionaryAlgorithmParams.fitnessType.GRAND_TOUR){
			this.fitness = FitnessModule.tripCost(laneGenome);
			this.fitness = FitnessModule.
		}else if(EvolutionaryAlgorithmParams.FINTESS_TYPE == EvolutionaryAlgorithmParams.fitnessType.SPLITTED){
			FitnessModule.split(this, false);
		}
	}
	
	/**Sets this genomes fitness to the given value.
	 * Useful for normalizing fitnesses*/
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	/*
	public void mutate(){
		if(!EvolutionaryAlgorithmParams.RANDOM_MUTATION){
			double genomeProperFitness = FitnessModule.tripCost(genome);	//In case fitness has been changed by fitness scaling selection
			for (int i = 0; i < genome.length; i++) {
				j = i because no need to check swaps already checked
				for (int j = i; j < genome.length - i; j++) {
					Utilities.swap(genome, i, j);
					if(FitnessModule.tripCost(genome) < genomeProperFitness){	//Less fitness is better
						calculateFitness();
						return;
					}
					Utilities.swap(genome, i, j);	//Undo the attempt before continuing
				}
			}
		}
		//No improvement was found or random was selected, flip two randomly
		int[] randomPoints = Utilities.getRandomCrossoverPoints();
		Utilities.swap(genome, randomPoints[0], randomPoints[1]);
		calculateFitness();
	}
	*/
	
	public int[] getLaneGenome(){
		return this.laneGenome.clone();
	}

	public int[] getSidewalkGenome(){
		return this.sidewalkGenome.clone();
	}

	public String getEntireTripGenomeEncodes(){
		ArrayList<Integer> trip = FloydWarshall.completePathThroughElementsUsingDepotNode(genome);

		String output = "";
		for (Integer elementID : trip) {
			output += Graph.getElementByID(elementID).getName();
			output += ",";
		}
		return output;
	}
	

	void initializeRandomly(){
		genome = Graph.getRequiredElementsIDs().clone();
		Utilities.shuffle(genome);
	}
	
	public String getFormatedGenotypeTabSeparated(){
		String output = "";
		for (int elementID : genome) {
			output += Graph.getElementByID(elementID).getName();
			output += "\t";
		}
		return output;
	}
	
	public String getFormatedGenotypeCommaSeparated(){
		String output = "";
		for (int elementID : genome) {
			output += Graph.getElementByID(elementID).getName();
			output += ",";
		}
		return output;
	}


	@Override
	public int compareTo(Genotype otherGenotype) {
		/*Pushes lower fitnesses further to the right,
		 * because in some other places (selection) we
		 * rely on that the best values are found at the far right.*/
		if(this.fitness < otherGenotype.fitness){
			return 1;
		}else if(this.fitness > otherGenotype.fitness){
			return -1;
		}else{
			return 0;			
		}
	}
}
