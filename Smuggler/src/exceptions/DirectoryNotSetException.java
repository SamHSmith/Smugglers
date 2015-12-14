package exceptions;

public class DirectoryNotSetException extends Exception {

	public DirectoryNotSetException(String message) {
		super(message);
	}

	/**
	 * This is a Exception thrown if the directory is not set
	 */
	private static final long serialVersionUID = 1L;

}
