package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

public class DrawShape extends DrawElem
{
    private Shape mShape;
    private Color mColor;
    private boolean mFilled;

    public DrawShape(Shape shape, Color color)
    {
        mShape = shape;
        mColor = color;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(mColor);
        if (mFilled)
            g.fill(mShape);
        else
            g.draw(mShape);
    }

    public boolean isFilled()
    {
        return mFilled;
    }

    public void setFilled(boolean filled)
    {
        mFilled = filled;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return mShape.equals(((DrawShape)obj).mShape);
    }
}
