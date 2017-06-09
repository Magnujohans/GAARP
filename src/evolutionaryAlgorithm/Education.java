package evolutionaryAlgorithm;

import InitialSolution.Vehicle;
import InitialSolution.Arc;

import java.util.*;

/**
 * Created by Magnu on 01.03.2017.
 */


//This is class represents the Education phase from the paper.
public class Education {
    Fenotype fenotype;
    Random rng;
    double educationRate;

    public Education(Fenotype fenotype, double educationRate){
        this.fenotype = fenotype;
        this.educationRate = educationRate;
        rng = new Random();
    }


    //This goes through each of the Offspring created and educates them given a probability(Parameter test showed that
    //to always educate was the best choice.
    public ArrayList<Genotype> educateChildren(ArrayList<Arc> arcs, ArrayList<Arc> lanes, ArrayList<Arc> sidewalks, ArrayList<Genotype> children){
        ArrayList<Genotype> educatedChildren = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            ArrayList<Vehicle> tempChild = fenotype.getFenotype(children.get(i));
            Object[] tempResult;
            if(rng.nextDouble() < educationRate){
                tempResult = educateOffspring(arcs, lanes, sidewalks, tempChild);
            }
            else{
                int fitness = fenotype.calculateFitness(tempChild);
                Object[] returnlist = new Object[2];
                returnlist[0] = tempChild;
                returnlist[1] = fitness;

                tempResult = returnlist;
            }
            ArrayList<Vehicle> tempResultFenotype = (ArrayList<Vehicle>) tempResult[0];
            Genotype tempResultGenotype = fenotype.createGenotype(tempResultFenotype, (Integer) tempResult[1]);
            educatedChildren.add(tempResultGenotype);
        }
        return educatedChildren;
    }



    //All numbers here refer to a "move" in the paper, for description, please consult the description there
    public Object[] One(ArrayList<Vehicle> vehicles, Vehicle from, Vehicle to, int u, int v){
        if(from == to){
            Arc V = to.tasks.get(v);
            Arc move = from.tasks.get(u);
            from.tasks.remove(move);
            from.tasks.add(from.tasks.indexOf(V), move);
        }
        else{
            Arc move = from.tasks.get(u);
            to.tasks.add(v+1, move);
            from.tasks.remove(move);
        }

        to.route = fenotype.getTourFromTasks(to.tasks, to.id);
        from.route = fenotype.getTourFromTasks(from.tasks, from.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;

    }

    public Object[] Two(ArrayList<Vehicle> vehicles, Vehicle from, Vehicle to, int u, int v){


        Arc V = to.tasks.get(v);
        Arc U = from.tasks.get(u);
        if(u == from.tasks.size()-1){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;
            return returnlist;
        }
        Arc X = from.tasks.get(u+1);

        from.tasks.remove(U);
        from.tasks.remove(X);
        to.tasks.add(to.tasks.indexOf(V)+1, X);
        to.tasks.add(to.tasks.indexOf(V)+1, U);




        to.route = fenotype.getTourFromTasks(to.tasks, to.id);
        from.route = fenotype.getTourFromTasks(from.tasks, from.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;

    }

    public Object[] Three(ArrayList<Vehicle> vehicles, Vehicle from, Vehicle to, int u, int v){
        Arc V = to.tasks.get(v);
        Arc U = from.tasks.get(u);
        if(u == from.tasks.size()-1){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;
            return returnlist;
        }
        Arc X = from.tasks.get(u+1);

        from.tasks.remove(U);
        from.tasks.remove(X);
        to.tasks.add(to.tasks.indexOf(V)+1, U);
        to.tasks.add(to.tasks.indexOf(V)+1, X);



        to.route = fenotype.getTourFromTasks(to.tasks, to.id);
        from.route = fenotype.getTourFromTasks(from.tasks, from.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;

    }

    public Object[] Four(ArrayList<Vehicle> vehicles, Vehicle vehicleOne,Vehicle vehicleTwo, int u, int v){
        Arc ArcOne = vehicleOne.tasks.get(u);
        Arc ArcTwo = vehicleTwo.tasks.get(v);

        vehicleTwo.tasks.set(v,ArcOne);
        vehicleOne.tasks.set(u, ArcTwo);

        vehicleOne.route = fenotype.getTourFromTasks(vehicleOne.tasks, vehicleOne.id);
        vehicleTwo.route = fenotype.getTourFromTasks(vehicleTwo.tasks, vehicleTwo.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Five(ArrayList<Vehicle> vehicles, Vehicle vehicleOne,Vehicle vehicleTwo, int u, int v){
        Arc ArcOne = vehicleOne.tasks.get(u);

        if(u == vehicleOne.tasks.size()-1 || (vehicleOne == vehicleTwo && u+1 == v)){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;

            return returnlist;
        }
        Arc X = vehicleOne.tasks.get(u+1);
        Arc ArcTwo = vehicleTwo.tasks.get(v);

        vehicleTwo.tasks.set(v, ArcOne);
        vehicleOne.tasks.set(u, ArcTwo);
        vehicleOne.tasks.remove(X);
        vehicleTwo.tasks.add(vehicleTwo.tasks.indexOf(ArcOne)+1, X);


        vehicleOne.route = fenotype.getTourFromTasks(vehicleOne.tasks, vehicleOne.id);
        vehicleTwo.route = fenotype.getTourFromTasks(vehicleTwo.tasks, vehicleTwo.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Six(ArrayList<Vehicle> vehicles, Vehicle vehicleOne,Vehicle vehicleTwo, int u, int v){
        Arc ArcOne = vehicleOne.tasks.get(u);
        Arc ArcTwo = vehicleTwo.tasks.get(v);

        if(u == vehicleOne.tasks.size()-1 || v == vehicleTwo.tasks.size()-1){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;

            return returnlist;
        }
        Arc X = vehicleOne.tasks.get(u+1);
        Arc Y = vehicleTwo.tasks.get(v+1);

        if(vehicleOne ==vehicleTwo){
            Collections.swap(vehicleOne.tasks, u, v);
            Collections.swap(vehicleOne.tasks, (u+1), (v+1));
        }
        else{
            vehicleOne.tasks.set(u, ArcTwo);
            vehicleTwo.tasks.set(v, ArcOne);
            //vehicleOne.tasks.set(u, vehicleTwo.tasks.set(v, ArcOne));
            vehicleTwo.tasks.set((v+1), X);
            vehicleOne.tasks.set((u+1), Y);
            //vehicleOne.tasks.set((u+1), vehicleTwo.tasks.set((v+1), X));
        }


        vehicleOne.route = fenotype.getTourFromTasks(vehicleOne.tasks, vehicleOne.id);
        vehicleTwo.route = fenotype.getTourFromTasks(vehicleTwo.tasks, vehicleTwo.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Seven(ArrayList<Vehicle> vehicles, Vehicle vehicleOne,Vehicle vehicleTwo, int u, int v){
        Arc ArcOne = vehicleOne.tasks.get(u);
        Arc ArcTwo = vehicleTwo.tasks.get(v);

        if(u == vehicleOne.tasks.size()-1 || v == vehicleTwo.tasks.size()-1){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;

            return returnlist;
        }
        Arc X = vehicleOne.tasks.get(u+1);
        Arc Y = vehicleTwo.tasks.get(v+1);

        if (vehicleOne == vehicleTwo){
            Collections.swap(vehicleOne.tasks, v, u+1);
        }
        else{
            vehicleOne.tasks.set(u+1, ArcTwo );
            vehicleTwo.tasks.set(v, X);
        }


        vehicleOne.route = fenotype.getTourFromTasks(vehicleOne.tasks, vehicleOne.id);
        vehicleTwo.route = fenotype.getTourFromTasks(vehicleTwo.tasks, vehicleTwo.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Eight(ArrayList<Vehicle> vehicles, Vehicle vehicleOne,Vehicle vehicleTwo, int u, int v){
        Arc ArcOne = vehicleOne.tasks.get(u);
        Arc ArcTwo = vehicleTwo.tasks.get(v);

        if(u == vehicleOne.tasks.size()-1 || v == vehicleTwo.tasks.size()-1){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;

            return returnlist;
        }
        Arc X = vehicleOne.tasks.get(u+1);
        Arc Y = vehicleTwo.tasks.get(v+1);

        vehicleOne.tasks.set(u+1, Y );
        vehicleTwo.tasks.set(v, X);
        vehicleTwo.tasks.set(v+1, ArcTwo);


        vehicleOne.route = fenotype.getTourFromTasks(vehicleOne.tasks, vehicleOne.id);
        vehicleTwo.route = fenotype.getTourFromTasks(vehicleTwo.tasks, vehicleTwo.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Nine(ArrayList<Vehicle> vehicles, Arc one, Arc two){
        if(one == two){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;

            return returnlist;
        }


        Vehicle One = getVehicleWithTask(vehicles, one);
        Vehicle Two = getVehicleWithTask(vehicles, two);
        int i = getIndexofTask(One, one);
        int j = getIndexofTask(Two, two);


        if(Two == One){
            One.tasks.remove(one);
            One.tasks.add(One.tasks.indexOf(two),one);
        }
        else{
            Two.tasks.add(j+1, one);
            One.tasks.remove(one);
        }


        One.route = fenotype.getTourFromTasks(One.tasks, One.id);
        Two.route = fenotype.getTourFromTasks(Two.tasks, Two.id);

        int fitness = fenotype.calculateFitness(vehicles);
        int sum = calculateSum(vehicles);

        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Ten(ArrayList<Vehicle> vehicles, Arc one, Arc two, Arc three, Arc four){
        /*if(one == two || three == four || three == two || one == four || one == three || two == four){
            Object[] returnlist = new Object[2];
            returnlist[0] = vehicles;
            returnlist[1] = -1;

            return returnlist;
        }*/

        Vehicle One = getVehicleWithTask(vehicles, one);
        Vehicle Two = getVehicleWithTask(vehicles, two);
        Vehicle Three = getVehicleWithTask(vehicles, three);
        Vehicle Four = getVehicleWithTask(vehicles, four);
        int i = getIndexofTask(One, one);
        int j = getIndexofTask(Two, two);
        int k = getIndexofTask(Three, three);
        int l = getIndexofTask(Four, four);

        if(Two == One){
            One.tasks.remove(one);
            One.tasks.add(One.tasks.indexOf(two),one);
        }
        else if (Two != One){
            Two.tasks.add(j+1, one);
            One.tasks.remove(one);
        }

        if(Three == Four){
            Three.tasks.remove(three);
            Three.tasks.add(Three.tasks.indexOf(four),three);
        }
        else if(Three != Four){
            Four.tasks.add(l+1, three);
            Three.tasks.remove(three);
        }


        One.route = fenotype.getTourFromTasks(One.tasks, One.id);
        Two.route = fenotype.getTourFromTasks(Two.tasks, Two.id);
        Three.route = fenotype.getTourFromTasks(Three.tasks, Three.id);
        Four.route = fenotype.getTourFromTasks(Four.tasks, Four.id);

        int fitness = fenotype.calculateFitness(vehicles);
        int sum = calculateSum(vehicles);

        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Eleven(ArrayList<Vehicle> vehicles, Arc one, Arc two){
        Vehicle One = getVehicleWithTask(vehicles, one);
        Vehicle Two = getVehicleWithTask(vehicles, two);
        int i = getIndexofTask(One, one);
        int j = getIndexofTask(Two, two);

        Two.tasks.set(j,one);
        One.tasks.set(i,two);

        One.route = fenotype.getTourFromTasks(One.tasks, One.id);
        Two.route = fenotype.getTourFromTasks(Two.tasks, Two.id);

        int fitness = fenotype.calculateFitness(vehicles);
        int sum = calculateSum(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public Object[] Twelve(ArrayList<Vehicle> vehicles, Arc one, Arc two, Arc three, Arc four){
        Vehicle One = getVehicleWithTask(vehicles, one);
        Vehicle Two = getVehicleWithTask(vehicles, two);

        int i = getIndexofTask(One, one);
        int j = getIndexofTask(Two, two);


        Two.tasks.set(j,one);
        One.tasks.set(i,two);

        Vehicle Three = getVehicleWithTask(vehicles, three);
        Vehicle Four = getVehicleWithTask(vehicles, four);

        int k = getIndexofTask(Three, three);
        int l = getIndexofTask(Four, four);

        Four.tasks.set(l,three);
        Three.tasks.set(k,four);


        One.route = fenotype.getTourFromTasks(One.tasks, One.id);
        Two.route = fenotype.getTourFromTasks(Two.tasks, Two.id);
        Three.route = fenotype.getTourFromTasks(Three.tasks, Three.id);
        Four.route = fenotype.getTourFromTasks(Four.tasks, Four.id);

        int fitness = fenotype.calculateFitness(vehicles);
        int sum = calculateSum(vehicles);

        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    //This returns the current vehicle which has a given task in this solution.
    public Vehicle getVehicleWithTask(ArrayList<Vehicle> vehicles, Arc task){
        for (Vehicle vehicle : vehicles) {
            if(vehicle.tasks.indexOf(task) > -1){
                return vehicle;
            }
        }
        return null;
    }

    //This returns the given index of a task in the vehicle in which it resides.
    public int getIndexofTask(Vehicle vehicle, Arc task){
        return vehicle.tasks.indexOf(task);
    }


    //Helping function, used to generate a random ArrayList of integers. This is used to generate random arc sequences, and move sequences.
    public ArrayList<Integer> getPermutation(int bound){
        ArrayList<Integer> permutation = new ArrayList<Integer>();
        for(int x = 0; x<bound; x++){
            permutation.add(x);
        }
        Collections.shuffle(permutation);
        return permutation;
    }

    //Using the function above, this function is used to randomly iterate over the 7 neighbourhood moves.
    public Object[] runNeighborMove(ArrayList<Vehicle> vehicles, int nr, Vehicle one, Vehicle two, int u, int v){
        Object[] result;
        switch (nr){
            case 0: result = One(vehicles,one,two,u,v);
                break;
            case 1: result = Two(vehicles,one,two,u,v);
                break;
            case 2: result = Three(vehicles,one,two,u,v);
                break;
            case 3: result = Four(vehicles,one,two,u,v);
                break;
            case 4: result = Five(vehicles,one,two,u,v);
                break;
            case 5: result = Six(vehicles,one,two,u,v);
                break;
            case 6: result = Seven(vehicles,one,two,u,v);
                break;
            case 7: result = Eight(vehicles,one,two,u,v);
                break;
            default: result = new Object[2];
                result[0] = vehicles;
                result[1] = -1;
                break;
        }
        return result;
    }

    //This is the same as the above, but adapted to the random moves where maximum two arcs are involved.
    public Object[] runRandomSingleMove(ArrayList<Vehicle> vehicles, int nr, Arc one, Arc two){
        Object[] result;
        switch (nr){
            case 0: result = Nine(vehicles,one,two);
                break;
            case 1: result = Eleven(vehicles,one,two);
                break;
            default: result = new Object[2];
                result[0] = vehicles;
                result[1] = -1;
                break;
        }
        return result;
    }

    //This is the same as the above, but adapted to the random moves where four arcs are involved.
    public Object[] runRandomDoubleMove(ArrayList<Vehicle> vehicles, int nr, Arc one, Arc two, Arc three, Arc four ){
        Object[] result;
        switch (nr){
            case 2: result = Ten(vehicles,one,two,three,four);
                break;
            case 3: result = Twelve(vehicles,one,two,three,four);
                break;
            default: result = new Object[2];
                result[0] = vehicles;
                result[1] = -1;
                break;
        }
        return result;
    }

    //This is the whole educate process for a solution. Iterates randomly over the arcs and its neighbours, and randomly over the
    //different neighbourhood moves. If this is unsuccesful, it iterates randomly over the last four random moves as well.
    //A more detailed instruction can be found in the paper.
    public Object[] educateOffspring(ArrayList<Arc> arcs, ArrayList<Arc> lanes, ArrayList<Arc> sidewalks, ArrayList<Vehicle> initial){
        ArrayList<Vehicle> bestVehicles = initial;
        int bestFitness = fenotype.calculateFitness(bestVehicles);
        Object[] result;

        ArrayList<Vehicle> tempVehicles = new ArrayList<>();
        for (int y = 0; y < bestVehicles.size(); y++) {
            tempVehicles.add(bestVehicles.get(y).copyVehicle());
        }

        ArrayList<Integer> ArcSequence = getPermutation(arcs.size());
        for (int i = 0; i < ArcSequence.size(); i++) {
            //System.out.println("Ny Bil");
            Arc ArcOne = arcs.get(ArcSequence.get(i));
            Vehicle vehicleOne = getVehicleWithTask(tempVehicles, ArcOne);
            int u = getIndexofTask(vehicleOne, ArcOne);
            ArrayList<Integer> neighbourSequence = getPermutation(ArcOne.neighbours.size());
            for (int j = 0; j < ArcOne.neighbours.size(); j++) {

                Arc ArcTwo = ArcOne.neighbours.get(neighbourSequence.get(j));
                Vehicle vehicleTwo = getVehicleWithTask(tempVehicles, ArcTwo);
                int v = getIndexofTask(vehicleTwo, ArcTwo);
                ArrayList<Integer> moveSequence;
                if(vehicleOne == vehicleTwo){
                    moveSequence = getPermutation(7);
                }
                else{
                    moveSequence = getPermutation(8);
                }
                for (int x = 0; x < moveSequence.size(); x++){

                    for (int y = 0; y < tempVehicles.size(); y++) {
                        tempVehicles.get(y).tasks = bestVehicles.get(y).copyTasks();
                        tempVehicles.get(y).route = bestVehicles.get(y).copyRoute();
                    }
                    int[] vehicleTimeBefore = fenotype.calculateVehicleFitness(tempVehicles, vehicleOne, vehicleTwo);
                    result = runNeighborMove(tempVehicles, moveSequence.get(x), vehicleOne, vehicleTwo, u, v);
                    int[] vehicleTimeAfter = fenotype.calculateVehicleFitness(tempVehicles, vehicleOne, vehicleTwo);

                    if(((Integer) result[1]) < 0){
                        for (int y = 0; y < tempVehicles.size(); y++) {
                            tempVehicles.get(y).tasks = bestVehicles.get(y).copyTasks();
                            tempVehicles.get(y).route = bestVehicles.get(y).copyRoute();

                        }
                        continue;
                    }
                    int vehicleTimeDifference = (vehicleTimeBefore[0] - vehicleTimeAfter[0]) + (vehicleTimeBefore[1] - vehicleTimeAfter[1]);

                    if( (vehicleTimeDifference > 0 && ((Integer) result[1]) <= bestFitness) ||  ((Integer) result[1]) < bestFitness){
                        //System.out.println(moveSequence.get(x));
                        return result;
                    }
                    else{
                        for (int y = 0; y < tempVehicles.size(); y++) {
                            tempVehicles.get(y).tasks = bestVehicles.get(y).copyTasks();
                            tempVehicles.get(y).route = bestVehicles.get(y).copyRoute();
                        }
                    }

                }


            }

        }

        ArcSequence = getPermutation(arcs.size());
        ArrayList<Arc> possible;
        for (int i = 0; i < ArcSequence.size(); i++) {
            Arc ArcOne = arcs.get(ArcSequence.get(i));
            if (ArcOne.type == 1){
                possible = lanes;
            }
            else{
                possible = sidewalks;
            }
            Vehicle vehicleOne = getVehicleWithTask(tempVehicles, ArcOne);

            Arc ArcTwo = possible.get(rng.nextInt(possible.size()));
            Arc ArcThree = possible.get(rng.nextInt(possible.size()));
            Arc ArcFour = possible.get(rng.nextInt(possible.size()));

            Vehicle vehicleTwo = getVehicleWithTask(tempVehicles, ArcTwo);
            Vehicle vehicleThree = getVehicleWithTask(tempVehicles, ArcThree);
            Vehicle vehicleFour = getVehicleWithTask(tempVehicles, ArcFour);

            ArrayList<Integer> moveSequence;
            moveSequence = getPermutation(4);
            for (int x = 0; x < moveSequence.size(); x++){
                for (int y = 0; y < tempVehicles.size(); y++) {
                    tempVehicles.get(y).tasks = bestVehicles.get(y).copyTasks();
                    tempVehicles.get(y).route = bestVehicles.get(y).copyRoute();
                }

                int[] vehicleTimeBefore = fenotype.calculateVehicleFitness(tempVehicles, vehicleOne, vehicleTwo, vehicleThree,vehicleFour);
                if(moveSequence.get(x) < 3){
                    result = runRandomSingleMove(tempVehicles,moveSequence.get(x),ArcOne,ArcTwo);
                }
                else{
                    result = runRandomDoubleMove(tempVehicles,moveSequence.get(x),ArcOne,ArcTwo,ArcThree,ArcFour);
                }
                int[] vehicleTimeAfter = fenotype.calculateVehicleFitness(tempVehicles, vehicleOne, vehicleTwo,vehicleThree,vehicleFour);

                if(((Integer) result[1]) < 0){
                    for (int y = 0; y < tempVehicles.size(); y++) {
                        tempVehicles.get(y).tasks = bestVehicles.get(y).copyTasks();
                        tempVehicles.get(y).route = bestVehicles.get(y).copyRoute();

                    }
                    continue;
                }
                int vehicleTimeDifference = (vehicleTimeBefore[0] - vehicleTimeAfter[0]) + (vehicleTimeBefore[1] - vehicleTimeAfter[1]);
                vehicleTimeDifference += (vehicleTimeBefore[2] - vehicleTimeAfter[2]) + (vehicleTimeBefore[3] - vehicleTimeAfter[3]);

                if( (vehicleTimeDifference > 0 && ((Integer) result[1]) <= bestFitness) ||  ((Integer) result[1]) < bestFitness){
                    //System.out.println(moveSequence.get(x));
                    return result;
                }
                else{
                    for (int y = 0; y < tempVehicles.size(); y++) {
                        tempVehicles.get(y).tasks = bestVehicles.get(y).copyTasks();
                        tempVehicles.get(y).route = bestVehicles.get(y).copyRoute();
                    }
                }

            }

        }


        Object[] best = new Object[2];
        best[0] = bestVehicles;
        best[1] = bestFitness;
        return best;
    }

    //Calculates the cumulative time used by all vehicles in the solution. Used for debugging.
    public int calculateSum(ArrayList<Vehicle> vehicles){
        int sum = 0;
        for (int i = 0; i < vehicles.size(); i++) {
            for (int x = 0; x<vehicles.get(i).tasks.size(); x++){
                sum += vehicles.get(i).tasks.get(x).identifier;
            }
        }
        return sum;
    }

}