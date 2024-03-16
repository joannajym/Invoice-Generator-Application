package com.fit2081.invoicegeneratorapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * InvoiceGeneratorActivity class responsible for generating and managing invoices.
 * Provides the functionality to create invoices with unique IDs, save and load invoice details,
 * and handle SMS commands related to invoices.
 */
public class InvoiceGeneratorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Declaring EditText fields
    EditText issuerName, buyerName, buyerAdd, itemName, itemQuantityNumber, itemCostNumberDecimal;
    // Declaring Button
//    Button saveButton;
    Button wikiButton;

    // Declaring Switch
    Switch paidSwitch;
    //Declaring Toolbar
    Toolbar toolbar;
    // Declaring SharedPreferences for storing invoice data
    private SharedPreferences sharedPreferences;
    //Declaring item list to store items of an invoice and variable to store its total amount
    private List<Item> items = new ArrayList<>();
    private double invoiceTotal = 0.0;
    // Declaring RecyclerView and Adapter
    private RecyclerView recyclerView;
    // Declaring Adapter for RecyclerView
    private ItemListAdapter adapter;
    //ViewModel for handling data related to invoices
    private InvoiceViewModel invoiceViewModel;
    // Declaring navigation drawer
    private DrawerLayout drawer;
    //Declaring gesture detector
    private GestureDetector gestureDetector;

    /**
     * onCreate method called when the activity is starting.
     * Initialises the activity, UI components, ViewModel, and even listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Initialize fields and Switch
        issuerName = findViewById(R.id.issuerNameID);
        buyerName = findViewById(R.id.buyerNameID);
        buyerAdd = findViewById(R.id.buyerAddressID);
        itemName = findViewById(R.id.itemNameID);
        itemQuantityNumber = findViewById(R.id.itemQuantityID);
        itemCostNumberDecimal = findViewById(R.id.itemCostID);
        paidSwitch = findViewById(R.id.paidSwitch);

        // Initialise toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialise RecyclerView
        recyclerView = findViewById(R.id.items_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise Adapter with list of items
        adapter = new ItemListAdapter(items);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);

//        // Comment out Invoice Fragment
//        // Add InvoiceListFragment
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, new InvoiceListFragment())
//                .commit();

        // Initialises FAB
        FloatingActionButton fab = findViewById(R.id.fab_add_invoice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Collect all items and calculate total
                calculateTotal();

                // Create a new Invoice object
                Invoice newInvoice = new Invoice(generateInvoiceId(), issuerName.getText().toString(), buyerName.getText().toString(), invoiceTotal);

                // Insert the new invoice into the database using the ViewModel
                invoiceViewModel.insert(newInvoice);

                Toast.makeText(InvoiceGeneratorActivity.this, "Your invoice has been added.", Toast.LENGTH_SHORT).show();

                // Clear the items list and reset the total for the next invoice
                items.clear();
                adapter.notifyDataSetChanged(); // Notify the adapter to reflect the changes in the UI
                invoiceTotal = 0.0; // Reset the invoice total
                updateInvoiceTotalInView(); // Update the invoice total TextView
            }
        });

        // Initialise Navigation Drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Sets the NavigationItemSelectedListener to the current Activity to handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(this);

        // Setting up ActionBarDrawerToggle
        // This toggle will sync the state of the drawer (open or closed) with the state of the app bar's home/up button.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, // Host activity
                drawer, // DrawerLayout object
                toolbar, // Toolbar to link the DrawerLayout to
                R.string.navigation_drawer_open, // Description for accessibility
                R.string.navigation_drawer_close);

        // Add the toggle as a DrawerListener to the DrawerLayout
        drawer.addDrawerListener(toggle);

        // Synchronise the state of the drawer indicator with the linked DrawerLayout
        toggle.syncState();

        // Initialise Wiki Button and set onClickListener
        // When the Wiki Button is clicked, it initiates and an Intent to start the WikipediaActivity
        // passing the current item name as an extra in the intent
        wikiButton = findViewById(R.id.btnWiki);
        wikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                // Gets the item name from the EditText field
                String itemNameStr = itemName.getText().toString();

                // Creates an Intent to start WikipediaActivity
                Intent intent = new Intent(InvoiceGeneratorActivity.this, WikipediaActivity.class);

                // Put the item name as an extra in the Intent
                intent.putExtra("ITEM_NAME", itemNameStr);

                // Start the WikipediaActivity
                startActivity(intent);
            }
        });

        // Set up GestureDetector with the custom gesture listener
        // GestureDetector is used to detect and handle different gestures as double taps
        gestureDetector = new GestureDetector(this, new CustomGestureListener());

        // Find the FrameLayout that will be used as the touchpad for gesture detection
        FrameLayout touchpad = findViewById(R.id.fragment_container);

        // Sets an OnTouchListener on the touchpad
        // The OnTouchListener is used to delegate touch events to the GestureDetector
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Delegates the touch event to the GestureDetector.
                gestureDetector.onTouchEvent(event);

                // Returns true to indicate that the touch event is handled
                return true;
            }
        });

        // Comment out as save button is removed and not used anymore
        // Initialize Save Button and set onClickListener
        // When Save button is clicked, it will trigger the generation of unique IDs
        // for the invoice, buyer, and item based on the input value
//        saveButton = findViewById(R.id.saveButton);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Generates unique identifiers for invoice, buyer, and item
//                String invoiceId = generateInvoiceId();
//                String buyerId = generateBuyerId(buyerName.getText().toString());
//                String itemId = generateItemId(itemName.getText().toString());
//
//                // Creates a message string containing the generated IDs
//                String message = "InvoiceID: " + invoiceId + ", BuyerID: " + buyerId + ", ItemID: " + itemId;
//
//                // Displays the message in a Toast
//                Toast.makeText(InvoiceGeneratorActivity.this, message, Toast.LENGTH_LONG).show();
//            }
//        });

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        /* Create and instantiate the local broadcast receiver
           This class listens to messages come from class SMSReceiver
         */
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

    }

    /**
     * Generates a unique Invoice ID
     * Format: 'I' followed by 2 random letters and a hyphen, then 4 random digits.
     * Example: "IA-1234"
     *
     * @return A unique Invoice ID string.
     */
    private String generateInvoiceId() {
        return "I" + getRandomCharacters(2) + "-" + getRandomDigits(4);
    }

    /**
     * Generates a BuyerID based on the buyer's name.
     * Format: 'B' followed by the first two characters of the buyer's name and a hyphen, then 3 random digits.
     * Example: "BJO-123" for a buyer names "John"
     *
     * @param buyerName The name of the buyer
     * @return A unique Buyer ID string
     */
    private String generateBuyerId(String buyerName) {
        // If buyer's name is less than 2 characters, default it to 'XX'
        if (buyerName.length() < 2) {
            buyerName = "XX"; // Default in case name is too short
        }
        return "B" + buyerName.substring(0, 2).toUpperCase() + "-" + getRandomDigits(3);
    }

    /**
     * Generates an Item ID based on the item's name.
     * Format: 'T' followed by the first two characters of the item's name and a hyphen, then 4 random digits.
     * Example: "TC-1234" for an item named "Chair"
     *
     * @param itemName The name of the item.
     * @return A unique Item ID string.
     */
    private String generateItemId(String itemName) {
        // If item name is less than 2 characters, default it to 'XX'
        if (itemName.length() < 2) {
            itemName = "XX"; // Default in case name is too short
        }
        return "T" + itemName.substring(0, 2).toUpperCase() + "-" + getRandomDigits(4);
    }

    /**
     * Helper method that generates a string of random alphabetic characters.
     *
     * @param length The desired length of the string.
     * @return A string of random uppercase alphabetic characters.
     */
    private String getRandomCharacters(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    /**
     * Helper method that generates a string of random digits.
     *
     * @param length The desired length of the string.
     * @return A string of random digits
     */
    private String getRandomDigits(int length) {
        String digits = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(digits.charAt(random.nextInt(digits.length())));
        }
        return sb.toString();
    }

    /**
     * Saves the current invoice details to SharedPreferences.
     * This method stores all the details entered by the user including: issuer name, buyer name, buyer address,
     * item name, item quantity, item cost and payment status.
     *
     */
    private void saveInvoice() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("issuerName", issuerName.getText().toString());
        editor.putString("buyerName", buyerName.getText().toString());
        editor.putString("buyerAddress", buyerAdd.getText().toString());
        editor.putString("itemName", itemName.getText().toString());
        editor.putString("itemQuantity", itemQuantityNumber.getText().toString());
        editor.putString("itemCost", itemCostNumberDecimal.getText().toString());
        editor.putBoolean("isPaid", paidSwitch.isChecked());
        editor.apply();

        Toast.makeText(this, "Invoice saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads the previosly saved invoice details from SharedPreferences.
     * This method retrieves and sets the invoice details in the respective UI components.
     */
    private void loadInvoice() {
        issuerName.setText(sharedPreferences.getString("issuerName", ""));
        buyerName.setText(sharedPreferences.getString("buyerName", ""));
        buyerAdd.setText(sharedPreferences.getString("buyerAddress", ""));
        itemName.setText(sharedPreferences.getString("itemName", ""));
        itemQuantityNumber.setText(sharedPreferences.getString("itemQuantity", ""));
        itemCostNumberDecimal.setText(sharedPreferences.getString("itemCost", ""));
        paidSwitch.setChecked(sharedPreferences.getBoolean("isPaid", false));

        Toast.makeText(this, "Invoice loaded", Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialises the contents of the Activity's standard options menu.
     * This is only called once, the first time the options menu is displayed.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return Returns true for the menu to be displayed; if return false, it will not be shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks. The action bar will automatically handle whenever an item in the
     * options menu is selected.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to proceed,
     * returns true to indicate that the menu selection event has been handled and should not be processed further.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Handles click on the home button in the action bar
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            // Handles clicks for add item
            case R.id.action_add_item:
                addItem();
                return true;
            // Handles clicks for clear all fields
            case R.id.action_clear_all_fields:
                clearFields();
                return true;
            // If no cases matches the ID of selected menu item, returns the default case
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Adds a new item to the invoice based on the user input.
     * Validates the input, creates a new Item object, and adds it to the items list.
     * And updates the UI accordingly.
     */
    private void addItem() {
        // Retrieve item details from input fields
        String itemNameStr = itemName.getText().toString();
        int itemQuantityInt;
        double itemCostDbl;

        // Checks if Quantity and Cost are Integers and Double data type
        try {
            itemQuantityInt = Integer.parseInt(itemQuantityNumber.getText().toString());
            itemCostDbl = Double.parseDouble(itemCostNumberDecimal.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Input is invalid. Please try again.", Toast.LENGTH_SHORT).show();
            return; // Exit the method early if parsing fails
        }

        // Check if the item name is empty
        if (itemNameStr.trim().isEmpty()) {
            Toast.makeText(this, "Please enter an item name.", Toast.LENGTH_SHORT).show();
            return; // Exit the method early if the item name is empty
        }

        // Create a new Item and add it to the item list
        Item newItem = new Item(itemNameStr, itemQuantityInt, itemCostDbl);
        items.add(newItem);
        adapter.notifyItemInserted(items.size() - 1);

        // Recalculate the invoice total and update the UI
        calculateTotal();
    }

    /**
     * Clears all input fields in the UI to reset the form.
     * And also resets the ;paid; switch to its default state.
     */
    private void clearFields() {
        issuerName.setText("");
        buyerName.setText("");
        buyerAdd.setText("");
        itemName.setText("");
        itemQuantityNumber.setText("");
        itemCostNumberDecimal.setText("");
        paidSwitch.setChecked(false);
    }

    /**
     * Calculates the total cost of the invoice by summing up the cost of all items.
     * Updates the invoiceTotal field and the UI to reflect the new total.
     */
    private void calculateTotal() {
        invoiceTotal = 0.0; // Resets the total

        // Calculation for the invoiceTotal
        for (Item item : items) {
            invoiceTotal += item.getItemCost() * item.getItemQuantity();
        }
        // Update the invoice total TextView
        updateInvoiceTotalInView();
    }

    /**
     * Updates the TextView that displays the invoice's total cost.
     * Formats the total to ensure it has two decimal places.
     */
    private void updateInvoiceTotalInView() {
        // Format the invoice total to ensure it has two decimal places
        String formattedTotal = String.format(Locale.getDefault(), "%.2f", invoiceTotal);
        // Find the TextView by its ID and update its text to the new total
        TextView invoiceTotalTextView = findViewById(R.id.invoiceTotalID);
        invoiceTotalTextView.setText(formattedTotal);
    }

    /**
     * Handles navigation item clicks in the navigation drawer.
     * Different actions are performed based on the options clicked.
     *
     * @param item The selected item
     * @return Returns true to display the menu; false to hide it.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_add_item:
                addItem();
                break;
            case R.id.nav_add_invoice:
                addInvoice();
                break;
            case R.id.nav_clear_fields:
                clearFields();
                break;
            case R.id.nav_items_map:
                itemsMap();
                break;
            case R.id.nav_list_invoices:
                listInvoices();
                break;
            case R.id.nav_exit_app:
                exitApp();
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Collects all items, calculates the total, and creates a new Invoice object.
     * Inserts the new invoice into the database and updates the UI.
     */
    private void addInvoice() {
        // Collect all items and calculate total
        calculateTotal();

        // Create a new Invoice object
        Invoice newInvoice = new Invoice(generateInvoiceId(), issuerName.getText().toString(), buyerName.getText().toString(), invoiceTotal);

        // Insert the new invoice into the database using the ViewModel
        invoiceViewModel.insert(newInvoice);

        Toast.makeText(InvoiceGeneratorActivity.this, "Your invoice has been added.", Toast.LENGTH_SHORT).show();

        // Clear the items list and reset the total for the next invoice
        items.clear();
        adapter.notifyDataSetChanged(); // Notify the adapter to reflect the changes in the UI
        invoiceTotal = 0.0; // Reset the invoice total
        updateInvoiceTotalInView(); // Update the invoice total TextView

        // Clear all input fields
        clearFields();
    }

    /**
     * Starts an activity to display the list of all invoices.
     */
    private void listInvoices() {
        Intent intent = new Intent(this, ListInvoicesActivity.class);
        startActivity(intent);
    }

    /**
     * Closes all activities and exists the app.
     */
    private void exitApp() {
        finishAffinity(); // Close all activities and exit the app
    }

    /**
     * Starts the ItemMapsActivity
     * Responsible for initiating an Intent to start the ItemsMapActivity
     */
    private void itemsMap() {
        Intent mapIntent = new Intent(this, ItemsMapActivity.class);
        startActivity(mapIntent);
    }

    /**
     * Custom gesture listener class extending from GestureDetector.SimpleGestureListener
     * Handles various gesture events, such as double taps, long taps, and scrolls,
     * and triggers the respective actions based on these gestures.
     */
    private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
        // Threshold for triggering cost and quantity changes
        private static final float SCROLL_THRESHOLD = 100;

        /**
         * Handles the double tap gesture.
         * On double tap, an invoice is added to the database
         *
         * @param e The down motion event of the first tap of the double-tap.
         * @return boolean Returns true to indicate the event was handled.
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Call addInvoice method to handle invoice creation and insertion
            addInvoice();

            // Shows a confirm message toast
            Toast.makeText(InvoiceGeneratorActivity.this, "Double Tap: Invoice Added", Toast.LENGTH_SHORT).show();
            return true;
        }

        /**
         * Handles the long press gesture.
         * On a long press, a new item is added.
         *
         * @param e The initial on down motion event that started the longpress.
         */
        @Override
        public void onLongPress(MotionEvent e) {
            // Adds a new item to the invoice
            addItem();

            // Shows a confirmation message
            Toast.makeText(InvoiceGeneratorActivity.this, "Long Press: Item Added", Toast.LENGTH_SHORT).show();
        }

        /**
         * Handles the scroll gesture.
         * Horizontal scrolls adjust the item cost, and vertical scrolls adjust the item quantity.
         *
         * @param e1 The first down motion event that started the scrolling. A {@code null} event
         *           indicates an incomplete event stream or error state.
         * @param e2 The move motion event that triggered the current onScroll.
         * @param distanceX The distance along the X axis that has been scrolled since the last
         *              call to onScroll. This is NOT the distance between {@code e1}
         *              and {@code e2}.
         * @param distanceY The distance along the Y axis that has been scrolled since the last
         *              call to onScroll. This is NOT the distance between {@code e1}
         *              and {@code e2}.
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Threshold to differentiate between horizontal and vertical scroll
            final float threshold = 50;

            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                // Horizontal scroll: adjusting item cost
                adjustItemCost(distanceX);
            } else {
                // Vertical scroll: Adjusting item quantity
                adjustItemQuantity(distanceY);
            }
            return true;
        }

        /**
         * Adjusts the cost of an item based on the scroll distance.
         *
         * @param distanceX The horizontal distance scrolled
         */
        private void adjustItemCost(float distanceX) {
            try {
                // Converts the string EditText to a double data type
                double currentCost = Double.parseDouble(itemCostNumberDecimal.getText().toString());
                // Adjust cost based on the distance of scroll
                currentCost += distanceX > 0 ? -1 : 1;
                currentCost = Math.max(currentCost, 0); // Ensure cost doesn't go below 0
                itemCostNumberDecimal.setText(String.format(Locale.getDefault(), "%.2f", currentCost));
            } catch (NumberFormatException e) { // Handles exception
                Toast.makeText(InvoiceGeneratorActivity.this, "Cost is invalid. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Adjusts the quantity of an item based on the scroll distance
         *
         * @param distanceY the vertical distance scrolled.
         */
        private void adjustItemQuantity(float distanceY) {
            try {
                int currentQuantity = Integer.parseInt(itemQuantityNumber.getText().toString());
                // Adjust quantity based on the distance of scroll
                currentQuantity += distanceY > 0 ? -1 : 1;
                currentQuantity = Math.max(currentQuantity, 0); // Ensure quantity doesn't go below 0
                itemQuantityNumber.setText(String.valueOf(currentQuantity));
            } catch (NumberFormatException e) {
                Toast.makeText(InvoiceGeneratorActivity.this, "Quantity is invalid. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Delegates touch events to the gesture detector.
     * This method overrides to ensure that gestureDetector can handle touch events
     *
     * @param event The touch screen event being processed.
     *
     * @return boolean Return true if the event was handled by the gestureDetector, else calls the superclass implementation
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * Inner class that extends BroadcastReceiver
     * Handles incoming SMS messages and performs actions based on the message content.
     * Processes invoice details, item details and commands to save or load invoices.
     */
    class MyBroadCastReceiver extends BroadcastReceiver {
        /**
         * Called when the BroadcastReceiver is receiving an Intent broadcast.
         * Different actions are taken depending on the SMS message received.
         *
         * @param context The Context in which the receiver is running.
         * @param intent The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extracts the message from the intent
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            // Checks the prefix of the message ti determine the type of SMS and handle accordingly
            if (msg.startsWith("invoice:")) {
                // Handles invoice related messages
                handleInvoiceMessage(msg.substring(8));
            } else if (msg.startsWith("item:")) {
                // Handles item related messages
                handleItemMessage(msg.substring(5));
            } else if (msg.equals("save:invoice")) {
                // Saves the invoice details to SharedPreferences
                saveInvoice();
            } else if (msg.equals("load:invoice")) {
                // Load the invoice details from SharedPreferences
                loadInvoice();
            } else {
                // Shows toast if command is not found or unrecognisable
                Toast.makeText(context, "Unrecognized SMS command", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Handles the processing of an invoice message.
         * Parses the message for the invoice attributes and updates the UI components.
         *
         * @param message The invoice message string received via SMS
         */
        private void handleInvoiceMessage(String message) {
            try {
                // Splits the message into tokens using a semicolon as the delimiter
                StringTokenizer sT = new StringTokenizer(message, ";");

                // Extracts and sets the respective invoice details from the tokens
                String issuerNameToken = sT.nextToken();
                String buyerNameToken = sT.nextToken();
                String buyerAddressToken = sT.nextToken();
                String paidSwitchToken = sT.nextToken();

                issuerName.setText(issuerNameToken);
                buyerName.setText(buyerNameToken);
                buyerAdd.setText(buyerAddressToken);
                paidSwitch.setChecked(Boolean.parseBoolean(paidSwitchToken));
            } catch (Exception e) {
                // Displays error message in a case of a format exception.
                Toast.makeText(InvoiceGeneratorActivity.this, "Error in invoice SMS format", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Handles the processing of an item message.
         * Parses the message for different item attributes and updates the UI components.
         *
         * @param message The item message string received via SMS.
         */
        private void handleItemMessage(String message) {
            try {
                // Splits the message into tokens using a semi colong as the delimiter
                StringTokenizer sT = new StringTokenizer(message, ";");

                // Extracts and set the respective item details from the tokens
                String itemNameToken = sT.nextToken();
                String itemQuantityToken = sT.nextToken();
                String itemCostToken = sT.nextToken();

                itemName.setText(itemNameToken);
                itemQuantityNumber.setText(itemQuantityToken);
                itemCostNumberDecimal.setText(itemCostToken);
            } catch (Exception e) {
                // Displays error message in case of a format exception
                Toast.makeText(InvoiceGeneratorActivity.this, "Error in item SMS format", Toast.LENGTH_SHORT).show();
            }
        }
    }
}