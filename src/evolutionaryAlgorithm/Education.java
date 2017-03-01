package evolutionaryAlgorithm;

import InitialSolution.Vehicle;

import java.util.ArrayList;

/**
 * Created by Magnu on 01.03.2017.
 */
public class Education {
    Fenotype fenotype;

    public Education(Fenotype fenotype){
        this.fenotype = fenotype;
    }

    public ArrayList<Vehicle>, boolean Swap(ArrayList<Vehicle> vehicles){
        ArrayList<Vehicle> bestVehicles = vehicles;
        int bestFitness = fenotype.calculateFitness(bestVehicles);

        ArrayList<Vehicle> tempVehicles = new ArrayList<>();
        for (int i = 0; i < bestVehicles.size(); i++) {
             tempVehicles.add(bestVehicles.get(i).copyVehicle());
        }
    }
}
