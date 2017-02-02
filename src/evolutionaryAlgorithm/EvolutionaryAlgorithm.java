package evolutionaryAlgorithm;

import generalUtilities.FileSaving;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import parameterFiles.EvolutionaryAlgorithmParams;
import parameterFiles.EvolutionaryAlgorithmParams.AdultSelection;

public class EvolutionaryAlgorithm {

	/*TODO:
	 * initialize population
	 * 
	 * while some condition(s):
	 * 	make pairs
	 * 	do crossovers (two point, as described)
	 * 	calculate fitnesses
	 * 	do mutations with fitness checking
	 * 	update population
	 * 		find best inidvidual
	 * 		kill some at random
	 * 		make new population and all that stuff
	 * 	output logging data*/

	@SuppressWarnings("unused")
	public void run(){
		if(EvolutionaryAlgorithmParams.ADULT_SELECTION == AdultSelection.FULL_REPLACEMENT && (EvolutionaryAlgorithmParams.NUMBER_OF_CROSSOVER_PAIRS + 0.0) != (EvolutionaryAlgorithmParams.POPULATION_SIZE + 0.0) / 2.0){
			System.out.println("Full generational replacement requires that there are exactly as many new children as there are spots in the population. " +
					"Aborting because number of crossoverpair is not equal to half the population size.");
			return;
		}
		
		long generationNumber = 0;
		long startTime = System.currentTimeMillis();
		long timeTaken;
		long lastGenerationFitnessWasUpdated = 0;
		Genotype bestIndividual;
		Genotype copyOfBestIndividual;
		Genotype currentGenerationsCandidate;
		
		ArrayList<Genotype> adults = new ArrayList<>();
		ArrayList<Genotype> selectedParents = new ArrayList<>();
		ArrayList<Genotype> children = new ArrayList<>();
		
		while(adults.size() < EvolutionaryAlgorithmParams.POPULATION_SIZE){
			adults.add(new Genotype());
		}
		for (Genotype individual : adults) {
			individual.calculateFitness();
		}
		bestIndividual = new Genotype(Collections.max(adults));
		
		while(true){
//			System.out.println("Starting EA main loop");
			
			selectedParents.clear();
			selectedParents = Selection.selectParents(adults);
			
//			System.out.println("Parent selection complete");
			
			children.clear();
			for (int i = 0; i < selectedParents.size(); i += 2) {
				int[] crossoverPoints = Utilities.getRandomCrossoverPoints();
				Genotype[] generatedChildren = Utilities.crossover(selectedParents.get(i), selectedParents.get(i + 1), crossoverPoints[0], crossoverPoints[1]); 
				children.add(generatedChildren[0]);
				children.add(generatedChildren[1]);
			}
			
//			System.out.println("Parent selection complete");
			
			for (Genotype child : children) {
				child.mutate();
				child.calculateFitness();
			}
			
//			System.out.println("Mutation complete");
			
			
			/*Make sure a mutated version of last best joins this
			 * generations children so that we can see what happens
			 * when the last best is not only a parent, but also
			 * directly mutated*/
			if(EvolutionaryAlgorithmParams.ADD_MUTATED_BEST_TO_CHILDREN){
				children.remove(Collections.min(children));
				copyOfBestIndividual = new Genotype(bestIndividual);
				copyOfBestIndividual.mutate();
				children.add(copyOfBestIndividual);				
			}
			
			/*Resets the fitness of the parents before adult selection
			 * in case we did fitness proportionate scaling or something like that.*/
			for (Genotype adult : adults) {
				adult.calculateFitness();
			}
			bestIndividual.calculateFitness();
			
			Selection.adultSelection(adults, children);
			
//			System.out.println("Doing after generation cleanup");
			
			currentGenerationsCandidate = Collections.max(adults);
			if (currentGenerationsCandidate.compareTo(bestIndividual) >  0) {
//				System.out.println("CurGenCanFit:\t" + currentGenerationsCandidate.getFitness() + "\tOldBestFit:\t" + bestIndividual.getFitness());
				bestIndividual = new Genotype(currentGenerationsCandidate);
				lastGenerationFitnessWasUpdated = generationNumber;
			}
			/*Make sure the previous best is always in the set before we update it so that we don't casually discard a good solution*/
			adults.remove(Collections.min(adults));
			copyOfBestIndividual = new Genotype(bestIndividual);
			adults.add(copyOfBestIndividual);
			generationNumber++;
			
			/*Give regular output so human can track interestingness of progress*/
			if(generationNumber % 16 == 0){
				System.out.println("Generation:\t" + generationNumber + "\t" + "best fitness:\t" + bestIndividual.getFitness() + "\t" + "Population size:\t" + adults.size());
				FileSaving.appendGenerationStats(generationalStats(adults, bestIndividual, generationNumber), startTime); //Not really needed outside of testing either?
			}
			/*Save regularly so that crashes are less catastrophic*/
			if(generationNumber % EvolutionaryAlgorithmParams.GENERATION_LOGGING_FREQUENCY == 0){
				timeTaken = System.currentTimeMillis() - startTime;
				FileSaving.writeEntireRun(makeOutputString(adults, bestIndividual, timeTaken, generationNumber), bestIndividual.getFitness(), startTime);
				System.out.println("Run saved");
			}
			if( (generationNumber - lastGenerationFitnessWasUpdated > EvolutionaryAlgorithmParams.MAX_GENERATIONS_WITHOUT_CHANGE) || generationNumber > EvolutionaryAlgorithmParams.MAX_GENERATIONS){
				break;
			}
//			System.out.println("Generation complete");
		}
		
		timeTaken = System.currentTimeMillis() - startTime;
		
		FileSaving.writeEntireRun(makeOutputString(adults, bestIndividual, timeTaken, generationNumber), bestIndividual.getFitness(), startTime);
	}
	
	String generationalStats(ArrayList<Genotype> population, Genotype bestIndividual, long generationNumber){
		String output = "";
		double averageFitness = 0.0;
		double fitnessStandardDev = 0.0;
		
		for (Genotype genotype : population) {
			averageFitness += genotype.getFitness();
		}
		averageFitness /= population.size();
		for (Genotype genotype : population) {
			fitnessStandardDev += (genotype.getFitness() - averageFitness)*(genotype.getFitness() - averageFitness);
		}
		fitnessStandardDev /= population.size();
		fitnessStandardDev = Math.sqrt(fitnessStandardDev);
		
		output += "Generation:\t" + generationNumber + "\t" + 
				"Best fitness:\t" + bestIndividual.fitness  + "\t" +
				"Average fitness:\t" + averageFitness + "\t" +
				"Fitness standard deviation:\t" + fitnessStandardDev;
//				+ "\t" +
//				"Best genome:\t" + bestIndividual.getFormatedGenotypeTabSeparated();
		
		return output;
	}
	
	String makeOutputString(ArrayList<Genotype> population, Genotype bestIndividual, double timeTaken, long numberOfGenerations){
		String output = "";
		output += "Instance name:\t" 			+ Graph.problemName + "\n";
		output += "EA number of generations:\t" + numberOfGenerations + "\n";
		output += "EA time used:\t" 			+ (timeTaken + 0.0)/1000.0 + "s" + "\n";
		output += "EA best genome:\t" 			+ bestIndividual.getFormatedGenotypeCommaSeparated() + "\n";
		output += "EA best genome fitness:\t" 	+ bestIndividual.getFitness() + "\n";
		output += "EA trip genome encodes:\t"	+ bestIndividual.getEntireTripGenomeEncodes() + "\n";
		output += "\n";
		output += "Random seed used:\t" 		+ Utilities.getSeed() + "\n";
		output += "\n";
		output += "EA param: " + "MAX_GENERATIONS\t= " 					+ EvolutionaryAlgorithmParams.MAX_GENERATIONS + "\n";
		output += "EA param: " + "MAX_GENERATIONS_WITHOUT_CHANGE\t= " 	+ EvolutionaryAlgorithmParams.MAX_GENERATIONS_WITHOUT_CHANGE + "\n";
		output += "EA param: " + "POPULATION_SIZE\t= " 					+ EvolutionaryAlgorithmParams.POPULATION_SIZE + "\n";
		output += "EA param: " + "NUMBER_OF_CROSSOVER_PAIRS\t= " 		+ EvolutionaryAlgorithmParams.NUMBER_OF_CROSSOVER_PAIRS + "\n";
		output += "EA param: " + "FINTESS_TYPE\t= " 					+ EvolutionaryAlgorithmParams.FINTESS_TYPE + "\n";
		output += "EA param: " + "PENALIZE_DEMAND_OUT_OF_ORDER\t= " 	+ EvolutionaryAlgorithmParams.PENALIZE_DEMAND_OUT_OF_ORDER + "\n";
		output += "EA param: " + "RANDOM_MUTATION\t= " 					+ EvolutionaryAlgorithmParams.RANDOM_MUTATION + "\n";
		output += "EA param: " + "PARENT_SELECTION\t= " 				+ EvolutionaryAlgorithmParams.PARENT_SELECTION + "\n";
		output += "EA param: " + "ADULT_SELECTION\t= " 					+ EvolutionaryAlgorithmParams.ADULT_SELECTION + "\n";
		output += "EA param: " + "TOURNAMENT_SIZE\t= " 					+ EvolutionaryAlgorithmParams.TOURNAMENT_SIZE + "\n";
		output += "EA param: " + "TOURNAMEN_SELECTION_PROBABILITY\t= " 	+ EvolutionaryAlgorithmParams.TOURNAMEN_SELECTION_PROBABILITY + "\n";
		output += "\n";
		output += "\n";
		ArrayList<int[]> trips = FitnessModule.split(bestIndividual,true);
		output += "EA best genome fitness when using split:\t" + bestIndividual.getFitness() + "\n";
		output += "EA trips from best tour splitted:\n";
		for (int i = 0; i < trips.size(); i++) {
			output += "\tTrip " + i + ":\t";
			for (int elementID : trips.get(i)) {
				output += Graph.getElementByID(elementID).getName();
				output += ",";
			}
			output += "\n";
		}
		return output;
	}

}































