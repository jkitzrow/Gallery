package cs1302.gallery;

import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 * Class which represents the progress bar
 */
public class Progress extends HBox {

    /** Progress bar */
    private ProgressBar progressBar;

    /**
     * Initializes the progress bar object
     */
    public Progress() {
        super();

        //Creates the progress bar and sets its progress to 0
        progressBar = new ProgressBar();
        progressBar.setProgress(0);

        //Creats Text
        Text provider = new Text("Images provided courtesy of iTunes");

        //Edits properties
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER_LEFT);
        
        //Add it to the hbox
        this.getChildren().addAll(progressBar, provider);
    }

    /**
     * Gets a reference to the {@code ProgressBar} object
     * @return a reference to the {@code ProgressBar}
     */
    public ProgressBar getProgress() {
        return progressBar;
    }
}
