package jo.basic.data;

import java.util.ArrayList;
import java.util.List;

public class FunctionBean
{
    private String  mName;
    private List<VariableBean> mArgs = new ArrayList<>();
    private ExpressionBean mCode;
    
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }
    public List<VariableBean> getArgs()
    {
        return mArgs;
    }
    public void setArgs(List<VariableBean> args)
    {
        mArgs = args;
    }
    public ExpressionBean getCode()
    {
        return mCode;
    }
    public void setCode(ExpressionBean code)
    {
        mCode = code;
    }
}
