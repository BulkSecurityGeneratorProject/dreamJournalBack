package pl.teneusz.dream.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Dream.
 */
@Entity
@Table(name = "dream")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dream")
public class Dream implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "description")
    private String description;

    @Column(name = "is_lucid")
    private Boolean isLucid;

    @Min(value = 1)
    @Column(name = "night_index")
    private Integer nightIndex;

    @Column(name = "visibility")
    private Boolean visibility;

    @Min(value = 5)
    @Max(value = 90)
    @Column(name = "lenght")
    private Integer lenght;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "score")
    private Integer score;

    @ManyToOne
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "dream_tags",
               joinColumns = @JoinColumn(name="dreams_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "dream", fetch = FetchType.LAZY)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Comment> comments = new HashSet<>();

    @Transient
    @JsonInclude
    private Integer commentCount = 0;

    @PostLoad
    void onLoad(){
        commentCount = comments.size();
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Dream title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public Dream createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public Dream description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsLucid() {
        return isLucid;
    }

    public Dream isLucid(Boolean isLucid) {
        this.isLucid = isLucid;
        return this;
    }

    public void setIsLucid(Boolean isLucid) {
        this.isLucid = isLucid;
    }

    public Integer getNightIndex() {
        return nightIndex;
    }

    public Dream nightIndex(Integer nightIndex) {
        this.nightIndex = nightIndex;
        return this;
    }

    public void setNightIndex(Integer nightIndex) {
        this.nightIndex = nightIndex;
    }

    public Boolean isVisibility() {
        return visibility;
    }

    public Dream visibility(Boolean visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Integer getLenght() {
        return lenght;
    }

    public Dream lenght(Integer lenght) {
        this.lenght = lenght;
        return this;
    }

    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }

    public Integer getScore() {
        return score;
    }

    public Dream score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public Dream user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Dream tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Dream addTags(Tag tag) {
        this.tags.add(tag);
        tag.getDreams().add(this);
        return this;
    }

    public Dream removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getDreams().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Dream comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Dream addComments(Comment comment) {
        this.comments.add(comment);
        comment.setDream(this);
        return this;
    }

    public Dream removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setDream(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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
        Dream dream = (Dream) o;
        if (dream.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dream.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Dream{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", isLucid='" + isIsLucid() + "'" +
            ", nightIndex='" + getNightIndex() + "'" +
            ", visibility='" + isVisibility() + "'" +
            ", lenght='" + getLenght() + "'" +
            ", score='" + getScore() + "'" +
            "}";
    }
}
