package karp;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Locale;

/**
 * @author Francisco Baeta
 * @version v0.1 - 26/12/2017
 */
public class Main {

	public static void main(String[] args) {
		
		
		// User input
		System.out.println("\n------------------------------Held Karp------------------------------\n");
		Scanner sc = new Scanner(System.in);
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
		Mapa map = new Mapa(cities, maxDistances, sameCost);
		HeldKarp calc = new HeldKarp(map.setMatrix(), startCity);
		
		// Time calculation
		long start = System.nanoTime();
		List<Integer> result = calc.calculateHeldKarp();
		long end = System.nanoTime() - start;
		
		// List results
		System.out.println("\nResults:");
		System.out.println("Time taken " + printTime(end));
		System.out.println("Shortest Path: " + calc.getOpt() + 
				"\nShortest route: " + result.toString());

		// List statistics
		System.out.println("\nStatistics:");
		System.out.println("Number of dictionary entries: " + calc.getDictionaryEntries());
		System.out.println("Number of possible paths: " + Combinations.getFactorial(map.getCities() - 1));
		System.out.println("\n---------------------------------------------------------------------\n");

		// Clean up and exit
		sc.close();
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

		while (true) {
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

}
