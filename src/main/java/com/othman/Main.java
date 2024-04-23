package com.othman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Pattern cityPattern = Pattern.compile("(\\w+),\\s*(\\[\\w+\\s*,\\s*\\d+\\s*,\\s*\\d+\\s*\\](?:,\\s*\\[\\w+\\s*,\\s*\\d+\\s*,\\s*\\s*\\d+\\s*\\])*)");
    private static final Pattern routePattern = Pattern.compile("\\[(\\w+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\]*");


    public static void main(String[] args) {
        readFile(Path.of("sample.txt"));
    }

    // TODO: Remove all spaces in the file
    // TODO: Throw exceptions for irrecoverable errors


    public static void readFile(Path path) {
        List<City> cities = new ArrayList<>();
        try (var bufferedReader = Files.newBufferedReader(path)) {

            String line;
            int lineCounter = 0;
            int stage = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank() || lineCounter < 2) {
                    lineCounter++;
                    logger.info("Skipping line: " + line + " line number: " + (lineCounter + 1));
                    continue;
                }

                Matcher matcher = cityPattern.matcher(line);
                if (!matcher.find()) {
                    logger.warning("could not find a match in line: " + line + " line number: " + (lineCounter + 1));
                    lineCounter++;
                    continue;
                }

                String cityName = matcher.group(1); // Should capture "D"
                String routes = matcher.group(2);  // Should capture "[F,25,50], [G,30,70], [H,18,70], [I, 27,60]"

                City city = new City(cityName, stage++);
                // Split the routes into individual routes

                if (!cities.contains(city)) {
                    cities.add(city);
                }

                int index = cities.indexOf(city);
                City cityInList = cities.get(index);


                String[] split = routes.split("\\],\\s*");

                if (split.length == 0) {
                    logger.warning("could not split routes: " + routes + " line number: " + (lineCounter + 1));
                    lineCounter++;
                    continue;
                }

                // Split the individual routes into their components
                for (String s : split) {
                    Matcher routeMatcher = routePattern.matcher(s);
                    if (!routeMatcher.find()) {
                        logger.warning("could not find a match in route: " + s + " line number: " + (lineCounter + 1));
                        continue;
                    }

                    String routeName = routeMatcher.group(1);
                    int gasCost = Integer.MIN_VALUE;
                    try {
                        gasCost = Integer.parseInt(routeMatcher.group(2));
                    } catch (NumberFormatException e) {
                        logger.severe("could not parse gas cost: " + routeMatcher.group(2) + " line number: " + (lineCounter + 1));
                    }

                    int hotelCost = Integer.MIN_VALUE;
                    try {
                        hotelCost = Integer.parseInt(routeMatcher.group(3));
                    } catch (NumberFormatException e) {
                        logger.severe("could not parse hotel cost: " + routeMatcher.group(3) + " line number: " + (lineCounter + 1));
                    }

                    City destCity = new City(routeName, cityInList.getStage() + 1);
                    if (!cities.contains(destCity)) {
                        cities.add(destCity);
                    }

                    int destCityIndex = cities.indexOf(destCity);
                    City destCityFromList = cities.get(destCityIndex);

                    cityInList.addRoute(new Route(destCityFromList, gasCost, hotelCost));

                    cityInList.getOptimalRoute()
                            .ifPresentOrElse(
                                    optRoute -> System.out.println("Optimal route for city: " + cityInList.getName() + " is " + optRoute),
                                    () -> System.err.println("No optimal route found for city: " + cityInList.getName())
                            );

//                    System.out.println(cityInList);
                }
                lineCounter++;
//                stage++;
            }

        } catch (IOException e) {
            logger.severe("Could not read file: " + e.getMessage());
        }
    }
}