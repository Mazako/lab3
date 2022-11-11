package model.collection;

import model.Animal;
import model.AnimalException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

public class GroupOfAnimals implements Iterable<Animal>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Collection<Animal> collection;
    protected CollectionType collectionType;
    protected String name;

    private final PropertyChangeSupport observable = new PropertyChangeSupport(this);

    public GroupOfAnimals(CollectionType collectionType, String name) throws AnimalException {
        setName(name);
        if (collectionType == null) {
            throw new AnimalException("Typ kolekcji nie może być pusty");
        }
        this.collectionType = collectionType;
        this.collection = collectionType.getCollection();

    }

    @Override
    public String toString() {
        return name + " [" + collectionType + "]";
    }

    @Override
    public Iterator<Animal> iterator() {
        return collection.iterator();
    }

    public boolean remove(Animal animal) {
        boolean isRemoved = collection.remove(animal);
        observable.firePropertyChange("size", size(), size() - 1);
        return isRemoved;
    }

    public int size() {
        return collection.size();
    }

    public void add(Animal animal) {
        collection.add(animal);
        observable.firePropertyChange("size", size(), size() + 1);
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


    private void convertCollection(CollectionType collectionType) throws AnimalException {
        Collection<Animal> oldCollection = this.collection;
        Collection<Animal> newCollection = collectionType.getCollection();
        newCollection.addAll(oldCollection);
        CollectionType oldCollectionType = this.collectionType;
        this.collectionType = collectionType;
        this.collection = newCollection;
        observable.firePropertyChange("collectionType", oldCollectionType, newCollection);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws AnimalException {
        if (name == null || name.trim().equals("")) {
            throw new AnimalException("Nazwa grupy nie może być pusta");
        }
        String prevName = this.name;
        this.name = name;
        observable.firePropertyChange("name", prevName, this.name);
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observable.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        observable.removePropertyChangeListener(pcl);
    }

}
