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
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    public static final int WINDOW_WIDTH = 660;
    public static final int WINDOW_HEIGHT = 700;
    public static final int SCROLL_PANE_HEIGHT = 450;
    public static final int SCROLL_PANE_WIDTH = 630;
    public static final int BUTTON_PANEL_HEIGHT = 170;
    public static final int BUTTON_PANEL_WIDTH = 630;
    public static final int PADDING_LENGTH = 15;
    public static final String ABOUT_MESSAGE = "Program do zarządzania grupami zwierząt\n\n" +
                                                "Autor: Michał Maziarz, grupa wtorek 13:15\n\n" +
                                                "Data: Listopad 2022";

    public static final String OR_MESSAGE = "SUMA GRUP\n\n" +
                                        "Tworzenie grupy zawierającej wszystkie osoby z grupy pierwszej\n" +
                                        "oraz wszystkie osoby z grupy drugiej\n";
    public static final String AND_MESSAGE = "ILOCZYN GRUP\n\n" +
                                            "Tworzenie grupy osób, które należą zarówno do grupy pierwszej\n" +
                                            "Jak i do grupy drugiej\n";
    public static final String DIFF_MESSAGE = "RÓŻNICA GRUP\n\n" +
                                            "Tworzenie grupy osób, które należą do grupy pierwszej\n" +
                                            "I nie ma ich w grupie drugiej\n";
    public static final String XOR_MESSAGE = "RÓŻNICA SYMETRYCZNA GRUP\n\n" +
                                            "Tworzenie grupy zawierającej osoby należące tylko do jednej z dwóch grup\n";
    public static final String INITIAL_BINARY_FILENAME = "init.o";

    private TableView tableView;
    private final JPanel panel = new JPanel();
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu groupMenu = new JMenu("Grupy");
        private final JMenuItem createGroupMenuItem = new JMenuItem("Utwórz grupę");
        private final JMenuItem editGroupMenuItem = new JMenuItem("Edytuj grupę");
        private final JMenuItem deleteGroupMenuItem = new JMenuItem("Usuń grupę");
        private final JMenuItem saveGroupMenuItem = new JMenuItem("Zapisz grupę do pliku");
        private final JMenuItem loadGroupMenuItem = new JMenuItem("Wczytaj grupę z pliku");
    private final JMenu specialGroupMenu = new JMenu("Grupy specjalne");
        private final JMenuItem orMenuItem = new JMenuItem("Połączenie grup");
        private final JMenuItem andMenuItem = new JMenuItem("Część wspólna grup");
        private final JMenuItem diffMenuItem = new JMenuItem("Różnica grup");
        private final JMenuItem xorMenuItem = new JMenuItem("Różnica symetryczna grup");
    private final JMenu aboutMenu = new JMenu("O programie");
        private final JMenuItem aboutMenuItem = new JMenuItem("Autor");
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
        loadStartData();
        tableView = new TableView(groupOfAnimalsList, SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Zarządzanie grupami");
        initMenuBar();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LENGTH, PADDING_LENGTH, PADDING_LENGTH, PADDING_LENGTH));
        initButtons();
        panel.add(tableView);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        this.setJMenuBar(menuBar);
        this.add(panel);
        this.setVisible(true);

    }

    private void initMenuBar() {
        menuBar.add(groupMenu);
        groupMenu.add(createGroupMenuItem);
        groupMenu.add(editGroupMenuItem);
        groupMenu.add(deleteGroupMenuItem);
        groupMenu.addSeparator();
        groupMenu.add(saveGroupMenuItem);
        groupMenu.add(loadGroupMenuItem);
        menuBar.add(specialGroupMenu);
        specialGroupMenu.add(orMenuItem);
        specialGroupMenu.add(andMenuItem);
        specialGroupMenu.add(diffMenuItem);
        specialGroupMenu.add(xorMenuItem);
        menuBar.add(aboutMenu);
        aboutMenu.add(aboutMenuItem);

        for (JMenuItem item : new JMenuItem[] {
            createGroupMenuItem,
            editGroupMenuItem,
            deleteGroupMenuItem,
            saveGroupMenuItem,
            loadGroupMenuItem,
            orMenuItem,
            andMenuItem,
            diffMenuItem,
            xorMenuItem,
                aboutMenuItem
        }) {
            item.addActionListener(this);
        }
    }

    private void loadStartData() {
        try {
            this.groupOfAnimalsList = readAnimalsFromFile(INITIAL_BINARY_FILENAME);
            JOptionPane.showMessageDialog(
                    this,
                    "Pomyślnie wczytano dane z pliku " + INITIAL_BINARY_FILENAME,
                    "Wczytano dane",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (AnimalException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
            if (source == createGroupButton || source == createGroupMenuItem) {
                createGroup();
            } else if (source == removeGroupButton || source == deleteGroupMenuItem) {
                removeGroup();
            } else if (source == modifyGroupButton || source == editGroupMenuItem) {
                modifyGroup();
            } else if (source == writeGroupButton || source == saveGroupMenuItem) {
                writeGroup();
            } else if (source == readGroupButton || source == loadGroupMenuItem) {
                readGroup();
            } else if (source == aboutMenuItem) {
                JOptionPane.showMessageDialog(this,
                        ABOUT_MESSAGE,
                        "O programie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (source == sumButton || source == orMenuItem) {
                createOrGroup();
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

    private void showCreationCancelInfo() {
        JOptionPane.showMessageDialog(
                this,
                "Anulowano tworzenie grupy",
                "Anulowano",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showCreationSuccessInfo() {
        JOptionPane.showMessageDialog(
                this,
                "Pomyślne złączono grupy",
                "Sukces",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void createOrGroup() throws AnimalException {
        GroupOfAnimals group1 = selectGroup(OR_MESSAGE, "Wybierz pierwszą grupę");
        if (group1 == null) {
            showCreationCancelInfo();
            return;
        }
        GroupOfAnimals group2 = selectGroup(OR_MESSAGE, "Wybierz drugą grupę");
        if (group2 == null) {
            showCreationCancelInfo();
            return;
        }
        GroupOfAnimals orGroup = GroupOfAnimals.createOrGroup(group1, group2);
        groupOfAnimalsList.add(orGroup);
        showCreationSuccessInfo();
    }

    private GroupOfAnimals selectGroup(String message, String group) {
        String finalMessage = message + group;
        Object option = JOptionPane.showInputDialog(this,
                finalMessage,
                "Wybierz grupę",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                groupOfAnimalsList.toArray(),
                null
        );
        return (GroupOfAnimals) option;

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

    private List<GroupOfAnimals> readAnimalsFromFile(String filename) throws AnimalException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INITIAL_BINARY_FILENAME))) {
            return (ArrayList<GroupOfAnimals>) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new AnimalException("Nie znaleziono pliku: " + filename + "\n plik zostanie utworzony po zamknięciu programu");
        } catch (IOException e) {
            throw new AnimalException("Błąd w zapisie do pliku");
        } catch (ClassNotFoundException e) {
            throw new AnimalException("Niekompatybilny typ klasy");
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
