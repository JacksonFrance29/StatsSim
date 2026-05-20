package statssim;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * CoverageAnalysis - runs repeated simulations for each distribution
 * across multiple sample sizes and computes:
 *   1) Simulated vs theoretical mean and variance
 *   2) 95% CI coverage rates
 *
 * Outputs a CSV file: coverage_results.csv
 */
public class CoverageAnalysis {

    // Number of times to repeat each simulation to estimate coverage
    private static final int REPETITIONS = 1000;

    // Sample sizes to test
    private static final int[] SAMPLE_SIZES = {30, 100, 500, 1000};

    public static void main(String[] args) throws IOException {

        StringBuilder sb = new StringBuilder();

        // ---- Header ----
        sb.append("distribution,sample_size,theoretical_mean,simulated_mean,"
                + "theoretical_variance,simulated_variance,ci_coverage_rate\n");

        // ---- Normal(mean=0, stdDev=1) ----
        // Theoretical: mean=0, variance=1
        double normalMean = 0.0;
        double normalStdDev = 1.0;
        double normalTheoMean = normalMean;
        double normalTheoVar  = normalStdDev * normalStdDev;

        for (int n : SAMPLE_SIZES) {
            double[] agg = runCoverage(new NormalDistribution(normalMean, normalStdDev, n),
                                       normalTheoMean, REPETITIONS);
            sb.append(String.format("Normal(0,1),%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",
                    n, normalTheoMean, agg[0], normalTheoVar, agg[1], agg[2]));
        }

        // ---- Uniform(min=0, max=10) ----
        // Theoretical: mean=(a+b)/2=5, variance=(b-a)^2/12
        double uMin = 0.0, uMax = 10.0;
        double uTheoMean = (uMin + uMax) / 2.0;
        double uTheoVar  = Math.pow(uMax - uMin, 2) / 12.0;

        for (int n : SAMPLE_SIZES) {
            double[] agg = runCoverage(new UniformDistribution(uMin, uMax, n),
                                       uTheoMean, REPETITIONS);
            sb.append(String.format("Uniform(0,10),%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",
                    n, uTheoMean, agg[0], uTheoVar, agg[1], agg[2]));
        }

        // ---- Binomial(n=20, p=0.5) ----
        // Theoretical: mean=np=10, variance=np(1-p)=5
        int bN = 20; double bP = 0.5;
        double bTheoMean = bN * bP;
        double bTheoVar  = bN * bP * (1 - bP);

        for (int n : SAMPLE_SIZES) {
            double[] agg = runCoverage(new BinomialDistribution(bN, bP, n),
                                       bTheoMean, REPETITIONS);
            sb.append(String.format("Binomial(20,0.5),%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",
                    n, bTheoMean, agg[0], bTheoVar, agg[1], agg[2]));
        }

        // ---- Exponential(lambda=1) ----
        // Theoretical: mean=1/lambda=1, variance=1/lambda^2=1
        double lambda = 1.0;
        double eTheoMean = 1.0 / lambda;
        double eTheoVar  = 1.0 / (lambda * lambda);

        for (int n : SAMPLE_SIZES) {
            double[] agg = runCoverage(new ExponentialDistribution(lambda, n),
                                       eTheoMean, REPETITIONS);
            sb.append(String.format("Exponential(1),%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",
                    n, eTheoMean, agg[0], eTheoVar, agg[1], agg[2]));
        }

        // ---- Geometric(p=0.3) ----
        // Theoretical: mean=1/p, variance=(1-p)/p^2
        double gP = 0.3;
        double gTheoMean = 1.0 / gP;
        double gTheoVar  = (1 - gP) / (gP * gP);

        for (int n : SAMPLE_SIZES) {
            double[] agg = runCoverage(new GeometricDistribution(gP, n),
                                       gTheoMean, REPETITIONS);
            sb.append(String.format("Geometric(0.3),%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",
                    n, gTheoMean, agg[0], gTheoVar, agg[1], agg[2]));
        }

        // ---- Write CSV ----
        try (PrintWriter pw = new PrintWriter(new FileWriter("coverage_results.csv"))) {
            pw.print(sb);
        }

        System.out.println("Done. Results written to coverage_results.csv");
    }

    /**
     * Runs the given distribution REPETITIONS times.
     * Returns double[3]:
     *   [0] = average simulated mean across repetitions
     *   [1] = average simulated variance across repetitions
     *   [2] = CI coverage rate (proportion of CIs containing trueMean)
     */
    private static double[] runCoverage(Distribution dist, double trueMean, int reps) {
        double sumMean = 0;
        double sumVar  = 0;
        int covered    = 0;

        for (int r = 0; r < reps; r++) {
            SimulationResult result = dist.runSimulation();
            sumMean += result.getMean();
            sumVar  += result.getVariance();

            double[] ci = result.getConfidenceInterval();
            if (trueMean >= ci[0] && trueMean <= ci[1]) {
                covered++;
            }
        }

        return new double[]{
            sumMean / reps,
            sumVar  / reps,
            (double) covered / reps
        };
    }
}
