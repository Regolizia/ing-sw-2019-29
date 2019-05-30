package adrenaline;


import org.junit.jupiter.api.Test;


class ControllerTest {

    static CoordinatesWithRoom coordinates;
  @Test
   void constructor() {
        Spawnpoint s = new Spawnpoint(1, 2);

        GameModel model = getModel(s);

        ClientCLI viewCli = new ClientCLI("127.0.0.1");
        ClientGUI viewGui = new ClientGUI("127.0.0.1");
        GameController controller = new GameController(model, viewCli, viewGui);
        // ClientGUI viewGui=new ClientGUI("127.0.0.1");


        if (model.getPlayers().size() > 0) {
            for (Player player : model.getPlayers()) {
                controller.updateView(player);
            }
        }

        model.getMapUsed().getGameBoard().getRoom(3).addSpawnpoint(s);
        //update model data
        //int x, int y, Room r

        model.getMapUsed().getGameBoard().getRoom(1).addSpawnpoint(s);
        model.getMapUsed().getGameBoard().getRoom(3).addSpawnpoint(s);
        controller.playerSettingNameAndColor("PIPPO", Figure.PlayerColor.BLUE, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(1)));
        controller.playerSettingNameAndColor("PLUTO", Figure.PlayerColor.YELLOW, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(1)));
        controller.playerSettingNameAndColor("TOM", Figure.PlayerColor.BLUE, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(3)));
        controller.playerSettingNameAndColor("GERRY", Figure.PlayerColor.PURPLE, coordinates);
        controller.playerSettingNameAndColor("LUKE", Figure.PlayerColor.BLUE, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(3)));
        controller.playerSettingNameAndColor("BOT", Figure.PlayerColor.GREEN, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(3)));
        controller.playerSettingNameAndColor("TOt", Figure.PlayerColor.GRAY, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(3)));
        controller.playerSettingNameAndColor("TO", Figure.PlayerColor.NONE, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(3)));
        controller.playerSettingNameAndColor("NOT_accepted", Figure.PlayerColor.PURPLE, new CoordinatesWithRoom(s.getSpawnpointX(), s.getSpawnpointY(), model.getMapUsed().getGameBoard().getRoom(3)));
        for (Player player : model.getPlayers()) {
            controller.updateView(player);
        }
    }

    private static GameModel  getModel(Spawnpoint s){
        GameModel model = new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT, 1 );
        getCoordinate(model);
        Player player=new Player(coordinates, Figure.PlayerColor.YELLOW);
        player.setName("PIPPO");
        model.addPlayer(player);
        return model;
    }
    @Test
    private static Room getRoom(GameModel model ){
        return model.getMapUsed().getGameBoard().getRoom(3);
    }
    @Test
    private static void getCoordinate(GameModel m){
        coordinates= new CoordinatesWithRoom(1,2,m.getMapUsed().getGameBoard().getRoom(3));
    }

}
