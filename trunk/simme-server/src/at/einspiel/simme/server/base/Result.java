package at.einspiel.simme.server.base;

/**
 * Result of a single move.
 * @author kariem
 */
public class Result {
    
    public static final Result POSITIVE = new Result(true);
    public static final Result NEGATIVE = new Result(false);
    
    private boolean success;

    public Result(boolean success) {
        this.success = success;
    }
    
    public boolean success() {
        return success;
    }
}
