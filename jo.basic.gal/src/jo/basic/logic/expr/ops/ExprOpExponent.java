package jo.basic.logic.expr.ops;

import jo.basic.logic.expr.ExprOperator;
import jo.basic.logic.expr.IExprProps;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.FloatUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.LongUtils;

public class ExprOpExponent extends ExprOperator
{
    public ExprOpExponent()
    {
        mAliases.add("^");
        mPrecedence = 16 - 4;
    }

    @Override
    public Object eval(Object arg1, Object arg2, IExprProps props)
    {
        if (arg1 == null)
            arg1 = 0;
        if (arg2 == null)
            arg2 = 0;
        if (arg1 instanceof Boolean)
            arg1 = ((Boolean)arg1).booleanValue() ? 1 : 0;
        if (arg2 instanceof Boolean)
            arg2 = ((Boolean)arg2).booleanValue() ? 1 : 0;
        if ((arg1 instanceof Double) || (arg2 instanceof Double))
            return Math.pow(DoubleUtils.parseDouble(arg1), DoubleUtils.parseDouble(arg2));
        if ((arg1 instanceof Float) || (arg2 instanceof Float))
            return (float)Math.pow(FloatUtils.parseFloat(arg1), FloatUtils.parseFloat(arg2));
        if ((arg1 instanceof Long) || (arg2 instanceof Long))
            return (long)Math.pow(LongUtils.parseLong(arg1), LongUtils.parseLong(arg2));
        if ((arg1 instanceof Integer) || (arg2 instanceof Integer))
            return (int)Math.pow(IntegerUtils.parseInt(arg1), IntegerUtils.parseInt(arg2));
        return 0;
    }
    
}
