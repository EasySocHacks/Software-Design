package statistic;

import api.API;
import api.exception.APIException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class to collect a social networks post's statistic.
 */
@SuppressWarnings("ClassCanBeRecord")
public class PostStatistic {
    private final @NotNull API api;

    /**
     * @param api An {@link API} of a social network to work with. The parameter must be not {@code null}.
     */
    public PostStatistic(@NotNull API api) {
        this.api = api;
    }

    /**
     * Gets posts occurred count statistic by specific {@code tag}.
     * <p>
     * {@code tag} must be formatted as following regex: ^#[а-яА-Я\w\s-]+$
     * {@code hoursAgo} must be greater than or equals to {@code 1} and less
     * than or equals to {@code 24}.
     *
     * @param tag   A tag to search posts with. The parameter must be not {@code null}.
     * @param hours An hours period to collect statistic for.
     * @return {@link List} of posts count. {@link List} in position {@code i} contains
     * posts count in period from {@code i + 1} to {@code i} hours ago. The value must be not {@code null}.
     */
    public @NotNull List<Integer> getPostCountByTagForPeriod(@NotNull String tag, int hours) {
        assert (tag.matches("^#[а-яА-я\\w\\s-]+$"));
        assert (1 <= hours);
        assert (hours <= 24);

        return IntStream.range(0, hours).parallel().mapToObj(
                it -> {
                    try {
                        return api.getPostCountByTagWithinHourPeriodHoursAgo(tag, it + 1);
                    } catch (APIException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
        ).collect(Collectors.toList());
    }
}
