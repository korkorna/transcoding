package korkorna.examples.transcoding;

import java.io.File;
import java.util.List;

public interface CreatedFileSender {

	void send(List<File> multimediaFiles, List<File> thumnails, Long jobId);

}
