package com.info.baymax.common.webflux.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FilePartUtils {

	public static File writeTempFile(FilePart filePart) throws IOException {
		return writeTempPath(filePart).toFile();
	}

	public static Path writeTempPath(FilePart filePart) throws IOException {
		Path path = Files.createTempFile("tempFile", filePart.filename());
		Path fileName = path.getFileName();
		DataBufferUtils.write(filePart.content(), AsynchronousFileChannel.open(path, StandardOpenOption.WRITE), 0)
			.doOnComplete(() -> {
				log.info("File saved to server location : " + fileName);
			}).subscribe();
		return path;
	}

	public static File writeFile(FilePart filePart, String parentPath) throws IOException {
		return writePath(filePart, parentPath).toFile();
	}

	public static File writeFile(FilePart filePart, String parentPath, String newFilename) throws IOException {
		return writePath(filePart, parentPath, newFilename).toFile();
	}

	public static Path writePath(FilePart filePart, String parentPath) throws IOException {
		return writePath(filePart, parentPath, null);
	}

	public static Path writePath(FilePart filePart, String parentPath, String newFilename) throws IOException {
		Path path = Files
			.createFile(Paths.get(parentPath, StringUtils.defaultIfEmpty(newFilename, filePart.filename())));
		Path fileName = path.getFileName();
		DataBufferUtils.write(filePart.content(), AsynchronousFileChannel.open(path, StandardOpenOption.WRITE), 0)
			.doOnComplete(() -> {
				log.info("File saved to server location : " + fileName);
			}).subscribe();
		return path;
	}

	public static void deleteQuietly(Path path) {
		if (path != null) {
			try {
				Files.deleteIfExists(path);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static void deleteQuietly(File file) {
		if (file != null) {
			try {
				file.deleteOnExit();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
