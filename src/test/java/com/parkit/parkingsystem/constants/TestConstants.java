package com.parkit.parkingsystem.constants;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestConstants {

    // RELATED TO THE PARKING_SPOT
    public static final int PARKING_SPOT_NUMBER_TEST = 1;
    public static final ParkingType PARKING_TYPE_TEST = ParkingType.CAR;
    public static final boolean PARKING_AVAILABILITY = false;

    // RELATED TO THE TICKET
    public static final int TICKET_ID_TEST = 1;
    public static final String VEHICLE_REG_NUMBER_TEST = "PLATEFORME";

    public static final Timestamp IN_TIME_TEST = Timestamp.valueOf("2024-11-08 09:00:00");
    public static final Timestamp OUT_TIME_TEST = Timestamp.valueOf("2024-11-08 10:45:00");
    public static final double PRICE_TEST = Fare.CAR_RATE_PER_MIN * ((OUT_TIME_TEST.getTime() - IN_TIME_TEST.getTime()) / (1000 * 60));





}
