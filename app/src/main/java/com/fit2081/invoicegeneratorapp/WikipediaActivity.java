package com.fit2081.invoicegeneratorapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity that displays a Wikipedia page for a given item name.
 * This class extends AppCompatActivity and is responsible for rendering
 * a web view that loads a specific Wikipedia page based on the item name received
 * via intent from another activity.
 */
public class WikipediaActivity extends AppCompatActivity {

    /**
     * Initializes the activity, sets the content view, and configures the web view
     * to display the Wikipedia page of the item name passed to this activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down, this Bundle contains the data it most recently
     * supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity as defined in activity_wikipedia.xml
        setContentView(R.layout.activity_wikipedia);

        // Obtain the WebView component defined in the activity's layout
        WebView webView = findViewById(R.id.webview);

        // Retrieve the item name passed through the intent from the calling activity
        String itemName = getIntent().getStringExtra("ITEM_NAME");

        // Set up the WebView client to handle loading URLs inside the WebView component
        // rather than opening them in a browser
        webView.setWebViewClient(new WebViewClient());

        // Load the Wikipedia page for the item name. The URL is constructed by appending
        // the item name to the base URL of Wikipedia's English website
        webView.loadUrl("https://en.wikipedia.org/wiki/" + itemName);
    }
}
