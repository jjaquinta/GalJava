package jo.basic.logic.ui.elem;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import jo.basic.logic.ui.ScaledScreenManager;

public class DrawFinePrint extends DrawElem
{
    private int mX;
    private int mY;
    private int mWidth;
    private String mStr;
    private Font mFont;

    public DrawFinePrint(int x, int y, String str, int width)
    {
        mX = x;
        mY = y;
        mStr = str;
        mWidth = width;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setFont(mFont);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(mStr, g);
        double dx = (mWidth - r.getWidth())/2;
        g.setColor(ScaledScreenManager.BASE_COLORS[0]);
        g.fillRect(mX, mY - fm.getAscent()/2, mWidth, (int)r.getHeight());
        g.setColor(ScaledScreenManager.BASE_COLORS[15]);
        g.drawString(mStr, (float)(mX + dx), mY + fm.getAscent()/2);
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

    public int getWidth()
    {
        return mWidth;
    }

    public void setWidth(int width)
    {
        mWidth = width;
    }

    public boolean extendsFrom(DrawFinePrint lastFP)
    {
        if (lastFP.mY != mY)
            return false;
        int dx = (lastFP.mX + lastFP.mWidth) - mX;
        if (dx > 5)
            return false;
        return true;
    }

    public void extendWith(DrawFinePrint fp)
    {
        mStr += fp.mStr;
        mWidth += fp.mWidth + 1;
    }
}
