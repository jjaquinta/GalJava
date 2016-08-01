package jo.basic.logic.expr;

import java.util.ArrayList;
import java.util.List;

public abstract class ExprOperator
{
    protected int          mPrecedence;
    protected List<String> mAliases = new ArrayList<>();
    protected boolean      mArg1Type = true;
    protected boolean      mArg2Type = true;
    
    public int getPrecedence()
    {
        return mPrecedence;
    }
    
    public boolean getArg1Type()
    {
        return mArg1Type;
    }
    
    public boolean getArg2Type()
    {
        return mArg2Type;
    }
    
    public List<String> getAliases()
    {
        return mAliases;
    }
    
    public abstract Object eval(Object arg1, Object arg2, IExprProps props);
    
    protected void assertNotNull(Object arg)
    {
        if (arg == null)
            throw new IllegalArgumentException("Unexpected null argument");
    }
}
