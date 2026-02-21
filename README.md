# Music Player

A simple, offline music player application for Android, built with Java. This project was developed as a learning exercise to understand core Android development concepts, from UI design with XML to media playback and resource management.

## Features

- **Dynamic Music Loading**: Automatically finds and loads all audio files placed in the `res/raw` directory.
- **Full Playback Control**: Includes Play, Pause, and Stop functionality.
- **Track Navigation**: Easily skip to the Next or Previous song.
- **Interactive Playlist**: Displays all available songs in a scrollable list. Tap any song to play it instantly.
- **Custom UI**: A clean, aesthetically pleasing user interface with themed backgrounds and a custom app icon.

## Installation

There are two ways to get the app:

#### For Users

You can download the latest ready-to-install APK from the **[Releases](https://github.com/avishek-sarkar/Music-Player/releases/tag/v1.0.0)** page.


#### For Developers

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/avishek-sarkar/Music-Player.git
    ```
2.  Open the project in Android Studio.
3.  Let Gradle sync the project dependencies.
4.  Build and run on an emulator or a physical device.

## How to Add Your Own Music

This app is designed to be easily customizable:

1.  Get your `.mp3`, `.wav`, or other Android-supported audio files.
2.  Place them inside the `app/src/main/res/raw` directory in the project.
3.  Build and run the app. The new songs will automatically appear in the playlist!

## Technologies Used

- **Language**: Java
- **Platform**: Android
- **UI**: XML for layouts, drawables, and resources.
- **Build System**: Gradle
