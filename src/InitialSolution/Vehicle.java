package InitialSolution;

import java.util.ArrayList;

/**
 * Created by Magnu on 06.12.2016.
 */

//The Vehicle class. Has a task sequence and a route. A positive ID is a plowing truck. A negative ID is a smaller vehicle.
public class Vehicle{
    public int id;
    public ArrayList<Arc> route;
    public ArrayList<Integer> StartTimeForArcs;
    public ArrayList<Arc> tasks;
    public int totalLength;


    public Vehicle(int id, ArrayList<Arc> tasks, ArrayList<Arc> route){
        this.id = id;
        this.tasks = tasks;
        this.route = route;
        //getStartTimesAndTotalCost();
    }
    //This is basically the simulating algorithm. Checks when the vehicle arrives at at arc, if it is the earliest plowing truck, the earliest time is set
    // to this. If it is a smaller vehicle, it cannot continue until it has waited.
    public void getStartTimesAndTotalCost(){
        ArrayList<Integer> startTimes = new ArrayList<>();
        int cost = 0;
        for(int x = 0; x<route.size(); x++){
            if(this.id < 0) {
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
            startTimes.add(cost);
            route.get(x).setStartOfService(cost);
            route.get(x).serveArc();
            cost += route.get(x).length;

        }
        totalLength = cost;
        StartTimeForArcs = startTimes;
    }

    //Simulates the plowing of the vehicles.
    public void reRoute(){
        getStartTimesAndTotalCost();
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

    //The output does not say whether the vehicle waits or not.
    @Override
    public String toString(){
        String s = "";
        for(int x = 0; x<route.size(); x++){
            s += "Vehicle " + id + " Takes arc " + route.get(x).identifier + " from " + route.get(x).from.nr + " to " + route.get(x).to.nr
                    + ". This takes " + route.get(x).length +" , .and it starts plowing the arc at time " + StartTimeForArcs.get(x)+ "\n";
        }
        s += ". Total time of route is " + (StartTimeForArcs.get(StartTimeForArcs.size()-1) + route.get(route.size()-1).length);
        return s;
    }


}
