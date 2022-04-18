package cs1302.gallery;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
        super();

        progressBar = new ProgressBar(0.0);
        creditLabel = new Label("Images provided by iTunes Search API.");

        init();
    }

    /**
     * Arranges the UI.
     */
    public void init() {
        HBox.setHgrow(creditLabel, Priority.NEVER);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        this.getChildren().addAll(progressBar, creditLabel);
    }
}
