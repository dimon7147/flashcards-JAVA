package flashcards;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FlashCardGame {

    private final HashMap<Integer, Question> questions = new HashMap<>();
    private final Logger logger = new Logger();

    public void start(){
        menu();
        logger.logAndPrintLn("Bye bye!");
    }

    private void menu(){
        var scanner = new Scanner(System.in);
        logger.logAndPrintLn("\nInput the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        var res = scanner.nextLine();
        logger.log("> "+ res + "\n");
        switch (res){
            case "add": {
                addQuestions(1);
                break;
            }
            case "remove": {
                logger.logAndPrintLn("The card:");
                var card = scanner.nextLine();
                logger.log("> " + card + "\n");
                remove(card);
                break;
            }
            case "import": {
                logger.logAndPrintLn("File name:");
                var file = scanner.next();
                logger.log("> " + file + "\n");
                readFromFile(file);
                break;
            }
            case "export": {
                logger.logAndPrintLn("File name:");
                var file = scanner.next();
                logger.log("> " + file + "\n");
                writeToFile(file);
                break;
            }
            case "ask": {
                logger.logAndPrintLn("How many times to ask?");
                var times = scanner.nextInt();
                logger.log("> " + times + "\n");
                askQuestions(times);
                break;
            }
            case "log": {
                logger.logAndPrintLn("File name:");
                var file = scanner.next();
                logger.log("> " + file + "\n");
                logger.setPath(file);
                logger.exportToFile();
                break;
            }
            case "hardest card": {
                var array = findHardest();
                if (array.size() == 1){
                    if (array.get(0) == -1){
                        logger.logAndPrintLn("There are no cards with errors.");
                        break;
                    }
                    logger.logAndPrintLn("The hardest card is \"" + questions.get(array.get(0)).getQuestion() + "\". You have " + questions.get(array.get(0)).getComplexity() + " errors answering it.");
                    break;
                }
                logger.logAndPrint("The hardest cards are ");
                for (int i = 0; i < array.size(); i++){
                    if (i == array.size()-1){
                        logger.logAndPrint("\"" + questions.get(array.get(i)).getQuestion() + "\". ");
                    }
                    else logger.logAndPrint("\"" + questions.get(array.get(i)).getQuestion() + "\", ");
                }
                logger.logAndPrintLn("You have " + questions.get(array.get(0)).getComplexity() + " errors answering them.");
                break;
            }
            case "reset stats": {
                resetComplexity();
                logger.logAndPrintLn("Card statistics has been reset.");
                break;
            }
            case "exit":{
                return;
            }
        }
        menu();
    }

    private void resetComplexity(){
        for (var el : questions.keySet()){
            questions.get(el).resetComplexity();
        }
    }

    private ArrayList<Integer> findHardest(){
        if (questions.size() == 0) return new ArrayList<>(List.of(-1));
        var arr = new ArrayList<Integer>();
        for (var el : questions.values()){
            arr.add(el.getComplexity());
        }
        Collections.sort(arr);
        int max = arr.get(arr.size()-1);
        if (max == 0) return new ArrayList<>(List.of(-1));
        var result = new ArrayList<Integer>();
        for (var el : questions.keySet()){
            if (questions.get(el).getComplexity() == max){
                result.add(el);
            }
        }
        return result;
    }

    private void readFromFile(String path){
        try(FileReader reader = new FileReader(path)) {
            var scan = new Scanner(reader);
            var loaded = 0;
            while (scan.hasNextLine()){
                var line = scan.nextLine().split("=~=");
                addQuestion(new Question(line[0], line[1], Integer.parseInt(line[2])));
                loaded++;
            }
            logger.logAndPrintLn(loaded + " cards have been loaded.");
        } catch(IOException ex){
            logger.logAndPrintLn("File not found.");
        }
    }

    //Print all questions for testing.
    private void printAll(){
        for (var item : questions.values()){
            logger.logAndPrintLn(item.getQuestion() + " " + item.getAnswer());
        }
    }

    private void writeToFile(String path){
        try(FileWriter writer = new FileWriter(path, false)) {
            var exported = 0;
            for (var el : questions.values()){
                writer.write(el.getQuestion() + "=~=" + el.getAnswer() + "=~=" + el.getComplexity() + "\n");
                exported++;
            }
            writer.flush();
            logger.logAndPrintLn(exported + " cards have been saved.");
        } catch(IOException ex){
            logger.logAndPrintLn("File not found.");
        }
    }

    private void askQuestions(int amount){
        var scanner = new Scanner(System.in);
        var random = new Random(questions.size());
        var RandomN = 0;
        for (int i = 0; i < amount; i++){
            do {
                RandomN = random.nextInt(questions.size());
            } while (!questions.containsKey(RandomN));
            logger.logAndPrintLn("Print the definition of \"" + questions.get(RandomN).getQuestion() + "\":");
            var answer = scanner.nextLine();
            logger.log("> " + answer + "\n");
            if (isCorrect(questions.get(RandomN), answer)){
                logger.logAndPrintLn("Correct answer.");
            } else {
                var id = getIdByQuestionAnswer(answer);
                questions.get(RandomN).increaseComplexity();
                if (id == -1){
                    logger.logAndPrintLn("Wrong answer. The correct one is \"" + questions.get(RandomN).getAnswer() + "\".");
                } else {
                    logger.logAndPrintLn("Wrong answer. The correct one is \"" + questions.get(RandomN).getAnswer() + "\"," +
                            " you've just written the definition of \""+questions.get(id).getQuestion()+"\".");
                }
            }
        }
    }

    private int findFreeId(){
        for (int i = 0; i <= questions.size(); i++){
            if (questions.containsKey(i)) continue;
            return i;
        }
        return -1;
    }

    private int getIdByQuestionAnswer(String answer){
        for (var value : questions.keySet()){
            if (questions.get(value).getAnswer().toLowerCase().equals(answer.toLowerCase())){
                return value;
            }
        }
        return -1;
    }

    private int getIdByQuestion(String name){
        for (var value : questions.keySet()){
            if (questions.get(value).getQuestion().toLowerCase().equals(name.toLowerCase())){
                return value;
            }
        }
        return -1;
    }

    private boolean isUniqueQuestionName(String name){
        for (var value : questions.values()){
            if (value.getQuestion().toLowerCase().equals(name.toLowerCase())){
                return false;
            }
        }
        return true;
    }

    private boolean isUniqueQuestionAnswer(String name){
        for (var value : questions.values()){
            if (value.getAnswer().toLowerCase().equals(name.toLowerCase())){
                return false;
            }
        }
        return true;
    }

    private void remove(String name){
        if (isUniqueQuestionName(name)){
            logger.logAndPrintLn("Can't remove \""+name+"\": there is no such card.");
            return;
        }
        questions.remove(getIdByQuestion(name));
        logger.logAndPrintLn("The card has been removed.");
    }

    private void addQuestion(Question question){
        if (isUniqueQuestionName(question.getQuestion())) {
            questions.put(findFreeId(),question);
        } else {
            var key = getIdByQuestion(question.getQuestion());
            questions.replace(key, question);
        }
    }

    private void addQuestions(int amount){
        var scanner = new Scanner(System.in);
        for (int i = 1; i <= amount; i++){
            logger.logAndPrintLn("The card:");
            var question = scanner.nextLine();
            logger.log("> " + question + "\n");
            if (!isUniqueQuestionName(question)){
                logger.logAndPrintLn("The card \"" + question + "\" already exists.");
                return;
            }
            logger.logAndPrintLn("The definition of the card:");
            var answer = scanner.nextLine();
            logger.log("> " + answer + "\n");
            if (!isUniqueQuestionAnswer(answer)){
                logger.logAndPrintLn("The definition \"" + answer + "\" already exists.");
                return;
            }
            addQuestion(new Question(question,answer, 0));
            logger.logAndPrintLn("The pair (\""+question+"\":\""+answer+"\") has been added.");
        }
    }

    private boolean isCorrect(Question question, String answer){
        return question.getAnswer().toLowerCase().equals(answer.toLowerCase());
    }
}
