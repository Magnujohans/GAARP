package evolutionaryAlgorithm;

import InitialSolution.Arc;
import InitialSolution.ArcNodeIdentifier;
import InitialSolution.Vehicle;
import InitialSolution.TypeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by Magnu on 15.02.2017.
 */
public class Fenotype {

    private final ArrayList<Arc> lanes;
    private final ArrayList<Arc> sidewalks;
    private final int plowtrucks;
    private final int smallervehicles;

    public int[] originalLaneGeno;
    public int[] originalSidewalkGeno;
    public HashMap<Integer, Arc> arcNodeMap;
    public int[][] FWLaneGraph;
    public int[][] FWSidewalkGraph;
    public int[][] FWLanePath;
    public int[][] FWSidewalkPath;
    public int depot;


    public Fenotype(ArrayList<Arc> lanes, ArrayList<Arc> sidewalks, HashMap<Integer, Arc> arcMap, int[][] FWlaneGraph, int[][] FWlanePath,
                    int[][] FWsidewalkGraph, int[][] FWsidewalkPath, int depot, int plowtrucks, int smallervehicles) {
        this.lanes = lanes;
        this.sidewalks = sidewalks;
        this.arcNodeMap = arcMap;
        this.FWLaneGraph = FWlaneGraph;
        this.FWSidewalkGraph = FWsidewalkGraph;
        this.FWLanePath = FWlanePath;
        this.FWSidewalkPath = FWsidewalkPath;
        this.depot = depot;
        this.plowtrucks = plowtrucks;
        this.smallervehicles = smallervehicles;

        this.originalLaneGeno = getAscendingLanes();
        this.originalSidewalkGeno = getAscendingSidewalks();
    }

    public ArrayList<Arc> getLanes(){
        return this.lanes;
    }

    public ArrayList<Arc> getSidewalks(){
        return this.sidewalks;
    }

    public Genotype createRandomGenotype(){

    }



    public ArrayList<Vehicle> getFenotype(Genotype genotype) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Vehicle temp = new Vehicle(1, new ArrayList<Arc>(), new ArrayList<Arc>());
        vehicles.add(temp);
        int newVehicleId = 1;
        int[] lanesGenotype = genotype.getLaneGenome();
        for (int x = 0; x < lanesGenotype.length; x++) {
            if (lanesGenotype[x] == -1) {
                newVehicleId++;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>()));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcNodeMap.get(lanesGenotype[x]));
        }
        newVehicleId = -1;
        int[] sidewalkGenotype = genotype.getSidewalkGenome();
        for (int x = 0; x < sidewalkGenotype.length; x++) {
            if (sidewalkGenotype[x] == -1) {
                newVehicleId--;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>()));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcNodeMap.get(lanesGenotype[x]));
        }


        return vehicles;
    }

    public ArrayList<Vehicle> getFenotypefromGenome(int[] laneGenome, int[] sideWalkGenome) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Vehicle temp = new Vehicle(1, new ArrayList<Arc>(), new ArrayList<Arc>());
        vehicles.add(temp);
        int newVehicleId = 1;
        int[] lanesGenotype = laneGenome.clone();
        for (int x = 0; x < lanesGenotype.length; x++) {
            if (lanesGenotype[x] == -1) {
                newVehicleId++;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>()));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcNodeMap.get(lanesGenotype[x]));
        }
        newVehicleId = -1;
        int[] sidewalkGenotype = sideWalkGenome.clone();
        for (int x = 0; x < sidewalkGenotype.length; x++) {
            if (sidewalkGenotype[x] == -1) {
                newVehicleId--;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>()));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcNodeMap.get(lanesGenotype[x]));
        }


        return vehicles;
    }

    public Genotype Genotype(ArrayList<Vehicle> initialVehicles){
        int[] laneGenome = new int[plowtrucks-1 + this.getLanes().size()];
        int z = 0;
        Collections.sort(initialVehicles, new TypeComparator());
        for(int x = 0; x<plowtrucks;x++){
            if(x>0){
                laneGenome[z] = -1;
                z++;
            }
            for (int y = 0; y<initialVehicles.get(x).tasks.size(); y++){
                laneGenome[z] = initialVehicles.get(x).tasks.get(y).identifier;
                z++;
            }
        }
        int[] sidewalkGenome = new int[plowtrucks-1 + this.getSidewalks().size()];
        z = 0;
        for(int x = plowtrucks; x<(plowtrucks + smallervehicles);x++){
            if(x>plowtrucks){
                sidewalkGenome[z] = -1;
                z++;
            }
            for (int y = 0; y<initialVehicles.get(x).tasks.size(); y++){
                sidewalkGenome[z] = initialVehicles.get(x).tasks.get(y).identifier;
                z++;
            }
        }
        return new Genotype(laneGenome, sidewalkGenome, calculateFitness(laneGenome, sidewalkGenome));
    }

    public int calculateFitness(Genotype genotype) {
        ArrayList<Vehicle> vehicles = getFenotype(genotype);
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        return getMakeSpan(vehicles);
    }

    public int calculateFitness(int[] laneGenome, int[] sidewalkGenome) {
        ArrayList<Vehicle> vehicles = getFenotypefromGenome(laneGenome,sidewalkGenome);
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        return getMakeSpan(vehicles);
    }

    public ArrayList<Arc> getTourFromTasks(ArrayList<Arc> tasks) {
        ArrayList<Arc> route = new ArrayList<>();
        if (depot != tasks.get(0).from.nr) {
            route.addAll(getArcsFromPath(depot, tasks.get(0).from.nr));
        }
        for (int x = 0; x < tasks.size() - 1; x++) {
            route.add(tasks.get(x));
            if (tasks.get(x).to.nr != tasks.get(x + 1).from.nr) {
                route.addAll(getArcsFromPath(tasks.get(x).to.nr, tasks.get(x + 1).from.nr));
            }
        }
        route.add(tasks.get(tasks.size() - 1));
        if (tasks.get(tasks.size() - 1).to.nr != depot) {
            route.addAll(getArcsFromPath(tasks.get(tasks.size() - 1).to.nr, depot));
        }
        return route;
    }

    public ArrayList<Arc> getArcsFromPath(int startNodeId, int endNodeId) {
        ArrayList<Integer> path = getPath(startNodeId, endNodeId);
        ArrayList<Arc> arcPath = new ArrayList<>();

        for (int x = 0; x < path.size() - 1; x++) {
            arcPath.add(arcNodeMap.get(new ArcNodeIdentifier(path.get(x), path.get(x + 1))));
        }
        return arcPath;
    }

    public ArrayList<Integer> getPath(int startID, int endID) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(startID);
        int nextID = startID;
        while (nextID != endID) {
            int oldId = nextID;
            nextID = FWLanePath[oldId][endID];
            if (nextID == -1) {
                nextID = FWSidewalkPath[oldId][endID];
            }
            path.add(nextID);
        }
        return path;
    }

    public int getMakeSpan(ArrayList<Vehicle> vehicles) {
        int max = 0;
        for (int x = 0; x < vehicles.size(); x++) {
            if (vehicles.get(x).totalLength > max) {
                max = vehicles.get(x).totalLength;
            }
        }
        return max;
    }

    public void resetPlowingtimes() {
        for (int x = 0; x < lanes.size(); x++) {
            lanes.get(x).startOfService = -1;
            lanes.get(x).serviced = false;
            lanes.get(x).waitingTime = 0;
        }
        for (int x = 0; x < sidewalks.size(); x++) {
            sidewalks.get(x).startOfService = -1;
            sidewalks.get(x).serviced = false;
            sidewalks.get(x).waitingTime = 0;
        }
    }

    public int[] getAscendingLanes(){
        int[] ascendingLanes = new int[lanes.size()+plowtrucks-1];

        for (int i = 0; i < ascendingLanes.length; i++) {
            ascendingLanes[i] = lanes.get(i).identifier;
        }

        return ascendingLanes;

    }

    public int[] getAscendingSidewalks(){
        int[] ascendingSidewalks = new int[sidewalks.size()+ smallervehicles-1];
        for (int i = sidewalks.size(); i < ascendingSidewalks.length; i++) {
            ascendingSidewalks[i] = sidewalks.get(i).identifier;
        }
        return ascendingSidewalks;
    }

    private void swap(int[] arr, int i, int j){
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
        }
    }
}
