package com.letsdolunch.letsdolunch.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  @Size(min = 2, max = 80)
  private String name;

  public User() { }

  public User(long id) { 
    this.id = id;
  }

  public User(String name) {
    this.name = name;
  }

  // getter and setter methods ...
  
} // class User