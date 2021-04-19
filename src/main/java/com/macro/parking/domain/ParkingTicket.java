package com.macro.parking.domain;

import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "parking_ticket")
public class ParkingTicket {
	 @Id
	 @GeneratedValue
	 @Column(name = "parking_ticket_id", nullable = false)
	 private int parkingTicketId;
	 
    @ManyToOne
    @JoinColumn(name = "car_number", updatable = false)
    private Car car;


    @ManyToOne
    @JoinColumn(name = "parking_lot_id", updatable = false)
    private ParkingLot parkingLot;

    @Column(name = "parking_ticket_name")
    private String parkingTicketName;
    
    @Column(name = "parking_time")
    private String parkingTime;

    @Column(name = "exit_time")
    private String exitTime;
    
}
