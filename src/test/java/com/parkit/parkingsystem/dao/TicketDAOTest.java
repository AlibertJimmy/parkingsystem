package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseTestConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.TestConstants;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TicketDAOTest {

    private static final Logger logger = LogManager.getLogger("TicketDAOTest");
    public static TicketDAO ticketDAO = new TicketDAO();
    public static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @BeforeAll
    public static void setUp() throws Exception {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
    }

    @Test
    void saveTicket() {
        logger.info("Test : save Ticket");
        Ticket ticketTest = new Ticket();

        ParkingSpot parkingSpotTest = new ParkingSpot(TestConstants.PARKING_SPOT_NUMBER_TEST, TestConstants.PARKING_TYPE_TEST, TestConstants.PARKING_AVAILABILITY);

        ticketTest.setParkingSpot(parkingSpotTest);
        ticketTest.setVehicleRegNumber(TestConstants.VEHICLE_REG_NUMBER_TEST);
        ticketTest.setInTime(TestConstants.IN_TIME_TEST);

        Connection con = null;
        try{
            // Access the database threw the configuration of the test database piv_one_test
            con = dataBaseTestConfig.getConnection();

            ticketDAO.saveTicket(ticketTest);

            // Prepare the creation of the SQL request to check the existence of the geographicCoordinates previously created
            PreparedStatement psCheck = con.prepareStatement(DBConstants.CHECK_TICKET_EXISTENCE);
            psCheck.setDouble(1, ticketTest.getParkingSpot().getId());
            psCheck.setString(2, ticketTest.getVehicleRegNumber());
            // psCheck.setTimestamp(3, new java.sql.Timestamp(ticketTest.getInTime().getTime()));
            psCheck.setTimestamp(3, new Timestamp(ticketTest.getInTime().getTime()));
            // Execute the existence check of the geographic_coordinates
            boolean isExisting = false;

            // Execute the query and check the result set
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                isExisting = rs.getBoolean("record_exists"); // Retrieve the boolean result from the column "record_exists"
            }
            logger.info("Test result: "+isExisting);
            rs.close();
            psCheck.close();

            // Use an assertion to set the test result based on `isExisting`
            assertTrue(isExisting, "The ticket should exist in the database after saving.");


        }  catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed during the test of : saveGeographicCoordinates");
        }
    }

    @Test
    void getTicket() {

        logger.info("Test : save Ticket");
        Ticket ticketTest = new Ticket();

        ParkingSpot parkingSpotTest = new ParkingSpot(TestConstants.PARKING_SPOT_NUMBER_TEST, TestConstants.PARKING_TYPE_TEST, TestConstants.PARKING_AVAILABILITY);

        ticketTest.setId(TestConstants.TICKET_ID_TEST);
        ticketTest.setParkingSpot(parkingSpotTest);
        ticketTest.setVehicleRegNumber(TestConstants.VEHICLE_REG_NUMBER_TEST);
        ticketTest.setInTime(TestConstants.IN_TIME_TEST);

        Connection con = null;

        try{
            // Access the database threw the configuration of the test database
            con = dataBaseTestConfig.getConnection();

            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);

            ps.setInt(1,ticketTest.getParkingSpot().getId());
            ps.setString(2, ticketTest.getVehicleRegNumber());
            ps.setDouble(3, ticketTest.getPrice());
            ps.setTimestamp(4, new Timestamp(ticketTest.getInTime().getTime()));
            ps.setTimestamp(5, (ticketTest.getOutTime() == null)?null: (new Timestamp(ticketTest.getOutTime().getTime())) );
            ps.execute();

            Ticket ticketRetrieved = ticketDAO.getTicket(TestConstants.VEHICLE_REG_NUMBER_TEST);

            // Use an assertion to set the test result based on `isExisting`
            // assertEquals(ticketTest, ticketRetrieved, "The ticket should exist in the database after saving.");

            assertEquals(ticketTest.getId(), ticketRetrieved.getId(), "Ticket IDs should match.");
            assertEquals(ticketTest.getParkingSpot(), ticketRetrieved.getParkingSpot(), "Parking spots should match.");
            assertEquals(ticketTest.getVehicleRegNumber(), ticketRetrieved.getVehicleRegNumber(), "Vehicle registration numbers should match.");
            assertEquals(ticketTest.getPrice(), ticketRetrieved.getPrice(), "Ticket prices should match.");
            assertEquals(ticketTest.getInTime(), ticketRetrieved.getInTime(), "In times should match.");
            assertEquals(ticketTest.getOutTime(), ticketRetrieved.getOutTime(), "Out times should match.");



        }  catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed during the test of : saveGeographicCoordinates");
        }
    }

    @Test
    void updateTicket() {
    }

    @Test
    void isUserRecurrent() {
    }
}