package jo.basic.logic.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EditPanel extends JPanel
{
    private static final long serialVersionUID = -6414517257739918151L;
    private ScreenManager   mParent;
    private boolean     mReadOnly;
    private File        mFile;
    private JTextArea   mClient;
    private JLabel      mTitle;
    private JButton     mSave;
    private JButton     mCancel;
    
    public EditPanel(ScreenManager parent, boolean readOnly)
    {
        mParent = parent;
        mReadOnly = readOnly;
        setLayout(new BorderLayout());
        mTitle = new JLabel();
        add("North", mTitle);
        mClient = new JTextArea(24, 80);
        mClient.setEnabled(!mReadOnly);
        add("Center", new JScrollPane(mClient));
        if (!mReadOnly)
        {
            mSave = new JButton("Save");
            mCancel = new JButton("Cancel");
            JPanel buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(2, 1));
            buttonBar.add(mSave);
            buttonBar.add(mCancel);
            add("South", buttonBar);
            mSave.addActionListener(new ActionListener() {            
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    doSave();
                }
            });
        }
        else
        {
            mCancel = new JButton("OK");
            add("South", mCancel);
        }        
        mCancel.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doCancel();
            }
        });
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        mClient.setForeground(Color.WHITE);
        mClient.setBackground(Color.BLACK);
        mClient.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }
    
    public void setFile(File f)
    {
        mFile = f;
        mTitle.setText(mFile.toString());
        mClient.setText("");
        try
        {
            BufferedReader rdr = new BufferedReader(new FileReader(mFile));
            for (;;)
            {
                String inbuf = rdr.readLine();
                if (inbuf == null)
                    break;
                mClient.append(inbuf);
                mClient.append(System.getProperty("line.separator"));
            }
            rdr.close();
        }
        catch (IOException e)
        {
            
        }
        if (mReadOnly)
            mParent.show("viewer");
        else
            mParent.show("editor");
    }
    
    private void doSave()
    {
        try
        {
            FileWriter wtr = new FileWriter(mFile);
            wtr.write(mClient.getText());
            wtr.close();
        }
        catch (IOException e)
        {
            
        }
        doCancel();
    }
    
    private void doCancel()
    {
        mParent.show("main");
    }
}
