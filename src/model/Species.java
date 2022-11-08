/*
 * Program: Typ wyliczeniowy Species zawierajacy informacje o gatunkach zwierzat
 *    Plik: Species.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: pazdziernik 2022 r.
 */
package model;

import java.util.Arrays;

public enum Species {
    MAMMAL("ssak"),
    FISH("ryba"),
    BIRD("ptak"),
    REPTILE("gad"),
    UNKNOWN("nieznany");


    private final String description;

    private Species(String description) {
        this.description = description;
    }

    public static Species fromDescription(String description) {
        for (Species value : Species.values()) {
            if (value.description.equals(description)) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static Species[] getSpeciesArrayWithoutUnknown() {
        return Arrays.stream(Species.values())
                .filter(x -> x != Species.UNKNOWN)
                .toArray(Species[]::new);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
