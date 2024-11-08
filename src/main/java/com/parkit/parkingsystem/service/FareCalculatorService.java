package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        double durationFare;
        double finalFare = 0;
        long durationInMinutes = getMinutesBetweenDates(ticket.getInTime(), ticket.getOutTime());
        System.out.println("Total park duration in minutes : "+durationInMinutes);

        if (durationInMinutes <= 30){
            durationInMinutes = 0;
        }
        System.out.println("Park duration in minutes with the free 30min: "+durationInMinutes);

        //TODO: Some tests are failing here. Need to check if this logic is correct


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                // ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                //ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_MIN);
                durationFare = durationInMinutes * Fare.CAR_RATE_PER_MIN;
                break;
            }
            case BIKE: {
                // ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                // ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_MIN);
                durationFare = durationInMinutes * Fare.BIKE_RATE_PER_MIN;
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }

        System.out.println("Fare before potential 5% reduction : "+durationFare);
        TicketDAO ticketDAO = new TicketDAO();
        boolean isRecurrent = ticketDAO.isUserRecurrent(ticket.getVehicleRegNumber());
        if (isRecurrent){
            System.out.println("Apply the 5% reduction");
            finalFare = durationFare - (0.05 * durationFare);
        }
        else{
            finalFare = durationFare;
            System.out.println("Don't apply the 5% reduction");
        }
        System.out.println("finalFare : "+finalFare);
        ticket.setPrice(finalFare);
        System.out.println("plop");
    }

    public static long getMinutesBetweenDates(Date startDate, Date endDate) {
        // Get the difference in milliseconds
        long differenceInMillis = endDate.getTime() - startDate.getTime();

        // Convert milliseconds to minutes
        long differenceInMinutes = differenceInMillis / (1000 * 60);

        return differenceInMinutes;
    }
}