package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseTestConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.TestConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {

    private static final Logger logger = LogManager.getLogger("TicketDAOTest");
    public static ParkingSpotDAO parkingSportDAO = new ParkingSpotDAO();
    public static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @BeforeAll
    public static void setUp() throws Exception {
        parkingSportDAO = new ParkingSpotDAO();
        parkingSportDAO.dataBaseConfig = dataBaseTestConfig;
    }
    @Test
    void getNextAvailableSlot() {
        Connection con = null;
        int result=-1;
        try {
            // Get all the car parking spot available
            con = dataBaseTestConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_ALL_AVAILABLE_CAR_PARKING_SPOT);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                // Contains the value of the first spot available for cars
                result = rs.getInt(1);
            }
            rs.close();
            ps.close();

            ParkingType parkingTypeTest = ParkingType.CAR;
            int requestTestedResult = parkingSportDAO.getNextAvailableSlot(parkingTypeTest);

            assertEquals(result, requestTestedResult, "The first parking spot available is not identified correctly");

        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }
    }

    @Test
    void updateParking() {
    }
}