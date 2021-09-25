package api;

import api.exception.APIException;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link VkAPI} class.
 */
public class VkAPITest {
    private final int PORT = 1234;

    private API api;

    @Before
    public void createAPIInstance() {
        api = new VkAPI("", "http://localhost:" + PORT);
    }

    @Test
    public void getPostsForCorrectPeriodAndTag() {
        withStubServer(s -> {
            whenHttp(s)
                    .match()
                    .then(stringContent(
                            "{ \"response\": { \"items\": [ {}, {}, {}, {}, {}, {}, {}, {}, {} ] } }"));

            try {
                for (int i = 1; i <= 24; i++) {
                    assertEquals(9, api.getPostCountByTagWithinHourPeriodHoursAgo("#tag", i));
                }
            } catch (APIException e) {
                fail();
            }
        });
    }

    @Test(expected = AssertionError.class)
    public void getPostsForIncorrectPeriod() {
        withStubServer(s -> {
            whenHttp(s)
                    .match()
                    .then(stringContent(
                            "{ \"response\": { \"items\": [ {}, {}, {}, {}, {}, {} ] } }"));

            try {
                api.getPostCountByTagWithinHourPeriodHoursAgo("#tag", 100);
            } catch (APIException e) {
                fail();
            }
        });
    }

    @Test(expected = AssertionError.class)
    public void getPostsForIncorrectTag() {
        withStubServer(s -> {
            whenHttp(s)
                    .match()
                    .then(stringContent(
                            "{ \"response\": { \"items\": [ {}, {}, {}, {}, {}, {} ] } }"));

            try {
                api.getPostCountByTagWithinHourPeriodHoursAgo("tag", 1);
            } catch (APIException e) {
                fail();
            }
        });
    }

    @Test
    public void getPostsWithHttpCodeError() {
        withStubServer(s -> {
            whenHttp(s)
                    .match()
                    .then(status(HttpStatus.NOT_FOUND_404));

            try {
                api.getPostCountByTagWithinHourPeriodHoursAgo("#tag", 24);
            } catch (APIException e) {
                return;
            }

            fail();
        });
    }

    private void withStubServer(Consumer<StubServer> callback) {
        StubServer stubServer = null;

        try {
            stubServer = new StubServer(PORT).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}