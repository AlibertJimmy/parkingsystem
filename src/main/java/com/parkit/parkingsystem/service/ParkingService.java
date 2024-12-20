package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private  TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    public void processIncomingVehicle() {
        try{
            // ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();


            Object[] result = getNextParkingNumberIfAvailable();
            ParkingSpot parkingSpot = (ParkingSpot) result[0];
            ParkingType parkingType = (ParkingType) result[1];

            if(parkingSpot !=null && parkingSpot.getId() > 0){
                String vehicleRegNumber = getVehichleRegNumber();
                // check if the vehicule is already inside, if so do not create a ticket
                boolean isVehiculeAlreadyInside = ticketDAO.isTicketAlreadyExisting(vehicleRegNumber, parkingType);
                if(!isVehiculeAlreadyInside){
                    System.out.println("Process to ticket creation");
                    parkingSpot.setAvailable(false);
                    parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false

                    Date inTime = new Date();
                    Ticket ticket = new Ticket();
                    //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                    //ticket.setId(ticketID);
                    ticket.setParkingSpot(parkingSpot);
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(0);
                    ticket.setInTime(inTime);
                    ticket.setOutTime(null);
                    ticket.setVehicleType(parkingType);
                    ticketDAO.saveTicket(ticket);
                    System.out.println("Generated Ticket and saved in DB");
                    System.out.println("Please park your vehicle in spot number:"+parkingSpot.getId());
                    System.out.println("Recorded in-time for vehicle number:"+vehicleRegNumber+" is:"+inTime);
                }
                else{
                    System.out.println("Can't process to ticket creation, vehicle already inside");
                }

            }
            else {
                // logger.info("The parking don't have anymore slots for "+parkingType);
            }
        }catch(Exception e){
            logger.error("Unable to process incoming vehicle",e);
        }
    }

    private String getVehichleRegNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }


    public Object[] getNextParkingNumberIfAvailable(){
        int parkingNumber=0;
        ParkingSpot parkingSpot = null;
        ParkingType parkingType = null;
        try{
            // ParkingType parkingType = getVehichleType();
            parkingType = getVehichleType();
            // Search if the vehicle is already inside before to look for a spot

            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if(parkingNumber > 0){
                parkingSpot = new ParkingSpot(parkingNumber,parkingType, true);

            }else{
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        }catch(IllegalArgumentException ie){
            logger.error("Error parsing user input for type of vehicle", ie);
        }catch(Exception e){
            logger.error("Error fetching next available parking slot", e);
        }
        return new Object[] {parkingSpot, parkingType};
    }

    private ParkingType getVehichleType(){
    System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch(input){
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    public void processExitingVehicle() {
        try{
            ParkingType parkingType = getVehichleType();
            String vehicleRegNumber = getVehichleRegNumber();

            // System.out.println("processExitingVehicle\n");
            // System.out.println("vehicleRegNumber : "+vehicleRegNumber+" vehicleType : "+parkingType);
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber, parkingType.toString());
            // System.out.println("Ticket passed to calculate the fare :");
            // System.out.println("Vehicle type : "+ticket.getVehicleType());
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if(ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            }else{
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }


}
