package ru.yakunina.filmrest.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString(of = {"id", "username", "password", "phonenumber"})
public class Usr {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonView(Views.IdUser.class)
    private Long id;
    @JsonView(Views.IdNameUser.class)
    private String username;
    @JsonView(Views.FullUser.class)        //
    private String password;
    @JsonView(Views.IdNameUser.class)
    private String phonenumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
