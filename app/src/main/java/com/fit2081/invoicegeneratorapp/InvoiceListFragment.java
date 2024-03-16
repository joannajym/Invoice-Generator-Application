package com.fit2081.invoicegeneratorapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A Fragment subclass for displaying a list of invoices.
 * This fragment handles the UI and functionality for displaying invoices in a RecyclerView,
 * and it interacts with InvoiceViewModel to observe changes in invoice data.
 */
public class InvoiceListFragment extends Fragment {

    // RecyleView for displaying the list of invoices
    private RecyclerView recyclerView;

    // Adapter for the RecyclerView
    private InvoiceListAdapter adapter;

    // ViewModel for observing and managing invoice-related data
    private InvoiceViewModel invoiceViewModel;

    /**
     * Required empty public constructor for instantiating the fragment
     */
    public InvoiceListFragment() {
        // Required empty public constructor
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (default implementation).
     * This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Returns the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_invoice_list, container, false);

        // Initialises the RecyclerView and set its layout manager
        recyclerView = rootView.findViewById(R.id.invoice_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the adapter with an empty list and set it to the RecyclerView
        adapter = new InvoiceListAdapter(new ArrayList<>(), (InvoiceListAdapter.OnInvoiceClickListener) getActivity());
        recyclerView.setAdapter(adapter);

        // Get a new or existing ViewModel from the ViewModelProvider
        invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);

        // Observe changes in the list of invoices and update the adapter when the data changes
        invoiceViewModel.getAllInvoices().observe(getViewLifecycleOwner(), invoices -> {
            // Update the cached copy of the invoices in the adapter.
            adapter.setInvoices(invoices);
        });

        return rootView;
    }
}
