package evolutionaryAlgorithm;

import com.sun.org.apache.xpath.internal.SourceTreeManager;
import graph.Graph;
import InitialSolution.Algorithm;
import InitialSolution.Vehicle;
import InitialSolution.Arc;
import InitialSolution.ArcNodeIdentifier;
import InitialSolution.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import parameterFiles.EvolutionaryAlgorithmParams;
import parameterFiles.EvolutionaryAlgorithmParams.AdultSelection;

public class EvolutionaryAlgorithm {

	Fenotype fenotype;
	Education education;
	Random rng = new Random();
	public Algorithm initial;
	public ArrayList<Vehicle> vehicles;
	public ArrayList<Arc> arcs;
	public ArrayList<Arc> sideWalkArcs;

	ArrayList<Genotype> adults;
	ArrayList<Genotype> selectedParents;
	ArrayList<Genotype> children;


	public EvolutionaryAlgorithm(int[][] inputGraph, int[][] inputSWGraph, int depot, int vehichles, int swVehicles){
		initial = new Algorithm(inputGraph, inputSWGraph, depot, vehichles, swVehicles);
		vehicles = initial.vehicles;
		arcs = initial.arcs;
		sideWalkArcs = initial.sideWalkArcs;
		this.fenotype = new Fenotype(arcs, sideWalkArcs, initial.arcMap, initial.arcNodeMap, initial.fwGraph, initial.fwPath, initial.fwGraphSW, initial.fwPathSW, depot, vehichles, swVehicles);
		education = new Education(fenotype);
	}


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
	public void run() {
		/*if(EvolutionaryAlgorithmParams.ADULT_SELECTION == AdultSelection.FULL_REPLACEMENT && (EvolutionaryAlgorithmParams.NUMBER_OF_CROSSOVER_PAIRS + 0.0) != (EvolutionaryAlgorithmParams.POPULATION_SIZE + 0.0) / 2.0){
			System.out.println("Full generational replacement requires that there are exactly as many new children as there are spots in the population. " +
					"Aborting because number of crossoverpair is not equal to half the population size.");
			return;
		}*/

		long generationNumber = 0;
		long startTime = System.currentTimeMillis();
		long timeTaken;
		long lastGenerationFitnessWasUpdated = 0;
		Genotype bestIndividual;
		Genotype copyOfBestIndividual;
		Genotype currentGenerationsCandidate;

		adults = new ArrayList<>();
		selectedParents = new ArrayList<>();
		children = new ArrayList<>();

		int counter = 0;
		while (adults.size() < 100) {
			adults.add(fenotype.createRandomGenotype());
			counter++;
			System.out.println("Creating Random offspring" + counter);
		}
		bestIndividual = new Genotype(Collections.max(adults));
		double bestSolution = Collections.min(adults).fitness;
		double tempBestSolution = 0;
		while (true) {
			//System.out.println("Starting EA main loop");
			ArrayList<Genotype> offspring = Selecting.Mating(adults, fenotype);
			children = education.educateChildren(offspring);
			adults.addAll(children);
			updatePopulation();
			tempBestSolution = Collections.min(adults).fitness;

			if (tempBestSolution< bestSolution){
				bestIndividual = new Genotype(Collections.max(adults));
				bestSolution = tempBestSolution;
				System.out.println(bestSolution);
			}
		}
	}



	public void updatePopulation() {
		Collections.sort(adults);
		int remove = 1;
		while(adults.size() > 100){
			if(rng.nextDouble() >= 0.1){
				if(remove >= adults.size()){
					remove = 1;
				}
				adults.remove(adults.size()-remove);
			}
			remove++;
		}
	}


}






























