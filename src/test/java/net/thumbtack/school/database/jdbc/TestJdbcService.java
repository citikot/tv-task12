package net.thumbtack.school.database.jdbc;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import net.thumbtack.school.database.model.Subject;
import net.thumbtack.school.database.model.Trainee;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJdbcService {

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
    public void testInsertTrainee() throws SQLException {
        Trainee trainee = new Trainee("Иван", "Иванов", 5);
        JdbcService.insertTrainee(trainee);
        Trainee traineeFromDBByColNames = JdbcService.getTraineeByIdUsingColNames(trainee.getId());
        assertEquals(trainee, traineeFromDBByColNames);
        Trainee traineeFromDBByColNumbers = JdbcService.getTraineeByIdUsingColNumbers(trainee.getId());
        assertEquals(trainee, traineeFromDBByColNumbers);
    }

    @Test
    public void testUpdateTrainee() throws SQLException {
        Trainee trainee = new Trainee("Иван", "Иванов", 5);
        JdbcService.insertTrainee(trainee);
        Trainee traineeFromDBByColNames = JdbcService.getTraineeByIdUsingColNames(trainee.getId());
        assertEquals(trainee, traineeFromDBByColNames);
        trainee.setFirstName("Петр");
        trainee.setLastName("Петров");
        JdbcService.updateTrainee(trainee);
        traineeFromDBByColNames = JdbcService.getTraineeByIdUsingColNames(trainee.getId());
        assertEquals(trainee, traineeFromDBByColNames);
    }

    @Test
    public void testDeleteTrainee() throws SQLException {
        Trainee trainee = new Trainee("Иван", "Иванов", 5);
        JdbcService.insertTrainee(trainee);
        Trainee traineeFromDBByColNames = JdbcService.getTraineeByIdUsingColNames(trainee.getId());
        assertEquals(trainee, traineeFromDBByColNames);
        JdbcService.deleteTrainee(trainee);
        traineeFromDBByColNames = JdbcService.getTraineeByIdUsingColNames(trainee.getId());
        assertNull(traineeFromDBByColNames);
    }

    @Test
    public void testInsertTrainees() throws SQLException {
        Trainee traineeIvanov = new Trainee("Иван", "Иванов", 5);
        JdbcService.insertTrainee(traineeIvanov);
        Trainee traineePetrov = new Trainee("Петр", "Петров", 4);
        JdbcService.insertTrainee(traineePetrov);
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(traineeIvanov);
        trainees.add(traineePetrov);
        List<Trainee> traineesFromDBByColNames = JdbcService.getTraineesUsingColNames();
        traineesFromDBByColNames.sort(Comparator.comparingInt(Trainee::getId));
        assertEquals(trainees, traineesFromDBByColNames);
        List<Trainee> traineesFromDBByColNumbers = JdbcService.getTraineesUsingColNumbers();
        traineesFromDBByColNumbers.sort(Comparator.comparingInt(Trainee::getId));
        assertEquals(trainees, traineesFromDBByColNumbers);
    }

    @Test
    public void testInsertSchool() throws SQLException {
        School school = new School("TTSchool", 2018);
        JdbcService.insertSchool(school);
        School schoolFromDBByColNames = JdbcService.getSchoolByIdUsingColNames(school.getId());
        assertEquals(school, schoolFromDBByColNames);
        School schoolFromDBByColNumbers = JdbcService.getSchoolByIdUsingColNumbers(school.getId());
        assertEquals(school, schoolFromDBByColNumbers);
    }

    @Test
    public void testInsertSubject() throws SQLException {
        Subject subject = new Subject("MySQL");
        JdbcService.insertSubject(subject);
        Subject subjectFromDBByColNames = JdbcService.getSubjectByIdUsingColNames(subject.getId());
        assertEquals(subject, subjectFromDBByColNames);
        Subject subjectFromDBByColNumbers = JdbcService.getSubjectByIdUsingColNumbers(subject.getId());
        assertEquals(subject, subjectFromDBByColNumbers);
    }

    @Test
    public void testInsertSchoolAndGroups() throws SQLException {
        School school = new School("TTSchool", 2018);
        JdbcService.insertSchool(school);
        School schoolFromDBByColNames = JdbcService.getSchoolByIdUsingColNames(school.getId());
        assertEquals(school, schoolFromDBByColNames);
        Group groupFrontEnd = new Group("Frontend", "11");
        Group groupBackEnd = new Group("Backend", "12");
        JdbcService.insertGroup(school, groupFrontEnd);
        JdbcService.insertGroup(school, groupBackEnd);
        List<Group> groups = new ArrayList<>();
        groups.add(groupFrontEnd);
        groups.add(groupBackEnd);
        school.setGroups(groups);
        School schoolFromDBWithGroups = JdbcService.getSchoolByIdWithGroups(school.getId());
        assertEquals(school, schoolFromDBWithGroups);
    }

    @Test
    public void testInsertTwoSchoolsAndGroups() throws SQLException {
        School school2018 = new School("TTSchool2018", 2018);
        JdbcService.insertSchool(school2018);
        School school2019 = new School("TTSchool2019", 2019);
        JdbcService.insertSchool(school2019);
        School school2018FromDBByColNames = JdbcService.getSchoolByIdUsingColNames(school2018.getId());
        assertEquals(school2018, school2018FromDBByColNames);
        School school2019FromDBByColNames = JdbcService.getSchoolByIdUsingColNames(school2019.getId());
        assertEquals(school2019, school2019FromDBByColNames);
        Group group2018FrontEnd = new Group("Frontend2018", "11");
        Group group2018BackEnd = new Group("Backend2018", "12");
        Group group2019FrontEnd = new Group("Frontend2019", "11");
        Group group2019BackEnd = new Group("Backend2019", "12");
        JdbcService.insertGroup(school2018, group2018FrontEnd);
        JdbcService.insertGroup(school2018, group2018BackEnd);
        JdbcService.insertGroup(school2019, group2019FrontEnd);
        JdbcService.insertGroup(school2019, group2019BackEnd);
        List<Group> groups2018 = new ArrayList<>();
        groups2018.add(group2018FrontEnd);
        groups2018.add(group2018BackEnd);
        school2018.setGroups(groups2018);
        List<Group> groups2019 = new ArrayList<>();
        groups2019.add(group2019FrontEnd);
        groups2019.add(group2019BackEnd);
        school2019.setGroups(groups2019);
        List<School> schools = new ArrayList<>();
        schools.add(school2018);
        schools.add(school2019);
        List<School> schoolsFromDBWithGroups = JdbcService.getSchoolsWithGroups();
        assertEquals(schools, schoolsFromDBWithGroups);
    }

    @Test
    public void testThrowsSQLException() {
        Method[] declaredMethods = JdbcService.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (!Modifier.isPublic(method.getModifiers()))
                continue;
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            boolean throwSQLException = false;
            for (Class<?> exception : exceptionTypes) {
                if (exception == SQLException.class) {
                    throwSQLException = true;
                    break;
                }
            }
            if (!throwSQLException) {
                fail();
            }
        }
    }
}
