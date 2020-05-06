package flashcards;

public class Question {

    private final String question;
    private final String answer;

    Question(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
}
