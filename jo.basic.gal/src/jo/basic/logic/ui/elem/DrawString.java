package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import jo.basic.logic.ui.ScaledScreenManager;

public class DrawString extends DrawElem
{
    private int mX;
    private int mY;
    private Color mColor;
    private String mStr;
    private int mCellX;
    private int mCellY;
    private Font mFont;

    public DrawString(int x, int y, Color color, String str)
    {
        mX = x;
        mY = y;
        mColor = color;
        mStr = str;
    }

    public DrawString(int x, int y, Color color, String str, int cellX, int cellY)
    {
        this(x, y, color, str);
        mCellX = cellX;
        mCellY = cellY;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setFont(mFont);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(mStr, g);
        g.setColor(ScaledScreenManager.BASE_COLORS[0]);
        g.fillRect(mX*mCellX, mY*mCellY, (int)r.getWidth(), (int)r.getHeight());
        g.setColor(mColor);
        g.drawString(mStr, mX*mCellX, mY*mCellY + fm.getAscent());
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

    public Color getColor()
    {
        return mColor;
    }

    public void setColor(Color color)
    {
        mColor = color;
    }

    public String getStr()
    {
        return mStr;
    }

    public void setStr(String str)
    {
        mStr = str;
    }

    public int getCellX()
    {
        return mCellX;
    }

    public void setCellX(int cellX)
    {
        mCellX = cellX;
    }

    public int getCellY()
    {
        return mCellY;
    }

    public void setCellY(int cellY)
    {
        mCellY = cellY;
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
