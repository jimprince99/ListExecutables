package com.jimprince99.listexecutables;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.PageAttributes.OriginType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListExecutablesTest {
	
	private static final String TEST1 = "test1.csv";
	private static final String TEST2 = "test2.csv";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Files.deleteIfExists(Paths.get(TEST1));
		Files.deleteIfExists(Paths.get(TEST2));

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * main test - run this one last
	 */
	@Test
	void test20001() {
		String path = ".";
		String outputFile = "executables.csv";
		String masterFile = "master.csv";
		
		ListExecutables list = new ListExecutables(outputFile, masterFile);
		
		Set<String> files = list.processFolder(path);
		
		list.writeExecutables(files, outputFile, false);
		list.mergeFiles(files, masterFile);
		
		list.writeExecutables(files, masterFile, true);

	}
	
	@Test
	void test10001() {
		String path = ".";
		String outputFile = "executables.csv";
		String masterFile = "master.csv";
		
		ListExecutables list = new ListExecutables(outputFile, masterFile);
	
		assertFalse(list.isExecutable.test(Paths.get("doesNotExist")));
		assertFalse(list.isExecutable.test(Paths.get("notExecutable.txt")));
		assertFalse(list.isExecutable.test(Paths.get("notExecutable2.txt")));
		assertTrue(list.isExecutable.test(Paths.get("C:\\Windows\\System32\\cmd.exe")));
	}
	
	/**
	 * test writing a file
	 */
	@Test
	void test10004() {
		String path = ".";
		String outputFile = "executables.csv";
		String masterFile = "master.csv";
		
		ListExecutables list = new ListExecutables(outputFile, masterFile);
	
		Set<String> files = new HashSet<>();
		files.add("Hello World");
		
		list.writeExecutables(files, TEST1, false);
		
		List<String> readLines;
		try {
			readLines = Files.readAllLines(Paths.get(TEST1));
		} catch (IOException e) {
			fail("Failed to read output file " + TEST1);
			return;
		}
		
		assertEquals(1, readLines.size());
		String s = readLines.get(0);
		assertEquals("Hello World,", s);
		
		try {
			Files.deleteIfExists(Paths.get(TEST1));
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * test appending a file
	 */
	@Test
	void test10005() {
		String path = ".";
		String outputFile = "executables.csv";
		String masterFile = "master.csv";
		
		ListExecutables list = new ListExecutables(outputFile, masterFile);
	
		Set<String> files = new HashSet<>();
		files.add("Hello World");
		
		list.writeExecutables(files, TEST1, false);
		list.writeExecutables(files, TEST1, true);
		
		List<String> readLines;
		try {
			readLines = Files.readAllLines(Paths.get(TEST1));
		} catch (IOException e) {
			fail("Failed to read output file " + TEST1);
			return;
		}
		
		assertEquals(2, readLines.size());
		String s = readLines.get(0);
		assertEquals("Hello World,", s);
		
		String s2 = readLines.get(1);
		assertEquals("Hello World,", s2);
		
		try {
			Files.deleteIfExists(Paths.get(TEST1));
		} catch (IOException e) {
			
		}

	}

	
	/**
	 * test merging an empty file and a list
	 */
	@Test
	void test10006() {
		String path = ".";
		try {
			Files.deleteIfExists(Paths.get(TEST1));
		} catch (IOException e) {
			
		}
		try {
			Files.deleteIfExists(Paths.get(TEST2));
		} catch (IOException e) {
			
		}
		
		ListExecutables list = new ListExecutables(TEST1, TEST2);
		
		Set<String> files = new HashSet<>();
		files.add("Hello World");
		
		String movedFile = list.mergeFiles(files, TEST1);
		
		List<String> readLines;
		try {
			readLines = Files.readAllLines(Paths.get(TEST1));
		} catch (IOException e) {
			fail("Failed to read output file " + TEST1);
			return;
		}
		
		assertEquals(1, readLines.size());
		String s = readLines.get(0);
		assertEquals("Hello World,", s);
		assertNull(movedFile);
		
		try {
			Files.deleteIfExists(Paths.get(TEST1));
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * test merging an empty file and a list
	 */
	@Test
	void test10007() {
		String path = ".";
		try {
			Files.deleteIfExists(Paths.get(TEST1));
		} catch (IOException e) {
			
		}
		try {
			Files.deleteIfExists(Paths.get(TEST2));
		} catch (IOException e) {
			
		}
		
		ListExecutables list = new ListExecutables(TEST1, TEST2);
		
		Set<String> files = new HashSet<>();
		files.add("Hello World");
		
		String movedFile = list.mergeFiles(files, TEST1);
		
		List<String> readLines;
		try {
			readLines = Files.readAllLines(Paths.get(TEST1));
		} catch (IOException e) {
			fail("Failed to read output file " + TEST1);
			return;
		}
		
		assertEquals(1, readLines.size());
		String s = readLines.get(0);
		assertEquals("Hello World,", s);
		assertNull(movedFile);
		
		
		Set<String> files2 = new HashSet<>();
		files2.add("Hello World2");
		String movedFile2 = list.mergeFiles(files2, TEST1);
		
		// confirm that movedFile2 has one line
		if (!Files.exists(Paths.get(movedFile2))) {
			fail("Backup file " + movedFile2 + " does not exist");
			return;
		}
		List<String> readLines2;
		try {
			readLines2 = Files.readAllLines(Paths.get(movedFile2));
		} catch (IOException e) {
			fail("Failed to read output file " + movedFile2);
			return;
		}
		
		assertEquals(1, readLines2.size());
		String s2 = readLines2.get(0);
		assertEquals("Hello World,", s2);
		
		
		// confirm that movedFile2 has two lines
		List<String> readLines3;
		try {
			readLines3 = Files.readAllLines(Paths.get(TEST1));
		} catch (IOException e) {
			fail("Failed to read output file " + TEST1);
			return;
		}
		
		assertEquals(2, readLines3.size());
		String s3 = readLines3.get(0);
		assertEquals("Hello World,", s3);
		String s4 = readLines3.get(1);
		assertEquals("Hello World2,", s4);

		try {
			Files.deleteIfExists(Paths.get(TEST1));
		} catch (IOException e) {
			
		}
		try {
			Files.deleteIfExists(Paths.get(TEST2));
		} catch (IOException e) {
			
		}
	}
}
