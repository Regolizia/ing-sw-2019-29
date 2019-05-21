package adrenaline;

import adrenaline.Figure;
import org.junit.jupiter.api.Test;

public class FigureTest {

    @Test
    public void testConstructor() {
        Figure f = new Figure(Figure.PlayerColor.BLUE);
        Figure.PlayerColor p = f.getPlayerColor();
        f.setPlayerColor(Figure.PlayerColor.YELLOW);

    }
}
