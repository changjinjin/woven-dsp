package com.info.baymax.common.webflux.utils;

import com.info.baymax.common.core.saas.SaasContext;
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
import java.util.function.BiConsumer;

@Slf4j
public class FilePartUtils {

	public static void writeTemp(FilePart filePart) throws IOException {
		writeTemp(filePart, null, null);
	}

	public static void writeTemp(FilePart filePart, SaasContext sc, BiConsumer<Path, SaasContext> bc)
		throws IOException {
		Path path = Files.createTempFile("tempFile", filePart.filename());
		DataBufferUtils.write(filePart.content(), AsynchronousFileChannel.open(path, StandardOpenOption.WRITE), 0)
			.doOnComplete(() -> {
				if (bc != null) {
					bc.accept(path, sc);
				}
				log.info("File saved to server location : " + path.getFileName());
			}).subscribe();
	}

	public static void write(FilePart filePart, String parentPath, SaasContext sc, BiConsumer<Path, SaasContext> bc)
		throws IOException {
		write(filePart, parentPath, null, sc, bc);
	}

	public static void write(FilePart filePart, String parentPath, String newFilename, SaasContext sc,
							 BiConsumer<Path, SaasContext> bc) throws IOException {
		Path path = Files
			.createFile(Paths.get(parentPath, StringUtils.defaultIfEmpty(newFilename, filePart.filename())));
		DataBufferUtils.write(filePart.content(), AsynchronousFileChannel.open(path, StandardOpenOption.WRITE), 0)
			.doOnComplete(() -> {
				if (bc != null) {
					bc.accept(path, sc);
				}
				log.info("File saved to server location : " + path.getFileName());
			}).subscribe();
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
