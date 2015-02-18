import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public abstract class Agent
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The humanoid's field.
    private Field field;
    // The humanoid's position in the field.
    private Location location;

    public int numberBirth = 0;
    
    /**
     * Create a new humanoid at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Agent(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this humanoid act - that is: make it do
     * whatever it wants/needs to do.
     * @param newHumanoids A list to receive newly born animals.
     */
    abstract public void act(List<Agent> newHumanoids);

    /**
     * Check whether the humanoid is alive or not.
     * @return true if the humanoid is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the humanoid's location.
     * @return The humanoid's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the humanoid at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the humanoid's field.
     * @return The humanoid's field.
     */
    protected Field getField()
    {
        return field;
    }


    public int getNumberBirth() {
        return numberBirth;
    }

    public void setNumberBirth(int numberBirth) {
        this.numberBirth = numberBirth;
    }



}
