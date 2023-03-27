package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Entity
@Table(name = "requests")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description", nullable = false)
    String description;

    @With
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @With
    @Column(name = "create_date", nullable = false)
    LocalDateTime created;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Item> items;
}
