package adrenaline;

import adrenaline.Figure;
import org.junit.jupiter.api.Test;

import javax.swing.text.rtf.RTFEditorKit;

public class FigureTest {
private Figure fig;
   /* @Test
    public void testConstructor() {
        Figure f = new Figure(Figure.PlayerColor.BLUE);
        Figure.PlayerColor p = f.getPlayerColor();
        f.setPlayerColor(Figure.PlayerColor.YELLOW);

    }*/
   /*test Figure creation, Figure.PlayerColor, getPlayerColor and setPlayerColor*/
   @Test
    void constructor(){
       fig=new Figure(Figure.PlayerColor.PURPLE);
       fig.getPlayerColor();
       fig.setPlayerColor(Figure.PlayerColor.YELLOW);
   }
}
