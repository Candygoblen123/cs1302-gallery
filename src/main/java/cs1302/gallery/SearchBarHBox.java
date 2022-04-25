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

import java.net.URLEncoder;
import java.io.IOException;

/**
 * A Node containing the search bar in the iTunes gallery app.
 */
public class SearchBarHBox extends HBox {
    private Button playButton;
    private Label searchLabel;
    private TextField searchTextField;
    private ComboBox<String> mediaTypeComboBox;
    private Button searchButton;

    /**
     * Constructs a new {@code SearchBarHBox}.
     */
    public SearchBarHBox() {
        super(4);

        playButton = new Button("Play");
        searchLabel = new Label("Search:");
        searchTextField = new TextField("nanahira");
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

        searchButton.setOnAction(this::loadImages);
    }

    public void loadImages(ActionEvent ae) {
        Thread t = new Thread(() -> {
            try {
                ItunesAPIDriver.getImageArr(searchTextField.getText(),
                mediaTypeComboBox.getValue());
            } catch (IOException | InterruptedException e) {
                System.out.println(e);
                System.out.println(e.getMessage());
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.ERROR, "URI: https://itunes.apple.com/search"
                        + String.format("?term=%s&media=%s&limit=%s",
                        URLEncoder.encode(searchTextField.getText()),
                        URLEncoder.encode(mediaTypeComboBox.getValue()), 200)
                        + "\n\nException: " + e.toString());
                    alert.showAndWait();
                });
            }
        });
        t.start();
    }
}
