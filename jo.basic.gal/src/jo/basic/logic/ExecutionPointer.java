package jo.basic.logic;

import java.io.File;

import jo.basic.data.BasicRuntime;
import jo.basic.data.ExpressionBean;
import jo.basic.data.LineBean;
import jo.basic.data.SyntaxBean;
import jo.basic.data.TokenBean;
import jo.basic.data.VariableBean;
import jo.basic.logic.expr.ExprUtils;
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
                error(var.getIndicies().size()+" dimensional arrays not supported");
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
        error("Expected int value, not "+val);
        return 0;
    }
    
    public long evalLong(ExpressionBean expr)
    {
        Object val = eval(expr);
        if (val instanceof Number)
            return ((Number)val).longValue();
        error("Expected long value, not "+val);
        return 0;
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
        Object rvalue = null;
        try
        {
            rvalue = ExprUtils.evalObject(expr.tokens(rt.getProgram()), this);
        }
        catch (Exception e)
        {
            error(e);
        }
        return rvalue;
    }
    
    public File makeFile(String fname)
    {
    	return IOLogic.makeFile(rt.getRoot(), fname);
    }

    public String commandText()
    {
        StringBuffer sb = new StringBuffer();
        SyntaxBean cmd = command();
        for (int t = cmd.getFirstToken(); t < cmd.getLastToken(); t++)
            sb.append(rt.getProgram().getTokens().get(t).getTokenText());
        return sb.toString();
    }
    
    public void error(Exception e) throws RuntimeException
    {
        error(null, e);
    }
        
    public void error(String msg) throws RuntimeException
    {
        error(msg, null);
    }
        
    public void error(String msg, Exception e) throws RuntimeException
    {
        SyntaxBean cmd = command();
        TokenBean firstToken = rt.getProgram().getTokens().get(cmd.getFirstToken());
        TokenBean lastToken = rt.getProgram().getTokens().get(cmd.getLastToken());
        LineBean line = firstToken.getLine();
        System.err.println(rt.getProgram().getSource().getName()+": line="+line.getNumber());
        System.err.println(line.getText());
        for (int i = 0; i < firstToken.getCharStart(); i++)
            System.err.print(" ");
        for (int i = 0; i < lastToken.getCharEnd() - firstToken.getCharStart(); i++)
            System.err.print("~");
        System.err.println();
        dumpVars(cmd.getArg1());
        dumpVars(cmd.getArg2());
        dumpVars(cmd.getArg3());
        dumpVars(cmd.getArg4());
        throw new RuntimeException(msg, e);
    }
    
    private void dumpVars(Object v)
    {
        if (v instanceof VariableBean)
        {
            VariableBean var = (VariableBean)v;
            System.err.println(var.getName()+"='"+get(var)+"'");
        }
    }
}