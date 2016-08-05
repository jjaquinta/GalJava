package jo.basic.logic.expr.func;

import jo.basic.data.BasicRuntime;
import jo.basic.data.FunctionBean;
import jo.basic.logic.RuntimeLogic;
import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionDefFn extends ExprFunction
{
    public ExprFunctionDefFn()
    {
    }
    
    @Override
    public boolean isFunction(String name, int numArgs)
    {
        FunctionBean val = RuntimeLogic.getFunction(name);
        if (val == null)
            return false;
        if (val.getArgs().size() != numArgs)
            return false;
        return true;
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        BasicRuntime rt = RuntimeLogic.getRuntime();
        FunctionBean func = RuntimeLogic.getFunction(name);
        return RuntimeLogic.invokeFunction(rt, func, args);
    }

}
