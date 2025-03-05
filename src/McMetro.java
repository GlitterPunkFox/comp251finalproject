import java.util.*;
import java.lang.Math.*;

public class McMetro {
    protected Track[] tracks;
    protected HashMap<BuildingID, Building> buildingTable = new HashMap<>();
    private HashMap<BuildingID, List<Edge>> adjList = new HashMap<>();
    private final TrieNode root = new TrieNode(); // trie root
    private final Set<String> allPassengers = new HashSet<>(); // tracking passengers, using Hashset only need to return one name

    private static class Edge {
        BuildingID destination;
        int capacity;
        int resCapacity;
        Edge resGraphEdge;

        Edge(BuildingID dest, int capacity) {
            this.destination = dest;
            this.capacity = capacity;
            this.resCapacity = capacity;
        }
    }


    private static class TrieNode {
        Map<Character, TrieNode> map;
        boolean isEndOfWord;

        public TrieNode() {
            map = new HashMap<>();
            isEndOfWord = false;
        }
    }



    // Constructor to initialize tracks and buildings
    McMetro(Track[] tracks, Building[] buildings) {
        this.tracks = tracks;

        // Populate buildings table
        if (buildings != null) {
            for (Building building : buildings) {
                buildingTable.putIfAbsent(building.id(), building);
                adjList.putIfAbsent(building.id(), new ArrayList<>()); // Ensure adjacency list entry
            }
        }

        // Populate the adjacency list
        if (tracks != null) {
            for (Track track : tracks) {
                BuildingID source = track.startBuildingId();
                BuildingID dest = track.endBuildingId();

                //bug fixed- was doing flow problem only with track capacities when it should be the min between track capacity
                //and amount of people in each building
                int trackCapacity = track.capacity();
                //yay it works now- I am sad this took me almost two hours
                int capacity = 0;

                if (buildingTable.containsKey(source) && buildingTable.containsKey(dest)) {
                    capacity = Math.min(trackCapacity,
                            Math.min(buildingTable.get(source).occupants(),
                                    buildingTable.get(dest).occupants()));





                            Edge forwardEdge = new Edge(dest, capacity);
                            Edge reverseEdge = new Edge(source, 0);

                            // assign the reverse graph edge attributes
                            forwardEdge.resGraphEdge = reverseEdge;
                            reverseEdge.resGraphEdge = forwardEdge;

                            // Ensure lists exist in the adjacency list
                            adjList.putIfAbsent(source, new ArrayList<>());
                            adjList.putIfAbsent(dest, new ArrayList<>());

                            adjList.get(source).add(forwardEdge);
                            adjList.get(dest).add(reverseEdge);

                }
            }
           /* int capacity = Math.min(trackCapacity, Math.min(buildingTable.get(source).occupants(), buildingTable.get(dest).occupants()));


            // Create forward and reverse edges
            Edge forwardEdge = new Edge(dest, capacity);
            Edge reverseEdge = new Edge(source, 0);

            // assign the reverse graph edge attributes
            forwardEdge.resGraphEdge = reverseEdge;
            reverseEdge.resGraphEdge = forwardEdge;

            // Ensure lists exist in adjacency list
            if(!adjList.containsKey(source)){
                adjList.put(source, new ArrayList<>());
            }
            if(!adjList.containsKey(dest)){
                adjList.put(dest, new ArrayList<>());
            }

            adjList.get(source).add(forwardEdge);
            adjList.get(dest).add(reverseEdge);*/
        }
    }

    // Maximum number of passengers that can be transported from start to end (source to sink)
    //https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
    //but instead of for loop looping backwards from source to sink I used while loops and a current tracker thing
    int maxPassengers(BuildingID start, BuildingID end) {
        //check if the buildings even exist
        if(!buildingTable.containsKey(start) || !buildingTable.containsKey(end)){
            return 0;
        }
        // Validate start and end
        if (!adjList.containsKey(start) || !adjList.containsKey(end)) {
            return 0; // No path possible if start or end is missing
        }

        //attempting pass testMaxPassengers9, i.e. there is a self loop
        if (start.equals(end)) {
            for (Edge edge : adjList.get(start)) {
                if (edge.destination.equals(start)) {
                    return edge.resCapacity;
                }
            }
            return 0;
        }




        int maxFlow = 0;

        // While there exists an augmenting path
        while (true) {
            HashMap<BuildingID, Edge> parent = new HashMap<>();
            boolean hasPath = bfsAugment(start, end, parent);

            // no augmenting path?
            if (!hasPath) break;

            // Find the bottleneck capacity
            int pathFlow = Integer.MAX_VALUE;
            BuildingID current = end; //start from sink
            while (!current.equals(start)) {
                Edge edge = parent.get(current);
                pathFlow = Math.min(pathFlow, edge.resCapacity);
                current = edge.resGraphEdge.destination;
            }

            // Update residual capacities along the path
            current = end;
            while (!current.equals(start)) {
                Edge edge = parent.get(current);
                edge.resCapacity -= pathFlow;
                edge.resGraphEdge.resCapacity += pathFlow;
                current = edge.resGraphEdge.destination;
            }

            // adding to maxflow
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    // BFS to find an augmenting path
    //https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
    private boolean bfsAugment(BuildingID start, BuildingID end, HashMap<BuildingID, Edge> parent) {
        Queue<BuildingID> queue = new LinkedList<>();
        Set<BuildingID> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            BuildingID u = queue.poll();
            for (Edge edge : adjList.get(u)) {
                // node not visited and has residual capacity
                if (!visited.contains(edge.destination) && edge.resCapacity > 0) {

                    // return true, we got to the sink
                    if (edge.destination.equals(end)) {
                        parent.put(edge.destination, edge);
                        return true;
                    }

                    queue.add(edge.destination);
                    parent.put(edge.destination, edge);
                    visited.add(edge.destination);
                }
            }
        }

        // No path found
        return false;
    }




    // Returns a list of trackIDs that connect to every building maximizing total network capacity taking cost into account
    TrackID[] bestMetroSystem() {
        // TODO: your implementation here
        //this was a pain
        PriorityQueue<Track> pq = new PriorityQueue<>(
                new Comparator<Track>() { //https://www.geeksforgeeks.org/priority-queue-in-reverse-order-in-java/
                    public int compare(Track a, Track b) {
                        double goodnessA = howGoodTrack(a);
                        double goodnessB = howGoodTrack(b);

                        // Sorting in descending order
                        if (goodnessA < goodnessB){
                            return 1;
                        }
                        if (goodnessA > goodnessB) {
                            return -1;
                        }
                        return 0; // They are equal
                    }
                }
        );

        for (Track track : tracks) {
            pq.add(track);
        }

        //adding each building
        NaiveDisjointSet<BuildingID> makeSet = new NaiveDisjointSet<>();
        for (BuildingID buildingID : buildingTable.keySet()) {
            makeSet.add(buildingID);
        }

        //https://en.wikipedia.org/wiki/Kruskal%27s_algorithm

        ArrayList<TrackID> best = new ArrayList<>();

        while (!pq.isEmpty() && best.size() < buildingTable.size() - 1) {
            Track curr = pq.poll();
           // System.out.println("Processing Track ID: " + curr.id() + ", Goodness: " + howGoodTrack(curr));



            // Check if adding this track forms a cycle
            if (!makeSet.find(curr.startBuildingId()).equals(makeSet.find(curr.endBuildingId()))) {
                // Add the track to the result
              //  System.out.println("Adding Track ID: " + curr.id() + " to the MST");
                best.add(curr.id());

                makeSet.union(curr.startBuildingId(), curr.endBuildingId());
            }
        }

        int size = best.size();

        TrackID[] actualBest = new TrackID[size];

        for (int i = 0; i < size; i++){
            actualBest[i] = best.get(i);
        }

        return actualBest;

        //return new TrackID[0];
    }

    int howGoodTrack(Track track){
        int minCapacity =  Math.min(track.capacity(), Math.min(buildingTable.get(track.startBuildingId()).occupants(), buildingTable.get(track.endBuildingId()).occupants()));
        return minCapacity/track.cost();
    }



    // Adds a passenger to the system
    //https://www.geeksforgeeks.org/trie-memory-optimization-using-hash-map/
    //initially was coding with static because it was easier to test
     void addPassenger(String name) {
        String nameToAdd = name.toLowerCase();
        TrieNode current = root;

        for (char c : nameToAdd.toCharArray()) {
            if(!current.map.containsKey(c)){
                current.map.put(c, new TrieNode());
            }
            current = current.map.get(c);
        }
        current.isEndOfWord = true;
        allPassengers.add(capital(name));
    }

    // Method to search for passengers by prefix
  ArrayList<String> searchForPassengers(String firstLetters) {
        String allLowerCase = firstLetters.toLowerCase();
        TrieNode current = root;
        ArrayList<String> results = new ArrayList<>();

        for (char c : allLowerCase.toCharArray()) {
            if (!current.map.containsKey(c)) {
                return results; // If prefix not found, return an empty list
            }
            current = current.map.get(c);
        }

        traverseTrie(current, firstLetters, results);

        //sorting alphabetically
        Collections.sort(results); //https://stackoverflow.com/questions/708698/how-can-i-sort-a-list-alphabetically
        return results;
    }

    // helper method
    //https://github.com/chenleishen/PrefixMatcher/blob/master/PrefixMatcher.java
    private void traverseTrie(TrieNode node, String prefix, List<String> results) {
        if (node.isEndOfWord) {
            results.add(capital(prefix));
        }
        for (Map.Entry<Character, TrieNode> mapElement : node.map.entrySet()) { //https://www.geeksforgeeks.org/traverse-through-a-hashmap-in-java/
            char next = mapElement.getKey();
            TrieNode child = mapElement.getValue();
            traverseTrie(child, prefix + next, results);
        }
    }

    //making it a capital letter again
    private String capital(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }




    // Do not change this
    void addPassengers(String[] names) {
        for (String s : names) {
            addPassenger(s);
        }
    }




    // Return how many ticket checkers will be hired
    //easy interval scheduling (not weighted so can use greedy thank god)
    //https://www.geeksforgeeks.org/how-to-implement-interval-scheduling-algorithm-in-python/
    //reread instructions apparently we can have negative times
    static int hireTicketCheckers(int[][] schedule) {
        // TODO: your implementation here

        //https://stackoverflow.com/questions/15452429/java-arrays-sort-2d-array
        Arrays.sort(schedule, new Comparator<int[]>() {
            public int compare(final int[] a, final int[] b) {
                return Integer.compare(a[1], b[1]); // fixed- should be sorted by end times oops
            }
        });

        int[][] temp = schedule;
        int count = 0;
        int end = Integer.MIN_VALUE;

        for (int[] t: temp){
            if (end <= t[0]) {
                end = t[1];
                count += 1;
            }

        }
        return count;
    }



}
