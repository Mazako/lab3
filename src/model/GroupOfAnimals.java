package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

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

    public int size() {
        return collection.size();
    }

    public void add(Animal animal) {
        collection.add(animal);
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
}
