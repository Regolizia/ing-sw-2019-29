package adrenaline;

// SOCKET
import java.io.*;
import java.util.*;
import java.util.stream.*;

// RMI

public class
Server {

    private static GameModel model;
    private static Action action;

    private static int currentPlayer = 0;
    private static boolean isFirstTurn = true;

    private static int time = 0;
    private static int connectionsCount = 0;
    private static int boardChosen = 0;

    // STRING LIST OF THE COLORS A PLAYER CAN CHOOSE AND LIST OF THOSE ALREADY CHOSEN
    private static List<String> possibleColors = Stream.of(Figure.PlayerColor.values())
            .map(Figure.PlayerColor::name)
            .collect(Collectors.toList());
    private static ArrayList<String> colorsChosen = new ArrayList<>();

    // All client names, so we can check for duplicates upon registration.
    private static ArrayList<String> names = new ArrayList<>();

    // IS THIS USEFUL?
    // The set of all the print writers for all the clients, used for broadcast.
    //private static List<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        time =Integer.parseInt(args[0]);
        possibleColors.remove("NONE");

        // TODO SEARCH FOR NEW CLIENTS
        // IF FOUND SET CONNECTIONSCOUNT++

      /*  System.out.println("The server is running...");
        var pool = Executors.newFixedThreadPool(500);
        try (var listener = new ServerSocket(59001)) {
           
            while (connectionsCount>=0) {
                pool.execute(new Handler(listener.accept()));
               
            }
        }*/
    }

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

                                Server.startGame();
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

    public void clientLogin(){
        String name = null;
        checkName(name);

        String color = null;
        checkColor(color);

        addPlayerToGame(name, color);

        chooseBoard(name); // it checks if is firstPlayer

    }

    public void checkName(String name){
        // TODO name = GET NAME FROM CLIENT

        // Keep requesting a name until we get a unique one.
        while (true) {
            if (name != null || !name.equals("null")) {
                synchronized (names) {
                    if (!isBlank(name) && !names.contains(name)) {
                        names.add(name);
                        break;
                    } else if (names.contains(name)) {
                        // TODO name = GET NAME BECAUSE DUPLICATE
                    }
                }
            }
        }
    }

    public void checkColor(String color){

        while (true) {
            String csv = String.join(", ", possibleColors);  // SEND POSSIBLE COLORS
            // TODO color = GET COLOR FROM CLIENT. MUST SEND csv TO CLIENT

            if (color != null) {
                synchronized (possibleColors) {
                    if (!possibleColors.isEmpty() && (possibleColors.contains(color.toUpperCase()) || possibleColors.contains(color))
                            && (colorsChosen.isEmpty() || (!colorsChosen.contains(color) && !colorsChosen.contains(color.toUpperCase())))) {
                        colorsChosen.add(color.toUpperCase());
                        possibleColors.remove(color.toUpperCase());
                        break;
                    } else if ((!possibleColors.contains(color.toUpperCase()) || !possibleColors.contains(color))
                            && !colorsChosen.contains(color.toUpperCase()) && !colorsChosen.contains(color)) {
                        // TODO ASK AGAIN FOR COLOR, IT WASN'T ACCEPTED

                    } else if (colorsChosen.contains(color.toUpperCase()) || colorsChosen.contains(color)) {
                        // TODO ASK AGAIN FOR COLOR, IT WAS A DUPLICATE COLOR
                    }
                }
            }
        }
    }

    public void addPlayerToGame(String name, String color){
        //model.addPlayer(new Player()); TODO

        // TODO BROADCAST NAME HAS JOINED
    }

    public void chooseBoard(String name){
        if(name.equals(model.getPlayers().get(0))){

            while(true){
                int result = 0;
                // TODO ASK FOR MAP 1-2-3-4

                if (result == 1 || result == 2 || result == 3 || result == 4) {
                    Server.setBoardChosen(result);
                    System.out.println("BOARD CHOSEN " + result);
                    break;
                } else {
                    // TODO ASK AGAIN BECAUSE NOT ACCEPTED
                }

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

    public static int isInteger(String input){
        try{
            int i = Integer.valueOf(input);
            return i;
        }catch(NumberFormatException e){
            return 0;
        }
    }
    public static void setBoardChosen(int i){
        boardChosen = i;
    }

    public static void startGame(){
        model = new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,boardChosen);
        action = new Action(model);

        // TODO SAY number boardChosen TO EVERYBODY
    }

    public void Turn(){
        int count;
        Player player = model.getPlayers().get(currentPlayer);

            if (isFirstTurn){
                LinkedList<PowerUpCard> twoCards= new LinkedList<>();
                twoCards.add(model.powerUpDeck.deck.removeFirst());
                twoCards.add(model.powerUpDeck.deck.removeFirst());

                // TODO CHIEDI QUALE CARTA DA TENERE (0) E QUALE DA USARE COME RESPAWN (Invertile se necessario))

                //action.firstTurn(model.getPlayers().get(currentPlayer, twoCards));

            }
            // TODO START TURN IN CURRENTPLAYER/CLIENT
        // choice (what action)

/*

       switch (choice) {
           case GRAB:
               grab(player);
               break;
           case RUN:
               run(player);
               break;
           case SHOOT:
               shoot(player);
               break;
       }
*/
        nextPlayer();
    }


    public void grab(Player player){
        // IF MI DICE GRAB
      //  LinkedList<CoordinatesWithRoom> possibleCells = action.proposeCellsGrab(player.getCoordinatesWithRooms(),model.getMapUsed().getGameBoard());
        // CHIEDI QUALE, RETURN chosenCell
        CoordinatesWithRoom chosenCell = null;

        if(chosenCell.containsSpawnpoint(model)) {
            /*
            * controllo io se può pagarla + ricarica ma tu devi:
            * 0-controllare che ti dica o effetto base o effetto alt nient'altro altrimenti non funziona il metodo
            * 1-controllare che abbia meno di 3 carte in mano
            * 2-una volta raccolta devi rimuoverla dalle carte dello spawnpoint
            * 3-una volta finito cio setta la posizione del player nel punto in cui ha scelto di raccogliere
            * se scarta una carta rimettila nel deck
            * */
            // cerca nel model se c'è uno spawnpoint lì o no
            //  TODO se spawn fai scegliere quale e mettila in card
            Spawnpoint s = chosenCell.getSpawnpoint(model);
            LinkedList<WeaponCard> weaponCards = s.getWeaponCards();
            // TODO SCEGLI CARTA, mettila in card
            WeaponCard card = null;

            // CHIEDI COME PAGARE AMMO O AMMOPOWER
            if (action.canPayCard(card, player, Action.PayOption.AMMO, AmmoCube.Effect.BASE)) {
                action.payAmmo(player, card, AmmoCube.Effect.BASE);

                if (player.canGrabWeapon()) {
                    // se va bene gliela passo
                    //action.grabCard(player, card, Action.PayOption.AMMOPOWER,model, AmmoCube.Effect.BASE,s);
                    // incrementa turno
                }
            }
            if (action.canPayCard(card, player, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE)) {
             //   action.payPower(player, card, AmmoCube.Effect.BASE, model);

                if (player.canGrabWeapon()) {
                    // se va bene gliela passo
                    //action.grabCard(player, card, Action.PayOption.AMMOPOWER,model, AmmoCube.Effect.BASE,s);
                    // incrementa turno
                }
            }
        }
        else {
            //se non spawnpoint
            /*
            * qui faccio direttamente io l'assegnazione della posizione del player  e rimuovo gia' l'ammotile
            * dalla mappa e ne aggiungo subito un altro
            * già incluso pescaggio power up
            * */
           // action.grabTile(player, chosenCell, model);
        }
    }

    public void run(Player player){
      //  LinkedList<CoordinatesWithRoom> cells = action.proposeCellsRun(player.getCoordinatesWithRooms(),model.getMapUsed().getGameBoard());
        // PROPONI LE CELLE E PRENDINE UNA
        CoordinatesWithRoom c = null;
        action.run(player,c);
    }

    public void shoot(Player player){
        /*
        * a meno di errori quando fai shoot controlla prima che la lista degli effetti pagati non sia nulla*/
        Action.PayOption payOption=null;

        //just in case he can't shoot
        LinkedList <EffectAndNumber> paidEffect=new LinkedList<>();
        CoordinatesWithRoom positionBeforeShoot=player.getCoordinatesWithRooms();
        //ask if the player wants to move before shooting
        // if yes
      //  LinkedList<CoordinatesWithRoom> possibleCells= action.proposeCellsRunBeforeShoot(player.getCoordinatesWithRooms(),model.getMapUsed().getGameBoard());
        //choose cell
        CoordinatesWithRoom playerPosition=null;
        //set new position
        action.run(player,playerPosition);
        //send player's hand , player chooses weapon
        WeaponCard weaponCard =null;
        //check if reload
      // if(weaponCard.getReloadAlt())
         //   paidEffect.add(0,AmmoCube.Effect.ALT,);
        // if(weaponCard.getReload())
        //   paidEffect.add(0,AmmoCube.Effect.BASE,);

        if(!weaponCard.getReloadAlt()&&!weaponCard.getReload()){
            //ask if he wants to pay BASE/ALT
            AmmoCube.Effect effect=null;
            //send message and delete the action
            if(!effect.equals(AmmoCube.Effect.BASE)&&!effect.equals(AmmoCube.Effect.ALT))
                return;
            //ask payment methods
            if(action.canPayCard(weaponCard,player,payOption,effect))
                {
                   // paidEffect.addAll(action.paidEffect(weaponCard,player,payOption,effect,model));
                    if(effect.equals(AmmoCube.Effect.BASE))
                        weaponCard.setReload();
                    if(effect.equals(AmmoCube.Effect.ALT))
                        weaponCard.setReloadAlt(true);
                }
            //if he can't delete action and send message
        }
        if(weaponCard.getReload()||weaponCard.getReloadAlt()){
            //ask if he wants to add more effects
            LinkedList<AmmoCube.Effect> effect=null;
            //ask payment option can be different for each effect
            for (AmmoCube.Effect e:effect
            ) {

                if(!effect.equals( AmmoCube.Effect.ALT)&&!effect.equals(AmmoCube.Effect.BASE)&&
                        action.canPayCard(weaponCard,player,payOption,e)){
               //     paidEffect.addAll(action.paidEffect(weaponCard,player,payOption,e));
                }
            }
            //send possible target
            for (EffectAndNumber e:paidEffect
                 ) {
                weaponCard.getPossibleTargetCells(playerPosition,e,model.getMapUsed().getGameBoard());
                //chose effective target
                //check if a player || spawnpoint (<--- if we choose this alternative)
                Object victim=null;
              //  action.shoot(weaponCard,playerPosition,player,paidEffect,model, model.getMapUsed().getGameBoard());
                //todo add vixtim to shoot
            }
        }
        weaponCard.setNotReload();
        weaponCard.setReloadAlt(false);
    }



    /**
     * Updates index of next Player.
     * If everybody has played, it resets.
     */
    public static void nextPlayer(){
        currentPlayer++;
        if(currentPlayer==model.getPlayers().size()) {
            currentPlayer = 0;
        isFirstTurn= false;
        }
    }}


