import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUICalculator extends JFrame {
    private JTextField display;
    private double result = 0;
    private String lastCommand = "=";
    private boolean start = true;
    private double memory = 0;
    private boolean on = false;
    private JButton onOffButton;

    public GUICalculator() {
        setTitle("Java Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField("");
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.PLAIN, 20));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "ON"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(new ButtonListener());
            if (label.equals("ON")) {
                onOffButton = button;
                onOffButton.setPreferredSize(new Dimension(100, 100));
            }
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("ON") || command.equals("OFF")) {
                toggleOnOff();
                return;
            }

            if (!on) {
                return;
            }

            if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.equals(".")) {
                if (start) {
                    display.setText(command);
                    start = false;
                } else {
                    display.setText(display.getText() + command);
                }
            } else if (command.equals("C")) {
                display.setText("0");
                start = true;
                result = 0;
                lastCommand = "=";
            } else if (command.equals("MC")) {
                memory = 0;
            } else if (command.equals("MR")) {
                display.setText("" + memory);
                start = true;
            } else if (command.equals("M+")) {
                memory += Double.parseDouble(display.getText());
            } else {
                if (start) {
                    if (command.equals("-")) {
                        display.setText(command);
                        start = false;
                    } else {
                        lastCommand = command;
                    }
                } else {
                    calculate(Double.parseDouble(display.getText()));
                    lastCommand = command;
                    start = true;
                }
            }
        }
    }

    private void toggleOnOff() {
        on = !on;
        if (on) {
            display.setText("0");
            onOffButton.setText("OFF");
        } else {
            display.setText("");
            onOffButton.setText("ON");
        }
    }

    public void calculate(double x) {
        switch (lastCommand) {
            case "+":
                result += x;
                break;
            case "-":
                result -= x;
                break;
            case "*":
                result *= x;
                break;
            case "/":
                result /= x;
                break;
            case "=":
                result = x;
                break;
        }
        display.setText("" + result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUICalculator calc = new GUICalculator();
            calc.setVisible(true);
        });
    }
}
