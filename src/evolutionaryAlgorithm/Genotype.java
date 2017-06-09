package evolutionaryAlgorithm;


//This is the Genotype/chromosome class, holding two chromosomes, one for each vehicle type
public class Genotype implements Comparable<Genotype>{
	int[] laneGenome;
	int[] sidewalkGenome;
	Fenotype fenotype;
	double fitness = -1.0;
	double diversity = -1.0;

	public int fitnessRank = -1;
	public int diversityRank = -1;



	//Two different constructors are created, the first is essentially a cloning mechanism, while the other takes in two chromosomes and
	//And the calculated fitness.
	public Genotype(Genotype genotype){
		this.laneGenome = genotype.getLaneGenome().clone();
		this.sidewalkGenome = genotype.getSidewalkGenome().clone();
	}
	public Genotype(int[] laneGenome, int[] sidewalkGenome, int fitness) {
		this.laneGenome = laneGenome.clone();
		this.sidewalkGenome = sidewalkGenome.clone();
		this.fitness = fitness;
	}

	

	

	public void setFitnessRank(int rank){
		this.fitnessRank = rank;
	}

	public void setDiversityRank(int rank){
		this.diversityRank = rank;
	}
	
	public int[] getLaneGenome(){
		return this.laneGenome.clone();
	}


	public int[] getSidewalkGenome(){
		return this.sidewalkGenome.clone();
	}

	//This compareTo method makes the chromosome sortable based on makespan
	@Override
	public int compareTo(Genotype otherGenotype) {
		/*Pushes lower fitnesses further to the right,
		 * because in some other places (selection) we
		 * rely on that the best values are found at the far right.*/
		if(this.fitness < otherGenotype.fitness){
			return -1;
		}else if(this.fitness > otherGenotype.fitness){
			return 1;
		}else{
			return 0;			
		}
	}
}
