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
            if (isCorrect(questions.get(i), scanner.nextLine())){
                System.out.println("Correct answer.");
            } else {
                System.out.println("Wrong answer. The correct one is \"" + questions.get(i).getAnswer() + "\".");
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

    private void addQuestion(Question question){
        questions.put(findFreeId(),question);
    }

    private void addQuestions(int amount){
        var scanner = new Scanner(System.in);
        for (int i = 1; i <= amount; i++){
            System.out.println("The card #" + i + ":");
            var question = scanner.nextLine();
            System.out.println("The definition of the card #" + i + ":");
            var answer = scanner.nextLine();
            addQuestion(new Question(question,answer));
        }
    }

    private boolean isCorrect(Question question, String answer){
        if (question.getAnswer().toLowerCase().equals(answer.toLowerCase())){
            return true;
        }
        return false;
    }
}
