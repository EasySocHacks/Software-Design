package api;

import api.exception.APIException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A class to proceed queries to a social network APIs.
 */
public interface API {
    /**
     * Gets posts {@link List} by specific {@code tag}.
     * Returns all posts count with specific tag within one-hour period {@code hoursAgo} ago.
     * <p>
     * {@code tag} must be formatted as following regex: ^#[\w\s-]+$
     * {@code hoursAgo} must be greater than or equals to {@code 1} and less
     * than or equals to {@code 24}.
     *
     * @param tag      A tag to search posts with. The parameter must be not {@code null}.
     * @param hoursAgo A period of time to search from current hours count before.
     * @return Count of post's publishing time found by specific {@code tag} within period.
     * @throws APIException Throws in case there are an error occurred
     *                      while querying to api.API.
     */
    int getPostCountByTagWithinHourPeriodHoursAgo(@NotNull String tag, int hoursAgo) throws APIException;
}
