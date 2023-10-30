package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SomeDataFeedback {
    // Class fields and methods go here
    private String name;
    private int age;

    private boolean status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}


/* TODO
 * example for reading some_data_feedback and updating batch to some_data
 * - create entity, repository, service some_data
 * - enhance save some_data in writer
 * - refactoring for easy implementation later
 * example for reading data from two tables and preparing data to write to one table
 *
 * */