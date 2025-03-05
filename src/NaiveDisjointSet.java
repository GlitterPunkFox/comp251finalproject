import java.util.*;

public class NaiveDisjointSet<T> {

    //hashmap given- key is the element value is the parent of the element
    HashMap<T, T> parentMap = new HashMap<>();
    //this is the rank of every integer
    HashMap<T, Integer> rank = new HashMap<>();

    //element initialized as their own parents
    //editing to also put rank of each element into rank map
    void add(T element) {
        parentMap.put(element, element);
        rank.put(element, 0); //initially rank is 0
    }

    // TODO: Implement path compression
    //copy paste from lecture 10 slide 38
    T find(T a) {
        T node = parentMap.get(a);
        if (node.equals(a)) { //if a is its on parent return a
            return node;
        } else {
            parentMap.put(a, find(parentMap.get(a)));
            return parentMap.get(a);//return parent
        }
    }

    // TODO: Implement union by size or union by rank
    //https://www.geeksforgeeks.org/introduction-to-disjoint-set-data-structure-or-union-find-algorithm/
    void union(T a, T b) {
        T aRoot = find(a); // root of a
        T bRoot = find(b); // root of b

        // If the elements are already in the same set, no union is needed
        if (aRoot.equals(bRoot)) {
            return;
        }

        // smaller rank tree will attach to larger rank tree
        if (rank.get(bRoot) < rank.get(aRoot)) {
            parentMap.put(bRoot, aRoot); // aRoot is parent to bRoot
        } else if (rank.get(bRoot) > rank.get(aRoot)) {
            parentMap.put(aRoot, bRoot); // bRoot is parent to aRoot
        } else { //if ranks are the same
            // I chose to move bRoot under aRoot- tbh doesn't really matter
            parentMap.put(bRoot, aRoot);
            rank.put(aRoot, rank.get(aRoot) + 1); //rank of this tree is 1 bigger
        }
    }
}
