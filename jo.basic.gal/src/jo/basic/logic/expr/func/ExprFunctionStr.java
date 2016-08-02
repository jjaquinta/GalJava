package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionStr extends ExprFunction
{
    public ExprFunctionStr()
    {
        addArg("str$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        int val = IntegerUtils.parseInt(args[0]);
        if (val < 0)
            return String.valueOf(val);
        else
            return " "+String.valueOf(val);
    }
}
