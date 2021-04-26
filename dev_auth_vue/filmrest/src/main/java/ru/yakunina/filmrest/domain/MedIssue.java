package ru.yakunina.filmrest.domain;

import lombok.ToString;
import javax.persistence.*;

@Entity
@Table
@ToString(of = {"id"})
public class MedIssue {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MedStorage medstorage;   // лекарство
    private int cnt;                 // количество
    private String issuedate;       //  дата выдачи

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

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }
}
