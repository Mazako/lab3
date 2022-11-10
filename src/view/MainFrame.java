package view;

import model.AnimalException;
import model.collection.CollectionType;
import model.collection.GroupOfAnimals;
import view.group.GroupViewWindow;
import view.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    public static final int WINDOW_WIDTH = 660;
    public static final int WINDOW_HEIGHT = 700;
    public static final int SCROLL_PANE_HEIGHT = 450;
    public static final int SCROLL_PANE_WIDTH = 630;
    public static final int BUTTON_PANEL_HEIGHT = 170;
    public static final int BUTTON_PANEL_WIDTH = 630;
    public static final int PADDING_LENGTH = 15;

    public static final String INITIAL_BINARY_FILENAME = "init.o";

    private TableView tableView;
    private final JPanel panel = new JPanel();
    private final JPanel buttonPanel = new JPanel();

    private final JButton createGroupButton = new JButton("Stwórz grupę");
    private final JButton modifyGroupButton = new JButton("Zmodyfikuj grupę");
    private final JButton removeGroupButton = new JButton("Usuń grupę");
    private final JButton readGroupButton = new JButton("Wczytaj z pliku binarnego");
    private final JButton writeGroupButton = new JButton("Zapisz do pliku binarnego");
    private final JButton sumButton = new JButton("Suma grup");
    private final JButton diffButton = new JButton("Różnica grup");
    private final JButton productButton = new JButton("Iloczyn grup");
    private final JButton symDiffButton = new JButton("Różnica symetryczna");
    private List<GroupOfAnimals> groupOfAnimalsList = new ArrayList<>();


    public MainFrame() {
        this.addWindowListener(new WindowEventOperator());
        tableView = new TableView(groupOfAnimalsList, SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Zarządzanie grupami");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LENGTH, PADDING_LENGTH, PADDING_LENGTH, PADDING_LENGTH));
        initButtons();
        panel.add(tableView);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        this.add(panel);
        this.setVisible(true);

    }

    private void initButtons() {
        buttonPanel.setMaximumSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT));
        buttonPanel.setLayout(new GridLayout(3, 3, 15, 15));
        for (JButton button : new JButton[] {
                createGroupButton,
                modifyGroupButton,
                removeGroupButton,
                readGroupButton,
                writeGroupButton,
                sumButton,
                diffButton,
                productButton,
                symDiffButton
        }) {
            buttonPanel.add(button);
            button.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == createGroupButton) {
                createGroup();
            } else if (source == removeGroupButton) {
                removeGroup();
            } else if (source == modifyGroupButton) {
                modifyGroup();
            } else if (source == writeGroupButton) {
                writeGroup();
            } else if (source == readGroupButton) {
                readGroup();
            }
        } catch (AnimalException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            tableView.refreshView(groupOfAnimalsList);
        }
    }

    private void readGroup() throws AnimalException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./"));
        fileChooser.showSaveDialog(this);
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
            GroupOfAnimals group = GroupOfAnimals.readGroupOfAnimalsFromFile(selectedFile);
            groupOfAnimalsList.add(group);
            JOptionPane.showMessageDialog(
                    this,
                    "Pomyślnie wczytano grupę",
                    "Wczytano",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void writeGroup() throws AnimalException {
        int selectedRow = tableView.getSelectedRow();
        GroupOfAnimals group = groupOfAnimalsList.get(selectedRow);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./"));
        fileChooser.showSaveDialog(this);
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
            GroupOfAnimals.writeGroupOfAnimalsToFile(group, selectedFile);
            JOptionPane.showMessageDialog(this,
                    "Pomyślnie zapisano grupę",
                    "Zapisano",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modifyGroup() throws AnimalException {
        int selectedRow = tableView.getSelectedRow();
        GroupOfAnimals group = groupOfAnimalsList.get(selectedRow);
        GroupViewWindow view = new GroupViewWindow(group, this);
    }

    private void removeGroup() throws AnimalException {
        int selectedRow = tableView.getSelectedRow();
        groupOfAnimalsList.remove(selectedRow);
    }

    private void createGroup() throws AnimalException {
        String name = JOptionPane.showInputDialog(
                this,
                "Wybierz nazwę",
                "Nazwa",
                JOptionPane.INFORMATION_MESSAGE
        );
        if (name == null) {
            return;
        }
        if (name.trim().equals("")) {
            throw new AnimalException("Imię nie może być puste");
        }
         Object option = JOptionPane.showInputDialog(this,
                "Wybierz kolekcję",
                "Kolekcja",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                CollectionType.values(),
                null
         );
        CollectionType collection = (CollectionType) option;
        GroupOfAnimals group = new GroupOfAnimals(collection, name);
        groupOfAnimalsList.add(group);
    }

    private void writeAnimalsToFile(String fileName) throws AnimalException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(groupOfAnimalsList);
        } catch (FileNotFoundException e) {
            throw new AnimalException("Nie znaleziono pliku: " + fileName);
        } catch (IOException e) {
            throw new AnimalException("Błąd w zapisie do pliku");
        }
    }

    private class WindowEventOperator extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            try {
                writeAnimalsToFile(INITIAL_BINARY_FILENAME);
                JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "Zapisano stan grup",
                        "Zapisano grupy",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (AnimalException ex) {
                JOptionPane.showMessageDialog(
                        MainFrame.this,
                        ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
            windowClosing(e);
        }
    }
}
