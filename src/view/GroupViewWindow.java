package view;

import model.GroupOfAnimals;

import javax.swing.*;

public class GroupViewWindow extends JDialog {

    public static final int WINDOW_WIDTH = 600;

    public static final int WINDOW_HEIGHT = 650;
    private GroupOfAnimals group;
    private JPanel mainPanel = new JPanel();

    public GroupViewWindow(GroupOfAnimals group, JFrame parent) {
        super(parent, true);
        this.group = group;
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setTitle("Podgląd grupy");
        mainPanel.setBorder(BorderFactory.createEmptyBorder(MainFrame.PADDING_LENGTH, MainFrame.PADDING_LENGTH, MainFrame.PADDING_LENGTH, MainFrame.PADDING_LENGTH));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.add(mainPanel);
        this.setVisible(true);

    }
}
