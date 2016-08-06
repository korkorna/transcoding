package korkorna.examples.transcoding.domain.job;

import java.io.File;
import java.util.List;

public interface ThumnailExtractor {

	List<File> extract(File mockMultimediaFile, Long jobId);

}
