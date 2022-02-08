import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HiraganaKatakanaStudyTool {

    private static final char[] NAME_OF_COLUMN = {'a', 'k', 's', 't', 'n', 'h', 'm', 'y', 'r', 'w'};

    private static boolean hiragana;
    private static Map<String, Map<String, String>> singleKanaAnswerKey;
    private static ArrayList<String> userChosenColumns;
    private static ArrayList<String>[] wordKanaAnswerKey;
    private static boolean practice;
    private static ArrayList<String> wrongWords;


    public static void main(String[] args) throws FileNotFoundException {
        printIntro();
        practice();
        System.out.println("Thank you for studying! Good luck on the rest of your Japanese learning!");
    }

    /**
     * Prints out the beginning statements.
     */
    private static void printIntro() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("**** WELCOME TO HIRAGANA/KATAKANA PRACTICE ****");

        boolean whichKana = true;
        while (whichKana) {
            System.out.print("Will you be studying Hiragana or Katakana today? ");
            String kana = sc.next();
            if (kana.toUpperCase().equals("HIRAGANA")) {
                hiragana = true;
                whichKana = false;
            } else if (kana.toUpperCase().equals("KATAKANA")) {
                hiragana = false;
                whichKana = false;
            } else {
                System.out.println("Please enter either Hiragana or Katakana!");
            }
        }
        boolean cont = true;
        while (cont) {
            System.out.println("Would you like to practice or take a quiz?");
            System.out.print("Enter \"PRACTICE\" or \"QUIZ\": ");
            String input = sc.next();
            if (input.toUpperCase().equals("PRACTICE")) {
                System.out.println("Let's start practicing your alphabet!");
                practice = true;
                cont = false;
            } else if (input.toUpperCase().equals("QUIZ")) {
                System.out.println("Let's start quizzing then!");
                practice = false;
                cont = false;
            } else {
                System.out.println("That's not a valid input! Input \"PRACTICE\" or \"QUIZ\"");
            }
        }
    }

    /**
     * Starts the practice!
     */
    private static void practice() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        boolean cont = true;
        while (cont) {
            System.out.println("Would you like to go over only single letters or full words?");
            System.out.print("Enter \"SINGLE\" or \"WORDS\": ");
            String input = sc.next();
            if (input.toUpperCase().equals("SINGLE")) {
                practiceSingle();
                cont = false;
            } else if (input.toUpperCase().equals("WORDS")) {
                practiceWords();
                cont = false;
            } else {
                System.out.println("Please enter a valid input.");
            }
        }
    }

    /**
     * Sets up the practice for single hiragana.
     */
    private static void practiceSingle() throws FileNotFoundException {
        readSingleKana();
        Scanner sc = new Scanner(System.in);
        boolean playAgain = true;
        while (playAgain) {
            boolean cont = true;
            while (cont) {
                System.out.println("Would you like to look at specific columns or all of them?");
                System.out.print("Enter \"ALL\" or \"SPECIFIC\": ");
                String input = sc.next();
                userChosenColumns = new ArrayList<>();
                if (input.toUpperCase().equals("SPECIFIC")) {
                    getSpecificColumns(sc);
                    cont = false;
                } else if (input.toUpperCase().equals("ALL")) {
                    for (char c : NAME_OF_COLUMN) {
                        userChosenColumns.add(c + "");
                    }
                    cont = false;
                } else {
                    System.out.println("Please enter \"ALL\" or \"SPECIFIC\"...");
                }
            }
            playRoundSingle();
            System.out.println("Would you like to study a different set of columns?");
            System.out.print("Input Y for Yes, anything else for No: ");
            if (!sc.next().toUpperCase().equals("Y")) {
                playAgain = false;
            }
        }
    }

    private static void readSingleKana() throws FileNotFoundException {
        String fileName = "";
        if (hiragana) {
            fileName = "single_hiragana.txt";
        } else {
            fileName = "single_katakana.txt";
        }
        File f = new File(fileName);
        Scanner sc = new Scanner(f);
        singleKanaAnswerKey = new TreeMap<>();
        for (char columnLetter : NAME_OF_COLUMN) {
            int numKana;
            if (columnLetter == 'y' || columnLetter == 'w') {
                numKana = 3;
            } else {
                numKana = 5;
            }
            Map<String, String> answerKey = new TreeMap<>();
            for (int i = 0; i < numKana; i++) {
                String kana = sc.next();
                String engLetter = sc.next();
                answerKey.put(kana, engLetter);
            }
            singleKanaAnswerKey.put(columnLetter + "", answerKey);
        }
    }

    private static void getSpecificColumns(Scanner sc) {
        boolean contSpecific = true;
        while (contSpecific) {
            //must progress cursor to next line
            sc.nextLine();
            System.out.println("Please enter what columns you would like to study with a SPACE in between characters: ");
            System.out.print("The columns are a k s t n h m y r w: ");
            String userInputColumns = sc.nextLine();
            Scanner readSpecificInput = new Scanner(userInputColumns);
            while (readSpecificInput.hasNext()) {
                char column = readSpecificInput.next().toLowerCase().charAt(0);
                for (char c : NAME_OF_COLUMN) {
                    if (column == c && !userChosenColumns.contains(c + "")) {
                        userChosenColumns.add(column + "");
                    }
                }
            }
            if (userChosenColumns.isEmpty()) {
                System.out.println("Please enter valid column names!");
                System.out.println("Press enter to retry!");
            } else {
                System.out.print("You have chosen these columns: ");
                for (String column : userChosenColumns) {
                    System.out.print(column + " ");
                }
                System.out.println("\nAre these correct?");
                System.out.print("Enter Y for Yes, anything else for no: ");
                String check = sc.next();
                if (check.toUpperCase().equals("Y")) {
                    contSpecific = false;
                } else {
                    userChosenColumns.clear();
                    System.out.println();
                }
            }
        }
    }

    private static void playRoundSingle() {
        ArrayList<String> kanaList = new ArrayList<>();
        ArrayList<String> answerList = new ArrayList<>();
        for (String colName : userChosenColumns) {
            Map<String, String> answerKey = singleKanaAnswerKey.get(colName);
            for (String kana : answerKey.keySet()) {
                kanaList.add(kana);
                answerList.add(answerKey.get(kana));
            }
        }

        if (kanaList.size() != answerList.size()) {
            throw new IllegalStateException("ERROR ERROR ERROR! THE TWO LIST SIZES DO NOT MATCH!!!");
        }
        Scanner sc = new Scanner(System.in);
        boolean contGame = true;
        while (contGame) {
            beginGame(sc, kanaList, answerList, kanaList.size());
            if (!practice) {
                printQuizResults(kanaList.size());
            }
            System.out.println("Would you like to try again with the same columns?");
            System.out.print("Enter Y for Yes, anything else for No: ");
            if (!sc.next().toUpperCase().equals("Y")) {
                contGame = false;
            }
        }
    }

    private static void practiceWords() throws FileNotFoundException {
        readWordKana();
        Scanner sc = new Scanner(System.in);
        int numWords = wordKanaAnswerKey[0].size();
        int rounds = 0;
        boolean cont = true;

        while (cont) {
            System.out.println("How many words would you like to study? There are " + numWords + " words.");
            System.out.print("Enter how many words: ");
            int words = sc.nextInt();
            if (words < 0 || words > numWords) {
                System.out.println("That's not a valid number of words! Enter Again!");
            } else {
                rounds = words;
                beginGame(sc, wordKanaAnswerKey[0], wordKanaAnswerKey[1], rounds);
                System.out.println("Would you like to practice again with a different amount of words?");
                System.out.print("Y for Yes, anything else for No: ");
                if (!sc.next().toUpperCase().equals("Y")) {
                    cont = false;
                }
            }
        }

    }

    private static void readWordKana() throws FileNotFoundException {
        String fileName = "";
        if (hiragana) {
            fileName = "words_hiragana.txt";
        } else {
            fileName = "words_katakana.txt";
        }
        File f = new File(fileName);
        Scanner sc = new Scanner(f);

        ArrayList<String> kana = new ArrayList<>();
        ArrayList<String> word = new ArrayList<>();

        while (sc.hasNext()) {
            kana.add(sc.next());
            word.add(sc.next());
        }
        wordKanaAnswerKey = new ArrayList[2];
        wordKanaAnswerKey[0] = kana;
        wordKanaAnswerKey[1] = word;
    }

    private static void beginGame(Scanner sc, ArrayList<String> kanaList, ArrayList<String> answerList, int rounds) {
        System.out.println("\n**** Let's begin! ****");
        Set<Integer> indicesUsed = new TreeSet<>();
        Random r = new Random();
        int index = 0;
        wrongWords = new ArrayList<>();
        while (indicesUsed.size() != rounds) {
            int oldSize = indicesUsed.size();
            while (indicesUsed.size() == oldSize) {
                index = r.nextInt(kanaList.size());
                indicesUsed.add(index);
            }

            String kana = kanaList.get(index);
            String answer = answerList.get(index);
            boolean conGuess = true;
            while (conGuess) {
                System.out.print(kana + " : ");
                String userAnswer = sc.next();
                if (!userAnswer.toLowerCase().equals(answer)) {
                    if (practice) {
                        System.out.println("Wrong! Try Again!");
                    } else {
                        wrongWords.add(kana);
                        wrongWords.add(userAnswer);
                        wrongWords.add(answer);
                        conGuess = false;
                    }
                } else {
                    if (practice) {
                        System.out.println("Correct!");
                    }
                    conGuess = false;
                }
            }
        }
        System.out.println("That's all of them!");
        if (!practice) {
            printQuizResults(rounds);
        }
    }

    private static void printQuizResults(int size) {
        int numRight = size - (wrongWords.size() / 3);
        System.out.println("These are your quiz results: " + numRight + "/" + size);
        if (numRight != size) {
            System.out.println("These are the words you got wrong.");
            String kana = "";
            if (hiragana) {
                kana = "Hiragana: ";
            } else {
                kana = "Katakana: ";
            }
            for (int i = 0; i < wrongWords.size(); i += 3) {
                System.out.println(kana + wrongWords.get(i) + " | You Inputted: " + wrongWords.get(i + 1) + " | The correct answer: " + wrongWords.get(i + 2));
            }
        } else {
            System.out.println("You got them all correct! おめでとう！！！");
        }
    }
}


