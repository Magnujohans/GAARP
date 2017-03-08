package evolutionaryAlgorithm;

import InitialSolution.Vehicle;
import InitialSolution.Arc;
import javafx.beans.binding.ObjectExpression;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Magnu on 01.03.2017.
 */
public class Education {
    Fenotype fenotype;
    Random rng;

    public Education(Fenotype fenotype){
        this.fenotype = fenotype;
        rng = new Random();
    }

    public ArrayList<Genotype> educateChildren(ArrayList<Genotype> children){
        ArrayList<Genotype> educatedChildren = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            ArrayList<Vehicle> tempChild = fenotype.getFenotype(children.get(i));
            Object[] tempResult = educate(tempChild);
            ArrayList<Vehicle> tempResultFenotype = (ArrayList<Vehicle>) tempResult[0];
            Genotype tempResultGenotype = fenotype.createGenotype(tempResultFenotype, (Integer) tempResult[1]);
            educatedChildren.add(tempResultGenotype);
        }
        return educatedChildren;
    }

    public Object[] educate(ArrayList<Vehicle> vehicles){
        int counter = 0;
        ArrayList<Vehicle> bestVehicles = vehicles;
        int bestFitness = fenotype.calculateFitness(bestVehicles);

        Object[] result;
        while(counter < 10){

            ArrayList<Vehicle> tempVehicles = new ArrayList<>();
            for (int i = 0; i < bestVehicles.size(); i++) {
                tempVehicles.add(bestVehicles.get(i).copyVehicle());
            }
            //int vehiclesFitness = fenotype.calculateFitness(bestVehicles);

            int[] makeSpanParameters = fenotype.calculateFitnessParameters(bestVehicles);
            if(makeSpanParameters[1] > 0){
                Vehicle tempOne = tempVehicles.get(makeSpanParameters[2]);
                int two = rng.nextInt(fenotype.plowtrucks);
                Vehicle tempTwo = tempVehicles.get(two);
                result = Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
            }
            else{
                if(rng.nextDouble() > 0.5){
                    Vehicle tempOne = tempVehicles.get(makeSpanParameters[2]);
                    int two = fenotype.plowtrucks + rng.nextInt(fenotype.smallervehicles);
                    Vehicle tempTwo = tempVehicles.get(two);
                    result = Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
                }
                else{
                    int one = rng.nextInt(fenotype.plowtrucks);
                    int two = rng.nextInt(fenotype.plowtrucks);
                    Vehicle tempOne = tempVehicles.get(one);
                    Vehicle tempTwo = tempVehicles.get(two);
                    result = Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
                }
            }
            if(((Integer) result[1]) < bestFitness){
                bestVehicles = (ArrayList<Vehicle>) result[0];
                bestFitness = (Integer) result[1];
                counter = 0;
            }
            else {
                counter++;
            }
        }
        Object[] best = new Object[2];
        best[0] = bestVehicles;
        best[1] = bestFitness;
        return best;
    }

    public Object[] Insert(ArrayList<Vehicle> vehicles, Vehicle from, Arc arc, Vehicle to, int index){
        from.tasks.remove(arc);
        to.tasks.add(index, arc);

        from.route = fenotype.getTourFromTasks(from.tasks);
        to.route = fenotype.getTourFromTasks(to.tasks);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;

    }
}