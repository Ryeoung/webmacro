package com.macro.parking.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "car")
public class Car {
    @Id
    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "licence")
    private String licence;
    
    @OneToMany(mappedBy = "car")
    private List<ParkingInfo> parkingInfo = new ArrayList<>();

}
