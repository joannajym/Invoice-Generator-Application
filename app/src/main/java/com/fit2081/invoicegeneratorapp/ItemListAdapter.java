package com.fit2081.invoicegeneratorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of items.
 * This adapter binds the data about item (contained within the Item objects) to views that are displayed
 * within the RecyclerView.
 *
 * (For efficiently displaying a large set of dynamic data where each item in the list is represented by a complex layout)
 * (RecyclerView: Determines how many items are in the list and how to display each item.)
 * (ViewHolder: Improves performance by reducing the number of calls to findViewById and recycling view objects.)
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.InvoiceItemViewHolder> {

    // The list of items that will be displayed in the RecyclerView
    private List<Item> items;

    /**
     * Constructor for ItemListAdapter.
     *
     * @param items The list of items that the adapter will manage.
     */
    public ItemListAdapter(List<Item> items) {
        this.items = items;
    }

    /**
     * Called when RecylerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public InvoiceItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_card_layout, parent, false);
        return new InvoiceItemViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the ViewHolder's View to reflect the item at the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(InvoiceItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemNameView.setText(item.getItemName());
        holder.itemQuantityView.setText(String.valueOf(item.getItemQuantity()));
        holder.itemCostView.setText(String.valueOf(item.getItemCost()));
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     * InvoiceItemViewHolder holds the view for each item in the RecyclerView.
     */
    public static class InvoiceItemViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameView;
        public TextView itemQuantityView;
        public TextView itemCostView;

        /**
         * Constructor for the InvoiceItemViewHolder.
         *
         * @param itemView The view of the individual item.
         */
        public InvoiceItemViewHolder(View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameCard);
            itemQuantityView = itemView.findViewById(R.id.itemQuantityNumberCard);
            itemCostView = itemView.findViewById(R.id.itemCostCard);
        }
    }
}
