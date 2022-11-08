package view;

import model.GroupOfAnimals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    public static final int WINDOW_WIDTH = 650;
    public static final int WINDOW_HEIGHT = 700;
    public static final int SCROLL_PANE_HEIGHT = 450;
    public static final int SCROLL_PANE_WIDTH = 630;
    public static final int BUTTON_PANEL_HEIGHT = 170;
    public static final int BUTTON_PANEL_WIDTH = 630;
    public static final int PADDING_LENGTH = 15;

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
        tableView = new TableView(groupOfAnimalsList);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LENGTH, PADDING_LENGTH, PADDING_LENGTH, PADDING_LENGTH));
        tableView.setMaximumSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
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
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

    }
}
