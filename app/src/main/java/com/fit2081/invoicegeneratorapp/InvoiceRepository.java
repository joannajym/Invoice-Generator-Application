package com.fit2081.invoicegeneratorapp;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class for handling data operations for the Invoice entity.
 * This class abstracts the data sources from the rest of the app and provides a clean API for data access to the rest of the app.
 * It works with the InvoiceDAO to get and save data, and it also fetches data from the network if needed.
 */
public class InvoiceRepository {

    // Data Access Object for Invoice entity
    private InvoiceDAO mInvoiceDao;

    // LiveData list of all Invoices
    private LiveData<List<Invoice>> mAllInvoices;

    // Executor service with a fixed thread pool for handling database operations on a background thread
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Constructor for the InvoiceRepository.
     * Initialises the repository with the InvoiceDatabase and the InvoiceDAO.
     * Retrieves all invoices from the database and stores them in LiveData for observable changes.
     *
     * @param application The application context used for getting the database instance.
     */
    InvoiceRepository(Application application) {
        InvoiceDatabase db = InvoiceDatabase.getDatabase(application);
        mInvoiceDao = db.invoiceDAO();
        mAllInvoices = mInvoiceDao.getAllInvoices();
    }

    /**
     * Gets all invoices.
     * This method returns cached invoices as LiveData.
     * When the data changes, the observer is notified.
     *
     * @return LiveData containing the list of all invoices.
     */
    LiveData<List<Invoice>> getAllInvoices() {
        return mAllInvoices;
    }

    /**
     * Inserts an invoice into the database asynchronously
     * This method uses the ExecutorService to perform the database insert operation on a background thread.
     *
     * @param invoice The invoice to be inserted.
     */
    void insert(Invoice invoice) {
        databaseWriteExecutor.execute(() -> mInvoiceDao.addInvoice(invoice));
    }


    public void delete(Invoice invoice) {
        databaseWriteExecutor.execute(() -> {
            mInvoiceDao.deleteInvoice(invoice);
            Log.d("InvoiceRepository", "Invoice deleted: " + invoice.getInvoiceID());
        });
    }
}
