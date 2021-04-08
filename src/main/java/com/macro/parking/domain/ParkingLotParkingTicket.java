package com.macro.parking.domain;

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
    @Column(name = "parking_lot_parking_ticket_id", nullable = false)
    private int parkingLotParkingTicketId;
    
	@ManyToOne
    @JoinColumn(name = "parking_lot_id", insertable=false, updatable = false)
    private ParkingLot parkingLot;

    @ManyToOne
    @JoinColumn(name = "parking_ticket_id", insertable=false, updatable = false)
    private ParkingTicket parkingTicket;

    

}
