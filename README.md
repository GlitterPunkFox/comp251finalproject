

## Overview
Final Project for COMP 251 at McGill FALL 2024

- **Graph Representation**: Uses adjacency lists to store metro buildings and tracks.
- **Maximum Flow Calculation**: Implements the **Ford-Fulkerson algorithm** to determine the maximum number of passengers that can travel between two buildings.
- **Best Metro System Selection**: Uses **Kruskal’s Algorithm** to find an optimal subset of tracks that maximizes capacity while considering costs.
- **Passenger Management**: Uses a **Trie data structure** to efficiently store and search passenger names based on prefixes.
- **Ticket Checker Hiring**: Implements an **interval scheduling algorithm** to determine the minimum number of ticket checkers required.

## **Data Structures & Algorithms Used**
- **Graph Representation**: `HashMap<BuildingID, List<Edge>>` for adjacency list.
- **Ford-Fulkerson Algorithm**: To compute max passenger flow between buildings.
- **Priority Queue & Kruskal’s Algorithm**: To find the best metro system.
- **Trie (Prefix Tree)**: For fast passenger name search.
- **Greedy Interval Scheduling**: To minimize ticket checkers required.

## **How It Works**
### **1. Metro System Setup**
- Buildings and tracks are stored in `buildingTable` and `tracks` arrays.
- An **adjacency list** is created to store track connections and capacities.
- Track capacities are determined based on **minimum available space** in connected buildings.

### **2. Maximum Passenger Flow Calculation**
- Uses **Ford-Fulkerson Algorithm** with **BFS-based augmentation**.
- Updates residual capacities dynamically to determine the **maximum number of passengers** that can be transported.

### **3. Selecting the Best Metro System**
- Uses **Kruskal’s Algorithm** with a **priority queue** to select tracks based on **capacity-to-cost ratio**.
- Implements **Disjoint Set Union (DSU)** to ensure a **minimum spanning tree** (MST).

### **4. Passenger Management Using Trie**
- Passenger names are stored in a **Trie** for fast prefix-based lookup.
- Names are case-insensitive and stored in **capitalized format**.

### **5. Hiring Ticket Checkers**
- Uses **greedy interval scheduling** to find the minimum number of ticket checkers needed.
- Sorts ticket checking times by **end time** and assigns checkers efficiently.
