import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a human.
 * Humans age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Human extends Humanoid
{
    // Characteristics shared by all humans (class variables).

    // The age at which a human can start to breed.
    private static final int MIN_BREEDING_AGE = 30;
    // The age at which a human can start to breed.
    private static final int MAX_BREEDING_AGE = 50;
    // The age to which a human can live.
    private static final int MAX_AGE = 100;
    // The age a human starts to die of natural causes
    private static final int NATURALCAUSE_AGE = 60;
    // The maximum number of births.
    private static final int MAX_BIRTHS = 2;
    private int earlierbirths = 0;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    private static int born = 0;
    private static int deaths = 0;

    public static int getBorn() {
        return born;
    }

    private static int days = 1;
    
    // Individual characteristics (instance fields).

    private double deathProbability = 0.01;

    /**
     * Create a new human. A human may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Human(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 10+rand.nextInt(15);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    public Human(boolean randomAge,Field field, Location location,Human mom,Human dad) {
        super(field, location,
                (mom.strength+dad.strength-2+rand.nextInt(8))/2,            //strength
                (mom.stamina+dad.stamina-2+rand.nextInt(8))/2,             //stamina
                (mom.luck+dad.luck-2+rand.nextInt(8))/2,                //luck
                10+rand.nextInt(15), //age
                rand.nextInt(10)     //hunger
        );
    }

    public static int getDeaths() {
        return deaths;
    }

    @Override
    protected void setDead(String reason) {
        super.setDead(reason);
        deaths++;

        GenerateCSV.fileAppendBuffer(age+","+reason+"\n","humanDeaths.csv");
        GenerateCSV.fileAppendBuffer(""+strength+","+stamina+","+luck+"\n","childStats.csv");
    }

    /**
     * This is what the human does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newHumanoids A list to return newly born humans.
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
                setDead("Overcrowding");
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
            setDead("Old age");
        }

        // Checks if the humans age is over the age set for death by natural causes
        if (age > NATURALCAUSE_AGE && rand.nextDouble() <= deathProbability) {
            setDead("Natural causes");
        }
        
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHumanoids A list to return newly born rabbits.
     */
    private void giveBirth(List<Agent> newHumanoids)
    {
        // New humans are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());

        List<Location> mateLocations = field.adjacentLocations(getLocation());
        ArrayList<Human> mates = new ArrayList<Human>();
        for(Location loc:mateLocations){
            Object o = field.getObjectAt(loc);
            if(o instanceof Human){
                mates.add((Human)o);
            }
        }
        if ((free.size() > 0) && canBreed() && earlierbirths< MAX_BIRTHS && mates.size()>0){
            Human mate=mates.get(0);
            for(Human human:mates){
                if(human.luck+human.stamina+human.strength>mate.luck+mate.stamina+mate.strength){
                    mate=human;
                }
            }
            if(luck+stamina+strength<=mate.luck+mate.stamina+mate.strength+2){
                if(rand.nextInt(10000)<3){
                    Location loc = free.remove(0);
                    Human young = new Human(false, field, loc,this,mate);
                    newHumanoids.add(young);
                    born++;
                    earlierbirths++;
                }
            }
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
        return MIN_BREEDING_AGE <= age && age <= MAX_BREEDING_AGE;
    }
}
