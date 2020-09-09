package cs1302.gallery;

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Separator;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.lang.Runnable;
import java.lang.Thread;
import javafx.geometry.*;
import javafx.scene.text.*;

/**
 * Class which represents the {@code HBox} that contains
 * the search bar as well as all the required buttons
 */
public class SearchBar extends HBox {

    /** Button to play/pause */
    private Button playPause;
    /** Button to search */
    private Button search;
    /** Search field */
    private TextField searchField;
    /** Specified text for search */
    private Text searchText;
    /** KeyFrame used for image rotation */
    private KeyFrame keyFrame;
    /** Timeline used for image rotation */
    private Timeline timeline;
    
    /**
     * Default constructor which represents an {@code HBox}
     * Contains the different buttons and fields for the search bar
     * @param imageGrid a reference to the {@code TilePane} object
     * @param progress a reference to the {@code ProgressBar}
     */
    public SearchBar(ImageGrid imageGrid, Progress progress) {
        super();

        //Create objects to be used
        playPause = new Button("Pause");
        search = new Button("Update Images");
        searchField = new TextField("rock");
        searchText = new Text("Search Query:");
        Separator separator = new Separator(Orientation.VERTICAL);
        this.initImageRotate(imageGrid);
        
        //Assign Handlers
        search.setOnAction(this.getSearchHandler(imageGrid, progress));
        playPause.setOnAction(this.getPlayHandler(imageGrid));
        
        //Format hbox to have proper spacing
        this.setSpacing(5);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.CENTER_LEFT);
        
        //Place objects into hbox
        this.getChildren().addAll(playPause, separator, searchText,
                                  searchField, search);
    }

    /**
     * Getter method for {@code TextField} object
     * @return reference to the {@code TextField}
     */
    public TextField getSearchField() {
        return searchField;
    }

    /**
     * Getter method for {@code search} button
     * @return search button
     */
    public Button getSearchButton() {
        return search;
    }

    /**
     * Getter method for {@code playPause} button
     * @return the play/pause button
     */
    public Button getPlayButton() {
        return playPause;
    }

    /**
     * Gets a reference to the {@code Timeline}
     * @return a reference to the {@code Timeline}
     */
    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * Helper method which makes the event handler for the search button
     * @param imageGrid the image grid which holds displayed images
     * @param progress the progress bar
     * @return an {@code EventHandler} for the {@code search} button
     */
    private EventHandler<ActionEvent> getSearchHandler(ImageGrid imageGrid, Progress progress) {
        EventHandler<ActionEvent> searchHandler = b -> {
            Runnable r = () -> {
                //Stops loop to load images
                timeline.stop();
                //Resets progress
                progress.getProgress().setProgress(0);
                //Updates the images on screen
                imageGrid.updateImages(this, progress);
            };
            
            Thread t2 = new Thread(r);
            t2.setDaemon(true);
            t2.start();
        };

        return searchHandler;
    }

    /**
     * Gets the {@code EventHandler} for the play/pause button
     * @param imageGrid a reference to the {@code TilePane} object
     * @return an {@code EventHandler} for the {@code playPause} button
     */
    private EventHandler<ActionEvent> getPlayHandler(ImageGrid imageGrid) {
        EventHandler<ActionEvent> playHandler = b -> {
            //If loop is playing, stop and change button
            if (playPause.getText().equals("Pause")) {
                timeline.stop();
                playPause.setText("Play");
            }
            //If loop is stopped, start and change button
            else {
                timeline.play();
                playPause.setText("Pause");
            }
        };

        return playHandler;
    }

    /**
     * Helper method to start the timeline and rotate images
     * @param imageGrid a reference to the {@code tilepane}
     */
    private void initImageRotate(ImageGrid imageGrid) {
        Runnable r = () -> {
            //Set up what to do on loop
            EventHandler<ActionEvent> swapImage = b -> imageGrid.imageRotation();
            //Make the loop itself
            keyFrame = new KeyFrame(Duration.seconds(2), swapImage);
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(keyFrame);
        };

        Thread t3 = new Thread(r);
        t3.setDaemon(true);
        t3.start();
    }
}
