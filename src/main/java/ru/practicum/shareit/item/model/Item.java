package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Entity
@Table(name = "items")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "is_available")
    boolean available;

    @With
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @With
    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Comment> comments;
}
