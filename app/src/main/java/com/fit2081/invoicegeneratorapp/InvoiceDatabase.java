package com.fit2081.invoicegeneratorapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract database class for the Invoice application.
 * Defines the database configuration and serves as the app's main access point to the live data.
 *
 * Uses the Room database library as an abstraction layer over SQLite to allow for more robust database access.
 */
@Database(entities = {Invoice.class}, version = 1, exportSchema = false)
public abstract class InvoiceDatabase extends RoomDatabase {

    /**
     * Returns the DAO (Data Access Object) for the Invoice entity
     *
     * @return The DAO object for Invoice
     */
    public abstract InvoiceDAO invoiceDAO();

    // Holds the singleton instance of the database
    private static volatile InvoiceDatabase INSTANCE;
    // Sets the number of threads for the database write executor
    private static final int NUMBER_OF_THREADS = 4;

    // Executor service with a fixed thread pool to handle database operations asynchronously
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets the singleton instance of the Invoice database.
     *
     * @param context The Context of the caller.
     * @return The singleton instance of the Invoice database.
     */
    static InvoiceDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (InvoiceDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    InvoiceDatabase.class, "invoice_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
