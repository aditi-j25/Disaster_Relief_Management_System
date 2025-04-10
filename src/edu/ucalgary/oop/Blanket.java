package edu.ucalgary.oop;

/**
 * The {@code Blanket} class represents a blanket-type supply item used in the disaster relief system.
 * It extends the {@link Supply} class and initializes the item with the type "blanket".
 * <p>
 * This class is part of the inventory system and allows blanket supplies to be allocated
 * to disaster victims or locations as needed.
 * </p>
 * 
 * @author 30208786
 * @version 1.0
 * @since 2025-04-08
 * @see Supply
 */
public class Blanket extends Supply {

    /**
     * Constructs a new {@code Blanket} supply item.
     * Initializes the item with the name "blanket".
     */
    public Blanket() {
        super("blanket");
    }
}
