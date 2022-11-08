/*
 * Program: Klasa przechowująca kolekcje zwierzat
 *    Plik: AnimalList.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: pazdziernik 2022 r.
 */


package model;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class AnimalList implements Serializable {

    private static final Long serialVersionUID = 1L;

    private final Vector<Animal> animals = new Vector<>();

    public static void writeToBinary(String filename, AnimalList animalList) throws AnimalException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(animalList);
        } catch (FileNotFoundException e) {
            throw new AnimalException("File " + filename + " not found");
        } catch (IOException e) {
            throw new AnimalException("Object write fatal error");
        }
    }

    public static AnimalList readFromBinary(String filename) throws AnimalException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (AnimalList) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new AnimalException("File " + filename + " not found");
        } catch (IOException e) {
            throw new AnimalException("Object write fatal error");
        } catch (ClassNotFoundException e) {
            throw new AnimalException("Class not found");
        } catch (ClassCastException e) {
            throw new AnimalException("Incompatible class types");
        }
    }

    public void add(Animal a) {
        animals.add(a);
    }

    public Animal get(int a) {
        return animals.get(a);
    }

    public void replace(int index, Animal animal) throws AnimalException {
        Animal currentAnimal = animals.get(index);
        currentAnimal.setName(animal.getName());
        currentAnimal.setType(animal.getType());
        currentAnimal.setAge(animal.getAge());
        currentAnimal.setWeight(animal.getWeight());
        currentAnimal.setSpecies(animal.getSpecies());
    }

    public void remove(int a) {
        animals.remove(a);
    }

    public Vector<Animal> getAnimals() {
        return new Vector<>(animals);
    }

    public void sort(Comparator<Animal> comparator) {
        Collections.sort(animals, comparator);
    }

    public boolean isEmpty() {
        return animals.isEmpty();
    }
}
