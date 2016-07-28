package korkorna.examples.transcoding;

public class TranscodingServiceImpl implements TranscodingService{
	private Transcoder transcoder;
	private ThumnailExtractor thumnailExtractor;
	private CreatedFileSender createdFileSender;
	private JobResultNotifier jobResultNotifier;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(Transcoder transcoder,
			ThumnailExtractor thumnailExtractor, CreatedFileSender createdFileSender,
			JobResultNotifier jobResultNotifier, JobRepository jobRepository) {
		super();
		this.transcoder = transcoder;
		this.thumnailExtractor = thumnailExtractor;
		this.createdFileSender = createdFileSender;
		this.jobResultNotifier = jobResultNotifier;
		this.jobRepository = jobRepository;
	}



	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(transcoder, thumnailExtractor, createdFileSender, jobResultNotifier);
	}

}
