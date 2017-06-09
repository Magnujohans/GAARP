package evolutionaryAlgorithm;


import InitialSolution.Vehicle;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		String Instance = "testData51N373A.txt";

		InputReader input = new InputReader(".\\src\\TestSet\\Resultatsett\\" + Instance);
		int[][] lanes = input.plowingtimeLane;
		int[][] numberLanes = input.numberOfPlowJobsLane;
		int[][] sidewalks = input.plowingtimeSidewalk;
		int[][] DHlanes = input.deadheadingtimeLane;
		int[][] DHsidewalks = input.deadheadingtimeSidewalk;
		int plowTrucks= input.numberOfVehiclesLane;
		int smallerVehicles = input.numberOfVehiclesSidewalk;



		int[] parameters = new int[]{35,75, 2, 1, 10, 1000,1, 6000 };
		EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(lanes, numberLanes, sidewalks,DHlanes,DHsidewalks,0, plowTrucks,smallerVehicles, true,true, parameters);
		ArrayList<Vehicle> result = ea.run();
		for (Vehicle vehicle : result) {
			System.out.println(vehicle);
		}
	}

}
