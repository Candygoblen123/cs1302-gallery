package cs1302.gallery;

import java.net.http.HttpClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents an iTunes Gallery App.
 */
public class GalleryApp extends Application {


    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private Stage stage;
    private Scene scene;
    private VBox root;
    private HBox searchBarHbox;
    private Label statusLabel;
    private TilePane imageTilePane;
    private HBox progressHbox;

    /**
     * Constructs a {@code GalleryApp} object}.
     */
    public GalleryApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox(4);
        this.searchBarHbox = new SearchBarHBox();
        this.statusLabel = new Label("Type in a term, select a media type, and click the button.");
        this.imageTilePane = new ImageTilePane();
        this.progressHbox = new ProgressHBox();
    } // GalleryApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        System.out.println("init() called");

        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(this.searchBarHbox, this.statusLabel,
            this.imageTilePane, this.progressHbox);

        root.setPadding(new Insets(4, 0, 4, 0));
        searchBarHbox.setPadding(new Insets(0, 4, 0, 4));
        statusLabel.setPadding(new Insets(0, 4, 0, 4));
        progressHbox.setPadding(new Insets(0, 4, 0, 4));
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("GalleryApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
    } // start

    /** {@inheritDoc} */
    @Override
    public void stop() {
        // feel free to modify this method
        System.out.println("stop() called");
    } // stop

} // GalleryApp
