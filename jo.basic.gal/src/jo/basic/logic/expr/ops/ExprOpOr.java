package jo.basic.logic.expr.ops;

import jo.basic.logic.expr.ExprOperator;
import jo.basic.logic.expr.IExprProps;
import jo.util.utils.obj.BooleanUtils;

public class ExprOpOr extends ExprOperator
{
    public ExprOpOr()
    {
        mAliases.add("OR");
        mPrecedence = 16 - 13;
    }

    @Override
    public Object eval(Object arg1, Object arg2, IExprProps props)
    {
        boolean a1 = BooleanUtils.parseBoolean(arg1);
        boolean a2 = BooleanUtils.parseBoolean(arg2);
        return a1 || a2;
    }
}
