package korkorna.examples.transcoding;

import java.io.File;
import java.util.List;

public class Job {

	public static enum State {
		MEDIASOURCECOPYING, COMPLETED, TRANSCODING, THUMNAILEXTRACTING, CREATEDFILESENDING, NOTIFING
	}
	
	private Long id;
	private State state = null;
	private Exception occurredException = null;
	private MediaSourceFile mediaSourceFile;
	
	public Job(Long id, MediaSourceFile mediaSourceFile) {
		super();
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
	}

	public boolean isSuccess() {
		// TODO Auto-generated method stub
		return this.state == State.COMPLETED;
	}

	public State getLastState() {
		// TODO Auto-generated method stub
		return this.state;
	}

	private void changeState(State newState) {
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

	private void exceptionOccurred(RuntimeException e) {
		// TODO Auto-generated method stub
		occurredException = e;
	}
	
	public void transcode(Transcoder transcoder,
			ThumnailExtractor thumnailExtractor,
			CreatedFileSender createdFileSender,
			JobResultNotifier jobResultNotifier) {
		try {
			//미디어 원본으로 부터 파일을 로컬에 복사한다.
			changeState(Job.State.MEDIASOURCECOPYING);
			File multimediaFile = copyMultimediaSourceTolocal();
			
			//로컬에 복사된 파일을 변환처리한다.
			changeState(Job.State.TRANSCODING);
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);
			
			//로컬에 복사된 파일로 부터 이미지를 추출한다.
			changeState(Job.State.THUMNAILEXTRACTING);
			List<File> thumnails = extractThumnail(multimediaFile, thumnailExtractor);
			
			//변환된 결과 파일과 썸네일 이미지를 목적지에 저장
			changeState(Job.State.CREATEDFILESENDING);
			sendCreatedFileToDestination(multimediaFiles, thumnails, createdFileSender);
			
			//결과를 통지
			changeState(Job.State.NOTIFING);
			notifyJobResultToRequester(jobResultNotifier);
			
			changeState(Job.State.COMPLETED);
		} catch(RuntimeException e) {
			exceptionOccurred(e);
			throw e;
		}
	}

	private void notifyJobResultToRequester(JobResultNotifier jobResultNotifier) {
		// TODO Auto-generated method stub
		jobResultNotifier.notifyToRequester(id);
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumnails, CreatedFileSender createdFileSender) {
		// TODO Auto-generated method stub
		createdFileSender.send(multimediaFiles, thumnails, id);
	}

	private List<File> extractThumnail(File multimediaFile, ThumnailExtractor thumnailExtractor) {
		// TODO Auto-generated method stub
		return thumnailExtractor.extract(multimediaFile, id);
	}

	private List<File> transcode(File multimediaFile, Transcoder transcoder) {
		// TODO Auto-generated method stub
		return transcoder.transcode(multimediaFile, id);
	}

	private File copyMultimediaSourceTolocal() {
		// TODO Auto-generated method stub
		return mediaSourceFile.getSourceFile();
	}
}
