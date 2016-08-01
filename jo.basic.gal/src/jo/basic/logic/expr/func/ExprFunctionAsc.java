package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionAsc extends ExprFunction
{
    public ExprFunctionAsc()
    {
        addArg("asc", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String txt = args[0].toString();
        if (txt.length() == 0)
            return 0;
        return (int)txt.charAt(0);
    }
}
