/**
 * Graphical User Interface that displays a word count application
 * 
 * Features:
 * - Counts number of characters
 * - Counts number of syllables
 * - Counts number of words
 * - Counts number of sentences
 * - Edit text through the user interface
 * - Open a text file
 * - Save a text file
 */
package gui;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import document.Document;
import document.FileSelection;
import document.ReadWriteFile;

/**
 * @author Mickael Grivolat
 */

// Graphical User Interface that displays a word count application
public class GuiWordCount extends JFrame implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	private static ImageIcon logo;
	private static JFrame frame;
	private static JPanel panel1, panel2, panel3, panel4, panel5, blank1, blank2, blank3, blank4;
	private static JPopupMenu popup;
	private static JMenuItem copy, paste;
	private static JTextArea textArea1;
	private static JScrollPane scrollPane;
	private static JLabel label1, label2, label3, label4;
	private static JButton button1, button2;
	private static final int frameW = 800, frameH = 600;
	
	
	GuiWordCount() {
		setup();
	}
	
	// Creating instance of all GUI required resources
	private void setup() {
		// Set the frame of the GUI and load the logo
		logo = new ImageIcon("src\\data\\count.png");
		frame = new JFrame();
		frame.setSize(frameW, frameH);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Word Count");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.white);
		frame.setVisible(true);
		frame.setIconImage(logo.getImage());
		frame.addMouseListener(this);
		
		
		// Set the panel components in the frame.
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		blank1 = new JPanel();
		blank2 = new JPanel();
		blank3 = new JPanel();
		blank4 = new JPanel();

		panel1.setBackground(Color.white);
		panel2.setBackground(new Color(179,255,242)); //Color = Celeste
		panel3.setBackground(new Color(179,255,242));
		panel4.setBackground(new Color(179,255,242));
		panel5.setBackground(new Color(179,255,242));
		blank1.setBackground(new Color(179,255,242));
		blank2.setBackground(new Color(179,255,242));
		blank3.setBackground(new Color(179,255,242));
		blank4.setBackground(new Color(179,255,242));
		// central panel (panel1) is dynamic, no need to set size
		panel2.setPreferredSize(new Dimension(200, 200)); 
		panel3.setPreferredSize(new Dimension(50, 50));
		panel4.setPreferredSize(new Dimension(25, 25));
		panel5.setPreferredSize(new Dimension(75, 75));
		blank1.setPreferredSize(new Dimension(250, 0));	// blank panel, height = 0 pixel
		blank2.setPreferredSize(new Dimension(250, 0));	// blank panel, height = 0 pixel
		blank3.setPreferredSize(new Dimension(250, 0));	// blank panel, height = 0 pixel
		blank4.setPreferredSize(new Dimension(250, 0));	// blank panel, height = 0 pixel

		/* Layouts setup. Right panel has blank panels to create a line break.
		 * So, the layout gap is accounted doubled twice (height) */
		panel1.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));	// center panel
		panel2.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 15));	// right panel
		frame.add(panel1, BorderLayout.CENTER);
		frame.add(panel2, BorderLayout.EAST);
		frame.add(panel3, BorderLayout.WEST);
		frame.add(panel4, BorderLayout.NORTH);
		frame.add(panel5, BorderLayout.SOUTH);

		// Text field for user input. Set line wrap and word wrap for better visuals
		textArea1 = new JTextArea();
		textArea1.getDocument().addDocumentListener(addDocumentListener1());
		textArea1.addMouseListener(this);
		textArea1.setLineWrap(true);
		textArea1.setWrapStyleWord(true);
		panel1.add(textArea1);
		
		// Scroll pane needs to be added to the frame and not the text area
		scrollPane = new JScrollPane(textArea1);
		scrollPane.setBorder(null);
		frame.add(scrollPane);
		
		// Right-side panel setup
		// Label fields
		label1 = new JLabel("Characters: 0");
		label2 = new JLabel("Syllables: 0");
		label3 = new JLabel("Words: 0");
		label4 = new JLabel("Sentences: 0");
		label1.setFont(new Font("Dialogue", Font.BOLD, 13));
		label2.setFont(new Font("Dialogue", Font.BOLD, 13));
		label3.setFont(new Font("Dialogue", Font.BOLD, 13));
		label4.setFont(new Font("Dialogue", Font.BOLD, 13));
		
		// Button to open or save a file
		button1 = new JButton("Open File");
		button1.setFont(new Font("Dialogue", Font.BOLD, 13));
		button1.addActionListener(this);
		button2 = new JButton("Save File");
		button2.setFont(new Font("Dialogue", Font.BOLD, 13));
		button2.addActionListener(this);

		/* Right panel has blank panels to create a line break */
		panel2.add(label1);
		panel2.add(blank1);
		panel2.add(label2);
		panel2.add(blank2);
		panel2.add(label3);
		panel2.add(blank3);
		panel2.add(label4);
		panel2.add(blank4);
		panel2.add(button1);
		panel2.add(button2);
		
		// add menu items to popup
		popup = new JPopupMenu();
		copy = new JMenuItem("Copy");
		paste = new JMenuItem("Paste");
		popup.add(copy);
		popup.add(paste);
		copy.addActionListener(this);
		paste.addActionListener(this);
		
		// repaint the frame for newly added components
		frame.revalidate();
		frame.repaint();
	}
	
	// Action event handler for copy/paste menu functionalities, open file and save file (button 1-2)
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(copy)) {
			clipboardCopy();
		}
		if(e.getSource().equals(paste)) {
			clipboardPaste();
		}
		// Case Read File: create an instance of file picker and assign the content to the text area
		if(e.getSource().equals(button1)) {
			FileSelection selection = new FileSelection(false);
			try {
				ReadWriteFile file = new ReadWriteFile(selection.getFile().toString());
				textArea1.setText(file.getContent());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// Case Save File: create an instance of file picker and save the content to a file
		if(e.getSource().equals(button2)) {
			FileSelection selection = new FileSelection(true);
			try {
				ReadWriteFile file = new ReadWriteFile(selection.getFile(), true);
				file.write(textArea1.getText(), false);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/* Event manager for textArea1 display the number of char, syllables, words and sentences
	 * in the current text area
	 * Document Listener is called when anything is typed or deleted in a given text field
	 */
	private DocumentListener addDocumentListener1() {
		 return new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				Document docText = new Document(textArea1.getText());
				label1.setText("Characters: " + docText.getNumCharacters());
				label2.setText("Syllables: " + docText.getNumSyllables());
				label3.setText("Words: " + docText.getNumWords());
				label4.setText("Sentences: " + docText.getNumSentences());
			}
		 };
	}
	
	// Event manager for mouse actions
	@Override
	public void mouseClicked(MouseEvent e) {
		// Mouse pressed and released
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		mouseReleased(e);
		
	}
	/* isPopupTrigger should be checked in both mousePressedand mouseReleased 
	 * for proper cross-platform functionality.*/
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// Invoked when a mouse enters a component
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Invoked when a mouse exits a component
		
	}
	
	private void clipboardCopy() {
		StringSelection ss = new StringSelection(textArea1.getSelectedText());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}
	
	private void clipboardPaste() {
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor))
        {
            try {
				String text = (String)systemClipboard.getData(DataFlavor.stringFlavor);
				textArea1.insert(text, textArea1.getCaretPosition());
				textArea1.validate();
			} catch (Exception e2) {
				//handle exception
			}
        }
	}
	
	public static void main(String[] args) {
		new GuiWordCount();
		
	}
}
