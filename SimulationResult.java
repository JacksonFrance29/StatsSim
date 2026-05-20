package statssim;

// Holds trial data and computes summary statistics.
 
public class SimulationResult {

    private String distributionName;
    private double[] data;

    public SimulationResult(String distributionName, double[] data) {
        this.distributionName = distributionName;
        this.data = data;
    }

    public double getMean() {
        double sum = 0;
        for (double v : data) sum += v;
        return sum / data.length;
    }

    public double getVariance() {
        double mean = getMean();
        double sumSqDiff = 0;
        for (double v : data) {
            double diff = v - mean;
            sumSqDiff += diff * diff;
        }
        return sumSqDiff / data.length;
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    //Returns a 95% confidence interval for the mean using z = 1.96.
    // Returns double[] { lowerBound, upperBound }
     
    public double[] getConfidenceInterval() {
        double mean = getMean();
        double margin = 1.96 * (getStdDev() / Math.sqrt(data.length));
        return new double[]{mean - margin, mean + margin};
    }

    public double getMin() {
        double min = data[0];
        for (double v : data) if (v < min) min = v;
        return min;
    }

    public double getMax() {
        double max = data[0];
        for (double v : data) if (v > max) max = v;
        return max;
    }

    public String getDistributionName() { 
        return distributionName; 
    }
    public double[] getData() { 
        return data; 
    }
    public int getNumTrials() { 
        return data.length; 
    }

    @Override
    public String toString() {
        double[] ci = getConfidenceInterval();
        return String.format(
            "Distribution : %s%n" +
            "Trials       : %d%n" +
            "Mean         : %.4f%n" +
            "Variance     : %.4f%n" +
            "Std Dev      : %.4f%n" +
            "Min          : %.4f%n" +
            "Max          : %.4f%n" +
            "95%% CI      : [%.4f, %.4f]",
            distributionName, data.length,
            getMean(), getVariance(), getStdDev(),
            getMin(), getMax(),
            ci[0], ci[1]
        );
    }
}
