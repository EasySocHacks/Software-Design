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
        API vkAPI = new VkAPI("VK TOKEN");

        PostStatistic postStatistic = new PostStatistic(vkAPI);
        System.out.println(postStatistic.getPostCountByTagForPeriod("#утро", 3));
    }
}
