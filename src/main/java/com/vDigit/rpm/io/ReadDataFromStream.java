package com.vDigit.rpm.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ReadDataFromStream {
	public String readData(InputStream is) {
		try {
			return readDataFromInputStream(is);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private String readDataFromInputStream(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return readDataFromReader(reader);
	}

	public String readDataFromReader(Reader reader) {

		try {
			BufferedReader bufferedReader = makeBufferedReader(reader);
			return readDataFromBufferedReader(bufferedReader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private String readDataFromBufferedReader(BufferedReader bufferedReader) throws IOException {
		StringBuffer sb = new StringBuffer();

		boolean canAddLine = false;
		while (true) {
			String line = bufferedReader.readLine();
			// System.out.println("Line ->"+line);
			if (line == null)
				break;

			if (canAddLine) {
				sb.append("\n");
			} else {
				canAddLine = true;
			}
			if (processLine(line) == false) {
				break;
			}
			if (canLineBeCollectedInOutput(line, sb)) {
				sb.append(line);
			}
		}
		return sb.toString();
	}

	public Collection parseFileAndCreateObjects(String file, Class clz, char separator,
			Map<String, String> fileAndClassFieldMappings) {
		try {
			Collection<Object> objects = new ArrayList<Object>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = reader.readLine();
			String fields[] = line.split("\\" + separator);
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				String data[] = line.split("\\" + separator);
				FieldMappings fm = new FieldMappings(fields, fileAndClassFieldMappings, data);
				objects.add(createObject(clz, fm.getClassFields(), fm.getFieldsData()));
			}
			reader.close();
			return objects;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	class FieldMappings {
		Map<String, String> fieldDataMapping = new LinkedHashMap<String, String>();

		FieldMappings(String fields[], Map<String, String> fieldAndClassFieldMapping, String[] data) {
			calculate(fields, fieldAndClassFieldMapping, data);
		}

		private void calculate(String[] fields, Map<String, String> fieldAndClassFieldMapping, String[] data) {
			for (int i = 0; i < fields.length; i++) {
				String value = fieldAndClassFieldMapping.get(fields[i]);
				if (value != null) {
					if (i < data.length) {
						fieldDataMapping.put(value, data[i]);
					}
				}
			}
		}

		public String[] getClassFields() {
			Collection<String> fields = fieldDataMapping.keySet();
			String[] fieldsArray = new String[fields.size()];
			return fields.toArray(fieldsArray);
		}

		public String[] getFieldsData() {
			Collection<String> data = fieldDataMapping.values();
			String[] dataArray = new String[data.size()];
			return data.toArray(dataArray);
		}
	}

	public Collection parseFileAndCreateObjects(String file, Class clz, char separator) {
		try {

			return parseStreamAndCreateObjects(new FileInputStream(file), clz, separator);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Collection parseStreamAndCreateObjects(InputStream is, Class clz, char separator) throws Exception {

		Collection<Object> objects = new ArrayList<Object>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		String fields[] = line.split("\\" + separator);
		while (true) {
			line = reader.readLine();
			if (line == null) {
				break;
			}
			objects.add(createObject(clz, fields, line.split("\\" + separator)));
		}
		reader.close();
		return objects;
	}

	private Object createObject(Class clz, String[] fields, String[] data) throws Exception {

		Object object = clz.newInstance();
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i];
			Field f = clz.getDeclaredField(name);
			f.setAccessible(true);
			if (i < data.length) {
				f.set(object, convertData(f, data[i]));
			}
		}
		return object;
	}

	public Object convertData(Field f, String s) {
		if (f.getType() == String.class) {
			return s;
		}
		if (f.getType() == Boolean.class || f.getType() == boolean.class) {
			return Boolean.getBoolean(s);
		}
		if (f.getType() == Integer.class || f.getType() == int.class) {
			return Integer.parseInt(s);
		}
		throw new RuntimeException("Please write code for more types -> " + f.getType() + " -> " + s);
	}

	public ReadDataFromStream() {
	}

	static class CleanedContent {
		String original;
		String cleaned;
	}

	static interface LineCleaner {
		public String getLine(String line);
	}

	static class DefaultLineCleaner implements LineCleaner {

		@Override
		public String getLine(String line) {
			return line;
		}

	}

	public static CleanedContent removeDuplicateLines(InputStream is, LineCleaner lc) {
		return removeDuplicateLines(new BufferedReader(new InputStreamReader(is)), lc);

	}

	private static BufferedReader makeBufferedReader(Reader reader) {
		if (reader instanceof BufferedReader) {
			return (BufferedReader) reader;
		} else {
			return new BufferedReader(reader);
		}

	}

	public static CleanedContent removeDuplicateLines(Reader reader, final LineCleaner lc) {
		CleanedContent cc = new CleanedContent();

		final Set<String> lines = new HashSet<String>();
		ReadDataFromStream stream = new ReadDataFromStream();
		stream.setLineProcessor(new ReadDataFromStream.LineProcessor() {

			@Override
			public boolean processLine(String line) {
				if (lc == null) {
					lines.add(line);
				} else {
					lines.add(lc.getLine(line));
				}
				return true;
			}
		});
		String content = stream.readDataFromReader(reader);
		StringBuffer sb = new StringBuffer(lines.size() * 200);
		boolean firstLine = false;
		for (String l : lines) {
			if (firstLine == true) {
				sb.append("\n");
			} else {
				firstLine = true;
			}
			sb.append(l);
		}
		String data = sb.toString();
		cc.cleaned = data;
		return cc;
	}

	public void dontCollectLines() {
		setLineCollector(new LineCollector() {

			@Override
			public boolean canLineBeCollectedInOutput(String line, StringBuffer sb) {

				return false;
			}
		});
	}

	private boolean canLineBeCollectedInOutput(String line, StringBuffer sb) {
		return getLineCollector().canLineBeCollectedInOutput(line, sb);
	}

	public String readDataFromFile(String file) throws RuntimeException {
		try {
			return readData(new FileInputStream(new File(file)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public boolean processLine(String line) {
		return getLineProcessor().processLine(line);
	}

	public static interface LineProcessor {
		boolean processLine(String line);
	}

	static interface LineCollector {
		boolean canLineBeCollectedInOutput(String line, StringBuffer sb);
	}

	static class LineCollectorImpl implements LineCollector {

		@Override
		public boolean canLineBeCollectedInOutput(String line, StringBuffer sb) {
			return true;
		}
	}

	static class LineProcessorImpl implements LineProcessor {

		@Override
		public boolean processLine(String line) {
			return true;
		}
	}

	public Object readObject(String file) throws RuntimeException {
		try {
			FileInputStream fis = new FileInputStream(new File(file));
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			return o;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private LineProcessor lineProcessor = new LineProcessorImpl();

	public void setLineProcessor(LineProcessor lp) {
		this.lineProcessor = lp;
	}

	public LineProcessor getLineProcessor() {
		return lineProcessor;
	}

	private LineCollector lineCollector = new LineCollectorImpl();

	public LineCollector getLineCollector() {
		return lineCollector;
	}

	public void setLineCollector(LineCollector lineCollector) {
		this.lineCollector = lineCollector;
	}

}
