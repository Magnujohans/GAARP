package InitialSolution;

import java.util.ArrayList;

/**
 * Created by Magnu on 06.12.2016.
 */

//The Vehicle class. Has a task sequence and a route. A positive ID is a plowing truck. A negative ID is a smaller vehicle.
public class Vehicle{
    private final boolean uTurnsAllowed;
    public int id;
    public ArrayList<Arc> route;
    public ArrayList<Integer> StartTimeForArcs;
    public ArrayList<Arc> tasks;
    public int totalLength;


    public Vehicle(int id, ArrayList<Arc> tasks, ArrayList<Arc> route, boolean uTurnsAllowed){
        this.id = id;
        this.tasks = tasks;
        this.route = route;
        this.StartTimeForArcs = new ArrayList<>();
        this.uTurnsAllowed = uTurnsAllowed;
        //getStartTimesAndTotalCost();
    }
    //This is basically the simulating algorithm. Checks when the vehicle arrives at at arc, if it is the earliest plowing truck, the earliest time is set
    // to this. If it is a smaller vehicle, it cannot continue until it has waited.
    public void getStartTimesAndTotalCost(){
        ArrayList<Integer> startTimes = new ArrayList<>();
        int cost = 0;
        for(int x = 0; x<route.size(); x++){
            if(this.id <0 && route.get(x).type == 2) {
                if (route.get(x).getEarliestStartingTimeForThisArc() > 0) {
                    if (cost < route.get(x).getEarliestStartingTimeForThisArc()) {
                        //route.get(x).setWaitingTime(route.get(x).getEarliestStartingTimeForThisArc() - cost);
                        for (int y = 0; y < route.get(x).haveToPreceed.size(); y++) {
                            route.get(x).haveToPreceed.get(y).setWaitingTime(cost - route.get(x).getEarliestStartingTimeForThisArc());
                        }
                        cost = route.get(x).getEarliestStartingTimeForThisArc();
                    }
                }
            }
            //This check adds a high penalty if there are U-turns in the solution, when U-turns are not allowed.
            if(!uTurnsAllowed && this.id > 0 && x > 0 && route.get(x).to == route.get(x-1).from ){
                cost += 10000;
            }

            //Implementation specific check. type 1 and 2 are arcs that should be serviced, while 3 and 4 are deadheading arcs.
            if(this.id > 0 && route.get(x).type == 1){
                route.get(x).setStartOfService(cost);
                route.get(x).serveArc();
            }
            if(this.id < 0 && route.get(x).type == 2){
                route.get(x).setStartOfService(cost);
                route.get(x).serveArc();
            }
            startTimes.add(cost);
            cost += route.get(x).length;

        }
        totalLength = cost;
        StartTimeForArcs = startTimes;
    }

    //Simulates the plowing of the vehicles.
    public void reRoute(){
        getStartTimesAndTotalCost();
    }

    public void setRoute(ArrayList<Arc> route){
        this.route = route;
    }


    public Vehicle copyVehicle(){
        Vehicle copy = new Vehicle(this.id, (ArrayList<Arc>) this.tasks.clone(), (ArrayList<Arc>) this.route.clone(), this.uTurnsAllowed);
        return copy;
    }

    public ArrayList<Arc> copyTasks(){
        return (ArrayList<Arc>) this.tasks.clone();
    }

    public ArrayList<Arc> copyRoute(){
        return (ArrayList<Arc>) this.route.clone();
    }

    //The output adds 2 to each node, this is so that the nodes will correspond to Arc-Flow model(1-indexed, and with artificial depot)
    @Override
    public String toString(){
        String s = "";
        if(route.size() == 0){
            s += "Vehicle " + id + " stays in depot";
            return s;
        }


        for(int x = 0; x<route.size(); x++){
            if(route.get(x).startOfService == StartTimeForArcs.get(x)){
                s += "Vehicle " + id + " plows arc " + route.get(x).identifier + " from " + (route.get(x).from.nr+2) + " to " + (route.get(x).to.nr+2)
                        + ". This takes " + route.get(x).length + " time units";
            }
            else{
                s += "Vehicle " + id + " takes arc " + route.get(x).identifier + " from " + (route.get(x).from.nr+2) + " to " + (route.get(x).to.nr+2)
                        + ". This takes " + route.get(x).length + " time units";
            }

            if(x> 0 && x < route.size()-1 &&(StartTimeForArcs.get(x) -  (StartTimeForArcs.get(x-1) + route.get(x-1).length) > 0)){
                s += ", but it has to wait for " + (StartTimeForArcs.get(x) -  (StartTimeForArcs.get(x-1) + route.get(x-1).length)) + " time units first";
            }
            //s +=". It starts plowing the arc at time " + StartTimeForArcs.get(x)+ "\n";
            if(x < route.size()){
                s +=". It starts at time " + StartTimeForArcs.get(x);
                s +=", and arrives at time " + (StartTimeForArcs.get(x)+route.get(x).length) + "\n";
            }

        }

        s += "Total time is " + totalLength;
        return s;
    }


}
