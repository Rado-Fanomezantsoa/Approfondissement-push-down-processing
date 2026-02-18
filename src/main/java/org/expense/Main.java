package org.expense;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();
        List<InvoiceTotal> invoiceTotals = retriever.findConfirmedAndPaidInvoiceTotals();

        for (InvoiceTotal total : invoiceTotals) {
            System.out.println(total);
        }

    }
}