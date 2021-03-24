package com.macro.parking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "parking_lot_parking_ticket")
public class ParkingLotParkingTicket {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable = false)
    private ParkingLot parkingLot;

    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable = false)
    private ParkingTicket parkingTicket;



}
