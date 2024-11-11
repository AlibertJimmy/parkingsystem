package com.parkit.parkingsystem.constants;

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";


    // RELATED TO THE TICKET
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

    public static final String CHECK_TICKET_EXISTENCE = "SELECT EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM ticket\n" +
            "    WHERE parking_number = ?\n" +
            "      AND vehicle_reg_number = ?\n" +
            "      AND in_time = ?\n" +
            ") AS record_exists;\n";

    public static final String IS_VEHICLE_RECURRENT = "SELECT *\n" +
            "FROM ticket\n" +
            "WHERE VEHICLE_REG_NUMBER = ?\n" +
            "  AND IN_TIME IS NOT NULL\n" +
            "  AND OUT_TIME IS NOT NULL;";

    // RELATED TO THE CLEANING OF THE DATABASE
    public static final String TRUNCATE_TICKET = "TRUNCATE TABLE ticket;";
    public static final String TRUNCATE_PARKING = "TRUNCATE TABLE parking;";

    // RELATED TO THE FOREIGN KEY CHECK
    // Temporarily disable foreign key checks
    public static final String DISABLE_FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = 0;";

    // Re-enable foreign key checks
    public static final String ENABLE_FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = 1;";

}
