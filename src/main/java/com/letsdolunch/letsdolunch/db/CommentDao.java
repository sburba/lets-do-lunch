package com.letsdolunch.letsdolunch.db;

import com.letsdolunch.letsdolunch.model.Comment;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface CommentDao extends CrudRepository<Comment, Long> {

}