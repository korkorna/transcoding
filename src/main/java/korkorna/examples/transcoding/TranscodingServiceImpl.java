package korkorna.examples.transcoding;

import java.io.File;
import java.util.List;

public class TranscodingServiceImpl implements TranscodingService{
	private MediaSourceCopier mediaSourceCopier;
	private Transcoder transcoder;
	private ThumnailExtractor thumnailExtractor;
	private CreatedFileSender createdFileSender;
	private JobResultNotifier jobResultNotifier;

	public TranscodingServiceImpl(MediaSourceCopier mediaSourceCopier, Transcoder transcoder,
			ThumnailExtractor thumnailExtractor, CreatedFileSender createdFileSender,
			JobResultNotifier jobResultNotifier) {
		super();
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumnailExtractor = thumnailExtractor;
		this.createdFileSender = createdFileSender;
		this.jobResultNotifier = jobResultNotifier;
	}

	public void transcode(Long jobId) {
		//미디어 원본으로 부터 파일을 로컬에 복사한다.
		File multimediaFile = copyMultimediaSourceTolocal(jobId);
		
		//로컬에 복사된 파일을 변환처리한다.
		List<File> multimediaFiles = transcode(multimediaFile, jobId);
		
		//로컬에 복사된 파일로 부터 이미지를 추출한다.
		List<File> thumnails = extractThumnail(multimediaFile, jobId);
		
		//변환된 결과 파일과 썸네일 이미지를 목적지에 저장
		sendCreatedFileToDestination(multimediaFiles, thumnails, jobId);
		
		//결과를 통지
		notifyJobResultToRequester(jobId);
	}

	private void notifyJobResultToRequester(Long jobId) {
		// TODO Auto-generated method stub
		jobResultNotifier.notifyToRequester(jobId);
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumnails, Long jobId) {
		// TODO Auto-generated method stub
		createdFileSender.send(multimediaFiles, thumnails, jobId);
	}

	private List<File> extractThumnail(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		return thumnailExtractor.extract(multimediaFile, jobId);
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		return transcoder.transcode(multimediaFile, jobId);
	}

	private File copyMultimediaSourceTolocal(Long jobId) {
		// TODO Auto-generated method stub
		return mediaSourceCopier.copy(jobId);
	}
}
