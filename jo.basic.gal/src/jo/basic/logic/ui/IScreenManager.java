package jo.basic.logic.ui;

import java.io.File;

public interface IScreenManager
{
    public void show(String panel);
    public void view(File f);
    public void screen(int num);
    public void cls();
    public void color(int num);
    public void palette(int colorNum, int colorVal);
    public void paletteReset();
    public void locate(int x, int y);
    public void print(String string);
    public void pset(int x, int y);
    public void line(int x1, int y1, int x2, int y2);
    public void lineTo(int x, int y);
    public void circle(int x, int y, int r, Integer color);
    public void paint(int x, int y, int color);
    public int[] get(int x1, int y1, int x2, int y2);
    public void put(int x, int y, int[] data);
    public String input(String prompt);
    public void saveScreen();
    public Object inkey();
}
