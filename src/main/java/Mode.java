public class Mode {
    protected enum mode {
        classic,
        domination
    };

    mode usedMode;


    public Mode(String choosenMode){

        if(choosenMode=="classic")
            usedMode=mode.classic;
        if(choosenMode=="domination")
            usedMode=mode.domination;
         }

    //to do multiple games options
    public String getMode(){
        if(usedMode==mode.classic)
            return "classic";
        if(usedMode==mode.domination)
            return "domination";
    return "";
    }
}
