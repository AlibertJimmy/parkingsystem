package com.parkit.parkingsystem.services;

// import config.DataBaseTestConfig;
import com.parkit.parkingsystem.config.DataBaseTestConfig;
import com.parkit.parkingsystem.constants.DBConstants;
// import DBConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClearDataBase {
    public static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearTicket(){
        try {
            Connection con = dataBaseTestConfig.getConnection();
            /*
            // Temporarily disable foreign key checks
            PreparedStatement psDisableFK = con.prepareStatement(DBConstants.DISABLE_FOREIGN_KEY_CHECKS);
            psDisableFK.execute();
            psDisableFK.close();
            */

            // Execute the deletion of the table geographic_coordinates
            PreparedStatement psTruncateAddress = con.prepareStatement(DBConstants.TRUNCATE_TICKET);
            psTruncateAddress.execute();
            psTruncateAddress.close();
            /*
            // Re-enable foreign key checks
            PreparedStatement psEnableFK = con.prepareStatement(DBConstants.ENABLE_FOREIGN_KEY_CHECKS);
            psEnableFK.execute();
            psEnableFK.close();
            */
        }
        catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Database cleaning failed");
        }
    }

    public void clearParking(){
        try {
            Connection con = dataBaseTestConfig.getConnection();
            // Temporarily disable foreign key checks
            PreparedStatement psDisableFK = con.prepareStatement(DBConstants.DISABLE_FOREIGN_KEY_CHECKS);
            psDisableFK.execute();
            psDisableFK.close();

            // Execute the deletion the table Address
            PreparedStatement psTruncateAddress = con.prepareStatement(DBConstants.TRUNCATE_PARKING);
            psTruncateAddress.execute();
            psTruncateAddress.close();

            // Re-enable foreign key checks
            PreparedStatement psEnableFK = con.prepareStatement(DBConstants.ENABLE_FOREIGN_KEY_CHECKS);
            psEnableFK.execute();
            psEnableFK.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Database cleaning failed");
        }
    }
}

