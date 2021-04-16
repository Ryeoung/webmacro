package com.macro.parking.domain;

import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "car_parking_lot")
public class CarParkingLot {
	 @Id
	 @GeneratedValue
	 @Column(name = "car_parking_lot_id", nullable = false)
	 private int carParkingLotId;
	 
    @ManyToOne
    @JoinColumn(name = "car_id", insertable=false, updatable = false)
    private Car car;


    @ManyToOne
    @JoinColumn(name = "parking_lot_id", insertable=false, updatable = false)
    private ParkingLot parkingLot;

    @Column
    private String parkingTime;

    @Column
    private String exitTime;
    
    @Column
    private String parkingTicketName;

}
