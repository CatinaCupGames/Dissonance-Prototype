package com.dissonance.framework.system.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class FileUtils {

    /**
     * Creates the directory/file if it doesn't exist.
     * @param path - The directory to create.
     * @param fileName - The file to create.
     * @param contents - The contents inside the file.
     * @throws IOException - Signals that an I/O exception has occurred.
     */
    public static void createIfNotExist(String path, String fileName, String contents) throws IOException {
        File filePath = new File(path);
        File fileFile = new File(path, fileName);

        boolean f = filePath.mkdirs();
        if (!f)
            throw new IOException("Failed to make parent directories!");

        if (!fileFile.exists()) {
            f = fileFile.createNewFile();
            if (!f)
                throw new IOException("Failed to create file!");

            PrintWriter writer = new PrintWriter(fileFile);
            writer.write(contents);

            try {
                writer.close();
                writer.flush();
            }
            finally {
                writer = null;
            }
        }
    }

    /**
     * Create all child directories contained in <b>filepath</b> if they
     * do not exist
     * @param filepath
     *                The full filepath to create child directories for
     */
    public static void createChildDirectories(String filepath) {
        String[] dirs = filepath.split("/");
        String path = "";
        for (String directory : dirs) {
            path += (path.equals("") ? directory : "/" + directory);
            if (!directory.contains(".") && !new File(path).exists())
                new File(path).mkdir();
        }
    }

    /**
     * Creates a file if it does not exists.
     *
     * @param path - The directory of the file
     * @param fileName - The name of the file
     *
     * @throws IOException - If there's a problem writing the file
     */
    public static void createIfNotExist(String path, String fileName) throws IOException {
        createIfNotExist(path, fileName, "");
    }

    /**
     * Creates a file if it does not exists.
     *
     * @param fileName - The name of the file
     *
     * @throws IOException - If there's a problem writing the file
     */
    public static boolean createIfNotExist(String fileName) throws IOException {
        File file = new File(fileName);
        return file.exists() || file.createNewFile();
    }

    /**
     * Delete the file/directory if exist.
     *
     * @param filePath - The path of the file/directory to delete.
     */
    public static boolean deleteIfExist(String filePath) {
        File file = new File(filePath);
        return !file.exists() || file.delete();
    }

    /**
     * Writes the specified line to the specified file, creating the file if doesn't exist
     *
     * @param filePath - The path of the file to write to
     * @param text - The text to write to the specified file
     *
     * @throws IOException If there's an error while writing to the file
     */
    public static void writeText(String filePath, String text) throws IOException {
        createIfNotExist(filePath);
        Formatter formatter = new Formatter(new FileWriter(new File(filePath), true));
        formatter.out().append(text).append("\r\n");
        formatter.close();
    }

    /**
     * Writes the specified string array to the specified file, creating the file if it doesn't exist
     *
     * @param filePath - The path of the file to write to
     * @param lines - The string array to write to the specified file
     *
     * @throws IOException If there's an error while writing to the file
     */
    public static void writeLines(String filePath, String... lines) throws IOException {
        createIfNotExist(filePath);
        Formatter formatter = new Formatter(new FileWriter(new File(filePath), true));
        for (String line : lines) {
            formatter.out().append(line).append("\r\n");
        }
        formatter.close();
    }

    /**
     * Reads the contents of the specified file
     *
     * @param filePath - The path of the file to read from
     *
     * @return A string array with the contents of the read file
     * @throws IOException If there's an error while reading from the file
     */
    public static String[] readAllLines(String filePath) throws IOException {
        List<String> lines = readToList(filePath);
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * Reads the contents of the specified file
     *
     * @param filePath - The path of the file to read from
     *
     * @return A string list with the contents of the read file
     * @throws IOException If there's an error while reading from the file
     */
    public static List<String> readToList(String filePath) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(filePath));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null)
            lines.add(line);
        reader.close();
        return lines;
    }

    /**
     * Copy an existing file to a new location.
     *
     * @param sourcePath The original file to be copied
     * @param newPath The new location for the copy to go
     *
     * @return Returns true if the copy was successful, returns false if an error occurred.
     */
    public static boolean copyFile(String sourcePath, String newPath) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try{

            File afile =new File(sourcePath);
            File bfile =new File(newPath);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.close();

            return true;

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the total number of lines the specified file has
     *
     * @param filePath - The path of the file to check for
     *
     * @return An integer representing the number of lines the file has
     * @throws IOException If there's an error while reading from the file
     */
    public static int getLineNumber(String filePath) throws IOException {
        return readToList(filePath).size();
    }

    /**
     * Checks whether the specified file exists
     *
     * @param filePath - the file path to check
     */
    public static boolean exists(String filePath) {
        return new File(filePath).exists();
    }
}
