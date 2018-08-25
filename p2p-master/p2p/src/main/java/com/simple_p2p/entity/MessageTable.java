package com.simple_p2p.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message_history")
public class MessageTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "username")
    private String userName;

    @Column(name = "message")
    private String message;

    public MessageTable() {

    }

    public MessageTable(LocalDateTime created, String userName, String message) {
        this.created = created;
        this.userName = userName;
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageTable that = (MessageTable) o;
        return id == that.id &&
                Objects.equals(created, that.created) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, created, userName, message);
    }
}
