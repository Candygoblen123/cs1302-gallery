package cs1302.gallery;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.application.Platform;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.Random;

/**
 * A grid of 20 images images.
 */
public class ImageTilePane extends TilePane {
    private ImageView[] imageViews;
    private ArrayList<Image> images;
    private boolean isPlaying;
    private Timeline timeline;

    /**
     * Constructs a new {@code ImageTilePane}.
     */
    public ImageTilePane() {
        super();

        isPlaying = false;
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
     */
    public void showImages(ArrayList<Image> images) {
        this.images = images;

        Platform.runLater(() -> {
            for (int i = 0; i < 20; i++) {
                this.imageViews[i].setImage(this.images.get(0));
                this.images.remove(0);
            }

        });
    }

    /**
     * Starts playing the album art swapping animation.
     */
    public void startPlaying() {
        this.timeline = new Timeline();
        EventHandler<ActionEvent> handler = this::swapOne;
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), handler);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /**
     * Pauses the album art swapping animation.
     */
    public void stopPlaying() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    /**
     * Swaps an image in an imageView with an image in the images ArrayList,
     * adding the old imageView image to the ArrayList, and removing the new
     * image from the ArrayList.
     * @param ae an ActionEvent.
     */
    public void swapOne(ActionEvent ae) {
        int rand = new Random().nextInt(this.imageViews.length);
        ImageView iv = this.imageViews[rand];
        Image tmp = iv.getImage();
        Image swap = this.images.remove(new Random().nextInt(this.images.size()));

        this.images.add(tmp);
        Platform.runLater(() -> iv.setImage(swap));
    }
}
