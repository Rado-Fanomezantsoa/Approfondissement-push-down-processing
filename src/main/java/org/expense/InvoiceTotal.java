package org.expense;

import java.math.BigDecimal;

public class InvoiceTotal {
    private int id;
    private String customerName;
    private InvoiceStatus status;
    private BigDecimal total;

    public InvoiceTotal(int id, String customerName, InvoiceStatus status, BigDecimal total) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = total;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    // Override toString to match the expected display format (id | name | total)
    @Override
    public String toString() {
        return id + " | " + customerName + " | " + total;
    }
}