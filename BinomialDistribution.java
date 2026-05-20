package statssim;

/**
 * Binomial distribution: counts successes in n independent Bernoulli trials.
 */
public class BinomialDistribution extends Distribution {

    private int n;
    private double p;

    public BinomialDistribution(int n, double p, int numTrials) {
        super("Binomial(n=" + n + ", p=" + p + ")", numTrials);
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive.");
        }
        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("p must be between 0 and 1.");
        }
        this.n = n;
        this.p = p;
    }

    @Override
    public double sample() {
        int successes = 0;
        for (int i = 0; i < n; i++) {
            if (Math.random() < p) {
                successes++;
            }
        }
        return successes;
    }
}
