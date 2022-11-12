/*
 * Program: Specjalna grupa zwierząt, która jest sumą/częścią wspólną/różnicą/różnicą symetryczną dwóch
 *          zwykłych grup
 *
 *    Plik: SpecialGroupOfAnimals.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package model.collection;

import model.Animal;
import model.AnimalException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class SpecialGroupOfAnimals extends GroupOfAnimals implements Serializable, PropertyChangeListener {

    @Serial
    private static final long serialVersionUID = 1L;
    private final GroupOfAnimals group1;
    private final GroupOfAnimals group2;

    private TypeOfSpecialGroup typeOfSpecialGroup;

    public SpecialGroupOfAnimals(CollectionType collectionType, String name, GroupOfAnimals group1, GroupOfAnimals group2, TypeOfSpecialGroup typeOfSpecialGroup) throws AnimalException {
        super(collectionType, name);
        this.typeOfSpecialGroup = typeOfSpecialGroup;
        if (group1 instanceof SpecialGroupOfAnimals || group2 instanceof SpecialGroupOfAnimals) {
            throw new AnimalException("Specjalna grupa nie może składać się ze specjalnych grup");
        }
        this.group1 = group1;
        this.group2 = group2;
        group1.addPropertyChangeListener(this);
        group2.addPropertyChangeListener(this);
    }

    public static SpecialGroupOfAnimals createOrGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.getName() + " OR " + group2.getName();
        SpecialGroupOfAnimals group = new SpecialGroupOfAnimals(type, name, group1, group2, TypeOfSpecialGroup.OR);
        group.collection.addAll(group1.collection);
        group.collection.addAll(group2.collection);
        return group;
    }

    public static SpecialGroupOfAnimals createAndGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " AND " + group2.name;
        SpecialGroupOfAnimals group = new SpecialGroupOfAnimals(type, name, group1, group2 ,TypeOfSpecialGroup.AND);
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
        SpecialGroupOfAnimals group = new SpecialGroupOfAnimals(type, name, group1, group2, TypeOfSpecialGroup.DIFF);
        group.collection.addAll(group1.collection);
        for (Animal animal : group2.collection) {
            group.remove(animal);
        }
        return group;
    }

    public static SpecialGroupOfAnimals createXorGroup(GroupOfAnimals group1, GroupOfAnimals group2) throws AnimalException {
        CollectionType type = getGroupTypeFromTwo(group1, group2);
        String name = group1.name + " XOR " + group2.name;
        SpecialGroupOfAnimals animals = new SpecialGroupOfAnimals(type, name, group1, group2, TypeOfSpecialGroup.XOR);
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

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        try {
            SpecialGroupOfAnimals newGroup = switch (this.typeOfSpecialGroup) {
                case OR -> createOrGroup(group1, group2);
                case DIFF -> createDiffGroup(group1, group2);
                case AND -> createAndGroup(group1, group2);
                case XOR -> createXorGroup(group1, group2);
            };
            newGroup.invalidateListener();
            this.name = newGroup.name;
            this.typeOfSpecialGroup = newGroup.typeOfSpecialGroup;
            this.collectionType = newGroup.collectionType;
            this.collection = newGroup.collection;
        } catch (AnimalException ex) {
            throw new RuntimeException();
        }
    }

    public void invalidateListener() {
        group2.removePropertyChangeListener(this);
        group1.removePropertyChangeListener(this);
    }

    private static enum TypeOfSpecialGroup {
        OR,AND,DIFF,XOR
    }
}
