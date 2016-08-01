package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionRight extends ExprFunction
{
    public ExprFunctionRight()
    {
        addArg("right$", 2, 2);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String str = args[0].toString();
        int len = IntegerUtils.parseInt(args[1]);
        if (len <= str.length())
            return str.substring(str.length() - len);
        else
            return str;
    }
}
