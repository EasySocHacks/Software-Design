package refactoring.servlet;

import base.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GetProductsServletTest {
    private GetProductsServlet getProductsServlet;

    private BaseTest baseTest;

    private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    private final PrintWriter printWriter = mock(PrintWriter.class);

    private StringBuilder output = new StringBuilder();

    @Before
    public void before() throws IOException {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        doAnswer(invocation -> {
            output.append(invocation.getArguments()[0]);
            output.append(System.lineSeparator());
            return null;
        }).when(printWriter).println((String) any());

        getProductsServlet = new GetProductsServlet(
                "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
                "sa",
                "sa");

        baseTest = new BaseTest();
    }

    @After
    public void after() {
        output = new StringBuilder();

        baseTest.close();

        verifyZeroInteractions(httpServletRequest);
        verifyNoMoreInteractions(httpServletRequest, printWriter);
    }

    @Test
    public void getProductsWithSomeDataTest() {
        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)," +
                            "('B', 1)," +
                            "('C', 2)," +
                            "('D', 5)," +
                            "('NAME', 0)");

            getProductsServlet.doGet(httpServletRequest, httpServletResponse);

            assertEquals(
                    "<html><body>\n" +
                            "A\t1</br>\n" +
                            "B\t1</br>\n" +
                            "C\t2</br>\n" +
                            "D\t5</br>\n" +
                            "NAME\t0</br>\n" +
                            "</body></html>\n",
                    output.toString());

            verify(httpServletResponse).getWriter();
            verify(printWriter).println("<html><body>");
            verify(printWriter).println("A\t1</br>");
            verify(printWriter).println("B\t1</br>");
            verify(printWriter).println("C\t2</br>");
            verify(printWriter).println("D\t5</br>");
            verify(printWriter).println("NAME\t0</br>");
            verify(printWriter).println("</body></html>");
            verify(httpServletResponse).setContentType("text/html");
            verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IOException e) {
            fail();
        }
    }

    @Test
    public void getProductsWithNoDataTest() {
        try {
            getProductsServlet.doGet(httpServletRequest, httpServletResponse);

            assertEquals(
                    "<html><body>\n" +
                            "</body></html>\n",
                    output.toString());

            verify(httpServletResponse).getWriter();
            verify(printWriter).println("<html><body>");
            verify(printWriter).println("</body></html>");
            verify(httpServletResponse).setContentType("text/html");
            verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void getProductsWithNoDatabaseTest() {
        try {
            baseTest.getStatement().execute("drop table product");

            getProductsServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        fail();
    }
}