import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class InventoryManagementApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField registerFirstNameField, registerLastNameField, registerUsernameField, registerPasswordField;
    private JTextField loginUsernameField, loginPasswordField;
    private JTextField productNameField, productQuantityField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    private int loggedInUserId;
    private static final Logger logger = Logger.getLogger(InventoryManagementApp.class.getName());

    public InventoryManagementApp() {
        setTitle("Inventory Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // This one is to Initialize the CardLayout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // This one is to Connect to the database and initialize components
        connectToDatabase();
        initLogger();
        initComponents();

        // This one is to Set the main panel as the content pane and show the login
        // panel
        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "Login");

        // This one is to Make the frame visible
        setVisible(true);
    }

    private void initLogger() {
        try {
            FileHandler fh = new FileHandler("application.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This one is to Establish a connection to the database
    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql:// This one is tolocalhost:3306/inventory_db", "root",
                    "");
        } catch (Exception e) {
            logger.severe("Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to connect to the database. Please check the database settings.");
        }
    }

    // This one is to Initialize all the UI components
    private void initComponents() {
        initLookAndFeel();
        initNavbar();
        initRegisterPanel();
        initLoginPanel();
        initDashboardPanel();
    }

    // This one is to Set a modern look and feel for the UI
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            logger.warning("Failed to set look and feel: " + e.getMessage());
        }
    }

    // This one is to Create the navigation bar with menu items
    private void initNavbar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem registerMenuItem = new JMenuItem("Register");
        registerMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        JMenuItem loginMenuItem = new JMenuItem("Login");
        loginMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        menu.add(registerMenuItem);
        menu.add(loginMenuItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    // This one is to Create the registration panel with input fields and a register
    // button
    private void initRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridLayout(5, 1));
        registerFirstNameField = new JTextField();
        registerLastNameField = new JTextField();
        registerUsernameField = new JTextField();
        registerPasswordField = new JPasswordField();

        registerPanel.add(new JLabel("First Name:"));
        registerPanel.add(registerFirstNameField);
        registerPanel.add(new JLabel("Last Name:"));
        registerPanel.add(registerLastNameField);
        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(registerUsernameField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(registerPasswordField);

        JButton registerButton = new JButton("Register");
        registerButton.setIcon(new ImageIcon("icons/register.png")); // This one is to Example icon
        registerButton.addActionListener(new RegisterAction());
        registerPanel.add(registerButton);

        mainPanel.add(registerPanel, "Register");
    }

    // This one is to Create the login panel with input fields and a login button
    private void initLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginUsernameField = new JTextField();
        loginPasswordField = new JPasswordField();

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(loginUsernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(loginPasswordField);

        JButton loginButton = new JButton("Login");
        loginButton.setIcon(new ImageIcon("icons/login.png")); // This one is to Example icon
        loginButton.addActionListener(new LoginAction());
        loginPanel.add(loginButton);

        mainPanel.add(loginPanel, "Login");
    }

    // This one is to Create the dashboard panel with input fields, buttons, and a
    // table
    private void initDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());

        JButton addButton = new JButton("Add Product");
        addButton.setIcon(new ImageIcon("icons/add.png")); // This one is to Example icon
        addButton.addActionListener(new AddProductAction());

        JPanel inputPanel = new JPanel(new GridLayout(1, 4));
        productNameField = new JTextField();
        productQuantityField = new JTextField();

        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(productQuantityField);

        tableModel = new DefaultTableModel(new String[] { "ID", "Product Name", "Quantity", "Edit", "Delete" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // This one is to Only the edit and delete columns are editable
            }
        };

        productTable = new JTable(tableModel);
        productTable.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        productTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), true));
        productTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        productTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), false));

        JScrollPane tableScrollPane = new JScrollPane(productTable);

        dashboardPanel.add(inputPanel, BorderLayout.NORTH);
        dashboardPanel.add(addButton, BorderLayout.CENTER);
        dashboardPanel.add(tableScrollPane, BorderLayout.SOUTH);

        mainPanel.add(dashboardPanel, "Dashboard");
    }

    // This one is to Renderer for the buttons in the table cells
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // This one is to Editor for the buttons in the table cells
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private boolean isEditAction;

        public ButtonEditor(JCheckBox checkBox, boolean isEditAction) {
            super(checkBox);
            this.isEditAction = isEditAction;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (isEditAction) {
                    editProductAction(productTable.getSelectedRow());
                } else {
                    deleteProductAction(productTable.getSelectedRow());
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        // This one is to Action for editing a product
        private void editProductAction(int row) {
            int productId = (int) productTable.getValueAt(row, 0);
            String newName = JOptionPane.showInputDialog("Enter new product name:", productTable.getValueAt(row, 1));
            if (newName != null && !newName.trim().isEmpty()) {
                String newQuantityStr = JOptionPane.showInputDialog("Enter new quantity:",
                        productTable.getValueAt(row, 2));
                if (newQuantityStr != null && !newQuantityStr.trim().isEmpty()) {
                    try {
                        int newQuantity = Integer.parseInt(newQuantityStr);
                        PreparedStatement ps = connection
                                .prepareStatement("UPDATE products SET name = ?, quantity = ? WHERE id = ?");
                        ps.setString(1, newName);
                        ps.setInt(2, newQuantity);
                        ps.setInt(3, productId);
                        ps.executeUpdate();
                        loadProducts(); // This one is to Refresh the product list
                        JOptionPane.showMessageDialog(null, "Product updated successfully!");
                    } catch (SQLException | NumberFormatException ex) {
                        logger.severe("Error updating product: " + ex.getMessage());
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            }
        }

        // This one is to Action for deleting a product
        private void deleteProductAction(int row) {
            int productId = (int) productTable.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?",
                    "Delete Product", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement ps = connection.prepareStatement("DELETE FROM products WHERE id = ?");
                    ps.setInt(1, productId);
                    ps.executeUpdate();
                    loadProducts(); // This one is to Refresh the product list
                    JOptionPane.showMessageDialog(null, "Product deleted successfully!");
                } catch (SQLException ex) {
                    logger.severe("Error deleting product: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        }
    }

    // This one is to Load the products from the database and display them in the
    // table
    private void loadProducts() {
        try {
            tableModel.setRowCount(0); // This one is to Clear the existing rows
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM products WHERE user_id = ?");
            ps.setInt(1, loggedInUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = { rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), "Edit", "Delete" };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            logger.severe("Error loading products: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading products. Please try again.");
        }
    }

    // This one is the Action for registering a new user
    private class RegisterAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String firstName = registerFirstNameField.getText();
            String lastName = registerLastNameField.getText();
            String username = registerUsernameField.getText();
            String password = new String(((JPasswordField) registerPasswordField).getPassword());

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required.");
                return;
            }

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO users (first_name, last_name, username, password) VALUES (?, ?, ?, ?)");
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, username);
                ps.setString(4, password);
                ps.executeUpdate();
                cardLayout.show(mainPanel, "Login");
                JOptionPane.showMessageDialog(null, "User registered successfully! Please log in.");
            } catch (SQLException ex) {
                logger.severe("Error registering user: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    // This one is to the Action for logging in a user
    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = loginUsernameField.getText();
            String password = new String(((JPasswordField) loginPasswordField).getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username and password are required.");
                return;
            }

            try {
                PreparedStatement ps = connection
                        .prepareStatement("SELECT id FROM users WHERE username = ? AND password = ?");
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    loggedInUserId = rs.getInt("id");
                    loadProducts();
                    cardLayout.show(mainPanel, "Dashboard");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials.");
                }
            } catch (SQLException ex) {
                logger.severe("Error logging in: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    // This one is to the Action for adding a new product
    private class AddProductAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = productNameField.getText().trim();
            String quantityText = productQuantityField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Product name cannot be empty.");
                return;
            }
            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Product quantity cannot be empty.");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for quantity.");
                return;
            }

            try {
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO products (name, quantity, user_id) VALUES (?, ?, ?)");
                ps.setString(1, name);
                ps.setInt(2, quantity);
                ps.setInt(3, loggedInUserId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Product added successfully!");
                loadProducts(); // This one is to Refresh the product list
            } catch (SQLException ex) {
                logger.severe("Error adding product: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    // This one is the Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagementApp::new);
    }
}
