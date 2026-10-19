# Smart Pantry Manager

**Student:** Mmametlafrederick Moloto (402411236)
**Module:** Mobile App Development 700

## Description
Smart Pantry Manager is an Android application built in Java that helps users reduce food waste. It tracks the ingredients a user currently has in their pantry and suggests recipes that can be cooked using *strictly* those ingredients. A recipe is only suggested if every required ingredient is present in sufficient quantity.

## Database Choice
This app uses **SQLite**. 
**Why?** SQLite is a local, on-device database. It requires no internet connection, making it perfect for a kitchen app. It integrates natively with Android through the `SQLiteOpenHelper` class, ensuring data persists across app restarts securely and reliably.

## Setup and Run Instructions
1. Clone this repository to your local machine.
2. Open the project in **Android Studio**.
3. Allow Gradle to sync and download dependencies.
4. Run the app on an Android Emulator (API 24 or higher) or a physical Android device.
5. The database will automatically seed 18 recipes on the first launch.
