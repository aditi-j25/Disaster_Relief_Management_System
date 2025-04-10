package edu.ucalgary.oop;

/**
 * The PersonalBelonging class represents a type of supply categorized as a personal item.
 * It extends the {@link Supply} class and is used to handle personal belongings with a description.
 * 
 * @author 30208786
 * @version 1.0
 * @since 2025-01-30
 * @see Supply
 */
public class PersonalBelonging extends Supply {

    /**
     * Constructs a new PersonalBelonging with the specified description.
     * The description will be set as the comments for the supply item.
     * 
     * @param description the description of the personal item
     */
    public PersonalBelonging(String description) {
        super("personal item");
        setComments(description); 
    }

    /**
     * Sets the comments (description) for the personal item.
     * This method calls the superclass {@link Supply#setComments(String)} to set the description.
     * 
     * @param comments the description of the personal belonging
     */
    @Override
    public void setComments(String comments) {
        super.setComments(comments);
    }
}
