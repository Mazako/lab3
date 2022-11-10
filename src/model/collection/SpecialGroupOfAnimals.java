package model.collection;

import model.Animal;
import model.AnimalException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class SpecialGroupOfAnimals extends GroupOfAnimals implements Serializable {

    private final GroupOfAnimals group1;
    private final GroupOfAnimals group2;

    public SpecialGroupOfAnimals(CollectionType collectionType, String name, GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        super(collectionType, name);
        if (group1 instanceof SpecialGroupOfAnimals || group2 instanceof SpecialGroupOfAnimals) {
            throw new AnimalException("Specjalna grupa nie może składać się ze specjalnych grup");
        }
        this.group1 = group1;
        this.group2 = group2;
    }

    public static SpecialGroupOfAnimals createOrGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.getName() + " OR " + group2.getName();
        SpecialGroupOfAnimals group = new SpecialGroupOfAnimals(type, name, group1, group2);
        group.collection.addAll(group1.collection);
        group.collection.addAll(group2.collection);
        return group;
    }

    public static SpecialGroupOfAnimals createAndGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " AND " + group2.name;
        SpecialGroupOfAnimals group = new SpecialGroupOfAnimals(type, name, group1, group2);
        Collection<Animal> tmpCollection = group.getCollectionType().getCollection();
        tmpCollection.addAll(group1.collection);
        for (Animal animal : group2.collection) {
            if (tmpCollection.remove(animal)) {
                group.collection.add(animal);
            }
        }
        return group;
    }

    public static SpecialGroupOfAnimals createDiffGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " SUB " + group2.name;
        SpecialGroupOfAnimals group = new SpecialGroupOfAnimals(type, name, group1, group2);
        group.collection.addAll(group1.collection);
        for (Animal animal : group2.collection) {
            group.remove(animal);
        }
        return group;
    }

    public static SpecialGroupOfAnimals createXorGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " XOR " + group2.name;
        SpecialGroupOfAnimals animals = new SpecialGroupOfAnimals(type, name, group1, group2);
        Collection<Animal> group1DiffGroup2 = createDiffGroup(group1, group2).collection;
        Collection<Animal> group2DiffGroup1 = createDiffGroup(group2, group1).collection;
        animals.collection.addAll(group2DiffGroup1);
        animals.collection.addAll(group1DiffGroup2);
        return animals;
    }

    private static CollectionType getGroupTypeFromTwo(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType collection1 = group1.getCollectionType();
        CollectionType collection2 = group2.getCollectionType();
        if (collection2.getCollection() instanceof Set<Animal> && !(collection1.getCollection() instanceof Set<Animal>)) {
            return collection2;
        } else {
            return collection1;
        }
    }
}
