package com.macro.parking.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JoinColumn(name = "parking_lot_id", updatable = false)
    private ParkingLot parkingLot;

    @Column(name = "web_name")
    private String webName;
    
    @Column(name = "app_name")
    private String appName;
    
    @Column(name = "hour")
    private Integer hour;
    
    @Column(name = "price")
    private Integer price;
    
    @OneToMany(mappedBy = "parkingTicket")
    private List<ParkingInfo> parkingInfo = new ArrayList<>();   
}