package tests;

import java.util.Arrays;

import evolutionaryAlgorithm.FitnessModule;
import evolutionaryAlgorithm.Genotype;
import evolutionaryAlgorithm.Utilities;
import generalUtilities.FileSaving;
import graph.FloydWarshall;
import graph.Graph;

public class EATests {
	
	
	public Genotype createSingleGenome(){
		Genotype genotype = new Genotype();
		return genotype;
	}
	
	public Genotype[] createTwoParentsAndChildren(){
		Genotype firstParent = new Genotype();
		Genotype secondParent = new Genotype();
		int firstCrossoverPoint = (int) Math.random() * Graph.numberOfRequiredElements;
		int secondCrossoverPoint = (int) Math.random() * Graph.numberOfRequiredElements;
		Genotype[] children = Utilities.crossover(firstParent, secondParent, firstCrossoverPoint, secondCrossoverPoint);
		firstParent.calculateFitness();
		secondParent.calculateFitness();
		children[0].calculateFitness();
		children[1].calculateFitness();
		
		validateCrossover(firstParent, secondParent, children[0], children[1], firstCrossoverPoint, secondCrossoverPoint);
		
		return new Genotype[] {firstParent, secondParent, children[0], children[1]};
	}
	
	
	public void validateCrossover(Genotype firstParent, Genotype secondParent,
			Genotype firstChild, Genotype secondChild,
			int firstCrossoverPoint, int secondCrossoverPoint){
		boolean firstChildFails = false;
		boolean secondChildFails = false;
		
		validateGenome(firstParent);
		validateGenome(secondParent);
		validateGenome(firstChild);
		validateGenome(secondChild);
		
		for (int i = firstCrossoverPoint; i != secondCrossoverPoint; i=(i+1)%Graph.numberOfRequiredElements) {
			if(firstChild.getGenome()[i] != firstParent.getGenome()[i]){
				firstChildFails = true;
			}
			if(secondChild.getGenome()[i] != secondParent.getGenome()[i]){
				secondChildFails = true;
			}
		}
		if(firstChildFails){
			System.out.println("Something was wrong with the first child");
		}
		if(secondChildFails){
			System.out.println("Something was wrong with the second child");			
		}
	}
	
	
	
	
	public void validateGenome(Genotype genotype){
//		boolean noDuplicateTasks = true;
		boolean correctNumberOfTasks = true;
		boolean allRequiredTasks = true;
		int[] genome = genotype.getGenome();
		
		if(genome.length != Graph.numberOfRequiredElements){
			correctNumberOfTasks = false;
		}
		Arrays.sort(genome);
		if(!Arrays.equals(genome, Graph.getRequiredElementsIDs())){
			allRequiredTasks = false;
		}
		
		if(!correctNumberOfTasks){
			System.out.println("Number of tasks has failed");
		}
		if(!allRequiredTasks){
			System.out.println("Sorted genome is different from graphs required IDs");
			System.out.println(FileSaving.getGson().toJson(genome));
			System.out.println(FileSaving.getGson().toJson(Graph.getRequiredElementsIDs()));
		}
				
	}
	
	
	
	public static void main(String[] args) {
		EATests test = new EATests();
		Graph.initialize();
		FloydWarshall.doFloydWarshall();
		Utilities.init();
		System.out.println("Number of required elements in graph " + Graph.numberOfRequiredElements);
		System.out.println("Required element IDs:");
		System.out.println(FileSaving.getGson().toJson(Graph.getRequiredElementsIDs()));
		
		Genotype genotype = test.createSingleGenome();
		System.out.println("Random genome:");
		System.out.println(FileSaving.getGson().toJson(genotype));
		genotype.calculateFitness();
		System.out.println("Genotype fitness: " + genotype.getFitness());
		
//		Genotype[] labrats = test.createTwoParentsAndChildren();
//		for (Genotype rat : labrats) {
//			System.out.println(FileSaving.getGson().toJson(rat));
//			test.validateGenome(rat);
//		}
		
//		System.out.println();
//		int[] testGenome = new int[60];
//		for (int i = 0; i < testGenome.length; i++) {
//			testGenome[i] = 60 + i;
//		}
////		Genotype testGenotype = new Genotype(testGenome);
//		System.out.println("The test genome has a fitness of: " + FitnessModule.tripCost(testGenome));

		
//		//Route KV_B_Midtbyen:
		String[] genomeFromData = {"E146137741","A146135111","E146137742","E146137743","E146137744","E146136061","E146136062","E107","E108","E146136771","E146136770","E245778140","E146151149","E146151148","E146137613","E146137612","E146132701","E146132702","E146132703","E212379613","E146135454","E146136748","E146137240","E245604093","A146135455","A146135456","E146134503","E245609100","E245609099","E245609101","E245609102","E323096222","E323096223","E146138046","E146135150","E146135457","E146135458","E146135459","E146135460","E245609097","E245609098","A146133117","A146133116","A146133115","A146133114","E146133537","E245626970","E245626969","E200425266","E200425265","E200425263","E200425264","E146136049","E200425255","E300008","E200425258","E200425259","E200425262","A146138004","A146138003","A146138002","E146138703","E146138702","A146138047","A146132704","A146138048","E146138049","E146138472","E146138473","E146138050","E146134938","E146134937","E245609089","E245609090","E200425261","E146138051","E200425253","E30007","E146138052","E146134153","E146132876","E146132875","E146136892","E146132874","E146139271","E146149233","E146148844","E146138155","E146133538","E146133604","E146134528","E146133603","E146137860","E146134227","E146134228","E146136917","E146136916","E146134494","E146134493","E146133554","E146133553","E146133552","E146135759","E146134492","A146132779","E146138342","E146138343","E146132989","E146138660","E146133876","E240551660"};
		//Route KV_H_Midtbyen:
//		String[] genomeFromData = {"A146132894","A146132893","A146132892","A146132891","A146132890","E146135169","A146133712","A146133713","A146133714","A146133715","A146133716","A146138154","A146138153","A146138152","E146135169","E146135170","E146135171","E146135172","A146135173","A146135174","A146135175","E146133559","E146133561","E146137957","E146137956","E146135321","E146135322","E146135323","E146135324","E146135325","E245609091","E481694094","E146136055","E146136056","E146136057","E146136058","E146190760","E146136059","E146136060","E146133819","E146133818","E146133817","E146133816","E200425245","E200425244","E146133814","E200425243","E200425242","E146133812","E146133811","E146133810","E146133809","E146133808","E146136318","E146136317","A146136316","A146133460","A200425238","E146136315","A146136314","E146136313","E146136312","E146136311","A146138693","A146138694","A146138695","A146138691","E146137246","E146137245","A146135176","A146135178","E146135181","E146135181","E146135179","A146135191","A146135192","E146135193","E146135194","E146135195","E146135196","E146135197","A146135198","E146135199","E146135190","A146135183","E146135184","A146135186","E146135187","A146135189"};
		
		
		int[] requiredIDs = Graph.getRequiredElementsIDs();
		int[] testGenome = new int[requiredIDs.length];
		int id = -1;
		
		for (int j = 0; j < genomeFromData.length; j++) {
			for (int i = 0; i < requiredIDs.length; i++) {
				if(Graph.getElementByID(requiredIDs[i]).getName().equals(genomeFromData[j])){
					id = requiredIDs[i];
					break;
				}
			}
			testGenome[j] = id;
		}
//		Genotype testGenotype = new Genotype(testGenome);
		System.out.println("The test genome has a fitness of: " + FitnessModule.tripCost(testGenome));
		
		
	}
}


































