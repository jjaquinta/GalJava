package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Graphics2D;

public class DrawFill extends DrawElem
{
    private int mX;
    private int mY;
    private Color mColor;

    public DrawFill(int x, int y, Color color)
    {
        mX = x;
        mY = y;
        mColor = color;
    }

    @Override
    public void draw(Graphics2D g)
    {
    }

}
