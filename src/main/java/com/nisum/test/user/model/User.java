package com.nisum.test.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Builder
public class User {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phones")
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "user_id")
    private List<Phone> phones;

    @Column(name = "created")
    private String created;

    @Column(name = "modified")
    private String modified;

    @Column(name = "last_login")
    private String lastLogin;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "token")
    private String token;


}
