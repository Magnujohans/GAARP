package evolutionaryAlgorithm;

import InitialSolution.*;

import java.util.*;

//This is the main evolutionary algorithm
public class EvolutionaryAlgorithm {

	Fenotype fenotype;
	Education education;

	public Algorithm initial;
	public ArrayList<Vehicle> vehicles;
	public ArrayList<Arc> arcs;
	public ArrayList<Arc> sideWalkArcs;
	public ArrayList<Arc> allArcs;

	ArrayList<Genotype> adults;
	ArrayList<Genotype> children;

	int population;
	int offspringPerEpoch;
	int diversitySurvivalFraction;
	int nElite;
	int nClose;
	int iNI;
	int iDIV;
	double nEduFactor;
	boolean adaptive;
	int maxTime;


	//The parameters in "params" are the number \mu, \lambda, \eta^Elite, \eta^Close, \eta^Edu,I^n, eta^div. All parameters are multiplied with 10,
	//so that they all fit into a int[]. This means that one would need to multiply them with 0.1 to make them work.
	public EvolutionaryAlgorithm(int[][] inputGraph, int[][] nrPlowjobs, int[][] inputSWGraph, int[][] deadheadingTimeLane, int[][] deadheadingTimeSidewalk, int depot, int vehichles, int swVehicles, boolean uTurnsAllowed, boolean adaptive, int[] params){
		initial = new Algorithm(inputGraph, nrPlowjobs, inputSWGraph, deadheadingTimeLane, deadheadingTimeSidewalk, depot, vehichles, swVehicles);
		vehicles = initial.vehicles;
		arcs = initial.arcs;
		sideWalkArcs = initial.sideWalkArcs;
		this.fenotype = new Fenotype(arcs, sideWalkArcs, initial.arcMap, initial.arcNodeMap, initial.SWarcNodeMap, initial.arcNodeMapLaneDH, initial.arcNodeMapSidewalkDH, initial.fwGraph, initial.fwPath, initial.fwGraphSW, initial.fwPathSW, depot, vehichles, swVehicles, uTurnsAllowed);

		this.adaptive = adaptive;

		nEduFactor = 0.1*params[4];
		education = new Education(fenotype, nEduFactor);

		population = params[0];
		offspringPerEpoch = params[1];
		iNI = params[5];
		double iDIVfactor = 0.1*params[5]*params[6];
		iDIV = (int) iDIVfactor;


		double nEliteFactor = 0.1*params[2]*population;
		double nCloseFactor = 0.1*params[3]*population;
		nElite = (int) nEliteFactor;
		nClose = (int) nCloseFactor;


		if(this.adaptive){
			nElite = 0;
		}

		diversitySurvivalFraction = 3;

		allArcs = getAllArcs();
		fenotype.setParameters(nElite, population);

		//Set all neighbours for the arcs.
		getNeighbours();
		if(uTurnsAllowed == false){
			fenotype.disallowUturn();
		}
		maxTime = params[7];
	}



	public ArrayList<Vehicle> run() {

		long generationNumber = 0;
		long startTime = System.currentTimeMillis();
		long timeTaken;
		Genotype bestIndividual;

		adults = new ArrayList<>();
		children = new ArrayList<>();


		//Fills the population with random chromosomes.
		int counter = 0;
		while (adults.size() < population) {
			Genotype temp = fenotype.createRandomGenotype();
			adults.add(fenotype.createRandomGenotype());
			counter++;
		}
		fenotype.RankGenotypes(adults);
		Selecting selecting = new Selecting(offspringPerEpoch,nElite,population);
		int newBestSolutionCounter = 0;
		bestIndividual = new Genotype(Collections.max(adults));
		double bestSolution = Collections.min(adults).fitness;
		double tempBestSolution = 0;
		while (true) {

			//This checks whether the algorithm has needs to be diversified.
			// If so, it removes 2/3's of the population, and add random ones instead.
			if (newBestSolutionCounter % iDIV == 0 && newBestSolutionCounter >1) {
				//System.out.println("Diversify");
				for (int x = adults.size()-1; x > population / diversitySurvivalFraction; x--) {
					adults.remove(x);
				}
				while (adults.size() < population) {
					Genotype temp = fenotype.createRandomGenotype();
					adults.add(fenotype.createRandomGenotype());
				}
				fenotype.RankGenotypes(adults);
			}

			//Select parents, and perform the Mating(FMX)
			ArrayList<Genotype> offspring = selecting.Mating(adults, fenotype);

			//Educate the offspring
			children = education.educateChildren(allArcs, arcs, sideWalkArcs, offspring);
			//Remove clones
			for (int x = 0; x < children.size(); x++) {
				if (!checkUniqueSolution(children.get(x))) {
					children.remove(x);
				}
			}
			//Add all offspring, and update each individuals biased fitness
			adults.addAll(children);
			updatePopulation();
			//Check the solution in the population which has the best makespan
			tempBestSolution = Collections.min(adults).fitness;
			newBestSolutionCounter++;

			//Update the current best found solution in the search.
			if (tempBestSolution < bestSolution) {
				bestIndividual = new Genotype(Collections.min(adults));
				bestSolution = tempBestSolution;
				System.out.println(bestSolution);
				newBestSolutionCounter = 0;
			}


			//If we have reached the maximum number of non-improving solutions, or we have reached the MaxTime, return the best solution.
			long endTime = System.currentTimeMillis();
			if (newBestSolutionCounter == iNI || (endTime-startTime)/1000 > maxTime) {
				ArrayList<Vehicle> bestResult = fenotype.getFenotype(bestIndividual);
				fenotype.resetPlowingtimes();
				Collections.sort(bestResult, new TypeComparator());
				for (Vehicle vehicle : bestResult) {
					vehicle.reRoute();
				}
				return bestResult;

			}


			generationNumber++;
			//If the we run the adaptive n^Elite, update this with the generation number. This formula is found in the paper.
			if(adaptive){
				double nEliteFactor = newBestSolutionCounter*population/iNI;
				nElite = (int) nEliteFactor;
				selecting.updateElite(nElite);
				fenotype.updateNElite(nElite);
			}

		}
	}
	//Find all neighbours for an arc. An arc is defined as in the paper
	public void getNeighbours(){
		for (int i = 0; i < allArcs.size(); i++) {
			for (int j = 0; j < allArcs.size(); j++) {
				if(allArcs.get(i).from.nr == allArcs.get(j).to.nr && allArcs.get(i).type == allArcs.get(j).type){
					allArcs.get(i).neighbours.add(allArcs.get(j));
				}
			}

		}

	}

	//Get all Arcs, both lanes and sidewalks
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

	//Checks whether or not a solution is unique or a clone
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


	//This function follows the formula from the paper, when removing the members of the population with the worst fitness.
	public void updatePopulation() {
		while(adults.size() > population){
			for (Genotype solution : adults) {
				solution.diversity = DiversityEvaluator.BiasedFitness(adults,solution,nClose);
			}
			fenotype.RankGenotypes(adults);
			adults.remove(adults.size()-1);
		}
	}


}






























