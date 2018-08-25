package com.simple_p2p.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KnownUsers")
public class KnownUsers implements Serializable {

    @Id
/*    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int Id;*/

    @Column(name = "userHash")
    private String userHash;

    @Column(name = "username")
    private String userName;

    @Column(name = "internalIpAddress")
    private String internalIpAddress;

    @Column(name = "externalIpAddress")
    private String externalIpAddress;

    public KnownUsers() {

    }

    public KnownUsers(String userHash, String userName, String internalIpAddress, String externalIpAddress) {
        this.userHash = userHash;
        this.userName = userName;
        this.internalIpAddress = internalIpAddress;
        this.externalIpAddress = externalIpAddress;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInternalIpAddress() {
        return internalIpAddress;
    }

    public void setInternalIpAddress(String internalIpAddress) {
        this.internalIpAddress = internalIpAddress;
    }

    public String getExternalIpAddress() {
        return externalIpAddress;
    }

    public void setExternalIpAddress(String externalIpAddress) {
        this.externalIpAddress = externalIpAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnownUsers that = (KnownUsers) o;
        return Objects.equals(userHash, that.userHash) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(internalIpAddress, that.internalIpAddress) &&
                Objects.equals(externalIpAddress, that.externalIpAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userHash, userName, internalIpAddress, externalIpAddress);
    }
}


