package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionString extends ExprFunction
{
    public ExprFunctionString()
    {
        addArg("string$", 2, 2);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        int count = IntegerUtils.parseInt(args[0]);
        String ch;
        if (args[1] instanceof String)
            ch = (String)args[1];
        else if (args[1] instanceof Number)
            ch = String.valueOf((char)(((Number)args[1]).intValue()));
        else
            ch = args[1].toString();
        if (ch.length() > 1)
            ch = ch.substring(0, 1);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++)
            sb.append(ch);
        return sb.toString();
    }
}
