package com.fit2081.invoicegeneratorapp;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class ListInvoicesActivity extends AppCompatActivity implements InvoiceListAdapter.OnInvoiceClickListener {
    private InvoiceViewModel invoiceViewModel;
    private RecyclerView recyclerView;
    private InvoiceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_invoices);

        // Initialise the InvoiceView Model
        invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);

        // Add InvoiceListFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, InvoiceListFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void onInvoiceClick(Invoice invoice) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_container_view),
                        "Delete this invoice?", Snackbar.LENGTH_LONG)
                .setAction("DELETE", v -> deleteInvoice(invoice));

        // Change the action button text color
        snackbar.setActionTextColor(Color.WHITE);

        // Display the Snackbar
        snackbar.show();
    }

    private void deleteInvoice(Invoice invoice) {
        invoiceViewModel.delete(invoice);
    }
}
