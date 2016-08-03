package jo.basic.logic.ui.elem;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import jo.basic.logic.ui.ScaledScreenManager;

public class DrawFinePrint extends DrawElem
{
    private int mX;
    private int mY;
    private String mStr;
    private Font mFont;

    public DrawFinePrint(int x, int y, String str)
    {
        mX = x;
        mY = y;
        mStr = str;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setFont(mFont);
        FontMetrics fm = g.getFontMetrics();
        //Rectangle2D r = fm.getStringBounds(mStr, g);
        g.setColor(ScaledScreenManager.BASE_COLORS[15]);
        g.drawString(mStr, mX, mY + fm.getAscent());
    }

    public int getX()
    {
        return mX;
    }

    public void setX(int x)
    {
        mX = x;
    }

    public int getY()
    {
        return mY;
    }

    public void setY(int y)
    {
        mY = y;
    }

    public String getStr()
    {
        return mStr;
    }

    public void setStr(String str)
    {
        mStr = str;
    }

    public Font getFont()
    {
        return mFont;
    }

    public void setFont(Font font)
    {
        mFont = font;
    }
}
