package jo.basic.logic.expr.ops;

import jo.basic.logic.expr.ExprOperator;
import jo.basic.logic.expr.IExprProps;
import jo.util.utils.obj.IntegerUtils;

public class ExprOpMod extends ExprOperator
{
    public ExprOpMod()
    {
        mAliases.add("MOD");
        mPrecedence = 16 - 4;
    }

    @Override
    public Object eval(Object arg1, Object arg2, IExprProps props)
    {
        if (arg1 == null)
            arg1 = 0;
        assertNotNull(arg2);
        int i1 = IntegerUtils.parseInt(arg1);
        int i2 = IntegerUtils.parseInt(arg2);
        return i1 % i2;
    }
    
}
