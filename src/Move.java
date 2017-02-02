/**
 * Created by Magnu on 18.09.2016.
 */
public class Move {
    public int queen1;
    public int queen2;

    public Move(int i, int j){
        this.queen1 = i;
        this.queen2 = j;
    }

    public int getQueen1() {
        return queen1;
    }

    public int getQueen2() {
        return queen2;
    }
}

