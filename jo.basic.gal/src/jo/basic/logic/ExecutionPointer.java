package jo.basic.logic;

import jo.basic.data.BasicRuntime;
import jo.basic.data.ExpressionBean;
import jo.basic.data.LineBean;
import jo.basic.data.SyntaxBean;
import jo.basic.data.TokenBean;
import jo.basic.data.VariableBean;
import jo.basic.logic.expr.ExprUtils;
import jo.basic.logic.expr.ExpressionEvaluationException;
import jo.basic.logic.expr.IExprProps;

public class ExecutionPointer implements IExprProps
{
    public BasicRuntime rt;
    
    public ExecutionPointer(BasicRuntime rt)
    {
        this.rt = rt;
    }    
    
    public boolean isMore()
    {
        return rt.getProgram().getSyntax().size() > rt.getExecutionPoint();
    }

    public SyntaxBean command()
    {
        return rt.getProgram().getSyntax().get(rt.getExecutionPoint());
    }
    
    public Object arg1()
    {
        return command().getArg1();
    }
    
    public Object arg2()
    {
        return command().getArg2();
    }
    
    public Object arg3()
    {
        return command().getArg3();
    }
    
    public Object arg4()
    {
        return command().getArg4();
    }
    
    public TokenBean token()
    {
        SyntaxBean cmd = command();
        return rt.getProgram().getTokens().get(cmd.getFirstToken());
    }
    
    public LineBean line()
    {
        TokenBean tok = token();
        return tok.getLine();
    }

    public String statement()
    {
        SyntaxBean cmd = command();
        TokenBean firstToken = rt.getProgram().getTokens().get(cmd.getFirstToken());
        TokenBean lastToken = rt.getProgram().getTokens().get(cmd.getLastToken() - 1);
        if (firstToken.getLine() == lastToken.getLine())
            return firstToken.getLine().getText().substring(firstToken.getCharStart(), lastToken.getCharEnd());
        else
            return firstToken.getLine().getText().substring(firstToken.getCharStart()) + " | " +
                lastToken.getLine().getText().substring(0, lastToken.getCharEnd());
    }
    
    public void put(String var, Object val)
    {
        rt.getVariables().put(var.toUpperCase(), val);
    }
    
    public Object get(String var)
    {
        if (var.equalsIgnoreCase("INKEY$"))
            return rt.getScreen().inkey();
        return rt.getVariables().get(var.toUpperCase());
    }
    
    public Object get(VariableBean var)
    {
        Object val = get(var.getName());
        if (var.getIndicies().size() == 1)
            return ((Object[])val)[evalInt(var.getIndicies().get(0))];
        if (var.getIndicies().size() == 2)
            return ((Object[][])val)[evalInt(var.getIndicies().get(0))][evalInt(var.getIndicies().get(1))];
        if (var.getIndicies().size() == 3)
            return ((Object[][][])val)[evalInt(var.getIndicies().get(0))][evalInt(var.getIndicies().get(1))][evalInt(var.getIndicies().get(2))];
        return val;
    }
 
    public void put(VariableBean var, Object rvalue)
    {
        if (var.getIndicies().size() == 0)
            put(var.getName(), rvalue);
        else
        {
            int[] idx = new int[var.getIndicies().size()];
            for (int i = 0; i < var.getIndicies().size(); i++)
                idx[i] = evalInt(var.getIndicies().get(i));
            if (idx.length == 1)
            {
                Object[] val = (Object[])get(var.getName());
                if (val == null)
                {
                    val = new Object[256];
                    put(var.getName(), val);
                }
                val[idx[0]] = rvalue;
            }
            else if (idx.length == 2)
            {
                Object[][] val = (Object[][])get(var.getName());
                val[idx[0]][idx[1]] = rvalue;
            }
            else if (idx.length == 3)
            {
                Object[][][] val = (Object[][][])get(var.getName());
                val[idx[0]][idx[1]][idx[2]] = rvalue;
            }
            else
                throw new RuntimeException(var.getIndicies().size()+" dimensional arrays not supported");
        }
    }
    
    public void inc()
    {
        rt.setExecutionPoint(rt.getExecutionPoint() + 1);
    }
    
    public int evalInt(ExpressionBean expr)
    {
        Object val = eval(expr);
        if (val instanceof Number)
            return ((Number)val).intValue();
        throw new RuntimeException("Expected int value, not "+val);
    }
    
    public long evalLong(ExpressionBean expr)
    {
        Object val = eval(expr);
        if (val instanceof Number)
            return ((Number)val).longValue();
        throw new RuntimeException("Expected long value, not "+val);
    }
    
    public String evalString(ExpressionBean expr)
    {
        Object val = eval(expr);
        if (val == null)
            return null;
        return val.toString();
    }
    
    public Object eval(ExpressionBean expr)
    {
        Object rvalue;
        try
        {
            rvalue = ExprUtils.evalObject(expr.tokens(rt.getProgram()), this);
        }
        catch (ExpressionEvaluationException e)
        {
            throw new RuntimeException(e);
        }
        return rvalue;
    }
}