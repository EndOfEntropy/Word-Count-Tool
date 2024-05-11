package document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/** 
 * A class that opens, read a file and stores the text in a String.
 * A class that saves a file to a text file.
 * 
 * @author Mickael Grivolat
 */

public class ReadWriteFile {

	private String path, content;
	private File file;
	
	/* Reads a file and assign the content locally */
	public ReadWriteFile(File f) throws IOException{
		setupRead(f);
	}
	/* Reads a file and assign the content locally */
	public ReadWriteFile(String directory) throws IOException{
		setupRead(directory);
	}
	/* Reads or writes a file and saves it as a text file */
	public ReadWriteFile(File f, boolean write) throws IOException{
		if(write) {
			setupWrite(f);
		} else {
			setupRead(f);
		}
	}
	/* Reads or writes a file and saves it as a text file */
	public ReadWriteFile(String directory, boolean write) throws IOException{
		if(write) {
			setupWrite(directory);
		} else {
			setupRead(directory);
		}
	}
	
	// get methods
	public String getPath() {
		return path;
	}
	
	public String getContent() {
		return content;
	}
	
	public File getFile() {
		return file;
	}
	
    // Create from a given File
    private void setupRead (File f) throws IOException {
        try {
        	setupRead(f.getCanonicalPath());
        }
        catch (Exception e) {
        	throw new IOException("File cannot be accessed: " + f, e);
        }
    }
    
    // Create from a given file name
	private void setupRead(String directory) throws IOException {
		path = directory;
		InputStream inputStream = null;
		
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(directory);
            if (inputStream == null) {
            	inputStream = new FileInputStream(directory);
            }
            content = readSourceContent(inputStream);
        }
        catch (Exception e) {
        	throw new IOException("File cannot be read: " + directory, e);
        }
	}
	
	// Reads the content of a file and returns it into a string
	private String readSourceContent(InputStream inputStream) throws IOException {
		BufferedReader buffer = null;
		
		try {
        	buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        	StringBuilder sb = new StringBuilder();
        	String line;
    		while((line = buffer.readLine()) != null) {
    			sb.append(line + "\n");
    		}
    		return sb.toString();
    		
		} catch (Exception e) {
			throw new IOException("File cannot be read: " + path, e);
			
		} finally {
            try {
                if (buffer != null) {
                	buffer.close();
                }
            }
            catch (Exception e) {
            	e.printStackTrace();
            }
		}
	}
	
	// create file for writing from File
	private void setupWrite(File f) throws IOException {
        try {
            file = f;
            if (f.exists() && f.canWrite()) {
                setupRead(f.getCanonicalPath());
            }
            else {
            	// content is set by using the write method
            	content = "";
                path = f.getCanonicalPath();
            }
        }
        catch (Exception e) {
            throw new IOException("File cannot be accessed: " + f, e);
        }
	}
	
    // create file for writing from path
    private void setupWrite (String fileName) throws IOException {
        try {
            // A URL resource can be something as simple as a file or a directory
        	URL loc = getClass().getClassLoader().getResource(fileName);
            if (loc != null) {
            	fileName = loc.toString();
            }
            setupWrite(new File(fileName));
        }
        catch (Exception e) {
            throw new IOException("File cannot be accessed: " + fileName);
        }
    }
    
    /* Writes a string to the end of this file. */
    public void write (String s, boolean append) throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s);
        write(list, append);
    }
    
    /* Writes a list of strings to the end of this file, one element per line. */
    public void write (String[] list, boolean append) throws IOException {
        write(new ArrayList<String>(Arrays.asList(list)), append);
    }
    
    /* Writes a list of strings to the end of this file, one element per line. 
     * Use boolean to replace text entirely or append to the file. 
     */
    public void write (ArrayList<String> list, boolean append) throws IOException {
        if (file != null) {
            // build string to save
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s);
                sb.append("\n");
            }
            // save it locally (so it can be read later)
            content += sb.toString();
            // save it externally to the file
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter(file, append));
                writer.println(sb.toString());
            }
            catch (Exception e) {
                throw new IOException("File cannot be changed: " + file);
            }
            finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                }
                catch (Exception e) {
                    // do nothing
                }
            }
        }
    }
	
	public static void main(String[] args) throws IOException {
		// ReadWriteFile source = new ReadWriteFile("src\\Test File.txt");		
		String str = "This text should be found in the newly saved file";
		
		FileSelection selection = new FileSelection(true);
		ReadWriteFile fileToWrite = new ReadWriteFile(selection.getFile(), true);
		fileToWrite.write(str, false);
	}
	
}
