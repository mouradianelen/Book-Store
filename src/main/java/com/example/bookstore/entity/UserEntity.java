package com.example.bookstore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "store_user", schema = "bookstore")
public class UserEntity implements Serializable {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
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
    @Column
    private String name;
    @Column
    private String lastName;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private Boolean enabled = false;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", schema = "bookstore",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> roles = new HashSet<>();

    public UserEntity() {

    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setRole(UserRole role) {
        this.roles.add(role);
    }


}
