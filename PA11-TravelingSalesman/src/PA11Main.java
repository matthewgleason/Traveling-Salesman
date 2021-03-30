/*
 * @author Matt Gleason
 *         File: PA11Main.java
 *         Assignment: PA11
 *         Course: CSC 210 - 001, Summer 2020
 *         Purpose: The main function to take a set of data, and turn it into a DGraph 
 *         that is traversed using a heuristic, recursive backtracking, and my own 
 *         formulation of path finding. 
 *         
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PA11Main {
    public static void main(String[] args) {
        String file = args[0];
        List<String> commands = new ArrayList<>();
        // takes out all of the extra chars and makes commands
        for (int i = 1; i < args.length; i++) {
            if (args[i].substring(0, 1).equals("[")) {
                args[i] = args[i].substring(1, args[i].length() - 1);
            }
            if (args[i].substring(args[i].length() - 1, args[i]
                    .length())
                    .equals("]")) {
                args[i] = args[i].substring(0, args[i].length() - 1);
            }
            commands.add(args[i].replaceAll(",", ""));
        }
        DGraph graph = createGraph(file);

        // prints out the data according to what method is used
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).toLowerCase().equals("heuristic")) {
                Trip hTrip = heuristic(graph);
                System.out.println(hTrip.toString(graph));
            } else if (commands.get(i).toLowerCase().equals("backtrack")) {
                Trip bTrip = backtrack(graph);
                System.out.println(bTrip.toString(graph));
            } else if (commands.get(i).toLowerCase().equals("mine")) {
                Trip mTrip = mineApproach(graph);
                System.out.println(mTrip.toString(graph));
            } else if (commands.get(i).toLowerCase().equals("time")) {
                String[] arr = new String[] { "HEURISTIC", "MINE", "BACKTRACK" };
                for (int j = 0; j < arr.length; j++) {
                    printTime(graph, arr[j]);
                }
            }
        }
    }

    /*
     * Prints out the path and the cost it took to get there.
     * Also shows runtime speed of the method.
     * Parameter: command is a string that is to tell which
     * method is being used.
     * Graph is the graph being analyzed.
     * Returns: void
     */
    private static void printTime(DGraph graph, String command) {
        // TODO Auto-generated method stub
        if (command.toLowerCase().equals("heuristic")) { // heuristic
            long startTime = System.nanoTime();
            Trip trip = heuristic(graph);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("heuristic: cost = " + trip.tripCost(graph)
                    + ", " + duration + " milliseconds");
        } else if (command.toLowerCase().equals("backtrack")) { // backtrack
            long startTime = System.nanoTime();
            Trip trip = backtrack(graph);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("backtrack: cost = " + trip.tripCost(graph)
                    + ", " + duration + " milliseconds");
        } else { // mine
            long startTime = System.nanoTime();
            Trip trip = mineApproach(graph);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("mine: cost = " + trip.tripCost(graph) + ", "
                    + duration + " milliseconds");
        }

    }

    /*
     * This method combines both heuristic and recursion methods in that
     * there is pruning here. Creates the trip for the salesman to follow.
     * Parameters: graph is a Dgraph that is used to look at what
     * nodes to traverse to.
     * Returns: trip is a Trip path that the sales person has followed.
     * 
     */
    private static Trip mineApproach(DGraph graph) {
        Trip trip = new Trip(graph.getNumNodes());
        Trip minTrip = new Trip(graph.getNumNodes());
        Integer city = trip.citiesLeft().get(0);
        trip.chooseNextCity(city);
        for (int i = 2; i <= graph.getNumNodes(); i++) { // start at 2 because
                                                         // we already put one
                                                         // in
            Integer closest = null;
            if (trip.citiesLeft().isEmpty()) { // prunes
                if (trip.tripCost(graph) < minTrip.tripCost(graph)) {
                    minTrip.copyOtherIntoSelf(trip); // copies into shorter path
                                                     // object
                }
                return minTrip;
            }
            if (trip.tripCost(graph) < minTrip.tripCost(graph)) {
                for (Integer neighbor : graph.getNeighbors(city)) { // looks for
                                                                    // the
                                                                    // closest
                    if (trip.isCityAvailable(neighbor)) { // available neighbor
                        if (closest == null) {
                            closest = neighbor;
                        }
                        if (graph.getWeight(city, closest) > graph
                                .getWeight(city, neighbor)) {
                            closest = neighbor;
                        }
                    }
                }
            }
            trip.chooseNextCity(closest);
            city = closest;
        }
        return trip;
    }


    /*
     * This method looks for a path recursively and manages the amount of nodes
     * being looked at by pruning. Creates the trip for the salesman to follow.
     * Parameters: graph is a Dgraph that is used to look at what
     * nodes to traverse to.
     * Returns: MinTrip is a Trip path that the sales person has followed that
     * has
     * the shortest possible total cost.
     */
    private static Trip backtrack(DGraph graph) {
        // TODO Auto-generated method stub\
        Trip trip = new Trip(graph.getNumNodes());
        Integer city = trip.citiesLeft().get(0);
        trip.chooseNextCity(city);
        Trip minTrip = new Trip(trip.citiesLeft().size() + 1);
        backHelper(graph, trip, minTrip);
        return minTrip;
    }

    /*
     * Helper method for backtrack.
     * Just finds the path for the salesman. Uses minTrip to store the lowest
     * costing trip which is to be used in the backtrack() method.
     * Parameters: graph is a Dgraph that is used to look at what
     * nodes to traverse to. MinTrip is a Trip path that the sales person has
     * followed that has
     * the shortest possible total cost. trip is a Trip path that the sales
     * person has followed.
     * Returns: void
     */
    private static void backHelper(DGraph graph, Trip trip, Trip minTrip) {
        // TODO Auto-generated method stub
        if (trip.citiesLeft().isEmpty()) {
            if (trip.tripCost(graph) < minTrip.tripCost(graph)) { // pruning
                minTrip.copyOtherIntoSelf(trip);
            }
            return;
        }
        if (trip.tripCost(graph) < minTrip.tripCost(graph)) { // pruning
            for (Integer city : trip.citiesLeft()) {
                trip.chooseNextCity(city); // choose
                backHelper(graph, trip, minTrip); // explore
                trip.unchooseLastCity(); // unchoose
            }
        }
    }

    /*
     * This method looks for a path heuristically, so
     * it looks for the shortest possible path every time
     * when traversing.
     * Creates the trip for the salesman to follow.
     * Parameters: graph is a Dgraph that is used to look at what
     * nodes to traverse to.
     * Returns: trip is a Trip path that the sales
     * person has followed.
     */
    private static Trip heuristic(DGraph graph) {
        // TODO Auto-generated method stub
        Trip trip = new Trip(graph.getNumNodes());
        Integer city = trip.citiesLeft().get(0);
        trip.chooseNextCity(city); // puts city into the trip
        for (int i = 2; i <= graph.getNumNodes(); i++) { // start at 2 because
                                                         // we already put one
                                                         // in
            Integer closest = null;
            for (Integer neighbor : graph.getNeighbors(city)) {
                // looks for closest, available neighbor
                if (trip.isCityAvailable(neighbor)) {
                    if (closest == null) {
                        closest = neighbor;
                    }
                    if (graph.getWeight(city, closest) > graph.getWeight(city,
                            neighbor)) {
                        closest = neighbor;
                    }
                }
            }
            // puts it into the trip list when we find the closest.
            trip.chooseNextCity(closest);
            city = closest;
        }
        return trip;
    }

    /*
     * Creates the graph object that is the basis of this program.
     * Parameters: command is the file we are taking in.
     * Returns: graph is a Dgraph that is used to look at what
     * nodes to traverse to.
     */
    private static DGraph createGraph(String command) {
        // TODO Auto-generated method stub
        Scanner scan = null;
        try {
            scan = new Scanner(new File(command));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DGraph graph = null;
        int count = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (!line.substring(0, 1).equals("%")) { // for the beginning of
                                                     // files.
                String[] arr = line.split("\\s+");
                if (count == 0) { // for creating the size of the Dgraph
                    graph = new DGraph(Integer.parseInt(arr[0]));
                    count++;
                    continue;
                }
                graph.addEdge(Integer.parseInt(arr[0]),
                        Integer.parseInt(arr[1]), Double.parseDouble(arr[2]));
            }
        }
        scan.close();
        return graph;
    }
}
