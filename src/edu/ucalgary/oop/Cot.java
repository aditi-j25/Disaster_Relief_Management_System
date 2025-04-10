package edu.ucalgary.oop;

/**
 * The {@code Cot} class represents a cot supply item used in the disaster relief system.
 * Each cot has an associated room and grid location, stored in the comments field.
 * <p>
 * This class extends the {@link Supply} class and sets the supply type to "cot".
 * </p>
 * 
 * @author 30208786
 * @version 1.0
 * @since 2025-04-08
 * @see Supply
 */
public class Cot extends Supply {

    /**
     * Constructs a new {@code Cot} supply item.
     * 
     * @param location A string representing the room and grid location (e.g., "101 A2").
     */
    public Cot(String location) {
        super("cot");
        setComments(location); // Room/grid location
    }

    /**
     * Sets the comments for the cot item, which stores the room and grid location.
     *
     * @param comments The location details of the cot.
     */
    public void setComments(String comments) { 
        super.setComments(comments); 
    }
}
