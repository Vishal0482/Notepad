package notepad;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.Element;

public class NotePad extends JFrame implements ActionListener, WindowListener {
	
	private static final long serialVersionUID = 1L;
	JTextArea jta = new JTextArea();
	JTextArea line;
	File fnameContainer;
	JLabel label;

	NotePad() {
		Font fnt = new Font("Arial", Font.PLAIN, 15);
		Container con = getContentPane();
		
//		Menu bar
		JMenuBar jmb = new JMenuBar();
		
//		Menu list
		JMenu jmfile = new JMenu("File");
		JMenu jmedit = new JMenu("Edit");
		JMenu jmformate = new JMenu("Formate");
		JMenu jmhelp = new JMenu("Help");

		con.setLayout(new BorderLayout());
		
//		scroll bar
		JScrollPane sbrText = new JScrollPane(jta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sbrText.setVisible(true);

//		font and line wrap
		jta.setFont(fnt);
		jta.setLineWrap(true);
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
		createMenuItem(jmfile, "Exit");

		createMenuItem(jmedit, "Cut");
		createMenuItem(jmedit, "Copy");
		createMenuItem(jmedit, "Paste");
		
		createMenuItem(jmformate, "Word wrap");

		createMenuItem(jmhelp, "About Notepad");

//		adding menu item to the menu bar
		jmb.add(jmfile);
		jmb.add(jmedit);
		jmb.add(jmformate);
		jmb.add(jmhelp);

		setJMenuBar(jmb);
		
		label = new JLabel("||       Ln 1, Col 1           ",JLabel.RIGHT);
		con.add(label,BorderLayout.SOUTH);
		
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
	
//	event handler
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		if(e.getActionCommand().equals("New")) {
			this.setTitle("Untitled.txt - Notepad");
			jta.setText("");
			fnameContainer = null;	
		}
		else if(e.getActionCommand().equals("Open")) {
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
//		else if(e.getActionCommand().equals("Word wrap")) {
//			boolean wrapBool = true;
//			if(wrapBool == true){
//				jta.setLineWrap(false);		
//				wrapBool = false;
//			}
//			else if(wrapBool == false) {
//				jta.setLineWrap(true);
//				wrapBool = true;
//			}
//		}	
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
	
	public void Exiting() {
		System.exit(0);  
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
}
