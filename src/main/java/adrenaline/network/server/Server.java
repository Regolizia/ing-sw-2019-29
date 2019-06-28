package adrenaline.network.server;

// SOCKET
import adrenaline.*;
import adrenaline.gameboard.GameBoard;
import adrenaline.powerups.Newton;
import adrenaline.powerups.Teleporter;
import adrenaline.weapons.MachineGun;
import adrenaline.weapons.Thor;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// RMI

public class Server {

    private static GameModel model;
    private static Action action;
    private static BotAction botAction;
    private static FreneticAction freneticAction;
    private static int currentPlayer = 0;
    private static boolean firstTurn = true;

    private static int time = 0;
    private static int connectionsCount = 0;
    private static int boardChosen = 42;
    private static boolean gameIsOn = false;
    private static boolean endgame = false;
    private static List<CoordinatesWithRoom> cellsWithoutTiles = new LinkedList<>();
    private static List<String> disconnected = new LinkedList<>();
    private static HashMap<String,Figure.PlayerColor> disconnectedColors = new HashMap<>();

    // STRING LIST OF THE COLORS A PLAYER CAN CHOOSE AND LIST OF THOSE ALREADY CHOSEN
    private static List<String> possibleColors = Stream.of(Figure.PlayerColor.values())
            .map(Figure.PlayerColor::name)
            .collect(Collectors.toList());
    private static ArrayList<String> colorsChosen = new ArrayList<>();

    // All client names, so we can check for duplicates upon registration.
    private static ArrayList<String> names = new ArrayList<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static HashMap<String,ObjectOutputStream> writers = new HashMap<String,ObjectOutputStream>();


    private static ServerSocket serverSocket;
    private Server server;
    private ExecutorService threadPool;

    static ReentrantLock lock = new ReentrantLock();;



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
    public static synchronized boolean isFirstTurn(){
        return firstTurn;
    }
    public static void endFirstTurn(){
        firstTurn= false;
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
                                handleTurns();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void disconnect(){
            try {

                sendToClient("DISCONNECTED");
                if(!disconnected.contains(nickname)) {
                    disconnected.add(nickname);
                }
                if(!disconnectedColors.containsKey(nickname)) {
                    disconnectedColors.put(nickname, color);
                }
                writers.remove(writers.get(nickname));
                for(String s : disconnected){
                    System.out.println(s+ " DISCONNECTED LIST");
                }

                System.out.println("DISCONNECTED CONTAINS NAME "+disconnected.contains(nickname));
                System.out.println("DISCONNECTEDCOLORS CONTAINS NAME "+disconnectedColors.containsKey(nickname));
                socket.close();
                removeOneConnection();
                System.out.println("Client Disconnected++++ HERE -1");

                if(lock.isHeldByCurrentThread()) {
                    while (lock.getHoldCount() > 0) {
                        lock.unlock();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendToClient(String message){
                try {
                    outputStream.writeObject(message);
                    outputStream.flush();
                } catch (IOException e) {
                    // DISCONNECTED
                }

        }
        public void sendListToClient(List<String> messages) throws IOException {
                outputStream.writeObject(messages);
                outputStream.flush();
        }

        public static void countConnections(){

            Server.connectionsCount++;
        }
        public static void removeOneConnection(){

            Server.connectionsCount--;
        }


        public void clientLogin(){
            try {
                if(!isGameOn()) {
                    while (true) {
                        nickname = loginName();
                        if (!disconnected.contains(nickname)) {
                            countConnections();
                            System.out.println(connectionsCount +" connections +++ HERE +1");

                            if(isGameOn()){
                                sendToClient("MESSAGE");
                                sendToClient("Sorry, the game has already started without you");
                                socket.close();
                            }

                            color = checkColor();
                            chooseBoard(nickname); // it checks if is firstPlayer and asks board

                            sendToClient("ACCEPTED");

                            if (connectionsCount == 3) {
                                Countdown c = new Countdown();
                            }
                            addPlayerToGame(nickname, color);
                            writers.put(nickname, outputStream);

                            break;
                        }
                        if (disconnected.contains(nickname)) {
                            // FALLO GIOCARE, WELCOME BACK
                            sendToClient("MESSAGE");
                            sendToClient("Welcome back!");

                            countConnections();
                            System.out.println(connectionsCount +" connections /////HERE +1");

                            if (disconnectedColors.get(nickname) == Figure.PlayerColor.NONE || disconnectedColors.get(nickname)==null) {
                                color = checkColor();
                            }

                            chooseBoard(nickname); // it checks if is firstPlayer and asks board

                            sendToClient("ACCEPTED");

                            if (connectionsCount == 3) {
                                Countdown c = new Countdown();
                            }
                            addPlayerToGame(nickname, color);
                            reconnect();
                            break;

                        }
                    }

                    //disconnect(); TO TRY TO DISCONNECT

                }else if(disconnected.isEmpty() && isGameOn()){
                    sendToClient("MESSAGE");
                    sendToClient("Sorry, the game has already started without you");
                    socket.close();

                }else if(!disconnected.isEmpty() && isGameOn()){
                    nickname = loginName();

                    if (disconnected.contains(nickname)) {
                        // FALLO GIOCARE, WELCOME BACK
                        sendToClient("MESSAGE");
                        sendToClient("Welcome back!");

                        countConnections();
                        System.out.println(connectionsCount + " connections *****HERE +1");

                        disconnected.remove(nickname);
                        disconnectedColors.remove(nickname);
                    }else {

                        sendToClient("MESSAGE");
                        sendToClient("Sorry, the game has already started without you");
                        socket.close();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();/*
                try {
                    sendToClient("DISCONNECTED");
                    disconnected.add(nickname);
                    disconnectedColors.put(nickname,color);
                    writers.remove(writers.get(nickname));
                    System.out.println("Client Disconnected");
                    socket.close();
                    if(nickname!=null) {
                        removeOneConnection();
                        System.out.println(nickname+" REMOVED ");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }*/
            }

        }
        public void reconnect(){
            try {
                writers.put(nickname, outputStream);

                if (disconnected.contains(nickname)) {
                    disconnected.remove(nickname);
                    System.out.println("REMOVED 1");
                }
                if (disconnectedColors.containsKey(nickname)) {
                    disconnectedColors.remove(nickname);
                    System.out.println("REMOVED 2");
                }
            }catch (Exception e){
                System.out.println("DIDN'T REMOVE FROM DISCONNECT");
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
                        } else if (names.contains(name) && !disconnected.contains(name)) {
                            System.out.println("names contains name "+names.contains(name));
                            System.out.println("disconnected contains name "+ disconnected.contains(name));
                            for(String s : disconnected){
                                System.out.println(s+ " DISCONNECTED LIST");
                            }

                            sendToClient("LOGIN");
                            name = (String)inputStream.readObject();
                        } else if(disconnected.contains(name)){
                            return name;
                        }
                    }
                }
            }
        }

        public Figure.PlayerColor checkColor(){
        try {
            while (true) {
                String csv = String.join(", ", possibleColors);  // SEND POSSIBLE COLORS
                sendToClient("COLOR");
                sendToClient(csv);
                String color = (String) inputStream.readObject();
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
        }catch (Exception e){
            System.out.println("--Color disconnection");
        }
        return Figure.PlayerColor.NONE;
        }

        public void addPlayerToGame(String name, Figure.PlayerColor color){
           try {
               Server.model.addPlayer(new Player(name, color));
               System.out.println(lock.isHeldByCurrentThread() + " held by " + nickname);
               System.out.println(lock.isLocked() + " is locked " + nickname);
               System.out.println(lock.getHoldCount() + " count " + nickname);

               if (lock.isHeldByCurrentThread() && lock.getHoldCount() == 1)
                   lock.unlock();

               broadcast(name + " has joined");
           }catch (Exception e){
               System.out.println("Couldn't add player to game");
           }
        }

        public void broadcast(String s){
            lock.lock();
            for (Map.Entry me : writers.entrySet()) {
                try {
                    ((ObjectOutputStream)me.getValue()).writeObject("MESSAGE");
                    ((ObjectOutputStream)me.getValue()).flush();

                    ((ObjectOutputStream)me.getValue()).writeObject(s);
                    ((ObjectOutputStream)me.getValue()).flush();
                } catch (IOException e) {
                    //e.printStackTrace();
                    // DISCONNECTED
                }
            }
            lock.unlock();
        }

        public void chooseBoard(String name){
            lock.lock();
            try {
            if(boardChosen==42){

                while(boardChosen==42){
                    int result = 0;
                    sendToClient("BOARD");
                    result = (int)inputStream.readObject();
                    if (result == 1 || result == 2 || result == 3 || result == 4) {
                        if(boardChosen!=42) return;

                        Server.setBoardChosen(result);
                        System.out.println("BOARD CHOSEN " + result);
                        Server.createBoard();
                        printWeaponSpawnpoints();
                        return;
                    } else {
                        //  ASK AGAIN BECAUSE NOT ACCEPTED
                    }

                }

                lock.unlock();
            }
            }catch (Exception e){
                lock.unlock();
                disconnect();
                System.out.println("--Board disconnection");
            }
        }
        ////


        public void printWeaponSpawnpoints(){
            Spawnpoint s = getSpawnpoint(AmmoCube.CubeColor.BLUE);
            for (WeaponCard w : s.getWeaponCards()){
                System.out.println(w.toString());
            }
            s = getSpawnpoint(AmmoCube.CubeColor.RED);
            for (WeaponCard w : s.getWeaponCards()){
                System.out.println(w.toString());
            }
            s = getSpawnpoint(AmmoCube.CubeColor.YELLOW);
            for (WeaponCard w : s.getWeaponCards()){
                System.out.println(w.toString());
            }
        }

        public void tagbackGrenade() {
            // CAN SEE SHOOTER
            Player me = fromNameToPlayer(nickname);
            if(me.hasTagbackGrenade()) {
                Player shooter = fromNameToPlayer(me.getShooter());
                WeaponCard w = new WeaponCard();
                EffectAndNumber en = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
                List<CoordinatesWithRoom> list = w.getPossibleTargetCells(me.getCoordinatesWithRooms(), en, model.getMapUsed().getGameBoard());
                List<Object> t = w.fromCellsToTargets(list, me.getCoordinatesWithRooms(), model.getMapUsed().getGameBoard(), me, model, en);
                for (Object o : t) {
                    if ((Player) o == shooter) {
                        try {// ASK IF WANT TO USE
                            lock.lock();
                            sendToClient("TAGBACKGRENADE");
                            String response = (String) inputStream.readObject();
                            lock.unlock();
                            if (response.toUpperCase().equals("Y")) {
                                // ADD MARK
                                shooter.addMarks(me, 1);
                                // DISCARD CARD
                                model.powerUpDeck.setUsedPowerUp(me.getTagbackGrenade());
                                me.getPowerUp().remove(me.getTagbackGrenade());

                                me.setDamagedStatus(false);
                                me.setShooter(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public boolean isCurrentPlayer(){
            synchronized (Server.model.getPlayers().get(currentPlayer)){
                 return Server.model.getPlayers().get(currentPlayer).getName().equals(nickname);
            }
        }
        public boolean hasBeenDamaged(){
            synchronized (Server.model.getPlayers()){
                 return fromNameToPlayer(nickname).damagedStatus();
            }
        }

        public Player fromNameToPlayer(String s){
            for (Player p : model.getPlayers()){
                if(p.getName().equals(s)){
                    return p;
                }
            }
            return new Player();
        }

        public boolean isGameOn(){
            synchronized (this){
                 return gameIsOn;
            }
        }

        public boolean isEndgame(){
            synchronized (this){
                 return endgame;
            }
        }

        public void handleTurns(){
            int numberOfActions =0;
            while(!isEndgame()) {
                if (isGameOn()) {

                    if (isCurrentPlayer()) {
                        try {
                            if (Server.isFirstTurn()) {
                                if(currentPlayer==0) {

                                    broadcast("\nThe board is number " + boardChosen);
                                }
                                lock.lock();
                                sendToClient("YOURFIRSTTURN");
                                System.out.println("FIRST TURN");
                                sendForBoardSetup();
                                firstTurn();
                                lock.unlock();
                                numberOfActions = 0;
                                System.out.println("CURRENT PLAYER " + currentPlayer);
                            }
                            if (numberOfActions != 2 && !firstTurn) {
                                lock.lock();
                                sendToClient("YOURTURN");

                                String choice = (String) inputStream.readObject();
                                lock.unlock();
                                System.out.println("\n"+choice);
                                switch (choice) {
                                    case "G":
                                        lock.lock();
                                        sendToClient("GRAB");
                                        grab();
                                        lock.unlock();
                                        numberOfActions++;
                                        break;
                                    case "R":
                                        lock.lock();
                                        sendToClient("RUN");
                                        playerRun();
                                        lock.unlock();
                                        numberOfActions++;
                                        break;
                                    case "M":
                                        lock.lock();
                                        sendToClient("MAP");
                                        sendForBoardSetup();
                                        lock.unlock();
                                        break;
                                    case "B":
                                        lock.lock();
                                        sendToClient("BOARDS");
                                        playerBoards();
                                        lock.unlock();
                                        break;
                                    case "P":
                                        powerup();
                                        break;
                                    case "S":
                                    default:
                                        shoot();
                                        numberOfActions++;
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            try {lock.lock();
                                sendToClient("DISCONNECTED");
                                lock.unlock();
                                disconnected.add(nickname);
                                disconnectedColors.put(nickname,color);
                                writers.remove(writers.get(nickname));
                                System.out.println("Client Disconnected");
                                socket.close();
                                if(nickname!=null) {
                                    removeOneConnection();
                                    System.out.println(nickname+" REMOVED HERE -1");
                                }
                                if(lock.isHeldByCurrentThread()) {
                                    while (lock.getHoldCount() > 0) {
                                        lock.unlock();
                                    }

                                    replaceAmmo();
                                    replaceWeapons();
                                    nextPlayer();
                                    numberOfActions = 0;
                                    System.out.println("CURRENT PLAYER " + currentPlayer);

                                }
                                break;
                            } catch (IOException ex) {
                                //ex.printStackTrace();
                            }
                        }
                        // END OF TURN
                        if (numberOfActions == 2 && !firstTurn) {

                            // CAN USE SOME POWERUPS BEFORE END OF TURN
                            powerup();

                            reload();
                            //scoring();
                            replaceAmmo();
                            replaceWeapons();

                            nextPlayer();
                            //broadcast(nickname +" ended his turn. Now is the turn of "+model.getPlayers().get(currentPlayer));
                            numberOfActions = 0;
                            System.out.println("CURRENT PLAYER " + currentPlayer);


                            System.out.println("\n Thread info: ");
                            System.out.println(lock.isHeldByCurrentThread()+ " " + nickname);
                            System.out.println(lock.isLocked() + " " + nickname);
                            System.out.println(lock.getHoldCount() + " " + nickname);

                            if(lock.isHeldByCurrentThread()&& lock.getHoldCount()==1)
                                lock.unlock();


                        }
                        if (Server.isFirstTurn()){
                            if (currentPlayer == model.getPlayers().size() - 1) {
                                Server.endFirstTurn();
                            }
                            nextPlayer();
                            //broadcast(nickname +" ended his turn. Now is the turn of "+model.getPlayers().get(currentPlayer));
                            numberOfActions = 0;
                            System.out.println("CURRENT PLAYER " + currentPlayer);

                            System.out.println("\n Thread info: ");
                            System.out.println(lock.isHeldByCurrentThread()+ " " + nickname);
                            System.out.println(lock.isLocked() + " " + nickname);
                            System.out.println(lock.getHoldCount() + " " + nickname);

                            int c = lock.getHoldCount();
                            if(lock.isHeldByCurrentThread()){
                                while (c>1){
                                    lock.unlock();
                                }
                            }

                        }
                    }
                    else if(hasBeenDamaged()){  //TODO SET DAMAGED AND SHOOTER WHEN YOU SHOOT
                        tagbackGrenade();
                    }
                    // TODO !Server.action.endOfTheGame(model.getMapUsed().getGameBoard()))
                    // SET endgame parameter in this class
                }
            }
        }

        public void mapInfo(){
            sendToClient(Integer.toString(boardChosen));
        }

        public void handleException() {
            lock.lock();
            sendToClient("DISCONNECTED");
            lock.unlock();
            disconnected.add(nickname);
            disconnectedColors.put(nickname, color);
            writers.remove(writers.get(nickname));
            System.out.println("Client Disconnected");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nickname != null) {
                removeOneConnection();
                System.out.println(nickname + " REMOVED ++++HERE -1");
            }
            if(lock.isHeldByCurrentThread()) {
                while (lock.getHoldCount() > 0) {
                    lock.unlock();
                }
            }
        }

        public void sendForBoardSetup(){
            sendToClient(Integer.toString(boardChosen));
            try {
                sendListToClient(colorsChosen);
                sendListToClient(names);
                sendSpawnpointWeaponsBlue();
                sendSpawnpointWeaponsRed();
                sendSpawnpointWeaponsYellow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void replaceWeapons(){
            System.out.println("Replacing weapons...");
            int weaponNum =getSpawnpoint(AmmoCube.CubeColor.BLUE).getWeaponCards().size();
            while (weaponNum<3){
                getSpawnpoint(AmmoCube.CubeColor.BLUE).getWeaponCards().add(model.weaponDeck.pickUpWeapon());
                weaponNum++;
            }
            weaponNum =getSpawnpoint(AmmoCube.CubeColor.RED).getWeaponCards().size();
            while (weaponNum<3){
                getSpawnpoint(AmmoCube.CubeColor.RED).getWeaponCards().add(model.weaponDeck.pickUpWeapon());
                weaponNum++;
            }
            weaponNum =getSpawnpoint(AmmoCube.CubeColor.YELLOW).getWeaponCards().size();
            while (weaponNum<3){
                getSpawnpoint(AmmoCube.CubeColor.YELLOW).getWeaponCards().add(model.weaponDeck.pickUpWeapon());
                weaponNum++;
            }
        }
        public void replaceAmmo(){
            System.out.println("Replacing ammo...");
        for(CoordinatesWithRoom c : cellsWithoutTiles){
            c.getRoom().addAmmoTile(model.ammoTileDeck.pickUpAmmoTile(),c.getX(),c.getY());
        }
        cellsWithoutTiles.clear();
        }

        public void respawn(Player p){
            p.getPowerUp().add(model.powerUpDeck.pickPowerUp());
            List<String> toSend = fromPowerupsToNames(p.getPowerUp());
            try {lock.lock();
                sendToClient("RESPAWN");
                sendListToClient(toSend);
                int x = (int) inputStream.readObject();
                x--;
                lock.unlock();
                PowerUpCard po = p.getPowerUp().remove(x);
                    System.out.println("TO USE AS POSITION " + po.toString());

                    p.newLife();
                    setInitialPosition(po.getPowerUpColor(), p);

            }catch (Exception e){
               handleException();
            }
        }

        public void firstTurn(){
            lock.lock();
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
            lock.unlock();
        }

        /**
         * Updates index of next Player.
         * If everybody has played, it resets.
         */
        public static void nextPlayer(){
            if(currentPlayer!=model.getPlayers().size()-1) {currentPlayer++;}
            else{
                currentPlayer = 0;
            }
            if(disconnected.contains(model.getPlayers().get(currentPlayer).getName())){
                nextPlayer();
            }
        }

        public void setInitialPosition(AmmoCube.CubeColor c, Player p){
            for (Room r: model.getMapUsed().getGameBoard().getRooms()){

                if(!r.getSpawnpoints().isEmpty() && r.getSpawnpoints().get(0).getColor().equals(c)){
                   CoordinatesWithRoom c1 = new CoordinatesWithRoom(r.getSpawnpoints().get(0).getSpawnpointX(),r.getSpawnpoints().get(0).getSpawnpointY(),r);
                   p.setPlayerPosition(c1);
                    p.setPlayerPositionSpawnpoint(c1);
                    broadcast("\n" + p.getName() + " is in "+c1.toString());
                    System.out.println("INITIAL POSITION OF "+p.getName()+" IS  "+p.getCoordinatesWithRooms().toString());
                    break;
                }
            }
        }

        public void scoring(){
            List<Player> victims = new LinkedList<>();
            for(Player p : model.getPlayers()){
                if(p.isDead()){
                    p.hasDied();
                    victims.add(p);
                }
            }
            for(Player p : victims){

                respawn(p);
            }

            action.canGetPoints(victims,model.getPlayers());     // TODO|| GIULIA CONTROLLA CHE TI VADA BENE COME CHIAMATA
                                                                 // TODO|| private boolean[] pointsArray;// HOW MANY TIMES PLAYER DIED, LO USI? lo uso in give points per vedere
            //                                                                // quanti max punti accettabili
            System.out.println("Scoring");
            for(Player p : model.getPlayers()){
                System.out.println(p.getPoints() + " points of "+ nickname);
            }
        }

        public void reload(){
            Player player = model.getPlayers().get(currentPlayer);
            try{
            lock.lock();
                List<WeaponCard> weapons = player.getHand();
                for (WeaponCard w : weapons){
                    if(w.getReload()){
                        weapons.remove(w);
                    }
                }
                for(WeaponCard w : weapons){
                    sendToClient("RELOAD");
                    sendListToClient(fromWeaponsToNames(weapons));
                    String response = (String) inputStream.readObject();

                    if(response.toUpperCase().equals("Y")){
                        LinkedList<PowerUpCard> playerPowerUpCards = new LinkedList<>();
                        sendToClient("PAYMENT");
                        int z = (int) inputStream.readObject();
                        lock.unlock();
                        Action.PayOption payOption;

                        if (z == 1) {
                            payOption = Action.PayOption.AMMO;
                        } else {
                            payOption = Action.PayOption.AMMOPOWER;
                        }

                        if (payOption.equals(Action.PayOption.AMMOPOWER)) {
                            playerPowerUpCards = payWithThesePowerUps(player);
                        }
                        if (!action.canPayCard(w, player, payOption, AmmoCube.Effect.BASE, playerPowerUpCards)) {
                            //MANDA MESSAGGIO
                            System.out.println("CAN'T PAY");
                        } else {
                            if(z==1){
                                action.payAmmo(player,w, AmmoCube.Effect.BASE,0);
                            }else{
                                action.payPowerUp(w,playerPowerUpCards,player, AmmoCube.Effect.BASE,0);
                                player.getPowerUp().removeAll(playerPowerUpCards);
                                model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);
                                playerPowerUpCards.clear();
                            }
                        }
                    }
                }
                lock.unlock();
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        public void powerup(){
            Player player = model.getPlayers().get(currentPlayer);
            if(!player.getPowerUp().isEmpty()){
                List<PowerUpCard> pows = new LinkedList<>();
                for(PowerUpCard p : player.getPowerUp()){
                    if(p.toString().equals("Teleporter, BLUE") || p.toString().equals("Teleporter, RED") ||
                       p.toString().equals("Teleporter, YELLOW") ||p.toString().equals("Newton, BLUE") ||
                       p.toString().equals("Newton, RED") ||p.toString().equals("Newton, YELLOW")){
                        pows.add(p);
                    }
                }lock.lock();
                if(pows.isEmpty()){
                    sendToClient("MESSAGE");
                    sendToClient("End of turn. Sorry you can't use any of your powerups now");
                }else{
                    sendToClient("POWERUP");
                    try {
                        List<String> toSend = fromPowerupsToNames(pows);
                        toSend.add(0, "No, I dont't want to use powerups");
                        sendListToClient(toSend);
                        int x = (int)inputStream.readObject();
                        x--;
                        if(x!=0) {
                            x--;
                            if (pows.get(x) instanceof Teleporter) {
                                teleporter();
                            }
                            if (pows.get(x) instanceof Newton) {
                                newton();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }lock.unlock();
            }
        }

        public void teleporter(){
            try {
            Player player = model.getPlayers().get(currentPlayer);
            List<CoordinatesWithRoom> possible = player.getTeleporter().getPossibleCells(model,player);
            lock.lock();
            sendToClient("CHOOSECELL");
            sendListToClient(fromCellsToNames(possible)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
            int x = (int)inputStream.readObject();
            x--;
            lock.unlock();
            player.setPlayerPosition(possible.get(x));
            broadcast("\n" + player.getName() + " teleported in "+possible.get(x).toString());

            PowerUpCard power = player.getTeleporter();
            player.getPowerUp().remove(power);
            model.powerUpDeck.usedPowerUp.add(power);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void newton(){
            try {
                Player player = model.getPlayers().get(currentPlayer);
                List<Object> targets = new LinkedList<>();
                targets.addAll(model.getPlayers());
                targets.remove(player);

                // ASK WHICH 1 TARGET TO MOVE
                lock.lock();
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int xh = (int)inputStream.readObject();
                xh--;
                lock.unlock();
                Object tt = targets.get(xh);
                targets.clear();
                targets.add(tt);
                CoordinatesWithRoom c = ((Player)targets.get(0)).getCoordinatesWithRooms();
                List<CoordinatesWithRoom> cells =c.tilesSameDirection(2,model.getMapUsed().getGameBoard(),false);
                lock.lock();
                sendToClient("CHOOSECELL");
                sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int x = (int)inputStream.readObject();
                x--;
                lock.unlock();
                ((Player)targets.get(0)).setPlayerPosition(cells.get(x));
                broadcast("\n" + ((Player) targets.get(0)).getName() + " moved to "+cells.get(x).toString());

                PowerUpCard power = player.getNewton();
                player.getPowerUp().remove(power);
                model.powerUpDeck.usedPowerUp.add(power);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Spawnpoint getSpawnpoint(AmmoCube.CubeColor c) {
            for (Room r : model.getMapUsed().getGameBoard().getRooms()) {
                if (!r.getSpawnpoints().isEmpty() && r.getSpawnpoints().get(0).getColor().equals(c)) {
                    return r.getSpawnpoints().get(0);
                }
            }
            return new Spawnpoint();
        }
        public CoordinatesWithRoom getSpawnpointCoordinates(Spawnpoint s) {
            for (Room r : model.getMapUsed().getGameBoard().getRooms()) {
                if (!r.getSpawnpoints().isEmpty() && r.getSpawnpoints().get(0).equals(s)) {
                    return new CoordinatesWithRoom(s.getSpawnpointX(),s.getSpawnpointY(),r);
                }
            }
            return new CoordinatesWithRoom();
        }

        public void sendSpawnpointWeaponsBlue(){
            try {
                List<String> weapons = new LinkedList<>();
                for(WeaponCard w : getSpawnpoint(AmmoCube.CubeColor.BLUE).getWeaponCards()){
                    weapons.add(w.toString());
                }

                sendListToClient(weapons);
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void sendSpawnpointWeaponsRed(){
            try {
                List<String> weapons = new LinkedList<>();
                for(WeaponCard w : getSpawnpoint(AmmoCube.CubeColor.RED).getWeaponCards()){
                    weapons.add(w.toString());
                }

                sendListToClient(weapons);
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void sendSpawnpointWeaponsYellow(){
            try {

                List<String> weapons = new LinkedList<>();
                for(WeaponCard w : getSpawnpoint(AmmoCube.CubeColor.YELLOW).getWeaponCards()){
                    weapons.add(w.toString());
                }
                sendListToClient(weapons);
                } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void playerBoards(){
            try {
                List<String> list = new LinkedList<>();
                sendListToClient(fromPlayersToNames(model.getPlayers()));
                for(Player p : model.getPlayers()){
                    for(Figure.PlayerColor c : p.getTrack()){
                        list.add(c.toString());
                    }
                }
                sendListToClient(list);
            }catch (Exception e){
                e.printStackTrace();
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

                broadcast("\n" + nickname + " moved to "+cells.get(x).toString());
                System.out.println("CURRENT POSITION " + player.getCoordinatesWithRooms().toString());
            } catch (Exception e) {
                //e.printStackTrace();
                disconnect();
            }
        }

    public void botShoot(Bot bot){
            LinkedList<CoordinatesWithRoom> possibleShoots= botAction.canSee(bot.getCoordinatesWithRooms(),bot);
          //TODO SEND POSSIBLESHOOT TO PLAYER
            //TODO RECIVED SHOOT TARGET
            CoordinatesWithRoom choosenCoordinate=new CoordinatesWithRoom();
        //TODO GET TARGET
             Player victim=new Player();
            botAction.botShoot(victim,bot);
    }
    public void botRun(Bot bot){
            LinkedList<CoordinatesWithRoom> possibleRun=botAction.proposeCellsRun(bot.getCoordinatesWithRooms());
            //TODO SEND POSSIBLE COORDINATES + CHOOSE ONE
            CoordinatesWithRoom choosenRun=new CoordinatesWithRoom();
            botAction.run(bot,choosenRun);
        broadcast("\nBot moved to "+choosenRun);
    }

    public void grab(){
        Player player = Server.model.getPlayers().get(currentPlayer);
        LinkedList<CoordinatesWithRoom> possibleCells = action.proposeCellsGrab(player);
        List<String> listOfCells = new LinkedList<>();
        List<String> listOfItems = new LinkedList<>();
        if(possibleCells.size()!=0)
        {
            for (CoordinatesWithRoom c : possibleCells){
                listOfCells.add(c.toString());
                if (c.containsSpawnpoint(model)&&!c.getSpawnpoint(model).getWeaponCards().isEmpty()) {
                    Spawnpoint s = c.getSpawnpoint(model);
                    String weapons = "";
                    for(WeaponCard w : s.getWeaponCards()){
                        weapons = weapons.concat(w.toString()+ " ");
                    }
                    listOfItems.add(weapons);
                }

                else if(c.getRoom().hasAmmoTile(c)){ // IT HAS AMMOTILES
                    listOfItems.add(c.getRoom().getAmmoTile(c).toString());

                    System.out.println("LIST OF ITEMS "+c.getRoom().getAmmoTile(c).toString());

                }else { // IT DOESN'T HAVE AN AMMOTILE
                    Server.addCellToList(c);    // IT'LL BE ADDED AT THE END OF TURN
                }
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


        if(chosenCell.containsSpawnpoint(model)) {
           grabFromSpawnpoint(chosenCell,player,listOfItems.get(x));
        }
        else {
            //se non spawnpoint
            //raccolgo un AmmoTile
            /*
            * qui faccio direttamente io l'assegnazione della posizione del player  e rimuovo gia' l'ammotile
            * dalla mappa e ne aggiungo subito un altro
            * già incluso pescaggio power up
            * */
            printPlayerAmmo(player);
            action.grabTile(player, chosenCell);
            printPlayerAmmo(player);

            broadcast(stringPlayerAmmo(player));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



        public void shoot(){
            Player player = model.getPlayers().get(currentPlayer);
            LinkedList<WeaponCard>playerWeaponCards=new LinkedList<>();
            WeaponCard weaponCard=new WeaponCard();
            LinkedList<EffectAndNumber> paidEffectAndNumber=new LinkedList<>();
            int z = 0;
            try {
                //CHIEDI CARTA DA PAGARE
                for (WeaponCard w : player.getHand()) {
                    if (w.getReload()) {
                        playerWeaponCards.add(w);
                    }
                }
                if (playerWeaponCards.size() == 0) {
                    lock.lock();
                    sendToClient("MESSAGE");
                    sendToClient("Sorry, you don't have reloaded weapons in your hand");
                    lock.unlock();
                } else {
                    lock.lock();
                    sendToClient("CHOOSEWEAPON");
                    sendListToClient(fromWeaponsToNames(playerWeaponCards));
                    z = (int) inputStream.readObject();
                    z--;
                    weaponCard = playerWeaponCards.get(z);  // TODO REMEMBER TO UNLOAD WEAPONCARD AFTER USING IT
                    lock.unlock();

                    int number = 0;
                    paidEffectAndNumber.add(shootBase(number));

                    AmmoCube.Effect effect;

                    if (weaponCard.canShootOp1()){
                        effect= AmmoCube.Effect.OP1;
                        lock.lock();
                        sendToClient("OP1");
                        String response = (String) inputStream.readObject();
                        lock.unlock();
                        if(response.toUpperCase().equals("Y")){
                            shootOtherEffect(effect,weaponCard, player, paidEffectAndNumber);

                        }
                    }
                    if (weaponCard.canShootOp2()){
                        effect= AmmoCube.Effect.OP2;
                        lock.lock();
                        sendToClient("OP2");
                        String response = (String) inputStream.readObject();
                        lock.unlock();
                        if(response.toUpperCase().equals("Y")){
                            shootOtherEffect(effect,weaponCard, player, paidEffectAndNumber);

                        }
                    }
                    if (weaponCard.canShootAlt()){
                        effect= AmmoCube.Effect.ALT;
                        lock.lock();
                        sendToClient("ALT");
                        String response = (String) inputStream.readObject();
                        lock.unlock();
                        if(response.toUpperCase().equals("Y")){
                            shootOtherEffect(effect,weaponCard, player, paidEffectAndNumber);

                        }
                    }
                    // REMOVES BASE IF I PAID FOR ALT
                    for(EffectAndNumber en : paidEffectAndNumber){
                        if(en.getEffect()== AmmoCube.Effect.ALT){
                            EffectAndNumber temp = en;
                            paidEffectAndNumber.clear();
                            paidEffectAndNumber.add(temp);
                        }
                    }
                    if(paidEffectAndNumber.size()>1) {
                        // NOW PAIDEFFECTSANDNUMBER HAS ALL THE EFFECTS PAID FOR
                        changeOrderOfEffects(paidEffectAndNumber, weaponCard);
                    }

                    List<Object> pastTragets = new LinkedList<>();
                    // SHOOT
                    for(EffectAndNumber paid : paidEffectAndNumber){
                        List<Object> temp = new LinkedList<>();
                        temp = requestsForEveryWeapon(paid,weaponCard,player,model.getMapUsed().getGameBoard(),model,pastTragets);
                        pastTragets=temp;
                    }
                }
                }catch(Exception e){
                    e.printStackTrace();
                }
        }

        public void shootOtherEffect(AmmoCube.Effect e, WeaponCard weaponCard, Player player, List<EffectAndNumber> effects) {

            LinkedList<PowerUpCard> playerPowerUpCards = new LinkedList<>();
            try {
                //CHIEDI METODO PAGAMENTO
                lock.lock();
                sendToClient("PAYMENT");
                int z = (int) inputStream.readObject();
                lock.unlock();
                Action.PayOption payOption;

                if (z == 1) {
                    payOption = Action.PayOption.AMMO;
                } else {
                    payOption = Action.PayOption.AMMOPOWER;
                }

                if (payOption.equals(Action.PayOption.AMMOPOWER)) {
                    playerPowerUpCards = payWithThesePowerUps(player);
                }
                // CONTROLLA SE PUO' PAGARE L'EFFETTO BASE SE NO ESCI E ANNULLA AZIONE
                if (!action.canPayCard(weaponCard, player, payOption, AmmoCube.Effect.BASE, playerPowerUpCards)) {
                    //MANDA MESSAGGIO
                    System.out.println("CAN'T PAY");
                    return;

                } else {
                    if(z==1){
                            effects.add(action.payAmmo(player,weaponCard,e,0));
                    }else{
                            effects.add(action.payPowerUp(weaponCard,playerPowerUpCards,player,e,0));
                            player.getPowerUp().removeAll(playerPowerUpCards);
                            model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);
                            playerPowerUpCards.clear();
                    }
                }
            } catch (Exception ex) {
            ex.printStackTrace();
            }
        }

        public EffectAndNumber shootBase(int number){
            return  new EffectAndNumber(AmmoCube.Effect.BASE,number);
        }

        public void changeOrderOfEffects(List<EffectAndNumber> list, WeaponCard w){
            // SOME WEAPONS MUST HAVE A CERTAIN ORDER
            // ASK ONLY IF I CAN CHANGE IT
            if(w instanceof Thor || w instanceof MachineGun)
                return;


            try{
                lock.lock();
                sendToClient("CHANGE");
                String response = (String) inputStream.readObject();
                if(response.toUpperCase().equals("Y")){
                    int x = list.size();
                    List<EffectAndNumber> list2 = list;
                    list.clear();

                    for(int i=0;i<x;i++){
                        //CHIEDI UN EFFETTO
                        sendToClient("CHOOSEORDER");
                        sendListToClient(fromEffectsAndNumberToNames(list2));
                        int z = (int) inputStream.readObject();
                        z--;
                        list.add(list2.remove(z));
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }

            lock.unlock();
        }


    public boolean grabFromSpawnpoint(CoordinatesWithRoom chosenCell, Player player,String cellItems){

        /*
         * controllo io se può pagarla + ricarica ma tu devi:
         * 1-controllare che abbia meno di 3 carte in mano x
         * 2-una volta raccolta devi rimuoverla dalle carte dello spawnpoint
         * 3-una volta finito cio setta la posizione del player nel punto in cui ha scelto di raccogliere
         * se scarta una carta rimettila nel deck x
         * */
        lock.lock();
        sendToClient("GRABWEAPON");
        sendToClient(cellItems); // RITORNA 1 O 2 O 3
        lock.unlock();
        try {
            int x = (int)inputStream.readObject();
            x--;
        Spawnpoint s = chosenCell.getSpawnpoint(model);
        WeaponCard weaponCard=s.getWeaponCards().get(x);

        LinkedList<PowerUpCard>playerPowerUpCards=new LinkedList<>();

        //CHIEDI METODO PAGAMENTO
            lock.lock();
        sendToClient("PAYMENT");
        int z = (int)inputStream.readObject();
        Action.PayOption payOption;
    
        if(z==1){
            payOption= Action.PayOption.AMMO;
        }else{
            payOption= Action.PayOption.AMMOPOWER;
        }

        if(payOption.equals(Action.PayOption.AMMOPOWER)){
            playerPowerUpCards=payWithThesePowerUps(player);
        }
        // CONTROLLA SE PUO' PAGARE L'EFFETTO BASE SE NO ESCI E ANNULLA AZIONE
        if(!action.canPayCard(weaponCard,player,payOption,AmmoCube.Effect.BASE,playerPowerUpCards)){
            //MANDA MESSAGGIO
            return false;}
        lock.unlock();

        // CONTROLLA SE PUO RACCOGLIERE ALTRIMENTI RICHIEDI DROP ARMA, METTILA IN DCARD
        if(!player.canGrabWeapon()){
            List<String> yourWeapons = new LinkedList<>();
            for (WeaponCard w : player.getHand()) {
                yourWeapons.add(w.toString());
            }
            lock.lock();
            sendToClient("DROPWEAPON");
            sendListToClient(yourWeapons); // RISPOSTA 1 O 2 O 3
            int y = (int)inputStream.readObject();
            // TODO METTERE L'ARMA SCARTATA NELLO SPAWNPOINT CON IL PRIMO CUBO PAGATO E BASTA
            lock.unlock();
        }

        //DOVRAI FARTI DARE UN NUMERO DALLE CARTE PER EFFECT&NUMBER??
        int number=0;

            if(z==1){action.payAmmo(player,weaponCard, AmmoCube.Effect.BASE,0);
            weaponCard.setReload();

            }else{action.payPowerUp(weaponCard,playerPowerUpCards,player, AmmoCube.Effect.BASE,0);
            weaponCard.setReload();
            }

        player.getHand().add(weaponCard);
        s.getWeaponCards().remove(weaponCard);
        player.getPowerUp().removeAll(playerPowerUpCards);
        model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);

        action.run(player,getSpawnpointCoordinates(s));
        broadcast(player+" grabbed "+weaponCard.toString()+ " from Spawnpoint "+ s.getColor().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public LinkedList<PowerUpCard> payWithThesePowerUps(Player player){
        LinkedList<PowerUpCard> chosenPower=new LinkedList<>();
        //MANDA CARTE POWER UP IN MANO
        for (PowerUpCard powerUp : player.getPowerUp()) {
            lock.lock();
            sendToClient("PAYWITHPOWERUP");
            sendToClient(powerUp.toString());   // RITORNA Y O N
            String response = null;
            try {
                response = (String) inputStream.readObject();
                lock.unlock();
            if(response.toUpperCase().equals("Y")){
                chosenPower.add(powerUp);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chosenPower;
    }

    public List<String> fromPowerupsToNames(List<PowerUpCard> list){
        List<String> pows = new LinkedList<>();
        for (PowerUpCard o : list) {
            pows.add(o.toString());
        }
        return pows;
    }
    public List<String> fromRoomsToNames(List<Room> list){
        List<String> rooms = new LinkedList<>();
        for (Room o : list) {
            rooms.add(Integer.toString(o.getToken()));
        }
        return rooms;
    }
    public List<String> fromTargetsToNames(List<Object> list){
        List<String> targets = new LinkedList<>();
        for (Object o : list) {
            targets.add(o.toString());
        }
        return targets;
    }
    public List<String> fromPlayersToNames(List<Player> list){
        List<String> targets = new LinkedList<>();
        for (Player o : list) {
            targets.add(o.toString());
        }
        return targets;
    }
    public List<String> fromCellsToNames(List<CoordinatesWithRoom> list){
        List<String> cells = new LinkedList<>();
        for (CoordinatesWithRoom c : list) {
            cells.add(c.toString());
        }
        return cells;
    }
    public List<String> fromWeaponsToNames(List<WeaponCard> weapons){
        List<String> list = new LinkedList<>();
        for (WeaponCard c : weapons) {
            list.add(c.toString());
        }
        return list;
    }
    public List<String> fromEffectsAndNumberToNames(List<EffectAndNumber> e){
        List<String> list = new LinkedList<>();
        for (EffectAndNumber c : e) {
            list.add(c.toString());
        }
        return list;
    }


    public List<Object> requestsForEveryWeapon(EffectAndNumber e, WeaponCard w, Player p, GameBoard g, GameModel model, List<Object> pastTargets){
        CoordinatesWithRoom playerPosition = p.getCoordinatesWithRooms();
        List<CoordinatesWithRoom> cells;
        List<Object> targets;
        try {
        switch (w.toString()){

            case "Cyberblade":lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE || e.getEffect()== AmmoCube.Effect.OP2){
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                    if (!targets.isEmpty()) {
                        // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xh = (int)inputStream.readObject();
                    xh--;
                    Object tt = targets.get(xh);
                    targets.clear();
                    targets.add(tt);

                    // CHECK TARGETS OP1 AND BASE DIFFERENTI (RITORNA IL GIOCATORE COLPITO COSì LO SALVI IN SHOOT)
                    // SE PASTTARGETS VUOTO OK, SE PIENO TARGET SCELTO DEVE ESSERE DIVERSO DA QUELLO
                    if(!pastTargets.isEmpty() && pastTargets.get(0)==targets.get(0)){
                        break;
                    }

                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                    lock.unlock();
                    return targets;
                }
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    // MOVE 1 SQUARE
                    List<CoordinatesWithRoom> one = playerPosition.oneTileDistant(g,false);
                    if (!one.isEmpty()) {
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(one)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int x = (int)inputStream.readObject();
                    x--;
                    p.setPlayerPosition(one.get(x));

                    lock.unlock();
                    broadcast("\n" + p.getName() + " is in "+one.get(x).toString());
                    lock.lock();
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no cells.");
                    }
                }
                lock.unlock();
                break;

            case "Electroscythe":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);
                lock.unlock();
                break;

            case "Flamethrower":lock.lock();
                cells = playerPosition.oneTileDistant(g,false);
                if (!cells.isEmpty()) {
                // ASK PLAYER TO CHOSE ONE OR TWO SQUARES  - SHOULD ASK SAYING 1 AND 2 MUST HAVE SAME DIR?
                sendToClient("CHOOSECELL");
                sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int xe = (int)inputStream.readObject();
                xe--;
                CoordinatesWithRoom chosen = cells.get(xe);
                List<CoordinatesWithRoom> cells1 = cells;
                cells.clear();
                cells.add(chosen);

                sendToClient("CHOOSEANOTHER");
                String response = (String) inputStream.readObject();
                if(response.toUpperCase().equals("Y")){
                    List<CoordinatesWithRoom> cellslist2 = w.getPossibleTargetCells(playerPosition,e,g);
                    cellslist2.removeAll(cells1);
                    if (!cellslist2.isEmpty()) {
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(cellslist2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xye = (int)inputStream.readObject();
                    xye--;

                    // CHECK FIRST (DISTANT 1) AND (SECOND DISTANT 2) SAME DIRECTION
                    // IF NOT, SECOND NOT ADDED
                    if(cells.get(0).checkSameDirection(cells.get(0),cellslist2.get(xye),10,g,false)) {
                        cells.add(cellslist2.get(xye));
                    }
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no cells.");
                    }
                }

                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                if(e.getEffect()== AmmoCube.Effect.BASE){
                    List<Object> targets2=targets;
                    targets.clear();
                    for(int i=0;i<cells.size();i++) {
                        // ASK 1 TARGET PER OGNI SQUARE (2 FORSE)
                        sendToClient("CHOOSETARGET");
                        if (!targets.isEmpty()) {
                        sendListToClient(fromTargetsToNames(targets2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xh = (int) inputStream.readObject();
                        xh--;
                        Object tt = targets2.get(xh);
                        targets.add(tt);
                        targets2.remove(tt);
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                        }
                    }
                }
                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;

            case "Furnace":lock.lock();
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
                    if (!possibleRooms.isEmpty()) {
                    // ASK PLAYER FOR A ROOM (DIFFERENT) - METTILA NELLA COORDINATA ROOM
                    // SEND PLAYER LIST OF ROOMS possibleRooms APPENA CREATA
                    sendToClient("CHOOSEROOM");
                    sendListToClient(fromRoomsToNames(possibleRooms)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xoh = (int) inputStream.readObject();
                    xoh--;
                    CoordinatesWithRoom room = new CoordinatesWithRoom(1,1,possibleRooms.get(xoh));

                    cells = w.getPossibleTargetCells(room, e, g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no rooms different from yours.");
                    }
                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    if (!cells.isEmpty()) {
                    // ASK PLAYER WHICH TILE, GET ONE BACK IN THAT LIST
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int x = (int)inputStream.readObject();
                    x--;
                    p.setPlayerPosition(cells.get(x));
                    broadcast("\n" + p.getName() + " is in "+cells.get(x).toString());

                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no cells.");
                    }
                }
                lock.unlock();
                break;

            case "GrenadeLauncher":lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                    if (!targets.isEmpty()) {
                    // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int x = (int)inputStream.readObject();
                    x--;
                    Object t = targets.get(x);
                    targets.clear();
                    targets.add(t);

                    w.applyDamage(targets, p, e);
                    useTargetingScope(p,targets);

                    // ASK SE VUOLE MUOVERE IL TARGET DI 1
                    sendToClient("MOVETARGET");
                    String res = (String) inputStream.readObject();
                    if(res.toUpperCase().equals("Y")){
                        CoordinatesWithRoom c =((Player)targets.get(0)).getCoordinatesWithRooms();
                        List<CoordinatesWithRoom> list = c.oneTileDistant(g,false);
                        if (!list.isEmpty()) {
                        sendToClient("CHOOSECELL");
                        sendListToClient(fromCellsToNames(list)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xyp = (int)inputStream.readObject();
                        xyp--;
                        ((Player) targets.get(0)).setPlayerPosition(list.get(xyp));
                        broadcast("\n" + ((Player) targets.get(0)).getName() + " is in "+list.get(xyp).toString());
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no cells.");
                        }
                    }
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                }
                if(e.getEffect()== AmmoCube.Effect.OP1) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    if (!cells.isEmpty()) {
                    // ASK UNA CELLA ANCHE LA TUA
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xr = (int)inputStream.readObject();
                    xr--;
                    CoordinatesWithRoom c2 =cells.get(xr);
                    cells.clear();
                    cells.add(c2);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no cells.");
                    }
                }
                lock.unlock();
                break;

            case "Heatseeker":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int x = (int)inputStream.readObject();
                x--;
                Object t = targets.get(x);
                targets.clear();
                targets.add(t);

                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;

            case "Hellion":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int y = (int)inputStream.readObject();
                y--;
                // SAVE IT FOR LATER
                Object target = targets.get(y);
                List<Object> targets2 = new LinkedList<>();
                targets2.add(target);
                targets.clear();
                targets.add(target);

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
                useTargetingScope(p,targets2); // IT HAS JUST FIRST TARGET (THE DAMAGED ONE)
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;

            case "LockRifle":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int r = (int)inputStream.readObject();
                r--;
                Object tar = targets.get(r);
                targets.clear();
                targets.add(tar);

                // CHECK TARGETS OP1 AND BASE DIFFERENTI (RITORNA IL GIOCATORE COLPITO COSì LO SALVI IN SHOOT)
                // SE PASTTARGETS VUOTO OK, SE PIENO TARGET SCELTO DEVE ESSERE DIVERSO DA QUELLO
                if(!pastTargets.isEmpty() && pastTargets.get(0)==targets.get(0)){
                    break;
                }

                w.applyDamage(targets,p,e);
                if(e.getEffect()== AmmoCube.Effect.BASE){   // ONLY BASE DAMAGES TARGETS
                    useTargetingScope(p,targets);
                }
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                return targets;

            case "MachineGun":  // CONSIDERO BASE PRIMA DEGLI ALTRI EFFETTI
                lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    if (!targets.isEmpty()) {
                    // ASK TO CHOOSE 1 OR 2 TARGETS -BASE
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int nu = (int) inputStream.readObject();
                    nu--;
                    Object tr = targets.get(nu);
                    List<Object> targ2 = targets;
                    targets.clear();
                    targets.add(tr);
                    targ2.remove(tr);

                    sendToClient("CHOOSEANOTHER");
                    String rsp = (String) inputStream.readObject();
                    if (rsp.toUpperCase().equals("Y")) {
                        sendToClient("CHOOSETARGET");
                        sendListToClient(fromTargetsToNames(targ2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xpo = (int) inputStream.readObject();
                        xpo--;
                        targets.add(targ2.get(xpo));
                    }
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                    lock.unlock();
                    return targets;             //SAVE THEM AS FIRST IN PASTTARGETS
                }
                // SHOOT AGAIN ONE OF THEM -OP1 FROM PASTTARGETS
                if(!pastTargets.isEmpty() && e.getEffect()== AmmoCube.Effect.OP1){
                    if(e.getNumber()==2 && pastTargets.size()==2){  // NOTHING TO SHOOT (OP2 BEFORE OP1)
                        break;
                    }
                    if(e.getNumber()==2 && pastTargets.size()==3){ // OP2 BEFORE OP1
                        pastTargets.removeAll(Collections.singleton(pastTargets.size() - 1));   // CAN'T DAMAGE AGAIN TARGET FROM OP2
                        w.applyDamage(pastTargets,p,e); //JUST ONE
                        useTargetingScope(p,pastTargets);

                        break;
                    }
                    if (!pastTargets.isEmpty()) {
                    sendListToClient(fromTargetsToNames(pastTargets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int u = (int)inputStream.readObject();
                    u--;
                    Object tor = targets.get(u);
                    targets.clear();
                    targets.add(tor);
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                    e.setNumber(1);     // IT MEANS THAT I EXECUTED OP1 (PASTTARGETS FROM OP1)
                    lock.unlock();
                    return targets;         // SAVE IT AFTER BASE TARGETS
                }
                // ASK TO SHOOT THE OTHER OR AND SOMEONE ELSE
                if(!pastTargets.isEmpty() && e.getEffect()== AmmoCube.Effect.OP2) {
                    List<Object> pastTargets2 = new LinkedList<>(); // NEED IT LATER IF E.NUMBER!=1

                    if (e.getNumber() == 1) {
                        // CHECK TARGETS OP1 AND OP2 DIFFERENTI
                        // SE PASTTARGETS VUOTO OK, SE PIENO TARGET SCELTO DEVE ESSERE DIVERSO DA QUELLO
                        pastTargets.removeAll(Collections.singleton(pastTargets.size() - 1));   // CAN'T DAMAGE AGAIN TARGET FROM OP1 (IT'S THE LAST)
                    }
                    if(!pastTargets.isEmpty()){
                    // SHOOT MAYBE AGAIN TARGET FROM BASE
                    sendToClient("CHOOSETARGET");
                    List<String> toSend = fromTargetsToNames(pastTargets);
                    toSend.add(0, "No, I dont't want these targets");
                    sendListToClient(toSend); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int nu = (int) inputStream.readObject();
                    nu--;
                    if (nu != 0) {
                        nu--;
                        Object tr = pastTargets.get(nu);
                        pastTargets2 = pastTargets;    // ORIGINAL PASTTARGETS (I NEED THEM IF E.NUMBER!=1)
                        pastTargets2.add(tr);

                        pastTargets.clear();
                        pastTargets.add(tr);
                        w.applyDamage(pastTargets, p, e);
                        useTargetingScope(p,pastTargets);
                    }
                } else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                    // SHOOT MAYBE DIFFERENT TARGET FROM BASE
                    targets.removeAll(pastTargets);
                    if (!targets.isEmpty()) {
                    sendToClient("CHOOSETARGET");
                    List<String> toSend2 = fromTargetsToNames(targets);
                    toSend2.add(0,"No, I dont't want these targets");
                    sendListToClient(toSend2); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int nku = (int) inputStream.readObject();
                    nku--;
                    if(nku!=0) {
                        nku--;
                        Object tr = targets.get(nku);
                        targets.clear();
                        targets.add(tr);
                        w.applyDamage(targets, p, e);
                        useTargetingScope(p,targets);
                    }

                    if(e.getNumber()!=1){   // MAYBE OP1 AFTER OP2
                        e.setNumber(2);
                        lock.unlock();
                        return pastTargets2;    // IF SIZE 3 (2 BASE + 1) IF SIZE 2 (1 BASE + 1, SAME TARGET)
                    }
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                }
                lock.unlock();
                break;

            case "PlasmaGun":lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    if (!targets.isEmpty()) {
                    // ASK WHICH 1 TARGET TO DAMAGE, (SAVE IT FOR THE OTHER EFFECT)
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int i = (int)inputStream.readObject();
                    i--;
                    Object tg = targets.get(i);
                    targets.clear();
                    targets.add(tg);

                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                    lock.unlock();
                    return targets;
                }
                if(e.getEffect()== AmmoCube.Effect.OP2){
                    targets=pastTargets;    // TODO OP2 DOPO BASE PER FORZA
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    lock.unlock();
                }
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    if (!cells.isEmpty()) {
                    // ASK WHERE TO MOVE
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xg = (int)inputStream.readObject();
                    xg--;
                    lock.unlock();
                    p.setPlayerPosition(cells.get(xg));
                    broadcast("\n" + p.getName() + " is in "+cells.get(xg).toString());
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no cells.");
                        lock.unlock();
                    }
                }
                break;

            case "PowerGlove":lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                    if (!targets.isEmpty()) {
                    // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xh = (int) inputStream.readObject();
                    xh--;
                    Object tt = targets.get(xh);
                    targets.clear();
                    targets.add(tt);

                    w.applyDamage(targets, p, e);
                    useTargetingScope(p, targets);

                    // MOVE PLAYER TO TARGET'S SQUARE
                    action.run(p, ((Player) tt).getCoordinatesWithRooms());
                    lock.unlock();
                    broadcast("\n" + p.getName() + " moved to "+((Player)tt).getCoordinatesWithRooms());
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                        lock.unlock();
                    }
                }
                // ALSO IF ALT
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    CoordinatesWithRoom c0 = playerPosition; // SAVED PLAYER'S POSITION
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    if (!cells.isEmpty()) {
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xpf = (int) inputStream.readObject();
                    xpf--;
                    action.run(p, cells.get(xpf));
                    broadcast("\n" + p.getName() + " moved to "+cells.get(xpf).toString());

                    CoordinatesWithRoom cc = cells.get(xpf);
                    cells.clear();
                    cells.add(cc);

                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                        if (!targets.isEmpty()) {
                    sendToClient("CHOOSETARGET");
                    List<String> toSend = fromTargetsToNames(targets);
                    toSend.add(0, "No, I dont't want these targets");
                    sendListToClient(toSend); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int nu = (int) inputStream.readObject();
                    nu--;
                    if (nu != 0) {
                        nu--;
                        Object ttt = targets.get(nu);
                        targets.clear();
                        targets.add(ttt);

                        w.applyDamage(targets, p, e);
                        useTargetingScope(p, targets);

                        CoordinatesWithRoom c2 = c0.getNextCell(c0,cc,g, false);
                        if(c2.getX()==0 || c2.getY()==0){
                            lock.unlock();
                            return new LinkedList<>();
                        }
                        cells.clear();
                        cells.add(c2);

                        toSend.clear();
                        toSend = fromCellsToNames(cells);
                        toSend.add(0, "No, I dont't want to move there");
                        sendToClient("CHOOSECELL");
                        sendListToClient(toSend); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xif = (int) inputStream.readObject();
                        xif--;
                        if(xif!=0) {
                            xif--;
                            action.run(p, c2);
                            lock.unlock();
                            broadcast("\n" + p.getName() + " moved to "+c2.toString());
                            lock.lock();
                            targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                            if (!targets.isEmpty()) {
                            sendToClient("CHOOSETARGET");
                            toSend.clear();
                            toSend = fromTargetsToNames(targets);
                            toSend.add(0, "No, I dont't want these targets");
                            sendListToClient(toSend); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                            int nur = (int) inputStream.readObject();
                            nur--;
                            if (nur != 0) {
                                nur--;
                                Object trtt = targets.get(nur);
                                targets.clear();
                                targets.add(trtt);

                                w.applyDamage(targets, p, e);
                                useTargetingScope(p, targets);
                            }
                            }else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no targets.");
                            }
                        }
                    }
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                        }
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no cells.");
                    }
                }
                lock.unlock();
                break;

            case "Railgun":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                // ASK TO CHOOSE 1 (BASE) OR 1-2 (ALT) TARGETS, REMOVE OTHERS
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int i = (int)inputStream.readObject();
                i--;
                Object tg = targets.get(i);
                List<Object> targets3 = targets;
                targets.clear();
                targets.add(tg);

                // ASK IF WANT ANOTHER TARGET IF ALT EFFECT
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    targets3.remove(tg);
                    if (!targets3.isEmpty()) {
                    sendToClient("CHOOSEANOTHER");
                    String rsp = (String) inputStream.readObject();
                    if (rsp.toUpperCase().equals("Y")) {
                        sendToClient("CHOOSETARGET");
                        sendListToClient(fromTargetsToNames(targets3)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xeo = (int) inputStream.readObject();
                        xeo--;
                        targets.add(targets3.get(xeo));
                    }
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                }
                if(e.getEffect()== AmmoCube.Effect.ALT && targets.size()==2 &&

                    !playerPosition.checkSameDirection(((Player)targets.get(0)).getCoordinatesWithRooms(),((Player)targets.get(1)).getCoordinatesWithRooms(),10,g,false)){
                        // 2 TARGETS OF ALT EFFECT HAVE TO BE IN THE SAME DIRECTION!!!!!
                        // IF checkSameDirection FALSE REMOVE SECOND TARGET
                        targets.remove(1);
                }
                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;

            case "RocketLauncher": lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    // ASK 1 TARGET (SAVE IT - POSITION - FOR OP2 EFFECT)
                    if(pastTargets.isEmpty()){  // PRIMA BASE- SE NON VIENE PASSATO NULLA RITORNA IL GIOCATORE COLPITO

                        cells = w.getPossibleTargetCells(playerPosition,e,g);

                    }else{// PRIMA OP2- SE GLI PASSANO UN GIOCATORE ALLORA PRENDI LA SUA POS,
                          // METTILA IN CELLS TO TARGETS, FAI SCEGLIERE UN TARGET E COLPISCILO E MUOVI SE VUOI
                        cells = new LinkedList<>();
                        cells.add(((Player)pastTargets.get(0)).getCoordinatesWithRooms());
                    }
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    if (!targets.isEmpty()) {
                    // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int n = (int)inputStream.readObject();
                    n--;
                    Object oj = targets.get(n);
                    targets.clear();
                    targets.add(oj);

                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);

                    // ASK IF MOVE TARGET 1 SQUARE IF PRIMA BASE
                    sendToClient("MOVETARGET");
                    String rs = (String) inputStream.readObject();
                    if (rs.toUpperCase().equals("Y")) {
                        CoordinatesWithRoom ctarget = ((Player)oj).getCoordinatesWithRooms();
                        List<CoordinatesWithRoom> one = ctarget.oneTileDistant(g, false);
                        if (!one.isEmpty()) {
                        sendToClient("CHOOSECELL");
                        sendListToClient(fromCellsToNames(one)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xf = (int) inputStream.readObject();
                        xf--;
                        ((Player) targets.get(0)).setPlayerPosition(one.get(xf));
                        lock.unlock();
                        broadcast("\n" + ((Player) targets.get(0)).getName() + " is in "+one.get(xf).toString());

                        w.applyDamage(targets,p,e);
                        useTargetingScope(p,targets);

                        // IF PRIMA BASE RETURN OLD TARGET POSITION (AS SECOND PLAYER) BEFORE MOVING IT
                        Player pp = new Player();
                        pp.setPlayerPosition(ctarget);
                        broadcast("\n" + pp.getName() + " is in "+ctarget);
                        targets.add(pp);
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no cells.");
                        }
                        return targets;
                    }
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }

                    }
                if(e.getEffect()== AmmoCube.Effect.OP2) {
                    if(pastTargets.isEmpty()) { // PRIMA OP2- (NULLA PASSATO) SCEGLI UNA CELLA, COLPISCILI E POI RITORNA LA CELLA (COME GIOCATORE)
                        cells = w.getPossibleTargetCells(playerPosition,e,g);
                        // CHIEDI 1 CELLA, COLPISCILI, RITORNA CELLA COME GIOCATORE
                        if (!cells.isEmpty()) {
                        sendToClient("CHOOSECELL");
                        sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int xg = (int)inputStream.readObject();
                        xg--;
                        CoordinatesWithRoom d = cells.get(xg);
                        cells.clear();
                        cells.add(d);
                        targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                        w.applyDamage(targets,p,e);
                        useTargetingScope(p,targets);

                        Player p2 = new Player();
                        p2.setPlayerPosition(d);
                        lock.unlock();
                        broadcast("\n" + p2.getName() + " is in "+d.toString());
                        List<Object> lis7 = new LinkedList<>();
                        lis7.add(p2);
                        lock.unlock();
                        return lis7;
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                            lock.unlock();
                            return new LinkedList<>();
                        }

                    }else{  // PRIMA BASE- PRENDI POSIZIONE VECCHIA DI TARGET (PASSATA COME GIOCATORE)
                        cells = w.getPossibleTargetCells(((Player)pastTargets.get(1)).getCoordinatesWithRooms(),e,g);
                        targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                        if(!targets.contains(pastTargets.get(0))){
                            targets.add(pastTargets.get(0));    // OLD TARGET ALSO DAMAGED
                        }
                        w.applyDamage(targets,p,e);
                        useTargetingScope(p,targets);
                        lock.unlock();
                    }
                }
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    if (!cells.isEmpty()) {
                    // ASK WHERE TO MOVE
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xg = (int)inputStream.readObject();
                    xg--;
                    p.setPlayerPosition(cells.get(xg));
                    lock.unlock();
                    broadcast("\n" + p.getName() + " is in "+cells.get(xg).toString());
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no cells.");
                }
                }
                break;

            case "Shockwave":lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                    targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);

                    if (!targets.isEmpty()) {

                        // ASK WHICH TARGETS TO DAMAGE - UP TO 3
                        sendToClient("CHOOSETARGET");
                        sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int nm = (int) inputStream.readObject();
                        nm--;
                        Object ot = targets.get(nm);
                        List<Object> list2 = targets;
                        targets.clear();
                        targets.add(ot);
                        list2.remove(ot);

                        for (int v = 0; v < 2; v++) {
                            sendToClient("CHOOSEANOTHER");
                            String rs = (String) inputStream.readObject();
                            if (rs.toUpperCase().equals("Y")) {
                                if (!list2.isEmpty()) {
                                sendToClient("CHOOSETARGET");
                                sendListToClient(fromTargetsToNames(list2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                                int ye = (int) inputStream.readObject();
                                ye--;
                                targets.add(list2.get(ye));
                                list2.remove(ye);
                                }else{
                                    sendToClient("MESSAGE");
                                    sendToClient("Sorry there are no targets.");
                                }
                            } else {
                                break;
                            }
                        }
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
                            } else {    // SE SBAGLIA TOLGO I TARGET E BASTA
                                if (targets.size() == 2) {
                                    targets.remove(1);
                                }
                                if (targets.size() == 3) {
                                    targets.remove(2);
                                }
                            }
                        }
                        w.applyDamage(targets, p, e);
                        useTargetingScope(p, targets);

                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                }
                lock.unlock();
                break;

            case "Shotgun":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int nm = (int)inputStream.readObject();
                nm--;
                Object ot = targets.get(nm);
                targets.clear();
                targets.add(ot);

                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);

                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    // ASK -IF- THEY WANT TO MOVE THAT TARGET 1 SQUARE
                    sendToClient("MOVETARGET");
                    String rs = (String) inputStream.readObject();
                    if (rs.toUpperCase().equals("Y")) {

                    List<CoordinatesWithRoom> one = playerPosition.oneTileDistant(g,false);
                        if (!one.isEmpty()) {
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(one)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xf = (int)inputStream.readObject();
                    xf--;
                    ((Player)targets.get(0)).setPlayerPosition(one.get(xf));
                    lock.unlock();
                    broadcast("\n" + ((Player)targets.get(0)).getName() + " is in "+one.get(xf).toString());
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                            lock.unlock();
                        }
                    }
                }
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                break;

            case "Sledgehammer":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int num = (int)inputStream.readObject();
                num--;
                Object ojt = targets.get(num);
                targets.clear();
                targets.add(ojt);
                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);

                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    // ASK TO MOVE THAT TARGET 0-1-2 SQUARE SAME DIRECTION
                    List<CoordinatesWithRoom> possibleCells = playerPosition.tilesSameDirection(2,g,false);
                    if (!possibleCells.isEmpty()) {
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(possibleCells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xj = (int)inputStream.readObject();
                    xj--;
                    ((Player)targets.get(0)).setPlayerPosition(possibleCells.get(xj));
                    lock.unlock();
                    broadcast("\n" + ((Player)targets.get(0)).getName() + " is in "+possibleCells.get(xj).toString());
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                }
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;

            case "Thor":lock.lock();
                cells = new LinkedList<>();

                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition, e, g);
                }

                if(e.getEffect()== AmmoCube.Effect.OP1 || e.getEffect()== AmmoCube.Effect.OP2) {
                    // PUT HERE TARGET'S POSITION, GLIEL'HA PASSATA
                    CoordinatesWithRoom targetPos=((Player)pastTargets.get(0)).getCoordinatesWithRooms();
                    cells = w.getPossibleTargetCells(targetPos, e, g);

                }
                targets = w.fromCellsToTargets(cells, playerPosition, g, p, model, e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int nr = (int)inputStream.readObject();
                nr--;
                Object oc = targets.get(nr);
                targets.clear();
                targets.add(oc);
                w.applyDamage(targets, p, e);
                useTargetingScope(p,targets);
                // RETURN THE TARGET (YOU'LL NEED TO CHECK THEY ARE DIFFERENT)
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                return targets;

            case "TractorBeam":lock.lock();
                if(e.getEffect()== AmmoCube.Effect.BASE) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);   // CELLS I SEE
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);   // TARGETS DISTANTI MAX 2 DA CIò CHE VEDO(cioè che posso muovere)
                    if (!targets.isEmpty()) {
                    // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xy = (int)inputStream.readObject();
                    xy--;
                    Object trg = targets.get(xy);
                    targets.clear();
                    targets.add(trg);

                    CoordinatesWithRoom c1 = ((Player)trg).getCoordinatesWithRooms(); // SALVACI LA POS DEL TARGET
                    LinkedList<CoordinatesWithRoom> listOne = new LinkedList<>();
                    List<CoordinatesWithRoom> listMoves;    // TARGET'S POSSIBLE MOVES

                            listMoves = c1.oneTileDistant(g, false);
                            listMoves.addAll(c1.xTilesDistant(g, 2));
                            listMoves.add(c1);
                    for(CoordinatesWithRoom c3: listMoves) {
                        if (cells.contains(c3)) {
                            listOne.add(c3);    // IF I SEE, THEM I ADD THEM
                            // CHECK CELLS.CONTAINS(A POSSIBLE MOVE)
                        }
                    }
                        if (!listOne.isEmpty()) {
                    // ASK WHERE TO MOVE
                    sendToClient("CHOOSECELL");
                    sendListToClient(fromCellsToNames(listOne)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xgy = (int)inputStream.readObject();
                    xgy--;
                    ((Player)trg).setPlayerPosition(listOne.get(xgy));
                    lock.unlock();
                    broadcast("\n" + ((Player)trg).getName() + " is in "+listOne.get(xgy).toString());
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no cells.");

                            lock.unlock();
                        }
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                        lock.unlock();
                    }
                }
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    cells = w.getPossibleTargetCells(playerPosition,e,g);
                    targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                    if (!targets.isEmpty()) {
                    // ASK WHICH 1 TARGET TO DAMAGE, REMOVE THE OTHERS
                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xz = (int)inputStream.readObject();
                    xz--;
                    Object tu = targets.get(xz);
                    targets.clear();
                    targets.add(tu);
                    //MOVE IT TO YOUR SQUARE
                    ((Player)targets.get(0)).setPlayerPosition(playerPosition);
                    lock.unlock();
                    broadcast("\n" + ((Player)targets.get(0)).getName() + " is in "+playerPosition.toString());
                    w.applyDamage(targets,p,e);
                    useTargetingScope(p,targets);
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                        lock.unlock();
                    }
                }
                break;

            case "VortexCannon":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                if (!cells.isEmpty()) {
                // CHOOSE A VORTEX
                sendToClient("CHOOSECELL");
                sendListToClient(fromCellsToNames(cells)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int xgt = (int)inputStream.readObject();
                xgt--;
                CoordinatesWithRoom vortex = cells.get(xgt);
                List<CoordinatesWithRoom> list;
                list = vortex.oneTileDistant(g,false);
                list.add(vortex);

                targets = w.fromCellsToTargets(list,playerPosition,g,p,model,e);
                    if (!targets.isEmpty()) {
                // ASK FOR 1 TARGET IF BASE OR UP TO 2 IF OP1
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int xn = (int)inputStream.readObject();
                xn--;
                Object ty = targets.get(xn);
                List<Object> targets1 = targets;
                targets.clear();
                targets.add(ty);
                targets1.remove(ty);
                if(e.getEffect()== AmmoCube.Effect.OP1){
                    sendToClient("CHOOSEANOTHER");
                    String rs = (String) inputStream.readObject();
                    if (rs.toUpperCase().equals("Y")) {
                        if (!targets1.isEmpty()) {
                        sendToClient("CHOOSETARGET");
                        sendListToClient(fromTargetsToNames(targets1)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int ye = (int) inputStream.readObject();
                        ye--;
                        targets.add(targets1.get(ye));
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                        }
                    }
                }
                if(!pastTargets.isEmpty()){
                    // CHECK DIFFERENTI DA QUELLI PASSATI
                    for(Object o : targets){
                        for(Object u :pastTargets){
                            if(o==u){
                                targets.remove(o);
                                break;
                            }
                        }
                    }
                }
                lock.unlock();
                // MOVE EVERY TARGET ON THE VORTEX
                for(Object o : targets){
                    ((Player)o).setPlayerPosition(vortex);
                    broadcast("\n" + ((Player)o).getName() + " is in "+vortex.toString());

                }

                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);
                        return targets;
                    }else{
                        sendToClient("MESSAGE");
                        sendToClient("Sorry there are no targets.");
                    }
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no cells.");
                }
                return new LinkedList<>();

            case "Whisper":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK WHICH 1 TARGET TO DAMAGE
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int l = (int)inputStream.readObject();
                l--;
                Object b = targets.get(l);
                targets.clear();
                targets.add(b);
                w.applyDamage(targets,p,e);
                useTargetingScope(p,targets);
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;

            case "Zx_2":lock.lock();
                cells = w.getPossibleTargetCells(playerPosition,e,g);
                targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);
                if (!targets.isEmpty()) {
                // ASK FOR 1 TARGET IF BASE OR UP TO 3 IF ALT
                sendToClient("CHOOSETARGET");
                sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int qw = (int)inputStream.readObject();
                qw--;
                Object tw = targets.get(qw);
                List<Object> tar2 = targets;
                targets.clear();
                targets.add(tw);
                tar2.remove(tw);

                //ASK AGAIN IF ALT
                if(e.getEffect()== AmmoCube.Effect.ALT) {
                    for (int v = 0; v < 2; v++) {
                        sendToClient("CHOOSEANOTHER");
                        String rs = (String) inputStream.readObject();
                        if (rs.toUpperCase().equals("Y")) {
                            if (!tar2.isEmpty()) {
                            sendToClient("CHOOSETARGET");
                            sendListToClient(fromTargetsToNames(tar2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                            int ye = (int) inputStream.readObject();
                            ye--;
                            targets.add(tar2.get(ye));
                            tar2.remove(ye);
                            }else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no targets.");
                            }
                        } else {
                            break;
                        }
                    }
                }
                w.applyDamage(targets,p,e);
                if(e.getEffect()== AmmoCube.Effect.BASE){
                    useTargetingScope(p,targets);
                }
                }else{
                    sendToClient("MESSAGE");
                    sendToClient("Sorry there are no targets.");
                }
                lock.unlock();
                break;
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        lock.unlock();
        return Collections.emptyList(); // SE NON DIVERSAMENTE SPECIFICATO
    }

    public void useTargetingScope(Player p, List<Object> targets){
            if(p.hasTargetingScope()){
                try {
                    lock.lock();
                // ASK USE TARGETING SCOPE
                sendToClient("TARGETINGSCOPE");
                String res = (String) inputStream.readObject();
                if(res.toUpperCase().equals("Y")){

                    // TODO Pay 1 ammo cube of any color

                    sendToClient("CHOOSETARGET");
                    sendListToClient(fromTargetsToNames(targets)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int xyp = (int)inputStream.readObject();
                    xyp--;
                    lock.unlock();
                    p.getTargetingScope().plusOneDamage(p,targets.get(xyp));
                    PowerUpCard pow = p.getTargetingScope();

                    p.getPowerUp().remove(pow);
                    model.powerUpDeck.getUsedPowerUp().add(pow);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

/*    private static class PlayerCountdown{
        public PlayerCountdown(){
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
    }*/





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
        //model.startingMap();
        model.populateMap();
        printSomeAmmos();
        action = new Action(model);
    }

    public static void printSomeAmmos(){
        for(AmmoTile t : model.getMapUsed().getGameBoard().getRooms().get(3).getTiles()){
            System.out.println(t.toString()+" "+t.getCoordinates().getX()+" "+t.getCoordinates().getY());
        }
    }
    public static String stringPlayerAmmo(Player p){
        return p.toString()+": BLUE "+p.getCubeBlue()+" RED "+p.getCubeRed()+" YELLOW "+p.getCubeYellow();
    }
    public static void printPlayerAmmo(Player p){
        System.out.println(p.toString()+": BLUE "+p.getCubeBlue()+" RED "+p.getCubeRed()+" YELLOW "+p.getCubeYellow());
    }

    public static void addCellToList(CoordinatesWithRoom c){
        cellsWithoutTiles.add(c);
    }


}


