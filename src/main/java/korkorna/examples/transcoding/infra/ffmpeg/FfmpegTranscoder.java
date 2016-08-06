package korkorna.examples.transcoding.infra.ffmpeg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

import korkorna.examples.transcoding.domain.job.OutputFormat;
import korkorna.examples.transcoding.domain.job.Transcoder;

public class FfmpegTranscoder implements Transcoder {

	private NamingRule namingRule;
	
	public FfmpegTranscoder(NamingRule namingRule) {
		super();
		this.namingRule = namingRule;
	}

	public List<File> transcode(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<File> transcode(File multisourceFile, List<OutputFormat> outputFormats) {
		// TODO Auto-generated method stub
		List<File> results = new ArrayList<File>();
		for (OutputFormat format : outputFormats) {
			results.add(transcode(multisourceFile, format));
		}
		return results;
	}
	
	private File transcode(File sourceFile, OutputFormat format) {
		IMediaReader reader = ToolFactory.makeReader(sourceFile.getAbsolutePath());
		
		String outputFile = getFileName(format);//"outputFile.mp4"; // 테스트를 통과하기 위한 코딩
		
		VideoConverter converter = new VideoConverter(outputFile, reader, format);
		reader.addListener(converter);
		
		while(reader.readPacket() == null) {
			do {
			} while (false);
		}
		
		return new File(outputFile);
	}

	private String getFileName(OutputFormat format) {
		// TODO Auto-generated method stub
		return namingRule.createName(format);
	}

}
