package jo.basic.logic.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScreenManager
{
    private static final Color[] COLORS = {
            new Color(0x00, 0x00, 0x50),
            new Color(0x00, 0x00, 0xA8),
            new Color(0x00, 0xA8, 0x00),
            new Color(0x00, 0xA8, 0xA8),
            new Color(0xA8, 0x00, 0x00),
            new Color(0xA8, 0x00, 0xA8),
            new Color(0xA8, 0x54, 0x00),
            new Color(0xA8, 0xA8, 0xA8),
            new Color(0x54, 0x54, 0x54),
            new Color(0x54, 0x54, 0xFC),
            new Color(0x54, 0xFC, 0x54),
            new Color(0x54, 0xFC, 0xFC),
            new Color(0xFC, 0x54, 0x54),
            new Color(0xFC, 0x54, 0xFC),
            new Color(0xFC, 0xFC, 0x54),
            new Color(0xFC, 0xFC, 0xFC),
    };
    
    private BufferedImage   mImage;
    private int             mColor;
    private int             mRows;
    private int             mCols;
    private int             mCursorX; // 0 based
    private int             mCursorY; // 0 based
    private int             mCellX;
    private int             mCellY;
    private FontMetrics     mMetrics;
    
    private StringBuffer    mKeyBuffer = new StringBuffer();
    
    private JFrame          mFrame;
    private JPanel          mCanvas;
    private EditPanel       mEditor;
    private EditPanel       mViewer;
    private String          mShowing;
    private Thread          mWaiting = null;
    
    public ScreenManager()
    {
        mFrame = new JFrame("Galactic 2.4");
        mCanvas = new JPanel() { 
            private static final long serialVersionUID = 5821442014097723796L;
            @Override
            public void paintComponent(Graphics g)
            {
                doPaint(g);
            }
            @Override
            public Dimension getMinimumSize()
            {
                if (mImage == null)
                    return new Dimension(640, 400);
                else
                    return new Dimension(mImage.getWidth(), mImage.getHeight());
            }
            @Override
            public Dimension getPreferredSize()
            {
                return getMinimumSize();
            }
        };
        mEditor = new EditPanel(this, false);
        mViewer = new EditPanel(this, true);
        mCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e)
            {
                //doKeyTyped(e.getKeyCode(), e.getKeyChar());
            }
            @Override
            public void keyPressed(KeyEvent e)
            {
                doKeyTyped(e.getKeyCode(), e.getKeyChar());
            }
        });
        
        mFrame.getContentPane().setLayout(new CardLayout());
        mFrame.getContentPane().add(mCanvas, "main");
        mFrame.getContentPane().add(mEditor, "editor");
        mFrame.getContentPane().add(mViewer, "viewer");
        show("main");
        mFrame.setVisible(true);
        mFrame.pack();
        mCanvas.requestFocus();
    }

    void show(String card)
    {
        mShowing = card;
        ((CardLayout)mFrame.getContentPane().getLayout()).show(mFrame.getContentPane(), mShowing);
        if (mWaiting != null)
            mWaiting.interrupt();
    }
    
    private static final Map<Integer, Integer> CODE_TO_CHAR = new HashMap<>();
    static
    {
        CODE_TO_CHAR.put(0x25, 75); // LEFT
        CODE_TO_CHAR.put(0x27, 77); // RIGHT
        CODE_TO_CHAR.put(0x26, 72); // UP
        CODE_TO_CHAR.put(0x28, 80); // DOWN
        CODE_TO_CHAR.put(0x21, 73); // PGUP
        CODE_TO_CHAR.put(0x22, 81); // PGDN
        //CODE_TO_CHAR.put(0x1b, 27); // ESC
        CODE_TO_CHAR.put(0x9b, 82); // Ins
        CODE_TO_CHAR.put(0x7f, 83); // Del
        CODE_TO_CHAR.put(0x70, 59); // F1
        CODE_TO_CHAR.put(0x71, 60); // F2
        CODE_TO_CHAR.put(0x72, 61); // F3
        CODE_TO_CHAR.put(0x73, 62); // F4
        CODE_TO_CHAR.put(0x74, 63); // F5
        CODE_TO_CHAR.put(0x75, 64); // F6
        CODE_TO_CHAR.put(0x76, 65); // F7
        CODE_TO_CHAR.put(0x77, 66); // F8
        CODE_TO_CHAR.put(0x78, 67); // F9
        CODE_TO_CHAR.put(0x79, 68); // F10
        CODE_TO_CHAR.put(0x7a, 69); // F11
        CODE_TO_CHAR.put(0x7b, 70); // F12
        //CODE_TO_CHAR.put(0x0a, 13); // Enter
    }
    
    private void doKeyTyped(int code, char ch)
    {
        //System.out.println("KeyTyped code="+Integer.toHexString(code)+", char='"+ch+"'");
        synchronized (mKeyBuffer)
        {
            if (CODE_TO_CHAR.containsKey(code))
            {
                mKeyBuffer.append((char)0);
                mKeyBuffer.append((char)CODE_TO_CHAR.get(code).intValue());
            }
            else if ((ch == '\n') || (ch == '\r'))
                mKeyBuffer.append((char)13);
            else
                mKeyBuffer.append(ch);
        }
    }
    
    private void doPaint(Graphics g)
    {
        if (mImage != null)
            g.drawImage(mImage, 0, 0, mCanvas.getWidth(), mCanvas.getHeight(), 0, 0, mImage.getWidth(), mImage.getHeight(), null);
    }
    
    public void screen(int num)
    {
        if (num == 12)
        {
            mImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            mRows = 30;
            mCols = 80;
        }
        else if (num == 0)
        {
            mImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            mRows = 25;
            mCols = 80;
        }
        else
            throw new RuntimeException("Unsupported screen type "+num);
        mCellX = mImage.getWidth()/mCols;
        mCellY = mImage.getHeight()/mRows;
        Graphics g = mImage.getGraphics();
        Font m = new Font(Font.MONOSPACED, Font.BOLD, 12);
        g.setFont(m);
        mMetrics = g.getFontMetrics();
        g.dispose();
        mCanvas.invalidate();
    }
    
    public void cls()
    {
        Graphics g = mImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, mImage.getWidth(), mImage.getHeight());
        g.dispose();
        mCursorX = 0;
        mCursorY = 0;
        mCanvas.repaint();
    }
    
    public void color(int num)
    {
        mColor = num;
    }
    
    public void locate(int x, int y)
    {
        mCursorX = x - 1;
        mCursorY = y - 1;
    }
    
    public void print(String txt)
    {
        System.out.print("@"+mCursorX+","+mCursorY+" c"+mColor+" "+txt);
        if (!txt.endsWith("\n"))
            System.out.println(";");
        Graphics g = mImage.getGraphics();
        for (char c : txt.toCharArray())
            print(g, c);
        g.dispose();
        mCanvas.repaint();
    }
    
    private void print(Graphics g, char c)
    {
        if (c == '\r')
        {
            mCursorX = 0;
            return;
        }
        if (c == '\n')
        {
            mCursorY++;
            if (mCursorY == mRows)
                mCursorY = 0;
            return;
        }
        c = CP437[c];
        //System.out.println("PRINT '"+c+"' at "+mCursorX+","+mCursorY+" in color "+mColor);
        g.setColor(Color.BLACK);
        g.fillRect(mCursorX*mCellX, mCursorY*mCellY, 8, 20);
        g.setColor(COLORS[mColor]);
        g.drawString(String.valueOf(c), mCursorX*mCellX, mCursorY*mCellY + mMetrics.getAscent());
        mCursorX++;
        if (mCursorX == mCols)
        {
            mCursorX = 0;
            mCursorY++;
            if (mCursorY == mRows)
                mCursorY = 0;
        }
        //saveScreen();
    }
    
    public int[] get(int x1, int x2, int y1, int y2)
    {
        int[] ret = new int[(x2 - x1 + 1)*(y2 - y1 + 1)];
        int o = 0;
        for (int y = y1; y <= y2; y++)
            for (int x = x1; x <= x2; x++)
                ret[o++] = mImage.getRGB(x-1, y-1);
        return ret;
    }
    
    public void pset(int x, int y)
    {
        Graphics g = mImage.getGraphics();
        g.setColor(COLORS[mColor]);
        g.fillRect(x-1, y-1, 1, 1);
        g.dispose();
        //saveScreen();
        mCanvas.repaint();
    }
    
    public void edit(File f)
    {
        mWaiting = Thread.currentThread();
        mEditor.setFile(f);
        while (!"main".equals(mShowing))
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
            }
        mWaiting = null;
        mCanvas.requestFocus();
    }
    
    public void view(File f)
    {
        mWaiting = Thread.currentThread();
        mViewer.setFile(f);
        while (!"main".equals(mShowing))
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
            }
        mWaiting = null;
        mCanvas.requestFocus();
    }
    
    private long mLastSnap = 0;
    public void saveScreen()
    {
        long now = System.currentTimeMillis();
        if (now < mLastSnap + 1000)
            return;
        mLastSnap = now;
        try
        {
            ImageIO.write(mImage, "PNG", new File("c:\\temp\\bas.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String inkey()
    {
        saveScreen();
        synchronized (mKeyBuffer)
        {
            if (mKeyBuffer.length() > 0)
            {
                String key = mKeyBuffer.substring(0, 1);
                mKeyBuffer.deleteCharAt(0);
                if (key.equals("\u0000"))
                {
                    key += mKeyBuffer.substring(0, 1);
                    mKeyBuffer.deleteCharAt(0);
                }
                return key;
            }
            else
                return "";
        }
    }
    
    public String input(String prompt)
    {
        print(prompt);
        return ""; // TODO
    }
    
    public static final char[] CP437 = {
        '\u0000', '\u263A', '\u263B', '\u2665', '\u2666', '\u2663', '\u2660', '\u2022',
        '\u25D8', '\u25CB', '\u25D9', '\u2642', '\u2640', '\u266A', '\u266B', '\u263C',

        '\u25BA', '\u25C4', '\u2195', '\u203C', '\u00B6', '\u00A7', '\u25AC', '\u21A8',
        '\u2191', '\u2193', '\u2192', '\u2190', '\u221F', '\u2194', '\u25B2', '\u25BC',

        '\u0020', '\u0021', '\u0022', '\u0023', '\u0024', '\u0025', '\u0026', '\'',
        '\u0028', '\u0029', '\u002A', '\u002B', '\u002C', '\u002D', '\u002E', '\u002F',

        '\u0030', '\u0031', '\u0032', '\u0033', '\u0034', '\u0035', '\u0036', '\u0037',
        '\u0038', '\u0039', '\u003A', '\u003B', '\u003C', '\u003D', '\u003E', '\u003F',

        '\u0040', '\u0041', '\u0042', '\u0043', '\u0044', '\u0045', '\u0046', '\u0047',
        '\u0048', '\u0049', '\u004A', '\u004B', '\u004C', '\u004D', '\u004E', '\u004F',

        '\u0050', '\u0051', '\u0052', '\u0053', '\u0054', '\u0055', '\u0056', '\u0057',
        '\u0058', '\u0059', '\u005A', '\u005B', '\\', '\u005D', '\u005E', '\u005F',

        '\u0060', '\u0061', '\u0062', '\u0063', '\u0064', '\u0065', '\u0066', '\u0067',
        '\u0068', '\u0069', '\u006A', '\u006B', '\u006C', '\u006D', '\u006E', '\u006F',

        '\u0070', '\u0071', '\u0072', '\u0073', '\u0074', '\u0075', '\u0076', '\u0077',
        '\u0078', '\u0079', '\u007A', '\u007B', '\u007C', '\u007D', '\u007E', '\u2302',

        '\u00C7', '\u00FC', '\u00E9', '\u00E2', '\u00E4', '\u00E0', '\u00E5', '\u00E7',
        '\u00EA', '\u00EB', '\u00E8', '\u00EF', '\u00EE', '\u00EC', '\u00C4', '\u00C5',

        '\u00C9', '\u00E6', '\u00C6', '\u00F4', '\u00F6', '\u00F2', '\u00FB', '\u00F9',
        '\u00FF', '\u00D6', '\u00DC', '\u00A2', '\u00A3', '\u00A5', '\u20A7', '\u0192',

        '\u00E1', '\u00ED', '\u00F3', '\u00FA', '\u00F1', '\u00D1', '\u00AA', '\u00BA',
        '\u00BF', '\u2310', '\u00AC', '\u00BD', '\u00BC', '\u00A1', '\u00AB', '\u00BB',

        '\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u2561', '\u2562', '\u2556',
        '\u2555', '\u2563', '\u2551', '\u2557', '\u255D', '\u255C', '\u255B', '\u2510',

        '\u2514', '\u2534', '\u252C', '\u251C', '\u2500', '\u253C', '\u255E', '\u255F',
        '\u255A', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256C', '\u2567',

        '\u2568', '\u2564', '\u2565', '\u2559', '\u2558', '\u2552', '\u2553', '\u256B',
        '\u256A', '\u2518', '\u250C', '\u2588', '\u2584', '\u258C', '\u2590', '\u2580',

        '\u03B1', '\u00DF', '\u0393', '\u03C0', '\u03A3', '\u03C3', '\u00B5', '\u03C4',
        '\u03A6', '\u0398', '\u03A9', '\u03B4', '\u221E', '\u03C6', '\u03B5', '\u2229',

        '\u2261', '\u00B1', '\u2265', '\u2264', '\u2320', '\u2321', '\u00F7', '\u2248',
        '\u00B0', '\u2219', '\u00B7', '\u221A', '\u207F', '\u00B2', '\u25A0', '\u00A0',
    };
}
