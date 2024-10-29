package com.parkit.parkingsystem.constants;

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, VEHICLE_TYPE) values(?,?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    // public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    // Edited request to integrate the vehicle type parameter not depending on the parking table
    public static final String GET_TICKET =
            "select ticket.PARKING_NUMBER, ticket.ID, ticket.PRICE, ticket.IN_TIME, ticket.OUT_TIME, ticket.VEHICLE_TYPE \n"+
            "from ticket, parking \n"+
            "where parking.PARKING_NUMBER = ticket.PARKING_NUMBER \n"+
            "and ticket.VEHICLE_REG_NUMBER=? \n"+
            "and ticket.VEHICLE_TYPE=? \n"+
            "and ticket.OUT_TIME IS NULL \n"+
            "order by ticket.IN_TIME  limit 1";

    // Request to check if the vehicle is already inside the parking before to look for an available place for it and therefore creating a new ticket
    public static final String IS_VEHICLE_ALREADY_IN = "SELECT *\n" +
            "FROM ticket\n" +
            "WHERE VEHICLE_REG_NUMBER = ?\n" +
            "  AND VEHICLE_TYPE = ?\n" +
            "  AND IN_TIME IS NOT NULL\n" +
            "  AND OUT_TIME IS NULL;";
}

// Créer une requete permettant de savoir si il s'agit d'un utilisateur récurrent à partir de ?
// -> du fait qu'il soit deja présent dans la database ?
// -> créer une colonne pour indiquer si le véhicule est récurrent ou non


