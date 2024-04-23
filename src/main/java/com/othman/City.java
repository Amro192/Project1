package com.othman;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class City {

    private final String name; // Start for example or "A"
    private final int stage;
    private final List<Route> routes = new ArrayList<>(); // [F,25,50], [G,30,70], [H,18,70], [I, 27,60]

    //if null means it's the start city
    private OptimalRoute optimalRoute;

    public City(String name, int stage) {
        this.name = name;
        this.stage = stage;
    }

    public String getName() {
        return name;
    }

    public int getStage() {
        return stage;
    }

    public Optional<Route> getOptimalRoute() {


//        if (stage == 0) {
//            return Optional.empty();
//        }
//
//        routes.stream()
//                .min(Comparator.comparingInt(Route::totalCost))
//                .map(route -> new OptimalRoute(this, route))
//                .ifPresent(optimalRoute -> this.optimalRoute = optimalRoute);
//
//        return Optional.ofNullable(optimalRoute)
//                .map(OptimalRoute::route);
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;

        return name.equals(city.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        String collect = routes.stream()
                .map(Route::toString)
                .collect(Collectors.joining("\n"));

        return "City %s, Stage %d - Routes:\n%s".formatted(name, stage, collect);
    }

    public record OptimalRoute(City from, Route route) {

    }
}
