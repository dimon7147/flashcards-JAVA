package flashcards;

public class Question {

    private final String question;
    private final String answer;
    private Integer complexity;

    Question(String question, String answer, Integer complexity){
        this.question = question;
        this.answer = answer;
        this.complexity = complexity;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public Integer getComplexity(){
        return complexity;
    }

    public void resetComplexity(){
        this.complexity = 0;
    }

    public void increaseComplexity(){
        this.complexity++;
    }
}
