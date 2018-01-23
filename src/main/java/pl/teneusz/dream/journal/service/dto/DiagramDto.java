package pl.teneusz.dream.journal.service.dto;

public class DiagramDto {

    private String x;
    private String y;

    public DiagramDto() {
    }

    public DiagramDto(String x, String yValue) {
        this.x = x;
        this.y = yValue;
    }

    public DiagramDto(Number x, Number yValue) {
        this.x = x.toString();
        this.y = yValue.toString();
    }

    public DiagramDto(String x, Number yValue) {
        this.x = x.toString();
        this.y = yValue.toString();
    }

    public DiagramDto(Number x, String yValue) {
        this.x = x.toString();
        this.y = yValue.toString();
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
