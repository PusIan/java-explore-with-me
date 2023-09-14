package ru.practicum.explorewithme.model;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "event")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "category_id")
    private Category category;
    private LocalDateTime createdOn = LocalDateTime.now();
    @Column(length = 7000)
    private String description;
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;
    @Column(length = 120)
    private String title;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Request> requests;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
    @Formula("(select count(r.id) from Request r where r.event_id = id and r.status = 'CONFIRMED')")
    private Integer confirmedRequests;
    private Integer rating = 0;
    @Transient
    private Long views;
}
