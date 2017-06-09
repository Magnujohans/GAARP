package evolutionaryAlgorithm;

import InitialSolution.*;

import java.util.*;


/**
 * Created by Magnu on 15.02.2017.
 */
public class Fenotype {

    Random rng;
    private final ArrayList<Arc> lanes;
    private final ArrayList<Arc> sidewalks;
    public final int plowtrucks;
    public final int smallervehicles;

    public int[] originalLaneGeno;
    public int[] originalSidewalkGeno;

    public HashMap<Integer, Arc> arcMap;
    public HashMap<ArcNodeIdentifier, Arc> arcNodeMap;
    public HashMap<ArcNodeIdentifier, Arc> arcNodeMapLaneDH;
    public HashMap<ArcNodeIdentifier, Arc> arcNodeMapSidewalkDH;

    public HashMap<SWArcNodeIdentifier, Arc> SWarcNodeMap;
    public int[][] FWLaneGraph;
    public int[][] FWSidewalkGraph;
    public int[][] FWLanePath;
    public int[][] FWSidewalkPath;
    public HashMap<ArcNodeIdentifier, UturnInformation> UturnMap;
    public int depot;
    public boolean uTurnsAllowed = true;

    public int nElite;
    public int nPopulation;


    //The Fenotype class contains everything related to conversion from chromosomes to solution(Fenotype)


    public Fenotype(ArrayList<Arc> lanes, ArrayList<Arc> sidewalks, HashMap<Integer, Arc> arcMap, HashMap<ArcNodeIdentifier, Arc> arcNodeMap, HashMap<SWArcNodeIdentifier, Arc> SWarcNodeMap,
                    HashMap<ArcNodeIdentifier, Arc> arcNodeMapLaneDH, HashMap<ArcNodeIdentifier, Arc> arcNodeMapSidewalkDH,
                    int[][] FWlaneGraphDH, int[][] FWlanePathDH,
                    int[][] FWsidewalkGraphDH, int[][] FWsidewalkPathDH, int depot, int plowtrucks, int smallervehicles, boolean uTurnsAllowed) {
        this.lanes = lanes;
        this.sidewalks = sidewalks;
        this.arcNodeMap = arcNodeMap;
        this.SWarcNodeMap = SWarcNodeMap;
        this.arcNodeMapLaneDH = arcNodeMapLaneDH;
        this.arcNodeMapSidewalkDH = arcNodeMapSidewalkDH;
        this.arcMap = arcMap;
        this.FWLaneGraph = FWlaneGraphDH;
        this.FWSidewalkGraph = FWsidewalkGraphDH;
        this.FWLanePath = FWlanePathDH;
        this.FWSidewalkPath = FWsidewalkPathDH;
        this.depot = depot;
        this.plowtrucks = plowtrucks;
        this.smallervehicles = smallervehicles;

        this.originalLaneGeno = getAscendingLanes();
        this.originalSidewalkGeno = getAscendingSidewalks();
        rng = new Random();

    }

    //Update Parameters for the adaptive n^Elite
    public void setParameters(int nElite, int population){
        this.nElite = nElite;
        this.nPopulation = population;
    }
    public void updateNElite(int nElite){
        this.nElite = nElite;
    }

    //This is the Biased Fitness sorting algorithm from the paper.
    public void RankGenotypes(ArrayList<Genotype> population){
        Collections.sort(population, new FitnessComparator());
        for (int x = 0; x <population.size(); x++){
            population.get(x).setFitnessRank(x+1);
        }
        Collections.sort(population, new DiversityComparator());
        for (int x = 0; x <population.size(); x++){
            population.get(x).setDiversityRank(x+1);
        }
        Collections.sort(population, new BFComparator(nElite,nPopulation));
    }

    public ArrayList<Arc> getLanes() {
        return this.lanes;
    }

    public ArrayList<Arc> getSidewalks() {
        return this.sidewalks;
    }

    // This is the proposed algorithm for generating random genotypes.
    //It works by iterativly choose one random task, and place it in a random index using a random permutation
    public Genotype createRandomGenotype(){
        int[] tempLane = new int[originalLaneGeno.length];
        int[] tempSideWalk = new int[originalSidewalkGeno.length];
        ArrayList<Integer> lanePermutation = getPermutation(originalLaneGeno.length);
        for(int x = 0; x < originalLaneGeno.length; x++){
            tempLane[x] = originalLaneGeno[lanePermutation.get(x)];
        }
        ArrayList<Integer> sidewalkPermutation = getPermutation(originalSidewalkGeno.length);
        for(int x = 0; x < originalSidewalkGeno.length; x++){
            tempSideWalk[x] = originalSidewalkGeno[sidewalkPermutation.get(x)];
        }

        return new Genotype(tempLane, tempSideWalk, calculateFitness(tempLane, tempSideWalk));
    }


    //Convert the genotype to a Fenotype
    public ArrayList<Vehicle> getFenotype(Genotype genotype) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Vehicle temp = new Vehicle(1, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed);
        vehicles.add(temp);
        int newVehicleId = 1;
        int[] lanesGenotype = genotype.getLaneGenome();
        for (int x = 0; x < lanesGenotype.length; x++) {
            if (lanesGenotype[x] == -1) {
                newVehicleId++;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcMap.get(lanesGenotype[x]));
        }
        newVehicleId = -1;
        vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed));
        int[] sidewalkGenotype = genotype.getSidewalkGenome();
        for (int x = 0; x < sidewalkGenotype.length; x++) {
            if (sidewalkGenotype[x] == -1) {
                newVehicleId--;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcMap.get(sidewalkGenotype[x]));
        }

        for (int i = 0; i < vehicles.size(); i++) {
            vehicles.get(i).route = getTourFromTasks(vehicles.get(i).tasks, vehicles.get(i).id);

        }


        return vehicles;
    }

    //Creates a solution from a lane genome, and a sidewalk genome.
    public ArrayList<Vehicle> getFenotypefromGenome(int[] laneGenome, int[] sideWalkGenome) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Vehicle temp = new Vehicle(1, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed);
        vehicles.add(temp);
        int newVehicleId = 1;
        int[] lanesGenotype = laneGenome.clone();
        for (int x = 0; x < lanesGenotype.length; x++) {
            if (lanesGenotype[x] == -1) {
                newVehicleId++;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcMap.get(lanesGenotype[x]));
            //System.out.println(arcNodeMap.get(lanesGenotype[x]));
        }

        temp = new Vehicle(-1, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed);
        vehicles.add(temp);
        newVehicleId = -1;
        int[] sidewalkGenotype = sideWalkGenome.clone();
        for (int x = 0; x < sidewalkGenotype.length; x++) {
            if (sidewalkGenotype[x] == -1) {
                newVehicleId--;
                vehicles.add(new Vehicle(newVehicleId, new ArrayList<Arc>(), new ArrayList<Arc>(), this.uTurnsAllowed));
                continue;
            }
            vehicles.get(vehicles.size() - 1).tasks.add(arcMap.get(sidewalkGenotype[x]));
        }

        for (int i = 0; i < vehicles.size(); i++) {
            vehicles.get(i).setRoute(getTourFromTasks(vehicles.get(i).tasks, vehicles.get(i).id));

        }


        return vehicles;
    }

    //This function creates a chromosome, based on a solution.
    public Genotype createGenotype(ArrayList<Vehicle> initialVehicles, int fitness) {
        int[] laneGenome = new int[plowtrucks - 1 + this.getLanes().size()];
        int z = 0;
        Collections.sort(initialVehicles, new TypeComparator());
        for (int x = 0; x < plowtrucks; x++) {
            if (x > 0) {
                laneGenome[z] = -1;
                z++;
            }
            for (int y = 0; y < initialVehicles.get(x).tasks.size(); y++) {
                laneGenome[z] = initialVehicles.get(x).tasks.get(y).identifier;
                z++;
            }
        }
        int[] sidewalkGenome = new int[smallervehicles - 1 + this.getSidewalks().size()];
        z = 0;
        for (int x = plowtrucks; x < (plowtrucks + smallervehicles); x++) {
            if (x > plowtrucks) {
                sidewalkGenome[z] = -1;
                z++;
            }
            for (int y = 0; y < initialVehicles.get(x).tasks.size(); y++) {
                sidewalkGenome[z] = initialVehicles.get(x).tasks.get(y).identifier;
                z++;
            }
        }
        return new Genotype(laneGenome, sidewalkGenome, fitness);
    }

    //Creates a genotype from a solution, and also calculates the fitness value(in contrast to the function above)
    public Genotype InitialGenotype(ArrayList<Vehicle> initialVehicles) {
        int[] laneGenome = new int[plowtrucks - 1 + this.getLanes().size()];
        int z = 0;
        Collections.sort(initialVehicles, new TypeComparator());
        for (int x = 0; x < plowtrucks; x++) {
            if (x > 0) {
                laneGenome[z] = -1;
                z++;
            }
            for (int y = 0; y < initialVehicles.get(x).tasks.size(); y++) {
                laneGenome[z] = initialVehicles.get(x).tasks.get(y).identifier;
                z++;
            }
        }
        int[] sidewalkGenome = new int[plowtrucks - 1 + this.getSidewalks().size()];
        z = 0;
        for (int x = plowtrucks; x < (plowtrucks + smallervehicles); x++) {
            if (x > plowtrucks) {
                sidewalkGenome[z] = -1;
                z++;
            }
            for (int y = 0; y < initialVehicles.get(x).tasks.size(); y++) {
                sidewalkGenome[z] = initialVehicles.get(x).tasks.get(y).identifier;
                z++;
            }
        }
        return new Genotype(laneGenome, sidewalkGenome, calculateFitness(laneGenome, sidewalkGenome));
    }


    //A function that calculates the vehicles cumulative fitness. This number is used for accepting or rejecting a
    // move in the education phase. This function is used when there are at the most two vehicles.
    public int[] calculateVehicleFitness(ArrayList<Vehicle> vehicles, Vehicle One, Vehicle Two){
        int[] vehicleTotalTime = new int[2];
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        for (int x = 0; x < vehicles.size(); x++) {
            if (vehicles.get(x) == One){
                vehicleTotalTime[0] = vehicles.get(x).totalLength;
            }
            if (vehicles.get(x) == Two){
                vehicleTotalTime[1] = vehicles.get(x).totalLength;
            }
        }
        return  vehicleTotalTime;
    }

    //A function that calculates the vehicles cumulative fitness. This number is used for accepting or rejecting a
    // move in the education phase. This function is used when there are at the most four vehicles.
    public int[] calculateVehicleFitness(ArrayList<Vehicle> vehicles, Vehicle One, Vehicle Two, Vehicle Three, Vehicle Four){
        int[] vehicleTotalTime = new int[4];
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        for (int x = 0; x < vehicles.size(); x++) {
            if (vehicles.get(x) == One){
                vehicleTotalTime[0] = vehicles.get(x).totalLength;
            }
            if (vehicles.get(x) == Two){
                vehicleTotalTime[1] = vehicles.get(x).totalLength;
            }
            if (vehicles.get(x) == Three){
                vehicleTotalTime[2] = vehicles.get(x).totalLength;
            }
            if (vehicles.get(x) == Four){
                vehicleTotalTime[3] = vehicles.get(x).totalLength;
            }
        }
        return  vehicleTotalTime;
    }

    //calculates the objective value fitness for a solution. In this case, the makespan value.
    public int calculateFitness(ArrayList<Vehicle> vehicles) {
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        return getMakeSpan(vehicles);
    }


    //Calucates a set of values generated by the solution in question, in this case the makespan, the type of the vehicle that
    // defines the makespan, and which index the this vehicle has in the solution.
    public int[] calculateFitnessParameters(ArrayList<Vehicle> vehicles) {
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        return getMakeSpanParameters(vehicles);
    }

    //Calculates makespan only based on a lane and sidewalk chromosome.
    public int calculateFitness(int[] laneGenome, int[] sidewalkGenome) {
        ArrayList<Vehicle> vehicles = getFenotypefromGenome(laneGenome, sidewalkGenome);
        resetPlowingtimes();
        Collections.sort(vehicles, new TypeComparator());
        for (int x = 0; x < vehicles.size(); x++) {
            vehicles.get(x).reRoute();
        }
        return getMakeSpan(vehicles);
    }


    //This function maps a task sequence to a complete route, using the floyd warshall paths that has already been created.
    //The function adds the route from the depot to the first task, the route between consecutive tasks, and the route
    // from the last task and back to the depot.
    public ArrayList<Arc> getTourFromTasks(ArrayList<Arc> tasks, int vehicleID) {
        ArrayList<Arc> route = new ArrayList<>();
        if (tasks.size() == 0) {
            return route;
        }
        if (depot != tasks.get(0).from.nr) {
            route.addAll(getArcsFromPath(depot, tasks.get(0).from.nr, vehicleID));
        }
        for (int x = 0; x < tasks.size() - 1; x++) {
            route.add(tasks.get(x));
            //System.out.println(tasks.get(x+1));
            if (tasks.get(x).to.nr != tasks.get(x + 1).from.nr) {
                route.addAll(getArcsFromPath(tasks.get(x).to.nr, tasks.get(x + 1).from.nr, vehicleID));
            }
        }
        route.add(tasks.get(tasks.size() - 1));
        if (tasks.get(tasks.size() - 1).to.nr != depot) {
            route.addAll(getArcsFromPath(tasks.get(tasks.size() - 1).to.nr, depot, vehicleID));
        }
        return route;
    }

    /**
     * Deprecated
     */


    //This is a helping function, creating a route of tasks based on the startnodeId, endNodeID, and the vehicle type
    //It needs the vehicle type to make sure that the vehicle deadheads the an allowed path.
    public ArrayList<Arc> getArcsFromPath(int startNodeId, int endNodeId, int vehicleID) {
        ArrayList<Integer> path = getPath(startNodeId, endNodeId, vehicleID);
        ArrayList<Arc> arcPath = new ArrayList<>();

        if(vehicleID > 0){
            for (int x = 0; x < path.size() - 1; x++) {
                arcPath.add(arcNodeMapLaneDH.get(new ArcNodeIdentifier(path.get(x), path.get(x + 1))));
            }
        }
        else {
            for (int x = 0; x < path.size() - 1; x++) {
                arcPath.add(arcNodeMapSidewalkDH.get(new ArcNodeIdentifier(path.get(x), path.get(x + 1))));
            }
        }
        return arcPath;
    }

    //returns a path of arc identifiers using floyd warshall, when given a start node id and end node id.
    //It also needs the vehicle type, so that there is no illegal deadheading.
    public ArrayList<Integer> getPath(int startID, int endID, int vehicleId) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(startID);
        int nextID = startID;
        while (nextID != endID) {
            int oldId = nextID;
            if (vehicleId > 0) {
                nextID = FWLanePath[oldId][endID];
            } else {
                nextID = FWSidewalkPath[oldId][endID];
            }
            path.add(nextID);
        }
        return path;
    }


    //Returns the makespan from a solution
    public int getMakeSpan(ArrayList<Vehicle> vehicles) {
        int max = 0;
        for (int x = 0; x < vehicles.size(); x++) {
            if (vehicles.get(x).totalLength > max) {
                max = vehicles.get(x).totalLength;
            }
        }
        return max;
    }

    //Resets the plowingtimes of all arcs, so waiting time and makespan would be correct when calculating the makespan of
    //a solution
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



    //This is a helping function for the getFitnessParameters above, this function needs to have the arcs reset in terms of when they
    // are serviced. This to make sure that the makespan is calculated correctly.
    public int[] getMakeSpanParameters(ArrayList<Vehicle> vehicles) {
        int[] makespanParam = new int[3];
        int max = 0;
        int type = 0;
        int index = 0;
        for (int x = 0; x < vehicles.size(); x++) {
            if (vehicles.get(x).totalLength > max) {
                max = vehicles.get(x).totalLength;
                type = vehicles.get(x).id;
                index = x;
            }
        }
        makespanParam[0] = max;
        makespanParam[1] = type;
        makespanParam[2] = index;
        return makespanParam;
    }
    //Returns all lane arc identifiers and trip delimiters in an ascending id number. Helping function used
    //for generating random offspring
    public int[] getAscendingLanes() {
        int[] ascendingLanes = new int[lanes.size() + plowtrucks - 1];
        int counter = 0;
        int extra = 0;
        int counter2 = 0;
        for (int i = 0; i < ascendingLanes.length; i++) {
            if (counter >= ((lanes.size() - 1) / plowtrucks) && extra < (plowtrucks - 1)) {
                ascendingLanes[i] = -1;
                counter = 0;
                extra++;
                continue;
            }
            ascendingLanes[i] = lanes.get(counter2).identifier;
            counter++;
            counter2++;
        }
        return ascendingLanes;
    }
    //Returns all sidewalk arc identifiers and trip delimiters in an ascending id number. Helping function used
    //for generating random offspring
    public int[] getAscendingSidewalks() {
        int[] ascendingSidewalks = new int[sidewalks.size() + smallervehicles - 1];
        int counter = 0;
        int extra = 0;
        int counter2 = 0;
        for (int i = 0; i < ascendingSidewalks.length; i++) {
            if (counter >= ((sidewalks.size() - 1) / smallervehicles) && extra < (smallervehicles - 1)) {
                ascendingSidewalks[i] = -1;
                counter = 0;
                extra++;
                continue;
            }
            ascendingSidewalks[i] = sidewalks.get(counter2).identifier;
            counter++;
            counter2++;
        }
        return ascendingSidewalks;
    }

    //A function returning a random permutation of the number up to and excluding the bound.
    public ArrayList<Integer> getPermutation(int bound){
        ArrayList<Integer> permutation = new ArrayList<Integer>();
        for(int x = 0; x<bound; x++){
            permutation.add(x);
        }
        Collections.shuffle(permutation);
        return permutation;
    }



    //This function disallows U-Turns.
    public void disallowUturn() {
        this.uTurnsAllowed = false;
    }
}

