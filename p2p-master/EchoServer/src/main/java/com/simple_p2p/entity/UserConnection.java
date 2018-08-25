package com.simple_p2p.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_connection")
public class UserConnection implements Serializable,EntityUniqueConstrain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;


    private LocalDateTime date_time;

    @Column(name = "inet_adress")
    private String inetAddress;


    @Column(name = "online")
    private int online;

    public UserConnection() {

    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void setDate_time(LocalDateTime date_time) {
        this.date_time = date_time;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate_time() {
        return date_time;
    }

    public int getOnline() {
        return online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConnection that = (UserConnection) o;
        return online == that.online &&
                Objects.equals(inetAddress, that.inetAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inetAddress, online);
    }

    @Override
    public String toString() {
        return "UserConnection{" +
                "id=" + id +
                ", date_time=" + date_time +
                ", inetAddress='" + inetAddress + '\'' +
                ", online=" + online +
                '}';
    }

    @Override
    public String getUniqueConstrainName() {
        return "inet_adress";
    }

    @Override
    public Object getUniqueConstrainValue() {
        return inetAddress;
    }
}
