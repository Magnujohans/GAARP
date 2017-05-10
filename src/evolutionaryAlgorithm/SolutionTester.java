package evolutionaryAlgorithm;

import InitialSolution.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Magnu on 15.03.2017.
 */
public class SolutionTester {
    Fenotype fenotype;
    Education education;
    Random rng = new Random();
    public Algorithm initial;
    public ArrayList<Vehicle> vehicles;
    public ArrayList<Arc> arcs;
    public ArrayList<Arc> sideWalkArcs;

    ArrayList<Genotype> adults;
    ArrayList<Genotype> selectedParents;
    ArrayList<Genotype> children;

    int population;


    public SolutionTester(int[][] inputGraph, int[][] inputSWGraph, int depot, int vehichles, int swVehicles){
        initial = new Algorithm(inputGraph, inputSWGraph, inputGraph, inputSWGraph, depot, vehichles, swVehicles);
        vehicles = initial.vehicles;
        arcs = initial.arcs;
        sideWalkArcs = initial.sideWalkArcs;
        this.fenotype = new Fenotype(arcs, sideWalkArcs, initial.arcMap, initial.arcNodeMap, initial.SWarcNodeMap,initial.arcNodeMap,initial.arcNodeMap,  initial.fwGraph, initial.fwPath, initial.fwGraphSW, initial.fwPathSW,  depot, vehichles, swVehicles, true);
        education = new Education(fenotype);

        population = 200;
    }

    public void run() {
        Vehicle plowtruck1 = new Vehicle(1, new ArrayList<>(), new ArrayList<>(), true);
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(0,1)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(1,0)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(4,5)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(5,4)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(5,11)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(11,10)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(10,7)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(7,8)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(8,6)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(6,5)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(5,9)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(9,3)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(3,1)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(1,3)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(3,4)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(0,8)));
        plowtruck1.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(8,0)));
        Vehicle plowtruck2 = new Vehicle(2, new ArrayList<>(), new ArrayList<>(), true);

        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(0,4)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(4,3)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(3,9)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(9,5)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(4,6)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(6,8)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(8,7)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(7,6)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(10,6)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(6,10)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(6,7)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(7,10)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(10,11)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(11,5)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(5,6)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(6,4)));
        plowtruck2.tasks.add(fenotype.arcNodeMap.get(new ArcNodeIdentifier(4,0)));


        Vehicle smallvehicle1 = new Vehicle(-1, new ArrayList<>(), new ArrayList<>(),true);
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(0,1)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(1,2)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(2,1)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(1,0)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(0,4)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(4,6)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(6,8)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(8,7)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(7,8)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(10,11)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(11,5)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(5,9)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(9,3)));
        smallvehicle1.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(3,4)));

        Vehicle smallvehicle2 = new Vehicle(-2, new ArrayList<>(), new ArrayList<>(),true);
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(3,9)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(9,5)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(5,11)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(11,10)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(10,12)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(12,11)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(11,12)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(12,10)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(6,4)));
        smallvehicle2.tasks.add(fenotype.SWarcNodeMap.get(new SWArcNodeIdentifier(4,0)));

        ArrayList<Vehicle> testSolution = new ArrayList<>();
        testSolution.add(plowtruck1);
        testSolution.add(plowtruck2);
        testSolution.add(smallvehicle1);
        testSolution.add(smallvehicle2);

        fenotype.resetPlowingtimes();
        for (int i = 0; i < testSolution.size(); i++) {
            testSolution.get(i).setRoute(fenotype.getTourFromTasks(testSolution.get(i).tasks, testSolution.get(i).id));
        }
        for (Vehicle vehicle : testSolution) {
            vehicle.reRoute();
            System.out.println(vehicle);
        }

        Genotype testGenotype = fenotype.createGenotype(testSolution, fenotype.calculateFitness(testSolution));

        ArrayList<Vehicle> testSolution2 = fenotype.getFenotype(testGenotype);

        System.out.println("");
        System.out.println("Så tuller vi litt");
        System.out.println("");

        fenotype.resetPlowingtimes();
        for (int i = 0; i < testSolution2.size(); i++) {
            testSolution2.get(i).setRoute(fenotype.getTourFromTasks(testSolution2.get(i).tasks, testSolution2.get(i).id));
        }
        for (Vehicle vehicle : testSolution2) {
            vehicle.reRoute();
            System.out.println(vehicle);
        }

        System.out.println(initial.feasibileSolution());



    }
}
