package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionUCase extends ExprFunction
{
    public ExprFunctionUCase()
    {
        addArg("ucase$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        return str.toUpperCase();
    }
}
