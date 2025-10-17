package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://dog.ceo/api/breed/";
    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        String url = API_URL + breed+"/list";
        System.out.println(url);
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getString("status").equals("success")) {
                JSONArray subBreedJsonArray = responseBody.getJSONArray("message");
                List<String> subBreedArray = new ArrayList<>();

                for (int i = 0; i < subBreedJsonArray.length(); i++) {
                    subBreedArray.add(subBreedJsonArray.getString(i));
                }
                System.out.println(subBreedArray);
                return subBreedArray;
            }
            else {
                throw new BreedNotFoundException(responseBody.getString("message"));
            }
        }
        catch (IOException | JSONException event) {
            throw new BreedNotFoundException(breed);
        }
    }
}