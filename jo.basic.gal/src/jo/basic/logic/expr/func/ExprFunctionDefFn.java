package jo.basic.logic.expr.func;

import jo.basic.data.BasicRuntime;
import jo.basic.data.FunctionBean;
import jo.basic.data.VariableBean;
import jo.basic.logic.ExecutionPointer;
import jo.basic.logic.RuntimeLogic;
import jo.basic.logic.expr.ExprFunction;

public class ExprFunctionDefFn extends ExprFunction
{
    public ExprFunctionDefFn()
    {
    }
    
    @Override
    public boolean isFunction(String name, int numArgs)
    {
        BasicRuntime rt = RuntimeLogic.getRuntime();
        FunctionBean val = rt.getProgram().getFunctions().get(name.toUpperCase());
        if (val == null)
            return false;
        if (val.getArgs().size() != numArgs)
            return false;
        return true;
    }

    @Override
    public Object eval(String name, Object[] args)
    {
        BasicRuntime rt = RuntimeLogic.getRuntime();
        FunctionBean func = rt.getProgram().getFunctions().get(name.toUpperCase());
        ExecutionPointer ep = new ExecutionPointer(rt);
        for (int i = 0; i < args.length; i++)
        {
            VariableBean var = func.getArgs().get(i);
            ep.put(var, args[i]);
        }
        if (func.getCode() != null)
        {
            Object val = ep.eval(func.getCode());
            return val;
        }
        else
        {
            int oldExecutionPoint = rt.getExecutionPoint();
            int newExecutionPoint = rt.getProgram().getLabels().get(name.toUpperCase());
            rt.setExecutionPoint(newExecutionPoint);
            RuntimeLogic.executeSteps(ep, RuntimeLogic.UNTIL_END_DEF);
            Object val = ep.get(func.getName());
            rt.setExecutionPoint(oldExecutionPoint);
            rt.getVariables().remove(func.getName().toUpperCase());
            return val;
        }
    }
}
