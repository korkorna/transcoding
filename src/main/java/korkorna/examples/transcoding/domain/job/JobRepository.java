package korkorna.examples.transcoding.domain.job;

public interface JobRepository {

	Job findById(Long jobId);

	Job save(Job job);

	Job findEldestJobOfCreatedState();

}
