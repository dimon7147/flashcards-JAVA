package flashcards;

import java.util.HashMap;
import java.util.Scanner;

public class FlashCardGame {

    private final HashMap<Integer, Question> questions = new HashMap<>();

    public void start(){
        var scanner = new Scanner(System.in);
        System.out.println("Input the number of cards:");
        addQuestions(scanner.nextInt());
        askQuestions(questions.size());
    }

    private void askQuestions(int amount){
        var scanner = new Scanner(System.in);
        for (int i = 0; i < amount; i++){
            System.out.println("Print the definition of \"" + questions.get(i).getQuestion() + "\":");
            var answer = scanner.nextLine();
            if (isCorrect(questions.get(i), answer)){
                System.out.println("Correct answer.");
            } else {
                var id = getIdByQuestionAnswer(answer);
                if (id == -1){
                    System.out.println("Wrong answer. The correct one is \"" + questions.get(i).getAnswer() + "\".");
                } else {
                    System.out.println("Wrong answer. The correct one is \"" + questions.get(i).getAnswer() + "\"," +
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

    private void addQuestion(Question question){
        questions.put(findFreeId(),question);
    }

    private void addQuestions(int amount){
        var scanner = new Scanner(System.in);
        for (int i = 1; i <= amount; i++){
            System.out.println("The card #" + i + ":");
            var question = scanner.nextLine();
            if (!isUniqueQuestionName(question)){
                do {
                    System.out.println("The card \"" + question + "\" already exists. Try again:");
                    question = scanner.nextLine();
                } while (!isUniqueQuestionName(question));
            }
            System.out.println("The definition of the card #" + i + ":");
            var answer = scanner.nextLine();
            if (!isUniqueQuestionAnswer(answer)){
                do {
                    System.out.println("The definition \"" + answer + "\" already exists. Try again:");
                    answer = scanner.nextLine();
                } while (!isUniqueQuestionAnswer(answer));
            }
            addQuestion(new Question(question,answer));
        }
    }

    private boolean isCorrect(Question question, String answer){
        return question.getAnswer().toLowerCase().equals(answer.toLowerCase());
    }
}
