package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import core.HicFileProcessing;
import utils.WholeGenomeAnalysis;

public class TestCallLoopsHicFile{
	/**
	 * Main function to run all the process, can be run with gui or in command line.
	 * With command line with 1 or less than 5 parameter => run only the help
	 * With zero parameter only java -jar noname.jar  => gui
	 * With more than 5 paramter => command line mode
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		String output= "/home/plop/Bureau/DataSetImageHiC/GM12878/subsample/1bil";
		String input = "/home/plop/Bureau/DataSetImageHiC/GM12878/subsample/GM12878_1bil.hic";
		//String output= "/home/plop/Bureau/DataSetImageHiC/Hichip_H3k4me1";
		//String input= "/home/plop/Bureau/DataSetImageHiC/Hichip_H3k4me1/NT_H3K4me1_2Reps.cis18797450.allValidPairs.hic";
		HashMap<String,Integer> chrsize = readChrSizeFile("/home/plop/Documents/Genome/HumanGenomeHg19/hg19_withoutChr.sizes");
		///home/plop/Documents/Genome/HumanGenomeHg19/hg19_withoutChr.sizes
		String juiceBoxTools = "/home/plop/Tools/juicer_tools.1.8.9_jcuda.0.8.jar";
		int matrixSize = 2000;
		int resolution = 5000;
		int diagSize = 6;
		double gauss = 1;
		double min = 1.5;
		double max = 1.5;
		int nbZero = 6;
		int thresholdMax = 3250;
		String juiceBoXNormalisation = "KR";
		double saturatedPixel = 0.05;
		boolean isObserved = false;
		ArrayList<Integer> factor = new ArrayList<Integer>();
		factor.add(1);
		factor.add(2);
		factor.add(5);
		
		System.out.println("input "+input+"\n"
				+ "output "+output+"\n"
				+ "juiceBox "+juiceBoxTools+"\n"
				+ "norm "+ juiceBoXNormalisation+"\n"
				+ "gauss "+gauss+"\n"
				+ "min "+min+"\n"
				+ "max "+max+"\n"
				+ "matrix size "+matrixSize+"\n"
				+ "diag size "+diagSize+"\n"
				+ "resolution "+resolution+"\n"
				+ "saturated pixel "+saturatedPixel+"\n"
				+ "threshold "+thresholdMax+"\n"
				+ "isObserved "+isObserved+"\n");
			
			File file = new File(output);
			if (file.exists()==false){file.mkdir();}
			
///-d 4 -g 0.25 -t 20 -norm K
			WholeGenomeAnalysis wga = new WholeGenomeAnalysis(output, chrsize, gauss, min, max, resolution, saturatedPixel, thresholdMax, diagSize, matrixSize, nbZero,factor);
			HicFileProcessing hfp =  new HicFileProcessing(input, wga, chrsize, juiceBoxTools, juiceBoXNormalisation);
			if(isObserved) hfp.run(true);
			else		hfp.run(false);
				
			
			System.out.println("End");
		}
		
		/**
		 * 
		 * @param chrSizeFile
		 * @throws IOException
		 */
	private static HashMap<String, Integer> readChrSizeFile( String chrSizeFile) throws IOException{
		HashMap<String,Integer> chrSize =  new HashMap<String,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(chrSizeFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null){
			sb.append(line);
			String[] parts = line.split("\\t");				
			String chr = parts[0]; 
			int size = Integer.parseInt(parts[1]);
			
			chrSize.put(chr, size);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		return  chrSize;
	} 
}