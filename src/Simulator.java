import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 256;//120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 256;//80;
    // The probability that a fox will be created in any given grid position.
    private static final double ZOMBIE_CREATION_PROBABILITY = 0.002;   //0.002  MO  0.002 likt
    // The probability that a rabbit will be created in any given grid position.
    private static final double HUMAN_CREATION_PROBABILITY = 0.2;      //0.3  MO   0.2 likt

    // List of humanoids in the field.
    private List<Agent> humanoids;
    // The current state of the field.
    private Field field;

    private int[][] agearray = new int[2][9000];

    private FieldStats stats;
    private GenerateCSV writeToFile;



    // The current step of the simulation.
    private int step;

    public int getStep() {
        return step;
    }

    public Field getField() {
        return field;
    }

    private static final int totSteps = 5000;
    // A graphical view of the simulation.
    private List<Thread> threads;
    private List<SimulatorView> views;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        humanoids = new ArrayList<Agent>();
        field = new Field(depth, width);
        stats = new FieldStats();

        threads = new ArrayList<Thread>();
        views = new ArrayList<SimulatorView>();

        SimulatorView view = new GridView(depth, width);
        view.setColor(Human.class, Color.BLACK);
        view.setColor(Zombie.class, Color.RED);
        view.setSim(this);
        views.add(view);
        threads.add(new Thread((Runnable) view));


        view = new GraphView(500, 150, 500);
        view.setColor(Human.class, Color.BLACK);
        view.setColor(Zombie.class, Color.RED);
        view.setSim(this);
        views.add(view);
        threads.add(new Thread((Runnable)view));


        // Setup a valid starting point.
        resetT();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        long time = System.currentTimeMillis();
        simulate(totSteps);
        System.out.println(System.currentTimeMillis()-time);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        //int[] zombiecount = new int[numSteps];
        //int[] humancount = new int [numSteps];


        for(int step = 1; step <= numSteps /*&& views.get(0).isViable(field)*/; step++) {
            simulateOneStep();
            stats.reset();
            GenerateCSV.fileAppendBuffer(""+step+","+stats.getPopulationCount(field, Zombie.class) + "," + +Zombie.getConvertions()+","+Zombie.getDeaths() + "," + stats.getPopulationCount(field, Human.class) + "," +Human.getBorn()+","+Human.getDeaths()+ "\n", "population.csv");
            GenerateCSV.fileAppendBuffer(""+step+","+Human.getBorn()+","+Human.getDeaths()+"\n","newBorns.csv");
        }
        GenerateCSV.generateCsvFiles();
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        //zombiecount[step] = stats.getPopulationCount(field, Zombie.class);
        //stats.getPopulationCount(field, Human.class);

        // Provide space for newborn humanoids.
        List<Agent> newHumanoids = new ArrayList<Agent>();        
        // Let all rabbits act.
        for(Iterator<Agent> it = humanoids.iterator(); it.hasNext(); ) {
            Agent humanoid = it.next();
            humanoid.act(newHumanoids);
            if(! humanoid.isAlive()) {
                it.remove();
            }
        }

        humanoids.addAll(newHumanoids);

        updateViewsT(); //updateviewsT for threaded
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        humanoids.clear();
        for (SimulatorView view : views) {
            view.reset();
        }

        populate();
        updateViews();
    }

    public void resetT()
    {
        step = 0;
        humanoids.clear();
//        for (SimulatorView view : views) {
//            view.reset();
//        }

        populate();
        updateViewsT();
    }
    
    /**
     * Update all existing views.
     */
    private void updateViews()
    {
        for (SimulatorView view : views) {
            view.showStatus(step, field);
        }
    }
    private void updateViewsT()
    {
        for (Thread t : threads) {
            t.run();
        }
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= ZOMBIE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Zombie zombie = new Zombie(false, field, location);
                    humanoids.add(zombie);
                }
                else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Human human = new Human(true, field, location);
                    humanoids.add(human);
                }
                // else leave the location empty.
            }
        }
    }

}
