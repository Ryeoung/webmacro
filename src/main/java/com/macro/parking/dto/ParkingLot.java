package com.macro.parking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "open_time", nullable = false)
    private String openTime;

    @Column(name = "close_time", nullable = false)
    private String closeTime;

    @Column(name = "website")
    private String website;

    @Column(name = "web_id")
    private String webId;

    @Column(name = "web_pwd")
    private String webPwd;

    @OneToMany(mappedBy = "parkingLot")
    private List<CarParkingLot> carParkingLots = new ArrayList<>();

    @OneToMany(mappedBy = "parkingLot")
    private List<ParkingLotParkingTicket> parkingLotParkingTickets = new ArrayList<>();

    @OneToMany(mappedBy = "parkingLot")
    private List<Employee> employees = new ArrayList<>();
}
