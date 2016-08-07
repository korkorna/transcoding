package korkorna.examples.transcoding.domain.job;

import java.io.File;
import java.util.List;

public interface Transcoder {

	List<File> transcode(File multisourceFile, List<OutputFormat> outputFormats);

}
