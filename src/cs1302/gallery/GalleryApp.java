package cs1302.gallery;

import javafx.application.Application; 
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** 
 * Represents an iTunes GalleryApp!
 */
public class GalleryApp extends Application {

    /** Menu bar on top */
    private TopMenuBar menuBar;
    /** SearchBar */
    private SearchBar searchBar;
    /** Image grid */
    private ImageGrid grid;
    /** Progress Bar */
    private Progress progress;
    
    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        VBox pane = new VBox();

        //Create the different parts of the scene
        menuBar = new TopMenuBar(stage);
        progress = new Progress();
        grid = new ImageGrid();
        searchBar = new SearchBar(grid, progress);
        
        //Loads default images
        grid.updateImages(searchBar, progress);        
        //Add parts to the container
        pane.getChildren().addAll(menuBar, searchBar, grid, progress);
        
        Scene scene = new Scene(pane);
        stage.setResizable(false);
        stage.setTitle("Gallery");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

} // GalleryApp

