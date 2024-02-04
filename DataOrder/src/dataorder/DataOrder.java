package dataorder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataOrder {

    private Connection connection;

    public DataOrder() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataorder", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getProductNames() {
        List<String> productNames = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT product_name FROM order_item")) {

            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                productNames.add(productName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productNames.toArray(new String[0]);
    }

    public void addNewOrder(String customerName, String employeeName, String productName, int quantity, int price) {
    try {
        
        String orderQuery = "INSERT INTO `order` (customer_name, employee_name, transaction_at) VALUES (?, ?, NOW())";
        try (PreparedStatement orderStatement = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
            orderStatement.setString(1, customerName);
            orderStatement.setString(2, employeeName);
            orderStatement.executeUpdate();
            
            ResultSet generatedKeys = orderStatement.getGeneratedKeys();
            int newOrderId = -1;
            if (generatedKeys.next()) {
                newOrderId = generatedKeys.getInt(1);
            }

            String orderItemQuery = "INSERT INTO order_item (order_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement orderItemStatement = connection.prepareStatement(orderItemQuery)) {
                orderItemStatement.setInt(1, newOrderId);
                orderItemStatement.setString(2, productName);
                orderItemStatement.setInt(3, quantity);
                orderItemStatement.setInt(4, price);
                orderItemStatement.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void showDataOrder(JTable table) {
    try {
        String query = "SELECT * FROM `order`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        while (resultSet.next()) {
            int order_id = resultSet.getInt("order_id");
            String tanggal_transaksi = resultSet.getString("transaction_at");
            String customer_name = resultSet.getString("customer_name");
            String employee_name = resultSet.getString("employee_name");

            model.addRow(new Object[]{order_id, tanggal_transaksi, customer_name, employee_name});
        }

        table.setModel(model);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void showDataOrderItem(JTable table, int order_id) {
        try {
            String query = "SELECT product_name, quantity, price FROM order_item WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, order_id);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                String product_name = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                int price = resultSet.getInt("price");

                model.addRow(new Object[]{product_name, quantity, price});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalTransaction(int order_id) {
        int total = 0;
        try {
            String query = "SELECT SUM(quantity * price) AS total FROM order_item WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, order_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void searchDataOrder(JTable table, String searchText) {
    try {
        String query = "SELECT * FROM `order` WHERE customer_name LIKE ? OR employee_name LIKE ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, "%" + searchText + "%");
        statement.setString(2, "%" + searchText + "%");
        ResultSet resultSet = statement.executeQuery();

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        while (resultSet.next()) {
            int order_id = resultSet.getInt("order_id");
            String tanggal_transaksi = resultSet.getString("transaction_at");
            String customer_name = resultSet.getString("customer_name");
            String employee_name = resultSet.getString("employee_name");

            model.addRow(new Object[]{order_id, tanggal_transaksi, customer_name, employee_name});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void addOrder(String customerName, String employeeName) {
        try {
            String query = "INSERT INTO `order` (customer_name, employee_name) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customerName);
            statement.setString(2, employeeName);
            statement.executeUpdate();

            // Mendapatkan order_id yang baru ditambahkan
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newOrderId = generatedKeys.getInt(1);
                System.out.println("Added new order with ID: " + newOrderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOrderItem(int orderId, String productName, int quantity, int price) {
        try {
            String query = "INSERT INTO order_item (order_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            statement.setString(2, productName);
            statement.setInt(3, quantity);
            statement.setInt(4, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
