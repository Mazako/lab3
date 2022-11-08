/*
 * Program: klasa ktora jest wyjatkiem obslugiwanym dotyczacym klasy Animal, ktory jest wywolywany gdy jakas operacja w
 *          klasie Animal pojdzie nie tak
 *    Plik: AnimalException.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: pazdziernik 2022 r.
 */
package model;

public class AnimalException extends Exception {

    private static final Long serialVersionUID = 1L;

    public AnimalException() {
    }

    public AnimalException(String message) {
        super(message);
    }
}
