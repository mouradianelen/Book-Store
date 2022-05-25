package com.example.bookstore.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store_user", schema = "bookstore")
public class UserEntity implements Serializable {
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    Set<BookRating> ratings = new HashSet<>();
    @OneToMany(mappedBy = "user")
    Set<BookOrder> orders = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String location;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", schema = "bookstore",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> roles = new HashSet<>();

    public UserEntity() {

    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<BookOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<BookOrder> orders) {
        this.orders = orders;
    }

    public Set<BookRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<BookRating> ratings) {
        this.ratings = ratings;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
