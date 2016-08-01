package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionInt extends ExprFunction
{
    public ExprFunctionInt()
    {
        addArg("int", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        return IntegerUtils.parseInt(args[0]);
    }
}
