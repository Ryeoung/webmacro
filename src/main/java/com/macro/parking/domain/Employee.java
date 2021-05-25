package com.macro.parking.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table
public class Employee {
	@Id
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

    @Column(name = "admin_id")
    private String admin_id;

    @Column(name = "admin_password")
    private String admin_password ;

    @OneToMany(mappedBy = "employee")
    private List<Management> managements = new ArrayList<>();

}
