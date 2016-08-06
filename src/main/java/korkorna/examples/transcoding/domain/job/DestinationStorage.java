package korkorna.examples.transcoding.domain.job;

import java.io.File;
import java.util.List;

public interface DestinationStorage {

	void save(List<File> multimediaFiles, List<File> thumnails);

}
