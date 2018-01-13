package karp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Locale;

/**
 * @author Francisco Baeta
 * @version v0.1 - 26/12/2017
 */
public class Main {
	
	// The alteration of these variables doesn't really hold any performance impact
	private static final boolean BENCH_SAME_COST = true;
	private static final int BENCH_MAX_DISTANCES = 10000;
	private static final int BENCH_STARTING_CITY = 0;
	private static final int BENCH_RUNS = 3;
	private static final int WARMUP_CITY_NUMBER = 15;
	
	// Actually anything above 24 cities takes an inconvenient amount of memory
	public static final int MAX_CITIES = 32;
	
	// Global scope variables
	private static long start;
	private static long end;
	private static Mapa map;
	private static HeldKarp calc;
	private static boolean running = true;
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		int input;
		System.out.println("\n------------------------------Held Karp------------------------------\n");
		
		while(running) {
			printMenu();
			input = enterInteger(sc);
			
			switch(input) {
			case 0:
				running = false;
				break;
			case 1:
				standardRun();
				break;
			case 2:
				benchmark();
				break;
			case 3:
				runForFun();
				break;
			default:
				System.out.println("Enter a valid option.\n");
				break;
			}
			
			System.out.println("\n---------------------------------------------------------------------\n");
		}
		
		// Cleanup and exit
		sc.close();
		
	}
	
	public static void benchmark() {
		// User input
		System.out.println("Running benchmark from ? to ? cities:");
		System.out.print("Inicial city: ");
		int lowerLimit = enterInteger(sc);
		System.out.print("Last city: ");
		int upperLimit = enterInteger(sc);
		upperLimit = constrain(0, 32, upperLimit);
		lowerLimit = constrain(0, upperLimit, lowerLimit);
		
		System.out.println("Presenting best results from " + BENCH_RUNS + " runs.");
		
		// Set up the benchmark
		List<Long> ends;
		long benchStart = 0;
		for(int i = lowerLimit - 1; i <= upperLimit; i++) {
			
			// We have to warm up to prevent first result being greater
			if(i == lowerLimit - 1)  {
				System.out.println("Warming up the processor...");
				map = new Mapa(WARMUP_CITY_NUMBER, BENCH_MAX_DISTANCES, BENCH_SAME_COST);
			} else {
				map = new Mapa(i, BENCH_MAX_DISTANCES, BENCH_SAME_COST);
			}
			
			// Perform calculation over a number of times
			calc = new HeldKarp(map.setMatrix(), BENCH_STARTING_CITY);
			ends = new ArrayList<>();
			for(int j = 0; j < BENCH_RUNS; j++) {
				start = System.nanoTime();
				calc.calculateHeldKarp();
				end = System.nanoTime() - start;
				ends.add(end);
			}
			
			// Choose the maximum
			if(i == lowerLimit - 1) {
				benchStart = System.nanoTime();
				System.out.println("Starting benchmark...");
			} else {
				System.out.println("Best time for " + i + " cities was " +
					printTime(Collections.min(ends)));
			}
			
		}
		// Benchmark info
		long benchEnd = System.nanoTime() - benchStart;
		System.out.println("Benchmark ended in: " + printTime(benchEnd));
	}
	
	public static void standardRun() {
		// User input
		System.out.println("Enter the number of cities: ");
		int cities = enterInteger(sc);
		System.out.println("Enter the starting city (from 0 to " + (cities - 1) + "): ");
		int startCity = enterInteger(sc);
		System.out.println("Enter the maximun distance between two cities: ");
		int maxDistances = enterInteger(sc);
		System.out.println("Is the path cost between two cities the same in both ways? (y/n): ");
		String str = sc.nextLine();
		boolean sameCost = "yes!".contains(str.toLowerCase());
		System.out.println("Calculating shortest path...");
		
		
		// Prepare maps and objects for the calculation
		map = new Mapa(cities, maxDistances, sameCost);
		calc = new HeldKarp(map.setMatrix(), startCity);
		
		// Time calculation
		start = System.nanoTime();
		List<Integer> result = calc.calculateHeldKarp();
		end = System.nanoTime() - start;
		
		// List results
		System.out.println("\nResults:");
		System.out.println("Time taken " + printTime(end));
		System.out.println("Shortest Path: " + calc.getOpt() + 
				"\nShortest route: " + result.toString());

		// List statistics
		System.out.println("\nStatistics:");
		System.out.println("Number of map entries: " + calc.getDictionaryEntries());
		System.out.println("Number of possible paths: " + Combinations.getFactorial(map.getCities() - 1));
	}
	
	public static void runForFun() {
		// User input
		System.out.println("Enter number of cities: ");
		int cities = enterInteger(sc);
		System.out.println("Enter number of iterations: ");
		int iterations = enterInteger(sc);
		System.out.println("Computing " + iterations + " iterations of " + cities + " cities.");
		
		// Set test bed
		map = new Mapa(cities, BENCH_MAX_DISTANCES, BENCH_SAME_COST);
		calc = new HeldKarp(map.setMatrix(), BENCH_STARTING_CITY);
		
		// Start testing
		start = System.nanoTime();
		long temp = System.nanoTime();
		for(int i = 1; i < iterations + 1; i++) {
			calc.calculateHeldKarp();
			if(System.nanoTime() - temp > 1000000000) {
				System.out.println(i + "/" + iterations + " - "+ String.
						format(Locale.ROOT, "%.2f", (double)i / iterations * 100.0) + "%");
				temp = System.nanoTime();
			}
		}
		
		// End test
		end = System.nanoTime() - start;
		System.out.println("Test took: " + printTime(end));
	}
	
	public static void printMenu() {
		System.out.println("1. Standard");
		System.out.println("2. Benchmark");
		System.out.println("3. Run for fun");
		System.out.println("0. Exit program\n");
		System.out.print("-> ");
	}
	
	public static String printTime(long end) {
		String msg = "(in ";
		if(end < 1000) {
			msg += "ns): " + String.format(Locale.ROOT, "%.3f", (float)(end));
		} else if(end >= 1000 && end < 1000000) {
			msg += "us): " + String.format(Locale.ROOT, "%.3f", ((float)end / 1000.0));
		} else if(end >= 1000000 && end < 1000000000) {
			msg += "ms): " + String.format(Locale.ROOT, "%.3f", ((float)end / 1000000.0));
		} else if(end >= 1000000000) {
			msg += "s): " + String.format(Locale.ROOT, "%.3f", ((float)end / 1000000000.0));
		}
		return msg;
	}
	
	public static int enterInteger(Scanner sc) {
		int result;

		while(true) {
			try {
				result = sc.nextInt();
				break;
			} catch (InputMismatchException e) {
				System.out.println("Please enter an integer: ");
			}
			sc.nextLine();
		}
		sc.nextLine();
		
		return result;
	}
	
	public static int constrain(int min, int max, int number) {
		if(number < min) {
			return min;
		} else if (number > max) {
			return max;
		} else {
			return number;
		}
	}

}