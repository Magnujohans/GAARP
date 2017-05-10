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
            if(!uTurnsAllowed && this.id > 0 && x > 0 && route.get(x).to == route.get(x-1).from ){
                cost += 99999;
            }
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
        //System.out.println("Halla");
    }

    //Simulates the plowing of the vehicles.
    public void reRoute(){
        getStartTimesAndTotalCost();
    }

    public void setRoute(ArrayList<Arc> route){
        this.route = route;
    }

    public Arc getArcWithHighestInverseWaitingTime(){
        int min = 0;
        Arc temp = tasks.get(tasks.size()-1);
        for(int x = 0; x < tasks.size(); x++){
            if(tasks.get(x).waitingTime < min){
                min = tasks.get(x).waitingTime;
                temp = tasks.get(x);
            }
        }
        return temp;
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

    //The output does not say whether the vehicle waits or not.
    @Override
    public String toString(){
        String s = "";
        if(route.size() == 0){
            s += "Vehicle " + id + " stays in depot";
            return s;
        }


        for(int x = 0; x<route.size()-1; x++){
            if(route.get(x).startOfService == StartTimeForArcs.get(x)){
                s += "Vehicle " + id + " plows arc " + route.get(x).identifier + " from " + (route.get(x).from.nr+2) + " to " + (route.get(x).to.nr+2)
                        + ". This takes " + route.get(x).length + " time units";
            }
            else{
                s += "Vehicle " + id + " takes arc " + route.get(x).identifier + " from " + (route.get(x).from.nr+2) + " to " + (route.get(x).to.nr+2)
                        + ". This takes " + route.get(x).length + " time units";
            }
            if(route.get(x).type == 2){
                s += ". This is a Sidewalk";
            }
            if(x> 0 && (StartTimeForArcs.get(x+1) -  (StartTimeForArcs.get(x) + route.get(x).length) > 0)){
                s += " And it has to wait for " + (StartTimeForArcs.get(x+1) -  (StartTimeForArcs.get(x) + route.get(x).length)) + " time units";
            }
            //s +=". It starts plowing the arc at time " + StartTimeForArcs.get(x)+ "\n";
            s +=". It is done at time " + StartTimeForArcs.get(x+1)+ "\n";
        }
        s += ". Total time of route is " + (StartTimeForArcs.get(StartTimeForArcs.size()-1) + route.get(route.size()-1).length);
        s += " Total time is " + totalLength;
        return s;
    }


}
