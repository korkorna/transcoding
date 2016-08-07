package korkorna.examples.transcoding.application.trancode;

import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.ThumnailExtractor;
import korkorna.examples.transcoding.domain.job.Transcoder;

public class TranscodingServiceImpl implements TranscodingService{
	private Transcoder transcoder;
	private ThumnailExtractor thumnailExtractor;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(Transcoder transcoder, ThumnailExtractor thumnailExtractor,
			JobRepository jobRepository) {
		super();
		this.transcoder = transcoder;
		this.thumnailExtractor = thumnailExtractor;
		this.jobRepository = jobRepository;
	}

	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(transcoder, thumnailExtractor);
	}

}
