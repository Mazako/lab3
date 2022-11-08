/*
 * Program: Klasa reprezentujaca zwierze, zawierajaca jego podstawowe parametry, konstruktor, gettery i settery ale
 *          rowniez statyczne metody umozliwiajacy odczyt z pliku i zapis do pliku na wskazana sciezke
 *    Plik: Animaljava
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: pazdziernik 2022 r.
 */
package model;

import java.io.*;

public class Animal implements Cloneable, Serializable {
    private String name;
    private String type;
    private int age;
    private double weight;
    private Species species;

    public Animal(String name, String type, int age, double weight, Species species) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.weight = weight;
        this.species = species;
    }

    public Animal() {
    }

    public static void writeAnimalToFile(String filePath, Animal animal) throws AnimalException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(convertAnimalToCsv(animal));
        } catch (IOException e) {
            throw new AnimalException("Unable to write animal to file");
        }
    }

    public static Animal readAnimalFromFile(String filePath) throws AnimalException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            return convertCsvToAnimal(line);
        } catch (FileNotFoundException e) {
            throw new AnimalException("file " + filePath + " not found");
        } catch (IOException e) {
            throw new AnimalException("file reading error");
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new AnimalException("Incompatible file type");
        }
    }

    private static Animal convertCsvToAnimal(String line) throws AnimalException {
        String[] split = line.split(";");
        String name = split[0];
        String type = split[1];
        int age = Integer.parseInt(split[2]);
        double weight = Double.parseDouble(split[3]);
        Species species = Species.fromDescription(split[4]);
        Animal animal = new Animal();
        animal.setName(name);
        animal.setType(type);
        animal.setAge(age);
        animal.setWeight(weight);
        animal.setSpecies(species);
        return animal;
    }

    private static String convertAnimalToCsv(Animal animal) {
        return animal.name +
                ";" +
                animal.type +
                ";" +
                animal.age +
                ";" +
                animal.weight +
                ";" +
                animal.species.getDescription();
    }

    public static void writeAnimalToBinary(String filename, Animal animal) throws AnimalException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(animal);
        } catch (FileNotFoundException e) {
            throw new AnimalException("file " + filename + " not found");
        } catch (IOException e) {
            throw new AnimalException("File saving error");
        }
    }

    public static Animal readAnimalFromBinary(String filename) throws AnimalException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Animal) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new AnimalException("file " + filename + " not found");
        } catch (IOException e) {
            throw new AnimalException("File reading error");
        } catch (ClassNotFoundException e) {
            throw new AnimalException("Class not found");
        } catch (ClassCastException e) {
            throw new AnimalException("Incompatible class types");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws AnimalException {
        if (name == null || name.isBlank()) {
            throw new AnimalException("name cannot be blank or null");
        }
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws AnimalException {
        if (age < 0) {
            throw new AnimalException("Age must be greater than or equal 0");
        }
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) throws AnimalException {
        if (weight <= 0) {
            throw new AnimalException("Weight must be greater than 0");
        }
        this.weight = weight;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "imie='" + name + '\'' +
                ", rodzaj='" + type + '\'' +
                ", wiek=" + age +
                ", waga=" + weight +
                ", gatunek=" + species +
                '}';
    }

    @Override
    public Animal clone() {
        try {
            return (Animal) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}