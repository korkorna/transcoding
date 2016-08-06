package korkorna.examples.transcoding.infra.ffmpeg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import korkorna.examples.transcoding.domain.job.AudioCodec;
import korkorna.examples.transcoding.domain.job.Container;
import korkorna.examples.transcoding.domain.job.OutputFormat;
import korkorna.examples.transcoding.domain.job.Transcoder;
import korkorna.examples.transcoding.domain.job.VideoCodec;

public class FfmpegTranscoderTest {

	private static final int WIDTH = 160;
    private static final int HEIGHT = 120;
    private static final int BITRATE = 150;
    private static final String SOURCE_FILE = "src/test/resources/sample.avi";
    private static final String TRANSCODED_FILE = "target/sample.mp4";
	
	private Transcoder transcorder;
	private File multimediaFile;
	private List<OutputFormat> outputFormats;
	
	private OutputFormat mp4Format;
	private OutputFormat mp4Format2;
	private OutputFormat aviFormat;
	
	@Before
	public void setup() {
		outputFormats = new ArrayList<OutputFormat>();
		mp4Format = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		mp4Format2 = new OutputFormat(80, 60, 80, Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		aviFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.AVI, VideoCodec.MPEG4, AudioCodec.MP3);
		multimediaFile = new File(SOURCE_FILE);
		
		transcorder = new FfmpegTranscoder(NamingRule.DEFAULT);
	}
	
	@Test
	public void transcodeWithOneMp4OutputFormat() {
		outputFormats.add(mp4Format);
		executeTranscoderAndAssert();
	}
	
	@Test
	public void transcodeWithOneAviOutputFormat() {
		outputFormats.add(aviFormat);
		executeTranscoderAndAssert();
	}
	
	@Test
	public void transcodeWithTwoMp4OutpufFormat() {
		outputFormats.add(mp4Format);
		outputFormats.add(mp4Format2);
		executeTranscoderAndAssert();
	}
	
	private void executeTranscoderAndAssert() {
		// TODO Auto-generated method stub
		List<File> transcodedFiles = transcorder.transcode(multimediaFile, outputFormats);
		assertEquals(outputFormats.size(), transcodedFiles.size());
		for (int i = 0; i < outputFormats.size(); i++) {
			assertTrue(transcodedFiles.get(i).exists());
			VideoFormatVerifier.verifyVideoFormat(outputFormats.get(i), transcodedFiles.get(i));
		}
	}

}
