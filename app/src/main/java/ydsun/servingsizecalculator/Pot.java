package ydsun.servingsizecalculator;

/**
 * Store information about a single pot
 */

public class Pot {
    private String name;
    private int weightInG;

    // Set member data based on parameters.
    public Pot(String name, int weightInG) {
        this.name = name;
        this.weightInG = weightInG;
    }

    // Return the weight
    public int getWeightInG() {
        return weightInG;
    }

    // Set the weight. Throws IllegalArgumentException if weight is less than 0.
    public void setWeightInG(int weightInG) {
        if(weightInG < 0){
            throw new IllegalArgumentException("weight must be positive!");
        }
        else{
            this.weightInG = weightInG;
        }
    }

    // Return the name.
    public String getName() {
        return name;
    }

    // Set the name. Throws IllegalArgumentException if name is an empty string (length 0),
    // or if name is a null-reference.
    public void setName(String name) {
        if(name.length() == 0 || name.equals(null)){
            throw new IllegalArgumentException("name cannot be empty!");
        }
        else{
            this.name = name;
        }
    }
}