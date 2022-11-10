package model.collection;

import model.Animal;
import model.AnimalException;
import model.collection.CollectionType;

import java.io.*;
import java.util.*;

public class GroupOfAnimals implements Iterable<Animal>, Serializable {

    private Collection<Animal> collection;
    private CollectionType collectionType;
    private String name;

    public GroupOfAnimals(CollectionType collectionType, String name) throws AnimalException {
        setName(name);
        if (collectionType == null) {
            throw new AnimalException("Typ kolekcji nie może być pusty");
        }
        this.collectionType = collectionType;
        this.collection = collectionType.getCollection();

    }

    public GroupOfAnimals(String collectionTypeDescription, String name) throws AnimalException {
        setName(name);
        if (collectionTypeDescription == null) {
            throw new AnimalException("Typ kolekcji nie może być pusty");
        }
    }

    @Override
    public Iterator<Animal> iterator() {
        return collection.iterator();
    }

    public boolean remove(Animal animal) {
        return collection.remove(animal);
    }

    public int size() {
        return collection.size();
    }

    public void add(Animal animal) {
        collection.add(animal);
    }

    public Optional<Animal> find(Animal animal) {
        Iterator<Animal> iterator = iterator();
        while (iterator.hasNext()) {
            Animal nextAnimal = iterator.next();
            if (animal.equals(nextAnimal)) {
                return Optional.of(nextAnimal);
            }
        }
        return Optional.empty();
    }

    public static void writeGroupOfAnimalsToFile(GroupOfAnimals group, File file) throws AnimalException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(group);
        } catch (FileNotFoundException e) {
            throw new AnimalException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new AnimalException("Błąd w zapisie do pliku");
        }
    }

    public static GroupOfAnimals readGroupOfAnimalsFromFile(File file) throws AnimalException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (GroupOfAnimals) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new AnimalException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new AnimalException("Błąd w zapisie do pliku");
        } catch (ClassNotFoundException e) {
            throw new AnimalException("Niepoprawny typ danych");
        }
    }

    public static void writeListOfGroupsToFile(List<GroupOfAnimals> list, File file) throws AnimalException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (FileNotFoundException e) {
            throw new AnimalException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new AnimalException("Błąd w zapisie do pliku");
        }
    }

    public static List<GroupOfAnimals> readListOfGroupsToFile(File file) throws AnimalException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<GroupOfAnimals>) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new AnimalException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new AnimalException("Błąd w zapisie do pliku");
        } catch (ClassNotFoundException e) {
            throw new AnimalException("Niepoprawny typ danych");
        }
    }
    public void sortByName() throws AnimalException {
        if(!isSortable()) {
            throw new AnimalException("Kolekcji " + collectionType + " nie można sortować.");
        }
         Collections.sort((List<Animal>) collection, Comparator.comparing(Animal::getName));
    }

    public void sortByAge() throws AnimalException {
        if (!isSortable()) {
            throw new AnimalException("Kolekcji " + collectionType + " nie można sortować.");
        }
        Collections.sort((List<Animal>) collection, Comparator.comparingInt(Animal::getAge));
    }

    public void sortByWeight() throws AnimalException {
        if (!isSortable()) {
            throw new AnimalException("Kolekcji " + collectionType + " nie można sortować.");
        }
        Collections.sort((List<Animal>) collection, Comparator.comparingDouble(Animal::getWeight));
    }

    private boolean isSortable() {
        return collectionType != CollectionType.HASH_SET && collectionType != CollectionType.TREE_SET;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollection(CollectionType collectionType) throws AnimalException {
        if (collectionType == null) {
            throw new AnimalException("Typ kolekcji nie może być pusty");
        }
        if (this.collectionType != collectionType) {
            convertCollection(collectionType);
        }
    }

    public void setCollection(String collectionTypeDescription) throws AnimalException {
        CollectionType collectionType = CollectionType.ofDescription(collectionTypeDescription);
        setCollection(collectionType);
    }

    private void convertCollection(CollectionType collectionType) throws AnimalException {
        Collection<Animal> oldCollection = this.collection;
        Collection<Animal> newCollection = collectionType.getCollection();
        Iterator<Animal> iterator = oldCollection.iterator();
        while (iterator.hasNext()) {
            newCollection.add(iterator.next());
        }
        this.collection = newCollection;
        this.collectionType = collectionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws AnimalException {
        if (name == null || name.trim().equals("")) {
            throw new AnimalException("Nazwa grupy nie może być pusta");
        }
        this.name = name;
    }

    public static GroupOfAnimals createOrGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " OR " + group2.name;
        GroupOfAnimals group = new GroupOfAnimals(type, name);
        group.collection.addAll(group1.collection);
        group.collection.addAll(group2.collection);
        return group;
    }

    public static GroupOfAnimals createAndGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " AND " + group2.name;
        GroupOfAnimals group = new GroupOfAnimals(type, name);
        Collection<Animal> tmpCollection = group.getCollectionType().getCollection();
        tmpCollection.addAll(group1.collection);
        for (Animal animal : group2.collection) {
            if (tmpCollection.remove(animal)) {
                group.collection.add(animal);
            }
        }
        return group;
    }

    public static GroupOfAnimals createDiffGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " SUB " + group2.name;
        GroupOfAnimals group = new GroupOfAnimals(type, name);
        group.collection.addAll(group1.collection);
        for (Animal animal : group2.collection) {
            group.remove(animal);
        }
        return group;
    }

    public static GroupOfAnimals createXorGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " XOR " + group2.name;
        GroupOfAnimals animals = new GroupOfAnimals(type, name);
        Collection<Animal> group1DiffGroup2 = createDiffGroup(group1, group2).collection;
        Collection<Animal> group2DiffGroup1 = createDiffGroup(group2, group1).collection;
        animals.collection.addAll(group2DiffGroup1);
        animals.collection.addAll(group1DiffGroup2);
        return animals;
    }

    private static CollectionType getGroupTypeFromTwo(GroupOfAnimals group1, GroupOfAnimals group2) {
        if (group2 instanceof Set<?> && !(group1 instanceof Set<?>)) {
            return group2.getCollectionType();
        } else {
            return group1.getCollectionType();
        }
    }
}
