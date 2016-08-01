package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionMid extends ExprFunction
{
    public ExprFunctionMid()
    {
        addArg("mid$", 3, 3);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        int start = IntegerUtils.parseInt(args[1]);
        int len = IntegerUtils.parseInt(args[2]);
        if (str.length() > start - 1)
            str = str.substring(start - 1);
        if (str.length() <= len)
            return str;
        else
            return str.substring(0, len);
    }
}
