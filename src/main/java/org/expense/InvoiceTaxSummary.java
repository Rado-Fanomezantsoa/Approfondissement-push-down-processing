package org.expense;

import java.math.BigDecimal;

public class InvoiceTaxSummary {
    private int id;
    private BigDecimal ht;
    private BigDecimal tva;
    private BigDecimal ttc;

    public InvoiceTaxSummary(int id, BigDecimal ht, BigDecimal tva, BigDecimal ttc) {
        this.id = id;
        this.ht = ht;
        this.tva = tva;
        this.ttc = ttc;
    }

    // Getters
    public int getId() {
        return id;
    }

    public BigDecimal getHt() {
        return ht;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public BigDecimal getTtc() {
        return ttc;
    }

    // Override toString to match the expected display format
    @Override
    public String toString() {
        return id + " | HT " + ht + " | TVA " + tva + " | TTC " + ttc;
    }
}
