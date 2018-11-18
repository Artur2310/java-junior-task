package com.mcb.javajuniortask.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Client {
    @Id
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "client")
    @Cascade(CascadeType.ALL)
    List<Debt> debts = new LinkedList<>();
    
    @OneToMany(mappedBy = "client")
    @Cascade(CascadeType.ALL)
    List<Payment> payments = new LinkedList<>();
}
