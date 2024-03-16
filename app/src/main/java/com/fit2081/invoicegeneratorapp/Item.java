package com.fit2081.invoicegeneratorapp;

/**
 * Represents an item in an invoice.
 * This class is a data model that stores details about an item, including its name, quantity, and cost.
 */
public class Item {
    // Field for storing the name of the item
    private String itemName;
    // Field for storing the quantity of the item
    private int itemQuantity;
    // Field for storing the cost of the item
    private double itemCost;

    /**
     * Constructor for creating a new Item.
     *
     * @param itemName: The name of the item
     * @param itemQuantity: The quantity of the time
     * @param itemCost: The cost of the item
     */
    public Item(String itemName, int itemQuantity, double itemCost) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemCost = itemCost;
    }

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Gets the quantity of the item.
     *
     * @return The quantity of the item.
     */
    public int getItemQuantity() {
        return itemQuantity;
    }

    /**
     * Gets the cost of the item.
     *
     * @return The cost of the item.
     */
    public double getItemCost() {
        return itemCost;
    }

    /**
     * Sets the name of the item.
     *
     * @param itemName The name of the item.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param itemQuantity The quantity of the item.
     */
    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     * Sets the cost of the item.
     *
     * @param itemCost The cost of the item.
     */
    public void setItemCost(double itemCost) {
        this.itemCost = itemCost;
    }
}
