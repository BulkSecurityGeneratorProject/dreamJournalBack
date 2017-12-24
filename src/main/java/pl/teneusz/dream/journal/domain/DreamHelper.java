package pl.teneusz.dream.journal.domain;

public class DreamHelper {
    private Long dreamId;
    private Integer commentCount;

    public DreamHelper(Long dreamId, Integer commentCount) {
        this.dreamId = dreamId;
        this.commentCount = commentCount;
    }

    public Long getDreamId() {
        return dreamId;
    }

    public void setDreamId(Long dreamId) {
        this.dreamId = dreamId;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}
