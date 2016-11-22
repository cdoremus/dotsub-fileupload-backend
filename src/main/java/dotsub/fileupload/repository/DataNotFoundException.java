package dotsub.fileupload.repository;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(Throwable error) {
		super(error);
	}

	public DataNotFoundException(String message, Throwable error) {
		super(message, error);
	}

	public DataNotFoundException(String message, Throwable error, boolean arg2, boolean arg3) {
		super(message, error, arg2, arg3);
	}

}
