package cs1302.gallery;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.application.Platform;

/**
 * A grid of 20 images images.
 */
public class ImageTilePane extends TilePane {
    private ImageView[] imageViews;

    /**
     * Constructs a new {@code ImageTilePane}.
     */
    public ImageTilePane() {
        super();

        imageViews = new ImageView[20];
        init();
    }

    /**
     * Arranges the User Interface.
     */
    public void init() {
        this.setPrefColumns(5);
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new ImageView();
            this.getChildren().add(imageViews[i]);
            imageViews[i].setImage(new Image("file:resources/default.png"));
        }
    }

    /**
     * Puts the first 20 images in the given array into the UI.
     * @param images the array of Image objects
     * @param app a refence to the Application
     */
    public void showImages(Image[] images, GalleryApp app) {
        Platform.runLater(() -> {
            for (int i = 0; i < 20; i++) {
                imageViews[i].setImage(images[i]);
            }
        });
    }
}
