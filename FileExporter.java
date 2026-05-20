package statssim;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Exports simulation results to a CSV file.
 * Format: summary block at top, then one row per trial.
 */
public class FileExporter {

    private String filePath;

    public FileExporter(String filePath) {
        this.filePath = filePath;
    }

    public void export(SimulationResult result) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            double[] ci = result.getConfidenceInterval();

            // Summary block
            writer.println("# StatsSim - Simulation Results");
            writer.println("# Distribution," + result.getDistributionName());
            writer.println("# Trials," + result.getNumTrials());
            writer.printf("# Mean,%.4f%n", result.getMean());
            writer.printf("# Variance,%.4f%n", result.getVariance());
            writer.printf("# Std Dev,%.4f%n", result.getStdDev());
            writer.printf("# Min,%.4f%n", result.getMin());
            writer.printf("# Max,%.4f%n", result.getMax());
            writer.printf("# 95%% CI Lower,%.4f%n", ci[0]);
            writer.printf("# 95%% CI Upper,%.4f%n", ci[1]);
            writer.println("#");

            // Data rows
            writer.println("trial,value");
            double[] data = result.getData();
            for (int i = 0; i < data.length; i++) {
                writer.printf("%d,%.6f%n", i + 1, data[i]);
            }
        }
    }

    public String getFilePath() { return filePath; }
}
