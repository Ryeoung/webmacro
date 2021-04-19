package com.macro.parking.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @Column(name = "number", nullable = false)
    private String number;


    @OneToMany(mappedBy = "car")
    private List<ParkingTicket> parkingTicket = new ArrayList<>();

}
