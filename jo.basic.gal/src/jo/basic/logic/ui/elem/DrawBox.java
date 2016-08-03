package jo.basic.logic.ui.elem;

import java.awt.Color;
import java.awt.Graphics2D;

public class DrawBox extends DrawElem
{
    private int mX;
    private int mY;
    private Color mColor;
    private char mBox;
    private int mCellX;
    private int mCellY;

    public DrawBox(int x, int y, Color color, char box)
    {
        mX = x;
        mY = y;
        mColor = color;
        mBox = box;
    }

    @Override
    public void draw(Graphics2D g)
    {
        int x = mX*mCellX;
        int xm = x + mCellX/2;
        int xh = x + mCellX;
        int y = mY*mCellY;
        int ym = y + mCellY/2;
        int yh = y + mCellY;
        g.setColor(mColor);
        switch (mBox)
        {
            case 179:
                g.drawLine(xm, y, xm, yh);
                break;
            case 180:
                g.drawLine(xm, y, xm, yh);
                g.drawLine(x, ym, xm, ym);
                break;
            case 181:
                g.drawLine(xm, y, xm, yh);
                g.drawLine(x, ym-1, xm, ym-1);
                g.drawLine(x, ym+1, xm, ym+1);
                break;
            case 182:
                g.drawLine(xm-1, y, xm-1, yh);
                g.drawLine(xm+1, y, xm+1, yh);
                g.drawLine(x, ym, xm-1, ym);
                break;
            case 183:
                g.drawLine(xm-1, ym, xm, yh);
                g.drawLine(xm+1, ym, xm, yh);
                g.drawLine(x, ym, xm, ym);
                break;
            case 185:
                g.drawLine(xm-1, y, xm-1, ym-1);
                g.drawLine(xm-1, ym+1, xm-1, yh);
                g.drawLine(xm+1, y, xm+1, yh);
                g.drawLine(x, ym-1, xm-1, ym-1);
                g.drawLine(x, ym+1, xm-1, ym+1);
                break;
            case 186:
                g.drawLine(xm-1, y, xm-1, yh);
                g.drawLine(xm+1, y, xm+1, yh);
                break;
            case 187:
                g.drawLine(x, ym-1, xm+1, ym-1);
                g.drawLine(x, ym+1, xm-1, ym+1);
                g.drawLine(xm-1, ym+1, xm-1, yh);
                g.drawLine(xm+1, ym-1, xm+1, yh);
                break;
            case 188:
                g.drawLine(x, ym-1, xm-1, ym-1);
                g.drawLine(x, ym+1, xm+1, ym+1);
                g.drawLine(xm-1, y, xm-1, ym-1);
                g.drawLine(xm+1, y, xm+1, ym+1);
                break;
            case 191:
                g.drawLine(xm, ym, xm, yh);
                g.drawLine(x, ym, xm, ym);
                break;
            case 192:
                g.drawLine(xm, y, xm, ym);
                g.drawLine(xm, ym, xh, ym);
                break;
            case 196:
                g.drawLine(x, ym, xh, ym);
                break;
            case 197:
                g.drawLine(x, ym, xh, ym);
                g.drawLine(xm, y, xm, yh);
                break;
            case 200:
                g.drawLine(xm-1, y, xm-1, ym+1);
                g.drawLine(xm+1, y, xm+1, ym-1);
                g.drawLine(xm+1, ym-1, xh, ym-1);
                g.drawLine(xm-1, ym+1, xh, ym+1);
                break;
            case 201:
                g.drawLine(xm-1, ym-1, xh, ym-1);
                g.drawLine(xm+1, ym+1, xh, ym+1);
                g.drawLine(xm-1, ym-1, xm-1, yh);
                g.drawLine(xm+1, ym+1, xm+1, yh);
                break;
            case 202:
                g.drawLine(xm-1, y, xm-1, ym-1);
                g.drawLine(xm+1, y, xm+1, ym-1);
                g.drawLine(x, ym-1, xm-1, ym-1);
                g.drawLine(xm-1, ym-1, xh, ym-1);
                g.drawLine(x, ym+1, xm+1, ym+1);
                break;
            case 203:
                g.drawLine(x, ym-1, xh, ym-1);
                g.drawLine(x, ym+1, xm-1, ym+1);
                g.drawLine(xm-1, ym+1, xh, ym+1);
                g.drawLine(xm-1, ym+1, xm-1, yh);
                g.drawLine(xm+1, ym+1, xm+1, yh);
                break;
            case 204:
                g.drawLine(xm-1, y, xm-1, yh);
                g.drawLine(xm+1, y, xm+1, ym-1);
                g.drawLine(xm+1, ym+1, xm+1, yh);
                g.drawLine(xm+1, ym-1, xh, ym-1);
                g.drawLine(xm+1, ym+1, xh, ym+1);
                break;
            case 205:
                g.drawLine(x, ym-1, xh, ym-1);
                g.drawLine(x, ym+1, xh, ym+1);
                break;
            case 206:
                g.drawLine(xm-1, y, xm-1, ym-1);
                g.drawLine(xm+1, y, xm+1, ym-1);
                g.drawLine(xm-1, ym+1, xm-1, yh);
                g.drawLine(xm+1, ym+1, xm+1, yh);
                g.drawLine(x, ym-1, xm-1, ym-1);
                g.drawLine(x-1, ym-1, x, ym-1);
                g.drawLine(x, ym+1, xm-1, ym+1);
                g.drawLine(x-1, ym+1, x, ym+1);
                break;
            case 217:
                g.drawLine(xm, y, xm, ym);
                g.drawLine(x, ym, xm, ym);
                break;
            case 218:
                g.drawLine(xm, ym, xm, yh);
                g.drawLine(xm, ym, xh, ym);
                break;
            default:
                System.err.println("Unsupported box: "+(int)mBox);
                break;
        }
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

}
