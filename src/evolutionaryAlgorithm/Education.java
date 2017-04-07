package evolutionaryAlgorithm;

import InitialSolution.Vehicle;
import InitialSolution.Arc;
import javafx.beans.binding.ObjectExpression;
import javafx.scene.shape.ArcTo;

import java.util.*;

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

    public ArrayList<Genotype> educateChildren(ArrayList<Genotype> children, int iterations, double factor){
        ArrayList<Genotype> educatedChildren = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            ArrayList<Vehicle> tempChild = fenotype.getFenotype(children.get(i));
            Object[] tempResult = educate(tempChild, iterations, factor);
            ArrayList<Vehicle> tempResultFenotype = (ArrayList<Vehicle>) tempResult[0];
            Genotype tempResultGenotype = fenotype.createGenotype(tempResultFenotype, (Integer) tempResult[1]);
            educatedChildren.add(tempResultGenotype);
        }
        return educatedChildren;
    }

    public ArrayList<Genotype> educateChildren2(ArrayList<Arc> arcs, ArrayList<Genotype> children, int iterations, double factor){
        ArrayList<Genotype> educatedChildren = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            ArrayList<Vehicle> tempChild = fenotype.getFenotype(children.get(i));
            Object[] tempResult;
            if(rng.nextDouble() > 0.5){
                tempResult = educateOffspring(arcs, tempChild);
            }
            else{
                tempResult = educate(tempChild, iterations, factor);
            }
            ArrayList<Vehicle> tempResultFenotype = (ArrayList<Vehicle>) tempResult[0];
            //System.out.println("Jess");
            Genotype tempResultGenotype = fenotype.createGenotype(tempResultFenotype, (Integer) tempResult[1]);
            educatedChildren.add(tempResultGenotype);
        }
        return educatedChildren;
    }
/*
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
    }*/


    public Object[] educate(ArrayList<Vehicle> vehicles, int generation, double factor){
        int counter = 0;
        ArrayList<Vehicle> bestVehicles = vehicles;
        int bestFitness = fenotype.calculateFitness(bestVehicles);

        Object[] result;
        while(counter < 10){
            double educate = rng.nextDouble();
            if (educate > 0.5){
                //break;
            }

            ArrayList<Vehicle> tempVehicles = new ArrayList<>();
            for (int i = 0; i < bestVehicles.size(); i++) {
                tempVehicles.add(bestVehicles.get(i).copyVehicle());
            }

            int[] makeSpanParameters = fenotype.calculateFitnessParameters(bestVehicles);
            int random = rng.nextInt(4);
            if(makeSpanParameters[1] > 0){
                Vehicle tempOne = tempVehicles.get(makeSpanParameters[2]);
                int two = rng.nextInt(fenotype.plowtrucks);
                Vehicle tempTwo = tempVehicles.get(two);

                while (tempOne.tasks.size() == 0){
                    tempOne = tempVehicles.get(rng.nextInt(fenotype.plowtrucks));
                }
                if (tempTwo.tasks.size() ==0){
                    Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, 0 );
                }
                if(random == 0){
                    result = Swap(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
                }
                else if (random == 1){
                    if (tempOne.tasks.size() < 2 || tempTwo.tasks.size() < 2) {
                        continue;
                    }
                    result = DoubleSwap(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
                }
                else if (random == 2){
                    result = Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
                }
                else{
                    if (tempOne.tasks.size() < 2 || tempTwo.tasks.size() < 2) {
                        continue;
                    }
                    result = DoubleInsert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()));
                }
            }
            else{
                if(rng.nextDouble() > 0.5){
                    Vehicle tempOne = tempVehicles.get(makeSpanParameters[2]);
                    int two = fenotype.plowtrucks + rng.nextInt(fenotype.smallervehicles);
                    Vehicle tempTwo = tempVehicles.get(two);
                    while (tempOne.tasks.size() == 0){
                        tempOne = tempVehicles.get(rng.nextInt(fenotype.plowtrucks));
                    }
                    if (tempTwo.tasks.size() ==0){
                        Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, 0 );
                    }

                    if(random == 0){
                        result = Swap(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
                    }
                    else if (random == 1){
                        if (tempOne.tasks.size() < 2 || tempTwo.tasks.size() < 2) {
                            continue;
                        }
                        result = DoubleSwap(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
                    }
                    else if (random == 2){
                        result = Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
                    }
                    else{
                        if (tempOne.tasks.size() < 2 || tempTwo.tasks.size() < 2) {
                            continue;
                        }
                        result = DoubleInsert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()));
                    }
                }
                else{
                    int one = rng.nextInt(fenotype.plowtrucks);
                    int two = rng.nextInt(fenotype.plowtrucks);
                    Vehicle tempOne = tempVehicles.get(one);
                    Vehicle tempTwo = tempVehicles.get(two);
                    while (tempOne.tasks.size() == 0){
                        tempOne = tempVehicles.get(rng.nextInt(fenotype.plowtrucks));
                    }
                    if (tempTwo.tasks.size() ==0){
                        Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, 0 );
                    }


                    if(random == 0){
                        result = Swap(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
                    }
                    else if (random == 1){
                        if (tempOne.tasks.size() < 2 || tempTwo.tasks.size() < 2) {
                            continue;
                        }
                        result = DoubleSwap(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
                    }
                    else if (random == 2){
                        result = Insert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
                    }
                    else{
                        if (tempOne.tasks.size() < 2 || tempTwo.tasks.size() < 2) {
                            continue;
                        }
                        result = DoubleInsert(tempVehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()));
                    }
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
        //if(rng.nextDouble() < generation*factor){
        //    return mutate(bestVehicles);
        //}
        Object[] best = new Object[2];
        best[0] = bestVehicles;
        best[1] = bestFitness;
        return best;
    }

    public Object[] Insert(ArrayList<Vehicle> vehicles, Vehicle from, Arc arc, Vehicle to, int index){
        from.tasks.remove(arc);
        to.tasks.add(index, arc);

        from.route = fenotype.getTourFromTasks(from.tasks, from.id);
        to.route = fenotype.getTourFromTasks(to.tasks, to.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;

    }

    public Object[] DoubleInsert(ArrayList<Vehicle> vehicles, Vehicle from, Arc arcOne, Vehicle to, int index1){
        Insert(vehicles, from, arcOne, to, index1);
        Arc arcTwo = from.tasks.get(rng.nextInt(from.tasks.size()));
        int index2 = rng.nextInt(to.tasks.size());

        return Insert(vehicles, from, arcTwo, to, index2);
    }

    public Object[] DoubleSwap(ArrayList<Vehicle> vehicles, Vehicle vehicleOne, Arc arcOneOne, Vehicle vehicleTwo, Arc arcTwoOne){
        Swap(vehicles, vehicleOne, arcOneOne, vehicleTwo, arcTwoOne);
        Arc arcOneTwo = vehicleOne.tasks.get(rng.nextInt(vehicleOne.tasks.size()));
        Arc arcTwoTwo = vehicleTwo.tasks.get(rng.nextInt(vehicleTwo.tasks.size()));

        return Swap(vehicles, vehicleOne, arcOneTwo, vehicleTwo, arcTwoTwo);
    }

    public Object[] Swap(ArrayList<Vehicle> vehicles, Vehicle vehicleOne, Arc arcOne, Vehicle vehicleTwo, Arc arcTwo){
        int index1 = vehicleOne.tasks.indexOf(arcOne);
        int index2 = vehicleTwo.tasks.indexOf(arcTwo);

        vehicleTwo.tasks.remove(arcTwo);
        vehicleTwo.tasks.add(index2, arcOne);
        vehicleOne.tasks.remove(arcOne);
        vehicleOne.tasks.add(index1, arcTwo);

        vehicleOne.route = fenotype.getTourFromTasks(vehicleOne.tasks, vehicleTwo.id);
        vehicleTwo.route = fenotype.getTourFromTasks(vehicleTwo.tasks, vehicleTwo.id);

        int fitness = fenotype.calculateFitness(vehicles);
        Object[] returnlist = new Object[2];

        returnlist[0] = vehicles;
        returnlist[1] = fitness;

        return returnlist;
    }

    public void mutate(ArrayList<Genotype> adults, int index){
        Object[] result;
        ArrayList<Vehicle> vehicles = fenotype.getFenotype(adults.get(index));
        for (int i = 0; i < 20; i++) {
            int random = rng.nextInt(2);
            double vehicleType = rng.nextDouble();

            Vehicle tempOne;
            Vehicle tempTwo;
            if (vehicleType > 0.5){
                int one = rng.nextInt(fenotype.plowtrucks);
                tempOne = vehicles.get(one);
                int two = rng.nextInt(fenotype.plowtrucks);
                tempTwo = vehicles.get(two);
            }

            else{
                int one = fenotype.plowtrucks + rng.nextInt(fenotype.smallervehicles);
                tempOne = vehicles.get(one);
                int two = fenotype.plowtrucks + rng.nextInt(fenotype.smallervehicles);
                tempTwo = vehicles.get(two);
            }

            if(random == 0){
                Swap(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
            }
            else if (random == 2){
                DoubleSwap(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
            }
            else if (random == 1){
                Insert(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
            }
            else{
                DoubleInsert(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()));
            }

        }
        adults.add(fenotype.InitialGenotype(vehicles));
    }

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

    public Vehicle getVehicleWithTask(ArrayList<Vehicle> vehicles, Arc task){
        for (Vehicle vehicle : vehicles) {
            if(vehicle.tasks.indexOf(task) > -1){
                return vehicle;
            }
        }
        return null;
    }

    public int getIndexofTask(Vehicle vehicle, Arc task){
        return vehicle.tasks.indexOf(task);
    }


    public ArrayList<Integer> getPermutation(int bound){
        ArrayList<Integer> permutation = new ArrayList<Integer>();
        for(int x = 0; x<bound; x++){
            permutation.add(x);
        }
        Collections.shuffle(permutation);
        return permutation;
    }

    public Object[] runMove(ArrayList<Vehicle> vehicles, int nr, Vehicle one, Vehicle two, int u, int v){
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

    public Object[] educateOffspring(ArrayList<Arc> arcs, ArrayList<Vehicle> initial){
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
                    result = runMove(tempVehicles, moveSequence.get(x), vehicleOne, vehicleTwo, u, v);
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

        Object[] best = new Object[2];
        best[0] = bestVehicles;
        best[1] = bestFitness;
        return best;
    }

    public Object[] mutate(ArrayList<Vehicle> vehicles){
        Object[] result;
        for (int i = 0; i < 20; i++) {
            int random = rng.nextInt(2);
            double vehicleType = rng.nextDouble();

            Vehicle tempOne;
            Vehicle tempTwo;
            if (vehicleType > 0.5){
                int one = rng.nextInt(fenotype.plowtrucks);
                tempOne = vehicles.get(one);
                int two = rng.nextInt(fenotype.plowtrucks);
                tempTwo = vehicles.get(two);
            }

            else{
                int one = fenotype.plowtrucks + rng.nextInt(fenotype.smallervehicles);
                tempOne = vehicles.get(one);
                int two = fenotype.plowtrucks + rng.nextInt(fenotype.smallervehicles);
                tempTwo = vehicles.get(two);
            }

            if(random == 0){
                Swap(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
            }
            else if (random == 2){
                DoubleSwap(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo,tempTwo.tasks.get(rng.nextInt(tempTwo.tasks.size())));
            }
            else if (random == 1){
                Insert(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()) );
            }
            else{
                DoubleInsert(vehicles,tempOne,tempOne.tasks.get(rng.nextInt(tempOne.tasks.size())),tempTwo, rng.nextInt(tempTwo.tasks.size()));
            }

        }
        Object[] returnList = new Object[2];
        returnList[0] = vehicles;
        returnList[1] = fenotype.getMakeSpan(vehicles);
        return returnList;
    }
}