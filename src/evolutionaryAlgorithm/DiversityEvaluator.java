package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Magnu on 06.05.2017.
 */
public class DiversityEvaluator {

    //This evaluates the Biased fitness, following the algorithm found in the paper.
    public static double BiasedFitness(ArrayList<Genotype> population, Genotype genotype, int Nclose){
        ArrayList<Double> Distance = new ArrayList<>();
        double factor = (genotype.laneGenome.length + genotype.sidewalkGenome.length);
        for (int i = 0; i < population.size(); i++) {
            int temp = 0;
            for(int j = 0; j < population.get(i).laneGenome.length; j++){
                if(genotype.laneGenome[j] != population.get(i).laneGenome[j]){
                    temp++;
                }
            }
            for(int j = 0; j < population.get(i).sidewalkGenome.length; j++){
                if(genotype.sidewalkGenome[j] != population.get(i).sidewalkGenome[j]){
                    temp++;
                }
            }
            Distance.add(0.0);
        }
        Collections.sort(Distance);
        double distanceNclose = 0;
        for(int x = 0; x < Nclose; x++){
            distanceNclose = distanceNclose + Distance.get(x);
        }
        return distanceNclose;
    }
}
