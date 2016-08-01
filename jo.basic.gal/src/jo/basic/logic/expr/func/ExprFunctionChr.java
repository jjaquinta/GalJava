package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionChr extends ExprFunction
{
    public ExprFunctionChr()
    {
        addArg("chr$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        int ascii = IntegerUtils.parseInt(args[0]);
        return String.valueOf((char)ascii);
    }
}
