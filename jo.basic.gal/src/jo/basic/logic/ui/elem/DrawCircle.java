package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Graphics2D;

public class DrawCircle extends DrawElem
{
    private int mX;
    private int mY;
    private int mR;
    private Color mColor;
    private boolean mFilled;

    public DrawCircle(int x, int y, int r, Color color)
    {
        mX = x;
        mY = y;
        mR = r;
        mColor = color;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(mColor);
        if (mFilled)
            g.fillOval(mX - mR, mY - mR, mR*2, mR*2);
        else
            g.drawOval(mX - mR, mY - mR, mR*2, mR*2);
    }

    public boolean isFilled()
    {
        return mFilled;
    }

    public void setFilled(boolean filled)
    {
        mFilled = filled;
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

    public int getR()
    {
        return mR;
    }

    public void setR(int r)
    {
        mR = r;
    }

    public Color getColor()
    {
        return mColor;
    }

    public void setColor(Color color)
    {
        mColor = color;
    }
}
