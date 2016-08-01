package jo.basic.logic.expr;

import java.util.ArrayList;
import java.util.List;

import jo.basic.data.TokenBean;

public class ParseNode
{
    public static final int CODE_BLOCK = 0;
    public static final int VALUE = 1;
    public static final int FUNCTION = 2;
    public static final int OPERATION = 3;
    public static final int METHOD = 4;
    public static final int IF = 5;
    
    private int             mType;
    private TokenBean       mToken;
    private List<ParseNode> mChildren = new ArrayList<>();
    
    @Override
    public String toString()
    {
        return ParseUtils.toString("", this);
    }
    
    public List<ParseNode> getChildren()
    {
        return mChildren;
    }
    public void setChildren(List<ParseNode> children)
    {
        mChildren = children;
    }
    public int getType()
    {
        return mType;
    }
    public void setType(int type)
    {
        mType = type;
    }
    public TokenBean getToken()
    {
        return mToken;
    }
    public void setToken(TokenBean token)
    {
        mToken = token;
    }
}
