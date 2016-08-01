package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionLCase extends ExprFunction
{
    public ExprFunctionLCase()
    {
        addArg("lcase$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        return str.toLowerCase();
    }
}
