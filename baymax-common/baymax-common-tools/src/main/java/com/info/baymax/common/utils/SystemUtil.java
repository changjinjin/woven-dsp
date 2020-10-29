package com.info.baymax.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class SystemUtil {

	public static void main(String[] args) {
		String pid = SystemUtil.getPid();
		System.out.println(pid);

		SystemUtil.storePid("run", "woven.pid", pid);
	}

	public static String getPid() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		return pid;
	}

	public static void storePid(String dir, String filename, String pid) {
		File folder = new File(dir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder, filename);
		try {
			FileOutputStream output = new FileOutputStream(file);
			output.write(pid.getBytes());
			output.close();
		} catch (IOException e) {
			System.err.println("Store pid to file failed: pidfile->" + filename + ",pid->" + pid);
		}
	}
}
