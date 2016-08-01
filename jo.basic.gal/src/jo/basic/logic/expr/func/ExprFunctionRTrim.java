package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionRTrim extends ExprFunction
{
    public ExprFunctionRTrim()
    {
        addArg("rtrim$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        while (str.endsWith(" "))
            str = str.substring(0, str.length() - 1);
        return str;
    }
}
