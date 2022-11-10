package view.group;

import model.Animal;
import model.AnimalException;
import model.collection.GroupOfAnimals;
import view.MainFrame;
import view.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class GroupViewWindow extends JDialog implements ActionListener {

    public static final int WINDOW_WIDTH = 600;

    public static final int WINDOW_HEIGHT = 650;

    public static final int INFO_PANEL_HEIGHT = 100;

    public static final int PANELS_WIDTH = 570;

    public static final int SCROLL_PANE_HEIGHT = 440;

    public static final int BUTTON_PANEL_HEIGHT = 100;
    private GroupOfAnimals group;
    private JPanel mainPanel = new JPanel();

    JPanel infoPanel = new JPanel();
    JLabel collectionNameLabel = new JLabel("Nazwa");
    JLabel collectionTypeLabel = new JLabel("Rodzaj");
    JTextField collectionNameField = new JTextField();
    JTextField collectionTypeField = new JTextField();
    JPanel nameDiv = new JPanel();
    JPanel typeDiv = new JPanel();
    TableView tableView;
    JPanel buttonPanel = new JPanel();
    JButton addNewAnimalButton = new JButton("Dodaj nową osobę");
    JButton editAnimalButton = new JButton("Edytuj osobę");
    JButton removeButton = new JButton("Usuń osobę");
    JButton saveAnimalButton = new JButton("Zapisz osobę do pliku");
    JButton readAnimalButton = new JButton("Wczytaj osobę z pliku");

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
        initScrollPanel();
        initButtonPanel();

        mainPanel.add(infoPanel);
        mainPanel.add(tableView);
        mainPanel.add(buttonPanel);
        this.add(mainPanel);
        this.setVisible(true);

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
            if (source == addNewAnimalButton) {
                addAnimal();
            } else if (source == editAnimalButton) {
                editAnimal();
            } else if (source == removeButton) {
                removeAnimal();
            } else if (source == saveAnimalButton) {
                saveAnimal();
            } else if (source == readAnimalButton) {
                readAnimal();
            }
        } catch (AnimalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        } finally {
            tableView.refreshView(group);
        }
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
