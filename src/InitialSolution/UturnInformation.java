package InitialSolution;

/**
 * Created by Magnu on 01.05.2017.
 */
public class UturnInformation {
    public int[][] laneGraph;
    public int[][] lanePath;
    public int[][] sidewalkGraph;
    public int[][] sidewalkPath;
    public int[][] bestGraph;
    public int[][] bestPath;

    public UturnInformation(int[][] laneGraph, int[][] lanePath, int[][] sidewalkGraph, int[][] sidewalkPath, int[][] bestGraph, int[][] bestPath){
        this.laneGraph = laneGraph;
        this.lanePath = lanePath;
        this.sidewalkGraph = sidewalkGraph;
        this.sidewalkPath = sidewalkPath;
        this.bestGraph = bestGraph;
        this.bestPath = bestPath;
    }

}
