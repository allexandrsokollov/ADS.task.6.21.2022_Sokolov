package ru.vsu.cs.sokolov.Graph;

import java.util.Objects;

public class Vertex {
    private final int name;

    public Vertex(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }

    @Override
    public String toString() {
        return Integer.toString(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return name == vertex.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
