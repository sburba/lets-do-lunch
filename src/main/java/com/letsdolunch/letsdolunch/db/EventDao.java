package com.letsdolunch.letsdolunch.db;

import com.letsdolunch.letsdolunch.model.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@Transactional
public interface EventDao extends CrudRepository<Event, Long> {
    @Query(value = "SELECT e FROM com.letsdolunch.letsdolunch.model.Event e WHERE e.time > :time")
    List<Event> findEventsNewerThan(@Param("time") OffsetDateTime time);
}