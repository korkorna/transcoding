package korkorna.examples.transcoding.infra.ffmpeg;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import korkorna.examples.transcoding.domain.job.OutputFormat;

public interface NamingRule {

	String createName(OutputFormat format);
	
	public static final NamingRule DEFAULT = new DefaultNamingRule();
	
	public static class DefaultNamingRule implements NamingRule {

		private Random random = new Random();
		private String baseDir;
		
		public DefaultNamingRule() {
			baseDir = System.getProperty("java.io.tmpdir");
		}
		
		public DefaultNamingRule(String baseDir) {
			super();
			this.baseDir = baseDir;
		}



		public String createName(OutputFormat format) {
			String fileName = getFileNameFromTime();
			File file = createFileFromFileNameAndFormat(format, baseDir, fileName);
			return file.getPath();
		}
		
		private File createFileFromFileNameAndFormat(OutputFormat format, String tempDir, String fileName) {
			// TODO Auto-generated method stub
			File file = new File(tempDir, fileName + "."+ format.getFileExtension());
			return file;
		}

		private String getFileNameFromTime() {
			// TODO Auto-generated method stub
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dateFormat.format(new Date());
			int num = random.nextInt(1000);
			String fileName = time + "_" + num;
			return fileName;
		}
		
		
		
	}

}
