package ru.yakunina.filmrest.domain;

import lombok.ToString;
import javax.persistence.*;

@Entity
@Table
@ToString(of = {"id"})
public class MedStorage {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MedType medtype;  // тип лекарства
    private String name;      // название лекарства
    private int cnt;          // количество имеющегося лекарства

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MedType getMedtype() {
        return medtype;
    }

    public void setMedtype(MedType medtype) {
        this.medtype = medtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
