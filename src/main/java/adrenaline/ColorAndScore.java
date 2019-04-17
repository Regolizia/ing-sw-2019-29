package adrenaline;

// I THINK IT HAS SOMETHING TO DO WITH SCORING THE POINTS WHEN SOMEONE DIES
public class ColorAndScore {
    private Figure.PlayerColor color;
    private int score;

    public ColorAndScore(){
        score = 0;
    }
    public ColorAndScore(Figure.PlayerColor c, int x){
        this.color = c;
        this.score = x;
    }

    public Figure.PlayerColor getColor(){
        return color;
    }
    public int getScore(){
        return score;
    }


}
