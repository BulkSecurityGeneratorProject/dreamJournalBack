package pl.teneusz.dream.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "is_reply")
    private Boolean isReply;

    @ManyToOne
    private User user;

    @ManyToOne
    private Dream dream;

    @ManyToOne
    @JsonIgnore
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> childs = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Comment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public Comment createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Boolean isIsReply() {
        return isReply;
    }

    public Comment isReply(Boolean isReply) {
        this.isReply = isReply;
        return this;
    }

    public void setIsReply(Boolean isReply) {
        this.isReply = isReply;
    }

    public User getUser() {
        return user;
    }

    public Comment user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dream getDream() {
        return dream;
    }

    public Comment dream(Dream dream) {
        this.dream = dream;
        return this;
    }

    public void setDream(Dream dream) {
        this.dream = dream;
    }

    public Comment getParent() {
        return parent;
    }

    public Comment parent(Comment comment) {
        this.parent = comment;
        return this;
    }

    public void setParent(Comment comment) {
        this.parent = comment;
    }

    public Set<Comment> getChilds() {
        return childs;
    }

    public Comment childs(Set<Comment> comments) {
        this.childs = comments;
        return this;
    }

    public Comment addChilds(Comment comment) {
        this.childs.add(comment);
        comment.setParent(this);
        return this;
    }

    public Comment removeChilds(Comment comment) {
        this.childs.remove(comment);
        comment.setParent(null);
        return this;
    }

    public void setChilds(Set<Comment> comments) {
        this.childs = comments;
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
        Comment comment = (Comment) o;
        if (comment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", isReply='" + isIsReply() + "'" +
            "}";
    }
}
