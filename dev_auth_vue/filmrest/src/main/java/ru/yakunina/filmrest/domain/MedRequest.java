package ru.yakunina.filmrest.domain;

import lombok.ToString;
import javax.persistence.*;

@Entity
@Table
@ToString(of = {"id"})
public class MedRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MedStorage medstorage;  // лекарство
    private String requestdate;     // дата заявки
    private int status;             // статус заявки 0-заявка подана  1-выполнена

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MedStorage getMedstorage() {
        return medstorage;
    }

    public void setMedstorage(MedStorage medstorage) {
        this.medstorage = medstorage;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
