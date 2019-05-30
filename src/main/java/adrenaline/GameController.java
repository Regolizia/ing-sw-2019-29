package adrenaline;

import java.util.LinkedList;

public class GameController {
    private GameModel model;
    private ClientCLI viewCli;
    private ClientGUI viewGui;

    public GameController(GameModel model, ClientCLI viewCli, ClientGUI viewGui){
       this.viewGui=viewGui;
       this.viewCli=viewCli;
       this.model=model;

    }
//todo viewGui
    public void playerSettingNameAndColor(String name,Figure.PlayerColor color,CoordinatesWithRoom spw){
        if(model.getPlayers().size()<5) {
            for (Player player2 : model.getPlayers()
            ) {
                if (player2.getName().equals(name)) {
                    viewCli.sendMessage("this name is already taken");
                    return;
                }
                if (player2.getColor().equals(color)) {
                    viewCli.sendMessage("this color is already taken");
                    return;
                }
                if(player2.getRespawnCoordinates().equals(spw)){
                    viewCli.sendMessage("this spawnpoint is already taken");
                }
            }
            Player player= new Player(spw,color);
            player.setName(name);
            model.getPlayers().add(player);
        }
        else viewCli.sendMessage("can't add another player");

    }


    public void updateView(Player player){
       viewCli.printPlayerDetails(player.getName(),player.returnPoints(),player.getColor());
    }


}
