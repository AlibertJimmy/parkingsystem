package com.parkit.parkingsystem.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestConstants {

    // RELATED TO THE PARKING_SPOT
    public static final int PARKING_SPOT_NUMBER_TEST = 1;
    public static final ParkingType PARKING_TYPE_TEST = ParkingType.CAR;
    public static final boolean PARKING_AVAILABILITY = true;

    // RELATED TO THE TICKET
    public static final int PARKING_NUMBER_TEST = 1;
    public static final String VEHICLE_REG_NUMBER_TEST = "PLATEFORME";


    public static final Date IN_TIME_TEST;

    static {
        Date tempDate = null;
        try {
            tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-11-08 09:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        IN_TIME_TEST = tempDate;
    }



}
