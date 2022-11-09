package view.table;

import model.Animal;
import model.AnimalException;
import model.Species;
import model.collection.GroupOfAnimals;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableView extends JScrollPane {
    private JTable table;
    private TableModel tableModel;

    public TableView(List<GroupOfAnimals> groupOfAnimalsList, int width, int height) {
        this(width, height);
        tableModel = new TableModel(groupOfAnimalsList);
        table = new JTable(tableModel);
        this.setViewportView(table);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
    }

    private TableView(int width, int height) {
        this.setMaximumSize(new Dimension(width, height));
    }

    public TableView(GroupOfAnimals groupOfAnimals, int width, int height) {
        this(width, height);
        tableModel = new TableModel(groupOfAnimals);
        table = new JTable(tableModel);
        this.setViewportView(table);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
    }



    public int getSelectedRow() throws AnimalException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            throw new AnimalException("Nie wybrano żadnego zwierzęcia");
        }
        return selectedRow;
    }

    public void refreshView(List<GroupOfAnimals> groupOfAnimalsList) {
        tableModel = new TableModel(groupOfAnimalsList);
        table.setModel(tableModel);
    }

    public void refreshView(GroupOfAnimals groupOfAnimals) {
        tableModel = new TableModel(groupOfAnimals);
        table.setModel(tableModel);
    }

    public Animal getAnimalFromTable(int index) {
        String name = (String) tableModel.getValueAt(index, 0);
        String type = (String) tableModel.getValueAt(index, 1);
        int age = (int) tableModel.getValueAt(index, 2);
        double weight = (double) tableModel.getValueAt(index, 3);
        Species species = (Species) tableModel.getValueAt(index, 4);
        return new Animal(name, type, age, weight, species);
    }
}

