package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Graphics2D;

public class DrawPoint extends DrawElem
{
    private int mX;
    private int mY;
    private Color mColor;

    public DrawPoint(int x, int y, Color color)
    {
        mX = x;
        mY = y;
        mColor = color;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(mColor);
        g.fillRect(mX, mY, 1, 1);
    }

}
