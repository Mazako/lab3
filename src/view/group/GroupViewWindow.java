package view.group;

import model.Animal;
import model.AnimalException;
import model.collection.CollectionType;
import model.collection.GroupOfAnimals;
import view.MainFrame;
import view.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GroupViewWindow extends JDialog implements ActionListener {

    public static final int WINDOW_WIDTH = 600;

    public static final int WINDOW_HEIGHT = 670;

    public static final int INFO_PANEL_HEIGHT = 100;

    public static final int PANELS_WIDTH = 570;

    public static final int SCROLL_PANE_HEIGHT = 440;

    public static final int BUTTON_PANEL_HEIGHT = 100;
    private final GroupOfAnimals group;
    private final JPanel mainPanel = new JPanel();

    private final JPanel infoPanel = new JPanel();
    private final JLabel collectionNameLabel = new JLabel("Nazwa");
    private final JLabel collectionTypeLabel = new JLabel("Rodzaj");
    private final JTextField collectionNameField = new JTextField();
    private final JTextField collectionTypeField = new JTextField();
    private final JPanel nameDiv = new JPanel();
    private final JPanel typeDiv = new JPanel();
    private TableView tableView;
    private final JPanel buttonPanel = new JPanel();
    private final JButton addNewAnimalButton = new JButton("Dodaj nową osobę");
    private final JButton editAnimalButton = new JButton("Edytuj osobę");
    private final JButton removeButton = new JButton("Usuń osobę");
    private final JButton saveAnimalButton = new JButton("Zapisz osobę do pliku");
    private final JButton readAnimalButton = new JButton("Wczytaj osobę z pliku");
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu animalListMenu = new JMenu("Lista osób");
        private final JMenuItem addMenuItem = new JMenuItem("Dodaj nową osobę");
        private final JMenuItem editMenuItem = new JMenuItem("Edytuj osobę");
        private final JMenuItem removeMenuItem = new JMenuItem("Usuń osobę");
        private final JMenuItem loadMenuItem = new JMenuItem("Wczytaj osobę do pliku");
        private final JMenuItem saveMenuItem = new JMenuItem("Zapisz osobę do pliku");
    private final JMenu sortMenu = new JMenu("Sortowanie");
        private final JMenuItem sortByNameMenuItem = new JMenuItem("Sortuj po nazwie");
        private final JMenuItem sortByAgeMenuItem = new JMenuItem("Sortuj po wieku");
        private final JMenuItem sortByWeightMenuItem = new JMenuItem("Sortuj po masie");
    private final JMenu propertiesMenu = new JMenu("Właściwości");
        private final JMenuItem changeNameMenuItem = new JMenuItem("Zmień nazwę");
        private final JMenuItem changeCollectionMenuItem = new JMenuItem("Zmień typ kolekcji");
    private final JMenu aboutMenu = new JMenu("O programie");
        private final JMenuItem authorMenuItem = new JMenuItem("Autor");

    public GroupViewWindow(GroupOfAnimals group, JFrame parent) {
        super(parent, true);
        this.group = group;
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setTitle("Podgląd grupy");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(MainFrame.PADDING_LENGTH, MainFrame.PADDING_LENGTH, MainFrame.PADDING_LENGTH, MainFrame.PADDING_LENGTH));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        initInfoPanel();
        initMenuBar();
        initScrollPanel();
        initButtonPanel();
        mainPanel.add(infoPanel);
        mainPanel.add(tableView);
        mainPanel.add(buttonPanel);
        this.setJMenuBar(menuBar);
        this.add(mainPanel);
        this.setVisible(true);

    }

    private void initMenuBar() {
        menuBar.add(animalListMenu);
        animalListMenu.add(addMenuItem);
        animalListMenu.add(editMenuItem);
        animalListMenu.add(removeMenuItem);
        animalListMenu.addSeparator();
        animalListMenu.add(loadMenuItem);
        animalListMenu.add(saveMenuItem);
        menuBar.add(sortMenu);
        sortMenu.add(sortByNameMenuItem);
        sortMenu.add(sortByAgeMenuItem);
        sortMenu.add(sortByWeightMenuItem);
        menuBar.add(propertiesMenu);
        propertiesMenu.add(changeNameMenuItem);
        propertiesMenu.add(changeCollectionMenuItem);
        menuBar.add(aboutMenu);
        aboutMenu.add(authorMenuItem);
        for (JMenuItem menuItem : new JMenuItem[] {
            addMenuItem,
            editMenuItem,
            removeMenuItem,
            loadMenuItem,
            saveMenuItem,
            sortByNameMenuItem,
            sortByAgeMenuItem,
            sortByWeightMenuItem,
            changeNameMenuItem,
            changeCollectionMenuItem,
            authorMenuItem
        }) {
            menuItem.addActionListener(this);
        }
    }

    private void initButtonPanel() {
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setMaximumSize(new Dimension(PANELS_WIDTH, BUTTON_PANEL_HEIGHT));
        for (JButton button : new JButton[] {
                addNewAnimalButton,
                editAnimalButton,
                removeButton,
                readAnimalButton,
                saveAnimalButton,
        }) {
            buttonPanel.add(button);
            button.addActionListener(this);
        }
    }

    private void initScrollPanel() {
        tableView = new TableView(group, PANELS_WIDTH, SCROLL_PANE_HEIGHT);
    }

    private void initInfoPanel() {
        infoPanel.setLayout(new FlowLayout());
        infoPanel.setMaximumSize(new Dimension(PANELS_WIDTH, INFO_PANEL_HEIGHT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 0));

        collectionNameLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        collectionTypeLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        collectionNameField.setEditable(false);
        collectionNameField.setColumns(15);
        collectionNameField.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        collectionNameField.setText(group.getName());


        collectionTypeField.setColumns(15);
        collectionTypeField.setEditable(false);
        collectionTypeField.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        collectionTypeField.setText(group.getCollectionType().getDescription());


        nameDiv.setLayout(new FlowLayout());
        nameDiv.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        nameDiv.add(collectionNameLabel);
        nameDiv.add(collectionNameField);

        typeDiv.setLayout(new FlowLayout());
        typeDiv.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        typeDiv.add(collectionTypeLabel);
        typeDiv.add(collectionTypeField);

        infoPanel.add(nameDiv);
        infoPanel.add(typeDiv);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == addNewAnimalButton || source == addMenuItem) {
                addAnimal();
            } else if (source == editAnimalButton || source == editMenuItem) {
                editAnimal();
            } else if (source == removeButton || source == removeMenuItem) {
                removeAnimal();
            } else if (source == saveAnimalButton || source == saveMenuItem) {
                saveAnimal();
            } else if (source == readAnimalButton || source == loadMenuItem) {
                readAnimal();
            } else if (source == sortByAgeMenuItem) {
                group.sortByAge();
            } else if (source == sortByNameMenuItem) {
                group.sortByName();
            } else if (source == sortByWeightMenuItem) {
                group.sortByWeight();
            } else if (source == changeNameMenuItem) {
                changeName();
            } else if (source == changeCollectionMenuItem) {
                changeCollection();
            }
        } catch (AnimalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        } finally {
            tableView.refreshView(group);
            collectionNameField.setText(group.getName());
            collectionTypeField.setText(group.getCollectionType().toString());
        }
    }

    private void changeCollection() throws AnimalException {
        Object option = JOptionPane.showInputDialog(this,
                "Wybierz kolekcję",
                "Kolekcja",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                CollectionType.values(),
                null
        );
        if (option == null) {
            return;
        }
        CollectionType prevCollection = group.getCollectionType();
        CollectionType type = (CollectionType) option;
        group.setCollection(type);
        JOptionPane.showMessageDialog(
                this,
                "Pomyślnie zmieniono kolekcję z " + prevCollection + " na " + type,
                "Sukces",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void changeName() throws AnimalException {
        String newName = JOptionPane.showInputDialog(
                this,
                "Wybierz nową nazwę",
                "Zmiana nazwy",
                JOptionPane.INFORMATION_MESSAGE
        );
        if (newName == null) {
            return;
        }
        group.setName(newName);
        JOptionPane.showMessageDialog(
                this,
                "Pomyślnie zmieniono nazwę",
                "Sukces",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void saveAnimal() throws AnimalException {
        int selectedRow = tableView.getSelectedRow();
        Animal animalFromTable = tableView.getAnimalFromTable(selectedRow);
        Animal foundAnimal = group.find(animalFromTable)
                .orElseThrow(() -> new AnimalException("Nie znaleziono w kolekcji takiego zwierzęcia"));
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("./"));
        jFileChooser.showSaveDialog(this);
        File selectedFile = jFileChooser.getSelectedFile();
        if (!(selectedFile == null)) {
            Animal.writeAnimalToBinary(selectedFile.getAbsolutePath(),foundAnimal);
            JOptionPane.showMessageDialog(
                    this,
                    "Pomyślnie zapisano zwierzę",
                    "Zapisano",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void readAnimal() throws AnimalException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("./"));
        jFileChooser.showOpenDialog(this);
        File selectedFile = jFileChooser.getSelectedFile();
        if (!(selectedFile == null)) {
            Animal animal = Animal.readAnimalFromBinary(selectedFile.getAbsolutePath());
            group.add(animal);
            JOptionPane.showMessageDialog(
                    this,
                    "Pomyślnie zapisano zwierzę",
                    "Zapisano",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void removeAnimal() throws AnimalException {
        int selectedRow = tableView.getSelectedRow();
        Animal animalFromTable = tableView.getAnimalFromTable(selectedRow);
        group.find(animalFromTable)
                .map(animal -> group.remove(animal))
                .orElseThrow(() -> new AnimalException("nie udało się usunąć zwierzęcia z kolekcji"));
    }

    private void editAnimal() throws AnimalException {
        int selectedRow = tableView.getSelectedRow();
        Animal animalFromTable = tableView.getAnimalFromTable(selectedRow);
        Animal animal = group.find(animalFromTable).orElseThrow(() -> new AnimalException("Nie znaleziono w kolekcji takiego zwierzęcia"));
        new AddAnimalDialog(this, animal);
    }

    private void addAnimal() {
        AddAnimalDialog animalDialog = new AddAnimalDialog(this);
        Animal animal = animalDialog.getAnimal();
        group.add(animal);
    }
}
