

  //Continuous Uniform distribution over [min, max].
 
public class UniformDistribution extends Distribution {

    private double min;
    private double max;

    // Stores the range [min, max] and rejects if the min >= max.
    public UniformDistribution(double min, double max, int numTrials) {
        super("Uniform(min=" + min + ", max=" + max + ")", numTrials);
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max.");
        }
        this.min = min;
        this.max = max;
    }

    // Scales a Uniform(0,1) value into the range [min, max].
    @Override
    public double sample() {
        return min + (max - min) * Math.random();
    }
}
