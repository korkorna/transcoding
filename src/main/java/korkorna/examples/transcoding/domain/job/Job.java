package korkorna.examples.transcoding.domain.job;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Job {

	public static enum State {
		MEDIASOURCECOPYING, COMPLETED, TRANSCODING, THUMNAILEXTRACTING, STORING, NOTIFING, WAITING
	}

	private Long id;
	private State state = null;
	private Exception occurredException = null;
	private MediaSourceFile mediaSourceFile;
	private DestinationStorage destinationStorage;
	private List<OutputFormat> outputFormats;

	public Job(Long id, MediaSourceFile mediaSourceFile, DestinationStorage destinationStorage
			, List<OutputFormat> outputFormats) {
		super();
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
		this.destinationStorage = destinationStorage;
		this.outputFormats = outputFormats;
		state = Job.State.WAITING;
	}

	public Job(MediaSourceFile mediaSourceFile, DestinationStorage destinationStorage,
			List<OutputFormat> outputFormats) {
		// TODO Auto-generated constructor stub
		this(null, mediaSourceFile, destinationStorage, outputFormats);
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
		return state == State.WAITING;
	}

	public boolean isFinished() {
		// TODO Auto-generated method stub
		return isSuccess() || isExceptionOccurred();
	}

	public boolean isExceptionOccurred() {
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

	public void transcode(Transcoder transcoder, ThumnailExtractor thumnailExtractor,
			JobResultNotifier jobResultNotifier) {
		try {
			// 미디어 원본으로 부터 파일을 로컬에 복사한다.
			File multimediaFile = copyMultimediaSourceTolocal();

			// 로컬에 복사된 파일을 변환처리한다.
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);

			// 로컬에 복사된 파일로 부터 이미지를 추출한다.
			List<File> thumnails = extractThumnail(multimediaFile, thumnailExtractor);

			// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장
			storeCreatedFilesToStorage(multimediaFiles, thumnails);

			// 결과를 통지
			notifyJobResultToRequester(jobResultNotifier);

			completed();
		} catch (RuntimeException e) {
			exceptionOccurred(e);
			throw e;
		}
	}

	private void completed() {
		changeState(Job.State.COMPLETED);
	}

	private void notifyJobResultToRequester(JobResultNotifier jobResultNotifier) {
		// TODO Auto-generated method stub
		changeState(Job.State.NOTIFING);
		jobResultNotifier.notifyToRequester(id);
	}

	private void storeCreatedFilesToStorage(List<File> multimediaFiles, List<File> thumnails) {
		// TODO Auto-generated method stub
		changeState(Job.State.STORING);
		destinationStorage.save(multimediaFiles, thumnails);
	}

	private List<File> extractThumnail(File multimediaFile, ThumnailExtractor thumnailExtractor) {
		// TODO Auto-generated method stub
		changeState(Job.State.THUMNAILEXTRACTING);
		return thumnailExtractor.extract(multimediaFile, id);
	}

	private List<File> transcode(File multimediaFile, Transcoder transcoder) {
		// TODO Auto-generated method stub
		changeState(Job.State.TRANSCODING);
		return transcoder.transcode(multimediaFile, outputFormats);
	}

	private File copyMultimediaSourceTolocal() {
		// TODO Auto-generated method stub
		changeState(Job.State.MEDIASOURCECOPYING);
		return mediaSourceFile.getSourceFile();
	}

	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public List<OutputFormat> getOutputFormats() {
		return Collections.unmodifiableList(outputFormats);
	}
}
