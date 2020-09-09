package cs1302.gallery;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * Class which represents the menu bar at the top of the program.
 */
public class TopMenuBar extends MenuBar {

    /** Menu for the file section */
    private Menu file;
    /** Menu for the help section */
    private Menu help;
    /** MenuItem for the exit button */
    private MenuItem exit;
    /** MenuItem for the about button */
    private MenuItem about;

    /**
     * Default constructor which creates all the 
     * menu tabs and their respectible options.
     * @param stage the main stage which the program runs
     */
    public TopMenuBar(Stage stage) {
        super();

        //Creates new menus to use
        file = new Menu("File");
        help = new Menu("Help");

        //Create menu items
        exit = new MenuItem("Exit");
        about = new MenuItem("About");

        //Set action handlers
        exit.setOnAction(this.getExitMenuHandler(stage));
        about.setOnAction(this.getAboutMenuHandler());

        //Adds the menu items to menu category
        file.getItems().add(exit);
        help.getItems().add(about);

        this.getMenus().addAll(file, help);
    }

    /**
     * Helper method which creates 
     * a new Window for the about section.
     */
    private void makeAboutWindow() {
        //Create displayed objects
        ImageView pic = new ImageView(new Image("file:resources/Headshot.jpeg"));
        Text name = new Text("Josh Kitzrow");
        Text email = new Text("jk94290@uga.edu");
        Text version = new Text("Version: 1.0");

        //Set up panes to hold objects
        HBox hbox = new HBox();
        VBox vbox = new VBox(50);

        //Add objects to panes
        hbox.getChildren().addAll(pic, vbox);
        vbox.getChildren().addAll(name, email, version);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About Josh Kitzrow");
        stage.setScene(new Scene(hbox));
        stage.sizeToScene();
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Getter method for the exit menu {@code EventHandler}
     * @param stage the main stage which the program runs on
     * @return the {@code EventHandler} for the exit menu
     */
    private EventHandler<ActionEvent> getExitMenuHandler(Stage stage) {
        //When the exit button is clicked, exit the program
        EventHandler<ActionEvent> exitHandler = b -> {
            stage.close();
        };

        return exitHandler;
    }

    /**
     * Getter method for the about menu {@code EventHandler}
     * @return the {@code EventHandler} for the about menu
     */
    private EventHandler<ActionEvent> getAboutMenuHandler() {
        //Creates new window when clicked
        EventHandler<ActionEvent> aboutHandler = b -> {
            this.makeAboutWindow();
        };

        return aboutHandler;
    }
}
