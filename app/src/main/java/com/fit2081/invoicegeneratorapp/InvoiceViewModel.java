package com.fit2081.invoicegeneratorapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * ViewModel for the Invoice entity.
 * This class is designed to store and manage UI-related data in a lifecycle-conscious way.
 * The ViewModel allows data to survive configuration changes such as screen rotations.
 * It acts as a communication center between the Repository and the UI.
 * (Requests and receives data from the Repository (data layer) and the UI (view layer),
 * ensuring that the UI reacts properly to any data changes and survives configuration changes within the app)
 */
public class InvoiceViewModel extends AndroidViewModel {

    // Repository for handling data operations
    private InvoiceRepository mRepository;
    // LiveData for observing changes in the list of all invoices.
    private LiveData<List<Invoice>> mAllInvoices;

    /**
     * Constructor for InvoiceViewModel
     * Initialises the repository and sets up LiveData for observing changes in the list of invoices.
     *
     * @param application The application that this ViewModel is associated with.
     */
    public InvoiceViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InvoiceRepository(application); // Initialises the repository
        mAllInvoices = mRepository.getAllInvoices(); // Gets LiveData reference for observing invoice data changes
    }

    /**
     * Exposes the LiveData list of invoices so the UI can observe it.
     *
     * @return LiveData that contains the list of all invoices.
     */
    public LiveData<List<Invoice>> getAllInvoices() {
        return mAllInvoices;
    }

    /**
     * Wrapper for the insert method in the repository.
     * Calls the repository's insert method to add a new invoice to the database.
     *
     * @param invoice The invoice to be inserted into the database.
     */
    public void insert(Invoice invoice) {
        mRepository.insert(invoice);
    }

    public void delete(Invoice invoice) {
        mRepository.delete(invoice);
    }
}
