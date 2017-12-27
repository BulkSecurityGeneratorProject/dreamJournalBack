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
}
