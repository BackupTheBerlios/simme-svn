package at.einspiel.simme.server.base;

/**
 * Represents a Simme user.
 * 
 * @author  kariem
 */
public class User implements Comparable {

   /** Deutsch */
   public static final byte LANG_DE = 0;
   /** English */
   public static final byte LANG_EN = 1;
   /** Español */
   public static final byte LANG_ES = 2;
   /** Français*/
   public static final byte LANG_FR = 3;
   /** Österreich */
   public static final String LOCATION_DEFAULT = "AT";

   private String nick;
   private String password;
   private String winmsg;
   private byte lang;
   private String info;
   private String location;
   private String clientmodel;

   /**
    * Creates a new <code>User</code> with the given properties.
    * @param nick Nickname of user.
    * @param password Password of user.
    * @param winmsg Win message, written after game ending in success for user.
    * @param lang Indicating user language.
    * @param info Additional information on user.
    * @param location Additional information on user location.
    */
   public User(String nick, String password, String winmsg, byte lang, String info, String location) {
      setNick(nick);
      setPassword(password);
      setWinmsg(winmsg);
      setLang(lang);
      setInfo(info);
      setLocation(location);
   }

   /**
    * Creates a new <code>User</code> with nick and password. Other values are
    * set to default.
    * @param nick Nickname of user.
    * @param password Password of user.
    */
   public User(String nick, String password) {
      this(nick, password, "", LANG_DE, "", LOCATION_DEFAULT);
   }

   /**
    * Loads a <code>User</code> from the database with the corresponding nick
    * name.
    *  
    * @param nick The nick name of the user.
    * 
    * @throws NoSuchUserException if the user with the given id cannot
    *         be found
    */
   protected User(String nick) throws NoSuchUserException {
      setNick(nick);
   }

   /**
    * @return String
    */
   public String getClientmodel() {
      return clientmodel;
   }

   /**
    * @return String
    */
   public String getInfo() {
      return info;
   }

   /**
    * @return byte
    */
   public byte getLang() {
      return lang;
   }

   /**
    * @return String
    */
   public String getLocation() {
      return location;
   }

   /**
    * @return String
    */
   public String getNick() {
      return nick;
   }

   /**
    * @return String
    */
   public String getPassword() {
      return password;
   }

   /**
    * @return String
    */
   public String getWinmsg() {
      return winmsg;
   }

   /**
    * Sets the clientmodel.
    * @param clientmodel The clientmodel to set
    */
   public void setClientmodel(String clientmodel) {
      this.clientmodel = clientmodel;
   }

   /**
    * Sets the info.
    * @param info The info to set
    */
   public void setInfo(String info) {
      this.info = info;
   }

   /**
    * Sets the lang.
    * @param lang The lang to set
    */
   public void setLang(byte lang) {
      this.lang = lang;
   }

   /**
    * Sets the location.
    * @param location The location to set
    */
   public void setLocation(String location) {
      this.location = location;
   }

   /**
    * Sets the nick.
    * @param nick The nick to set
    */
   public void setNick(String nick) {
      this.nick = nick;
   }

   /**
    * Sets the password.
    * @param password The password to set
    */
   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * Sets the winmsg.
    * @param winmsg The winmsg to set
    */
   public void setWinmsg(String winmsg) {
      this.winmsg = winmsg;
   }

   /**
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object o) {
      if (o instanceof User) {
         User otherUser = (User) o;
         return otherUser.getNick().compareTo(getNick());
      }
      return 1;
   }

}
