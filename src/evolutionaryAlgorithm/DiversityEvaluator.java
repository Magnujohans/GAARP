package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Magnu on 06.05.2017.
 */
public class DiversityEvaluator {

    public static double BiasedFitness(ArrayList<Genotype> population, Genotype genotype, int Nclose){
        ArrayList<Double> Distance = new ArrayList<>();
        double factor = 1/(genotype.laneGenome.length + genotype.sidewalkGenome.length);
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
            Distance.add(temp*factor);
        }
        Collections.sort(Distance);
        double distanceNclose = 0;
        for(int x = 0; x < Nclose; x++){
            distanceNclose = distanceNclose + Distance.get(x);
        }
        return distanceNclose;
    }
}
