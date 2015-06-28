package com.letsdolunch.letsdolunch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;
    @NotNull
    @NotEmpty
    @JsonProperty
    private String message;

    public Optional<Long> id() {
        return Optional.ofNullable(id);
    }

    public String message() {
        return message;
    }
}
