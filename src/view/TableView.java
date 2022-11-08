package view;

import model.GroupOfAnimals;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TableView extends JScrollPane {
    private final JTable table;
    private TableModel tableModel;

    public TableView(List<GroupOfAnimals> groupOfAnimalsList) {
        tableModel = new TableModel(groupOfAnimalsList);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        this.setViewportView(table);
    }

    public TableView(GroupOfAnimals groupOfAnimals) {
        tableModel = new TableModel(groupOfAnimals);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        this.setViewportView(table);
    }



    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public void refreshView(List<GroupOfAnimals> groupOfAnimalsList) {
        tableModel = new TableModel(groupOfAnimalsList);
        table.setModel(tableModel);
    }

    public void refreshView(GroupOfAnimals groupOfAnimals) {
        tableModel = new TableModel(groupOfAnimals);
        table.setModel(tableModel);
    }
}

