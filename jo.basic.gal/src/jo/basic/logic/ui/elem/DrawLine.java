package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Graphics2D;

public class DrawLine extends DrawElem
{
    private int mX1;
    private int mY1;
    private int mX2;
    private int mY2;
    private Color mColor;

    public DrawLine(int x1, int y1, int x2, int y2, Color color)
    {
        mX1 = x1;
        mY1 = y1;
        mX2 = x2;
        mY2 = y2;
        mColor = color;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(mColor);
        g.drawLine(mX1, mY1, mX2, mY2);
    }

    @Override
    public boolean equals(Object obj)
    {
        DrawLine l2 = (DrawLine)obj;
        return (mX1 == l2.mX1) && (mY1 == l2.mY1) && (mX2 == l2.mX2) && (mY2 == l2.mY2);
    }

    public Color getColor()
    {
        return mColor;
    }

    public void setColor(Color color)
    {
        mColor = color;
    }

    public boolean joinsFrom(DrawLine prev)
    {
        if ((mX1 != prev.mX2) || (mY1 != prev.mY2))
            return false;
        return true;
    }

    public int getX1()
    {
        return mX1;
    }

    public void setX1(int x1)
    {
        mX1 = x1;
    }

    public int getY1()
    {
        return mY1;
    }

    public void setY1(int y1)
    {
        mY1 = y1;
    }

    public int getX2()
    {
        return mX2;
    }

    public void setX2(int x2)
    {
        mX2 = x2;
    }

    public int getY2()
    {
        return mY2;
    }

    public void setY2(int y2)
    {
        mY2 = y2;
    }
}
