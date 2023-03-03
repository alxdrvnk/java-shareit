package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "email", nullable = false)
    String email;
}