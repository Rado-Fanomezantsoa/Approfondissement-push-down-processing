package org.expense;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    //1
    public List<InvoiceTotal> findInvoiceTotals() {
        List<InvoiceTotal> totals = new ArrayList<>();
        String sql = "SELECT " +
                "    i.id, " +
                "    i.customer_name, " +
                "    i.status, " +
                "    COALESCE(SUM(il.quantity * il.unit_price), 0) AS total " +
                "FROM " +
                "    invoice i " +
                "LEFT JOIN " +
                "    invoice_line il ON i.id = il.invoice_id " +
                "GROUP BY " +
                "    i.id, " +
                "    i.customer_name, " +
                "    i.status " +
                "ORDER BY " +
                "    i.id;";


        try (Connection connection = new DbConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                String statusStr = rs.getString("status");
                InvoiceStatus status = InvoiceStatus.valueOf(statusStr); // Convert string to enum
                BigDecimal total = rs.getBigDecimal("total");

                totals.add(new InvoiceTotal(id, customerName, status, total));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return totals;
    }

    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        List<InvoiceTotal> totals = new ArrayList<>();
        String sql = "SELECT " +
                "    i.id, " +
                "    i.customer_name, " +
                "    i.status, " +
                "    COALESCE(SUM(il.quantity * il.unit_price), 0) AS total " +
                "FROM " +
                "    invoice i " +
                "LEFT JOIN " +
                "    invoice_line il ON i.id = il.invoice_id " +
                "WHERE " +
                "    i.status IN ('CONFIRMED', 'PAID') " +
                "GROUP BY " +
                "    i.id, " +
                "    i.customer_name, " +
                "    i.status " +
                "ORDER BY " +
                "    i.id;";
        try (Connection connection = new DbConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                String statusStr = rs.getString("status");
                InvoiceStatus status = InvoiceStatus.valueOf(statusStr); // Convert string to enum
                BigDecimal total = rs.getBigDecimal("total");
                totals.add(new InvoiceTotal(id, customerName, status, total));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totals;
    }
}