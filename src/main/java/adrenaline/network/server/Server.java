package adrenaline.network.server;

// SOCKET
import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// RMI

public class Server {

    private static GameModel model;
    private static Action action;

    private static int currentPlayer = 0;
    private static boolean isFirstTurn = true;

    private static int time = 0;
    private static int connectionsCount = 0;
    private static int boardChosen = 0;
    private static boolean gameIsOn = false;

    // STRING LIST OF THE COLORS A PLAYER CAN CHOOSE AND LIST OF THOSE ALREADY CHOSEN
    private static List<String> possibleColors = Stream.of(Figure.PlayerColor.values())
            .map(Figure.PlayerColor::name)
            .collect(Collectors.toList());
    private static ArrayList<String> colorsChosen = new ArrayList<>();

    // All client names, so we can check for duplicates upon registration.
    private static ArrayList<String> names = new ArrayList<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static List<ObjectOutputStream> writers = new ArrayList<>();


    private static ServerSocket serverSocket;
    private Server server;
    private ExecutorService threadPool;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    public void setup(String[] args) throws Exception {
        time =Integer.parseInt(args[0]);
        possibleColors.remove("NONE");

        server = new Server();

        start();
    }

    public Server() {
        threadPool = Executors.newCachedThreadPool();
    }


    public static void start() {
        try {
            serverSocket = new ServerSocket(4321);
        } catch (IOException e) {
            e.printStackTrace();
            // Server non si avvia
        }
        System.out.println("SERVER ONLINE");
        while (true) {

            try {
                Socket socket = serverSocket.accept();

                new RequestHandler(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    public static class RequestHandler extends Thread {

        Figure.PlayerColor color;
        String nickname;

        private final transient Socket socket;

        private final transient ObjectInputStream inputStream;

        private final transient ObjectOutputStream outputStream;

        public RequestHandler(Socket socket) throws IOException {


            this.socket = socket;
            this.outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.outputStream.flush();
            this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

        }

        @Override
        public void run() {
            try {
                try {

                    while (true) {
                        String msg = (String) inputStream.readObject();
                        System.out.println(msg);
                        clientLogin();
                        System.out.println("LOGIN DONE " + nickname);
                                handleTurns();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void sendToClient(String message){
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Client Disconnected");
            }

        }
        public void sendListToClient(List<String> messages) throws IOException {
            outputStream.writeObject(messages);
            outputStream.flush();

        }

        public static void countConnections(){

            Server.connectionsCount++;
        }


        public void clientLogin(){
            try {
                nickname = loginName();

                color = checkColor();

                chooseBoard(nickname); // it checks if is firstPlayer and asks board

                sendToClient("ACCEPTED");

                countConnections();

                if(connectionsCount==3){
                    Countdown c = new Countdown();
                }
                addPlayerToGame(nickname, color);
                writers.add(outputStream);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public String loginName() throws Exception {
            sendToClient("LOGIN");
            String name = (String)inputStream.readObject();
            // Keep requesting a name until we get a unique one.
            while (true) {
                if (name != null || !name.equals("null")) {
                    synchronized (names) {
                        if (!isBlank(name) && !names.contains(name)) {
                            names.add(name);
                            return name;
                        } else if (names.contains(name)) {
                            sendToClient("LOGIN");
                            name = (String)inputStream.readObject();
                        }
                    }
                }
            }
        }

        public Figure.PlayerColor checkColor() throws Exception {

            while (true) {
                String csv = String.join(", ", possibleColors);  // SEND POSSIBLE COLORS
                sendToClient("COLOR");
                sendToClient(csv);
                String color = (String)inputStream.readObject();
                if (color != null) {
                    synchronized (possibleColors) {
                        if (!possibleColors.isEmpty() && (possibleColors.contains(color.toUpperCase()) || possibleColors.contains(color))
                                && (colorsChosen.isEmpty() || (!colorsChosen.contains(color) && !colorsChosen.contains(color.toUpperCase())))) {
                            colorsChosen.add(color.toUpperCase());
                            possibleColors.remove(color.toUpperCase());
                            return Figure.PlayerColor.valueOf(color.toUpperCase());
                        } else if ((!possibleColors.contains(color.toUpperCase()) || !possibleColors.contains(color))
                                && !colorsChosen.contains(color.toUpperCase()) && !colorsChosen.contains(color)) {
                            //  ASK AGAIN FOR COLOR, IT WASN'T ACCEPTED

                        } else if (colorsChosen.contains(color.toUpperCase()) || colorsChosen.contains(color)) {
                            //  ASK AGAIN FOR COLOR, IT WAS A DUPLICATE COLOR
                        }
                    }
                }
            }
        }
        public void addPlayerToGame(String name, Figure.PlayerColor color){
            Server.model.addPlayer(new Player(name, color));

            for (ObjectOutputStream writer : writers) {
                try {
                    writer.writeObject("MESSAGE" + name + " has joined");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void chooseBoard(String name) throws Exception {
            if(name.equals(names.get(0))){

                while(true){
                    int result = 0;
                    sendToClient("BOARD");
                    result = (int)inputStream.readObject();
                    if (result == 1 || result == 2 || result == 3 || result == 4) {
                        Server.setBoardChosen(result);
                        System.out.println("BOARD CHOSEN " + result);
                        boardChosen = result;
                        Server.createBoard();
                        break;
                    } else {
                        //  ASK AGAIN BECAUSE NOT ACCEPTED
                    }

                }
            }
        }
        ////

        public boolean isCurrentPlayer(){
            synchronized (Server.model.getPlayers().get(currentPlayer)){
                 return Server.model.getPlayers().get(currentPlayer).getName().equals(nickname);
            }
        }

        public void handleTurns(){
            System.out.println("TURN OF " + nickname);
            int numberOfActions =0;
            while(true){
                if(isCurrentPlayer()){
                    try {
                        if(isFirstTurn){
                            sendToClient("YOURFIRSTTURN");
                            System.out.println("FIRST TURN");
                            firstTurn();
                            numberOfActions=2;
                        }
                        if(numberOfActions!=2) {
                            sendToClient("YOURTURN");

                            String choice = (String) inputStream.readObject();
                            System.out.println(choice);
                            switch (choice) {
                                case "G":
                                    sendToClient("GRAB");
                                    grab();
                                    numberOfActions++;
                                    break;
                                case "R":
                                    sendToClient("RUN");
                                    playerRun();
                                    numberOfActions++;
                                    break;
                                case "M":
                                    // MAP
                                    break;
                                case "B":
                                    // PLAYER
                                    break;
                                case "C":
                                    // OTHER PLAYERS
                                    break;
                                case "S":
                                default:
                                    //shoot(player);
                                    numberOfActions++;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // END OF TURN
                    if(numberOfActions==2){
                    nextPlayer();
                    numberOfActions=0;
                    System.out.println("CURRENT PLAYER " + currentPlayer);}
                }
                // TODO !Server.action.endOfTheGame(model.getMapUsed().getGameBoard()))
            }
        }

        public void firstTurn(){
                LinkedList<PowerUpCard> twoCards= new LinkedList<>();
                twoCards.add(model.powerUpDeck.deck.removeFirst());
                twoCards.add(model.powerUpDeck.deck.removeFirst());
                List<String> cards = new LinkedList<>();
                cards.add(0,twoCards.get(0).toString());
                cards.add(1,twoCards.get(1).toString());
            try {
                sendListToClient(cards);
                int x =(int)inputStream.readObject();
                // CHIEDI QUALE CARTA DA TENERE (1 o 2) E QUALE DA USARE COME RESPAWN


                PowerUpCard p = twoCards.removeFirst();
                System.out.println("TO KEEP "+p.toString());
                if(x==1){
                    Server.model.getPlayers().get(currentPlayer).getPowerUp().add(p);

                    p = twoCards.removeFirst();
                    System.out.println("TO USE AS POSITION "+p.toString());

                    setInitialPosition(p.getPowerUpColor(), Server.model.getPlayers().get(currentPlayer));
                }else {
                    setInitialPosition(p.getPowerUpColor(), Server.model.getPlayers().get(currentPlayer));

                    p = twoCards.removeFirst();
                    System.out.println("TO USE AS POSITION "+p.toString());

                    Server.model.getPlayers().get(currentPlayer).getPowerUp().add(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * Updates index of next Player.
         * If everybody has played, it resets.
         */
        public static void nextPlayer(){
            if(currentPlayer!=model.getPlayers().size()-1) {currentPlayer++;}
            else{
                currentPlayer = 0;
                isFirstTurn= false;
            }
        }

        public void setInitialPosition(AmmoCube.CubeColor c, Player p){
            for (Room r: model.getMapUsed().getGameBoard().getRooms()){

                if(!r.getSpawnpoints().isEmpty() && r.getSpawnpoints().get(0).getColor().equals(c)){
                   CoordinatesWithRoom c1 = new CoordinatesWithRoom(r.getSpawnpoints().get(0).getSpawnpointX(),r.getSpawnpoints().get(0).getSpawnpointY(),r);
                   p.setPlayerPosition(c1);
                    p.setPlayerPositionSpawnpoint(c1);
                    System.out.println("INITIAL POSITION OF "+p.getName()+" IS  "+p.getCoordinatesWithRooms().toString());
                    // TODO SEND POSITION TO PLAYER MAYBE
                    break;
                }
            }
        }


        public void playerRun(){
            System.out.println("RUN");
            System.out.println("PREVIOUS POSITION "+model.getPlayers().get(currentPlayer).getCoordinatesWithRooms().toString());

            Player player = model.getPlayers().get(currentPlayer);
            LinkedList<CoordinatesWithRoom> cells = action.proposeCellsRun(player.getCoordinatesWithRooms());
            List<String> possibilities = new LinkedList<>();
            for(CoordinatesWithRoom c : cells){
                possibilities.add(c.toString());
            }
            try {
                sendListToClient(possibilities); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int x = (int)inputStream.readObject();
                x--;
                action.run(player,cells.get(x));
                System.out.println("CURRENT POSITION " + player.getCoordinatesWithRooms().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    public void grab(){
        Player player = Server.model.getPlayers().get(currentPlayer);
        LinkedList<CoordinatesWithRoom> possibleCells = action.proposeCellsGrab(player);
        List<String> listOfCells = new LinkedList<>();
        List<String> listOfItems = new LinkedList<>();
        for (CoordinatesWithRoom c : possibleCells){
            listOfCells.add(c.toString());
            if (c.containsSpawnpoint(model)) {
                Spawnpoint s = c.getSpawnpoint(model);
                String weapons = "";
                for(WeaponCard w : s.getWeaponCards()){
                    weapons = weapons.concat(w.toString()+ " ");
                }
                listOfItems.add(weapons);
            }else{ // IT HAS AMMOTILES
                listOfItems.add(c.getRoom().getAmmoTile(c).toString());
            }
        }


        CoordinatesWithRoom chosenCell = new CoordinatesWithRoom();
        try {
            sendListToClient(listOfItems);
            sendListToClient(listOfCells); // RITORNA 1 OPPURE 2 OPPURE 3 ....
            int x = (int)inputStream.readObject();
            x--;
            chosenCell = possibleCells.get(x);
            System.out.println("CELL FOR GRAB " + chosenCell.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(chosenCell.containsSpawnpoint(model)) {
           grabFromSpawnpoint(chosenCell,player);
        }
        else {
            //se non spawnpoint
            //raccolgo un AmmoTile
            /*
            * qui faccio direttamente io l'assegnazione della posizione del player  e rimuovo gia' l'ammotile
            * dalla mappa e ne aggiungo subito un altro
            * già incluso pescaggio power up
            * */
            action.grabTile(player, chosenCell);
        }
    }



    public void shoot(Player player){
        /*
        * a meno di errori quando fai shoot controlla prima che la lista degli effetti pagati non sia nulla*/
        //TODO CHIEDI PAGAMENTO
        Action.PayOption payOption=null;
        LinkedList <EffectAndNumber> paidEffect=new LinkedList<>();
        int number;
        //SAVING PLAYER INITIAL POSITION IN CASE HE CAN'T SHOOT/HE DOESN'T SHOOT
        CoordinatesWithRoom positionBeforeShoot=player.getCoordinatesWithRooms();

        //TODO GIOCATORE SI VUOLE SPOSTARE PRIMA?
        boolean moves=true;
          if(moves==true){
              LinkedList<CoordinatesWithRoom> possibleCells= action.proposeCellsRunBeforeShoot(player);
              //TODO CHIEDI CELLA E METTILA IN PLAYER POSITION
              CoordinatesWithRoom playerPosition=null;
              //set new position
              action.run(player,playerPosition);
          }
        WeaponCard weaponCard =null;
       //TODO MANDA MANO WEAPON AL GIOCATORE
        //TODO SCEGLI ARMA DA USARE E METTILA IN weaponCard
        for (WeaponCard w:player.getHand()
             ) {
            w.toString();
            //ask if wants this card
            //if yes
            weaponCard=w;
            break;

        }

       //TODO RICEVI UN NUMERO DA METODI CARTE
           number=0;

          //if weapon is already reloaded or reloaded alt, player can request other effect

        if(!weaponCard.getReloadAlt()&&!weaponCard.getReload()){
                if(!shootBase(weaponCard,player,number,positionBeforeShoot))
                    {
                        //send message
                       // return false;
                    }
        }
        //TODO VUOI ALTRI EFFETTI?
        boolean otherEffect=true;
            if(otherEffect&&(weaponCard.getReload()||weaponCard.getReloadAlt())){
             paidEffect.addAll(shootOtherEffect(weaponCard,player,number));

            //send possible target
            LinkedList<Object>targets=new LinkedList<>();

            // SOME EFFECTS REQUIRE A CHECK THAT TARGETS ARE DIFFERENT FROM EFFECT TO EFFECT
            // I HAVE TO PASS THE OLD TARGETS
            List<Object> pastTargets = null;

            for (EffectAndNumber e:paidEffect) {
                pastTargets = requestsForEveryWeapon(e, weaponCard, player, model.getMapUsed().getGameBoard(), model, pastTargets);
            }
            for (EffectAndNumber e: paidEffect
            ) {
                action.shoot(weaponCard,player,e,targets);
            }
        }


        weaponCard.setNotReload();
        weaponCard.setReloadAlt(false);
    }

    public LinkedList<EffectAndNumber>  shootOtherEffect(WeaponCard weaponCard,Player player, int number) {

            //TODO CONTROLLA CHE L'EFFETTO NON SIA NE BASE NE ALT
            LinkedList<PowerUpCard> playerPowerUpCards = new LinkedList<>();
            LinkedList<AmmoCube.Effect> effect = new LinkedList<>();
            LinkedList<Action.PayOption> payOptions = new LinkedList<>();
            LinkedList<EffectAndNumber> paidEffect = new LinkedList<>();
            Action.PayOption payOption=null;
            //TODO PER OGNI EFFETTO CHIEDI METODO PAGAMENTO
            for (AmmoCube.Effect e : AmmoCube.Effect.values()) {

                 if(weaponCard.getPrice().contains(e)){
                    //TODO CHIEDI SE VUOI PAGARE QUESTO EFFETTO
                     //IF YES
                     effect.add(e);
                     //TODO CHIEDI PAGAMENTO
                     //payOption= response
                     payOptions.add(payOption);

                 }

            }

            for (AmmoCube.Effect e : effect
            ) {

                if (payOptions.get(effect.indexOf(e)).equals(Action.PayOption.AMMOPOWER)) {
                    playerPowerUpCards.addAll(payWithThesePowerUps(player));
                    player.getPowerUp().removeAll(playerPowerUpCards);
                }
                if (!action.canPayCard(weaponCard, player, payOptions.get(effect.indexOf(e)), e, playerPowerUpCards)) {
                    player.getPowerUp().addAll(playerPowerUpCards);
                    playerPowerUpCards.clear();

                } else {
                    model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);
                    paidEffect.add(action.paidEffect(weaponCard, player, payOptions.get(effect.indexOf(e)), e, playerPowerUpCards, number));
                    playerPowerUpCards.clear();
                }

            }

        return paidEffect;
    }


    public boolean shootBase(WeaponCard weaponCard,Player player,int number,CoordinatesWithRoom positionBeforeShoot){
        //TODO CHIEDI SE VUOLE EFFETTO BASE O ALTERNATIVO
        LinkedList<PowerUpCard>powers=new LinkedList<>();
        AmmoCube.Effect effect=null;
        LinkedList <EffectAndNumber> paidEffect=new LinkedList<>();

        //TODO MANDA MESSAGGIO PER AZIONE NON AVVENUTA
        if(!effect.equals(AmmoCube.Effect.BASE)&&!effect.equals(AmmoCube.Effect.ALT))
            return false;
        //TODO CHIEDI COME PAGARE
        //payOption= ...
        Action.PayOption payOption=null;

        if(payOption.equals(Action.PayOption.AMMOPOWER))
        {
            //TODO CHIEDI POWERUP
            for (PowerUpCard p:player.getPowerUp()
                 ) {
                p.toString();
                //TODO SCEGLI Y/N
                String response="";
                if(response=="YES"){
                    powers.add(p);
                }
            }

        }

        if(action.canPayCard(weaponCard,player,payOption,effect,powers))
        {
            paidEffect.add(action.paidEffect(weaponCard,player,payOption,effect,powers,number));
            if(effect.equals(AmmoCube.Effect.BASE))
                weaponCard.setReload();
            if(effect.equals(AmmoCube.Effect.ALT))
                weaponCard.setReloadAlt(true);
            player.getPowerUp().removeAll(powers);
            model.powerUpDeck.getUsedPowerUp().addAll(powers);
        }
        else {
            //TODO MANDA MESS ERRORE
            action.run(player,positionBeforeShoot);
            return false; // delete action plus send message
        }

        return true;
    }

    public boolean grabFromSpawnpoint(CoordinatesWithRoom chosenCell, Player player){

        /*
         * controllo io se può pagarla + ricarica ma tu devi:
         * 0-controllare che ti dica o effetto base o effetto alt nient'altro altrimenti non funziona il metodo x
         * 1-controllare che abbia meno di 3 carte in mano x
         * 2-una volta raccolta devi rimuoverla dalle carte dello spawnpoint
         * 3-una volta finito cio setta la posizione del player nel punto in cui ha scelto di raccogliere
         * se scarta una carta rimettila nel deck x
         * */

        Spawnpoint s = chosenCell.getSpawnpoint(model);
        //  TODO se spawn fai scegliere quale e mettila in card
        //send card
        for (WeaponCard w: s.getWeaponCards()) {
            //TODO MANDA AL CLIENT TO STRING
                w.toString();
        }
        //TODO SALVA LA SCELTA
        WeaponCard weaponCard=null;

        LinkedList<PowerUpCard>playerPowerUpCards=new LinkedList<>();

        //TODO CHIEDI EFFETTO BASE/ALT

        AmmoCube.Effect effect=null;

        //TODO CHIEDI METODO PAGAMENTO
        Action.PayOption payOption=null;  //payOption= Action.PayOption.AMMO OR   payOption= Action.PayOption.AMMOPOWER
        if(payOption.equals(Action.PayOption.AMMOPOWER)){
            playerPowerUpCards=payWithThesePowerUps(player);
        }
        //TODO CONTROLLA SE PUO' PAGARE L'EFFETTO BASE O ALT SE NO ESCI E ANNULLA AZIONE
        if(!action.canPayCard(weaponCard,player,payOption,effect,playerPowerUpCards)){
            //TODO MANDA MESSAGGIO
            return false;}

        // TODO CONTROLLA SE PUO RACCOGLIERE ALTRIMENTI RICHIEDI DROP ARMA, METTILA IN DCARD
        if(!player.canGrabWeapon()){
            for (WeaponCard w:player.getHand()
                 ) {
                w.toString();
                String response="";
                //TODO CHIEDI SE VUOLE SCARTARE QUESTA
                if(response=="YES")
                    {
                        player.getHand().remove(w);
                        model.weaponDeck.getUsedWeaponCard().add(w);
                        break;
                    }
            }

        }

        //TODO DOVRAI FARTI DARE UN NUMERO DALLE CARTE PER EFFECT&NUMBER
        int number=0;
        action.payAmmo(player,weaponCard,effect,number);

        player.getHand().add(weaponCard);
        s.getWeaponCards().remove(weaponCard);
        player.getPowerUp().removeAll(playerPowerUpCards);
        model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);
        action.run(player,weaponCard.getCoordinatesOnMap());

        //TODO INCREMENTA TURNO



        return true;
    }




    public LinkedList<PowerUpCard> payWithThesePowerUps(Player player){
        LinkedList<PowerUpCard> chosenPower=new LinkedList<>();
        //TODO MANDA CARTE POWER UP IN MANO
        for (PowerUpCard powerUp:player.getPowerUp()) {
            powerUp.toString();
            //TODO CHIEDI SE VUOI UTILIZZARE QUESTO
            String response="";
            if(response=="YES"){
                chosenPower.add(powerUp);
            }
        }
        return chosenPower;
    }




    public List<Object> requestsForEveryWeapon(EffectAndNumber e, WeaponCard w, Player p, GameBoard g, GameModel model, List<Object> pastTargets){
        CoordinatesWithRoom playerPosition = p.getCoordinatesWithRooms();
        List<CoordinatesWithRoom> cells;
        List<Object> targets;
        switch (w.toString()){

            case "Cyberblade":
                if(e.getEffect()== AmmoCube.Effect.BASE || e.getEffect()== AmmoCube.Effect.OP2){
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS

                    // TODO CHECK TARGETS OP1 AND BASE DIFFERENTI (RITORNA IL GIOCATORE COLPITO)
                    // SE PASTTARGETS VUOTO OK, SE PIENO TARGET SCELTO DEVE ESSERE DIVERSO DA QUELLO

                    w.applyDamage(targets,p,e);
                }
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    // TODO MOVE 1 SQUARE
                }
                break;

            case "Electroscythe":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                w.applyDamage(targets,p,e);
                break;

            case "Flamethrower":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                // TODO ASK PLAYER TO CHOSE ONE OR TWO SQUARES (CHECK FIRST DISTANT 1, SECOND DISTANT 2, SAME DIR)

                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                if(e.getEffect()== AmmoCube.Effect.BASE){
                    // TODO ASK 1 TARGET PER OGNI SQUARE (2 FORSE)
                }
                w.applyDamage(targets,p,e);
                break;

            case "Furnace":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    LinkedList<Room> possibleRooms = new LinkedList<>();

                    for (int k = 0; k < g.getDoors().size(); k++) {
                        if (playerPosition.getX() == g.getDoors().get(k).getCoordinates1().getX() &&
                                playerPosition.getY() == g.getDoors().get(k).getCoordinates1().getY() &&
                                playerPosition.getRoom().getToken() == g.getDoors().get(k).getCoordinates1().getRoom().getToken()) {

                            possibleRooms.add(g.getDoors().get(k).getCoordinates2().getRoom());
                        }

                        if (playerPosition.getX() == g.getDoors().get(k).getCoordinates2().getX() &&
                                playerPosition.getY() == g.getDoors().get(k).getCoordinates2().getY() &&
                                playerPosition.getRoom().getToken() == g.getDoors().get(k).getCoordinates2().getRoom().getToken()) {

                            possibleRooms.add(g.getDoors().get(k).getCoordinates1().getRoom());

                        }
                    }

                    // TODO ASK PLAYER FOR A ROOM (DIFFERENT) - METTILA NELLA COORDINATA ROOM
                    // SEND PLAYER LIST OF ROOMS possibleRooms APPENA CREATA
                    CoordinatesWithRoom room = new CoordinatesWithRoom();

                    cells = w.getPossibleTargetCells(room, e, g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);

                    // TODO ASK PLAYER WHICH TILE, GET ONE BACK IN THAT LIST

                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                }
                break;

            case "GrenadeLauncher":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                    // TODO ASK QUALE TARGET COLPIRE (1)
                    w.applyDamage(targets, p, e);
                    // TODO ASK SE VUOLE MUOVERE IL TARGET DI 1
                }
                if(e.getEffect()== AmmoCube.Effect.OP1) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    // TODO ASK UNA CELLA ANCHE LA TUA
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                }
                break;

            case "Heatseeker":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                // TODO ASK 1 TARGET
                w.applyDamage(targets,p,e);
                break;
            case "Hellion":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                // TODO ASK 1 TARGET - SAVE IT FOR LATER
                Object target = new Object();

                CoordinatesWithRoom c = ((Player)target).getCoordinatesWithRooms(); // COORD DEL TARGET
                List<CoordinatesWithRoom> newList = new LinkedList<>();
                newList.add(c); // IT HAS JUST COORD TARGET
                targets = w.fromCellsToTargets(newList,playerPosition,g,p,model,e);

                for(Object o : targets){
                    if(o instanceof Player && ((Player) o).getColor()==((Player) target).getColor()){
                        targets.remove(o);
                        break;
                    }
                }
                targets.add(0,target);  // TARGET HAS TO BE IN FRONT

                w.applyDamage(targets,p,e);
                break;

            case "LockRifle":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                // TODO ASK 1 TARGET TO DAMAGE

                // TODO CHECK TARGETS OP1 AND BASE DIFFERENTI (RITORNA IL GIOCATORE COLPITO COSì LO SALVI IN SHOOT)
                // SE PASTTARGETS VUOTO OK, SE PIENO TARGET SCELTO DEVE ESSERE DIVERSO DA QUELLO

                w.applyDamage(targets,p,e);
                break;

            case "MachineGun":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                // TODO ASK TO CHOOSE 1 OR 2 TARGETS
                // TODO CHECK TARGETS OP1 AND OP2 DIFFERENTI
                // SE PASTTARGETS VUOTO OK, SE PIENO TARGET SCELTO DEVE ESSERE DIVERSO DA QUELLO
                // TODO ASK TO SHOOT THE OTHER OR AND SOMEONE ELSE
                w.applyDamage(targets,p,e);
                break;

            case "PlasmaGun":
                if(e.getEffect()== AmmoCube.Effect.BASE || e.getEffect()== AmmoCube.Effect.OP2) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO ASK 1 TARGET (SAVE IT FOR THE OTHER EFFECT)
                    w.applyDamage(targets,p,e);
                }
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    // TODO ASK WHERE TO MOVE
                    // MOVE
                }
                break;

            case "PowerGlove":
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO ASK 1 TARGET
                    w.applyDamage(targets,p,e);

                    CoordinatesWithRoom c0 = playerPosition; // SAVED PLAYER'S POSITION
                    // TODO MOVE PLAYER TO TARGET'S SQUARE

                // ALSO
                if(e.getEffect()== AmmoCube.Effect.ALT){
                    CoordinatesWithRoom c2 = c0.getNextCell(c0,playerPosition,g,false);
                    if(c2.getX()!=0){
                        // TODO ASK IF PLAYER WANTS TO MOVE THERE

                        // TO BE CONTINUED...
                        // POSSO MUOVERE SOLAMENTE IN ALT O DEVO PER FORZA COLPIRE GLI AVVERSARI NELLA MOSSA?????????
                    }
                }
                break;

            case "Railgun":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                // TODO ASK TO CHOOSE 1 (BASE) OR 1-2 (ALT) TARGETS, REMOVE OTHERS

                if(e.getEffect()== AmmoCube.Effect.ALT && targets.size()==2) {

                    // 2 TARGETS OF ALT EFFECT HAVE TO BE IN THE SAME DIRECTION!!!!!
                    // TODO ASK AGAIN IF THIS IS FALSE
                    playerPosition.checkSameDirection(((Player)targets.get(0)).getCoordinatesWithRooms(),((Player)targets.get(1)).getCoordinatesWithRooms(),10,g,false);
                }
                w.applyDamage(targets,p,e);
                break;

            case "RocketLauncher":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO ASK 1 TARGET (SAVE IT - POSITION - FOR OP2 EFFECT)
                    w.applyDamage(targets,p,e);
                    // TODO ASK IF MOVE TARGET

                    // PRIMA BASE- SE NON VIENE PASSATO NULLA RITORNA IL GIOCATORE COLPITO
                    // PRIMA OP2- SE GLI PASSANO UN GIOCATORE ALLORA PRENDI LA SUA POS, METTILA IN CELLS TO TARGETS, FAI SCEGLIERE UN TARGET E COLPISCILO E MUOVI SE VUOI
                }
                if(e.getEffect()== AmmoCube.Effect.OP2) {
                    // PRIMA BASE- PRENDI POSIZIONE VECCHIA DI TARGET (PASSATA COME GIOCATORE)
                    // PRIMA OP2- (NULLA PASSATO) TODO SCEGLI UNA CELLA, COLPISCILI E POI RITORNA LA CELLA (COME GIOCATORE)
                }
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    // TODO ASK WHERE TO MOVE
                    // MOVE
                }
                break;

            case "Shockwave":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);

                    // TODO ASK WHICH TARGETS TO DAMAGE - UP TO 3
                    boolean ok = false;
                    while (!ok) {
                        if ((targets.size() == 3 &&
                                !((Player) targets.get(0)).getCoordinatesWithRooms().equals(((Player) targets.get(1)).getCoordinatesWithRooms())
                                && !((Player) targets.get(0)).getCoordinatesWithRooms().equals(((Player) targets.get(2)).getCoordinatesWithRooms())
                                && !((Player) targets.get(1)).getCoordinatesWithRooms().equals(((Player) targets.get(2)).getCoordinatesWithRooms())) ||
                                (targets.size() == 2 &&
                                        !((Player) targets.get(0)).getCoordinatesWithRooms().equals(((Player) targets.get(1)).getCoordinatesWithRooms())
                                )) {
                            ok = true;
                        } else {
                            // TODO ASK AGAIN
                        }
                    }
                    w.applyDamage(targets, p, e);
                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                }
                break;

            case "Shotgun":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO CHOOSE 1 TARGET
                    w.applyDamage(targets,p,e);
                    // TODO ASK TO MOVE THAT TARGET 1 SQUARE
                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO CHOOSE 1 TARGET
                    w.applyDamage(targets,p,e);
                }
                break;

            case "Sledgehammer":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                // TODO CHOOSE 1 TARGET
                w.applyDamage(targets,p,e);

                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    // TODO ASK TO MOVE THAT TARGET 0-1-2 SQUARE SAME DIRECTION
                    // FROM LIST
                    List<CoordinatesWithRoom> possibleCells = playerPosition.tilesSameDirection(2,g,false);
                }
                break;

            case "Thor":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                    // TODO ASK 1 TARGET
                    w.applyDamage(targets, p, e);
                    // RETURN THE TARGET (YOU'LL NEED TO CHECK THEY ARE DIFFERENT)
                }
                if(e.getEffect()== AmmoCube.Effect.OP1 || e.getEffect()== AmmoCube.Effect.OP2) {
                    // PUT HERE TARGET'S POSITION, GLIEL'HA PASSATA
                    CoordinatesWithRoom targetPos = new CoordinatesWithRoom();

                    cells = w.getPossibleTargetCells(targetPos, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                    // TODO ASK 1 TARGET
                    w.applyDamage(targets, p, e);
                    // RETURN THE TARGET (YOU'LL NEED TO CHECK THEY ARE DIFFERENT)
                }
                break;

            case "TractorBeam":
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO ASK 1 TARGET
                    CoordinatesWithRoom c1 = new CoordinatesWithRoom(); // SALVACI LA POS DEL TARGET

                    // TODO ASK NUOVA POSIZIONE (QUESTA VOLTA NON PROPONGO NULLA)

                    // CHECK CELLS.CONTAINS(NEWPOS) &&
                    // CHECK possibleMoves.CONTAINS(NEWPOS)
                    // SE VA BENE MUOVI IL TARGET E APPLYDAMAGE, ALTRIMENTI CHIEDI ANCORA

                    List<CoordinatesWithRoom> possibleMoves;
                    possibleMoves = c1.oneTileDistant(g, false);
                    possibleMoves.addAll(c1.xTilesDistant(g, 2));
                    possibleMoves.add(c1);


                    w.applyDamage(targets,p,e);

                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    // TODO CHOOSE 1 TARGET AND MOVE IT TO YOUR SQUARE
                    w.applyDamage(targets,p,e);
                }
                break;

            case "VortexCannon":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                // TODO CHOOSE A VORTEX

                CoordinatesWithRoom vortex = new CoordinatesWithRoom();
                List<CoordinatesWithRoom> list;
                list = vortex.oneTileDistant(g,false);
                list.add(vortex);

                targets = w.fromCellsToTargets(list,playerPosition,g,p,model,e);
                // TODO ASK FOR 1 TARGET IF BASE OR UP TO 2 IF OP1
                // CHECK DIFFERENTI DA QUELLI PASSATI
                // MOVE EVERY TARGET ON THE VORTEX

                w.applyDamage(targets,p,e);
                // RETURN THE TARGETS
                break;

            case "Whisper":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                // TODO CHOOSE 1 TARGET
                w.applyDamage(targets,p,e);
                break;

            case "Zx_2":
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                // TODO ASK FOR 1 TARGET IF BASE OR UP TO 3 IF ALT
                w.applyDamage(targets,p,e);
                break;
        }
        return Collections.emptyList(); // SE NON DIVERSAMENTE SPECIFICATO
    }




    } // REQUEST HANDLER



    // TIMER
    private static class Countdown{
        public Countdown(){
            final Timer timer = new Timer();
            try {
                timer.scheduleAtFixedRate(new TimerTask() {
                    int i = time;

                    public void run() {
                        System.out.println(i--);
                        if (i< 0 || connectionsCount<3 || connectionsCount==5 && colorsChosen.size()==5) {
                            if(i<0 || connectionsCount==5 && colorsChosen.size()==5) {
                                System.out.println("Game is starting...");

                                gameIsOn = true;
                                //Server.startGame();
                                // DO SOMETHING TO START THE GAME
                            }
                            else if(connectionsCount<3){
                                System.out.println("TIMER STOPPED: LESS THAN 3 CONNECTIONS");
                            }
                            timer.cancel();
                        }
                    }
                }, 0, 1000);

            } catch (Exception e) {
                //
            }

        }
    }





    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static void setBoardChosen(int i){
        boardChosen = i;
    }

    public static void createBoard(){
        model = new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,boardChosen);
        action = new Action(model);
        model.populateMap(model.getMapUsed());
    }






}


