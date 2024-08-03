package com.cgtfarmer.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {

  private int id;

  private String first_name;

  private String last_name;

  private int age;

  private float weight;

  private boolean smoker;
}
