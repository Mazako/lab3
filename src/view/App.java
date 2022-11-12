/*
 * Program: Klasa w której znajduje się metoda main naszej aplikacji
 *
 *    Plik: App.java
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package view;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
