package korkorna.examples.transcoding;

import java.io.File;
import java.util.List;

public interface Transcoder {

	List<File> transcode(File multimediaFile, Long jobId);

	List<File> transcode(File multisourceFile, List<OutputFormat> outputFormats);

}
