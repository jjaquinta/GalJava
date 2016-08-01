package jo.basic.logic.expr;

import java.util.ArrayList;
import java.util.List;

public abstract class ExprFunction
{
    private List<String> mAliases = new ArrayList<>();
    private List<Integer> mLowArgs = new ArrayList<>();
    private List<Integer> mHighArgs = new ArrayList<>();
    
    protected void addArg(String alias, int lowArg, int highArg)
    {
        mAliases.add(alias.toLowerCase());
        mLowArgs.add(lowArg);
        mHighArgs.add(highArg);
    }
    
    public boolean isFunction(String name, int numArgs)
    {
        for (int i = 0; i < mAliases.size(); i++)
            if (name.equalsIgnoreCase(mAliases.get(i)) && (numArgs >= mLowArgs.get(i)) && (numArgs <= mHighArgs.get(i)))
                return true;
        return false;
    }
    
    public abstract Object eval(String name, Object[] args);
}
