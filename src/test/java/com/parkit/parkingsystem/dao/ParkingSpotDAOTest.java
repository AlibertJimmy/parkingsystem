package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseTestConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.TestConstants;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.services.ClearDataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {

    private static final Logger logger = LogManager.getLogger("ParkingSpotDAOTest");
    public static ParkingSpotDAO parkingSportDAO = new ParkingSpotDAO();
    public static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    public static ClearDataBase clearDataBase = new ClearDataBase();

    @BeforeAll
    public static void setUp() throws Exception {
        parkingSportDAO = new ParkingSpotDAO();
        parkingSportDAO.dataBaseConfig = dataBaseTestConfig;
    }

    @AfterAll
    public static void Reset() throws Exception {
        clearDataBase.resetParkingSpotOne();
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

        //update the availability fo that parking slot
        Connection con = null;
        boolean resultAfter = true;

        try {
            con = dataBaseTestConfig.getConnection();
            // Set the parking spot as true
            PreparedStatement psSetState = con.prepareStatement(DBConstants.SET_PARKING_SPOT_TRUE);
            psSetState.execute();

            // Update the parking spot
            /*
            PreparedStatement psUpdate = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            psUpdate.setBoolean(1, false);
            psUpdate.setInt(2, TestConstants.PARKING_SPOT_NUMBER_TEST);
            psUpdate.execute();
            */

            ParkingSpot parkingSpotTest = new ParkingSpot(TestConstants.PARKING_SPOT_NUMBER_TEST, TestConstants.PARKING_TYPE_TEST, false);
            parkingSportDAO.updateParking(parkingSpotTest);


            PreparedStatement psGetState = con.prepareStatement(DBConstants.CHECK_PARKING_SPOT_STATE);
            ResultSet rsAfter = psGetState.executeQuery();

            if(rsAfter.next()){
                // Contains the value of the first spot available for cars
                resultAfter = rsAfter.getBoolean(1);
                System.out.println("result : "+resultAfter);

            }
            assertEquals(true, !resultAfter);



        }catch (Exception ex){
            logger.error("Error updating parking info",ex);

        }
    }
}