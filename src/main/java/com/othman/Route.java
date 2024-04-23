package com.othman;

public record Route(City name, int gasCost, int hotelCost) {

    public int totalCost() {
        return gasCost + hotelCost;
    }

    @Override
    public String toString() {
        return "Route to %s and its stage is %d , gasCost: %d, hotelCost: %d ,total cost %d".formatted(name.getName(), name.getStage(), gasCost, hotelCost, totalCost());
    }
}
