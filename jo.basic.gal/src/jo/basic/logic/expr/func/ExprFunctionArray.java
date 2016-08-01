package jo.basic.logic.expr.func;

import jo.basic.data.BasicRuntime;
import jo.basic.logic.RuntimeLogic;
import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionArray extends ExprFunction
{
    public ExprFunctionArray()
    {
    }
    
    @Override
    public boolean isFunction(String name, int numArgs)
    {
        BasicRuntime rt = RuntimeLogic.getRuntime();
        Object val = rt.getVariables().get(name.toUpperCase());
        if (val != null)
            return true;
        return false;
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        BasicRuntime rt = RuntimeLogic.getRuntime();
        Object val = rt.getVariables().get(name.toUpperCase());
        if (val instanceof Object[][][])
            return ((Object[][][])val)[IntegerUtils.parseInt(args[0])][IntegerUtils.parseInt(args[1])][IntegerUtils.parseInt(args[2])];
        if (val instanceof Object[][])
            return ((Object[][])val)[IntegerUtils.parseInt(args[0])][IntegerUtils.parseInt(args[1])];
        if (val instanceof Object[])
            return ((Object[])val)[IntegerUtils.parseInt(args[0])];
        throw new RuntimeException("Unrecognized variable "+name+" type "+val);
    }
}
