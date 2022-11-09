/*
 * Program: Klasa odpowiadająca za dialog do dodawania / edycji zwierzat
 *    Plik: AnnAnimalDialog.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: pazdziernik 2022 r.
 */

package view.group;

import model.Animal;
import model.AnimalException;
import model.Species;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

class AddAnimalDialog extends JDialog implements ActionListener {

    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 300;
    private static final int LABEL_WIDTH = 85;
    private static final int LABEL_HEIGHT = 16;

    private static final int TEXT_FIELD_WIDTH = 150;
    private static final int TEXT_FIELD_HEIGHT = 28;

    private static final int BUTTON_WIDTH = 275;
    private static final int BUTTON_HEIGHT = 40;
    private static final String DIALOG_TITLE = "Dodawanie nowego zwierzecia ";

    JPanel panel = new JPanel();
    JLabel nameLabel = new JLabel("Imie: ");
    JLabel typeLabel = new JLabel("Typ: ");
    JLabel ageLabel = new JLabel("Wiek: ");
    JLabel weightLabel = new JLabel("Masa [kg]: ");
    JLabel speciesLabel = new JLabel("Gatunek: ");
    JTextField nameTextField = new JTextField();
    JTextField typeTextField = new JTextField();
    JTextField ageTextField = new JTextField();
    JTextField weightTextField = new JTextField();
    JComboBox<Species> speciesComboBox;
    JButton readyButton = new JButton("Gotowe");
    JButton cancelButton = new JButton("Anuluj");
    private Animal animal;


    public AddAnimalDialog(Frame owner, Animal animal) {
        super(owner, true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle(DIALOG_TITLE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        speciesComboBox = new JComboBox<>(Species.getSpeciesArrayWithoutUnknown());

        if (!(animal == null)) {
            this.setTitle(animal.getName());
            nameTextField.setText(animal.getName());
            typeTextField.setText(animal.getType());
            ageTextField.setText(Integer.toString(animal.getAge()));
            weightTextField.setText(Double.toString(animal.getWeight()));
            speciesComboBox.setSelectedIndex(animal.getSpecies().ordinal());
        }

        readyButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        readyButton.addActionListener(this);

        cancelButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        cancelButton.addActionListener(this);

        for (JLabel label : Arrays.asList(
                nameLabel,
                typeLabel,
                ageLabel,
                weightLabel,
                speciesLabel
        )) {
            label.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            label.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
            label.setHorizontalAlignment(JLabel.RIGHT);
        }

        for (JTextField textField : Arrays.asList(
                nameTextField,
                typeTextField,
                ageTextField,
                weightTextField
        )) {
            textField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
            textField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        }
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        for (Component component : Arrays.asList(
                nameLabel,
                nameTextField,
                typeLabel,
                typeTextField,
                ageLabel,
                ageTextField,
                weightLabel,
                weightTextField,
                speciesLabel,
                speciesComboBox,
                readyButton,
                cancelButton
        )) {
            panel.add(component);
        }
        panel.setBackground(Color.LIGHT_GRAY);
        this.add(panel);
        this.setVisible(true);
    }

    public AddAnimalDialog(Frame owner) {
        this(owner, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == cancelButton) {
            this.dispose();
        } else if (source == readyButton) {
            try {
                animal = createAnimal();
                this.dispose();
            } catch (AnimalException | NumberFormatException ex) {
                String message = ex.getMessage();
                if (ex.getClass() == NumberFormatException.class) {
                    message = "Wpisano niepoprawną liczbę";
                }
                JOptionPane.showMessageDialog(
                        this,
                        message,
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private Animal createAnimal() throws AnimalException {
        Animal animal = new Animal();
        String name = nameTextField.getText();
        String type = typeTextField.getText();
        int age = Integer.parseInt(ageTextField.getText());
        double weight = Double.parseDouble(weightTextField.getText());
        Species species = (Species) speciesComboBox.getSelectedItem();
        animal.setName(name);
        animal.setType(type);
        animal.setAge(age);
        animal.setWeight(weight);
        animal.setSpecies(species);
        return animal;
    }

    public Animal getAnimal() {
        return this.animal;
    }
}