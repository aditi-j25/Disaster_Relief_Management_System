package edu.ucalgary.oop;

import java.util.ArrayList;

/**
 * Represents a physical location in the disaster management system.
 * Locations can house disaster victims and store supplies.
 * 
 * @author 30208786
 * @version 1.0
 * @since 2025-04-01
 */
public class Location {
    private String name;
    private String address;
    private ArrayList<DisasterVictim> occupants = new ArrayList<>();
    private ArrayList<Supply> supplies = new ArrayList<>();
    
    /**
     * Constructs a new Location with the specified name and address.
     * 
     * @param name    the name of the location
     * @param address the physical address of the location
     */
    public Location(String name, String address) {
        this.name = name;
        this.address = address;
    }
    
    /**
     * Returns the name of this location.
     * 
     * @return the location name
     */
    public String getName() { return name; }
    
    /**
     * Sets the name of this location.
     * 
     * @param name the new location name
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Returns the address of this location.
     * 
     * @return the location address
     */
    public String getAddress() { return address; }
    
    /**
     * Sets the address of this location.
     * 
     * @param address the new location address
     */
    public void setAddress(String address) { this.address = address; }
    
    /**
     * Returns a copy of the list of occupants at this location.
     * 
     * @return a new ArrayList containing the occupants
     */
    public ArrayList<DisasterVictim> getOccupants() { return new ArrayList<>(occupants); }
    
    /**
     * Sets the list of occupants at this location.
     * Creates a defensive copy of the provided list.
     * 
     * @param occupants the new list of occupants
     */
    public void setOccupants(ArrayList<DisasterVictim> occupants) { this.occupants = new ArrayList<>(occupants); }
    
    /**
     * Returns a copy of the list of supplies at this location.
     * 
     * @return a new ArrayList containing the supplies
     */
    public ArrayList<Supply> getSupplies() { return new ArrayList<>(supplies); }
    
    /**
     * Sets the list of supplies at this location.
     * Creates a defensive copy of the provided list.
     * 
     * @param supplies the new list of supplies
     */
    public void setSupplies(ArrayList<Supply> supplies) { this.supplies = new ArrayList<>(supplies); }
    
    /**
     * Adds an occupant to this location.
     * 
     * @param occupant the disaster victim to add
     */
    public void addOccupant(DisasterVictim occupant) { 
        if (occupant == null) {
            System.out.println("Cannot add null occupant!");
            return; // Exit method without adding
        }
        this.occupants.add(occupant);
    }
    
    /**
     * Removes an occupant from this location.
     * 
     * @param occupant the disaster victim to remove
     */
    public void removeOccupant(DisasterVictim occupant) { occupants.remove(occupant); }
    
    /**
     * Adds a supply item to this location.
     * 
     * @param supply the supply to add
     */
    public void addSupply(Supply supply) { supplies.add(supply); }
    
    /**
     * Removes a supply item from this location.
     * 
     * @param supply the supply to remove
     */
    public void removeSupply(Supply supply) { supplies.remove(supply); }
}