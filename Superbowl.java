



class Superbowl{
    private final int year;
    private final String date;
    private final String superbowlNumber;
    private final String winningTeam;
    private final int winningPoints;
    private final String losingTeam;
    private final int losingPoints;
    private final String mvp;
    private final String stadium;
    private final String city;
    private final String state;

    public Superbowl(int y, String d, String sN, String wT, int wP, String lT,int lP, String mvp, String s, String c, String st ) { // constructor for creating a custom superbowl not from data file, dont really need this mostly just for testing
        this.year = y;
        this.date = d;
        this.superbowlNumber = sN;
        this.winningTeam = wT;
        this.losingTeam = lT;
        this.winningPoints = wP;
        this.losingPoints = lP;
        this.mvp = mvp;
        this.city = c;
        this.stadium = st;
        this.state = s;
    }

    public Superbowl (String rawData){ // constructor to take in raw data from the file
     String[] data = rawData.split("\\|"); // splits data along the | char into an array, then uses indexes of array to find relevant data

     this.year = Integer.valueOf(data[0].substring(0, 4));
     this.date = data[0];
     this.superbowlNumber = data[1];
     this.winningTeam = data[2];
     this.winningPoints =  Integer.valueOf( data[3]);
     this.losingTeam = data [4];
     this.losingPoints = Integer.valueOf(data[5]);
     this.mvp = data[6];
     this.stadium = data[7];
     this.city = data [8];
     this.state = data[9];
        
    }

    public String getStateSearchString(){ // returns small string containing state and teams that played split by "|", in order to match against search query
        StringBuilder sb = new StringBuilder();

        sb.append(this.state);
        sb.append("|");
        sb.append(this.city);
        sb.append(", ");
        sb.append(this.stadium);
        sb.append("|");
        sb.append(this.superbowlNumber);

        return sb.toString();
    }

    public String getTeamSearchString(){ // same as above but returns details for teams instead of states
        StringBuilder sb = new StringBuilder();

        sb.append(this.winningTeam);
        sb.append("|");
        sb.append(this.losingTeam);

        return sb.toString();
    }

    public String getTeamOutputString(){ // returns necessary data for team search output table
        StringBuilder sb = new StringBuilder();
        sb.append(this.year);
        sb.append(" (");
        sb.append(this.superbowlNumber);
        sb.append("), ");
        sb.append("|");
        sb.append(this.winningTeam);
        sb.append("|");
        sb.append(this.losingTeam);

        return sb.toString();
    }

    public String listData(){ // returns its data as a small string to act as a row in a table
        String outStr;
        StringBuilder sb = new StringBuilder();
 
        sb.append("| " + this.year + " | ");

        outStr = this.superbowlNumber;
        sb.append(outStr);
        sb.append(addPadding(outStr, 13)); // adds padding to column
        sb.append(" | ");

        outStr = this.winningTeam;
        sb.append(outStr);
        sb.append(addPadding(outStr, 29));
        sb.append(" | ");

        outStr = this.losingTeam;
        sb.append(outStr);
        sb.append(addPadding(outStr, 29));
        sb.append(" | ");

        outStr = sb.toString();
        return outStr;
        
    }

    public String toString(){ // toString implementation to output full info about this superbowl
        int ammountOfPadding = 0;
        StringBuilder sb = new StringBuilder();
        String bufferString;

        sb.append("------------------------------------------------------------------------------------------\n"); //90 chars long
        sb.append("|                                                                                        |\n");

        // header

        // outStr = "Superbowl "; // 10 chars long
        bufferString = "Superbowl " + this.superbowlNumber;
        ammountOfPadding = (90 - bufferString.length()) - 2; // calculating the total amount of padding needed, -2 for the pipes at beginning and end

        sb.append("|");
        sb.append(addPadding( Math.floorDiv(ammountOfPadding, 2))); // adds half the padding before the words to center it
        sb.append("Superbowl ");
        sb.append(this.superbowlNumber);
        sb.append(addPadding( Math.ceilDiv(ammountOfPadding, 2))); // floor on one and ceil on other to deal with uneven lengths
        sb.append("| \n");

        // venue

        // "Venue:  in , " - 13 chars without stadium city or state
        bufferString = "Venue: " + this.stadium + " in " + this.city + ", " + this.state;
        ammountOfPadding = (90 - bufferString.length()) - 2;

        sb.append("|");
        sb.append(addPadding(Math.floorDiv(ammountOfPadding, 2)));
        sb.append("Venue: ");
        sb.append(this.stadium);
        sb.append(" in ");
        sb.append(this.city);
        sb.append(", ");
        sb.append(this.state);
        sb.append(addPadding(Math.ceilDiv(ammountOfPadding, 2))); 
        sb.append("|\n");

        // outcome
        // "The  beat the  by  points to  points" - 36 chars
        bufferString = "The " + this.winningTeam + " beat the " + this.losingTeam + " by " + this.winningPoints + " points to " + this.losingPoints + " points";
        ammountOfPadding = 97 - bufferString.length() - 2; // absolutely ZERO idea as to why it needs to be 97 instead of the same 90 as everywhere else. i have spent 4 hours trying to figure out why and i just dont know anymore. whatever it works with 97 fuck it 

        sb.append("|");
        sb.append(addPadding(Math.floorDiv(ammountOfPadding, 2)));
        sb.append("The ");
        sb.append(this.winningTeam);
        sb.append((" beat the "));
        sb.append(this.losingTeam);
        sb.append(" by ");
        sb.append(this.winningPoints);
        sb.append(" to ");
        sb.append(this.losingPoints);
        sb.append(" points");
        sb.append(addPadding(Math.ceilDiv(ammountOfPadding, 2))); 
        sb.append("|\n");


        //mvp
        // "The most valuable player award went to  " - 39 chars
        bufferString = "The most valuable player award went to " + this.mvp;
        ammountOfPadding = ( 90 - bufferString.length() - 2);

        sb.append("|");
        sb.append(addPadding(Math.floorDiv(ammountOfPadding, 2)));
        sb.append("The most valuable player award went to ");
        sb.append(this.mvp);
        sb.append(addPadding(Math.ceilDiv(ammountOfPadding, 2)));
        sb.append("|\n");

        sb.append("|                                                                                        |\n");
        sb.append("------------------------------------------------------------------------------------------\n");

        return sb.toString();
    }

    private String addPadding(String toPadd, int padNum){ // takes string and target string length and pads until target length is reached
        StringBuilder sb = new StringBuilder();
        
        for (int i = toPadd.length(); i < padNum; i++){ // fills rest of column w padding
            sb.append(" ");
        }

        return sb.toString();
    }

    public static String addPadding(int charsOfPadding){ // overloaded version to ignore input strings and just return a set amount of padding, public and static so that it can be used by the main class
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < charsOfPadding; i++){
            sb.append(" ");
        }

        return sb.toString();
    }

}