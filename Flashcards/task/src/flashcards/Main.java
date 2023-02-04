package flashcards;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static String option = "";

    private static String term = "";

    private static String definition = "";

    private static boolean nextIteration = true;

    private static String fileName = "";

    private static String logRecord = "";

    private static Map<String, String> flashCards = new LinkedHashMap<>();
    private static Map<String, Integer> mistakes = new LinkedHashMap<>();
    private static List<String> hardestCards = new ArrayList<>();

    private static String export = null;

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i += 2) {

            if ("-import".equals(args[i])) {
                fileName = args[i + 1];
                importCards();
            } else if ("-export".equals(args[i])) {
                export = args[i + 1];
            }
        }
        showOption();
    }

    private static void showOption() {

        String message = "";

        do {
            message = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
            System.out.println(message);
            logRecord += message + "\n";

            option = scanner.nextLine();
            logRecord += option + "\n";

            switch (option) {

                case "add":
                    message = "The card:";
                    System.out.println(message);
                    logRecord += message + "\n";

                    term = scanner.nextLine();
                    logRecord += term + "\n";

                    if (flashCards.containsKey(term)) {
                        message = String.format("The card \"%s\" already exists.%n%n", term);
                        System.out.printf(message);
                        logRecord += message;
                        break;
                    }

                    message = "The definition of the card:";
                    System.out.println(message);
                    logRecord += message + "\n";

                    definition = scanner.nextLine();
                    logRecord += definition + "\n";

                    if (flashCards.containsValue(definition)) {
                        message += String.format("The definition \"%s\" already exists.%n%n", definition);
                        System.out.printf(message);
                        logRecord += message;
                        break;
                    }

                    flashCards.put(term, definition);

                    message = String.format("The pair (\"%s\":\"%s\") has been added.%n%n", term, definition);
                    System.out.printf(message);
                    logRecord += message;
                    break;

                case "remove":
                    message = "Which card?";
                    System.out.println(message);
                    logRecord += message + "\n";

                    term = scanner.nextLine();
                    logRecord += term + "\n";

                    if (flashCards.containsKey(term)) {
                        flashCards.remove(term);
                        message = "The card has been removed.\n";
                        System.out.println(message);
                        logRecord += message;

                    } else {
                        message = String.format("Can't remove \"%s\": there is no such card.%n%n", term);
                        System.out.printf(message);
                        logRecord += message;
                    }

                    break;
                case "import":
                    System.out.println("File name:");
                    fileName = scanner.nextLine();
                    logRecord += "File name:\n" + fileName + "\n";
                    importCards();
                    break;
                case "export":
                    message = "File name:";
                    System.out.println(message);
                    fileName = scanner.nextLine();
                    logRecord += message + "\n" + fileName + "\n";
                    exportCards();
                    break;
                case "ask":
                    message = "How many times to ask?";
                    System.out.println(message);
                    logRecord += message + "\n";

                    int times = scanner.nextInt();
                    logRecord += times + "\n";
                    scanner.nextLine();

                    for (String terms : flashCards.keySet()) {
                        message += String.format("Print the definition of \"%s\":%n", terms);
                        System.out.printf(message);
                        String answer = scanner.nextLine();

                        logRecord += message;
                        logRecord += answer + "\n";

                        if (flashCards.containsValue(answer) && !flashCards.get(terms).equals(answer)) {
                            for (String key: flashCards.keySet()) {
                                if (flashCards.get(key).equals(answer)) {
                                    message = String.format("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".%n%n", flashCards.get(terms), key);
                                    System.out.printf(message);
                                    logRecord += String.format(message);
                                    mistakes.put(terms, (mistakes.get(terms) == null)? 0 :  mistakes.get(terms) + 1);
                                    break;
                                }
                            }
                        } else {

                            if (answer.equals(flashCards.get(terms))) {
                                System.out.println("Correct!");
                            } else {
                                System.out.println("Wrong. The right answer is \"" +
                                        flashCards.get(terms) + "\".\n");
                                mistakes.put(terms, (mistakes.get(terms) == null)? 1 :  mistakes.get(terms) + 1);
                            }

                            logRecord += answer.equals(flashCards.get(terms)) ?
                                    "Correct!\n" : "Wrong. The right answer is \"" +
                                    flashCards.get(terms) + "\".\n\n";
                        }

                        times--;

                        if (times == 0) {
                            break;
                        }
                    }

                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    if (export != null) {
                        fileName = export;
                        exportCards();
                    }
                    logRecord += "Bye bye!\n";
                    nextIteration = false;
                    break;
                case "log":
                    System.out.println("File name:");
                    logRecord += "File name:\n";

                    fileName = scanner.nextLine();
                    logRecord += fileName + "\n";

                    File file = new File(fileName);



                    System.out.println("The log has been saved.\n");
                    logRecord += "The log has been saved.\n\n";

                    try {
                        FileWriter writer = new FileWriter(fileName, false);
                        writer.write(logRecord.toString());
                        writer.close();

                        /*Scanner readingLog = new Scanner(file);
                        while (readingLog.hasNext()) {
                            System.out.println(readingLog.nextLine());
                        }*/

                    } catch (FileNotFoundException ex) {

                    } catch (IOException ex) {

                    }
                    break;
                case "hardest card":

                    hardestCards.clear();

                    int max = 0;

                    for (String key: mistakes.keySet()) {
                        if (mistakes.get(key) > max) {
                            max = mistakes.get(key);
                        }
                    }

                    for (String key: mistakes.keySet()) {
                        if (mistakes.get(key) == max) {
                            hardestCards.add(key);
                        }
                    }

                    message = "";

                    if (hardestCards.size() == 0) {
                        message = "There are no cards with errors.\n";
                        System.out.println(message);
                        logRecord += message + "\n";
                    } else if (hardestCards.size() == 1) {
                        message = String.format("The hardest card is \"%s\". You have %d errors answering it.\n\n",
                                hardestCards.get(0), max);
                        System.out.printf(message);
                        logRecord += message + "\n";
                    } else {
                        message = "The hardest cards are ";

                        for (int i = 0; i < hardestCards.size(); i++) {
                            message += String.format("\"%s\"", hardestCards.get(i));

                            if (i != hardestCards.size() -1) {
                                message += ", ";
                            } else {
                                message += ".";
                            }
                        }

                        int totalErrors = 0;

                        for (String key : mistakes.keySet()) {
                            totalErrors += mistakes.get(key);
                        }

                        message += String.format(" You have %d errors answering them.%n", totalErrors);

                        System.out.println(message);
                        logRecord += message + "\n";

                    }
                    break;
                case "reset stats":
                    hardestCards.clear();
                    mistakes.clear();

                    message = "Card statistics have been reset.";
                    System.out.println(message + "\n");
                    logRecord += message + "\n\n";
                    break;
            }

        } while (nextIteration);
    }

    private static void exportCards() {

        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file, false);
            writer.write(flashCards.toString());
            writer.write("\n");
            writer.write(mistakes.toString());
            writer.close();

            System.out.printf("%d cards have been saved.%n%n", flashCards.size());
            logRecord += String.format("%d cards have been saved.%n%n", flashCards.size());

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.\n");
            logRecord += "File not found.\n\n";
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void importCards() {

        try {
            File file = new File(fileName);
            Scanner readFile = new Scanner(file);
            String importedCards = "";
            String inpotedMistakes = "";
            String[] formattedCards;
            String[] formattedMistakes;

            int counterTerm = 0;

            while (readFile.hasNext()) {

                if (counterTerm++ == 0) {
                    importedCards = readFile.nextLine();
                } else {
                    inpotedMistakes = readFile.nextLine();
                }
            }

            formattedCards = formatDocument(importedCards);
            formattedMistakes = formatDocument(inpotedMistakes);

            int counter = 0;

            for (int i = 1; i < formattedCards.length; i += 2) {

                if (flashCards.containsKey(formattedCards[i-1])) {
                    flashCards.replace(formattedCards[i-1], formattedCards[i]);
                } else {
                    flashCards.put(formattedCards[i-1], formattedCards[i]);
                }

                counter++;
            }

            for (int i = 1; i < formattedMistakes.length; i += 2) {

                if (mistakes.containsKey(formattedMistakes[i-1])) {
                    mistakes.replace(formattedMistakes[i-1], Integer.parseInt(formattedMistakes[i]));
                } else {
                    mistakes.put(formattedMistakes[i-1], Integer.parseInt(formattedMistakes[i]));
                }
            }

            System.out.printf("%d cards have been loaded.%n%n", counter);
            logRecord += String.format("%d cards have been loaded.%n%n", counter);

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            logRecord += "File not found.\n";
        }
    }

    public static String[] formatDocument(String input) {
        return input.replace("{", "")
                .replace("}", "")
                .replaceAll("=", ",")
                .replaceAll(", ", ",")
                .split(",");
    }
}
