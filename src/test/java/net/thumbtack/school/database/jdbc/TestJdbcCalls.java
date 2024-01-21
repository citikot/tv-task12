package net.thumbtack.school.database.jdbc;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class TestJdbcCalls {

    private static Connection spyConnection;
    private static final String USER = "test";
    private static final String PASSWORD = "test";
    private static final String URL = "jdbc:mysql://localhost:3306/ttschool?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Omsk&allowPublicKeyRetrieval=true";

    private static boolean setUpIsDone = false;

    @BeforeAll
    public static void setUp() {
        if (!setUpIsDone) {
            boolean createConnection = JdbcUtils.createConnection();
            if (!createConnection) {
                throw new RuntimeException("Can't create connection, stop");
            }
            setUpIsDone = true;
        }
    }
    @AfterAll
    public static void close() {
        if (setUpIsDone)
            JdbcUtils.closeConnection();
    }

    @BeforeEach
    public void clearDatabase() throws SQLException {
        JdbcService.deleteSchools();
        JdbcService.deleteTrainees();
        JdbcService.deleteSubjects();
    }

    @Test
    public void testStatementsNumber() throws SQLException, ClassNotFoundException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        spyConnection = spy(connection);
        try(MockedStatic<JdbcUtils> jdbcUtilsMockedStatic = mockStatic(JdbcUtils.class)) {
            when(JdbcUtils.getConnection()).thenReturn(spyConnection);
            School school = new School("TTSchool", 2018);
            JdbcService.insertSchool(school);
            Group groupFrontEnd = new Group("Frontend", "11");
            Group groupBackEnd = new Group("Backend", "12");
            JdbcService.insertGroup(school, groupBackEnd);
            JdbcService.insertGroup(school, groupFrontEnd);

            reset(spyConnection);
            JdbcService.getSchoolByIdWithGroups(school.getId());
            verify(spyConnection, never()).createStatement();
            verify(spyConnection).prepareStatement(anyString());

            reset(spyConnection);
            JdbcService.getSchoolsWithGroups();
            verify(spyConnection, never()).createStatement();
            verify(spyConnection).prepareStatement(anyString());
        }
    }
}
