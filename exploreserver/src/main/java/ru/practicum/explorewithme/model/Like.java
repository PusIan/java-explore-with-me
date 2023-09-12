package ru.practicum.explorewithme.model;

import javax.persistence.*;

@Entity
@Table(name = "_like", uniqueConstraints = {
        @UniqueConstraint(name = "uq_like_event_user", columnNames = {"event_id", "user_id"})})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Boolean isPositive;

    public Like(Long id, Event event, User user, Boolean isPositive) {
        this.id = id;
        this.event = event;
        this.user = user;
        this.isPositive = isPositive;
    }

    public Like() {
    }

    public static LikeBuilder builder() {
        return new LikeBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getIsPositive() {
        return this.isPositive;
    }

    public void setIsPositive(Boolean isPositive) {
        this.isPositive = isPositive;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Like)) return false;
        final Like other = (Like) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Like;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public String toString() {
        return "Like(id=" + this.getId() + ", isPositive=" + this.getIsPositive() + ")";
    }

    public static class LikeBuilder {
        private Long id;
        private Event event;
        private User user;
        private Boolean isPositive;

        LikeBuilder() {
        }

        public LikeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LikeBuilder event(Event event) {
            this.event = event;
            return this;
        }

        public LikeBuilder user(User user) {
            this.user = user;
            return this;
        }

        public LikeBuilder isPositive(Boolean isPositive) {
            this.isPositive = isPositive;
            return this;
        }

        public Like build() {
            return new Like(this.id, this.event, this.user, this.isPositive);
        }

        public String toString() {
            return "Like.LikeBuilder(id=" + this.id + ", event=" + this.event + ", user=" + this.user + ", isPositive=" + this.isPositive + ")";
        }
    }
}
