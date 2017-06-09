package evolutionaryAlgorithm;

/**
 * Created by Magnu on 24.04.2017.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;



//A Input reader class, takes in the numeric values following the files made.
public class InputReader {

    int numberOfVehiclesLane;
    int numberOfVehiclesSidewalk;
    int[][] numberOfPlowJobsLane;
    int[][] numberOfPlowJobsSidewalk;
    int[][] numberofSidewalksOnArc;
    int[][] numberOfLanesOnArc;
    int[][] plowingtimeLane;
    int[][] plowingtimeSidewalk;
    int[][] deadheadingtimeSidewalk;
    int[][] deadheadingtimeLane;

    public InputReader(String testdata) {


        try {
            File file = new File(testdata);
            FileReader reader = new FileReader(file);
            BufferedReader fr = new BufferedReader(reader);


            String line = fr.readLine();

            String[] list1 = line.split(":");
            int antallNoder = Integer.parseInt(list1[1].trim());

            int[][] tempArray1 = new int[antallNoder][antallNoder];
            int[][] tempArray2 = new int[antallNoder][antallNoder];
            int[][] tempArray3 = new int[antallNoder][antallNoder];
            int[][] tempArray4 = new int[antallNoder][antallNoder];
            int[][] tempArray5 = new int[antallNoder][antallNoder];
            int[][] tempArray6 = new int[antallNoder][antallNoder];
            int[][] tempArray7 = new int[antallNoder][antallNoder];
            int[][] tempArray8 = new int[antallNoder][antallNoder];
            numberOfPlowJobsLane = tempArray1;
            numberOfPlowJobsSidewalk = tempArray2;
            numberofSidewalksOnArc = tempArray3;
            numberOfLanesOnArc = tempArray4;
            plowingtimeLane = tempArray5;
            plowingtimeSidewalk = tempArray6;
            deadheadingtimeSidewalk = tempArray7;
            deadheadingtimeLane = tempArray8;


            fr.readLine();
            line = fr.readLine();
            list1 = line.split(":");
            numberOfVehiclesLane = Integer.parseInt(list1[1].trim());
            // System.out.println(numberOfVehiclesLane);


            line = fr.readLine();
            list1 = line.split(":");
            numberOfVehiclesSidewalk = Integer.parseInt(list1[1].trim());
            //System.out.println(numberOfVehiclesSidewalk);


            fr.readLine();
            line = fr.readLine();
            list1 = line.split(":");
            line = fr.readLine();
            list1 = line.split(":");

            fr.readLine();
            fr.readLine();
            line = fr.readLine();
            list1 = line.split(":");


            fr.readLine();
            fr.readLine();
            for (int i = 0; i < antallNoder; i++) {
                line = fr.readLine();
                line = line.trim();
                list1 = line.split("\\t+");
                for (int j = 0; j < antallNoder; j++) {
                    numberOfLanesOnArc[i][j] = Integer.parseInt(list1[j].trim());
                    numberOfPlowJobsLane[i][j] = Integer.parseInt(list1[j].trim());
                }
            }


            fr.readLine();
            fr.readLine();
            fr.readLine();
            for (int i = 0; i < antallNoder; i++) {
                line = fr.readLine();
                //System.out.println(line);
                line = line.trim();
                //System.out.println(line);
                list1 = line.split("\\t+");
                for (int j = 0; j < antallNoder; j++) {
                    plowingtimeLane[i][j] = Integer.parseInt(list1[j].trim());
                }
            }

            fr.readLine();
            fr.readLine();
            fr.readLine();
            for (int i = 0; i < antallNoder; i++) {
                line = fr.readLine();
                //System.out.println(line);
                line = line.trim();
                //System.out.println(line);
                list1 = line.split("\\t+");
                for (int j = 0; j < antallNoder; j++) {
                    numberOfPlowJobsSidewalk[i][j] = Integer.parseInt(list1[j].trim());
                    numberofSidewalksOnArc[i][j] = Integer.parseInt(list1[j].trim());
                }
            }

            fr.readLine();
            fr.readLine();
            fr.readLine();
            for (int i = 0; i < antallNoder; i++) {
                line = fr.readLine();
                //System.out.println(line);
                line = line.trim();
                //System.out.println(line);
                list1 = line.split("\\t+");
                for (int j = 0; j < antallNoder; j++) {
                    plowingtimeSidewalk[i][j] = Integer.parseInt(list1[j].trim());
                }
            }

            fr.readLine();
            fr.readLine();
            fr.readLine();
            for (int i = 0; i < antallNoder; i++) {
                line = fr.readLine();
                //System.out.println(line);
                line = line.trim();
                //System.out.println(line);
                list1 = line.split("\\t+");
                for (int j = 0; j < antallNoder; j++) {
                    deadheadingtimeLane[i][j] = Integer.parseInt(list1[j].trim());
                }
            }

            fr.readLine();
            fr.readLine();
            fr.readLine();
            for (int i = 0; i < antallNoder; i++) {
                line = fr.readLine();
                //System.out.println(line);
                line = line.trim();
                //System.out.println(line);
                list1 = line.split("\\t+");
                for (int j = 0; j < antallNoder; j++) {
                    deadheadingtimeSidewalk[i][j] = Integer.parseInt(list1[j].trim());
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("Fetched input");
    }
}
