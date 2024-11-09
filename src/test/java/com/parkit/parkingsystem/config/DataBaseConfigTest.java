package com.parkit.parkingsystem.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseConfigTest {

    private DataBaseConfig dataBaseConfigTest;
    @BeforeEach
    public void setUp() {
        dataBaseConfigTest = new DataBaseTestConfig();
    }

    @Test
    public void testGetConnection() {
        // Attempt to open a connection to the test database
        // automatically closes con at the end of the try block
        try (Connection con = dataBaseConfigTest.getConnection()) {
            // defensive assertion, ensuring the validity of the connection object before performing further checks
            assertNotNull(con, "Connection should not be null");
            // Check that the connection is open
            assertFalse(con.isClosed(), "Connection should be open");
        } catch (Exception e) {
            fail("Exception should not be thrown while getting a connection: " + e.getMessage());
        }
    }

    @Test
    public void testCloseConnection() {
        Connection connection = null;
        try {
            // Obtain a real connection
            connection = dataBaseConfigTest.getConnection();
            assertNotNull(connection, "Connection should not be null");

            // Ensure the connection is open before calling closeConnection
            assertFalse(connection.isClosed(), "Connection should initially be open");

            // Call closeConnection with the real connection
            dataBaseConfigTest.closeConnection(connection);

            // Verify that the connection is now closed
            assertTrue(connection.isClosed(), "Connection should be closed after calling closeConnection");

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClosePreparedStatement() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Set up a real connection and create a PreparedStatement
            connection = dataBaseConfigTest.getConnection();
            assertNotNull(connection, "Connection should not be null");

            preparedStatement = connection.prepareStatement("SELECT 1");
            assertNotNull(preparedStatement, "PreparedStatement should not be null");

            // Ensure the PreparedStatement is open before closing
            assertFalse(preparedStatement.isClosed(), "PreparedStatement should initially be open");

            // Call closePreparedStatement to close it
            dataBaseConfigTest.closePreparedStatement(preparedStatement);

            // Verify that the PreparedStatement is now closed
            assertTrue(preparedStatement.isClosed(), "PreparedStatement should be closed after calling closePreparedStatement");

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        } finally {
            // Ensure the connection is closed
            dataBaseConfigTest.closeConnection(connection);
        }
    }


    @Test
    public void testCloseResultSet() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Set up a real connection, PreparedStatement, and ResultSet
            connection = dataBaseConfigTest.getConnection();
            assertNotNull(connection, "Connection should not be null");

            preparedStatement = connection.prepareStatement("SELECT 1");
            resultSet = preparedStatement.executeQuery();
            assertNotNull(resultSet, "ResultSet should not be null");

            // Ensure the ResultSet is open before closing
            assertFalse(resultSet.isClosed(), "ResultSet should initially be open");

            // Call closeResultSet to close it
            dataBaseConfigTest.closeResultSet(resultSet);

            // Verify that the ResultSet is now closed
            assertTrue(resultSet.isClosed(), "ResultSet should be closed after calling closeResultSet");

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        } finally {
            // Ensure all resources are closed
            dataBaseConfigTest.closePreparedStatement(preparedStatement);
            dataBaseConfigTest.closeConnection(connection);
        }
    }

}
