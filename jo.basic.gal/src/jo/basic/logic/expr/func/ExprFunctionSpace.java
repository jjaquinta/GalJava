package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionSpace extends ExprFunction
{
    public ExprFunctionSpace()
    {
        addArg("space$", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        int count = IntegerUtils.parseInt(args[0]);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++)
            sb.append(" ");
        return sb.toString();
    }
}
