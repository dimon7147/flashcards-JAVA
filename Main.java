package flashcards;

public class Main {
    public static void main(String[] args) {
        var game = new FlashCardGame();
        if (args.length > 0) game.parseArgs(args);
        game.start();
    }
}
