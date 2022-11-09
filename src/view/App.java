package view;

import view.group.GroupViewWindow;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GroupViewWindow(null, null));
    }
}
