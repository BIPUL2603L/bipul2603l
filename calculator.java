// File: Calculator.java
// Compile: javac Calculator.java
// Run:     java Calculator

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private final JTextField display;
    private double current = 0;
    private String operator = "=";
    private boolean startNew = true;

    public Calculator() {
        setTitle("Simple Calculator");
        setSize(320, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("SansSerif", Font.PLAIN, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(Color.WHITE);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 6, 6));
        String[] buttons = {
            "C", "±", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", ""
        };

        for (String b : buttons) {
            if (b.equals("")) {
                buttonPanel.add(new JLabel()); // placeholder
                continue;
            }
            JButton btn = new JButton(b);
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.addActionListener(new ButtonListener());
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = ((JButton)e.getSource()).getText();

            if ("0123456789.".contains(cmd)) {
                if (startNew) {
                    display.setText(cmd.equals(".") ? "0." : cmd);
                    startNew = false;
                } else {
                    if (cmd.equals(".") && display.getText().contains(".")) return;
                    display.setText(display.getText() + cmd);
                }
            } else if (cmd.equals("C")) {
                current = 0;
                operator = "=";
                display.setText("0");
                startNew = true;
            } else if (cmd.equals("±")) {
                String txt = display.getText();
                if (txt.equals("0")) return;
                if (txt.startsWith("-")) display.setText(txt.substring(1));
                else display.setText("-" + txt);
            } else if (cmd.equals("%")) {
                try {
                    double v = Double.parseDouble(display.getText()) / 100.0;
                    display.setText(trim(v));
                    startNew = true;
                } catch (NumberFormatException ex) { /* ignore */ }
            } else { // operator or =
                try {
                    double x = Double.parseDouble(display.getText());
                    calculate(x);
                    operator = cmd;
                    startNew = true;
                } catch (NumberFormatException ex) { /* ignore */ }
            }
        }
    }

    private void calculate(double x) {
        switch (operator) {
            case "+":
                current += x; break;
            case "-":
                current -= x; break;
            case "*":
                current *= x; break;
            case "/":
                if (x == 0) {
                    JOptionPane.showMessageDialog(this, "Division by zero!", "Error", JOptionPane.ERROR_MESSAGE);
                    current = 0;
                } else current /= x;
                break;
            case "=":
                current = x; break;
        }
        display.setText(trim(current));
    }

    private String trim(double v) {
        if (v == (long) v) return String.format("%d", (long) v);
        else return String.format("%s", v);
    }

    public static void main(String[] args) {
        // Ensure UI on EDT
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}