package statssim;

// Abstract base class for all distributions,
public abstract class Distribution {

    protected String name; 
    protected int numTrials;

    public Distribution(String name, int numTrials) {
        if (numTrials <= 0) {
            throw new IllegalArgumentException("Number of trials must be positive.");
        }
        this.name = name;
        this.numTrials = numTrials;
    }

    // Generate one random sample from this distribution
    
    public abstract double sample();

    // Run the full simulation by calling sample() numTrials times

    public SimulationResult runSimulation() {
        double[] data = new double[numTrials];
        for (int i = 0; i < numTrials; i++) {
            data[i] = sample();
        }
        return new SimulationResult(name, data);
    }

    public String getName() { 
        return name; 
    }
    public int getNumTrials() { 
        return numTrials; 
    }
}
