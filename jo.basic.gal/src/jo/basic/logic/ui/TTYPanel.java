package jo.basic.logic.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import jo.basic.logic.ui.elem.DrawBox;
import jo.basic.logic.ui.elem.DrawCircle;
import jo.basic.logic.ui.elem.DrawElem;
import jo.basic.logic.ui.elem.DrawFinePrint;
import jo.basic.logic.ui.elem.DrawLine;
import jo.basic.logic.ui.elem.DrawShape;
import jo.basic.logic.ui.elem.DrawString;

public class TTYPanel extends JPanel
{
    private static final long serialVersionUID = 5821442014097723796L;
    
    private List<DrawElem> mElements;
    boolean mNeedsOptimization = false;
    private int mCharsWide;
    private int mCharsHigh;
    private Font mBigFont;
    private Font mSmallFont;
    
    public TTYPanel()
    {
        mElements = new ArrayList<>();
        mBigFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        setFont(mBigFont);
        mSmallFont = new Font(Font.MONOSPACED, Font.PLAIN, 8);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        doPaint((Graphics2D)g);
    }
    @Override
    public Dimension getMinimumSize()
    {
        return new Dimension(640, 480);
    }
    @Override
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
    public void clearElements()
    {
        synchronized (mElements)
        {
            mElements.clear();
        }
        mNeedsOptimization = true;
        repaint();
    }
    
    public void addElement(DrawElem ele)
    {
        synchronized(mElements)
        {
            mElements.add(ele);
        }
        mNeedsOptimization = true;
        repaint();
    }
    
    private void optimizeElements()
    {
        synchronized (mElements)
        {
            mNeedsOptimization = false;
            char[][] scr = new char[mCharsHigh][mCharsWide];
            Color[][] color = new Color[mCharsHigh][mCharsWide];
            DrawBox[][] boxes = new DrawBox[mCharsHigh][mCharsWide];
            List<DrawShape> shapes = new ArrayList<>();
            for (int i = 0; i < mElements.size(); i++)
            {
                DrawElem ele = mElements.get(i);
                if (ele instanceof DrawString)
                {
                    DrawString ds = (DrawString)ele;
                    //System.out.println("<"+ds.getY()+", "+ds.getX()+"x"+ds.getStr().length()+" "+ds.getStr());
                    for (int j = 0; j < ds.getStr().length(); j++)
                    {
                        char ch = ds.getStr().charAt(j);
                        try
                        {
                            scr[ds.getY()][ds.getX()+j] = ch;
                            color[ds.getY()][ds.getX()+j] = ds.getColor();
                            boxes[ds.getY()][ds.getX()+j] = null;
                        }
                        catch (ArrayIndexOutOfBoundsException e)
                        {
                            System.out.println("?");
                        }
                    }
                    mElements.remove(i);
                    i--;
                }
                else if (ele instanceof DrawFinePrint)
                {
                    DrawFinePrint fp = (DrawFinePrint)ele;
                    fp.setFont(mSmallFont);
                }
                else if (ele instanceof DrawBox)
                {
                    DrawBox db = (DrawBox)ele;
                    db.setCellX(640/mCharsWide);
                    db.setCellY(480/mCharsHigh);
                    boxes[db.getY()][db.getX()] = db;
                    mElements.remove(i);
                    i--;
                }
                else if (ele instanceof DrawLine)
                {
                    Shape path = findLine(i);
                    shapes.add(new DrawShape(path, ((DrawLine)ele).getColor()));
                    i--;
                }
                else if (ele instanceof DrawCircle)
                {
                    DrawCircle circ = (DrawCircle)ele;
                    Shape path = new Ellipse2D.Double(circ.getX() - circ.getR(), circ.getY() - circ.getR(), circ.getR()*2, circ.getR()*2);
                    shapes.add(new DrawShape(path, circ.getColor()));
                    mElements.remove(i);
                    i--;
                }
                else if (ele instanceof DrawShape)
                {
                    shapes.add((DrawShape)ele);
                    mElements.remove(i);
                    i--;
                }
//                else if (ele instanceof DrawFill)
//                {
//                    if (mElements.get(i - 1) instanceof DrawCircle)
//                        ((DrawCircle)mElements.get(i - 1)).setFilled(true);
//                }
            }
            optimizeShapes(shapes);
            optimizeBoxes(boxes);
            optimizeChars(scr, color);
        }
    }
    private void optimizeShapes(List<DrawShape> shapes)
    {
        for (int i = 1; i < shapes.size(); i++)
        {
            DrawShape si = shapes.get(i);
            for (int j = 0; j < i; j++)
            {
                DrawShape sj = shapes.get(j);
                if (sj.equals(si))
                {
                    shapes.remove(j);
                    i--;
                }
            }
        }
        mElements.addAll(0, shapes);
    }
    
    private Shape findLine(int startIdx)
    {
        int endIdx = startIdx;
        DrawLine start = (DrawLine)mElements.get(endIdx);
        DrawLine prev = start;
        for (;;)
        {
            endIdx++;
            if (endIdx == mElements.size())
                break;
            if (!(mElements.get(endIdx) instanceof DrawLine))
                break;
            DrawLine l = (DrawLine)mElements.get(endIdx);
            if (start.getColor() != l.getColor())
                break;
            if (!l.joinsFrom(prev))
                break;
            if (start.joinsFrom(l))
            {
                endIdx++;
                break;
            }
        }
        Path2D path = new Path2D.Double();
        path.moveTo(start.getX1(), start.getY1());
        for (int i = startIdx; i < endIdx; i++)
        {
            DrawLine l = (DrawLine)mElements.get(i);
            path.lineTo(l.getX2(), l.getY2());
        }
        for (int i = endIdx - 1; i >= startIdx; i--)
            mElements.remove(i);
        return path;
    }
    
    private void optimizeChars(char[][] scr, Color[][] color)
    {
        for (int y = 0; y < mCharsHigh; y++)
            for (int x = 0; x < mCharsWide;)
                if ((scr[y][x] != 0) && (scr[y][x] != ' '))
                {
                    int x1 = x++;
                    while ((x < mCharsWide) && (scr[y][x] != 0) && (scr[y][x] != ' ') && (color[y][x1].equals(color[y][x])))
                        x++;
                    int l = x - x1;
                    DrawString ds = new DrawString(x1, y, color[y][x1], new String(scr[y], x1, l), 640/mCharsWide, 480/mCharsHigh);
                    ds.setFont(mBigFont);
                    mElements.add(ds);
                    //System.out.println(">"+ds.getY()+", "+ds.getX()+"x"+ds.getStr().length()+" "+ds.getStr());
                }
                else
                    x++;
    }
    private void optimizeBoxes(DrawBox[][] boxes)
    {
        for (int y = 0; y < mCharsHigh; y++)
            for (int x = 0; x < mCharsWide; x++)
                if (boxes[y][x] != null)
                    mElements.add(boxes[y][x]);
    }
    
    private void doPaint(Graphics2D g)
    {
        if (mNeedsOptimization)
            optimizeElements();
        double sx = (double)getWidth()/(double)640;
        double sy = (double)getHeight()/(double)480;
        AffineTransform oldTrans = g.getTransform();
        g.setTransform(AffineTransform.getScaleInstance(sx, sy));
        g.setColor(ScaledScreenManager.BASE_COLORS[0]);
        g.fillRect(0, 0, 640, 480);
        for (DrawElem e : mElements.toArray(new DrawElem[0]))
            e.draw(g);
        g.setTransform(oldTrans);
    }
    public List<DrawElem> getElements()
    {
        return mElements;
    }
    public void setElements(List<DrawElem> elements)
    {
        mElements = elements;
    }
    public int getCharsWide()
    {
        return mCharsWide;
    }
    public void setCharsWide(int charsWide)
    {
        mCharsWide = charsWide;
    }
    public int getCharsHigh()
    {
        return mCharsHigh;
    }
    public void setCharsHigh(int charsHigh)
    {
        mCharsHigh = charsHigh;
    }
}
