package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionLen extends ExprFunction
{
    public ExprFunctionLen()
    {
        addArg("len", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        return str.length();
    }
}
