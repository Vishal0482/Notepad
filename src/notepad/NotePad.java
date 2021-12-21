package notepad;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;

import javax.swing.event.*;
import javax.swing.text.Element;

public class NotePad extends JFrame implements ActionListener, WindowListener, ItemListener, ListSelectionListener {
	
	private static final long serialVersionUID = 1L;
	JTextArea jta = new JTextArea();
	JTextArea line;
	JCheckBox c,c1;
	File fnameContainer;
	JLabel label;
	Frame frame = new Frame();
	JList<String> styleList,fontList,sizeList;
	JDialog fontDialog;

	NotePad() {
		Font fnt = new Font("Arial", Font.PLAIN, 15);
		Container con = getContentPane();
		
//		Menu bar
		JMenuBar jmb = new JMenuBar();
		
//		Menu list
		JMenu jmfile = new JMenu("File");
		JMenu jmedit = new JMenu("Edit");
		JMenu jmformate = new JMenu("Formate");
		JMenu jmview = new JMenu("View");
		JMenu jmzoom = new JMenu("zoom");
		JMenu jmhelp = new JMenu("Help");
		
		con.setLayout(new BorderLayout());
		
//		scroll bar
		JScrollPane sbrText = new JScrollPane(jta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sbrText.setVisible(true);

//		font and line wrap
		jta.setFont(fnt);
//		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		
//		new text area(nested) for line numbers
		line = new JTextArea("1");
		line.setFont(fnt);
		line.setBackground(Color.LIGHT_GRAY);
		sbrText.getViewport().add(jta);
		sbrText.setRowHeaderView(line);
		line.setEditable(false);
		
//		line number printer
		jta.getDocument().addDocumentListener(new DocumentListener() {
			public String getText() {
	            int caretPosition = jta.getDocument().getLength();
	            Element root = jta.getDocument().getDefaultRootElement();
	            String text = "1" + System.getProperty("line.separator");
	               for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
	                  text += i + System.getProperty("line.separator");
	               }
	            return text;
	         }

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				line.setText(getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				line.setText(getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				line.setText(getText());
			}
	         
		});
		
//		adding scroll bar to container
		con.add(sbrText);
		sbrText.getViewport().add(jta);
		sbrText.setRowHeaderView(line);

//		creating menu item for individual menus using createMenuItem method
		createMenuItem(jmfile, "New");
		createMenuItem(jmfile, "Open");
		createMenuItem(jmfile, "Save");
		jmfile.addSeparator();
		createMenuItem(jmfile, "Print...");
		jmfile.addSeparator();
		createMenuItem(jmfile, "Exit");

		createMenuItem(jmedit, "Cut");
		createMenuItem(jmedit, "Copy");
		createMenuItem(jmedit, "Paste");
		createMenuItem(jmedit, "Delete");
		jmedit.addSeparator();
		createMenuItem(jmedit, "Find");
		createMenuItem(jmedit, "Replace");
		jmedit.addSeparator();
		createMenuItem(jmedit, "Select All");
		
//		creating check box object and adding into format menu
		c = new JCheckBox("Word wrap",true);
		c.addItemListener(this);
		jmformate.add(c);
		
		createMenuItem(jmformate, "Fonts..");
		JMenuItem zoomIn = new JMenuItem("Zoom In");
		JMenuItem zoomOut = new JMenuItem("Zoom Out");
		JMenuItem zoomRestore = new JMenuItem("Restore Default Zoom");
		jmzoom.add(zoomIn);
		jmzoom.add(zoomOut);
		jmzoom.add(zoomRestore);
		jmview.add(jmzoom);
		
		c1 = new JCheckBox("Status Bar",true);
		c1.addItemListener(this);
		jmview.add(c1);

		createMenuItem(jmhelp, "About Notepad");
		
//		adding menu item to the menu bar
		jmb.add(jmfile);
		jmb.add(jmedit);
		jmb.add(jmformate);
		jmb.add(jmview);
		jmb.add(jmhelp);

		setJMenuBar(jmb);
		
		label = new JLabel("||       Ln 1, Col 1           ",JLabel.RIGHT);
		con.add(label,BorderLayout.SOUTH);
		
//		Font style dialog box
		fontDialog = new JDialog(frame,"Fonts");
		fontDialog.setSize(400,650);
		
		JPanel jp = new JPanel(new GridLayout(1,3));
		
		JPanel jpfont = new JPanel(new GridLayout(2,1));
		JLabel fontLabel = new JLabel("Fonts");
		jpfont.add(fontLabel);
		String fonts[] = {"Arial","Calibri","Cambria","Consolas","Courier","Georgia","Terminal","Times New Roman"};
		fontList = new JList<String>(fonts);
		fontList.setSelectedIndex(0);
		jpfont.add(fontList);
		jp.add(jpfont);
		fontList.addListSelectionListener(this);
		
		JPanel jpStyle = new JPanel(new GridLayout(2,1));
		JLabel fontStyle = new JLabel("Font Style:");
		jpStyle.add(fontStyle);
		String style[] = {"Regular", "Bold", "Italic"};
		styleList = new JList<String>(style);
		styleList.setSelectedIndex(0);
		jpStyle.add(styleList);
		jp.add(jpStyle);
		styleList.addListSelectionListener(this);
		
		JPanel jpSize = new JPanel(new GridLayout(2,1));
		JLabel fontSize = new JLabel("Font Size:");
		jpSize.add(fontSize);
		String size[] = {"8","9","10","11","12","14","16","18","20","22","24","26","28","36","48","72"};
		sizeList = new JList<String>(size);
		sizeList.setSelectedIndex(4);
	
//		JScrollPane fontScroll = new JScrollPane(jpSize);
//		fontScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		fontScroll.setVisible(true);

		jpSize.add(sizeList);
		jp.add(jpSize);
		sizeList.addListSelectionListener(this);
		
		fontDialog.add(jp);
		
//		line and column number counter
		jta.addCaretListener(new CaretListener() {  
			public void caretUpdate(CaretEvent e)  {  
				int lineNumber=0, column=0, pos=0;  
				  try {  
					pos=jta.getCaretPosition();  
					lineNumber=jta.getLineOfOffset(pos);  
					column=pos-jta.getLineStartOffset(lineNumber);  
				}
				catch(Exception excp){}  
				if(jta.getText().length()==0){
					lineNumber=0; column=0;
				}  
				label.setText("||       Ln "+(lineNumber+1)+", Col "+(column+1)+"           ");  
			}  
		});  
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("Notepad.gif"));
		addWindowListener(this);
		setSize(500, 500);
		setTitle("Untitled.txt - Notepad");
		setVisible(true);
	}
	
//	method to create menu items
	public void createMenuItem(JMenu jm, String txt) {
		JMenuItem jmi = new JMenuItem(txt);
		jmi.addActionListener(this);
		jm.add(jmi);
	}
	
	JFileChooser jfc = new JFileChooser();
	public void saveChanges() {
		if(fnameContainer!= null) {
			jfc.setCurrentDirectory(fnameContainer);
			jfc.setSelectedFile(fnameContainer);
		}
		else {
			jfc.setSelectedFile(new File("Untitled.txt"));
		}
		
		int ret = jfc.showSaveDialog(null);
		if(ret == JFileChooser.APPROVE_OPTION) {
			try {
				File fyl = jfc.getSelectedFile();
				saveFile(fyl.getAbsolutePath());
				this.setTitle(fyl.getName()+" - Notepad");
				fnameContainer = fyl;
			}
			catch(Exception ets) {}	
		}
	}
//	event handler
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New")) {
			this.setTitle("Untitled.txt - Notepad");
			jta.setText("");
			fnameContainer = null;	
		}
		else if(e.getActionCommand().equals("Open")) {
			DialogBox();
			int ret = jfc.showDialog(null,"Open");
			if(ret==JFileChooser.APPROVE_OPTION) {
				try{
					File fyl = jfc.getSelectedFile();
					OpenFile(fyl.getAbsolutePath());
					this.setTitle(fyl.getName()+"- Notepad");
					fnameContainer = fyl;
				}
				catch(IOException ers){}
			}
		}
		else if(e.getActionCommand().equals("Save")) {
			saveChanges();
		}
		else if(e.getActionCommand().equals("Print...")) {
			try {
				jta.print();
			} catch (PrinterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals("Exit")) {
			Exiting();
		}
		else if(e.getActionCommand().equals("Copy")) {
			jta.copy();
		}
		else if(e.getActionCommand().equals("Paste")) {
			jta.paste();
		}
		else if(e.getActionCommand().equals("Cut")) {
			jta.cut();
		}	
		else if(e.getActionCommand().equals("Delete")) {
			jta.replaceSelection("");
		}	
		else if(e.getActionCommand().equals("Find")) {
			String findText = JOptionPane.showInputDialog(this,"Find What:");
			String text = jta.getText();
			System.out.print(text);
			int index = text.indexOf(findText);
			jta.select(index,findText.length());
		}	
		else if(e.getActionCommand().equals("Replace")) {
			
			JPanel panel = new JPanel();
			JTextField findText1 = new JTextField(5);
			JTextField replaceText1 = new JTextField(5);
			
			panel.add(new JLabel("Find"));
			panel.add(findText1);
			panel.add(Box.createHorizontalStrut(15));
			panel.add(new JLabel("Replace"));
			panel.add(replaceText1);
			
			JOptionPane.showConfirmDialog(null,panel,"Replace",JOptionPane.INFORMATION_MESSAGE);
			
			String replaceText = replaceText1.getText();
			String findText = findText1.getText();
			String text = jta.getText();
			int index = text.indexOf(findText);
			jta.select(index,findText.length());
			if(index != 0) {
				jta.replaceSelection(replaceText);
			}
		}	
		else if(e.getActionCommand().equals("Select All")) {
			jta.selectAll();
		}	
		else if(e.getActionCommand().equals("Fonts..")) {
			fontDialog.setVisible(true);
		}	
		else if(e.getActionCommand().equals("About Notepad")) {
			JOptionPane.showMessageDialog(this,"Created by vishal parmar","Notepad", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
//	method to open new file
	public void OpenFile(String fname) throws IOException{
		BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
		String l;
		jta.setText("");
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		while((l=d.readLine())!=null) {
			jta.setText(jta.getText()+l+"\r\n");			
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		d.close();
	}
	
//	method to save file
	public void saveFile(String fname) throws IOException{
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		DataOutputStream o = new DataOutputStream(new FileOutputStream(fname));
		o.writeBytes(jta.getText());
		o.close();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		Exiting();
	}
	int a;
	public void DialogBox() {
		a = JOptionPane.showConfirmDialog(jta, "Do yo want to save changes to Untitled?");
	}
	
	public void Exiting() {
		DialogBox();
		if(a==JOptionPane.YES_OPTION) {
			saveChanges();
			System.exit(0);
		}
		else if(a==JOptionPane.NO_OPTION) {
			System.exit(0);
		}
		else if(a==JOptionPane.CANCEL_OPTION) {
			
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
//		condition for word wrap
		if(e.getSource() == c) {
			if(e.getStateChange() == 1 ) {
				jta.setLineWrap(true);
			}
			else {
				jta.setLineWrap(false);
			}
		}
//		condition for status bar visibility toggle
		else {
			if(e.getStateChange() == 1 ) {
				label.setVisible(true);
			}
			else {
				label.setVisible(false);
			}	
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		int ss = sizeList.getSelectedIndex();
		int size[] = {8,9,10,11,12,14,16,18,20,22,24,26,28,36,48,72};
		
		int ff = fontList.getSelectedIndex();
		String fonts[] = {"Arial","Calibri","Cambria","Consolas","Courier","Georgia","Terminal","Times New Roman"};
		
		if(styleList.getSelectedValue().equals("Bold")) {
			jta.setFont(new Font(fonts[ff],Font.BOLD,size[ss]));
			line.setFont(new Font(fonts[ff],Font.BOLD,size[ss]));
		}
		if(styleList.getSelectedValue().equals("Regular")) {
			jta.setFont(new Font(fonts[ff],Font.PLAIN,size[ss]));
			line.setFont(new Font(fonts[ff],Font.BOLD,size[ss]));
		}
		if(styleList.getSelectedValue().equals("Italic")) {
			jta.setFont(new Font(fonts[ff],Font.ITALIC,size[ss]));
			line.setFont(new Font(fonts[ff],Font.BOLD,size[ss]));
		}
	}
}
