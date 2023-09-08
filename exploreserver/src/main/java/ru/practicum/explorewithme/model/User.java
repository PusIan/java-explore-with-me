package ru.practicum.explorewithme.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 254)
    private String email;
    @Column(length = 250)
    private String name;
}
