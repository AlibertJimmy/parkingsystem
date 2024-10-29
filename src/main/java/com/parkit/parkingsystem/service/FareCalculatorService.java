package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //TODO: Some tests are failing here. Need to check if this logic is correct

        /*
        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();
        int duration = outHour - inHour;
        System.out.println("Hour IN : "+inHour+" Hour OUT : "+outHour);
        System.out.println("Duration : "+duration);
        */

        long durationInMinutes = getMinutesBetweenDates(ticket.getInTime(), ticket.getOutTime());
        System.out.println("Vehicle type : "+ticket.getParkingSpot().getParkingType());
        System.out.println("Total park duration in minutes : "+durationInMinutes);

        if (durationInMinutes >= 30){
            durationInMinutes = durationInMinutes - 30;
        } else {
            durationInMinutes = 0;
        }
        System.out.println("Park duration in minutes with the free 30min: "+durationInMinutes);


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                // ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_MIN);
                break;
            }
            case BIKE: {
                // ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_MIN);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }

    }

    public static long getMinutesBetweenDates(Date startDate, Date endDate) {
        // Get the difference in milliseconds
        long differenceInMillis = endDate.getTime() - startDate.getTime();

        // Convert milliseconds to minutes
        long differenceInMinutes = differenceInMillis / (1000 * 60);

        return differenceInMinutes;
    }
}