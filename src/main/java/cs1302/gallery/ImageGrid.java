package cs1302.gallery;

import javafx.application.Platform;
import javafx.scene.layout.TilePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;

/**
 * The grid where all the {@code Images} are located
 */
public class ImageGrid extends TilePane {

    /** All Images found */
    private ArrayList<Image> allImages;
    /** Current images on tilepane */
    private ArrayList<Image> currentImages;
    /** SearchQuery used to look up images */
    private SearchQuery searchList;
    
    /**
     * Default Constructor which creates the grid
     * with the images specified
     */
    public ImageGrid() {
        super();

        //Initialize tile size
        this.setPrefColumns(5);
        this.setPrefRows(4);
        this.setPrefTileWidth(100);
        this.setPrefTileHeight(100);

        //Sets up with default images
        for (int i = 0; i < 20; i++) {
            ImageView defaultImg = new ImageView();
            defaultImg.setFitWidth(100);
            defaultImg.setFitHeight(100);
            this.getChildren().add(defaultImg);
        }
    }

    /**
     * Method to update images with ones from itunes
     * @param search reference to the {@code SearchBar} object
     * @param progress reference to the {@code ProgressBar} object
     */
    public void updateImages(SearchBar search, Progress progress) {
        search.getSearchButton().setDisable(true);
        search.getPlayButton().setDisable(true);
        searchList = new SearchQuery(search);

        //If there are not enough images, show error
        if (searchList.getUrls().size() < 21) {
            //reset progress
            progress.getProgress().setProgress(1);
            //Handle error
            Platform.runLater(() -> this.makeErrorWindow());
            this.resumePlayback(search);
            //Re enable buttons
            search.getSearchButton().setDisable(false);
            search.getPlayButton().setDisable(false);
            return;
        }
        
        Runnable r = () -> {
            //Loads all found images
            this.setAllImages(search, searchList, progress);
            //Puts images in the tile pane array
            this.setCurrentImages();  
            //Puts images on tilepane
            this.loadImages();
            //Re enable buttons
            search.getSearchButton().setDisable(false);
            search.getPlayButton().setDisable(false);
        };

        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Sets an array of all the results to pull from
     * @param search a reference to the {@code SearchBar}
     * @param searchList reference to the {@code SearchQuery}
     * @param progress reference to the {@code ProgressBar}
     */
    private void setAllImages(SearchBar search, SearchQuery searchList, Progress progress) {
        int size = searchList.getUrls().size();
        allImages = new ArrayList<>();
        
        //Downloads all images
        for (int i = 0; i < size; i++) {
            allImages.add(new Image(searchList.getUrls().get(i)));
            progress.getProgress().setProgress((1.0/size) * i);
        }

        //Fully sets progress
        progress.getProgress().setProgress(1);

        //If play button is activated, start loop again
        this.resumePlayback(search);
    }

    /**
     * Helper method to load images onto the tilepane
     */
    private void loadImages() {
        //Clear out previous images
        Platform.runLater(() -> this.getChildren().clear());

        //Add new images
        for (Image newImage: currentImages) {
            ImageView newImageView = new ImageView(newImage);
            newImageView.setFitWidth(100);
            newImageView.setFitHeight(100);
            Platform.runLater(() -> this.getChildren().add(newImageView));
        }
    }

    /**
     * Helper method to assign images to the 
     * {@code currentImages} array
     */
    private void setCurrentImages() {
        currentImages = new ArrayList<>();
        //Loops through and sets the new images
        for (int i = 0; i < 20; i++) {
            //Gets random number in allImages which isn't in currentList
            int random = (int)(Math.random() * allImages.size());
            while (currentImages.contains(allImages.get(random))) {
                random = (int)(Math.random() * allImages.size());
            }

            //Adds the random image to the current shown images
            currentImages.add(allImages.get(random));
        }
    }
    
    /**
     * Creates an error window
     */
    public void makeErrorWindow() {
        //Create objects used
        Stage stage = new Stage();
        Button ok = new Button("Ok");
        Text text = new Text("Error: not enough images");
        VBox vbox = new VBox();

        //Set some properties
        text.setStyle("-fx-font: 24 arial;");
        vbox.setAlignment(Pos.CENTER);
        ok.setPrefSize(50, 30);
        //Sets the handler
        ok.setOnAction(this.getOkHandler(stage));
        //Add all to the vbox
        vbox.getChildren().addAll(text, ok);
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Error");
        stage.setScene(new Scene(vbox));
        stage.sizeToScene();
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Helper method which gets the event handler for the ok button
     * @param stage the {@code Stage} which it works with
     */
    private EventHandler<ActionEvent> getOkHandler(Stage stage) {
        EventHandler<ActionEvent> okHandler = b -> {
            stage.close();
        };

        return okHandler;
    }

    /**
     * Getter method for {@code allImages}
     * @return a reference to {@code allImages}
     */
    public ArrayList<Image> getAllImages() {
        return allImages;
    }

    /**
     * Getter method for {@code currentImages}
     * @return a reference to {@code currentImages}
     */
    public ArrayList<Image> getCurrentImages() {
        return currentImages;
    }

    /**
     * Method which changes one of the images in the current list with
     * a random one in the list of all images
     */
    public void imageRotation() {
        int random = (int)(Math.random() * allImages.size());

        //Loops until it gets a random image that isn't in currentlist
        while (currentImages.contains(allImages.get(random))) {
            random = (int)(Math.random() * allImages.size());
        }

        //Picks random spot in currentList and replaces it
        int currentRandom = (int)(Math.random() * currentImages.size());
        currentImages.set(currentRandom, allImages.get(random));

        this.loadImages();
    }

    /**
     * Helper method to resume the playing of images
     * @param search the {@code SearchBar} object being used
     */
    private void resumePlayback(SearchBar search) {
        if (search.getPlayButton().getText().equals("Pause")) {
            search.getTimeline().play();
        }
    }
}
