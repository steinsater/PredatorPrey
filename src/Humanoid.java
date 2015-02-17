/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Random;

/**
 *
 * @author Stein
 */
public abstract class Humanoid extends Agent{
    
    protected int strength;
    protected int stamina;
    protected int luck;
    protected int age;
    protected int hunger;

    protected Humanoid(Field field, Location location, int strength, int stamina, int luck, int age, int hunger) {
        super(field, location);
        this.strength = strength;
        this.stamina = stamina;
        this.luck = luck;
        this.age = age;
        this.hunger = hunger;
    }
    protected Humanoid(Field field,Location location){
        super(field, location);
        Random rand = Randomizer.getRandom();
        this.strength = rand.nextInt(10);
        this.stamina = rand.nextInt(10);
        this.luck = rand.nextInt(10);
        this.age = rand.nextInt(10);
        this.hunger = rand.nextInt(10);
    }
}