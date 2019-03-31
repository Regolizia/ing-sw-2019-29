public class Mode {
    protected enum mode {
        classic,
        domination,
        terminatorClassic,
        terminatorDomination
    };

    mode usedMode;


    public Mode(String choosenMode, boolean terminator){

        if(choosenMode=="classic"&&!terminator)
            usedMode=mode.classic;
        if(choosenMode=="domination"&&!terminator)
            usedMode=mode.domination;
        if(choosenMode=="classic"&&terminator)
            usedMode=mode.terminatorClassic;
        if(choosenMode=="domination"&&terminator)
            usedMode=mode.terminatorDomination;
    }
    //to do multiple games options
    public String getMode(){
        if(usedMode==mode.classic)
            return "classic";
        if(usedMode==mode.domination)
            return "domination";
        if(usedMode==mode.terminatorClassic)
            return "tClassic";
        if(usedMode==mode.terminatorDomination)
            return "tDomination";
    return "";
    }
}
