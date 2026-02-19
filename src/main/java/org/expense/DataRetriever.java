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

    public InvoiceStatusTotals computeStatusTotals() {
        InvoiceStatusTotals totals = null;
        String sql = "SELECT " +
                "    SUM(CASE WHEN i.status = 'PAID' THEN il.quantity * il.unit_price ELSE 0 END) AS total_paid, " +
                "    SUM(CASE WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price ELSE 0 END) AS total_confirmed, " +
                "    SUM(CASE WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price ELSE 0 END) AS total_draft " +
                "FROM " +
                "    invoice i " +
                "LEFT JOIN " +
                "    invoice_line il ON i.id = il.invoice_id;";
        try (Connection connection = new DbConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                BigDecimal totalPaid = rs.getBigDecimal("total_paid");
                BigDecimal totalConfirmed = rs.getBigDecimal("total_confirmed");
                BigDecimal totalDraft = rs.getBigDecimal("total_draft");
                totals = new InvoiceStatusTotals(totalPaid, totalConfirmed, totalDraft);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totals;
    }

    public Double computeWeightedTurnover() {
        Double weightedTotal = 0.0;
        String sql = "SELECT " +
                "    SUM(CASE " +
                "        WHEN i.status = 'PAID' THEN il.quantity * il.unit_price * 1.0 " +
                "        WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price * 0.5 " +
                "        WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price * 0.0 " +
                "        ELSE 0 " +
                "    END) AS weighted_total " +
                "FROM " +
                "    invoice i " +
                "JOIN " +
                "    invoice_line il ON i.id = il.invoice_id;";
        try (Connection connection = new DbConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                weightedTotal = rs.getDouble("weighted_total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weightedTotal;
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        List<InvoiceTaxSummary> summaries = new ArrayList<>();
        String sql = "SELECT " +
                "    i.id, " +
                "    COALESCE(SUM(il.quantity * il.unit_price), 0) AS ht, " +
                "    COALESCE(SUM(il.quantity * il.unit_price), 0) * (SELECT rate FROM tax_config WHERE label = 'TVA STANDARD') / 100 AS tva, " +
                "    COALESCE(SUM(il.quantity * il.unit_price), 0) * (1 + (SELECT rate FROM tax_config WHERE label = 'TVA STANDARD') / 100) AS ttc " +
                "FROM " +
                "    invoice i " +
                "LEFT JOIN " +
                "    invoice_line il ON i.id = il.invoice_id " +
                "GROUP BY " +
                "    i.id " +
                "ORDER BY " +
                "    i.id;";
        try (Connection connection = new DbConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                BigDecimal ht = rs.getBigDecimal("ht");
                BigDecimal tva = rs.getBigDecimal("tva");
                BigDecimal ttc = rs.getBigDecimal("ttc");
                summaries.add(new InvoiceTaxSummary(id, ht, tva, ttc));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return summaries;
    }
}