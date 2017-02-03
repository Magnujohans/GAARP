package InitialSolution;
import java.util.Comparator;

/**
 * Created by Magnu on 10.12.2016.
 */
    //This is just a comparator, so we can sort the vehicles based on ID. Used to sort the vehicles, so the
    //Plowing trucks are simulated before the smaller vehicles.
public class TypeComparator implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle a, Vehicle b) {
        return b.id - a.id;
    }
}
