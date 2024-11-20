
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void searchMenu(List<Superbowl> superbowlData) { // menu to allow user to select between search modes
        Scanner scn = new Scanner(System.in);
        int usrIn;
        boolean loop = true;
        System.out.println("-----------------------\n"
                + "Search superbowls by:\n"
                + "-----------------------\n"
                + "Team .................1\n"
                + "State.................2\n"
                + "Main menu.............0\n"
                + "-----------------------\n"
                + "Enter choice:> ");
        while (loop)

        try {
            usrIn = Integer.valueOf(scn.nextLine().trim());
            switch (usrIn) {
                case 0:
                    loop = false;
                    break;

                case 1:
                    loop = false;
                    searchWrapper(superbowlData, SearchType.TEAM);
                    break;

                case 2:
                    loop = false;
                    searchWrapper(superbowlData, SearchType.STATE);
                    break;

                default:
                    System.out.println("Please give a valid input \n>");
                    ;
            }

        } catch (Exception e) {
            System.out.println("Error, bad input");
            System.out.println(e.getMessage());
        }

    }

    public static void select(List<Superbowl> SuperbowlData) { // outputs toString of a user selected year to console
        int year;
        Scanner scn = new Scanner(System.in);
        boolean loop = true;

        System.out.println("Enter championship year > ");
        while (loop) {
            try {
                year = Integer.valueOf(scn.nextLine().trim());
                if (year >= 1967 && year <= 2024) {
                    System.out.println(SuperbowlData.get(year - 1967).toString());
                    loop = false;
                }
            } catch (Exception e) {
                System.out.println("Error, enter a valid year\n > ");
            }

        }
    }

    public static void printMenu() { // just prints the menu
        System.out.println("-----------------------\n"
                + " NFL Superbowls menu \n"
                + "-----------------------\n"
                + "List .................1\n"
                + "Select ...............2\n"
                + "Search................3\n"
                + "Exit..................0\n"
                + "-----------------------\n"
                + "Enter choice:> ");
    }

    public static void list(List<Superbowl> superbowlData) { // lists superbowls between 2 user given dates
        Scanner scn = new Scanner(System.in);
        boolean loop = true;
        int startYear = 0, endYear = 0;

        while (loop) { // loops until correct data is given

            System.out.print("Enter start year > ");

            try {
                startYear = Integer.valueOf(scn.nextLine());
            } catch (Exception e) {
                System.out.println("Error, invalid date given \n");
                loop = true;
            }

            System.out.println("Enter end year >");

            try {
                endYear = Integer.valueOf(scn.nextLine());
                loop = false;
            } catch (Exception e) {
                System.out.println("Error, invalid date given \n");
                loop = true;
            }

            if ((startYear < 1967) || (endYear > 2024)) { // making sure that input is within the correct region
                loop = true;
                System.out.println("Error, invalid date given \n");
            }
        }

        int startIndex = startYear - 1967;
        int endIndex = endYear - 1967;
        System.out.println("----------------------------------------------------------------------------------------\n"
                + "| Year | Superbowl No. | Champions                     | Runners-up                    |\n"
                + "----------------------------------------------------------------------------------------");
        for (; startIndex <= endIndex; startIndex++) { // wonky for loop, iterates through superbowlData (list) from startIndex (starting year) and calls listData on each of them up until endIndex (ending year)
            System.out.println(superbowlData.get(startIndex).listData());
        }
        System.out.println("----------------------------------------------------------------------------------------");

    }

    public static void searchWrapper(List<Superbowl> superbowlData, SearchType type) { // wrapper func for search
        String usrIn = getUserInput(type);
        List<Superbowl> matches = new ArrayList();

        for (Superbowl bowl : superbowlData) {
            String searchString = (type.equals(SearchType.STATE)) ? bowl.getStateSearchString() // gets search string based on search type
                    : bowl.getTeamSearchString();
            if (search(searchString, usrIn)) { // compares the search string with user input to prune the total bowls to ones that dont contain the substring
                matches.add(bowl);
            }
        }

        if (matches.isEmpty()) { // error message if nothing is found
            System.out.println("| Error, no matches found for the given search term.");
        } else {
            printResults(matches, usrIn, type);
        }

        System.out.print("-------------------------------------------------------------------------------------------------\n");
    }

    private static String getUserInput(SearchType type) { // gets user input based on search type
        Scanner scn = new Scanner(System.in);
        String prompt = (type.equals(SearchType.STATE)) ? "Enter search term for U.S. state (e.g., Florida) >" // sets the prompt based on the search type
                : "Enter search term for NFL team (e.g., Giants) > ";
        System.out.println(prompt);

        return scn.nextLine().trim(); // returns trimmed user input
    }

    private static Boolean search(String searchString, String usrIn) { // searching func, returns bool based on if it was found or not WIP: not matching substrings
        String[] fields = searchString.split("\\|");

        for (String field : fields) {
            Pattern pattern = Pattern.compile(Pattern.quote(usrIn.trim()), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(field.trim());
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    private static void printResults(List<Superbowl> matches, String usrIn, SearchType type) { // outputs results of search
        boolean isFirstRow = true;
        String header = (type.equals(SearchType.STATE)) ? "| State                          | Superbowl         | City & Stadium                           |" //31, 18, 41 chars respectively 
                : "| Team                           | No. Appearances   | Details                                  |";
        StringBuilder sb = new StringBuilder();
        String[] currentBowlData;

        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println(header);
        System.out.println("-------------------------------------------------------------------------------------------------");

        if (type.equals(SearchType.STATE)) {
            for (Superbowl bowl : matches) {
                currentBowlData = bowl.getStateSearchString().split("\\|");
                if (currentBowlData[0].equalsIgnoreCase(usrIn)) {
                    formatStateResult(currentBowlData, isFirstRow);
                }
                isFirstRow = false; // updates flag to no longer reprint unwanted info
            }
        } else {
            String[] teams;
            String foundteam = ""; // as the users search is likely not 100% accurate to the name the actual name must be found
            int appearanceCount = matches.size();

            for (Superbowl bowl : matches) {
                teams = bowl.getTeamSearchString().trim().split("\\|");
                if (compareTeam(teams[0], usrIn) || compareTeam(teams[1], usrIn)) { // final check to make sure it is the correct team as the matcher and regex funcs sometimes give false positives
                    foundteam = getTeamName(teams, usrIn); // saves correct team name
                    formatTeamResult(foundteam, appearanceCount, bowl, isFirstRow);
                }
                isFirstRow = false; // same as previously
            }

        }
    }

    private static void formatStateResult(String[] currentBowlData, boolean isFirstRow) { // prints the state search result
        StringBuilder sb = new StringBuilder();

        sb.append("| ");
        
        if (isFirstRow){
            sb.append(currentBowlData[0]); // state
            sb.append(Superbowl.addPadding(31 - currentBowlData[0].length())); // padding based on char width of each column, as shown in header of printResults
        }
        else {
            sb.append(Superbowl.addPadding(31));
        }
        
        sb.append("| ").append(currentBowlData[2]); // superbowl num
        sb.append(Superbowl.addPadding(18 - currentBowlData[2].length()));
        sb.append("| ").append(currentBowlData[1]); // city and stadium
        sb.append(Superbowl.addPadding(41 - currentBowlData[1].length()));
        sb.append("|");
        System.out.println(sb.toString());
    }

    private static void formatTeamResult(String team, int count, Superbowl bowl, boolean isFirstRow) { //  prints the team search result
        String[] details = bowl.getTeamOutputString().split("\\|");
        StringBuilder sb = new StringBuilder();
        
        sb.append("| ");

        if (isFirstRow) { // to only print the team name and num of appearances once
            sb.append(team);
            sb.append(Superbowl.addPadding(31 - team.length())); // padding based on previously mentioned width of columns
            sb.append("| ");
            sb.append(count);
            sb.append(Superbowl.addPadding(18 - String.valueOf(count).length()));
        } else {
            sb.append(Superbowl.addPadding(31)); 
            sb.append("| ");
            sb.append(Superbowl.addPadding(18)); 
        }
        
        sb.append("| ");
        sb.append(details[0]);

        int charsLong = details[0].length(); // checks if team won or lost
        if (team.equalsIgnoreCase(details[1])) {
            sb.append(" Winner");
            charsLong += 6;
        } else {
            sb.append(" Runner-up");
            charsLong += 9;
        }

        sb.append(Superbowl.addPadding(40 - charsLong));
        sb.append("|");
        System.out.println(sb.toString());
    }

    private static boolean compareTeam(String team, String usrIn) { // helper method for searchTeamWrapper
        return team.toLowerCase().contains(usrIn.toLowerCase());
    }

    private static String getTeamName(String[] teams, String usrIn) { // helper method for searchTeamWrapper, returns which of the teams is the correct one
        if (teams[0].toLowerCase().contains(usrIn.toLowerCase())) {
            return teams[0];
        } else if (teams[1].toLowerCase().contains(usrIn.toLowerCase())) {
            return teams[1];
        }

        return ""; // base case if neither team matches

    }

    public static void main() {
        File file = new File("superbowls.txt"); // defining path to file
        List<Superbowl> superbowlData = new ArrayList<>();
        Scanner scn = new Scanner(System.in);
        String line;
        boolean loop = true;

        // -- initial setup for file -- 
        try (Scanner fileIn = new Scanner(file)) { // reads data from file into superbowlData
            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                superbowlData.add(new Superbowl(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // -- loop containing control flow of program -- 
        while (loop) {
            printMenu();
            try {
                line = scn.nextLine().trim(); // reusing line variable to act as a buffer
                switch (Integer.valueOf(line)) {
                    case 0:
                        loop = false;
                        break;

                    case 1:
                        list(superbowlData);
                        break;

                    case 2:
                        select(superbowlData);
                        break;

                    case 3:
                        searchMenu(superbowlData);
                        break;

                    default:
                        System.out.println("Please give a valid input \n>");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error, bad input");
                System.out.println(e.getMessage());
            }

        }
    }

}
