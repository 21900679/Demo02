import java.awt.*;

public class undo {
    Point uStart;
    Point uEnd;
    int uMode;
    Color uColor;
    Float uStroke;

    undo(Point uStart, Point uEnd, int uMode, Color uColor, Float uStroke){
        this.uStart = uStart;
        this.uEnd = uEnd;
        this.uMode = uMode;
        this.uColor = uColor;
        this.uStroke = uStroke;
    }

    public Point getuStart() {
        return uStart;
    }

    public void setuStart(Point uStart) {
        this.uStart = uStart;
    }

    public Point getuEnd() {
        return uEnd;
    }

    public void setuEnd(Point uEnd) {
        this.uEnd = uEnd;
    }

    public int getuMode() {
        return uMode;
    }

    public void setuMode(int uMode) {
        this.uMode = uMode;
    }

    public Color getuColor() {
        return uColor;
    }

    public void setuColor(Color uColor) {
        this.uColor = uColor;
    }

    public Float getuStroke() {
        return uStroke;
    }

    public void setuStroke(Float uStroke) {
        this.uStroke = uStroke;
    }

}
