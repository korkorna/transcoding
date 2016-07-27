package korkorna.examples.transcoding;

import java.io.File;
import java.util.List;

import korkorna.examples.transcoding.Job.State;

public class TranscodingServiceImpl implements TranscodingService{
	private MediaSourceCopier mediaSourceCopier;
	private Transcoder transcoder;
	private ThumnailExtractor thumnailExtractor;
	private CreatedFileSender createdFileSender;
	private JobResultNotifier jobResultNotifier;
	private JobStateChanger jobStateChanger;
	private TranscodingExceptionHandler transcodingExceptionHandler;

	public TranscodingServiceImpl(MediaSourceCopier mediaSourceCopier, Transcoder transcoder,
			ThumnailExtractor thumnailExtractor, CreatedFileSender createdFileSender,
			JobResultNotifier jobResultNotifier, JobStateChanger jobStateChanger,
			TranscodingExceptionHandler transcodingExceptionHandler) {
		super();
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumnailExtractor = thumnailExtractor;
		this.createdFileSender = createdFileSender;
		this.jobResultNotifier = jobResultNotifier;
		this.jobStateChanger = jobStateChanger;
		this.transcodingExceptionHandler = transcodingExceptionHandler;
	}

	public void transcode(Long jobId) {
		
		//미디어 원본으로 부터 파일을 로컬에 복사한다.
		changeJobState(jobId, Job.State.MEDIASOURCECOPYING);
		File multimediaFile = copyMultimediaSourceTolocal(jobId);
		
		//로컬에 복사된 파일을 변환처리한다.
		changeJobState(jobId, Job.State.TRANSCODING);
		List<File> multimediaFiles = transcode(multimediaFile, jobId);
		
		//로컬에 복사된 파일로 부터 이미지를 추출한다.
		changeJobState(jobId, Job.State.THUMNAILEXTRACTING);
		List<File> thumnails = extractThumnail(multimediaFile, jobId);
		
		//변환된 결과 파일과 썸네일 이미지를 목적지에 저장
		changeJobState(jobId, Job.State.CREATEDFILESENDING);
		sendCreatedFileToDestination(multimediaFiles, thumnails, jobId);
		
		//결과를 통지
		changeJobState(jobId, Job.State.NOTIFING);
		notifyJobResultToRequester(jobId);
		
		changeJobState(jobId, Job.State.COMPLETED);
	}

	private void changeJobState(Long jobId, State newJobState) {
		// TODO Auto-generated method stub
		try {
			jobStateChanger.changeJobState(jobId, newJobState);
		} catch (RuntimeException e) {
			// TODO: handle exception
			transcodingExceptionHandler.notifiyToJob(jobId, e);
			throw e;
		}
	}

	private void notifyJobResultToRequester(Long jobId) {
		// TODO Auto-generated method stub
		try {
			jobResultNotifier.notifyToRequester(jobId);
		} catch (RuntimeException e) {
			// TODO: handle exception
			transcodingExceptionHandler.notifiyToJob(jobId, e);
			throw e;
		}
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumnails, Long jobId) {
		// TODO Auto-generated method stub
		try {
			createdFileSender.send(multimediaFiles, thumnails, jobId);
		} catch (RuntimeException e) {
			// TODO: handle exception
			transcodingExceptionHandler.notifiyToJob(jobId, e);
			throw e;
		}
	}

	private List<File> extractThumnail(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		try {
			return thumnailExtractor.extract(multimediaFile, jobId);
		} catch (RuntimeException e) {
			// TODO: handle exception
			transcodingExceptionHandler.notifiyToJob(jobId, e);
			throw e;
		}
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		try {
			return transcoder.transcode(multimediaFile, jobId);
		} catch (RuntimeException e) {
			// TODO: handle exception
			transcodingExceptionHandler.notifiyToJob(jobId, e);
			throw e;
		}
	}

	private File copyMultimediaSourceTolocal(Long jobId) {
		// TODO Auto-generated method stub
		try {
			return mediaSourceCopier.copy(jobId);
		} catch (RuntimeException e) {
			// TODO: handle exception
			transcodingExceptionHandler.notifiyToJob(jobId, e);
			throw e;
		}
	}
}
