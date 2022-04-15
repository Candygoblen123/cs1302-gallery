package cs1302.gallery;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;

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
     * Arranges the User Interface
     */
    public void init() {
        this.setPrefColumns(5);
        for (ImageView iv : imageViews) {
            iv = new ImageView(new Image("file:resources/default.png"));
            this.getChildren().add(iv);
        }
    }
}
