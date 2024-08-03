package com.cgtfarmer.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  private int id;

  private String firstName;

  private String lastName;

  private int age;

  private float weight;

  private boolean smoker;
}
