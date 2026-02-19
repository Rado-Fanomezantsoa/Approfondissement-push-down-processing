package org.expense;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();
        List<InvoiceTotal> invoiceTotals = retriever.findInvoiceTotals();

        for (InvoiceTotal total : invoiceTotals) {
            System.out.println(total);
        }

        InvoiceStatusTotals statusTotals = retriever.computeStatusTotals();

        System.out.println("total_paid = " + statusTotals.getTotalPaid());
        System.out.println("total_confirmed = " + statusTotals.getTotalConfirmed());
        System.out.println("total_draft = " + statusTotals.getTotalDraft());
    }
}