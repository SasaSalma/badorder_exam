package dataorder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApp extends JFrame {

    private DataOrder dataOrder;

    private JTable table1, table2;
    private DefaultTableModel tableModel1, tableModel2;
    private JLabel totalLabel;
    private JTextField searchField, orderIdField, transactionDateField, customerNameField, employeeNameField, quantityField, priceField, productNameField;
    private JButton searchButton, addButton;
    private JPanel inputPanel;

    public MainApp() {
        dataOrder = new DataOrder();

        setTitle("Data Order Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        tableModel1 = new DefaultTableModel(new String[]{"Order ID", "Transaction Date", "Customer Name", "Employee Name"}, 0);
        table1 = new JTable(tableModel1);

        tableModel2 = new DefaultTableModel(new String[]{"Product Name", "Quantity", "Price"}, 0);
        table2 = new JTable(tableModel2);

        JScrollPane scrollPane1 = new JScrollPane(table1);
        JScrollPane scrollPane2 = new JScrollPane(table2);

        totalLabel = new JLabel("Total Transaction: ");

        searchField = new JTextField();
        searchButton = new JButton("Search");

        searchField.setPreferredSize(new Dimension(120, 25));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        productNameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();

        orderIdField = new JTextField();
        transactionDateField = new JTextField();
        customerNameField = new JTextField();
        employeeNameField = new JTextField();

        addButton = new JButton("Add");

        createInputPanel();

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.add(new JLabel("Data Order Management"));
        headerPanel.add(searchField);
        headerPanel.add(searchButton);

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(new JScrollPane(table1));
        tablePanel.add(new JScrollPane(table2));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(totalLabel);

        setLayout(new BorderLayout(10, 10));
        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(totalPanel, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.EAST);

        table1.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                int orderID = (int) table1.getValueAt(selectedRow, 0);
                dataOrder.showDataOrderItem(table2, orderID);
                updateTotalLabel(orderID);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewOrder();
            }
        });

        refreshData();

        setVisible(true);
    }

    private void createInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Order"));

        JPanel orderPanel = new JPanel(new GridLayout(6, 4, 5, 5));
        orderPanel.add(new JLabel("Order ID: "));
        orderPanel.add(orderIdField);
        orderPanel.add(new JLabel("Transaction Date: "));
        orderPanel.add(transactionDateField);
        orderPanel.add(new JLabel("Customer Name: "));
        orderPanel.add(customerNameField);
        orderPanel.add(new JLabel("Employee Name: "));
        orderPanel.add(employeeNameField);

        JPanel itemPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        itemPanel.add(new JLabel("Product Name: "));
        itemPanel.add(productNameField);
        itemPanel.add(new JLabel("Quantity: "));
        itemPanel.add(quantityField);
        itemPanel.add(new JLabel("Price: "));
        itemPanel.add(priceField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        Dimension inputFieldSize = new Dimension(120, 25);
        setFieldSize(orderIdField, inputFieldSize);
        setFieldSize(transactionDateField, inputFieldSize);
        setFieldSize(customerNameField, inputFieldSize);
        setFieldSize(employeeNameField, inputFieldSize);
        setFieldSize(productNameField, inputFieldSize);
        setFieldSize(quantityField, inputFieldSize);
        setFieldSize(priceField, inputFieldSize);

        inputPanel.add(orderPanel);
        inputPanel.add(itemPanel);
        inputPanel.add(buttonPanel);
    }

    private void setFieldSize(JTextField field, Dimension size) {
        field.setPreferredSize(size);
        field.setMaximumSize(size);
    }

    private void addNewOrder() {
        try {
            String customerName = customerNameField.getText();
            String employeeName = employeeNameField.getText();
            String productName = productNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int price = Integer.parseInt(priceField.getText());

            dataOrder.addNewOrder(customerName, employeeName, productName, quantity, price);

            refreshData();
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields() {
        orderIdField.setText("");
        transactionDateField.setText("");
        customerNameField.setText("");
        employeeNameField.setText("");
        productNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    private void performSearch() {
        String searchText = searchField.getText();
        dataOrder.searchDataOrder(table1, searchText);
        showDataOrder(table1);
        refreshData();
    }

    private void updateTotalLabel(int orderID) {
        int total = dataOrder.getTotalTransaction(orderID);
        totalLabel.setText("Total Transaction: " + total);
    }

    private void refreshData() {
        tableModel2.setRowCount(0);
        totalLabel.setText("Total Transaction: ");
        table1.revalidate();
        table1.repaint();
        showDataOrder(table1);
    }

    private void showDataOrder(JTable table) {
        dataOrder.showDataOrder(table);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
