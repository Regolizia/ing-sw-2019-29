public class Mode {
    protected enum mode {
        classic,
        domination,
        terminator
    };

    mode usedMode;


    public Mode(String choosenMode){

        if(choosenMode=="classic")
            usedMode=mode.classic;
        if(choosenMode=="domination")
            usedMode=mode.domination;
        if(choosenMode=="terminator")
            usedMode=mode.terminator;
    }
    //to do multiple games options
    public String getMode(){
        if(usedMode==mode.classic)
            return "classic";
        if(usedMode==mode.domination)
            return "domination";
        if(usedMode==mode.classic)
            return "domination";
    return "";
    }
}
