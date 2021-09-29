package refactoring.servlet;

import base.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AddProductServletTest {
    private AddProductServlet addProductServlet;

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

        addProductServlet = new AddProductServlet(
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
    public void addProductWithEmptyDatabaseTest() {
        when(httpServletRequest.getParameter("name")).thenReturn("abacaba");
        when(httpServletRequest.getParameter("price")).thenReturn("111");

        try {
            addProductServlet.doGet(httpServletRequest, httpServletResponse);

            ResultSet resultSet = baseTest.getStatement().executeQuery("select * from product");
            if (resultSet.next()) {
                assertEquals("abacaba", resultSet.getString("name"));
                assertEquals(111, resultSet.getInt("price"));

                if (resultSet.next()) {
                    fail();
                }
            } else {
                fail();
            }

            verify(httpServletRequest).getParameter("name");
            verify(httpServletRequest).getParameter("price");
            verify(httpServletResponse).setContentType("text/html");
            verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
            verify(httpServletResponse).getWriter();
            verify(printWriter).println("OK");
        } catch (IOException | SQLException e) {
            fail();
        }
    }

    @Test
    public void addProductWithSameDataAsInsertingInDatabaseTest() {
        when(httpServletRequest.getParameter("name")).thenReturn("asd");
        when(httpServletRequest.getParameter("price")).thenReturn("12333");

        try {
            baseTest.getStatement().executeUpdate(
                    "insert into product (name, PRICE) values " +
                            "('asd', 12333)");

            addProductServlet.doGet(httpServletRequest, httpServletResponse);

            ResultSet resultSet = baseTest.getStatement().executeQuery("select * from product");
            for (int i = 0; i < 2; i++) {
                if (resultSet.next()) {
                    assertEquals("asd", resultSet.getString("name"));
                    assertEquals(12333, resultSet.getInt("price"));
                } else {
                    fail();
                }
            }

            if (resultSet.next()) {
                fail();
            }

            verify(httpServletRequest).getParameter("name");
            verify(httpServletRequest).getParameter("price");
            verify(httpServletResponse).setContentType("text/html");
            verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
            verify(httpServletResponse).getWriter();
            verify(printWriter).println("OK");
        } catch (IOException | SQLException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void addProductWithNoDatabaseTest() throws IOException {
        try {
            baseTest.getStatement().execute("drop table product");

            addProductServlet.doGet(httpServletRequest, httpServletResponse);
        } catch (SQLException | IOException e) {
            fail();
        } finally {
            verify(httpServletRequest).getParameter("name");
            verify(httpServletRequest).getParameter("price");
            verify(httpServletResponse).getWriter();
        }

        fail();
    }
}