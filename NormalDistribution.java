package statssim;


// Normal distribution using the Box-Muller transform
 
public class NormalDistribution extends Distribution {

    private double mean;
    private double stdDev;

    public NormalDistribution(double mean, double stdDev, int numTrials) {
        super("Normal(mean=" + mean + ", stdDev=" + stdDev + ")", numTrials);
        if (stdDev <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive.");
        }
        this.mean = mean;
        this.stdDev = stdDev;
    }

    @Override
    public double sample() {
        // Box-Muller transform
        double u1 = Math.random();
        double u2 = Math.random();
        double z = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        return mean + stdDev * z;
    }
}
