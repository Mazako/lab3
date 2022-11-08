package view;

import model.GroupOfAnimals;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

class TableView extends JScrollPane {
    private JTable table;
    private TableModel tableModel;

    public TableView(List<GroupOfAnimals> groupOfAnimalsList, int width, int height) {
        this(width, height);
        tableModel = new TableModel(groupOfAnimalsList);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        this.setViewportView(table);
    }

    private TableView(int width, int height) {
        this.setMaximumSize(new Dimension(width, height));
    }

    public TableView(GroupOfAnimals groupOfAnimals, int width, int height) {
        this(width, height);
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

