import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class ZombieTemplate extends Humanoid
{
    // Characteristics shared by all foxes (class variables).
    
   
    // The age to which a fox can live.
    private static final int MAX_AGE = 2000;
    private static final int HUMAN_FOOD_VALUE = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    private static int days = 1;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public ZombieTemplate(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(HUMAN_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = HUMAN_FOOD_VALUE;
        }
    }

    public ZombieTemplate(Field field,Location location,int strength, int stamina, int luck, int age, int hunger){
        super(field, location, strength, stamina, luck, age, hunger);
    }

    public static ZombieTemplate makeZombie(Humanoid human){
        ZombieTemplate zomb = new ZombieTemplate(human.getField(),human.getLocation(),human.strength,human.stamina,human.luck,human.age,human.hunger);
        human.setDead();
        return zomb;
    }

    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newZombies A list to return newly born foxes.
     */
    public void act(List<Agent> newZombies)
    {
        days++;
        incrementAge();
        if(isAlive()) {
                      
            // Move towards a source of food if found.
            Location newLocation = findHuman(newZombies);
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                //setDead();

            }
        }
    }

    /**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge()
    {
        if(days % 365 == 0){
            age++;
        }
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findHuman(List<Agent> newZombies)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object humanoid = field.getObjectAt(where);
            if(humanoid instanceof HumanTemplate) {
                HumanTemplate human = (HumanTemplate) humanoid;
                //ZombieTemplate zombie = (ZombieTemplate) humanoid;
                if(human.isAlive()) { 
                    //human.setDead();
                    //return where;
                    attemptEat(human,newZombies);
                }
            }
        }
        return null;
    }

    private void attemptEat(HumanTemplate human,List<Agent> newZombies){
        if(battle()>human.battle()){
            newZombies.add(makeZombie(human));
        }
    }
   
  
}
