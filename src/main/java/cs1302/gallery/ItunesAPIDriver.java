package cs1302.gallery;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.time.Duration;
import com.google.gson.Gson;
import javafx.scene.image.Image;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * A class that does all the talking with the iTunes API.
 */
public class ItunesAPIDriver {
    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .connectTimeout(Duration.ofSeconds(20))
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /**
     * Returns an array of urls for album art images.
     * @param searchTerm The earch term to get album art for
     * @param type the type of data
     * @return a String[] of image URLs
     * @throws IOException when the network request failed.
     * @throws InterruptedException when the network request failed.
     * @throws IllegalArgumentException when there are less than 21 images available.
     */
    public static String[] getImageArr(String searchTerm, String type)
        throws IOException, InterruptedException, IllegalArgumentException {
        String term = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        String media = URLEncoder.encode(type, StandardCharsets.UTF_8);
        String limit = URLEncoder.encode("200", StandardCharsets.UTF_8);
        String query = String.format("?term=%s&media=%s&limit=%s", term, media, limit);

        String json = ItunesAPIDriver.apiRequest(query);
        Gson gson = new Gson();
        ItunesResponse response = gson.fromJson(json, ItunesResponse.class);

        if (response.resultCount <= 21) {
            throw new IllegalArgumentException(response.resultCount +
                " distinct results found, but 21 or more are needed.");
        }

        String[] urls = new String[response.resultCount];
        for (int i = 0; i < response.resultCount; i++) {
            urls[i] = response.results[i].artworkUrl100;
        }

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>(Arrays.asList(urls));

        String[] distinctUrls = linkedHashSet.toArray(new String[] {});

        return distinctUrls;
    }

    /**
     * Sends a request to the iTunes API using the provided paramaters.
     * @param params a string containing url-encoded params
     * @return a json of the response
     * @throws IOException when the network request failed.
     * @throws InterruptedException when the network request failed.
     */
    public static String apiRequest(String params) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://itunes.apple.com/search" + params))
            .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }

        String responseBody = response.body().trim();
        return responseBody;

    }

    /**
     * Download the images into an array of Image objects.
     * @param imageUrls an array of urls that point to images
     * @param app a refrence to the Application object
     * @return an array of Image objects
     * @throws Exception when an image fails to download
     */
    public static ArrayList<Image> downloadImages(String[] imageUrls, GalleryApp app)
        throws Exception {
        app.updateProgress(0.0);

        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i < imageUrls.length; i++) {
            Image img = new Image(imageUrls[i]);
            if (img.isError()) {
                throw img.getException();
            }
            images.add(img);

            // I know casting bad, but the division won't work correctly otherwise.
            app.updateProgress((double)i / (double)imageUrls.length);
        }

        return images;
    }
}
