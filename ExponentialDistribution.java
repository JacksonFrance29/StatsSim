
// Exponential distribution using inverse transform sampling.
// Models the time between events in a Poisson process.
 
public class ExponentialDistribution extends Distribution {

    private double lambda;

    // Stores lambda; rejects non-positive values.
    public ExponentialDistribution(double lambda, int numTrials) {
        super("Exponential(lambda=" + lambda + ")", numTrials);
        if (lambda <= 0) {
            throw new IllegalArgumentException("Lambda must be positive.");
        }
        this.lambda = lambda;
    }

    // Uses inverse-CDF sampling.
    @Override
    public double sample() {
        return -Math.log(1.0 - Math.random()) / lambda;
    }
}
