package ru.yakunina.filmrest.domain;

import lombok.ToString;
import javax.persistence.*;

@Entity
@Table
@ToString(of = {"id"})
public class MedType {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String typename;  // тип лекарства

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
