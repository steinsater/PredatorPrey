import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class HumanTemplate extends Humanoid
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a human can start to breed.
    private static final int MIN_BREEDING_AGE = 18;
    // The age at which a human can start to breed.
    private static final int MAX_BREEDING_AGE = 40;
    // The age to which a human can live.
    private static final int MAX_AGE = 100;
    // The age a human starts to die of natural causes
    private static final int NATURALCAUSE_AGE = 60;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 40;
    private static int earlierbirths = 0;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    private static int born = 0;

    private static int days = 1;
    
    // Individual characteristics (instance fields).
    
    // The humans's age.
    private int age;
    private double deathProbability = 0.01;

    /**
     * Create a new human. A human may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public HumanTemplate(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newHumanoids A list to return newly born rabbits.
     */
    public void act(List<Agent> newHumanoids)
    {
        days++;
        incrementAge();
        if(isAlive()) {
            giveBirth(newHumanoids);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {

        if(days % 365 == 0){
            age++;
            deathProbability = (deathProbability + 0.01);
        }

        if(age > MAX_AGE) {
            setDead();
        }

        // Checks if the humans age is over the age set for death by natural causes
        if (age > NATURALCAUSE_AGE && rand.nextDouble() <= deathProbability) {
            setDead();
        }
        
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHumanoids A list to return newly born rabbits.
     */
    private void giveBirth(List<Agent> newHumanoids)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        Location randomLocation = field.randomAdjacentLocation(getLocation());
        //Object o = field.getObjectAt(randomLocation); //&& o != null
        if ((free.size() > 0) && canBreed() && earlierbirths<MAX_LITTER_SIZE){
            Location loc = free.remove(0);
            HumanTemplate young = new HumanTemplate(false, field, loc);
            newHumanoids.add(young);
            born++;
            earlierbirths++;
        }

    }
        
    private void attack(){

    }


    /**
     * A human can breed if it has reached the breeding age and not passed max breeding age.
     * @return true if the human can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return MIN_BREEDING_AGE <= age && age >= MAX_BREEDING_AGE;
    }
}
