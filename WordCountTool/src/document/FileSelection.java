package document;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * This utility class creates a file dialog box for loading and saving files.
 * @author Mickael Grivolat
 */

public class FileSelection {
	/* Opens a file dialogue box at the last used location and returns a file
	 * Use a different constructor to change behavior */
	private static File file;
    private static JFileChooser filePicker = new JFileChooser(new File(System.getProperty("user.dir")));
    static {
    	filePicker.setFileSelectionMode(JFileChooser.FILES_ONLY); // Open files only
    }
	
    /* Pops up a dialog box to open a file if TRUE, or save a file if FALSE */
    public FileSelection(boolean write) {
    	selectFile(write);
    }
    
    private File selectFile(boolean write) {
        int returnOption = 0;
    	
        if(write) {
        	returnOption = filePicker.showSaveDialog(null);
        } else {
        	returnOption = filePicker.showOpenDialog(null);
        }

        // if the user selects a file
        if (returnOption == JFileChooser.CANCEL_OPTION)
        {
        	file = null;
        } else {      	
        	file = filePicker.getSelectedFile();
        }
        
        return file;
    }
    // get method
    public File getFile() {
    	return file;
    }
    
    public static void main(String[] args) {
    	FileSelection fs = new FileSelection(true);
    	
    }
}
