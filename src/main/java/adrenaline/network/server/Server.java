

        package adrenaline.network.server;

// SOCKET
        import adrenaline.*;
        import adrenaline.gameboard.GameBoard;
        import adrenaline.powerups.Newton;
        import adrenaline.powerups.Teleporter;
        import adrenaline.weapons.MachineGun;
        import adrenaline.weapons.PlasmaGun;
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

    private GameModel model;
    private static Action action;
    private static FreneticAction freneticAction;
    private static int currentPlayer = 0;
    private static boolean firstTurn = true;

    private static int time = 0;
    private static int connectionsCount = 0;
    private static int boardChosen = 42;
    private static boolean frenzy = false;  // TO KNOW IF I HAVE CHOSEN YET
    private static boolean frenzyState = false;  // TO KNOW IF IT STARTED
    private static boolean firstPlayerFrenzy = false;
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

    private static HashMap<String,ObjectInputStream> writers2 = new HashMap<>();


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


    public void start() {
        try {
            serverSocket = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println("Couldn't start server.");
            // Server non si avvia
        }
        System.out.println("SERVER ONLINE");
        while (true) {

            try {
                Socket socket = serverSocket.accept();

                new RequestHandler(socket).start();
            } catch (IOException e) {
                System.out.println("Couldn't accept socket connection.");
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

    public static void stopHandler(RequestHandler handler){
        handler.interrupt();
    }

    public class RequestHandler extends Thread {

        Figure.PlayerColor color;
        String nickname;
        boolean flag = false;
        PlayerCountdown countdown;
        int numberOfActions =0;
        int numberOfActionsFrenzy =0;

        private final Socket socket;

        private final ObjectInputStream inputStream;

        private final ObjectOutputStream outputStream;

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
                    System.out.println("Handler couldn't reach client. ");
                    if(model.getPlayers().get(currentPlayer).getName().equals(nickname)) {
                        nextPlayer();
                        removeOneConnection();
                    }
                    Server.stopHandler(this);

                    if(connectionsCount<3 && isGameOn()){
                        System.out.println("Sent Final Scoring");
                        sendFinalScoring();
                    }
                }
            } catch (Exception e) {
                System.out.println("Handler couldn't reach client BIS.");
            }
        }

        public void disconnect(){
            try {
                if(nickname!=null) {
                    sendToClient("DISCONNECTED");
                    if (!disconnected.contains(nickname)) {
                        disconnected.add(nickname);
                    }
                    if (!disconnectedColors.containsKey(nickname)) {
                        disconnectedColors.put(nickname, color);
                    }
                    writers.remove(nickname, outputStream);
                    writers2.remove(nickname, inputStream);

               /* for(String s : disconnected){
                    System.out.println(s+ " DISCONNECTED LIST");
                }

                */
                    System.out.println("DISCONNECTED CONTAINS " + nickname + " " + disconnected.contains(nickname) + " " + disconnectedColors.containsKey(nickname));

                    removeOneConnection();
                    System.out.println("Client Disconnected++++ HERE -1");
                }
                    socket.close();
                Server.stopHandler(this);

                if(lock.isHeldByCurrentThread()) {
                    while (lock.getHoldCount() > 0) {
                        lock.unlock();
                    }
                }
                if(connectionsCount<3 && isGameOn()){
                    System.out.println("Sent Final Scoring");
                    sendFinalScoring();
                }

            }catch (IOException e) {
                System.out.println("Couldn't disconnect.");
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

        public void countConnections(){

            Server.connectionsCount++;
        }
        public void removeOneConnection(){

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
                                for(String s : disconnected){
                                    System.out.println(s + " disconnected+++++++++++");
                                }
                                sendToClient("MESSAGE");
                                sendToClient("Sorry, the game has already started without you");
                                socket.close();
                            }

                            color = checkColor();
                            chooseBoard(nickname); // it checks if is firstPlayer and asks board
                            chooseFrenzy();

                            sendToClient("ACCEPTED");

                            if (connectionsCount == 3) {
                                Countdown c = new Countdown();
                            }
                            addPlayerToGame(nickname, color);
                            writers.put(nickname, outputStream);
                            writers2.put(nickname, inputStream);

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
                            chooseFrenzy();

                            sendToClient("ACCEPTED");

                            if (connectionsCount == 3) {
                                Countdown c = new Countdown();
                            }
                            addPlayerToGame(nickname, color);
                            writers.put(nickname, outputStream);
                            writers2.put(nickname, inputStream);
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

                        writers.put(nickname, outputStream);
                        writers2.put(nickname, inputStream);
                        disconnected.remove(nickname);
                        disconnectedColors.remove(nickname);
                    }else {

                        sendToClient("MESSAGE");
                        sendToClient("Sorry, the game has already started without you");
                        socket.close();
                    }
                }

            } catch (Exception e) {
                System.out.println("Client couldn't log in.");
            }

        }
        public void reconnect(){
            try {
                writers.put(nickname, outputStream);
                writers2.put(nickname, inputStream);

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

        public String loginName(){
            try {
                sendToClient("LOGIN");
                String name = (String) inputStream.readObject();
                // Keep requesting a name until we get a unique one.
                while (true) {
                    if (name != null || !name.equals("null")) {
                        synchronized (names) {
                            if (!isBlank(name) && !names.contains(name)) {
                                names.add(name);
                                return name;
                            } else if (names.contains(name) && !disconnected.contains(name)) {
                                System.out.println("names contains name " + names.contains(name));
                                System.out.println("disconnected contains name " + disconnected.contains(name));
                                for (String s : disconnected) {
                                    System.out.println(s + " DISCONNECTED LIST");
                                }

                                sendToClient("LOGIN");
                                name = (String) inputStream.readObject();
                            } else if (disconnected.contains(name)) {
                                return name;
                            }
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("Couldn't log in");
            }
            return null;
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
                lock.lock();
                model.addPlayer(new Player(name, color));
                lock.unlock();
                /*
                System.out.println(lock.isHeldByCurrentThread() + " held by " + nickname);
                System.out.println(lock.isLocked() + " is locked " + nickname);
                System.out.println(lock.getHoldCount() + " count " + nickname);
*/
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
                    if(nickname!=null) {
                        // CAUSE: DISCONNECTED
                        //ELIMINA ENTRY IN WRITER
                        //AGGIUNGI A DISCONNECTED
                        //AGGIUNGI A DISCONNECTEDCOLORS
                        System.out.println("Someone disconnected from game and I couldn't reach him.");
                        if (!disconnected.contains(me.getKey().toString())) {
                            disconnected.add(me.getKey().toString());
                        }
                        if (!disconnectedColors.containsKey(me.getKey().toString())) {
                            disconnectedColors.put(me.getKey().toString(), disconnectedColors.get(me.getKey().toString()));
                        }
                        writers.remove(me.getKey().toString(), (ObjectOutputStream) me.getValue());
                        writers2.remove(me.getKey().toString());
                   /* for(String d : disconnected){
                        System.out.println(d+ " DISCONNECTED LIST");
                    }

                   */
                        System.out.println("DISCONNECTED CONTAINS " + me.getKey().toString() + " " + disconnected.contains(me.getKey().toString()) + " " + disconnectedColors.containsKey(me.getKey().toString()));
                        removeOneConnection();
                        System.out.println("Client Disconnected++ HERE -1");

                    }
                    if(lock.isHeldByCurrentThread()) {
                        while (lock.getHoldCount() > 1) {
                            lock.unlock();
                        }
                    }

                    if(connectionsCount<3 && isGameOn()){
                        System.out.println("Sent Final Scoring");
                        sendFinalScoring();
                    }
                }
            }
            lock.unlock();
        }

        public void ending(){

            if(model.hasFrenzyOn()){
                //FRENZY
                // SI PASSA A DUE TURNI SPECIALI
                frenzyState=true;

            }else {

                sendFinalScoring();
                //setGameIsOn(false); // EXITS????????????
            }
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

                            setBoardChosen(result);
                            System.out.println("BOARD CHOSEN " + result);

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
        public void chooseFrenzy(){
            try {
                if(!frenzy){
                    lock.lock();
                    while(!frenzy){

                        sendToClient("FRENZY");
                        String response = (String) inputStream.readObject();
                        if (response.toUpperCase().equals("Y")) {

                            createBoard(true);
                            setFrenzyChosen();
                            lock.unlock();
                            return;
                        } else {
                            createBoard(false);
                            setFrenzyChosen();
                            lock.unlock();
                            return;
                        }

                    }

                }
            }catch (Exception e){
                lock.unlock();
                disconnect();
                System.out.println("--Frenzy disconnection");
            }
        }
        ////


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
                            System.out.println("Couldn't use Tagback Grenade.");
                        }
                    }
                }
            }
        }

        public boolean isCurrentPlayer(){
            synchronized (model.getPlayers().get(currentPlayer)){
                return model.getPlayers().get(currentPlayer).getName().equals(nickname);
            }
        }
        public boolean hasBeenDamaged(){
            synchronized (model.getPlayers()){
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
        public boolean hasFirstPlayerPlayedFrenzy(){
            synchronized (this){
                return firstPlayerFrenzy;
            }
        }
        public boolean isFrenzyOn(){
            synchronized (this){
                return frenzyState;
            }
        }
        public void firstPlayerPlayedFrenzy(){
            synchronized (this){
                firstPlayerFrenzy=true;
            }
        }

        public synchronized void startCountdown(RequestHandler handler){
            countdown = new PlayerCountdown(handler);
            System.out.println(handler.nickname + " "+ handler);
        }

        public void flagFalse(){
            flag=false;
        }

        public void setNumberofActions(int x){
            numberOfActions=x;
        }
        public int getNumberofActions(){
            return numberOfActions;
        }
        public void numberofActionsPlusOne(){
            numberOfActions++;
        }
        public void numberofActionsMinusOne(WeaponCard w){
            numberOfActions--;
            w.setReload();
        }

        public void setNumberOfActionsFrenzy(int x){
            numberOfActionsFrenzy=x;
        }
        public int getNumberOfActionsFrenzy(){
            return numberOfActionsFrenzy;
        }
        public void numberofActionsFrenzyPlusOne(){
            numberOfActionsFrenzy++;
        }
        public void numberofActionsFrenzyMinusOne(WeaponCard w){
            numberOfActionsFrenzy--;
            w.setReload();
        }

        public void handleTurns(){
            setNumberofActions(0);
            boolean counterOn=false;
            while(!isEndgame()) {
                if (isGameOn()) {

                    if (isCurrentPlayer()) {
         if(!isFrenzyOn()){
                        if (!counterOn) {
                            startCountdown(this);
                            counterOn = true;
                        }

                        try {
                            if (Server.isFirstTurn()) {
                                if (currentPlayer == 0) {

                                    broadcast("\nThe board is number " + boardChosen);
                                }
                                lock.lock();
                                sendToClient("YOURFIRSTTURN");
                                System.out.println("FIRST TURN");
                                System.out.println("\n" + nickname);
                                sendForBoardSetup();
                                System.out.println(nickname + "\n");
                                lock.unlock();
                                firstTurn();
                                setNumberofActions(0);
                                System.out.println("CURRENT PLAYER " + currentPlayer);
                            }
                            if (getNumberofActions() != 2 && !firstTurn) {
                                lock.lock();
                                sendToClient("YOURTURN");

                                String choice = (String) inputStream.readObject();
                                lock.unlock();
                                System.out.println(choice);
                                switch (choice) {
                                    case "G":
                                        lock.lock();
                                        sendToClient("GRAB");
                                        grab(false,false);
                                        lock.unlock();
                                        numberofActionsPlusOne();
                                        break;
                                    case "R":
                                        lock.lock();
                                        sendToClient("RUN");
                                        playerRun();
                                        lock.unlock();
                                        numberofActionsPlusOne();
                                        break;
                                    case "M":
                                        lock.lock();
                                        sendToClient("MAP");
                                        sendForBoardSetup();
                                        lock.unlock();
                                        break;
                                    case "C":
                                        lock.lock();
                                        sendToClient("SCORE");
                                        sendScoring();
                                        lock.unlock();
                                        break;
                                    case "B":
                                        lock.lock();
                                        sendToClient("BOARDS");
                                        playerBoards();
                                        sendMarks();
                                        sendWeapons();
                                        sendPowerUps();
                                        sendAmmo();
                                        sendPositions();
                                        lock.unlock();
                                        break;
                                    case "P":
                                        powerup();
                                        break;
                                    case "S":
                                    default:
                                        shoot();
                                        numberofActionsPlusOne();
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            try {//e.printStackTrace();

                                countdown.timer.cancel();
                                counterOn = false;
                                /*
                                System.out.println("EXCEPTION DISCONNECTION");
                                System.out.println("by "+ this.toString());*/

                                if (lock.isHeldByCurrentThread()) {
                                    while (lock.getHoldCount() > 0) {
                                        lock.unlock();
                                    }
                                }

                                Server.stopHandler(this);
                                /*
//                                    replaceAmmo();
//                                    replaceWeapons();
//                                    nextPlayer();

                                    numberOfActions = 0;
                                    setNotDamaged();
                                    System.out.println("CURRENT PLAYER " + currentPlayer + " " + nickname);

                                disconnect();

                                    lock.lock();
                                    sendToClient("DISCONNECTED");
                                    lock.unlock();*/
                                break;
                            } catch (Exception ex) {
                                System.out.println("Couldn't disconnect and pass turn.");
                            }
                        }
                        // END OF TURN
                        if (getNumberofActions() == 2 && !firstTurn) {

                            // CAN USE SOME POWERUPS BEFORE END OF TURN
                            powerup();

                            reload();
                            countdown.timer.cancel();
                            counterOn = false;
                            scoring();
                            setNotDamaged();
                            //synchronized (model.getMapUsed()) {model.populateMap();}

                            //replaceAmmo();
                            //replaceWeapons();
                            model.populateMap();

                            System.out.println("        ---NEXT PLAYER");
                            nextPlayer();
                            //broadcast(nickname +" ended his turn. Now is the turn of "+model.getPlayers().get(currentPlayer));
                            setNumberofActions(0);
                            System.out.println("CURRENT PLAYER " + currentPlayer);

/*
                            System.out.println("\n Thread info: ");
                            System.out.println(lock.isHeldByCurrentThread()+ " " + nickname);
                            System.out.println(lock.isLocked() + " " + nickname);
                            System.out.println(lock.getHoldCount() + " " + nickname);
*/
                            if (lock.isHeldByCurrentThread()) {
                                while (lock.getHoldCount() > 0) {
                                    lock.unlock();
                                }
                            }

                        }
                        if (Server.isFirstTurn() && !flag) {
                            if (currentPlayer == model.getPlayers().size() - 1) {
                                Server.endFirstTurn();
                            }
                            countdown.timer.cancel();
                            counterOn = false;

                            System.out.println("        +++NEXT PLAYER");
                            nextPlayer();
                            //broadcast(nickname +" ended his turn. Now is the turn of "+model.getPlayers().get(currentPlayer));
                            setNumberofActions(0);
                            System.out.println("CURRENT PLAYER " + currentPlayer);
/*
                            System.out.println("\n Thread info: ");
                            System.out.println(lock.isHeldByCurrentThread()+ " " + nickname);
                            System.out.println(lock.isLocked() + " " + nickname);
                            System.out.println(lock.getHoldCount() + " " + nickname);
*/

                            if (lock.isHeldByCurrentThread()) {
                                while (lock.getHoldCount() > 0) {
                                    lock.unlock();
                                }
                            }

                        }

         }else { // FRENZY ON

             if (!counterOn) {
                 startCountdown(this);
                 counterOn = true;
             }
             try{
             if(!hasFirstPlayerPlayedFrenzy() && !model.getPlayers().get(0).getName().equals(nickname)) {

                 lock.lock();
                 sendToClient("FRENZY1");

                 String choice = (String) inputStream.readObject();
                 lock.unlock();
                 System.out.println(choice);
                 switch (choice) {
                     case "G":
                         lock.lock();
                         grabFrenzy();
                         numberofActionsPlusOne();
                         lock.unlock();
                         break;
                     case "R":
                         lock.lock();
                         runFrenzy();
                         numberofActionsPlusOne();
                         lock.unlock();
                         break;
                     case "M":
                         lock.lock();
                         sendToClient("MAP");
                         sendForBoardSetup();
                         lock.unlock();
                         break;
                     case "C":
                         lock.lock();
                         sendToClient("SCORE");
                         sendScoring();
                         lock.unlock();
                         break;
                     case "B":
                         lock.lock();
                         sendToClient("BOARDS");
                         playerBoards();
                         sendMarks();
                         sendWeapons();
                         sendPowerUps();
                         sendAmmo();
                         lock.unlock();
                         break;
                     case "S":
                     default:
                         lock.lock();
                         shootFrenzy();
                         numberofActionsPlusOne();
                         lock.unlock();
                         break;
                 }

                 if(numberOfActionsFrenzy ==2) {
                     countdown.timer.cancel();
                     counterOn = false;
                     scoring();
                     setNotDamaged();
                     replaceAmmo();
                     replaceWeapons();

                 }
             }else{ // FIRST PLAYER HAS PLAYED FRENZY

                 if (model.getPlayers().get(0).getName().equals(nickname)) {
                     firstPlayerPlayedFrenzy();
                 }


                 lock.lock();
                 sendToClient("FRENZY2");
                 int z = (int) inputStream.readObject(); // RITORNA 1 O 2
                 lock.unlock();
                 if (z == 1) {

                     lock.lock();
                     shootFrenzy2();
                     lock.unlock();

                 } else {

                     lock.lock();
                     grabFrenzy2();
                     lock.unlock();

                 }

                 countdown.timer.cancel();
                 counterOn = false;
                 scoring();
                 setNotDamaged();
                 replaceAmmo();
                 replaceWeapons();


                 if(model.getPlayers().get(model.getPlayers().size()-1).getName().equals(nickname)){
                     sendFinalScoring();
                 }

             }
         } catch (Exception e) {
                            try {//e.printStackTrace();

                                countdown.timer.cancel();
                                counterOn = false;

                                if (lock.isHeldByCurrentThread()) {
                                    while (lock.getHoldCount() > 0) {
                                        lock.unlock();
                                    }
                                }

                                Server.stopHandler(this);
                                break;
                            } catch (Exception ex) {
                                System.out.println("Couldn't disconnect and pass turn.");
                            }
                        }

         }
                    } // NOT CURRENT PLAYER
                    else if(hasBeenDamaged()){  //SET DAMAGED AND SHOOTER WHEN YOU SHOOT
                        tagbackGrenade();
                    }else{
                        flag=false;
                    }
                    // !Server.action.endOfTheGame(model.getMapUsed().getGameBoard()))
                    // SET endgame parameter in this class
                }

                    //BROADCAST CLASSIFICA
                    //EXIT
                    //setEndgame(true);

            }
            //System.exit(0);
        }

        public void grabFrenzy(){

                grab(true,false);
        }

        public void runFrenzy() {
            try {
                Player p =model.getPlayers().get(currentPlayer);
                List<CoordinatesWithRoom> four = freneticAction.proposeCellsRunFrenzy(p.getCoordinatesWithRooms());
                sendToClient("CHOOSECELL");
                sendListToClient(fromCellsToNames(four)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                int x = (int) inputStream.readObject();
                x--;
                    p.setPlayerPosition(four.get(x));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void shootFrenzy(){
            Player p = model.getPlayers().get(currentPlayer);
            try {
                List<CoordinatesWithRoom> one = p.getCoordinatesWithRooms().oneTileDistant(model.getMapUsed().getGameBoard(), false);
                if (!one.isEmpty()) {
                    List<String> toSend = new LinkedList<>();
                    toSend = fromCellsToNames(one);
                    toSend.add(0,"No, I don't want to move");

                    sendToClient("CHOOSECELL");
                    sendListToClient(toSend); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int x = (int) inputStream.readObject();
                    x--;
                    if(x!=0) {
                        x--;
                        p.setPlayerPosition(one.get(x));
                    }
                }
                reload();

                shoot();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void shootFrenzy2(){
            Player p = model.getPlayers().get(currentPlayer);
            try {
                List<CoordinatesWithRoom> two = p.getCoordinatesWithRooms().oneTileDistant(model.getMapUsed().getGameBoard(), false);
                two.addAll(p.getCoordinatesWithRooms().xTilesDistant(model.getMapUsed().getGameBoard(),2));
                if (!two.isEmpty()) {
                    List<String> toSend = new LinkedList<>();
                    toSend = fromCellsToNames(two);
                    toSend.add(0,"No, I don't want to move");

                    sendToClient("CHOOSECELL");
                    sendListToClient(toSend); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                    int x = (int) inputStream.readObject();
                    x--;
                    if(x!=0) {
                        x--;
                        p.setPlayerPosition(two.get(x));
                    }
                }
                reload();

                shoot();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void grabFrenzy2(){
                grab(true,true);
        }


        public void setNotDamaged(){
            for (Player p : model.getPlayers()){
                p.setDamagedStatus(false);
                p.setShooter(null);
            }
        }

        public void handleException() {
            lock.lock();
            sendToClient("DISCONNECTED");
            lock.unlock();
            disconnected.add(nickname);
            disconnectedColors.put(nickname, color);
            writers.remove(nickname,outputStream);
            writers2.remove(nickname,inputStream);
            System.out.println("Client Disconnected");
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close socket.");
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

            if(connectionsCount<3 && isGameOn()){
                System.out.println("Sent Final Scoring");
                sendFinalScoring();
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
                sendAmmoTiles();
            } catch (IOException e) {
                System.out.println("Couldn't send info about game.");
            }
        }

        public void sendPositions(){
            List<String> positions = new LinkedList<>();
            for(Player p : model.getPlayers()){
                if(!Server.isFirstTurn()){
                    positions.add(p.getCoordinatesWithRooms().toString());
                }else{
                    positions.add("NOT YET DECIDED");
                }
            }
            try {
            sendListToClient(positions);
        }catch (Exception e){
            System.out.println("Couldn't send positions");
        }
        }

        public void sendAmmoTiles(){
            List<String> listOfCells = new LinkedList<>();
            List<String> listOfItems = new LinkedList<>();
            // PER TUTTE LE CELLE DI TUTTE LE STANZE

            for(Room r : model.getMapUsed().getGameBoard().getRooms()){
                for(int x =1; x<=r.getRoomSizeX();x++){
                    for(int y =1; y<=r.getRoomSizeY();y++) {
                        CoordinatesWithRoom c = new CoordinatesWithRoom(x,y,r);
                        listOfCells.add(c.toString());

                        if (c.isSpawnpointCoordinates(model)) {
                            listOfItems.add("Spawnpoint "+c.getSpawnpoint(model).getColor().toString());
                        }else if(c.hasAmmoTile()){ // IT HAS AMMOTILES
                            listOfItems.add(c.getRoom().getAmmoTile(c).toString());
                        }else { // IT DOESN'T HAVE AN AMMOTILE
                            listOfItems.add("No tile");
                            doThis(c);
                        }
                    }
                }
            }
            try {
                sendListToClient(listOfItems);
                sendListToClient(listOfCells);

                System.out.println("\nCells&Items on map:");
                for(int i =0;i<listOfCells.size();i++){
                    System.out.println(listOfCells.get(i) + " "+listOfItems.get(i));
                }

            }catch (Exception e){
                System.out.println("Couldn't send tiles");
            }
        }

        public void doThis(CoordinatesWithRoom c){
            System.out.println("Coord "+ c.getX()+ " "+c.getY()+ " "+c.getRoom().getToken());
            for (AmmoTile t : c.getRoom().getTiles()){
                System.out.println(t.getCoordinates().getX()+ " "+t.getCoordinates().getY());
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

        public synchronized void respawn(Player p){
            p.getPowerUp().add(model.powerUpDeck.pickPowerUp());
            List<String> toSend = fromPowerupsToNames(p.getPowerUp());
            try {lock.lock();

                for (Map.Entry me : writers.entrySet()) {
                        if(me.getKey().equals(p.getName())){
                            ((ObjectOutputStream)me.getValue()).writeObject("RESPAWN");
                            ((ObjectOutputStream)me.getValue()).flush();

                            ((ObjectOutputStream)me.getValue()).writeObject(toSend);
                            ((ObjectOutputStream)me.getValue()).flush();

                            int x =1;
                            for (Map.Entry me2 : writers2.entrySet()) {
                                if(me2.getKey().equals(me.getKey())) {

                                    x = (int) ((ObjectInputStream)me2.getValue()).readObject();
                                    x--;
                                }

                                lock.unlock();
                                PowerUpCard po = p.getPowerUp().remove(x);
                                System.out.println("TO USE AS POSITION " + po.toString());

                                p.newLife();
                                setInitialPosition(po.getPowerUpColor(), p);
                            }
                        }
                }
            }catch (Exception e){
                handleException();
            }
        }

        public synchronized void firstTurn(){
            synchronized (model.getPlayers().get(currentPlayer)) {
                lock.lock();
                LinkedList<PowerUpCard> twoCards = new LinkedList<>();
                twoCards.add(model.powerUpDeck.pickPowerUp());
                twoCards.add(model.powerUpDeck.pickPowerUp());
                List<String> cards = new LinkedList<>();
                cards.add(0, twoCards.get(0).toString());
                cards.add(1, twoCards.get(1).toString());
                Player player = model.getPlayers().get(currentPlayer);
                try {
                    sendListToClient(cards);
                    int x = (int) inputStream.readObject();
                    x--;
                    // CHIEDI QUALE CARTA DA TENERE (1 o 2) E QUALE DA USARE COME RESPAWN


                    PowerUpCard p = twoCards.removeFirst();
                    //System.out.println("TO KEEP "+p.toString());
                    if (x == 1) {
                        model.getPlayers().get(currentPlayer).getPowerUp().add(p);

                        p = twoCards.removeFirst();
                        //System.out.println("TO USE AS POSITION "+p.toString());

                        setInitialPosition(p.getPowerUpColor(), model.getPlayers().get(currentPlayer));
                    } else {
                        setInitialPosition(p.getPowerUpColor(), model.getPlayers().get(currentPlayer));

                        p = twoCards.removeFirst();
                        //System.out.println("TO USE AS POSITION "+p.toString());

                        model.getPlayers().get(currentPlayer).getPowerUp().add(p);
                    }

                } catch (Exception e) {
                    System.out.println("Couldn't do firstTurn. Default.");

                    player.getPowerUp().add(twoCards.get(0));
                    setInitialPosition(twoCards.get(1).getPowerUpColor(), player);
                    broadcast("\n" + player.getName() + " is in " + player.getCoordinatesWithRooms().toString());
                }
                lock.unlock();
            }
        }

        /**
         * Updates index of next Player.
         * If everybody has played, it resets.
         */
        public void nextPlayer(){
            try {                System.out.println("------CHANGING PLAYER");
                if (currentPlayer != model.getPlayers().size() - 1) {
                    currentPlayer++;
                } else {
                    currentPlayer = 0;
                }
                if (disconnected.contains(model.getPlayers().get(currentPlayer).getName())) {
                    if (currentPlayer != model.getPlayers().size() - 1) {
                        currentPlayer++;
                    } else {
                        currentPlayer = 0;
                    }
                    if (disconnected.contains(model.getPlayers().get(currentPlayer).getName())) {
                        if (currentPlayer != model.getPlayers().size() - 1) {
                            currentPlayer++;
                        } else {
                            currentPlayer = 0;
                        }
                        if (disconnected.contains(model.getPlayers().get(currentPlayer).getName())) {
                            if (currentPlayer != model.getPlayers().size() - 1) {
                                currentPlayer++;
                            } else {
                                currentPlayer = 0;
                            }
                        }
                    }
                }
            }catch (Exception e){
                //
            }
        }

        public synchronized void setInitialPosition(AmmoCube.CubeColor c, Player p){
            for (Room r: model.getMapUsed().getGameBoard().getRooms()){
                for (Spawnpoint s:r.getSpawnpoints()
                     ) {
                    if(s.getColor().equals(c))
                    {
                        p.setPlayerPosition(s.getSpawnpointX(),s.getSpawnpointY(),r);
                        p.setPlayerPositionSpawnpoint(p.getCoordinatesWithRooms());
                        broadcast("\n" + p.getName() + " is in "+p.getCoordinatesWithRooms().toString());
                        System.out.println("INITIAL POSITION OF "+p.getName()+" IS  "+p.getCoordinatesWithRooms().toString());
                        return;
                    }
                }

            }
        }

        // EVERY TURN
        public void scoring(){
            List<Player> victims = new LinkedList<>();
            for(Player p : model.getPlayers()){
                if(p.isDead()){
                    victims.add(p);
                }
            }
            action.canGetPoints(victims,model.getPlayers());

            System.out.println("Scoring");
            for(Player p : model.getPlayers()){
                System.out.println(p.getPoints() + " points of "+ p.getName());
            }

            for(Player p : victims){

                respawn(p);
            }


            // ENDGAME
            if (action.endOfTheGame(model.getMapUsed().getGameBoard())){

                ending();
            }

        }

        public void sendScoring(){
            try {
                List<String> list = new LinkedList<>();
                for(Player p : model.getPlayers()){
                    list.add(p.toString());
                    list.add(Integer.toString(p.getPoints()));
                }
                sendListToClient(list);
            }catch (Exception e){
                System.out.println("Couldn't send score.");
            }
        }


        public void sendFinalScoring(){
            lock.lock();
            action.canGetPoints(model.getPlayers(),model.getPlayers());
            action.finalScoring();
            lock.lock();
            sendToClient("ENDGAME");

            List<String> finalList = new LinkedList<>();
            for(Player p : model.getPlayers()){
                finalList.add(p.getPlayerPos().toString());
                finalList.add(p.getName());
                finalList.add(Integer.toString(p.getPoints()));
            }

            for (Map.Entry me : writers.entrySet()) {
                try {
                    ((ObjectOutputStream)me.getValue()).writeObject("ENDGAME");
                    ((ObjectOutputStream)me.getValue()).flush();

                    ((ObjectOutputStream)me.getValue()).writeObject(finalList);
                    ((ObjectOutputStream)me.getValue()).flush();

                } catch (IOException e) {
                    // DON'T CARE ITS ENDGAME
                }
            }
            lock.unlock();
            lock.unlock();
        }


        public void reload(){
            System.out.println("Current player reload "+ model.getPlayers().get(currentPlayer).getName());
            Player player = model.getPlayers().get(currentPlayer);
            try{
                lock.lock();
                List<WeaponCard> weapons = new LinkedList<>(player.getHand());
                for (WeaponCard w : player.getHand()){
                    if(w.getReload()){
                        weapons.remove(w);
                    }else{
                        System.out.println(w.toString()+" "+w.getReload());
                    }
                }
                if(!weapons.isEmpty()) {
                    for (WeaponCard w : weapons) {
                        sendToClient("RELOAD");
                        sendToClient(w.toString());
                        String response = (String) inputStream.readObject();

                        if (response.toUpperCase().equals("Y")) {
                            LinkedList<PowerUpCard> playerPowerUpCards = new LinkedList<>();
                            sendToClient("PAYMENT");
                            int z = (int) inputStream.readObject();

                            Action.PayOption payOption;

                            if (z == 1) {
                                payOption = Action.PayOption.AMMO;
                            } else {
                                payOption = Action.PayOption.AMMOPOWER;
                            }

                            if (payOption.equals(Action.PayOption.AMMOPOWER)) {
                                playerPowerUpCards = payWithThesePowerUps(player);
                            }
                            if (!action.canPayGrab(w, player,playerPowerUpCards)) {
                                //MANDA MESSAGGIO
                                System.out.println("CAN'T PAY");
                            }
                            if (z == 1 && action.canPayGrab(w, player,playerPowerUpCards)) {
                                    action.payAmmo(player, w, AmmoCube.Effect.GRAB, 0);
                                    w.setReload();
                                } else {
                                    action.payPowerUp(w, playerPowerUpCards, player, AmmoCube.Effect.GRAB, 0);
                                    player.getPowerUp().removeAll(playerPowerUpCards);
                                    model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);
                                    playerPowerUpCards.clear();
                                    w.setReload();

                            }
                        }
                    }
                }
                lock.unlock();
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Couldn't reload.");
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
                    sendToClient("Sorry you can't use any of your powerups now");
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
                        System.out.println("Couldn't do powerup.");
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
                System.out.println("Couldn't use Teleporter.");
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
                System.out.println("Couldn't use Newton.");;
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
                System.out.println("Couldn't send BLUE weapons.");
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
                System.out.println("Couldn't send RED weapons.");
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
                System.out.println("Couldn't send YELLOW weapons.");
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
                System.out.println("Couldn't send player boards.");
            }
        }

        public void sendMarks(){
            try {
                List<String> list = new LinkedList<>();
                for(Player p : model.getPlayers()){
                    synchronized (p.getMarks()) {
                        for (int i = 0; i < 12; i++) {
                                list.add(p.getMarks()[i].toString());
                        }
                    }
                }
                sendListToClient(list);
            }catch (Exception e){
                System.out.println("Couldn't send marks.");
            }
        }

        public void sendWeapons(){
            try {
                List<String> list = new LinkedList<>();
                for(Player p : model.getPlayers()){
                    synchronized (p.getHand()) {
                        for (int i = 0; i < 3; i++) {
                            if (p.getHand().size() > i) {
                                list.add(p.getHand().get(i).toString());
                                list.add(Boolean.toString(p.getHand().get(i).getReload()));
                            } else {
                                list.add("[   ]");
                                list.add("[   ]");
                            }
                        }
                    }
                }
                sendListToClient(list);
            }catch (Exception e){
                System.out.println("Couldn't send hand weapons.");
            }
        }

        public void sendPowerUps(){
            try {
                List<String> list = new LinkedList<>();
                for(Player p : model.getPlayers()){
                    synchronized (p.getPowerUp()) {
                        for (int i = 0; i < 3; i++) {
                            if (p.getPowerUp().size() > i) {
                                list.add(p.getPowerUp().get(i).toString());
                            } else {
                                list.add("[   ]");
                            }
                        }
                    }
                }
                sendListToClient(list);
            }catch (Exception e){
                System.out.println("Couldn't send hand weapons.");
            }
        }
        public void sendAmmo(){
            try {
                List<String> list = new LinkedList<>();
                for(Player p : model.getPlayers()){
                    list.add(Integer.toString(p.getCubeBlue()));
                    list.add(Integer.toString(p.getCubeRed()));
                    list.add(Integer.toString(p.getCubeYellow()));
                }
                sendListToClient(list);
            }catch (Exception e){
                System.out.println("Couldn't send ammo.");
            }
        }

        public void playerRun(){
            System.out.println("RUN");
//            System.out.println("PREVIOUS POSITION "+model.getPlayers().get(currentPlayer).getCoordinatesWithRooms().toString());

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
//                System.out.println("CURRENT POSITION " + player.getCoordinatesWithRooms().toString());
            } catch (Exception e) {
                System.out.println("Couldn't run.");
            }
        }

         public void grab(boolean frenzy, boolean second){
                    Player player = model.getPlayers().get(currentPlayer);
                    List<CoordinatesWithRoom> possibleCells = new LinkedList<>();

                    if(frenzy){ // FRENZY GRAB - UP TO 2
                        possibleCells = player.getCoordinatesWithRooms().oneTileDistant(model.getMapUsed().getGameBoard(),false);
                        possibleCells.addAll(player.getCoordinatesWithRooms().xTilesDistant(model.getMapUsed().getGameBoard(),2));
                        possibleCells.add(player.getCoordinatesWithRooms());
                        if(second){
                            possibleCells.addAll(player.getCoordinatesWithRooms().xTilesDistant(model.getMapUsed().getGameBoard(),3));
                        }

                    }else { //NORMAL GRAB
                        possibleCells = action.proposeCellsGrab(player);
                    }
                    List<String> listOfCells = new LinkedList<>();
                    List<String> listOfItems = new LinkedList<>();
                    if (!possibleCells.isEmpty()) {
                        for (CoordinatesWithRoom c : possibleCells) {
                            listOfCells.add(c.toString());
                            if (c.isSpawnpointCoordinates(model) && !c.getSpawnpoint(model).getWeaponCards().isEmpty()) {
                                Spawnpoint s = c.getSpawnpoint(model);
                                String weapons = "";
                                for (WeaponCard w : s.getWeaponCards()) {
                                    weapons = weapons.concat(w.toString() + " ");
                                }
                                listOfItems.add(weapons);
                            } else if (c.getRoom().hasAmmoTile(c)) { // IT HAS AMMOTILES
                                listOfItems.add(c.getRoom().getAmmoTile(c).toString());

                                // System.out.println("LIST OF ITEMS "+c.getRoom().getAmmoTile(c).toString());

                            } else { // IT DOESN'T HAVE AN AMMOTILE
                                Server.addCellToList(c);    // IT'LL BE ADDED AT THE END OF TURN
                                System.out.println("ADDED TO LIST " + c.toString());
                            }
                        }
                    }
                    CoordinatesWithRoom chosenCell = new CoordinatesWithRoom();
                    try {
                        sendListToClient(listOfItems);
                        sendListToClient(listOfCells); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                        int x = (int) inputStream.readObject();
                        x--;
                        chosenCell = possibleCells.get(x);
                        //System.out.println("CELL FOR GRAB " + chosenCell.toString());


                        if (chosenCell.isSpawnpointCoordinates(model)) {
                            grabFromSpawnpoint(chosenCell, player, listOfItems.get(x));
                        } else {
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
                            broadcast(nickname + " moved to "+ player.getCoordinatesWithRooms().toString());
                        }
                    } catch (Exception e) {
                        System.out.println("Couldn't grab or couldn't broadcast it.");
                    }

        }



        public void shoot(){
            synchronized (model){
            Player player = model.getPlayers().get(currentPlayer);
            LinkedList<WeaponCard>playerWeaponCards=new LinkedList<>();
            WeaponCard weaponCard=new WeaponCard();
            LinkedList<EffectAndNumber> paidEffectAndNumber=new LinkedList<>();
            int z = 0;

            if(player.checkDamage()==2) {
                lock.lock();
                sendToClient("RUN");
                {
                    LinkedList<CoordinatesWithRoom> cells = action.proposeCellsRunBeforeShoot(player);
                    List<String> possibilities = new LinkedList<>();
                    for (CoordinatesWithRoom c : cells) {
                        possibilities.add(c.toString());
                    }
                    possibilities.add(0, player.getCoordinatesWithRooms().toString() + "  This is your position");
                    try {
                        sendListToClient(possibilities); // RITORNA 1 OPPURE 2 OPPURE 3 .... il primo è la pos iniziale
                        int x = (int) inputStream.readObject();
                        x--;
                        if (x > 0) {
                            action.run(player, cells.get(x));
                            lock.unlock();
                            broadcast("\n" + nickname + " moved to " + cells.get(x).toString());
                        }
//                System.out.println("CURRENT POSITION " + player.getCoordinatesWithRooms().toString());
                    } catch (Exception e) {
                        System.out.println("Couldn't run.");
                        lock.unlock();

                    }


                }
            }
            try {
                //CHIEDI CARTA DA PAGARE
                synchronized (player.getHand()) {
                    for (WeaponCard w : player.getHand()) {
                        if (w.getReload()) {
                            playerWeaponCards.add(w);
                        }
                    }
                }
                if (playerWeaponCards.isEmpty()) {
                    lock.lock();
                    sendToClient("MESSAGE");
                    sendToClient("Sorry, you don't have reloaded weapons in your hand");
                    numberofActionsMinusOne(new WeaponCard());
                    lock.unlock();
                } else {
                    lock.lock();
                    sendToClient("CHOOSEWEAPON");
                    sendListToClient(fromWeaponsToNames(playerWeaponCards));
                    z = (int) inputStream.readObject();
                    z--;
                    weaponCard = playerWeaponCards.get(z);
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
                        if(pastTragets!=null&&!pastTragets.isEmpty()&&(paid.getEffect().equals(AmmoCube.Effect.BASE)||paid.getEffect().equals(AmmoCube.Effect.ALT)))
                        {
                            weaponCard.setNotReload();
                            weaponCard.setReloadAlt(false);
                        }
                    }

                }
            }catch(Exception e){
                System.out.println("Couldn't shoot.");
                e.printStackTrace();
            }}
        }

        public void shootOtherEffect(AmmoCube.Effect e, WeaponCard weaponCard, Player player, List<EffectAndNumber> paidEffects) {

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
                if (!action.canPayCard(weaponCard, player, payOption, e, playerPowerUpCards)) {
                    //MANDA MESSAGGIO
                    System.out.println("CAN'T PAY");
                    return;

                } else {
                    if(z==1){
                        paidEffects.add(action.payAmmo(player,weaponCard,e,0));
                    }else{
                        paidEffects.add(action.payPowerUp(weaponCard,playerPowerUpCards,player,e,0));
                        player.getPowerUp().removeAll(playerPowerUpCards);
                        model.powerUpDeck.getUsedPowerUp().addAll(playerPowerUpCards);
                        playerPowerUpCards.clear();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Couldn't shootOtherEffects.");
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
                    List<EffectAndNumber> list2 = new LinkedList<>(list);
                    list.clear();

                    for(int i=0;i<x;i++){
                        //CHIEDI UN EFFETTO
                        sendToClient("CHANGEORDER");
                        sendListToClient(fromEffectsAndNumberToNames(list2));
                        int z = (int) inputStream.readObject();
                        z--;
                        list.add(list2.remove(z));
                    }

                }

            }catch (Exception e){
                System.out.println("Couldn't change order of effects.");
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
                    playerPowerUpCards.clear();
                    return false;}
                lock.unlock();

                // CONTROLLA SE PUO RACCOGLIERE ALTRIMENTI RICHIEDI DROP ARMA, METTILA IN DCARD
                if(!player.canGrabWeapon()){
                    List<String> yourWeapons = new LinkedList<>();
                    synchronized (player.getHand()) {
                        for (WeaponCard w : player.getHand()) {
                            yourWeapons.add(w.toString());
                        }
                    }
                    lock.lock();
                    sendToClient("DROPWEAPON");
                    sendListToClient(yourWeapons); // RISPOSTA 1 O 2 O 3
                    int y = (int)inputStream.readObject();
                    y--;
                    WeaponCard weaponCard1 = player.getHand().remove(y);
                    weaponCard1.setNotReload();
                    weaponCard1.getPrice().get(0).setPaid(true);
                    s.getWeaponCards().add(weaponCard1);
                    lock.unlock();
                }

                lock.lock();


                if(z==1){
                    action.payAmmo(player,weaponCard, AmmoCube.Effect.BASE,0);
                    weaponCard.setReload();

                }else{
                    action.payPowerUp(weaponCard,playerPowerUpCards,player, AmmoCube.Effect.BASE,0);
                    weaponCard.setReload();
                }

                //////
                CoordinatesWithRoom spc = getSpawnpointCoordinates(s);
                action.grabWeapon(player,weaponCard, s, playerPowerUpCards, spc);
                //////

                lock.unlock();
                broadcast(player+" grabbed "+weaponCard.toString()+ " from Spawnpoint "+ s.getColor().toString());

            } catch (Exception e) {
                System.out.println("Couldn't grab from Spawnpoint.");
            }
        lock.unlock();
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
                    System.out.println("Couldn't pay with powerups.");
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
                                numberofActionsMinusOne(w);
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
                            List<CoordinatesWithRoom> cells1 = new LinkedList<>(cells);
                            cells.clear();
                            cells.add(chosen);

                            List<CoordinatesWithRoom> cellslist2 = w.getPossibleTargetCells(playerPosition,e,g);
                            cellslist2.removeAll(cells1);
                            if (!cellslist2.isEmpty()) {

                            sendToClient("CHOOSEANOTHER");
                            String response = (String) inputStream.readObject();
                            if(response.toUpperCase().equals("Y")){
                                    sendToClient("CHOOSECELL");
                                    sendListToClient(fromCellsToNames(cellslist2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                                    int xye = (int)inputStream.readObject();
                                    xye--;

                                    // CHECK FIRST (DISTANT 1) AND (SECOND DISTANT 2) SAME DIRECTION
                                    // IF NOT, SECOND NOT ADDED
                                    if(cells.get(0).checkSameDirection(cells.get(0),cellslist2.get(xye),10,g,false)) {
                                        cells.add(cellslist2.get(xye));
                                    }
                                }
                            }else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no cells.");
                            }

                            targets = w.fromCellsToTargets(cells,playerPosition,g,p,model,e);

                            if(e.getEffect()== AmmoCube.Effect.BASE){
                                List<Object> targets2=new LinkedList<>(targets);
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
                                        numberofActionsMinusOne(w);
                                    }
                                }
                            }
                            w.applyDamage(targets,p,e);
                            useTargetingScope(p,targets);
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                            numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                            numberofActionsMinusOne(w);
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
                            numberofActionsMinusOne(w);
                            return Collections.emptyList();
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
                            numberofActionsMinusOne(w);
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
                                List<Object> targ2 = new LinkedList<>(targets);
                                targets.clear();
                                targets.add(tr);
                                targ2.remove(tr);

                                if(!targ2.isEmpty()) {
                                    sendToClient("CHOOSEANOTHER");
                                    String rsp = (String) inputStream.readObject();
                                    if (rsp.toUpperCase().equals("Y")) {
                                        sendToClient("CHOOSETARGET");
                                        sendListToClient(fromTargetsToNames(targ2)); // RITORNA 1 OPPURE 2 OPPURE 3 ....
                                        int xpo = (int) inputStream.readObject();
                                        xpo--;
                                        targets.add(targ2.get(xpo));
                                    }
                                    w.applyDamage(targets, p, e);
                                    useTargetingScope(p, targets);
                                }
                            }else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no targets.");
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                    pastTargets2 = new LinkedList<>(pastTargets);    // ORIGINAL PASTTARGETS (I NEED THEM IF E.NUMBER!=1)
                                    pastTargets2.add(tr);

                                    pastTargets.clear();
                                    pastTargets.add(tr);
                                    w.applyDamage(pastTargets, p, e);
                                    useTargetingScope(p,pastTargets);
                                }
                            } else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no targets.");
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
                            }
                            lock.unlock();
                            return targets;
                        }
                        if(e.getEffect()== AmmoCube.Effect.OP2){
                            if(!pastTargets.isEmpty()) {
                                targets = new LinkedList<>(pastTargets);
                                w.applyDamage(targets, p, e);
                                useTargetingScope(p, targets);
                            }
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
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                    numberofActionsMinusOne(w);
                                }
                            }else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no cells.");
                                numberofActionsMinusOne(w);
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
                            List<Object> targets3 = new LinkedList<>(targets);
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
                            numberofActionsMinusOne(w);
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
                                        numberofActionsMinusOne(w);
                                    }
                                    return targets;
                                }
                            }else{
                                sendToClient("MESSAGE");
                                sendToClient("Sorry there are no targets.");
                                numberofActionsMinusOne(w);
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
                                    numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
                            }
                        }
                        break;

                    case "Shockwave":
                        lock.lock();
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
                                List<Object> list2 = new LinkedList<>(targets);
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
                                numberofActionsMinusOne(w);
                                return Collections.emptyList();
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
                            numberofActionsMinusOne(w);
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
                                    numberofActionsMinusOne(w);
                                }
                            }
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no targets.");
                            numberofActionsMinusOne(w);
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
                            numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                numberofActionsMinusOne(w);
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
                                List<Object> targets1 = new LinkedList<>(targets);
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
                                numberofActionsMinusOne(w);
                            }
                        }else{
                            sendToClient("MESSAGE");
                            sendToClient("Sorry there are no cells.");
                            numberofActionsMinusOne(w);
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
                            numberofActionsMinusOne(w);
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
                            List<Object> tar2 = new LinkedList<>(targets);
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
                            numberofActionsMinusOne(w);
                        }
                        lock.unlock();
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Couldn't shoot.");
                numberofActionsMinusOne(w);
                ex.printStackTrace();
            }
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

                        sendToClient("CUBE");
                        int r = (int) inputStream.readObject();
                        AmmoCube.CubeColor cube = AmmoCube.CubeColor.FREE;
                        if(r==1){ cube = AmmoCube.CubeColor.BLUE;}
                        if(r==2){ cube = AmmoCube.CubeColor.RED;}
                        if(r==3){ cube = AmmoCube.CubeColor.YELLOW;}

                        action.canPayTargetingScope(cube,p);

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
                    System.out.println("Couldn't use Targeting Scope.");
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

                                while (names.size()!=colorsChosen.size()){
                                    names.remove(names.size()-1);
                                }
                                setGameIsOn(true);

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

 private static class PlayerCountdown{
     final Timer timer = new Timer();
        public PlayerCountdown(RequestHandler handler){
            try {
                timer.scheduleAtFixedRate(new TimerTask() {
                    int i = time*20;

                    public void run() {
                        System.out.println(i--);
                        if (i< 0) {
                            System.out.println("------TIME'S UP FOR "+ handler.nickname);
                            timer.cancel();
                            System.out.println("          NEXT PLAYER");

                            if(lock.isHeldByCurrentThread()) {
                                while (lock.getHoldCount() > 0) {
                                    lock.unlock();
                                }
                            }

                            handler.disconnect();
                            handler.nextPlayer();
                            handler.flagFalse();
                        }
                    }
                }, 0, 1000);
            } catch (Exception e) {
                //
            }
        }
    }





    public boolean isBlank(String str) {
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
    public void setFrenzyChosen(){
        frenzy=true;
    }

    public static void setBoardChosen(int i){
        boardChosen = i;
    }

    public void createBoard(boolean frenzyChoice){
        model = new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,boardChosen, frenzyChoice);
        //model.startingMap();
        model.populateMap();
        //printSomeAmmos();
        action = new Action(model);
        freneticAction = new FreneticAction(model);
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

    public static void setGameIsOn(boolean state){
        gameIsOn=state;
    }
    public static void setEndgame(boolean state){
        endgame=state;
    }
}


