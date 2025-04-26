package bg.sofia.uni.fmi.mjt.socialnetwork.exception;

public class UserRegistrationException extends Exception {
  public UserRegistrationException() {
    super("User cannot make registration");
  }

  public UserRegistrationException(String message) {
    super(message);
  }

  public UserRegistrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
