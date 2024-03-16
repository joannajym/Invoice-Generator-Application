package com.fit2081.invoicegeneratorapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/**
 * Data Access Object (DAO) for the invoice table.
 * This interface defines the standard operations to be performed on the Invoices table.
 */
@Dao
public interface InvoiceDAO {
    /**
     * Retrieves all invoices from the database.
     *
     * @return A LiveData list of all Invoice objects, allowing the UI to be notified of changes.
     */
    @Query("SELECT * FROM invoices")
    LiveData<List<Invoice>> getAllInvoices();

    /**
     * Retrieves a single invoice from the database, identified by its invoiceID.
     *
     * @param invoiceId The ID of the invoice to retrieve.
     * @return A LiveData containing the Invoice object if found, or null if not found.
     */
    @Query("SELECT * FROM invoices WHERE invoiceID = :invoiceId")
    LiveData<Invoice> getInvoice(String invoiceId);

    /**
     * Inserts an invoice into the database. If the invoice already exists, it will be ignored.
     *
     * @param invoice The Invoice object to be inserted.
     */
    @Insert
    void addInvoice(Invoice invoice);

    /**
     * Deletes a specific invoice from the database.
     *
     * @param invoice The Invoice object to delete.
     */
    @Delete
    void deleteInvoice(Invoice invoice);
}
