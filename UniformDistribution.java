package statssim;

/**
 * Continuous Uniform distribution over [min, max].
 */
public class UniformDistribution extends Distribution {

    private double min;
    private double max;

    public UniformDistribution(double min, double max, int numTrials) {
        super("Uniform(min=" + min + ", max=" + max + ")", numTrials);
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max.");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public double sample() {
        return min + (max - min) * Math.random();
    }
}
