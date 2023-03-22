package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "email", unique = true)
    String email;
}