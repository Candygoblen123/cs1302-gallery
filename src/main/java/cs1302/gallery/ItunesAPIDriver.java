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

/**
 * A class that does all the talking with the iTunes API.
 */
public class ItunesAPIDriver {
    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /**
     * Returns an array of urls for album art images.
     * @param searchTerm The earch term to get album art for
     * @param type the type of data
     * @return a String[] of image URLs
     * @throws IOException when the network request failed.
     * @throws InterruptedException when the network request failed.
     */
    public static String getImageArr(String searchTerm, String type)
        throws IOException, InterruptedException {
        String term = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        String media = URLEncoder.encode(type, StandardCharsets.UTF_8);
        String limit = URLEncoder.encode("200", StandardCharsets.UTF_8);
        String query = String.format("?term=%s&media=%s&limit=%s", term, media, limit);

        String response = ItunesAPIDriver.apiRequest(query);
        System.out.println(response);
        return response;
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
        System.out.println("hello"
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

        System.out.println(response.statusCode());
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }

        String responseBody = response.body().trim();
        return responseBody;

    }
}
