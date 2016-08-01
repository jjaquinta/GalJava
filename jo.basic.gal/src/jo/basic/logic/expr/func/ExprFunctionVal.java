package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.DoubleUtils;

public class ExprFunctionVal extends ExprFunction
{
    public ExprFunctionVal()
    {
        addArg("val", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        return DoubleUtils.parseDouble(args[0]);
    }
}
