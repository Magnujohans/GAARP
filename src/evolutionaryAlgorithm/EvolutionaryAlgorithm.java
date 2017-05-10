package evolutionaryAlgorithm;

import InitialSolution.*;
import com.sun.org.apache.xpath.internal.SourceTreeManager;
import graph.Graph;

import java.util.*;

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
	public ArrayList<Arc> allArcs;

	ArrayList<Genotype> adults;
	ArrayList<Genotype> selectedParents;
	ArrayList<Genotype> children;

	int population;
	int diversitySurvivalFraction;
	int nElite;


	public EvolutionaryAlgorithm(int[][] inputGraph, int[][] inputSWGraph, int[][] deadheadingTimeLane, int[][] deadheadingTimeSidewalk, int depot, int vehichles, int swVehicles, boolean uTurnsAllowed){
		initial = new Algorithm(inputGraph, inputSWGraph, deadheadingTimeLane, deadheadingTimeSidewalk, depot, vehichles, swVehicles);
		vehicles = initial.vehicles;
		arcs = initial.arcs;
		sideWalkArcs = initial.sideWalkArcs;
		this.fenotype = new Fenotype(arcs, sideWalkArcs, initial.arcMap, initial.arcNodeMap, initial.SWarcNodeMap, initial.arcNodeMapLaneDH, initial.arcNodeMapSidewalkDH, initial.fwGraph, initial.fwPath, initial.fwGraphSW, initial.fwPathSW, depot, vehichles, swVehicles, uTurnsAllowed);
		education = new Education(fenotype);

		population = 200;
		diversitySurvivalFraction = 3;
		nElite = 80;

		allArcs = getAllArcs();
		fenotype.setParameters(nElite, population);

		getNeighbours();
		//printNeighbours();
		if(uTurnsAllowed == false){
			//HashMap <ArcNodeIdentifier, UturnInformation> uTurnInfo = initial.getUturnMatrix();
			fenotype.setUturnMatrix();
		}
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
		while (adults.size() < population) {
			Genotype temp = fenotype.createRandomGenotype();
			adults.add(fenotype.createRandomGenotype());
			counter++;
			System.out.println("Creating Random offspring" + counter);
		}
		fenotype.RankGenotypes(adults);

		int newBestSolutionCounter = 0;
		bestIndividual = new Genotype(Collections.max(adults));
		double bestSolution = Collections.min(adults).fitness;
		double tempBestSolution = 0;
		while (true) {
			//System.out.println("Starting EA main loop");
			ArrayList<Genotype> offspring = Selecting.Mating(adults, fenotype);
			//children = education.educateChildren(offspring, newBestSolutionCounter, 0.0000);
			children = education.educateChildren2(allArcs, arcs, sideWalkArcs, offspring, 10000, 0);
			for (int x = 0; x < children.size(); x++) {
				if (!checkUniqueSolution(children.get(x))) {
					children.remove(x);
				}
			}
			adults.addAll(children);
			updatePopulation();
			tempBestSolution = Collections.min(adults).fitness;
			newBestSolutionCounter++;

			if (tempBestSolution < bestSolution) {
				bestIndividual = new Genotype(Collections.min(adults));
				bestSolution = tempBestSolution;
				System.out.println(bestSolution);
				newBestSolutionCounter = 0;
			}
/*
			if(newBestSolutionCounter == 10000){
				int mutateIndex = population-2;
				Collections.sort(adults);
				while (mutateIndex >= 0){
					education.mutate(adults, mutateIndex);
					mutateIndex--;
				}
				updatePopulation();
				*/
			//adults.clear();
			//adults.add(bestIndividual);
			//System.out.println("New Pop");
			//while (adults.size() < population) {
			//	adults.add(fenotype.createRandomGenotype());
			//}
			//newBestSolutionCounter = 0;

			//}


			if (newBestSolutionCounter == 1000) {
				ArrayList<Vehicle> bestResult = fenotype.getFenotype(bestIndividual);
				fenotype.resetPlowingtimes();
				Collections.sort(vehicles, new TypeComparator());
				for (Vehicle vehicle : bestResult) {
					vehicle.reRoute();
					for (int x = 0; x < vehicle.tasks.size(); x++) {
						System.out.println(vehicle.tasks.get(x).identifier + "  " + vehicle.tasks.get(x).type);
					}
					System.out.println("NESTE KJØRETØY");
				}
				for (Vehicle vehicle : bestResult) {
					System.out.println(vehicle);
				}
				break;

			}

			if (newBestSolutionCounter % 200 == 0 && newBestSolutionCounter >1) {
				System.out.println("Diversify");
				for (int x = adults.size()-1; x > population / diversitySurvivalFraction; x--) {
					adults.remove(x);
				}
				while (adults.size() < population) {
					Genotype temp = fenotype.createRandomGenotype();
					adults.add(fenotype.createRandomGenotype());
				}
				fenotype.RankGenotypes(adults);
			}
			generationNumber++;
			System.out.println(generationNumber);
		}
	}

	public void getNeighbours(){
		for (int i = 0; i < allArcs.size(); i++) {
			for (int j = 0; j < allArcs.size(); j++) {
				if(allArcs.get(i).from.nr == allArcs.get(j).to.nr && allArcs.get(i).type == allArcs.get(j).type){
					allArcs.get(i).neighbours.add(allArcs.get(j));
				}
			}

		}

	}

	public void printNeighbours(){
		for (int i = 0; i < arcs.size(); i++) {
			System.out.println("ARC");
			System.out.println(arcs.get(i).from.nr +" : " + arcs.get(i).to.nr);
			System.out.println("NEIGHBORS");
			for (int j = 0; j < arcs.get(i).neighbours.size(); j++) {
				System.out.println(arcs.get(i).neighbours.get(j).from.nr +" : " + arcs.get(i).neighbours.get(j).to.nr);
			}

		}

	}

	public ArrayList<Arc> getAllArcs(){
		ArrayList<Arc> all = new ArrayList<>();
		for (int i = 0; i < arcs.size(); i++) {
			 all.add(arcs.get(i));
		}
		for (int i = 0; i < sideWalkArcs.size(); i++) {
			all.add(sideWalkArcs.get(i));
		}
		return all;
	}

	public boolean checkUniqueSolution(Genotype newSolution){
		for(int x = 0; x < adults.size(); x++){
			boolean unique = false;
			for(int z = 0; z < newSolution.laneGenome.length; z++){
				if (adults.get(x).laneGenome[z] != newSolution.laneGenome[z]){
					unique = true;
				}
			}
			for(int z = 0; z < newSolution.sidewalkGenome.length; z++){
				if (adults.get(x).sidewalkGenome[z] != newSolution.sidewalkGenome[z]){
					unique = true;
				}
			}
			if (unique == false){
				return false;
			}

		}
		return true;
	}



	public void updatePopulation() {
		for (Genotype solution : adults) {
			solution.diversity = DiversityEvaluator.BiasedFitness(adults,solution,40);
		}
		fenotype.RankGenotypes(adults);
		//for(int x = 0; x< adults.size(); x++){
		//	System.out.println(adults.get(x).fitness);
		//}
		int remove = 1;
		while(adults.size() > population){
			if(rng.nextDouble() >= 0.3){
				if(remove >= adults.size()-nElite){
					remove = 1;
				}
				adults.remove(adults.size()-remove);
			}
			remove++;
		}
	}


}






























