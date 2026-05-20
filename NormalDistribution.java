

// Normal distribution using the Box-Muller transform
 
public class NormalDistribution extends Distribution {

    private double mean;
    private double stdDev;

    // Stores mean and standard deviation and rejects non-positive stdDev.
    public NormalDistribution(double mean, double stdDev, int numTrials) {
        super("Normal(mean=" + mean + ", stdDev=" + stdDev + ")", numTrials);
        if (stdDev <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive.");
        }
        this.mean = mean;
        this.stdDev = stdDev;
    }

    // Generates a Normal sample using the Box-Muller transform:
    // converts two Uniform(0,1) values into one standard Normal z,
    // then scales and shifts to match the desired mean and stdDev.
    @Override
    public double sample() {
        double u1 = Math.random();
        double u2 = Math.random();
        double z = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        return mean + stdDev * z;
    }
}
