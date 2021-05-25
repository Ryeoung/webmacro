package com.macro.parking.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "management")
public class Management {
    @Id
    @GeneratedValue
    @Column(name = " management_id", nullable = false)
    private Long managementId;


    @ManyToOne
    @JoinColumn(name = "parking_lot_id", updatable = false)
    private ParkingLot parkingLot;


    @ManyToOne
    @JoinColumn(name = "employee_id", updatable = false)
    private Employee employee;


}
