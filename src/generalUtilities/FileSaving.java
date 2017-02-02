package generalUtilities;

import graph.FloydWarshall;
import graph.Graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileSaving {
	
	static Gson gson;
	static GsonBuilder gsonBuilder = new GsonBuilder();
	static TimeZone timeZone = TimeZone.getTimeZone("UTC");
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss'Z'");
	
	public static void writeFloydWarshall(){
//		String contentToBeWritten = getGson().toJson(new FloydWarshall());
		String contentToBeWritten = new FloydWarshall().allPairsToString();
		try {
			PrintWriter writer = new PrintWriter(buildFileName("FloydWarshall"), "UTF-8");
			writer.println(contentToBeWritten);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeGraph(){
		String contentToBeWritten = getGson().toJson(new Graph());
		try {
			PrintWriter writer = new PrintWriter(buildFileName("Graph"), "UTF-8");
			writer.println(contentToBeWritten);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void appendGenerationStats(String generationStats, long startTime){
		dateFormat.setTimeZone(timeZone);
		String path = "./results/runs/"
				+ Graph.problemName + "/"
				+ dateFormat.format(new Date(startTime)) + "/";
		
		new File(path).mkdirs();
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path + "GenerationalStats.txt", true)));
			writer.println(generationStats);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeEntireRun(String runDataFromEA, double bestFitness, long startTime){
		dateFormat.setTimeZone(timeZone);
		String path = "./results/runs/"
				+ Graph.problemName + "/"
				+ dateFormat.format(new Date(startTime)) + "/";
		
		new File(path).mkdirs();
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path + "Run Stats.txt")) );
			writer.println(runDataFromEA);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String buildFileName(String customPartOfName){
		dateFormat.setTimeZone(timeZone);
		
		String path = "./results/runs/" + Graph.problemName + "/";
		new File(path).mkdirs();
		String filename = "";
		filename += path +
				dateFormat.format(new Date())
				+ "_" + customPartOfName;
		
		
		return filename;
	}
	
	public static Gson getGson(){
		if(gson != null){
			return gson;
		}else{
			gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
			gson = gsonBuilder.create();
			return gson;
		}
	}
}
