package pl.teneusz.dream.journal.service.dto;

import pl.teneusz.dream.journal.domain.UserDetails;

public class UserInfoDto {

    private Long userId;
    private UserDetails details;
    private Integer dreamCount;
    private Integer commentCount;

    public UserInfoDto(Long userId, UserDetails details, Integer dreamCount, Integer commentCount) {
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

    public Integer getDreamCount() {
        return dreamCount;
    }

    public void setDreamCount(Integer dreamCount) {
        this.dreamCount = dreamCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}
