package test.sim.util;


/**
 * Is used to communicate information when using preferences in general.
 * @author kariem
 */
public class PrefsException extends Exception
{
  /**
   * Creates a new instance.
   */
  public PrefsException()
  {
    super();
  }

  /**
  * Creates a new instance with the given <code>message<code>.
  * @param message The message for this <code>PrefsException</code>
   */
  public PrefsException(String message)
  {
    super(message);
  }
}
