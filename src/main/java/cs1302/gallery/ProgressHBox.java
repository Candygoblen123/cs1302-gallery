package cs1302.gallery;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.application.Platform;

/**
 * A Bar that contains a progress bar and an iTunes image credit.
 */
public class ProgressHBox extends HBox {
    private ProgressBar progressBar;
    private Label creditLabel;

    /**
     * Constructs a new {@code ProgressHBox}.
     */
    public ProgressHBox() {
        super(4);

        progressBar = new ProgressBar();
        progressBar.setProgress(0.0);
        creditLabel = new Label("Images provided by iTunes Search API.");

        init();
    }

    /**
     * Arranges the UI.
     */
    public void init() {
        HBox.setHgrow(creditLabel, Priority.NEVER);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().addAll(progressBar, creditLabel);
    }

    /**
     * Updates the progressBar.
     * @param progress the progress to update to.
     */
    public void updateProgress(double progress) {
        Platform.runLater(() -> progressBar.setProgress(progress));
    }
}
