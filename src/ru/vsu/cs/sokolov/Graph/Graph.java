package ru.vsu.cs.sokolov.Graph;

import java.util.*;

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
            stringGraph.append(entry.getKey()).append(" -- { ");

            for (Vertex vertex : temp) {
                stringGraph.append(vertex).append(" ");
            }

            stringGraph.append("}\n");
        }

        stringGraph.append("}");

        return stringGraph.toString();
    }
    public void divideGraph(int t) {
        t = 1;
        int minAmountInSubgraph = graph.size() / t;

        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<Vertex> queue = new LinkedList<>();

        Vertex temp;
        int i = 0;

        for (Map.Entry<Vertex, ArrayList<Vertex>> v : graph.entrySet()) {
            temp = v.getKey();
            ArrayList<Vertex> edges = graph.get(temp);

            visited.add(temp);
            i++;

            for (Vertex ed : edges) {
                if (!visited.contains(ed) && !edges.contains(ed)) {
                    queue.add(ed);
                }
            }

            while (i < 3) {

                System.out.println();
                System.out.println(edges);
                System.out.println(visited);
                System.out.println();


                temp = queue.poll();
                edges = graph.get(temp);

                visited.add(temp);
                i++;


                if (edges != null) {

                    for (Vertex ed : edges) {
                        if (!visited.contains(ed) && !edges.contains(ed)) {
                            queue.add(ed);
                        }
                    }
                }

                System.out.println();
                System.out.println(edges);
                System.out.println(visited);
                System.out.println();
            }
            break;
        }

//        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : graph.entrySet()) {
//            int amountVertInNew;
//            visited.put(entry.getKey(), true);
//            amountVertInNew = 1;
//
//            queue.addAll(graph.get(entry.getKey()));
//
//            while (!queue.isEmpty()) {
//                visited.put(queue.get(0), true);
//
//                for (Vertex toAdd : graph.get(queue.get(0))) {
//                    if (!visited.containsKey(toAdd) && !queue.contains(toAdd)) {
//                        queue.add(toAdd);
//                    }
//                }
//
//                queue.remove(0);
//                amountVertInNew++;
//
//
//                if (amountVertInNew == minAmountInSubgraph) {
//                    break;
//                }
//            }
//        }

        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : graph.entrySet()) {

            if (!visited.contains(entry.getKey())) {
               graph.get(entry.getKey()).removeIf(visited::contains);

            } else {

                //graph.get(entry.getKey()).removeIf(visited::contains);
            }
        }
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
