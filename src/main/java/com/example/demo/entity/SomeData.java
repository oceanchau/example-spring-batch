package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SomeData {
    // Class fields and methods go here
    private String name;
    private int age;

    private boolean status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
