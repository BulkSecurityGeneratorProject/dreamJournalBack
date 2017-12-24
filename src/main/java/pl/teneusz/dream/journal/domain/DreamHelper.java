package pl.teneusz.dream.journal.domain;

public class DreamHelper {
    private Long dreamId;
    private Long commentCount;

    public DreamHelper(Long dreamId, Long commentCount) {
        this.dreamId = dreamId;
        this.commentCount = commentCount;
    }

    public Long getDreamId() {
        return dreamId;
    }

    public void setDreamId(Long dreamId) {
        this.dreamId = dreamId;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}
