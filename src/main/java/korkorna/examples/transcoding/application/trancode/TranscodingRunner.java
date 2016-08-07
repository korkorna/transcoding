package korkorna.examples.transcoding.application.trancode;

import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;

public class TranscodingRunner {
	private TranscodingService transcodingService;
	private JobRepository jobRepository;

	public TranscodingRunner(TranscodingService transcodingService, JobRepository jobRepository) {
		super();
		this.transcodingService = transcodingService;
		this.jobRepository = jobRepository;
	}

	public void run() {
		// TODO Auto-generated method stub
		Job job = getNextJob();
		runTranscoding(job);
	}

	private void runTranscoding(Job job) {
		// TODO Auto-generated method stub
		if (job == null)
			return;
		
		transcodingService.transcode(job.getId());
	}

	private Job getNextJob() {
		// TODO Auto-generated method stub
		return jobRepository.findEldestJobOfCreatedState();
	}

}
