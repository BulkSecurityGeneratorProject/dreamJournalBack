package pl.teneusz.dream.journal.service.dto;

import pl.teneusz.dream.journal.domain.UserDetails;

public class UserInfoDto {

    private Long userId;
    private UserDetails details;
    private Long dreamCount;
    private Long commentCount;

    public UserInfoDto(Long userId, UserDetails details, Long dreamCount, Long commentCount) {
        this.userId = userId;
        this.details = details;
        this.dreamCount = dreamCount;
        this.commentCount = commentCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    public Long getDreamCount() {
        return dreamCount;
    }

    public void setDreamCount(Long dreamCount) {
        this.dreamCount = dreamCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}
