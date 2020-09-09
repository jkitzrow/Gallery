package cs1302.gallery;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.net.URL;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Class dedicated for searching the itunes database
 */
public class SearchQuery {

    /** String of url prefix */
    private static final String URL_PREFIX = "https://itunes.apple.com/search?term=";
    /** Search keywords */
    private String searchWords = "";
    /** Full url */
    private URL url;
    /** Array of image urls */
    private ArrayList<String> imageUrls = new ArrayList<>();

    /**
     * The object which searches and parses
     * the results from the iTunes api
     * @param searchBar a reference to the {@code SearchBar} object
     */
    public SearchQuery(SearchBar searchBar) {
        try {
            //Gets the the words used in the search bar and formats it into part of a URL
            searchWords = URLEncoder.encode(searchBar
                                            .getSearchField().getText().trim(), "UTF-8");

            //Forms the url and sets up a StreamReader to get the file
            url = new URL(URL_PREFIX + searchWords + "&limit=200");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            
            //Gets the urls of the images and adds it to an ArrayList
            this.parseURL(reader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the urls from the JSON file provided by the itunes api
     * @param reader The file returned from the api
     */
    public void parseURL(InputStreamReader reader) {
        //Parses each individual element in JSON file
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(reader);

        //Get the results as an array and set up the string array
        JsonObject root = je.getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");
        int numResults = results.size();

        //Loops through and parses the artwork url from the JSON objects
        for (int i = 0; i < numResults; i++) {
            JsonObject result = results.get(i).getAsJsonObject();
            JsonElement artworkUrl100 = result.get("artworkUrl100");
            //If the url isn't a duplicate, add it to the array
            if (artworkUrl100 != null && !imageUrls.contains(artworkUrl100.getAsString())) {
                String artUrl = artworkUrl100.getAsString();
                imageUrls.add(artUrl);
            }
        }
    }

    /**
     * Method to get the array which holds the image urls
     * @return array holding image urls
     */
    public ArrayList<String> getUrls() {
        return imageUrls;
    }
}
