package jo.basic.data;

public class LineBean
{
    private int     mNumber;
    private String  mText;
    private String  mLabel;
    private LineBean    mNext;
    private LineBean    mPrevious;

    public String getText()
    {
        return mText;
    }

    public void setText(String text)
    {
        mText = text;
    }

    public LineBean getNext()
    {
        return mNext;
    }

    public void setNext(LineBean next)
    {
        mNext = next;
    }

    public LineBean getPrevious()
    {
        return mPrevious;
    }

    public void setPrevious(LineBean previous)
    {
        mPrevious = previous;
    }

    public int getNumber()
    {
        return mNumber;
    }

    public void setNumber(int number)
    {
        mNumber = number;
    }

    public String getLabel()
    {
        return mLabel;
    }

    public void setLabel(String label)
    {
        mLabel = label;
    }
}
