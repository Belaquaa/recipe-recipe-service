package dika.recipeservice.exception;


public class SendingException extends RuntimeException {

    public SendingException(String message, final Exception e) {
        super(message);
    }
}
