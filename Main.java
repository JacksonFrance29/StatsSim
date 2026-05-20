
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The user is prompted to choose a distribution and set parameters
 * The simulation runs, prints the results, and exports to CSV.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================================");
        // System.out.println("         StatsSim - Statistical Simulator    ");
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

    // Prints the menu, reads the user's choice, and constructs the matching Distribution.
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
        
        // Based on the user's choice, prompt for the appropriate parameters and create the distribution.
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

    // Asks for the number of simulation trials and makes sure that trials > 0.
    private static int promptTrials() {
        System.out.print("Number of simulation trials: ");
        int n = readInt();
        if (n <= 0) throw new IllegalArgumentException("Trials must be positive.");
        return n;
    }

    // Reads an int and makes sure it is positive.
    private static int promptPositiveInt(String prompt) {
        System.out.print(prompt);
        int val = readInt();
        if (val <= 0) throw new IllegalArgumentException("Value must be positive.");
        return val;
    }

    // Reads any double value
    private static double promptDouble(String prompt) {
        System.out.print(prompt);
        return readDouble();
    }

    // Reads a probability and requires it to be in the range (0, 1].
    private static double promptProbability(String prompt) {
        System.out.print(prompt);
        double val = readDouble();
        if (val <= 0 || val > 1) {
            throw new IllegalArgumentException("Probability must be between 0 (exclusive) and 1.");
        }
        return val;
    }

    // Reads a filename, defaults to results.csv, and appends .csv if missing.
    private static String promptOutputFile() {
        System.out.print("\nEnter output CSV filename (e.g. results.csv): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "results.csv";
        if (!name.endsWith(".csv")) name += ".csv";
        return name;
    }

    // Reads an int from the Scanner and also rethrows as IllegalArgumentException if there is a bad input.
    private static int readInt() {
        try {
            int val = scanner.nextInt();
            scanner.nextLine(); // consumes the newline created from previous line
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Expected an integer.");
        }
    }

    // Reads a double from the Scanner making sure it is a valid number and rethrows as IllegalArgumentException on bad input.
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
