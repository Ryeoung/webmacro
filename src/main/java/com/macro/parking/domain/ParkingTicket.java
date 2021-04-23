package com.macro.parking.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @Column(name = "app_flag")
    private boolean appFlag;
    
    @Column(name = "parking_ticket_name")
    private String parkingTicketName;
    
    @Column(name = "order_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderTime;
    
    @Column(name = "parking_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime parkingTime;

    @Column(name = "exit_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime exitTime;    
    
}
