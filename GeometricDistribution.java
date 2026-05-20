package statssim;

/**
 * Geometric distribution: counts trials until the first success.
 */
public class GeometricDistribution extends Distribution {

    private double p;

    public GeometricDistribution(double p, int numTrials) {
        super("Geometric(p=" + p + ")", numTrials);
        if (p <= 0 || p > 1) {
            throw new IllegalArgumentException("p must be between 0 (exclusive) and 1.");
        }
        this.p = p;
    }

    @Override
    public double sample() {
        int trials = 0;
        do {
            trials++;
        } while (Math.random() >= p);
        return trials;
    }
}
