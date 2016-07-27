package korkorna.examples.transcoding;

public class Job {

	public static enum State {
		MEDIASOURCECOPYING, COMPLETED, TRANSCODING, THUMNAILEXTRACTING, CREATEDFILESENDING, NOTIFING
	}
	private State state = null;
	private Exception occurredException = null;
	
	public boolean isSuccess() {
		// TODO Auto-generated method stub
		return this.state == State.COMPLETED;
	}

	public State getLastState() {
		// TODO Auto-generated method stub
		return this.state;
	}

	public void changeState(State newState) {
		// TODO Auto-generated method stub
		this.state = newState;
	}

	public boolean isWaiting() {
		// TODO Auto-generated method stub
		return state == null;
	}

	public boolean isFinish() {
		// TODO Auto-generated method stub
		return isSuccess() || isExceptionOccurred();
	}

	private boolean isExceptionOccurred() {
		// TODO Auto-generated method stub
		return occurredException != null;
	}

	public Exception getOccurredException() {
		// TODO Auto-generated method stub
		return occurredException;
	}

	public void exceptionOccurred(RuntimeException e) {
		// TODO Auto-generated method stub
		occurredException = e;
	}

}
