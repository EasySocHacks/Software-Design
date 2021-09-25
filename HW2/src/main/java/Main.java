import api.API;
import api.VkAPI;
import statistic.PostStatistic;

/**
 * Main class.
 */
public class Main {
    /**
     * Main method.
     *
     * @param args System arguments.
     */
    public static void main(String[] args) {
        API vkAPI = new VkAPI("6c5954876c5954876c595487946c20275e66c596c5954870d14c49e1eb4e742a45b862f");

        PostStatistic postStatistic = new PostStatistic(vkAPI);
        System.out.println(postStatistic.getPostCountByTagForPeriod("#утро", 3));
    }
}
