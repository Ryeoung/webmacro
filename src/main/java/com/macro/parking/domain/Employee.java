package com.macro.parking.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table
public class Employee {
	@Id
    @GeneratedValue
    @Column(name = "employee_id", nullable = false)
    private int employeeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private int bankAccount;

    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable = false)
    private ParkingLot parkingLot;
}
