package pl.teneusz.dream.journal.service.dto;

public class DiagramDto {

    private Double x;
    private Double y;

    public DiagramDto() {
    }

    public DiagramDto(Number x, Number yValue) {
        this.x = x.doubleValue();
        this.y = yValue.doubleValue();
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
