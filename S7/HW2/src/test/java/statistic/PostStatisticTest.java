package statistic;

import api.API;
import api.VkAPI;
import api.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PostStatisticTest {
    private PostStatistic postStatistic;

    @Mock
    private VkAPI api;

    @Before
    public void initAndCreateInstances() {
        MockitoAnnotations.initMocks(this);

        postStatistic = new PostStatistic(api);
    }

    @After
    public void noMoreInteractions() {
        //verifyNoMoreInteractions(api);
    }

    @Test
    public void getPostCountStatisticWithCorrectTagAndOneHourPeriod() {
        try {
            when(api.getPostCountByTagWithinHourPeriodHoursAgo(notNull(String.class), anyInt())).thenReturn(123);

            assertEquals(List.of(123), postStatistic.getPostCountByTagForPeriod("#Тег", 1));

            verify(api).getPostCountByTagWithinHourPeriodHoursAgo("#Тег", 1);
        } catch (APIException e) {
            fail();
        }
    }

    @Test
    public void getPostCountStatisticWithCorrectTagAndManyHourPeriod() {
        try {
            when(api.getPostCountByTagWithinHourPeriodHoursAgo(notNull(String.class), anyInt())).thenReturn(111);

            assertEquals(
                    List.of(111, 111, 111, 111, 111, 111, 111, 111, 111, 111),
                    postStatistic.getPostCountByTagForPeriod("#A", 10));

            verify(api).getPostCountByTagWithinHourPeriodHoursAgo("#A", 10);
        } catch (APIException e) {
            fail();
        }
    }

    @Test(expected = AssertionError.class)
    public void getPostCountStatisticWithIncorrectTag() {
        try {
            when(api.getPostCountByTagWithinHourPeriodHoursAgo(notNull(String.class), anyInt())).thenReturn(444);

            postStatistic.getPostCountByTagForPeriod("B#", 10);

            verify(api).getPostCountByTagWithinHourPeriodHoursAgo("B#", 10);
        } catch (APIException e) {
            fail();
        }
    }

    @Test
    public void getPostCountStatisticWithReceivingExceptionFromOutside() {
        try {
            //noinspection unchecked
            when(api.getPostCountByTagWithinHourPeriodHoursAgo(notNull(String.class), anyInt()))
                    .thenThrow(APIException.class);

            assertEquals(
                    Arrays.asList(null, null, null, null, null, null, null, null, null, null),
                    postStatistic.getPostCountByTagForPeriod("#B", 10));

            verify(api).getPostCountByTagWithinHourPeriodHoursAgo("#B", 10);
        } catch (APIException e) {
            fail();
        }
    }
}