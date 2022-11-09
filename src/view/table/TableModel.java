package view.table;

import model.Animal;
import model.collection.GroupOfAnimals;

import javax.swing.table.DefaultTableModel;
import java.util.Iterator;
import java.util.List;

class TableModel extends DefaultTableModel {

    public static final String[] GROUP_LIST_HEADERS = new String[] {
            "Nazwa grupy",
            "Typ kolekcji",
            "Liczba osób"
    };

    public static final String[] GROUP_HEADERS = new String[]{
            "Imię",
            "Typ",
            "Wiek",
            "Masa",
            "Gatunek"
    };


    public TableModel(List<GroupOfAnimals> groupOfAnimalsList) {
        super(GROUP_LIST_HEADERS, 0);
        for (GroupOfAnimals group : groupOfAnimalsList) {
            addRow(new Object[]{group.getName(), group.getCollectionType().getDescription(), group.size()});
        }

    }

    public TableModel(GroupOfAnimals groupOfAnimals) {
        super(GROUP_HEADERS, 0);
        if (groupOfAnimals == null) {
            return;
        }
        Iterator<Animal> iterator = groupOfAnimals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            addRow(new Object[]{animal.getName(), animal.getType(), animal.getAge(), animal.getWeight(), animal.getSpecies()});
        }
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
