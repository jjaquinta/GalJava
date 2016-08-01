package jo.basic.logic.expr.func;

import java.io.IOException;
import java.io.Reader;

import jo.basic.data.BasicRuntime;
import jo.basic.logic.RuntimeLogic;
import jo.basic.logic.expr.ExprFunction;
import jo.util.utils.obj.IntegerUtils;

public class ExprFunctionEOF extends ExprFunction
{
    public ExprFunctionEOF()
    {
        addArg("eof", 1, 1);
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        int idx = IntegerUtils.parseInt(args[0]);
        BasicRuntime rt = RuntimeLogic.getRuntime();
        Object stream = rt.getStreams()[idx];
        if (stream instanceof Reader)
            try
            {
                return ((Reader)stream).ready() ? 0 : -1;
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        throw new RuntimeException("No input stream #"+idx);
    }
}
