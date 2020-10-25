package com.jimprince99.listexecutables;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

/**
 * Find, and list all executables in a Windows folder
 * and any subfolders
 * 
 * @author jimprince99@gmail.com
 *
 */
public class ListExecutables {
	
	String path = "c:\tmp";
	String outputFile = "executables.csv";
	String masterFile = "master.csv";

	public static void main(String[] args) {

		String path = "";
		if (args.length < 1) {
			path = System.getProperty("user.dir");
		} else {
			path = args[0];
		}
		
		String outputFile = "executables.csv";
		String masterFile = "master.csv";
		
		ListExecutables list = new ListExecutables(outputFile, masterFile);
		Set<String> files = list.processFolder(path);

		System.out.println("Found " + files.size() + " executables.");

		list.writeExecutables(files, outputFile, false);
		list.mergeFiles(files, masterFile);

	}
	
	/**
	 * Merge our list of files with the master list of files.
	 * @param files out list of files
	 */
	public String mergeFiles(Set<String> files, String destFile) {
		String backupFile = destFile + "-" + LocalDateTime.now();
		backupFile = backupFile.replaceAll(":", ".");
		if (Files.exists(Paths.get(destFile))) {
			try {
				Files.move(Paths.get(destFile), Paths.get(backupFile));
			} catch (IOException e1) {
				System.out.println("Unable to backup master file " + destFile + " to backup file " + backupFile);			
			}
		
			Stream<String> lines = Stream.empty();
			try {
				lines = Files.lines(Paths.get(backupFile));
			} catch (IOException e) {
				System.out.println("Unable to read master file: " + destFile + ", error: " + e.getMessage());
			}
		
			lines
			.map(e -> removeComma.apply(e))
			.forEach(e -> files.add(e));
			lines.close();
		} else {
			backupFile = null;
		}
		
		// Now write merged file out
		writeExecutables(files, destFile, false);
		return backupFile;
	}
		
	/**
	 * Write a list of paths to an output file
	 * @param files the list of paths to write
	 * @param outputFile the name of the output file to write to
	 */
	public void writeExecutables(Set<String> files, String outputFile, boolean append) {
		StandardOpenOption option = (append) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE;
		LinkedHashSet<String> files2 = files.stream()
				.sorted()
				.collect(Collectors.toCollection(LinkedHashSet::new));
		try {
			StringBuilder s = new StringBuilder();
			files2.stream().forEach(e -> s.append(e + ",\n"));
			Files.write(Paths.get(outputFile), s.toString().getBytes(), option);
		} catch (IOException e) {
			System.out.println("Unable to write " + outputFile + " file: " + e.getMessage());
		}
	}
	
	/**
	 * constructor
	 * @param path the Folder to check
	 * @param outputFile the csv file to create
	 * @param masterFile the running total of files. We merge our new files with this file.
	 */
	ListExecutables(String outputFile, String masterFile) {
		this.outputFile = outputFile;
		this.masterFile = masterFile;
	}
	
	/**
	 * Get a set of path names for processing later on
	 * @return
	 */
	public Set<String> processFolder(String path) {
		this.path = path;
		Set<String> foundExecutables = new HashSet<>();
		
		File folder = new File(path);
		find(folder, foundExecutables);
		return foundExecutables;
	};
		
	
	
	/**
	 * check if the file we found is Windows executable
	 */
	Predicate<Path> isExecutable = (myPath) -> {
		if (myPath == null) return false;
		byte[] bytes = new byte[0];
		try {
			bytes = Files.readAllBytes(myPath);
		} catch (Exception e) {
			return false;
		}
		if (bytes.length < 2) return false;
		return ((bytes[0] == 0x4d) && (bytes[1] == 0x5a));
	};
	
	UnaryOperator<String> removeComma = s -> {
		
		if (s.endsWith(",")) {
			return s.substring(0,s.length()-1);
		} else {
			return s;
		}
	};
	
	/**
	 * find all the executable files in a folder
	 * @param folder
	 * @param paths
	 * @return
	 */
	protected Set<String> find(File folder, Set<String> paths) {
		for (File file : folder.listFiles()) {
			//System.out.println(file.getAbsolutePath());

			if (file.isDirectory()) {
				try {
					find(file, paths);
				} catch (Exception e) {};
			}
			if (file.isFile()) {
				Path path = file.toPath();			
				if (isExecutable.test(path)) {
					paths.add(file.getAbsolutePath());
				}
			}
		}
		return paths;
	}

}
