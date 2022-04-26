package cs1302.gallery;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;

import java.net.URLEncoder;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A Node containing the search bar in the iTunes gallery app.
 */
public class SearchBarHBox extends HBox {
    private Button playButton;
    private Label searchLabel;
    private TextField searchTextField;
    private ComboBox<String> mediaTypeComboBox;
    private Button searchButton;
    private GalleryApp app;
    private boolean playEnable;

    /**
     * Constructs a new {@code SearchBarHBox}.
     * @param app a refence to the Application object
     */
    public SearchBarHBox(GalleryApp app) {
        super(4);
        this.app = app;
        this.playEnable = true;

        playButton = new Button("Play");
        searchLabel = new Label("Search:");
        searchTextField = new TextField("Bill Wurtz");
        mediaTypeComboBox = new ComboBox<String>();
        searchButton = new Button("Get Images");
        init();
    }

    /**
     * Aranges the UI.
     */
    public void init() {
        this.setAlignment(Pos.CENTER_LEFT);
        playButton.setDisable(true);
        mediaTypeComboBox.getItems().addAll("movie", "podcast", "music", "musicVideo", "audiobook",
            "shortFilm", "tvShow", "software", "ebook", "all");
        mediaTypeComboBox.setValue("music");
        this.getChildren().addAll(playButton, searchLabel, searchTextField,
            mediaTypeComboBox, searchButton);

        HBox.setHgrow(searchTextField, Priority.ALWAYS);
        searchButton.setOnAction(this::loadImages);
        playButton.setOnAction(this::playPause);
    }

    /**
     * Everything that needs to happen to get the images to appear in the UI.
     * @param ae an action event.
     */
    public void loadImages(ActionEvent ae) {
        Thread t = new Thread(() -> {
            this.app.updateStatus("Getting images...");
            Platform.runLater(() -> {
                this.searchButton.setDisable(true);
                this.playButton.setDisable(true);
                this.playButton.setText("Play");
                this.app.stopPlaying();
            });

            String searchText = searchTextField.getText();
            String mediaType = mediaTypeComboBox.getValue();

            try {
                this.app.updateProgress(-1.0);
                String[] imageUrls = ItunesAPIDriver.getImageArr(searchText, mediaType);

                ArrayList<Image> images = ItunesAPIDriver.downloadImages(imageUrls, app);

                this.app.showImages(images);

                this.app.updateStatus("https://itunes.apple.com/search"
                    + String.format("?term=%s&media=%s&limit=%s",
                    URLEncoder.encode(searchText),
                    URLEncoder.encode(mediaType), 200));
                this.app.updateProgress(1.0);
                this.playEnable = true;
            } catch (Exception e) {
                this.app.updateStatus("Last attempt to get images failed...");
                this.app.updateProgress(1.0);
                Platform.runLater(() -> {

                    Alert alert = new Alert(AlertType.ERROR, "URI: https://itunes.apple.com/search"
                        + String.format("?term=%s&media=%s&limit=%s",
                        URLEncoder.encode(searchText),
                        URLEncoder.encode(mediaType), 200)
                        + "\n\nException: " + e.toString());
                    alert.showAndWait();
                });
            }
            Platform.runLater(() -> this.searchButton.setDisable(false));
            if (this.playEnable) {
                Platform.runLater(() -> this.playButton.setDisable(false));
            }

        });
        t.start();
    }

    /**
     * Handles the playing and pausing of artwork.
     * @param ae an ActionEvent.
     */
    public void playPause(ActionEvent ae) {
        if (this.playButton.getText().equals("Play")) {
            this.playButton.setText("Pause");
            this.app.startPlaying();
            this.app.resizeToScene();
        } else {
            this.playButton.setText("Play");
            this.app.stopPlaying();
            this.app.resizeToScene();
        }

    }
}
