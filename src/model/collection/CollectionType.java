/*
 * Program: Enum odpowiadający za reprezentowanie typu kolekcji
 *
 *    Plik: CollectionType.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package model.collection;

import model.Animal;
import model.AnimalException;

import java.util.*;

public enum CollectionType {
    VECTOR("Klasa Vector"),
    ARRAY_LIST("Klasa ArrayList"),
    LINKED_LIST("Klasa LinkedList"),
    HASH_SET("Klasa HashSet"),
    TREE_SET("Klasa TreeSet");

    private String description;

    private CollectionType(String description) {
        this.description = description;
    }

    public static CollectionType ofDescription(String description) throws AnimalException {
        for (CollectionType value : values()) {
            if (value.description.equals(description)) {
                return value;
            }
        }
        throw new AnimalException("Niepoprawny typ kolekcji");
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return description;
    }

    public Collection<Animal> getCollection() throws AnimalException{
        return switch (this) {
            case VECTOR -> new Vector<>();
            case HASH_SET -> new HashSet<>();
            case TREE_SET -> new TreeSet<>();
            case ARRAY_LIST -> new ArrayList<>();
            case LINKED_LIST -> new LinkedList<>();
        };
    }

}
