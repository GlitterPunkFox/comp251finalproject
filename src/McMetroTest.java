import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;



class NaiveDisjointSetTest {

    @Test
    void testAddAndFind() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        ds.add(1);
        ds.add(2);
        ds.add(3);

        assertEquals(1, ds.find(1));
        assertEquals(2, ds.find(2));
        assertEquals(3, ds.find(3));
    }

    @Test
    void testUnionAndFind() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        ds.add(1);
        ds.add(2);
        ds.add(3);

        ds.union(1, 2);

        assertEquals(ds.find(1), ds.find(2));

        assertNotEquals(ds.find(1), ds.find(3));
    }

    @Test
    void testPathCompression() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        ds.add(1);
        ds.add(2);
        ds.add(3);
        ds.add(4);
        ds.add(5);

        ds.union(1, 2);
        ds.union(2, 3);
        ds.union(3, 4);
        ds.union(4, 5);

        int root = ds.find(1);
        assertEquals(root, ds.find(4));

        assertEquals(root, ds.find(2));
        assertEquals(root, ds.find(3));
        assertEquals(root, ds.find(4));
    }

    @Test
    void testUnionByRank() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        ds.add(1);
        ds.add(2);
        ds.add(3);
        ds.add(4);

        // Perform unions by rank
        ds.union(1, 2); // Rank of 1 becomes 1
        ds.union(3, 4); // Rank of 3 becomes 1
        ds.union(1, 3); // Rank of 1 becomes 2, as both sets are of equal rank

        // Verify the ranks and roots
        assertEquals(ds.find(1), ds.find(2));
        assertEquals(ds.find(1), ds.find(3));
        assertEquals(ds.find(1), ds.find(4));
    }

    @Test
    void testRedundantUnions() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        // Add elements
        ds.add(1);
        ds.add(2);

        // Perform a union
        ds.union(1, 2);

        // Perform the same union again
        ds.union(2, 1);


        // Check that the structure remains consistent
        assertEquals(ds.find(1), ds.find(2));
    }

    @Test
    void testFindOnUnaddedElement() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        // Attempting to find or union an unadded element should throw an exception
        assertThrows(NullPointerException.class, () -> ds.find(1));
    }

    @Test
    void testUnionOnUnaddedElements() {
        NaiveDisjointSet<Integer> ds = new NaiveDisjointSet<>();

        // Attempting to union unadded elements should throw an exception
        assertThrows(NullPointerException.class, () -> ds.union(1, 2));
    }

    @Test
    void testMultipleDisjointSets() {
        NaiveDisjointSet<String> ds = new NaiveDisjointSet<>();

        // Add elements
        ds.add("A");
        ds.add("B");
        ds.add("C");
        ds.add("D");

        // Perform unions to create two disjoint sets
        ds.union("A", "B");
        ds.union("C", "D");

        // Verify that the sets remain disjoint
        assertNotEquals(ds.find("A"), ds.find("C"));
        assertNotEquals(ds.find("B"), ds.find("D"));
        assertEquals(ds.find("A"), ds.find("B"));
        assertEquals(ds.find("C"), ds.find("D"));
    }
}

class McMetroTest {
    @Test
    void testDirectConnection() {
        // Create buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        // Create tracks
        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 50, 120)
        };

        // Initialize McMetro
        McMetro metro = new McMetro(tracks, buildings);

        // Test maxPassengers
        assertEquals(100, metro.maxPassengers(bid1, bid2)); // Occupant constraint
        assertEquals(100, metro.maxPassengers(bid2, bid1)); // Reverse direction
    }

    @Test
    void testNoConnection() {
        // Create buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid3 = new BuildingID(3);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid3, 150)
        };

        // No tracks
        Track[] tracks = new Track[0];

        // Initialize McMetro
        McMetro metro = new McMetro(tracks, buildings);

        // Test maxPassengers
        assertEquals(0, metro.maxPassengers(bid1, bid3)); // No track exists
    }

    @Test
    void testCapacityConstraint() {
        // Create buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        Building[] buildings = new Building[]{
                new Building(bid1, 300),
                new Building(bid2, 400)
        };

        // Create tracks
        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 50, 150) // Capacity is 150
        };

        // Initialize McMetro
        McMetro metro = new McMetro(tracks, buildings);

        // Test maxPassengers
        assertEquals(150, metro.maxPassengers(bid1, bid2)); // Capacity constraint
    }

    @Test
    void testNonExistentBuilding() {
        // Create buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        Building[] buildings = new Building[]{
                new Building(bid1, 100)
        };

        // Create tracks
        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 50, 120)
        };

        // Initialize McMetro
        McMetro metro = new McMetro(tracks, buildings);

        // Test maxPassengers
        assertEquals(0, metro.maxPassengers(bid1, bid2)); // Building 2 does not exist
    }

    @Test
    void testNoTracks() {
        // Create buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        // No tracks
        Track[] tracks = new Track[0];

        // Initialize McMetro
        McMetro metro = new McMetro(tracks, buildings);

        // Test maxPassengers
        assertEquals(0, metro.maxPassengers(bid1, bid2)); // No tracks available
    }

    @Test
    void testMultipleTracks() {
        // Create buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 150)
        };

        // Create tracks
        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 50, 120),
                new Track(new TrackID(2), bid2, bid3, 60, 100)
        };

        // Initialize McMetro
        McMetro metro = new McMetro(tracks, buildings);

        // Test maxPassengers
        assertEquals(100, metro.maxPassengers(bid1, bid2)); // Track 1
        assertEquals(100, metro.maxPassengers(bid2, bid3)); // Track 2
        assertEquals(0, metro.maxPassengers(bid1, bid3)); // No direct track
    }

    void testMaxPassengers() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 50)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(50, maxPassengers);
    }

    @Test
    void testMaxPassengers2() {
        // random graph
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 400)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid2, bid3, 100, 50),
                new Track(new TrackID(3), bid3, bid4, 100, 100),
                new Track(new TrackID(4), bid1, bid3, 100, 25),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid4);
        assertEquals(75, maxPassengers);
    }

    @Test
    void testMaxPassengers3() {
        // graph with loop
        // random graph
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 400)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid2, bid3, 100, 50),
                new Track(new TrackID(3), bid3, bid4, 100, 100),
                new Track(new TrackID(4), bid3, bid2, 100, 25),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid4);
        assertEquals(50, maxPassengers);
    }

    @Test
    void testMaxPassengers4() {
        // test disconnected graph
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers5() {
        // test disconnected graph
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid2, bid3, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid3, bid1);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers6() {
        // test disconnected graph
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        Track[] tracks = new Track[]{};

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers7() {
        // weird loop
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 100),
                new Building(bid3, 100)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid2, bid3, 100, 100),
                new Track(new TrackID(3), bid3, bid1, 100, 100),

                // other loop
                new Track(new TrackID(4), bid2, bid1, 100, 100),
                new Track(new TrackID(5), bid3, bid2, 100, 100),
                new Track(new TrackID(6), bid1, bid3, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(200, maxPassengers);
    }

    @Test
    void testMaxPassengers8() {
        // only 1 building
        BuildingID bid1 = new BuildingID(1);
        Building[] buildings = new Building[]{
                new Building(bid1, 100)
        };

        Track[] tracks = new Track[]{};

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid1);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers9() {
        // only 1 building
        BuildingID bid1 = new BuildingID(1);
        Building[] buildings = new Building[]{
                new Building(bid1, 100)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid1, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid1);
        assertEquals(100, maxPassengers);
    }

    @Test
    void testMaxPassengers10() {
        // long path, but start building has 0 occupants
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);
        BuildingID bid7 = new BuildingID(7);
        BuildingID bid8 = new BuildingID(8);
        BuildingID bid9 = new BuildingID(9);

        Building[] buildings = new Building[]{
                new Building(bid1, 0),
                new Building(bid2, 100),
                new Building(bid3, 200),
                new Building(bid4, 300),
                new Building(bid5, 400),
                new Building(bid6, 500),
                new Building(bid7, 600),
                new Building(bid8, 700),
                new Building(bid9, 800)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid2, bid3, 100, 100),
                new Track(new TrackID(3), bid3, bid4, 100, 100),
                new Track(new TrackID(4), bid4, bid5, 100, 100),
                new Track(new TrackID(5), bid5, bid6, 100, 100),
                new Track(new TrackID(6), bid6, bid7, 100, 100),
                new Track(new TrackID(7), bid7, bid8, 100, 100),
                new Track(new TrackID(8), bid8, bid9, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid9);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers11() {
        // long path, but end building has 0 occupants
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);
        BuildingID bid7 = new BuildingID(7);
        BuildingID bid8 = new BuildingID(8);
        BuildingID bid9 = new BuildingID(9);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 400),
                new Building(bid5, 500),
                new Building(bid6, 600),
                new Building(bid7, 700),
                new Building(bid8, 800),
                new Building(bid9, 0)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid2, bid3, 100, 100),
                new Track(new TrackID(3), bid3, bid4, 100, 100),
                new Track(new TrackID(4), bid4, bid5, 100, 100),
                new Track(new TrackID(5), bid5, bid6, 100, 100),
                new Track(new TrackID(6), bid6, bid7, 100, 100),
                new Track(new TrackID(7), bid7, bid8, 100, 100),
                new Track(new TrackID(8), bid8, bid9, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid9);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers12() {
        // 6 buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 400),
                new Building(bid5, 500),
                new Building(bid6, 600)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid3, bid4, 100, 100),
                new Track(new TrackID(3), bid4, bid5, 100, 100),
                new Track(new TrackID(4), bid5, bid1, 100, 100),
                new Track(new TrackID(5), bid3, bid6, 100, 25),
                new Track(new TrackID(6), bid6, bid4, 100, 100),
                new Track(new TrackID(7), bid2, bid3, 100, 100),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid6);
        assertEquals(25, maxPassengers);
    }

    @Test
    void testMaxPassengers13() {
        // 6 buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 100),
                new Building(bid3, 100),
                new Building(bid4, 100),
                new Building(bid5, 100),
                new Building(bid6, 100)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid3, bid4, 100, 25),
                new Track(new TrackID(3), bid4, bid5, 100, 100),
                new Track(new TrackID(4), bid5, bid1, 100, 100),
                new Track(new TrackID(5), bid3, bid6, 100, 25),
                new Track(new TrackID(6), bid6, bid4, 100, 100),
                new Track(new TrackID(7), bid2, bid3, 100, 100),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid4);
        assertEquals(50, maxPassengers);
    }

    @Test
    void testMaxPassengers14() {
        // 6 buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 100),
                new Building(bid3, 100),
                new Building(bid4, 100),
                new Building(bid5, 100),
                new Building(bid6, 100)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid3, bid4, 100, 25),
                new Track(new TrackID(3), bid4, bid5, 100, 100),
                new Track(new TrackID(4), bid5, bid1, 100, 100),
                new Track(new TrackID(5), bid3, bid6, 100, 25),
                new Track(new TrackID(6), bid6, bid4, 100, 100),
                new Track(new TrackID(7), bid2, bid3, 100, 100),
                new Track(new TrackID(8), bid2, bid4, 100, 75),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid4);
        assertEquals(100, maxPassengers);
    }

    @Test
    void testMaxPassengers15() {
        // 6 buildings
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 100),
                new Building(bid3, 100),
                new Building(bid4, 100),
                new Building(bid5, 100),
                new Building(bid6, 100)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid3, bid4, 100, 25),
                new Track(new TrackID(3), bid4, bid5, 100, 100),
                new Track(new TrackID(4), bid5, bid1, 100, 100),
                new Track(new TrackID(5), bid3, bid6, 100, 25),
                new Track(new TrackID(6), bid6, bid4, 100, 100),
                new Track(new TrackID(7), bid2, bid3, 100, 100),
                new Track(new TrackID(8), bid2, bid4, 100, 75),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid4);
        assertEquals(100, maxPassengers);
    }

    @Test
    void testMaxPassengers16() {
        // multiple tracks from same building 1 to 2
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100),
                new Track(new TrackID(2), bid1, bid2, 100, 50),
                new Track(new TrackID(3), bid1, bid2, 100, 25),
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(175, maxPassengers);
    }

    @Test
    void testMaxPassengers_noPath() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 50)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers_noCapacity() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 0),
                new Track(new TrackID(2), bid2, bid3, 100, 50)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers_parallelPaths() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 30),
                new Track(new TrackID(2), bid2, bid3, 100, 50),
                new Track(new TrackID(3), bid1, bid3, 100, 40)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(70, maxPassengers);
    }

    @Test
    void testMaxPassengers_simplecyclePath() { // taken from https://brilliant.org/wiki/flow-network/
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 300),
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 2),
                new Track(new TrackID(2), bid1, bid3, 100, 4),
                new Track(new TrackID(3), bid2, bid3, 100, 3),
                new Track(new TrackID(4), bid3, bid2, 100, 3),
                new Track(new TrackID(5), bid2, bid4, 100, 4),
                new Track(new TrackID(6), bid3, bid4, 100, 3)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid4);
        assertEquals(6, maxPassengers);
    }

    @Test
    void testMaxPassengers_complexcyclePath() { // taken from https://brilliant.org/wiki/flow-network/
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 300),
                new Building(bid5, 300),
                new Building(bid6, 300),
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 16),
                new Track(new TrackID(2), bid1, bid3, 100, 13),
                new Track(new TrackID(3), bid2, bid3, 100, 10),
                new Track(new TrackID(4), bid3, bid2, 100, 4),
                new Track(new TrackID(5), bid2, bid4, 100, 12),
                new Track(new TrackID(6), bid3, bid5, 100, 14),
                new Track(new TrackID(7), bid4, bid3, 100, 9),
                new Track(new TrackID(8), bid5, bid4, 100, 7),
                new Track(new TrackID(9), bid4, bid6, 100, 20),
                new Track(new TrackID(10), bid5, bid6, 100, 4)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid6);
        assertEquals(23, maxPassengers);
    }

    @Test
    void testMaxPassengers_complexcyclePath2() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);
        BuildingID bid7 = new BuildingID(7);
        BuildingID bid8 = new BuildingID(8);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 300),
                new Building(bid5, 300),
                new Building(bid6, 300),
                new Building(bid7, 300),
                new Building(bid8, 300),
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 6),
                new Track(new TrackID(2), bid1, bid3, 100, 6),
                new Track(new TrackID(3), bid3, bid2, 100, 5),
                new Track(new TrackID(4), bid2, bid4, 100, 4),
                new Track(new TrackID(5), bid2, bid5, 100, 2),
                new Track(new TrackID(6), bid3, bid5, 100, 9),
                new Track(new TrackID(7), bid4, bid6, 100, 4),
                new Track(new TrackID(8), bid4, bid7, 100, 7),
                new Track(new TrackID(9), bid5, bid4, 100, 8),
                new Track(new TrackID(10), bid5, bid7, 100, 7),
                new Track(new TrackID(11), bid6, bid8, 100, 7),
                new Track(new TrackID(12), bid7, bid6, 100, 11),
                new Track(new TrackID(13), bid7, bid8, 100, 4)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid8);
        assertEquals(11, maxPassengers);
    }

    @Test
    void testBestMetro() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        TrackID tid = new TrackID(1);
        Track[] tracks = new Track[]{
                new Track(tid, bid1, bid2, 100, 50)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        TrackID[] expected = new TrackID[]{tid};
        assertArrayEquals(expected, bms);
    }
    @Test
    void testMaxPassengers_zeroCapacityTrack() {
        // Test track with zero capacity
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 100)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 0, 0) // Zero capacity track
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(0, maxPassengers);
    }

    @Test
    void testMaxPassengers_trackCapacityLessThanOccupants() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 500),
                new Building(bid2, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100) // Capacity less than occupants
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(100, maxPassengers); // Limited by track capacity
    }

    @Test
    void testMaxPassengers_zeroOccupantsBuildings() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 0),
                new Building(bid2, 0)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(0, maxPassengers); // No passengers can travel
    }

    @Test
    void testMaxPassengers_parallelTracks() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 300),
                new Building(bid2, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 50),
                new Track(new TrackID(2), bid1, bid2, 150, 100)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(150, maxPassengers); // Sum of parallel track flows
    }

    @Test
    void testMaxPassengers_noCapacityTrack() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 300),
                new Building(bid2, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 0, 0) // Zero capacity track
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(0, maxPassengers); // No flow possible
    }

    @Test
    void testMaxPassengers_disconnectedGraph() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 100, 50) // bid3 is disconnected
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid3);
        assertEquals(0, maxPassengers); // No path to bid3
    }

    @Test
    void testMaxPassengers_noTracks() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        Track[] tracks = new Track[]{}; // No tracks connecting any buildings

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(0, maxPassengers); // No tracks, so no path
    }


    @Test
    void testMaxPassengers_multipleTracksBetweenBuilding() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200)
        };

        Track[] tracks = new Track[]{
                new Track(new TrackID(1), bid1, bid2, 1, 30),
                new Track(new TrackID(2), bid1, bid2, 1, 40) // duplicate
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        int maxPassengers = mcMetro.maxPassengers(bid1, bid2);
        assertEquals(70, maxPassengers); //not sure if this is correct
    }











    @Test
    void testBestMetro2() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        TrackID tid = new TrackID(1);
        TrackID tid2 = new TrackID(2);
        TrackID tid3 = new TrackID(3);
        Track[] tracks = new Track[]{
                new Track(tid, bid1, bid2, 1, 100),
                new Track(tid2, bid2, bid3, 1, 200),
                new Track(tid3, bid1, bid3, 1, 300)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        for (TrackID trackID : bms) {
            System.out.println(trackID);
        }
        TrackID[][] expected = {
                new TrackID[]{tid2, tid3},
                new TrackID[]{tid3, tid2},
                new TrackID[]{tid2, tid},
                new TrackID[]{tid, tid2},
        };
        assertTrue(Arrays.equals(expected[0], bms) || Arrays.equals(expected[1], bms) || Arrays.equals(expected[2], bms) || Arrays.equals(expected[3], bms));
    }

    @Test
    void testBestMetro3() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300),
                new Building(bid4, 400)
        };

        TrackID tid = new TrackID(1);
        TrackID tid2 = new TrackID(2);
        TrackID tid3 = new TrackID(3);
        TrackID tid4 = new TrackID(4);
        Track[] tracks = new Track[]{
                new Track(tid, bid1, bid2, 1, 100),
                new Track(tid2, bid2, bid3, 1, 200),
                new Track(tid3, bid1, bid3, 1, 300),
                new Track(tid4, bid3, bid4, 1, 400)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        for (TrackID trackID : bms) {
            System.out.println(trackID);
        }
        TrackID[][] expected = {
                new TrackID[]{tid2, tid3, tid4},
                new TrackID[]{tid2, tid, tid4},
        };

        Arrays.sort(bms);
        Arrays.sort(expected[0]);
        Arrays.sort(expected[1]);

        // check if arrays are equal ignoring order
        assertTrue(Arrays.equals(expected[0], bms) || Arrays.equals(expected[1], bms));
    }

  /*   @Test
   void testBestMetro4() {
        // 3 triangle cycle
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);

        Building[] buildings = new Building[]{
                new Building(bid1, 100),
                new Building(bid2, 200),
                new Building(bid3, 300)
        };

        TrackID tid = new TrackID(1);
        TrackID tid2 = new TrackID(2);
        TrackID tid3 = new TrackID(3);

        Track[] tracks = new Track[]{
                new Track(tid, bid1, bid2, 1, 100),
                new Track(tid2, bid2, bid3, 1, 200),
                new Track(tid3, bid3, bid1, 1, 300)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        for (TrackID trackID : bms) {
            System.out.println(trackID);
        }
        TrackID[] expected = new TrackID[] {tid2, tid}; // if your printed output has the same tid, you are good
        assertArrayEquals(expected, bms);
    }*/

    @Test
    void testBestMetro5() {
        // only 1 building
        BuildingID bid1 = new BuildingID(1);
        Building[] buildings = new Building[]{
                new Building(bid1, 100)
        };

        Track[] tracks = new Track[]{};

        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        for (TrackID trackID : bms) {
            System.out.println(trackID);
        }
        TrackID[] expected = new TrackID[]{}; // if your printed output has the same tid, you are good
        assertArrayEquals(expected, bms);
    }

    @Test
    void testBestMetro6() {
        // multiple tracks from same building 1 to 2 with other buildnigs
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);

        Building[] buildings = new Building[]{
                new Building(bid1, 1000),
                new Building(bid2, 1000),
                new Building(bid3, 1000),
                new Building(bid4, 1000)
        };

        TrackID tid = new TrackID(1);
        TrackID tid2 = new TrackID(2);
        TrackID tid3 = new TrackID(3);
        TrackID tid4 = new TrackID(4);
        TrackID tid5 = new TrackID(5);
        TrackID tid6 = new TrackID(6);
        TrackID tid7 = new TrackID(7);
        TrackID tid8 = new TrackID(8);
        TrackID tid9 = new TrackID(9);
        TrackID tid10 = new TrackID(10);
        TrackID tid11 = new TrackID(11);
        TrackID tid12 = new TrackID(12);
        TrackID tid13 = new TrackID(13);
        TrackID tid14 = new TrackID(14);
        TrackID tid15 = new TrackID(15);
        TrackID tid16 = new TrackID(16);
        TrackID tid17 = new TrackID(17);
        TrackID tid18 = new TrackID(18);

        Track[] tracks = new Track[]{
                new Track(tid, bid1, bid2, 1, 100),
                new Track(tid2, bid1, bid2, 1, 50),
                new Track(tid3, bid1, bid2, 1, 25),
                new Track(tid4, bid1, bid3, 1, 100),
                new Track(tid5, bid1, bid3, 1, 50),
                new Track(tid6, bid1, bid3, 1, 25),
                new Track(tid7, bid1, bid4, 1, 100),
                new Track(tid8, bid1, bid4, 1, 50),
                new Track(tid9, bid1, bid4, 1, 25),
                new Track(tid10, bid2, bid3, 1, 50),
                new Track(tid11, bid2, bid3, 1, 50),
                new Track(tid12, bid2, bid3, 1, 25),
                new Track(tid13, bid2, bid4, 1, 50),
                new Track(tid14, bid2, bid4, 1, 50),
                new Track(tid15, bid2, bid4, 1, 25)
        };

        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        for (TrackID trackID : bms) {
            System.out.println(trackID);
        }
        TrackID[] expected = new TrackID[]{tid, tid4, tid7}; // if your printed output has the same tid, you are good
        assertArrayEquals(expected, bms);
    }

    @Test
    void testBestMetro7() {
        BuildingID bid1 = new BuildingID(1);
        BuildingID bid2 = new BuildingID(2);
        BuildingID bid3 = new BuildingID(3);
        BuildingID bid4 = new BuildingID(4);
        BuildingID bid5 = new BuildingID(5);
        BuildingID bid6 = new BuildingID(6);
        Building[] buildings = new Building[]{
                new Building(bid1, 1000),
                new Building(bid2, 2000),
                new Building(bid3, 3000),
                new Building(bid4, 1000),
                new Building(bid5, 2000),
                new Building(bid6, 3000),
        };

        TrackID tid1 = new TrackID(1);
        TrackID tid2 = new TrackID(2);
        TrackID tid3 = new TrackID(3);
        TrackID tid4 = new TrackID(4);
        TrackID tid5 = new TrackID(5);
        TrackID tid6 = new TrackID(6);
        TrackID tid7 = new TrackID(7);
        Track[] tracks = new Track[]{
                new Track(tid1, bid1, bid2, 100, 50),
                new Track(tid2, bid1, bid3, 100, 100),
                new Track(tid3, bid2, bid3, 100, 200),
                new Track(tid4, bid4, bid5, 100, 50),
                new Track(tid5, bid4, bid6, 100, 100),
                new Track(tid6, bid5, bid6, 100, 200),
                new Track(tid7, bid5, bid2, 100, 300),
        };
        McMetro mcMetro = new McMetro(tracks, buildings);
        TrackID[] bms = mcMetro.bestMetroSystem();
        for (TrackID trackID : bms) {
            System.out.println(trackID);
        }
        TrackID[] expected = new TrackID[]{tid7, tid6, tid3, tid5, tid2}; // if your printed output has the same tid, you are good
        assertArrayEquals(expected, bms);
    }

    /*@Test
    void testSearchForPassengers() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {"Alex", "Bob", "Ally"};
        String[] expected = {"Alex", "Ally"};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("al");

        assertArrayEquals(expected, found.toArray(new String[0]));
    }*/

    @Test
    void testSearchForPassengers() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]); // Create instance
        String[] passengers = {"Alex", "Bob", "Ally"};               // Input passengers
        String[] expected = {"Alex", "Ally"};                        // Expected result
        mcMetro.addPassengers(passengers);                           // Add passengers to instance

        // Call the instance method
        ArrayList<String> found = mcMetro.searchForPassengers("al");

        // Assert the results match the expected output
        assertArrayEquals(expected, found.toArray(new String[0]));
    }

    @Test
    void testSearchForPassengers2() {
        McMetro mcMetro = new McMetro(null, null);
        String[] passengers = {"Alex", "Bob", "Ally"};
        String[] expected = {"Alex", "Ally"};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("al");

        assertArrayEquals(expected, found.toArray(new String[0]));
    }

    @Test
    void testSearchForPassengers3() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis"};
        String[] expected = {"Al", "Alex", "Ally"};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("al");

        //printing arrays to compare
        String actualResult ="";
        for(int i=0; i<found.size(); i++){
            actualResult += found.get(i)+", ";
        }
        String expectedResult ="";
        for(int i=0; i<expected.length; i++){
            expectedResult += expected[i]+", ";
        }
        System.out.println("Extepted: " + expectedResult);
        System.out.println("Actual: " + actualResult);

        assertArrayEquals(expected, found.toArray(new String[0]));
    }
    @Test
    void testSearchForPassengers4() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis",};
        String[] expected = {"Bob", "Bobbert", "Bobby"};
        mcMetro.addPassengers(passengers);
        String[] found = mcMetro.searchForPassengers("bob").toArray(new String[0]);
        Arrays.sort(found);
        Arrays.sort(expected);

        // check if arrays are equal ignoring order
        assertTrue(Arrays.equals(expected, found));

    }
    @Test
    void testSearchForPassengers5() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis",};
        String[] expected = {"Davis", "David", "Davie"};
        mcMetro.addPassengers(passengers);

        String[] found = mcMetro.searchForPassengers("dav").toArray(new String[0]);
        Arrays.sort(found);
        Arrays.sort(expected);

        // check if arrays are equal ignoring order
        assertTrue(Arrays.equals(expected, found));
    }
    @Test
    void testSearchForPassengers6() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis",};
        String[] expected = {};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("col");

        //printing arrays to compare
        String actualResult ="";
        for(int i=0; i<found.size(); i++){
            actualResult += found.get(i)+", ";
        }
        String expectedResult ="";
        for(int i=0; i<expected.length; i++){
            expectedResult += expected[i]+", ";
        }
        System.out.println("Extepted: " + expectedResult);
        System.out.println("Actual: " + actualResult);

        assertArrayEquals(expected, found.toArray(new String[0]));
    }

    @Test
    void testHireTicketCheckers() {
        int[][] schedule = new int[4][2];
        schedule[0][0] = 1;
        schedule[0][1] = 2;
        schedule[1][0] = 2;
        schedule[1][1] = 3;
        schedule[2][0] = 3;
        schedule[2][1] = 4;
        schedule[3][0] = 1;
        schedule[3][1] = 3;

        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(3, toHire);
    }

    @Test
    void testHireTicketCheckers2() {
        int[][] schedule = new int[7][2];
        schedule[0][0] = 0;
        schedule[0][1] = 2;
        schedule[1][0] = 1;
        schedule[1][1] = 3;
        schedule[2][0] = 2;
        schedule[2][1] = 5;
        schedule[3][0] = 4;
        schedule[3][1] = 6;
        schedule[4][0] = 5;
        schedule[4][1] = 9;
        schedule[5][0] = 6;
        schedule[5][1] = 9;
        schedule[6][0] = 8;
        schedule[6][1] = 10;

        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(3, toHire);
    }

    @Test
    void testHireTicketCheckers3() {
        int[][] schedule = new int[7][2];
        schedule[0][0] = -1000;
        schedule[0][1] = 20000;
        schedule[1][0] = 1;
        schedule[1][1] = 3;
        schedule[2][0] = 2;
        schedule[2][1] = 5;
        schedule[3][0] = 4;
        schedule[3][1] = 6;
        schedule[4][0] = 5;
        schedule[4][1] = 9;
        schedule[5][0] = 6;
        schedule[5][1] = 9;
        schedule[6][0] = 8;
        schedule[6][1] = 10;

        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(3, toHire);
    }

    @Test
    void testHireTicketCheckers4() {
        int[][] schedule = new int[4][2];
        schedule[0][0] = 1;
        schedule[0][1] = 1;
        schedule[1][0] = 1;
        schedule[1][1] = 1;
        schedule[2][0] = 1;
        schedule[2][1] = 1;
        schedule[3][0] = 1;
        schedule[3][1] = 1;

        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(4, toHire);
    }

    @Test
    void testHireTicketCheckers_SingleInterval() {
        int[][] schedule = {{5, 10}};
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(1, toHire);
    }

    @Test
    void testHireTicketCheckers_NoIntervals() {
        int[][] schedule = {};
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(0, toHire);
    }

    @Test
    void testHireTicketCheckers_AllFullyOverlapping() {
        int[][] schedule = {
                {1, 10},
                {2, 9},
                {3, 8},
                {4, 7}
        };
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(1, toHire);
    }

    @Test
    void testHireTicketCheckers_NegativeIntervals() {
        int[][] schedule = {
                {-10, -5},
                {-8, -3},
                {-7, 0},
                {-4, -1}
        };
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(2, toHire);
    }

    @Test
    void testHireTicketCheckers_NonOverlappingBackToBack() {
        int[][] schedule = {
                {1, 2},
                {2, 3},
                {3, 4},
                {4, 5}
        };
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(4, toHire);
    }

    @Test
    void testHireTicketCheckers_SameIntervals() {
        int[][] schedule = {
                {5, 5},
                {5, 5},
                {5, 5},
                {5, 5}
        };
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(4, toHire);
    }











}