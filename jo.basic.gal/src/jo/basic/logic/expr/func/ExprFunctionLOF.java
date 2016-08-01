package jo.basic.logic.expr.func;

import java.io.File;

import jo.basic.data.BasicRuntime;
import jo.basic.logic.RuntimeLogic;
import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionLOF extends ExprFunction
{
    public ExprFunctionLOF()
    {
        addArg("lof", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        int idx = IntegerUtils.parseInt(args[0]);
        BasicRuntime rt = RuntimeLogic.getRuntime();
        File stream = rt.getStreamFiles()[idx];
        if (stream != null)
            return stream.length();
        throw new RuntimeException("No input stream #"+idx);
    }
}
