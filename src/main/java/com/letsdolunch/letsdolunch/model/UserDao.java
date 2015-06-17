package com.letsdolunch.letsdolunch.model;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface UserDao extends CrudRepository<User, Integer> {

  /**
   * This method is not implemented and its working code will be
   * automagically generated from its signature by Spring Data JPA.
   *
   * @param name the user name.
   * @return the user having the passed name or null if no user is found.
   */
  public User findByName(String name);

} // class UserDao