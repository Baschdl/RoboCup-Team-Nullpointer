package de.null_pointer.navigation.test;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.null_pointer.navigation.map.Navigation;

public class FileHandler {
	private static Logger logger = Logger.getLogger(Navigation.class);

	private Handler handler = null;

	public FileHandler(Handler handler) {
		this.handler = handler;
	}

	public void saveFile(Frame actualWindow) {
		String filename;
		int rows = 0;
		BufferedWriter file = null;

		FileDialog fDialog = new FileDialog(actualWindow,
				"Select file to write", FileDialog.SAVE);
		fDialog.setVisible(true);

		int[][] values = handler.getValues();
		if (fDialog.getFile() != null) {
			try {
				filename = fDialog.getFile().toLowerCase();
				if (filename.indexOf(".map") == -1) {
					filename = filename + ".map";
				}
				file = new BufferedWriter(new FileWriter(fDialog.getDirectory()
						+ filename));

				StringBuffer data = new StringBuffer();
				data.insert(0, Integer.toString(values[0].length) + ";"
						+ Integer.toString(rows = values.length) + "\r\n");
				file.write(data.toString());

				for (int i = 0; i < rows; i++) {
					data = new StringBuffer(Integer.toString(values[i][0]));
					for (int j = 1; j < values[i].length; j++) {
						data.append(";" + Integer.toString(values[i][j]));
					}
					data.append("\r\n");
					file.write(data.toString());
				}
			} catch (IOException e) {
				logger.error("Fehler beim Erstellen der Datei");
			} finally {
				try {
					if (file != null) {
						file.close();
					}
				} catch (IOException e) {
				}
			}
		}
	}

	public void loadFile(Frame actualWindow) {
		String data;
		BufferedReader file = null;
		FileDialog fDialog = new FileDialog(actualWindow,
				"Choose file to load", FileDialog.LOAD);
		fDialog.setVisible(true);

		int[][] tempValues = null;
		int[] tempData = null;

		if (fDialog.getFile() != null) {
			try {
				file = new BufferedReader(new FileReader(fDialog.getDirectory()
						+ fDialog.getFile()));
				int i = -1;
				while ((data = file.readLine()) != null) {
					if (i == -1) {
						tempData = parseData(readData(data, 2));
						tempValues = new int[tempData[1]][tempData[0]];
					} else {
						tempValues[i] = parseData(readData(data,
								tempValues[i].length));
					}
					i++;
				}
			} catch (Exception e) {
				logger.error("Fehler beim Lesen der Datei");
			} finally {
				try {
					if (file != null) {
						file.close();
					}
				} catch (IOException e) {
				}
			}
			handler.setValues(tempValues);
		}
	}

	private String[] readData(String dataString, int numberData) {
		String puf[] = new String[numberData];
		// TODO: Siehe Unterrichtsmaterial, Kommentare ergaenzen
		int index1 = 0, index2 = 0;
		for (int i = 0; i < numberData; i++) {
			if (index1 < dataString.length()) {
				index2 = dataString.indexOf(";", index1);
				if (index2 >= 0) {
					puf[i] = dataString.substring(index1, index2);
					index1 = index2 + 1;
				} else {
					puf[i] = dataString.substring(index1);
				}
			}
		}
		return puf;
	}

	private int[] parseData(String[] data) {
		int numberData = data.length;
		int[] dataInt = new int[numberData];
		// Wandelt die Strings in Integer um und speichert sie ab
		for (int i = 0; i < numberData; i++) {
			if (data[i].matches("[-0-9]+")) {
				dataInt[i] = Integer.parseInt(data[i]);
			} else {
				logger.error("Ungueltige Zeichen im gelesenen String");
				return null;
			}
		}
		return dataInt;
	}

}