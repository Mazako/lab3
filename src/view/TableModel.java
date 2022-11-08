package view;

import model.GroupOfAnimals;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TableModel extends DefaultTableModel {

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
        //TODO: dodać dodawanie

    }

    public TableModel(GroupOfAnimals groupOfAnimals) {
        super(GROUP_HEADERS, 0);
        //todo: dodać dodwanie
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
