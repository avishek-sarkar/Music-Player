package com.example.musicplayer;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * The main activity for the Music Player application.
 * This class handles the user interface, media playback, and the music playlist.
 */
public class MainActivity extends AppCompatActivity {

    // The MediaPlayer instance to handle audio playback.
    private MediaPlayer mediaPlayer;
    // Index of the currently playing track in the musicTracks list.
    private int currentTrack = 0;
    // A list of resource IDs for the raw music files.
    private final ArrayList<Integer> musicTracks = new ArrayList<>();
    // A list of display names for the music tracks.
    private final ArrayList<String> musicTrackNames = new ArrayList<>();

    // The TextView that displays the title of the currently playing song.
    private TextView songTitleTextView;

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components.
        songTitleTextView = findViewById(R.id.song_title_text_view);
        ListView musicListView = findViewById(R.id.music_list_view);

        // Load all music tracks from the res/raw directory.
        loadMusicTracks();

        // If no music is found, display a message and exit setup.
        if (musicTracks.isEmpty()) {
            songTitleTextView.setText(R.string.no_music_found);
            Log.e("MainActivity", "No music files found in res/raw. Please add some!");
            return;
        }

        // Set up the ListView with the list of song names.
        ArrayAdapter<String> musicListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musicTrackNames);
        musicListView.setAdapter(musicListAdapter);
        // Set a listener to play a track when its name is clicked in the list.
        musicListView.setOnItemClickListener((parent, view, position, id) -> playTrack(position));

        // Initialize buttons and set their click listeners.
        Button playButton = findViewById(R.id.play_button);
        Button pauseButton = findViewById(R.id.pause_button);
        Button stopButton = findViewById(R.id.stop_button);
        Button nextButton = findViewById(R.id.next_button);
        Button previousButton = findViewById(R.id.previous_button);

        initializeMediaPlayer();

        // --- OnClickListener Definitions ---

        playButton.setOnClickListener(v -> {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        });

        stopButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release(); // Release the player to reset it.
                initializeMediaPlayer();
            }
        });

        nextButton.setOnClickListener(v -> playNext());
        previousButton.setOnClickListener(v -> playPrevious());
    }

    /**
     * Dynamically loads all audio file resources from the 'res/raw' directory.
     * Uses reflection to inspect the R.raw class and retrieve all its fields.
     */
    private void loadMusicTracks() {
        musicTracks.clear();
        musicTrackNames.clear();
        Resources res = getResources();
        // Use reflection to get all fields from the R.raw class.
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            try {
                int resourceId = field.getInt(null);
                musicTracks.add(resourceId);

                // Get the resource name and format it for display.
                String trackName = res.getResourceEntryName(resourceId);
                String formattedName = trackName.replace('_', ' ');
                formattedName = Character.toUpperCase(formattedName.charAt(0)) + formattedName.substring(1);
                musicTrackNames.add(formattedName);

            } catch (IllegalAccessException | Resources.NotFoundException e) {
                Log.e("MainActivity", "Could not load music track", e);
            }
        }
    }

    /**
     * Initializes the MediaPlayer with the current track.
     */
    private void initializeMediaPlayer() {
        if (musicTracks.isEmpty()) return;
        mediaPlayer = MediaPlayer.create(this, musicTracks.get(currentTrack));
        updateSongTitle();
    }

    /**
     * Updates the song title TextView to display the name of the current track.
     */
    private void updateSongTitle() {
        if (musicTrackNames.isEmpty()) return;
        songTitleTextView.setText(getString(R.string.now_playing_format, musicTrackNames.get(currentTrack)));
    }

    /**
     * Plays a specific track from the playlist based on its position.
     * @param position The index of the track to play in the musicTracks list.
     */
    private void playTrack(int position) {
        if (musicTracks.isEmpty() || position < 0 || position >= musicTracks.size()) return;

        // Release the previous player before creating a new one.
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        currentTrack = position;
        initializeMediaPlayer();
        mediaPlayer.start();
    }

    /**
     * Plays the next track in the playlist. Wraps around to the beginning if at the end.
     */
    private void playNext() {
        if (musicTracks.isEmpty()) return;
        int nextTrack = (currentTrack + 1) % musicTracks.size();
        playTrack(nextTrack);
    }

    /**
     * Plays the previous track in the playlist. Wraps around to the end if at the beginning.
     */
    private void playPrevious() {
        if (musicTracks.isEmpty()) return;
        int previousTrack = (currentTrack - 1 + musicTracks.size()) % musicTracks.size();
        playTrack(previousTrack);
    }

    /**
     * Called when the activity is being destroyed. Releases the MediaPlayer resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
