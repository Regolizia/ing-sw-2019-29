package adrenaline;

public class Wall {

        private Room room1;
        private Room room2;
        private Coordinates c1;
        private Coordinates c2;

        public Wall(){

        }

        public Wall(Room r1, int x1, int y1, Room r2, int x2, int y2) {
            this.room1 = r1;
            this.room2 = r2;
            this.c1 = new Coordinates(x1,y1);
            this.c2 = new Coordinates(x2,y2);
        }

        public Room getRoom1(){
            return this.room1;
        }
        public Room getRoom2(){
            return this.room2;
        }
        public Coordinates getCoordinates1(){
            return c1;
        }
        public Coordinates getCoordinates2(){
            return c2;
        }



        // PASSAGE BETWEEN ROOM1 TO ROOM2
        // 1 -> NS
        // 2 -> SN
        // 3 -> WE
        // 4 -> EW
        public int hasDirection(){

            if(c1.getY()==room2.getRoomSizeY() && c2.getY()==1) {
                return 1;   // FROM N TO S
            }
            if(c2.getY()==room2.getRoomSizeY() && c1.getY()==1) {
                return 2;   // FROM S TO N
            }
            if(c2.getX()==1 && c1.getX()==room1.getRoomSizeX()) {
                return 3;    // FROM W TO E
            }
            if(c1.getX()==1 && c2.getX()==room2.getRoomSizeX()) {
                return 4;   // FROM E TO W
            }
            return 42;

        }






    }
