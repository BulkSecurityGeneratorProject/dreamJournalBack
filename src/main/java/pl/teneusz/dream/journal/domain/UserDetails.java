package pl.teneusz.dream.journal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import pl.teneusz.dream.journal.domain.enumeration.GenderEnum;

/**
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userdetails")
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "birth_date")
    private ZonedDateTime birthDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public UserDetails gender(GenderEnum gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public UserDetails birthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public User getUser() {
        return user;
    }

    public UserDetails user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetails userDetails = (UserDetails) o;
        if (userDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", gender='" + getGender() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }


}
