package refactoring.servlet;

import base.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import refactoring.servlet.query.QueryServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class QueryServletTest {
    private QueryServlet queryServlet;

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
        }).when(printWriter).println(anyString());
        doAnswer(invocation -> {
            output.append(invocation.getArguments()[0]);
            output.append(System.lineSeparator());
            return null;
        }).when(printWriter).println(anyInt());

        queryServlet = new QueryServlet(
                "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
                "sa",
                "sa");

        baseTest = new BaseTest();
    }

    @After
    public void after() {
        output = new StringBuilder();

        baseTest.close();

        verifyNoMoreInteractions(httpServletRequest, httpServletResponse, printWriter);
    }

    @Test
    public void maxQueryWithSomeDataWithExactlyOneMaxPriceTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("max");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)," +
                            "('B', 1)," +
                            "('C', 2)," +
                            "('D', 5)," +
                            "('NAME', 0)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "D\t5</br>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with max price: </h1>");
        verify(printWriter).println("D\t5</br>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void maxQueryWithSomeDataWithMoreThanOneMaxPriceTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("max");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)," +
                            "('B', 5)," +
                            "('C', 2)," +
                            "('D', 5)," +
                            "('NAME', 5)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "B\t5</br>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with max price: </h1>");
        verify(printWriter).println("B\t5</br>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void maxQueryWithSingleMaxPriceDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("max");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "A\t1</br>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with max price: </h1>");
        verify(printWriter).println("A\t1</br>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void maxQueryWithNoDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("max");

        try {
            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with max price: </h1>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test(expected = RuntimeException.class)
    public void maxQueryWithNoDatabaseTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("max");

        try {
            baseTest.getStatement().execute("drop table product");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        } finally {
            verify(httpServletRequest).getParameter("command");
            verify(httpServletResponse).getWriter();
        }

        fail();
    }

    @Test
    public void minQueryWithSomeDataWithExactlyOneMinPriceTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("min");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)," +
                            "('B', 1)," +
                            "('C', 2)," +
                            "('D', 5)," +
                            "('NAME', 0)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "NAME\t0</br>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with min price: </h1>");
        verify(printWriter).println("NAME\t0</br>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void minQueryWithSomeDataWithMoreThanOneMinPriceTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("min");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 6)," +
                            "('B', 2)," +
                            "('C', 7)," +
                            "('D', 2)," +
                            "('NAME', 2)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "B\t2</br>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with min price: </h1>");
        verify(printWriter).println("B\t2</br>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void minQueryWithSingleMinPriceDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("min");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "A\t1</br>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with min price: </h1>");
        verify(printWriter).println("A\t1</br>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void minQueryWithNoDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("min");

        try {
            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with min price: </h1>");
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test(expected = RuntimeException.class)
    public void minQueryWithNoDatabaseTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("min");

        try {
            baseTest.getStatement().execute("drop table product");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        } finally {
            verify(httpServletRequest).getParameter("command");
            verify(httpServletResponse).getWriter();
        }

        fail();
    }

    @Test
    public void sumQueryWithSomeDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("sum");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)," +
                            "('B', 2)," +
                            "('C', 3)," +
                            "('D', 4)," +
                            "('NAME', 90)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "Summary price: \n" +
                        "100\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("Summary price: ");
        verify(printWriter).println(100);
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void sumQueryWithNoDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("sum");

        try {
            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "Summary price: \n" +
                        "0\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("Summary price: ");
        verify(printWriter).println(0);
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test(expected = RuntimeException.class)
    public void sumQueryWithNoDatabaseTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("sum");

        try {
            baseTest.getStatement().execute("drop table product");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        } finally {
            verify(httpServletRequest).getParameter("command");
            verify(httpServletResponse).getWriter();
        }

        fail();
    }

    @Test
    public void countQueryWithSomeDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("count");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, price) values " +
                            "('A', 1)," +
                            "('B', 2)," +
                            "('C', 3)," +
                            "('D', 4)," +
                            "('NAME', 90)");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "Number of products: \n" +
                        "5\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("Number of products: ");
        verify(printWriter).println(5);
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void countQueryWithNoDataTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("count");

        try {
            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (IOException e) {
            fail();
        }

        assertEquals(
                "<html><body>\n" +
                        "Number of products: \n" +
                        "0\n" +
                        "</body></html>\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("Number of products: ");
        verify(printWriter).println(0);
        verify(printWriter).println("</body></html>");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test(expected = RuntimeException.class)
    public void countQueryWithNoDatabaseTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("count");

        try {
            baseTest.getStatement().execute("drop table product");

            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        } finally {
            verify(httpServletRequest).getParameter("command");
            verify(httpServletResponse).getWriter();
        }

        fail();
    }

    @Test
    public void unknownQueryTest() throws IOException {
        when(httpServletRequest.getParameter("command")).thenReturn("query");

        try {
            queryServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (IOException e) {
            fail();
        }

        assertEquals(
                "Unknown command: query\n",
                output.toString());

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).getWriter();
        verify(printWriter).println("Unknown command: query");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }
}