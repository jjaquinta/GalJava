package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionLeft extends ExprFunction
{
    public ExprFunctionLeft()
    {
        addArg("left$", 2, 2);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        int len = IntegerUtils.parseInt(args[1]);
        if (str.length() <= len)
            return str;
        else
            return str.substring(0, len);
    }
}
