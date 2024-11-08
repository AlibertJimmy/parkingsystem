package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTestMock {

    @Mock
    private static DataBaseConfig dataBaseConfig = new DataBaseConfig();

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @InjectMocks
    private TicketDAO ticketDAO;

    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        try{
            MockitoAnnotations.initMocks(this);

            // Create a sample Ticket object for testing
            ticket = new Ticket();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABC123");
            ticket.setPrice(10.0);
            ticket.setInTime(new Date());
            ticket.setOutTime(null);

            // Set up the mock behaviors
            when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.execute()).thenReturn(true);
        }  catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    void saveTicket() {
        try{
            // Call the method to be tested
            boolean result = ticketDAO.saveTicket(ticket);

            // Verify that all expected interactions occurred
            verify(dataBaseConfig, times(1)).getConnection();
            verify(mockConnection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);

            // Verify parameters passed to the prepared statement
            verify(mockPreparedStatement, times(1)).setInt(1, ticket.getParkingSpot().getId());
            verify(mockPreparedStatement, times(1)).setString(2, ticket.getVehicleRegNumber());
            verify(mockPreparedStatement, times(1)).setDouble(3, ticket.getPrice());
            verify(mockPreparedStatement, times(1)).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            verify(mockPreparedStatement, times(1)).setTimestamp(5, null);

            // Verify execute method was called
            verify(mockPreparedStatement, times(1)).execute();
            assertEquals("ABC123",ticket.getVehicleRegNumber());
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to test saveTicket");
        }
    }
    /*
    @Test
    public void SaveTicketTest() {
        String regNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(2, parkingType, true);
        Ticket ticketToSave = new Ticket();
        ticketToSave.setParkingSpot(parkingSpot);
        ticketToSave.setVehicleRegNumber(regNumber);
        ticketToSave.setPrice(2);
        ticketToSave.setInTime(new Date());
        ticketToSave.setOutTime(new Date());
        Assertions.assertThat(ticketDAO.saveTicket(ticketToSave)).isNotNull();
        Ticket result = ticketDAO.getTicket(regNumber);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).extracting(r -> r.getVehicleRegNumber()).isEqualTo(regNumber);
    }
    */
    /*
    @Test
    void getTicket() {
    }

    @Test
    void updateTicket() {
    }

    @Test
    void isUserRecurrent() {
    }
    */
}
