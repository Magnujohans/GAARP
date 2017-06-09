package InitialSolution;



import java.util.*;

/**
 * Created by Magnu on 23.11.2016.
 */
public class Algorithm {

    public ArrayList<Node> nodes;
    public ArrayList<Arc> allArcs;
    public ArrayList<Arc> arcs;
    public ArrayList<Node> sideWalkNodes;
    public ArrayList<Arc> sideWalkArcs;
    public ArrayList<Arc> onlySideWalkArcs;
    public HashMap<Integer, Arc> arcMap;
    public HashMap<Integer, Arc> SWarcMap;
    public HashMap<ArcNodeIdentifier, Arc> arcNodeMap;
    public HashMap<SWArcNodeIdentifier, Arc> SWarcNodeMap;
    public HashMap<ArcNodeIdentifier, Arc> arcNodeMapLaneDH;
    public HashMap<ArcNodeIdentifier, Arc> arcNodeMapSidewalkDH;


    public int[][] graph;
    public int[][] nrPlowjobs;
    public int[][] swMatrix;
    public int[][] originalGraphClone;
    //public int[][] bestMatrix;
    public int[][] deadheadLane;
    public int[][] deadheadSW;


    public int[][] fwGraph;
    public int[][] fwPath;
    public int[][] fwGraphSW;
    public int[][] fwPathSW;
    //public int[][] fwBestGraph;
    //public int[][] fwBestPath;

    public ArrayList<Integer> GiantTour;
    public ArrayList<Integer> GiantTourNoDuplicates;
    public ArrayList<Integer> GiantTourNoDuplicatesSW;
    public int depot;
    public int nrVehicles;
    public int nrSWVehicles;
    public int capacity;

    public ArrayList<Vehicle> vehicles;

    public GiantTourGenerator G;


    //The entire algorithm is placed in the constructor, setting up all initial information
    public Algorithm(int[][] inputGraph, int[][] nrPlowjobs, int[][] inputSWMatrix, int[][] deadHeadingLane, int[][] deadHeadingSidewalk, int depot, int nrVehicles, int nrSWVehicles) {
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();
        sideWalkArcs = new ArrayList<>();
        sideWalkNodes = new ArrayList<>();
        onlySideWalkArcs = new ArrayList<>();
        allArcs = new ArrayList<>();
        arcMap = new HashMap<>();
        arcNodeMap = new HashMap<>();
        arcNodeMapLaneDH = new HashMap<>();
        arcNodeMapSidewalkDH = new HashMap<>();
        SWarcNodeMap = new HashMap<>();


        this.depot = depot;
        this.nrVehicles = nrVehicles;
        this.nrSWVehicles = nrSWVehicles;

        graph = RemoveDepot(inputGraph);
        nrPlowjobs = RemoveDepot(nrPlowjobs);
        swMatrix = RemoveDepot(inputSWMatrix);
        deadheadLane = RemoveDepot(deadHeadingLane);
        deadheadSW = RemoveDepot(deadHeadingSidewalk);
        originalGraphClone = graph.clone();


        floydWarshall fw1 = new floydWarshall();
        floydWarshall fw2 = new floydWarshall();
        fwGraph = fw1.Algorithm(deadheadLane);
        fwPath = fw1.path;
        fwGraphSW = fw2.Algorithm(deadheadSW);
        fwPathSW = fw2.path;



        for (int x = 0; x < graph.length; x++) {
            nodes.add(new Node(x));

        }

        int counter = 0;
        for (int x = 0; x < graph.length; x++) {
            for (int y = 0; y < graph[x].length; y++) {
                //If we have an entry in input matrix, we have an arc.
                if (graph[x][y] >= 0 && x != y) {
                    ArrayList<Arc> tempList = new ArrayList<>();
                    for (int z = 0; z <nrPlowjobs[x][y]; z++){
                        Arc tempArc = new Arc(nodes.get(x), nodes.get(y), graph[x][y], 1, counter);
                        arcMap.put(counter, tempArc);
                        arcNodeMap.put(new ArcNodeIdentifier(nodes.get(x).nr, nodes.get(y).nr), tempArc);
                        counter++;
                        arcs.add(tempArc);
                        allArcs.add(tempArc);

                        nodes.get(x).outGoing.add(tempArc);
                        nodes.get(y).inComing.add(tempArc);
                        tempList.add(tempArc);
                    }

                    //If the Sidewalk matrix has a higher value, we have a sidewalk that needs to be serviced, with
                    //Precedence constraints.
                    if (swMatrix[x][y] > 0) {
                        Arc tempSW = new Arc(nodes.get(x), nodes.get(y), swMatrix[x][y], 2, counter);
                        arcMap.put(counter, tempSW);
                        SWarcNodeMap.put(new SWArcNodeIdentifier(nodes.get(x).nr, nodes.get(y).nr), tempSW);
                        sideWalkArcs.add(tempSW);
                        //allArcs.add(tempSW);
                        for(int z = 0; z < tempList.size(); z++){
                            tempSW.addPrecedingArc(tempList.get(z));
                            tempList.get(z).addSuccedingArc(tempSW);
                        }
                        nodes.get(x).outGoingSW.add(tempSW);
                        nodes.get(y).inComingSW.add(tempSW);
                        counter++;
                    }
                }
                //If we have an entry in the sidewalkMatrix where we don't have an entry in the laneMatrix, we have a sidewalk here
                //without precedence constraints
                if (graph[x][y] < 0 && swMatrix[x][y] >= 0 && x != y) {
                    Arc tempSW = new Arc(nodes.get(x), nodes.get(y), swMatrix[x][y], 2, counter);
                    arcMap.put(counter, tempSW);
                    arcNodeMap.put(new ArcNodeIdentifier(nodes.get(x).nr, nodes.get(y).nr), tempSW);
                    SWarcNodeMap.put(new SWArcNodeIdentifier(nodes.get(x).nr, nodes.get(y).nr), tempSW);
                    sideWalkArcs.add(tempSW);
                    onlySideWalkArcs.add(tempSW);
                    allArcs.add(tempSW);
                    nodes.get(x).outGoingSW.add(tempSW);
                    nodes.get(y).inComingSW.add(tempSW);
                    counter++;
                }
                if (deadheadLane[x][y] >= 0 && x != y) {
                    Arc tempDH = new Arc(nodes.get(x), nodes.get(y), deadheadLane[x][y], 3, counter);
                    arcNodeMapLaneDH.put(new ArcNodeIdentifier(nodes.get(x).nr, nodes.get(y).nr), tempDH);
                    counter++;

                }
                if (deadheadSW[x][y] >= 0 && x != y) {
                    Arc tempSWDH = new Arc(nodes.get(x), nodes.get(y), deadheadSW[x][y], 4, counter);
                    arcNodeMapSidewalkDH.put(new ArcNodeIdentifier(nodes.get(x).nr, nodes.get(y).nr), tempSWDH);
                    counter++;

                }
            }
        }
    }

    //We dont need the depots in this implementation. They are always the first and the last node.
    public int[][] RemoveDepot(int[][] inputGraph) {
        int[][] graph = new int[inputGraph.length - 2][inputGraph.length - 2];
        for (int x = 1; x < inputGraph.length - 1; x++) {
            for (int y = 1; y < inputGraph.length - 1; y++) {
                graph[x - 1][y - 1] = inputGraph[x][y];
            }
        }
        return graph;
    }




    //Iterates through the arcs, checks that they are serviced, and that they are serviced at legal times
    public boolean feasibileSolution() {
        boolean feasible = true;
        for (int x = 0; x < arcs.size(); x++) {
            if (!arcs.get(x).serviced) {
                feasible = false;
            }
        }
        for (int x = 0; x < sideWalkArcs.size(); x++) {
            if (!sideWalkArcs.get(x).serviced || sideWalkArcs.get(x).startOfService < sideWalkArcs.get(x).getEarliestStartingTimeForThisArc()) {
                feasible = false;
                System.out.println(sideWalkArcs.get(x).from.nr + " to " + sideWalkArcs.get(x).to.nr);

            }
        }

        return feasible;
    }


}