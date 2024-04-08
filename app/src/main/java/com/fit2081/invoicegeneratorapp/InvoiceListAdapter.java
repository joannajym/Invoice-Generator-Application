package com.fit2081.invoicegeneratorapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter class for RecyclerView to display a list of invoices.
 * This class extends the RecyclerView.Adapter and binds the Invoice data to views that
 * are displayed within a RecyclerView.
 */
public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.InvoiceViewHolder> {

    private List<Invoice> invoiceList; // List to hold Invoice objects.
    private OnInvoiceClickListener listener; // Listener for invoice click events.

    /**
     * Constructor for InvoiceListAdapter.
     *
     * @param invoiceList List of Invoice objects to be displayed.
     * @param listener Listener for handling clicks on invoice items.
     */
    public InvoiceListAdapter(List<Invoice> invoiceList, OnInvoiceClickListener listener) {
        this.invoiceList = invoiceList;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link InvoiceViewHolder} of the given type to represent
     * an item. This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML layout file.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_card_layout, parent, false);
        return new InvoiceViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link InvoiceViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at
     *               the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);
        holder.invoiceIdTextView.setText(invoice.getInvoiceID());
        holder.issuerNameTextView.setText(invoice.getIssuerName());
        holder.buyerNameTextView.setText(invoice.getBuyerName());
        holder.totalTextView.setText(String.valueOf(invoice.getInvoiceTotal()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onInvoiceClick(invoice);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    /**
     * Updates the list of invoices and notifies the adapter to refresh the display.
     *
     * @param newInvoices The new list of invoices to display.
     */
    public void setInvoices(List<Invoice> newInvoices) {
        invoiceList = newInvoices;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for layout of each item in the RecyclerView.
     */
    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView invoiceIdTextView, issuerNameTextView, buyerNameTextView, totalTextView;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            invoiceIdTextView = itemView.findViewById(R.id.invoiceIDCard);
            issuerNameTextView = itemView.findViewById(R.id.issuerNameCard);
            buyerNameTextView = itemView.findViewById(R.id.buyerNameCard);
            totalTextView = itemView.findViewById(R.id.invoiceTotalCard);
        }
    }

    /**
     * Interface for handling click events on invoice items.
     */
    public interface OnInvoiceClickListener {
        void onInvoiceClick(Invoice invoice);
    }
}
