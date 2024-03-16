# Invoice-Generator-Application
This Invoice Generator App is a mobile application developed using Android Studio, designed to simplify the creation and management of invoices directly from Android devices. The main features of this app includes the dasboard for invoice management and generating of invoices, calculating the total items of the invoices, and saving them to a database.

## Table of Contents
- [Installation](#installation)
- [Features](#features)

## Installation

To install the app, follow these steps:

1. Clone the repository to your local machine
2. Open the project in Android Studio
3. Connect an Android device or use the Android Emulator
4. Build and run the application

## Features
- **User Registration and Login**: Securely manage access with a registration form and login activity, storing credentials locally using SharedPreferences.
- **Dashboard for Invoice Management**: A user-friendly interface for entering new invoices and their items.
- **SMS Integration**: Automatically parse invoice and item details from incoming SMS messages, supporting commands like `Fill Invoice`, `Fill Item`, `Save Invoice`, and `Load Invoice`.
- **Persistent Storage**: Save and load invoices using SharedPreferences, with functionality to handle single credentials and invoice data.
- **Responsive Design**: Ensure usability across device orientations.
- **Enhanced User Interface**: Incorporate a navigation drawer, options menu, and Floating Action Button (FAB) for navigation and operation.
- **Data Display and Management**: Utilize RecyclerViews for listing items and invoices, and support invoice deletion via user interaction.
- **External Linking**: Include a feature to search item information on Wikipedia.
- **Interactive Gestures**: Integrate gestures for intuitive control over invoice item details.
- **Location Services**: Embed Google Maps with functionality to identify and display selected countries.
