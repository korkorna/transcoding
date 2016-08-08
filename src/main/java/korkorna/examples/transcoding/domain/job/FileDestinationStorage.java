package korkorna.examples.transcoding.domain.job;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileDestinationStorage extends DestinationStorage{

	public FileDestinationStorage(String url) {
		super(url);
	}

	public void save(List<File> multimediaFiles, List<File> thumnails) {
		// TODO Auto-generated method stub
		try {
			copy(multimediaFiles, getUrl());
			copy(thumnails, getUrl());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Fail to copy: " + e.getMessage(), e);
		}
	}

	private void copy(List<File> files, String directory) throws IOException {
		// TODO Auto-generated method stub
		for (File file : files) {
			copy(file, directory);
		}
	}

	private void copy(File file, String directory) throws IOException {
		// TODO Auto-generated method stub
		String fileName = getFileName(file);
		File target = new File(directory, fileName);
		copy(file, target);
	}

	private void copy(File source, File target) throws IOException {
		// TODO Auto-generated method stub
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(target));
			
			byte[] buffer = new byte[8096];
			int len = -1;
			while((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		} finally {
			// TODO: handle finally clause
			closeStream(bis);
			closeStream(bos);
		}
	}

	private void closeStream(Closeable closeable) {
		// TODO Auto-generated method stub
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private String getFileName(File file) {
		// TODO Auto-generated method stub
		return file.getName();
	}

}
