package statssim;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Entry point for StatsSim.
 * Prompts the user to choose a distribution, set parameters,
 * run the simulation, print results, and export to CSV.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("         StatsSim - Statistical Simulator    ");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            try {
                Distribution dist = selectDistribution();
                SimulationResult result = dist.runSimulation();

                System.out.println("\n--- Results ---");
                System.out.println(result);

                String filePath = promptOutputFile();
                FileExporter exporter = new FileExporter(filePath);
                exporter.export(result);
                System.out.println("\nResults exported to: " + filePath);

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }

            System.out.print("\nRun another simulation? (yes/no): ");
            String again = scanner.nextLine().trim().toLowerCase();
            running = again.equals("yes") || again.equals("y");
        }

        System.out.println("Goodbye.");
        scanner.close();
    }

    private static Distribution selectDistribution() {
        System.out.println("\nSelect a distribution:");
        System.out.println("  1. Normal");
        System.out.println("  2. Uniform");
        System.out.println("  3. Binomial");
        System.out.println("  4. Exponential");
        System.out.println("  5. Geometric");
        System.out.print("Choice: ");

        int choice = readInt();
        int numTrials = promptTrials();

        switch (choice) {
            case 1:
                double mean = promptDouble("Mean: ");
                double stdDev = promptDouble("Standard deviation: ");
                return new NormalDistribution(mean, stdDev, numTrials);

            case 2:
                double min = promptDouble("Min: ");
                double max = promptDouble("Max: ");
                return new UniformDistribution(min, max, numTrials);

            case 3:
                int n = promptPositiveInt("Number of Bernoulli trials per sample (n): ");
                double p = promptProbability("Success probability (p): ");
                return new BinomialDistribution(n, p, numTrials);

            case 4:
                double lambda = promptDouble("Rate (lambda): ");
                return new ExponentialDistribution(lambda, numTrials);

            case 5:
                double pGeo = promptProbability("Success probability (p): ");
                return new GeometricDistribution(pGeo, numTrials);

            default:
                throw new IllegalArgumentException("Choice must be 1-5.");
        }
    }

    private static int promptTrials() {
        System.out.print("Number of simulation trials: ");
        int n = readInt();
        if (n <= 0) throw new IllegalArgumentException("Trials must be positive.");
        return n;
    }

    private static int promptPositiveInt(String prompt) {
        System.out.print(prompt);
        int val = readInt();
        if (val <= 0) throw new IllegalArgumentException("Value must be positive.");
        return val;
    }

    private static double promptDouble(String prompt) {
        System.out.print(prompt);
        return readDouble();
    }

    private static double promptProbability(String prompt) {
        System.out.print(prompt);
        double val = readDouble();
        if (val <= 0 || val > 1) {
            throw new IllegalArgumentException("Probability must be between 0 (exclusive) and 1.");
        }
        return val;
    }

    private static String promptOutputFile() {
        System.out.print("\nEnter output CSV filename (e.g. results.csv): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "results.csv";
        if (!name.endsWith(".csv")) name += ".csv";
        return name;
    }

    private static int readInt() {
        try {
            int val = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Expected an integer.");
        }
    }

    private static double readDouble() {
        try {
            double val = scanner.nextDouble();
            scanner.nextLine();
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Expected a number.");
        }
    }
}
