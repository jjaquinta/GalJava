package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionLTrim extends ExprFunction
{
    public ExprFunctionLTrim()
    {
        addArg("ltrim$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        while (str.startsWith(" "))
            str = str.substring(1);
        return str;
    }
}
