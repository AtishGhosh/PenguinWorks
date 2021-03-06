import java.awt.*;
import javax.swing.*;
import java.io.*;

import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.net.URI;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.GraphicsEnvironment;

public class Notepad extends JFrame
{
    private JFrame frame;
    private JMenuBar menu;
    private JTextArea textspace;
    private JScrollPane spV;
    private JScrollPane spH;
    private boolean FilePresent;
    private String fpath;
    private String fontType;
    private String[] availableColours;
    private int textColour;
    private int backColour;
    private int fontStyle;
    private int fontSize;
    Notepad () {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        frame = new JFrame("Notepad");
        menu = new JMenuBar();
        textspace = new JTextArea();
        spV = new JScrollPane(textspace);
        spH = new JScrollPane(textspace);
        fpath = "";
        FilePresent = false;
        fontType = "Dialog";
        fontStyle = Font.PLAIN;
        fontSize = 12;
        availableColours = new String[]{"Black", "Blue", "Cyan", "Dark Gray", "Gray", "Green", "Light Gray", "Magenta", "Orange", "Pink", "Red", "White", "Yellow"};
        textColour = 0;
        backColour = 11;
    }
    public static void main (String[] args)
    {
        new Notepad().Interface();
    }
    private void SaveFileUI()
    {
        String texttosave = textspace.getText();
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(fpath));
            os.write(texttosave.getBytes(), 0, texttosave.length());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                os.close();
            } catch (IOException e) { }
        }
        JFrame f = new JFrame ("Saved To");
        JOptionPane.showMessageDialog(f,"File saved as "+fpath);
    }
    private String getExtensionForFilter (javax.swing.filechooser.FileFilter filtername)
    {
        if (filtername.getDescription() == "Text document (.txt)")
        {
            return ".txt";
        }
        else
        {
            return "";
        }
    }
    private void SaveFileAsUI()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setSelectedFile(new File("Untitled"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text document (.txt)", "txt"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            fpath = fileToSave.getAbsolutePath();
            String extension = getExtensionForFilter(fileChooser.getFileFilter());
            if(!fpath.endsWith(extension))
            {
                fileToSave = new File(fpath + extension);
            }
            String texttosave = textspace.getText();
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File(fileToSave.getAbsolutePath()));
                os.write(texttosave.getBytes(), 0, texttosave.length());
            } catch (IOException e) {  }
            finally {
                try {
                    os.close();
                } catch (IOException e) {  }
            }
            FilePresent = true; frame.setTitle("Notepad - "+fileToSave.getName());
            JFrame f = new JFrame ("Saved To");
            JOptionPane.showMessageDialog(f,"File saved as "+fileToSave.getAbsolutePath());
        }
    }
    private void OpenFileUI ()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text document (.txt)", "txt"));
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            String extension = getExtensionForFilter(fileChooser.getFileFilter());
            if(!((String)(fileToOpen.getName())).endsWith(extension)) {
                fileToOpen = new File(fileToOpen.getAbsolutePath() + extension);
            }
            if (!fileToOpen.exists()) {
                JFrame f = new JFrame ("Saved To");
                JOptionPane.showMessageDialog(f,fileToOpen.getName()+" does not exist in this folder.");
            }
            else {
                fpath = fileToOpen.getAbsolutePath();
                frame.setTitle("Notepad - " + fileToOpen.getName());
                FilePresent = true;
            }
        } else {FilePresent = false;}
        try {
            BufferedReader br=new BufferedReader(new FileReader(fpath));
            String s1="",s2="";
            while((s1=br.readLine())!=null){
                s2+=s1+"\n";
            }
            textspace.setText(s2);
            br.close();
        } catch (Exception e) { }
    }
    private void AboutUI ()
    {
        JFrame aboutbox = new JFrame ();
        aboutbox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aboutbox.setSize(400,200);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
        JOptionPane.showMessageDialog(aboutbox," \nNotepad\n\nby Atish Ghosh\n2020\nversion 1.0\n ");
    }
    private void FontTypeUI ()
    {
        JFrame f = new JFrame ("Font Type");
        JOptionPane optionPane = new JOptionPane();
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontnames = e.getAvailableFontFamilyNames();
        int i;
        for (i = fontnames.length -1; i > 0; i--) {
            if ( fontnames[i] == fontType)
            { break; }
        }
        String selected = (String)optionPane.showInputDialog(f, "Select Font Type", "Font Type", JOptionPane.QUESTION_MESSAGE, null, fontnames, fontnames[i]);
        if (selected != null)
        {
            fontType = selected;
            textspace.setFont(new Font(fontType, fontStyle, fontSize));
        }
    }
    private void FontSizeUI ()
    {
        JFrame f = new JFrame ("Font Type");
        JOptionPane optionPane = new JOptionPane();
        String newFontSize = JOptionPane.showInputDialog(f,"Enter Font Size", fontSize);
        int selected = 0;
        try {
            selected = Integer.parseInt(newFontSize);
        }
        catch (Exception e) {
            if ( newFontSize != null )
            {JOptionPane.showMessageDialog(f, "Font size requires a number.");}
            return;
        }
        if (selected == 0 )
        {
            JOptionPane.showMessageDialog(f, "Font size cannot be zero.");
        }
        else if (selected < 0 )
        {
            JOptionPane.showMessageDialog(f, "Font size requires a positive number.");
        }
        else
        {
            fontSize = selected;
            textspace.setFont(new Font(fontType, fontStyle, fontSize));
        }
    }
    private void FontStyleUI ()
    {
        JFrame f = new JFrame ("Font Type");
        JOptionPane optionPane = new JOptionPane();
        String[] fontstyles = {"Plain","Bold","Italic"};
        int i=0;
        if (fontStyle==Font.BOLD) { i=1; }
        else if (fontStyle==Font.ITALIC) { i=2; }
        String selected = (String)optionPane.showInputDialog(f, "Select Font Style", "Font Style", JOptionPane.QUESTION_MESSAGE, null, fontstyles, fontstyles[i]);
        if (selected != null)
        {
            if (selected=="Plain") { fontStyle = Font.PLAIN; }
            else if (selected=="Bold") { fontStyle = Font.BOLD; }
            else if (selected=="Italic") { fontStyle = Font.ITALIC; }
            else {return;}
            textspace.setFont(new Font(fontType, fontStyle, fontSize));
        }
        else {return;}
    }
    private void TextUI ()
    {
        JFrame f = new JFrame ("Theme");
        JOptionPane optionPane = new JOptionPane();
        String selected = (String)optionPane.showInputDialog(f, "Select Text Colour", "Text Colour", JOptionPane.QUESTION_MESSAGE, null, availableColours, availableColours[textColour]);
        if (selected == "Black") {textspace.setForeground(Color.BLACK); textColour=0;}
        else if (selected == "Blue") {textspace.setForeground(Color.BLUE); textColour=1;}
        else if (selected == "Cyan") {textspace.setForeground(Color.CYAN); textColour=2;}
        else if (selected == "Dark Gray") {textspace.setForeground(Color.DARK_GRAY); textColour=3;}
        else if (selected == "Gray") {textspace.setForeground(Color.GRAY); textColour=4;}
        else if (selected == "Green") {textspace.setForeground(Color.GREEN); textColour=5;}
        else if (selected == "Light Gray") {textspace.setForeground(Color.LIGHT_GRAY); textColour=6;}
        else if (selected == "Magenta") {textspace.setForeground(Color.MAGENTA); textColour=7;}
        else if (selected == "Orange") {textspace.setForeground(Color.ORANGE);  textColour=8;}
        else if (selected == "Pink") {textspace.setForeground(Color.PINK); textColour=9;}
        else if (selected == "Red") {textspace.setForeground(Color.RED); textColour=10;}
        else if (selected == "White") {textspace.setForeground(Color.WHITE); textColour=11;}
        else if (selected == "Yellow") {textspace.setForeground(Color.YELLOW); textColour=12;}
        else return;
    }
    private void BackgroundUI ()
    {
        JFrame f = new JFrame ("Theme");
        JOptionPane optionPane = new JOptionPane();
        String selected = (String)optionPane.showInputDialog(f, "Select Background Colour", "Background Colour", JOptionPane.QUESTION_MESSAGE, null, availableColours, availableColours[backColour]);
        if (selected == "Black") {textspace.setBackground(Color.BLACK); backColour=0;}
        else if (selected == "Blue") {textspace.setBackground(Color.BLUE); backColour=1;}
        else if (selected == "Cyan") {textspace.setBackground(Color.CYAN); backColour=2;}
        else if (selected == "Dark Gray") {textspace.setBackground(Color.DARK_GRAY); backColour=3;}
        else if (selected == "Gray") {textspace.setBackground(Color.GRAY); backColour=4;}
        else if (selected == "Green") {textspace.setBackground(Color.GREEN); backColour=5;}
        else if (selected == "Light Gray") {textspace.setBackground(Color.LIGHT_GRAY); backColour=6;}
        else if (selected == "Magenta") {textspace.setBackground(Color.MAGENTA); backColour=7;}
        else if (selected == "Orange") {textspace.setBackground(Color.ORANGE);  backColour=8;}
        else if (selected == "Pink") {textspace.setBackground(Color.PINK); backColour=9;}
        else if (selected == "Red") {textspace.setBackground(Color.RED); backColour=10;}
        else if (selected == "White") {textspace.setBackground(Color.WHITE); backColour=11;}
        else if (selected == "Yellow") {textspace.setBackground(Color.YELLOW); backColour=12;}
        else return;
    }
    private void MenuBarUI ()
    {
        JMenu menu1 = new JMenu("File");
        JMenu menu2 = new JMenu("Edit");
        JMenu menu3 = new JMenu("More");
        JMenuItem more1 = new JMenuItem("Feedback");
        JMenuItem more2 = new JMenuItem("About");
        JMenu edit1 = new JMenu("Font");
        JMenuItem font1 = new JMenuItem("Type");
        JMenuItem font2 = new JMenuItem("Size");
        JMenuItem font3 = new JMenuItem("Style");
        JMenu edit2 = new JMenu("Theme");
        JMenuItem theme1 = new JMenuItem ("Text");
        JMenuItem theme2 = new JMenuItem ("Background");
        JMenuItem file1 = new JMenuItem("New      ");
        JMenuItem file2 = new JMenuItem("Open     ");
        JMenuItem file3 = new JMenuItem("Save     ");
        JMenuItem file4 = new JMenuItem("Save As  ");

        // JMenu "File"
        file1.setAccelerator(KeyStroke.getKeyStroke("control N"));
        file1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                new Notepad().Interface();
            }
        });
        menu1.add(file1);
        file2.setAccelerator(KeyStroke.getKeyStroke("control O"));
        file2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                OpenFileUI();
            }
        });
        menu1.add(file2);
        file3.setAccelerator(KeyStroke.getKeyStroke("control S"));
        file3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                if (FilePresent == false) {
                    SaveFileAsUI();
                }
                else {
                    SaveFileUI();
                }
            }
        });
        menu1.add(file3);
        file4.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        file4.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                SaveFileAsUI();
            }
        });
        menu1.add(file4);
        more1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI uri = URI.create("mailto:atishghosh30@gmail.com?subject=Notepad%20Feedback");
                    desktop.mail(uri);
                } catch (Exception e) {  }
            }
        });

        // JMenu "Edit"
        font1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                FontTypeUI();
            }
        });
        edit1.add(font1);
        font2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                FontSizeUI();
            }
        });
        edit1.add(font2);
        font3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                FontStyleUI();
            }
        });
        edit1.add(font3);
        menu2.add(edit1);
        theme1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                TextUI();
            }
        });
        edit2.add(theme1);
        theme2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                BackgroundUI();
            }
        });
        edit2.add(theme2);
        menu2.add(edit2);

        // JMenu "More"
        menu3.add(more1);
        more2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0) {
                AboutUI();
            }
        });
        menu3.add(more2);


        menu.add(menu1);
        menu.add(menu2);
        menu.add(menu3);
    }
    private void SaveFileAsOnClose ()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setSelectedFile(new File("Untitled"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text document (.txt)", "txt"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            fpath = fileToSave.getAbsolutePath();
            String extension = getExtensionForFilter(fileChooser.getFileFilter());
            if(!fpath.endsWith(extension))
            {
                fileToSave = new File(fpath + extension);
            }
            String texttosave = textspace.getText();
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File(fileToSave.getAbsolutePath()));
                os.write(texttosave.getBytes(), 0, texttosave.length());
            } catch (IOException e) {  }
            finally {
                try {
                    os.close();
                } catch (IOException e) {  }
            }
            JFrame f = new JFrame ("Saved To");
            JOptionPane.showMessageDialog(f,"File saved as "+fileToSave.getAbsolutePath());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(false);
            frame.dispose();
        }
    }
    private void pullThePlug()
    {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
        setVisible(false);
        dispose();
        System.exit(0);
    }
    public void Interface ()
    {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        int result = JOptionPane.showConfirmDialog(frame, "Save file before closing?");
                        if (result==JOptionPane.YES_OPTION){
                            if (FilePresent == false) {
                                SaveFileAsOnClose();
                            }
                            else {
                                SaveFileUI();
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(false);
                                frame.dispose();
                            }
                        }
                        else if (result==JOptionPane.NO_OPTION) {
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setVisible(false);
                            frame.dispose();
                        }
                        else
                        {  }
                    }
                });
            }
        };
        textspace.setWrapStyleWord(true);
        MenuBarUI();
        textspace.setFont(new Font(fontType, fontStyle, fontSize));
        spH.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(BorderLayout.NORTH, menu);
        frame.getContentPane().add(BorderLayout.CENTER, textspace);
        frame.getContentPane().add(BorderLayout.SOUTH, spH);
        frame.getContentPane().add(BorderLayout.EAST, spV);
        spH.setViewportView(textspace);
        spV.setViewportView(textspace);
        frame.add(spH);
        frame.add(spV);
        frame.setSize(480,640);
        frame.setVisible(true);
        SwingUtilities.invokeLater(r);
    }
}
