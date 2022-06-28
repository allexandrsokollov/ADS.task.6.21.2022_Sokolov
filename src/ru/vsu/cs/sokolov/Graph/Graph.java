package ru.vsu.cs.sokolov.Graph;

import java.util.*;

/*
graph {
    2 -- { 3 4 }
    3 -- 6 -- { 2 4 }
}
 */
public class Graph {

    private final Map<Vertex, ArrayList<Vertex>> graph;

    public Graph(String strGraph) throws GraphException {
        this.graph = new LinkedHashMap<>();
        this.stringToGraph(strGraph);
    }

    private void stringToGraph(String stringGraph) throws GraphException {

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
        } else {
            System.out.println("This Vertex already exist in graph!");
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
