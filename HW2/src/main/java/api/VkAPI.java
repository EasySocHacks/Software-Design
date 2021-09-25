package api;

import api.exception.APIException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static utils.TimeManager.getUnixTimeOffsetHoursAgo;

/**
 * Class for operation with {@link API}.
 * <p>
 *
 * @see API
 */
public class VkAPI implements API {
    private final String accessToken;
    private String host = "https://api.vk.com";

    /**
     * Create an {@link API} instance specific for VK API.
     *
     * @param accessToken An access token of VK application. The parameter must be not {@code null}.
     */
    public VkAPI(@NotNull String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Create an {@link API} instance specific for VK API.
     * Excpect {@code host} to connect to.
     *
     * @param accessToken An access token of VK application. The parameter must be not {@code null}.
     * @param host        A host to send post request to.
     */
    public VkAPI(@NotNull String accessToken, String host) {
        this.accessToken = accessToken;
        this.host = host;
    }

    /**
     * Gets posts count within an hour period {@code hoursAgo} hours ago specific for VK API.
     *
     * @param tag      A tag to search posts with. The parameter must be not {@code null}.
     * @param hoursAgo A period of time to search from current hours count before.
     * @return Posts count with specific {@code tag} within an hour period {@code hoursAgo} hours ago.
     * @throws APIException Throws in case couldn't proceed {@link API} query.
     */
    @Override
    public int getPostCountByTagWithinHourPeriodHoursAgo(@NotNull String tag, int hoursAgo) throws APIException {
        assert (tag.matches("^#[а-яА-Я\\w\\s-]+$"));
        assert (1 <= hoursAgo);
        assert (hoursAgo <= 24);

        OkHttpClient httpClient = new OkHttpClient();

        String url = host + "/method/newsfeed.search?" +
                "q=%23" + tag.substring(1) + "&" +
                "start_time=" + getUnixTimeOffsetHoursAgo(hoursAgo) + "&" +
                "end_time=" + getUnixTimeOffsetHoursAgo(hoursAgo - 1) + "&" +
                "count=200" + "&" +
                "access_token=" + accessToken + "&" +
                "v=5.131";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            String jsonString = Objects.requireNonNull(response.body()).string();

            JSONObject jsonObject = new JSONObject(jsonString);

            return jsonObject.getJSONObject("response").getJSONArray("items").length();
        } catch (IOException e) {
            throw new APIException("Couldn't get posts by tag with specified api.API tokens", e);
        } catch (Exception e) {
            throw new APIException("Couldn't proceed request or response.", e);
        }
    }
}
