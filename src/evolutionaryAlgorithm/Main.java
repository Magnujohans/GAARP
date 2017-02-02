package evolutionaryAlgorithm;

import graph.FloydWarshall;
import graph.Graph;

public class Main {
	
	
	
	
	
	public static void main(String[] args) {
		Graph.initialize();
		if(Graph.numberOfRequiredElements == 0){
			System.out.println("You have to specify at least some elements to service for this to work. Aborting.");
			return;
		}
		FloydWarshall.doFloydWarshall();
		Utilities.init();
		EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();
		ea.run();
	}
}
