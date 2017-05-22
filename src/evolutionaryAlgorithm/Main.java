package evolutionaryAlgorithm;


import InitialSolution.Vehicle;
import jdk.internal.org.objectweb.asm.tree.ParameterNode;
import jdk.internal.util.xml.impl.Input;

import java.io.File;
import java.util.ArrayList;

public class Main {
	int[][] graph30 = {{0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,0,4,-1,-1,3,-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0},
		{-1,4,0,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,-1,-1},
		{-1,-1,5,-1,0,4,-1,-1,-1,-1,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,3,-1,-1,4,0,2,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,2,0,-1,-1,-1,3,-1,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,4,-1,0,-1,1,-1,4,3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,0,5,-1,5,-1,-1,-1,7,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,6,-1,-1,-1,-1,-1,1,5,0,-1,-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,4,-1,3,-1,-1,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,4,5,-1,-1,0,-1,-1,-1,-1,-1,-1,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,4,3,-1,-1,-1,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,-1,-1,-1,0,4,-1,-1,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,7,-1,-1,-1,-1,-1,4,0,4,-1,-1,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,0,6,-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,5,-1,-1,-1,-1,-1,-1,-1,6,0,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,-1,-1,-1,-1,-1,4,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,-1,-1,-1,-1,0,3,-1,-1,-1,4,4,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,-1,-1,-1,3,0,5,-1,5,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,-1,-1,5,0,4,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,0,6,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,-1,6,0,5,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,-1,-1,-1,5,0,-1,-1,4,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,-1,-1,-1,-1,-1,0,5,5,-1,-1},
		{-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,0,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,5,-1,0,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0}};

	int[][] sw30 = {{0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,0,6,-1,-1,4,-1,-1,-1,7,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0},
		{-1,6,0,5,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,5,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,8,-1,-1,-1},
		{-1,-1,5,-1,0,4,-1,-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,4,-1,-1,6,0,2,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,2,0,-1,-1,-1,5,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,6,-1,0,-1,3,-1,6,3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,0,7,-1,5,-1,-1,-1,7,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,7,-1,-1,-1,-1,-1,1,7,0,-1,-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,6,-1,5,-1,-1,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,4,5,-1,-1,0,-1,7,-1,-1,-1,-1,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,5,3,-1,-1,-1,-1,0,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,7,6,0,-1,-1,-1,-1,10,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,-1,-1,-1,0,4,-1,-1,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,7,-1,-1,-1,-1,-1,4,0,4,-1,-1,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,0,6,-1,-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,7,-1,-1,-1,-1,-1,-1,-1,6,0,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,-1,10,-1,-1,-1,4,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,-1,-1,-1,-1,0,3,-1,-1,-1,4,6,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,-1,-1,-1,3,0,5,-1,5,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,-1,-1,5,0,4,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,0,6,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,6,0,5,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,-1,-1,-1,7,0,-1,-1,4,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,6,-1,-1,-1,-1,-1,0,7,5,-1,-1},
		{-1,-1,-1,8,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,7,0,-1,8,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,6,5,-1,0,7,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,8,7,0,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0}};

	int[][] graph11 ={{-1,	0,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1},
			{-1,	-1,	4,	-1,	-1,	3,	-1,	-1,	-1,	6,	0},
			{-1,	4,	-1,	-1,	5,	-1,	-1,	-1,	-1,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1},
			{-1,	-1,	5,	-1,	-1,	4,	-1,	-1,	-1,	-1,	-1},
			{-1,	3,	-1,	-1,	4,	-1,	2,	4,	-1,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	2,	-1,	3,	-1,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	4,	3,	-1,	4,	1,	-1},
			{-1,	-1,	-1,	-1,	-1,	-1,	-1,	4,	-1,	4,	-1},
			{-1,	6,	-1,	-1,	-1,	-1,	-1,	1,	4,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1}};

	int[][] sw11 ={{-1,	0,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1},
			{-1,	-1,	4,	-1,	-1,	3,	-1,	-1,	-1,	6,	0},
			{-1,	4,	-1,	5,	5,	-1,	-1,	-1,	-1,	-1,	-1},
			{-1,	-1,	5,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1},
			{-1,	-1,	5,	-1,	-1,	6,	-1,	-1,	-1,	-1,	-1},
			{-1,	3,	-1,	-1,	-1,	-1,	2,	6,	-1,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	2,	-1,	3,	-1,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	6,	3,	-1,	4,	1,	-1},
			{-1,	-1,	-1,	-1,	-1,	-1,	-1,	4,	-1,	5,	-1},
			{-1,	6,	-1,	-1,	-1,	-1,	-1,	3,	5,	-1,	-1},
			{-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1,	-1}};

	int[][] graph15 = {{0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,0,4,-1,-1,3,-1,-1,-1,6,-1,-1,-1,-1,0},
			{-1,4,0,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,5,-1,0,4,-1,-1,-1,-1,4,-1,-1,-1,-1},
			{-1,3,-1,-1,4,0,2,4,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,2,0,3,-1,-1,3,-1,4,-1,-1},
			{-1,-1,-1,-1,-1,4,3,0,4,1,-1,2,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,4,0,4,-1,5,-1,-1,-1},
			{-1,6,-1,-1,-1,-1,-1,1,4,0,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,4,-1,3,-1,-1,-1,0,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,2,5,-1,-1,0,6,-1,-1},
			{-1,-1,-1,-1,-1,-1,4,-1,-1,-1,-1,6,0,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0}};

	int[][] sw15 = {{0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,0,6,-1,-1,4,-1,-1,-1,6,-1,-1,-1,-1,0},
			{-1,6,0,5,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,5,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,5,-1,0,6,-1,-1,-1,-1,6,-1,-1,-1,-1},
			{-1,4,-1,-1,4,0,2,6,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,2,0,3,-1,-1,5,-1,5,-1,-1},
			{-1,-1,-1,-1,-1,6,3,0,4,3,-1,2,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,4,0,5,-1,5,-1,-1,-1},
			{-1,6,-1,-1,-1,-1,-1,1,5,0,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,6,-1,5,-1,-1,-1,0,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,2,5,-1,-1,0,8,9,-1},
			{-1,-1,-1,-1,-1,-1,5,-1,-1,-1,-1,8,0,10,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,9,10,0,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0}};


	public static void main(String[] args) {
		InputReader input = new InputReader(".\\src\\TestSet\\ParameterTest\\testData11N41A.txt");
		int[][] lanes = input.plowingtimeLane;
		int[][] sidewalks = input.plowingtimeSidewalk;
		int[][] DHlanes = input.deadheadingtimeLane;
		int[][] DHsidewalks = input.deadheadingtimeSidewalk;
		int plowTrucks= input.numberOfVehiclesLane;
		int smallerVehicles = input.numberOfVehiclesSidewalk;
		Main main = new Main();

		//EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(main.graph15, main.sw15,0,2,2, true);
		int[] parameters = new int[]{35,75, 2, 1, 10, 2000,1 };
		EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(lanes, sidewalks,DHlanes,DHsidewalks,0, plowTrucks,smallerVehicles, true, parameters);
		ArrayList<Vehicle> result = ea.run();
		for (Vehicle vehicle : result) {
			System.out.println(vehicle);
		}
		//SolutionTester st = new SolutionTester(main.graph15, main.sw15, 0,2,2);
		//st.run();
		//ParameterTest();
	}

	public static void generateResults(int[] parameterValues){

		ArrayList<String> CaseList = new ArrayList<>();
		String Case1 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData11N41A.txt";
		String Case2 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData11N44A.txt";;
		String Case3 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData11N49A.txt";;
		String Case4 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData27N135A.txt";;
		String Case5 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData27N137A.txt";;
		String Case6 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData27N138A.txt";;
		String Case7 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData51N291A.txt";;
		String Case8 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData51N294A.txt";;
		String Case9 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData51N304A.txt";;
//		CaseList.add(Case1);
//		CaseList.add(Case2);
//		CaseList.add(Case3);
		CaseList.add(Case4);
//		CaseList.add(Case5);
		CaseList.add(Case6);
//		CaseList.add(Case7);
//		CaseList.add(Case8);
		CaseList.add(Case9);

		int[] defaultValues = new int[]{35,75,2,1, 1000, 2};
		for(int Case = 0; Case < CaseList.size(); Case++){
			InputReader ir = new InputReader(CaseList.get(Case));
			int[][] lanes = ir.plowingtimeLane;
			int[][] sidewalks = ir.plowingtimeSidewalk;
			int[][] DHlanes = ir.deadheadingtimeLane;
			int[][] DHsidewalks = ir.deadheadingtimeSidewalk;
			int plowTrucks= ir.numberOfVehiclesLane;
			int smallerVehicles = ir.numberOfVehiclesSidewalk;

			int[] resultList = new int[10];
			int[] timeList = new int[10];
			for(int nr = 0; nr < 10; nr++){
				int nr1Index = nr+1;
				long startTime = System.currentTimeMillis();
				EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(lanes, sidewalks, DHlanes, DHsidewalks, 0, plowTrucks, smallerVehicles, true, parameterValues);
				ArrayList<Vehicle> result = ea.run();
				long endTime = System.currentTimeMillis();
				int makespan = ea.fenotype.getMakeSpan(result);
				String resultString = "The " + nr1Index + " trial of case " + Case + " got the result of " + makespan + " and was done in " +
						(endTime-startTime)/1000 + " seconds";
				System.out.println(resultString);
				resultList[nr] = makespan;
				timeList[nr] = (int) (endTime-startTime)/1000;
			}
			String resultStringAV = "AVERAGE ON CASE " + Case +  " GOT THE RESULT OF " + calculateMean(resultList) + " AND AVERAGE TIME WAS " +
					calculateMean(timeList) + " SECONDS";
			System.out.println(resultStringAV);
		}
	}


	public static void ParameterTest(){
		ArrayList<ArrayList<Integer>> parameterValues= new ArrayList<>();
		ArrayList<Integer> nPop = new ArrayList<>();
		nPop.add(15);
		nPop.add(25);
		nPop.add(35);
		nPop.add(50);
		parameterValues.add(nPop);
		ArrayList<Integer> nGen = new ArrayList<>();
		nGen.add(25);
		nGen.add(50);
		nGen.add(75);
		nGen.add(100);
		parameterValues.add(nGen);
		ArrayList<Integer> nElite = new ArrayList<>();
		nElite.add(2);
		nElite.add(3);
		nElite.add(4);
		nElite.add(5);
		nElite.add(6);
		parameterValues.add(nElite);
		ArrayList<Integer> nClose = new ArrayList<>();
		nClose.add(1);
		nClose.add(2);
		nClose.add(3);
		nClose.add(4);
		nClose.add(5);
		parameterValues.add(nClose);
		ArrayList<Integer> nINI = new ArrayList<>();
		nINI.add(1000);
		nINI.add(5000);
		nINI.add(10000);
		parameterValues.add(nINI);
		ArrayList<Integer> nDiv = new ArrayList<>();
		nDiv.add(1);
		nDiv.add(4);
		nDiv.add(7);
		parameterValues.add(nDiv);

		ArrayList<String> CaseList = new ArrayList<>();
		String Case1 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData11N41A.txt";
		String Case2 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData11N44A.txt";;
		String Case3 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData11N49A.txt";;
		String Case4 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData27N135A.txt";;
		String Case5 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData27N137A.txt";;
		String Case6 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData27N138A.txt";;
		String Case7 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData51N291A.txt";;
		String Case8 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData51N294A.txt";;
		String Case9 = "D:\\Backup\\IdeaProjects\\GAARP\\src\\TestSet\\ParameterTest\\testData51N304A.txt";;
//		CaseList.add(Case1);
//		CaseList.add(Case2);
//		CaseList.add(Case3);
		CaseList.add(Case4);
//		CaseList.add(Case5);
		CaseList.add(Case6);
//		CaseList.add(Case7);
//		CaseList.add(Case8);
		CaseList.add(Case9);

		int[] defaultValues = new int[]{35,75,2,1, 1000, 2};
		for(int value1 = 0; value1 < parameterValues.get(4).size(); value1++){
			defaultValues[4] = parameterValues.get(4).get(value1);
			for(int value2 = 0; value2 < parameterValues.get(5).size(); value2++){
				defaultValues[5] = parameterValues.get(5).get(value2);
				int[] resultList = new int[5];
				int[] timeList = new int[5];
				for(int Case = 0; Case < CaseList.size();Case++){

					InputReader ir = new InputReader(CaseList.get(Case));
					int[][] lanes = ir.plowingtimeLane;
					int[][] sidewalks = ir.plowingtimeSidewalk;
					int[][] DHlanes = ir.deadheadingtimeLane;
					int[][] DHsidewalks = ir.deadheadingtimeSidewalk;
					int plowTrucks= ir.numberOfVehiclesLane;
					int smallerVehicles = ir.numberOfVehiclesSidewalk;

					for(int nr = 0; nr < 5; nr++){
						int nr1Index = nr+1;
						long startTime = System.currentTimeMillis();
						EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(lanes, sidewalks, DHlanes, DHsidewalks, 0, plowTrucks, smallerVehicles, true, defaultValues);
						ArrayList<Vehicle> result = ea.run();
						long endTime = System.currentTimeMillis();
						int makespan = ea.fenotype.getMakeSpan(result);
						String resultString = "The " + nr1Index + " trial of parameter " + parameterValues.get(4).get(value1) + " and parameter " +
								+parameterValues.get(5).get(value2) + " On case " + Case +  " Got the result of " + makespan + " And was done in " +
								(endTime-startTime)/1000 + " Seconds";
						System.out.println(resultString);
						resultList[nr] = makespan;
						timeList[nr] = (int) (endTime-startTime)/1000;
					}
					String resultStringAV = "AVERAGE OF PARAMETER " + parameterValues.get(4).get(value1) + " AND PARAMETER " +
							+ parameterValues.get(5).get(value2) + " ON CASE " + Case +  " GOT THE RESULT OF " + calculateMean(resultList) + " AND AVERAGE TIME WAS " +
							calculateMean(timeList) + " SECONDS";
					System.out.println(resultStringAV);
				}
			}

		}

	}

	public static int calculateMean(int[] list){
		int sum = 0;
		for (int i = 0; i < list.length; i++) {
			sum += list[i];
		}
		return sum/list.length;
	}
}
