package at.einspiel.base;

/**
 * Result of a single move.
 * @author kariem
 */
public class Result {

   /** A simple succeeded result */
   public static final Result POSITIVE = new Result(true);
   /** A simple not succeeded result */
   public static final Result NEGATIVE = new Result(false);

   private boolean success;

   /**
    * Creates a result with the given value for success.
    * @param success the value for success.
    */
   public Result(boolean success) {
      this.success = success;
   }

   /**
    * Returns the whether the operation has succeeded.
    * @return <code>true</code> if the operation has succeeded.
    */
   public boolean success() {
      return success;
   }
}
