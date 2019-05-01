package adrenaline;

import adrenaline.Action;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import static adrenaline.GameModel.Mode.DEATHMATCH;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    private Action action;

//______________________________________________Test Constructor______________________________________________________//

    @Test
    void newStart() {
        action = new Action(true);
        if (action.newStart) {
            assertEquals(false, action.endTurn, "newTurn");
            assertEquals(false, action.executedFirstAction, "new Turn");
            assertEquals(false, action.executedSecondAction, "newTurn");
            assertEquals(false, action.deletedAction, "newTurn");
        }
    }

    @Test
    void noNewStart() {
        action = new Action(false);
        if (action.endTurn) {
            assertEquals(false, action.endTurn, "newTurn");
            assertEquals(false, action.executedFirstAction, "new Turn");
            assertEquals(false, action.executedSecondAction, "newTurn");
            assertEquals(false, action.deletedAction, "newTurn");
        }
    }

    //______________________________________Type Action___________________________________________________________________//
    @Test
    void typeAction() {
        newStart();
        Action.ActionType actionSelected = Action.ActionType.RUN;
    }

    //______________________________________Pay Option____________________________________________________________________//
    @Test
    void payOption() {
        newStart();
        Action.PayOption paymentSelected = Action.PayOption.AMMO;

    }
}
