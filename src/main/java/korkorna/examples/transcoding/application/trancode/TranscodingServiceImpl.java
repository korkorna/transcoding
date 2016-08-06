package korkorna.examples.transcoding.application.trancode;

import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.JobResultNotifier;
import korkorna.examples.transcoding.domain.job.ThumnailExtractor;
import korkorna.examples.transcoding.domain.job.Transcoder;

public class TranscodingServiceImpl implements TranscodingService{
	private Transcoder transcoder;
	private ThumnailExtractor thumnailExtractor;
	private JobResultNotifier jobResultNotifier;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(Transcoder transcoder,
			ThumnailExtractor thumnailExtractor, 
			JobResultNotifier jobResultNotifier, JobRepository jobRepository) {
		super();
		this.transcoder = transcoder;
		this.thumnailExtractor = thumnailExtractor;
		this.jobResultNotifier = jobResultNotifier;
		this.jobRepository = jobRepository;
	}



	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(transcoder, thumnailExtractor, jobResultNotifier);
	}

}
