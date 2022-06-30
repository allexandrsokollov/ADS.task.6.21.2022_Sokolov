package ru.vsu.cs.sokolov.Graph;

import java.util.*;

/* graph example
graph {
    0 -- { 1 2 }
    1 -- { 3 4 }
    2 -- { 5 6 }
}
* */
public class Graph {

    private final Map<Vertex, ArrayList<Vertex>> graph;

    public Graph(String strGraph) {
        this.graph = new LinkedHashMap<>();
        this.stringToGraph(strGraph);
    }


    @Override
    public String toString() {
        StringBuilder stringGraph = new StringBuilder();
        stringGraph.append("graph {\n");

        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : graph.entrySet()) {
            ArrayList<Vertex> temp = graph.get(entry.getKey());
            stringGraph.append("    ").append(entry.getKey()).append(" -- { ");

            for (Vertex vertex : temp) {
                stringGraph.append(vertex).append(" ");
            }

            stringGraph.append("}\n");
        }

        stringGraph.append("}");

        return stringGraph.toString();
    }
    public void divideGraph(int t) {
        int minAmountInSubgraph = graph.size() / (t + 1);

        HashSet<Vertex> visited = null;

        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : graph.entrySet()) {
            visited = new HashSet<>(getNNearestVertexes(entry.getKey(), minAmountInSubgraph));
            if (isSecondGraphConnected(visited)) {
                break;
            }
        }

        deleteEdges(visited);
    }

    private boolean isSecondGraphConnected(HashSet<Vertex> visited) {
        ArrayList<Vertex> secondGraphVertexes = new ArrayList<>();

        for (Vertex vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                secondGraphVertexes.add(vertex);
            }
        }

        ArrayList<Vertex> reachedVertexes = getConnectedVertexesFromSecondGraph(secondGraphVertexes, visited);

        for (Vertex vertex : secondGraphVertexes) {
            if (!reachedVertexes.contains(vertex)) {
                System.out.println("IMPOSSIBLE TO DIVIDE THIS WAY!");
                return false;
            }
        }

        return true;
    }

    private ArrayList<Vertex> getConnectedVertexesFromSecondGraph(ArrayList<Vertex> secondGraphVertexes, HashSet<Vertex> visited) {
        HashSet<Vertex> vstd = new HashSet<>();
        LinkedList<Vertex> queue = new LinkedList<>();

        Vertex startingVertex = secondGraphVertexes.get(0);

        vstd.add(startingVertex);
        queue.add(startingVertex);

        Vertex currentVertex;

        while (!queue.isEmpty()) {
            currentVertex = queue.poll();

            ArrayList<Vertex> currentVertexEdges = graph.get(currentVertex);

            for (Vertex vertex : currentVertexEdges) {
                if (!vstd.contains(vertex) && !visited.contains(vertex)) {
                    vstd.add(vertex);
                    queue.add(vertex);
                }
            }
        }

        return new ArrayList<>(vstd);
    }

    private void deleteEdges(HashSet<Vertex> visited) {
        for (Vertex vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                for (Vertex visVert : visited) {
                    removeEdge(visVert, vertex);
                }
            }
        }
    }

    private ArrayList<Vertex> getNNearestVertexes(Vertex startingVertex, int n) {
        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<Vertex> queue = new LinkedList<>();

        if (n % 2 == 1) {
            n++;
        }

        visited.add(startingVertex);
        queue.add(startingVertex);

        Vertex currentVertex;

        while (!queue.isEmpty() || visited.size() < n) {
            currentVertex = queue.poll();

            ArrayList<Vertex> currentVertexEdges = graph.get(currentVertex);

            for (Vertex vertex : currentVertexEdges) {
                if (!visited.contains(vertex)) {
                    visited.add(vertex);
                    queue.add(vertex);
                }
                if (visited.size() >= n) {
                    break;
                }
            }
        }

        return new ArrayList<>(visited);
    }

    private void stringToGraph(String stringGraph)  {

        Scanner scanner = new Scanner(stringGraph);
        ArrayList<String> graphStrings = new ArrayList<>();

        while (scanner.hasNextLine()) {
            graphStrings.add(scanner.nextLine());
        }

        graphStrings.remove(0);
        graphStrings.remove(graphStrings.size() - 1);

        for (String str : graphStrings) {
            scanner = new Scanner(str);

            Vertex tempVertex = null;

            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    tempVertex = new Vertex(scanner.nextInt());
                    this.addVertex(tempVertex);

                } else {
                    String scannerElem = scanner.next();

                    if (scannerElem.equals("--") && scanner.hasNextInt()) {

                        Vertex temp = new Vertex(scanner.nextInt());
                        this.addVertex(temp);

                        if (tempVertex != null) {
                            this.addEdge(new Vertex(tempVertex.getName()), temp);
                            tempVertex = temp;
                        }

                    } else {
                        ArrayList<Vertex> tempVertexesInBraces = new ArrayList<>();

                        while (scanner.hasNextInt()) {
                            Vertex tempV = new Vertex(scanner.nextInt());
                            this.addVertex(tempV);
                            tempVertexesInBraces.add(tempV);
                        }

                        if (tempVertex != null) {
                            for (Vertex v : tempVertexesInBraces) {
                                this.addEdge(tempVertex, v);
                            }
                        }

                    }
                }
            }
        }
    }

    public void addVertex(Vertex vertex)  {
        if (!graph.containsKey(vertex)) {
            graph.put(vertex, new ArrayList<>());
        }
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        graph.get(vertex1).add(vertex2);
        graph.get(vertex2).add(vertex1);
    }

    public void removeEdge(Vertex vertex1, Vertex vertex2) {
        List<Vertex> vertList1 = getAdjVertexes(vertex1);
        List<Vertex> vertList2 = getAdjVertexes(vertex2);

        if (vertList1 != null) {
            vertList1.remove(vertex2);
        }

        if (vertList2 != null) {
            vertList2.remove(vertex1);
        }
    }

    ArrayList<Vertex> getAdjVertexes(Vertex vertex) {
        return graph.get(vertex);
    }
}
