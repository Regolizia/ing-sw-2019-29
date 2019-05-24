package adrenaline;

import java.util.LinkedList;

public class GameController {
    private GameModel model;
    private Server view;
    private LinkedList<Player> players;

    public GameController(){
        view=new Server();
        model=new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,view.getBoardChosen());

    }

    public void StartGame(){
        try{ while(view.getGameIsOn()){




            }

        }catch (Error errorGameNotOn){

        }

    }

}
