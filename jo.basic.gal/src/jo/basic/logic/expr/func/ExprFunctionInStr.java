package jo.basic.logic.expr.func;

import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionInStr extends ExprFunction
{
    public ExprFunctionInStr()
    {
        addArg("instr", 2, 3);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        String base = args[args.length - 2].toString().toUpperCase();
        String pattern = args[args.length - 1].toString().toUpperCase();
        if (args.length == 0)
        {
            int idx = IntegerUtils.parseInt(args[0]);
            return base.indexOf(pattern, idx - 1) + 1;
        }
        else
            return base.indexOf(pattern) + 1;
    }
}
