package jo.basic.logic;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Stack;

import jo.basic.data.BasicRuntime;
import jo.basic.data.ExpressionBean;
import jo.basic.data.SyntaxBean;
import jo.basic.data.TokenBean;
import jo.basic.data.VariableBean;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class RuntimeLogic
{
    public static final int UNTIL_END = 0;
    public static final int UNTIL_NEXT = 1;
    public static final int UNTIL_RETURN = 2;
    public static final int UNTIL_LOOP = 3;
    public static final int UNTIL_END_IF = 4;
    public static final int UNTIL_ONE = 5;
    public static final int UNTIL_END_SELECT = 6;
    public static final int UNTIL_END_DEF = 7;
    
    public static void execute(BasicRuntime rt)
    {
        rt.setExecutionPoint(0);
        rt.setDataPointer(0);
        rt.getVariables().clear();
        ExecutionPointer ep = new ExecutionPointer(rt);
        ep.put("TIMER", System.currentTimeMillis());
        if (!StringUtils.isTrivial(rt.getArgs()))
            ep.put("COMMAND$", rt.getArgs());
        setRuntime(rt);
        try
        {
            executeSteps(ep, UNTIL_END);
        }
        catch (FunctionExitException e)
        {
            throw new RuntimeException("Unexpected function exit", e);
        }
        unsetRuntime();
    }
    
    private static Stack<BasicRuntime> mRTCache = new Stack<>();
    
    private static void setRuntime(BasicRuntime rt)
    {
        mRTCache.push(rt);
    }
    
    private static void unsetRuntime()
    {
        mRTCache.pop();
    }
    
    public static BasicRuntime getRuntime()
    {
        return mRTCache.peek();
    }
    
    public static void executeSteps(ExecutionPointer ep, int terminalCondition) throws FunctionExitException
    {
        while (ep.isMore())
        {
            SyntaxBean cmd = ep.command();
            //System.out.println(ep.statement());
            switch (cmd.getType())
            {
                case SyntaxBean.DIM:
                    executeDim(ep);
                    break;
                case SyntaxBean.ONERRORGOTO:
                    executeOnErrorGoto(ep);
                    break;
                case SyntaxBean.RANDOMIZE:
                    executeRandomize(ep);
                    break;
                case SyntaxBean.DEF:
                    executeDef(ep);
                    break;
                case SyntaxBean.LET:
                    executeLet(ep);
                    break;
                case SyntaxBean.SWAP:
                    executeSwap(ep);
                    break;
                case SyntaxBean.FOR:
                    executeFor(ep);
                    break;
                case SyntaxBean.DO_UNTIL:
                    executeDoUntil(ep);
                    break;
                case SyntaxBean.SELECT:
                    executeSelect(ep);
                    break;
                case SyntaxBean.READ:
                    executeRead(ep);
                    break;
                case SyntaxBean.GOSUB:
                    executeGosub(ep);
                    break;
                case SyntaxBean.GOTO:
                    executeGoto(ep);
                    break;
                case SyntaxBean.OPEN:
                    executeOpen(ep);
                    break;
                case SyntaxBean.LINE_INPUT:
                    executeLineInput(ep);
                    break;
                case SyntaxBean.CLOSE:
                    executeClose(ep);
                    break;
                case SyntaxBean.IF_GOTO:
                    executeIfGoto(ep);
                    break;
                case SyntaxBean.IF_GOSUB:
                    executeIfGosub(ep);
                    break;
                case SyntaxBean.IF_BLOCK:
                    executeIfBlock(ep);
                    break;
                case SyntaxBean.IF_COMMAND:
                    executeIfCommand(ep);
                    break;
                case SyntaxBean.IF_LET:
                    executeIfLet(ep);
                    break;
                case SyntaxBean.IF_RETURN:
                    if (executeIfReturn(ep))
                        if (terminalCondition == UNTIL_RETURN)
                        {
                            return;
                        }
                        else
                            ep.error("Unexpected RETURN, mode="+terminalCondition);
                    break;
                case SyntaxBean.SCREEN:
                    executeScreen(ep);
                    break;
                case SyntaxBean.CLS:
                    executeClearScreen(ep);
                    break;
                case SyntaxBean.COLOR:
                    executeColor(ep);
                    break;
                case SyntaxBean.PALETTE:
                    executePalette(ep);
                    break;
                case SyntaxBean.LOCATE:
                    executeLocate(ep);
                    break;
                case SyntaxBean.PRINT:
                    executePrint(ep);
                    break;
                case SyntaxBean.PSET:
                    executePSet(ep);
                    break;
                case SyntaxBean.DRAW_LINE:
                    executeDrawLine(ep);
                    break;
                case SyntaxBean.CIRCLE:
                    executeCircle(ep);
                    break;
                case SyntaxBean.PAINT:
                    executePaint(ep);
                    break;
                case SyntaxBean.GET_IMAGE:
                    executeGetImage(ep);
                    break;
                case SyntaxBean.PUT_IMAGE:
                    executePutImage(ep);
                    break;
                case SyntaxBean.SHELL:
                    executeShell(ep);
                    break;
                case SyntaxBean.SOUND:
                    executeSound(ep);
                    break;
                case SyntaxBean.NEXT:
                    if (terminalCondition == UNTIL_NEXT)
                    {
                        ep.inc();
                        return;
                    }
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected NEXT");
                case SyntaxBean.LOOP:
                    if (terminalCondition == UNTIL_LOOP)
                    {
                        ep.inc();
                        return;
                    }
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected LOOP");
                case SyntaxBean.RETURN:
                    if (terminalCondition == UNTIL_RETURN)
                    {
                        ep.inc();
                        return;
                    }
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected RETURN, mode="+terminalCondition);
                case SyntaxBean.EXIT_FUNCTION:
                    throw new FunctionExitException();
                case SyntaxBean.ELSE:
                case SyntaxBean.ELSEIF_BLOCK:
                    if (terminalCondition == UNTIL_END_IF)
                    {
                        ep.inc();
                        skipToEndIf(ep);
                        return;
                    }
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected ELSE");
                case SyntaxBean.END_IF:
                    if (terminalCondition == UNTIL_END_IF)
                    {
                        ep.inc();
                        return;
                    }
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected END IF");
                case SyntaxBean.CASE:
                    if (terminalCondition == UNTIL_END_SELECT)
                        return;
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected CASE");
                case SyntaxBean.END_DEF:
                case SyntaxBean.END_FUNCTION:
                    if (terminalCondition == UNTIL_END_DEF)
                        return;
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected END DEF");
                case SyntaxBean.END_SELECT:
                    if (terminalCondition == UNTIL_END_SELECT)
                        return;
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected END_SELECT");
                case SyntaxBean.END_PROGRAM:
                    if (terminalCondition == UNTIL_END)
                        return;
                    System.err.println(ep.line().getText());
                    ep.error("Unexpected END");
                case SyntaxBean.DEBUG:
                    executeDebug(ep);
                    break;
                default:
                    System.err.println(ep.line().getText());
                    ep.error("Unhandled command #"+cmd.getType());
            }
            if (terminalCondition == UNTIL_ONE)
                break;
        }
    }
    
    private static void executeShell(ExecutionPointer ep)
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        ep.inc();
        String cmdline = ep.evalString(expr);
        if (cmdline.toUpperCase().indexOf("EGACOLOR") >= 0)
            return; // only used to remap colors
        int o = cmdline.indexOf(' ');
        if (o < 0)
            o = cmdline.length();
        String cmd = cmdline.substring(0, o);
        if (cmd.toUpperCase().indexOf("EGACOLOR") >= 0)
            return; // only used to remap colors
        if (isBasicFile(ep, cmd))
        {
            BasicRuntime subRT = new BasicRuntime();
            subRT.setRoot(ep.rt.getRoot());
            subRT.setScreen(ep.rt.getScreen());
            subRT.setArgs(cmdline.substring(o).trim());
            try
            {
                IOLogic.load(subRT, cmd + ".BAS");
                RuntimeLogic.execute(subRT);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (cmdline.startsWith("call less "))
        {
            String fname = cmdline.substring(9).trim();
            File f = ep.makeFile(fname);
            ep.rt.getScreen().view(f);
        }
        else if (cmdline.startsWith("less "))
        {
            String fname = cmdline.substring(4).trim();
            File f = ep.makeFile(fname);
            ep.rt.getScreen().view(f);
        }
        else if (cmdline.startsWith("erase "))
        {
            String fname = cmdline.substring(5).trim();
            File f = ep.makeFile(fname);
            f.delete();
        }
        else
        {
            System.out.println("Exec: "+cmdline);
            try
            {
                Runtime.getRuntime().exec(cmdline);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private static void executeSound(ExecutionPointer ep)
    {
        Toolkit.getDefaultToolkit().beep();
        ep.inc();
    }
    
    private static boolean isBasicFile(ExecutionPointer ep, String cmd)
    {
        File f = ep.makeFile(cmd+".BAS");
        return f.exists();
    }
    
    private static void executeScreen(ExecutionPointer ep)
    {
        int num = IntegerUtils.parseInt(ep.command().getArg1());
        ep.rt.getScreen().screen(num);
        ep.inc();
    }
    
    private static void executeClearScreen(ExecutionPointer ep)
    {
        ep.rt.getScreen().cls();
        ep.inc();
    }
    
    private static void executeColor(ExecutionPointer ep)
    {
        int num = ep.evalInt((ExpressionBean)ep.command().getArg1());
        ep.rt.getScreen().color(num);
        ep.inc();
    }
    
    private static void executePalette(ExecutionPointer ep)
    {
        @SuppressWarnings("unchecked")
        List<ExpressionBean> exprs = (List<ExpressionBean>)ep.command().getArg1();
        if (exprs.size() >= 2)
        {
            int colorNum = ep.evalInt(exprs.get(0));
            int colorVal = ep.evalInt(exprs.get(1));
            ep.rt.getScreen().palette(colorNum, colorVal);
        }
        else
            ep.rt.getScreen().paletteReset();
        ep.inc();
    }
    
    private static void executeLocate(ExecutionPointer ep)
    {
        @SuppressWarnings("unchecked")
        List<ExpressionBean> exprs = (List<ExpressionBean>)ep.command().getArg1();
        int y = ep.evalInt(exprs.get(0));
        int x = ep.evalInt(exprs.get(1));
        ep.rt.getScreen().locate(x, y);
        ep.inc();
    }
    
    private static void executePrint(ExecutionPointer ep)
    {
        Integer stream = null;
        StringBuffer txt = new StringBuffer();
        @SuppressWarnings("unchecked")
        List<Object> args = (List<Object>)ep.command().getArg1();
        for (int i = 0; i < args.size(); i++)
        {
            Object o = args.get(i);
            if (o instanceof TokenBean)
                switch (((TokenBean)o).getType())
                {
                    case TokenBean.HASH:
                        i++;
                        ExpressionBean expr = ((ExpressionBean)args.get(i));
                        stream = ep.evalInt(expr);
                        break;
                    case TokenBean.COMMA:
                        txt.append(" ");
                        while ((txt.length()%8) != 0)
                            txt.append(" ");
                        break;
                    case TokenBean.SEMICOLON:
                        break;
                    default:
                        ep.error("Unknown print argument #"+((TokenBean)o).getType());
                }
            else if (o instanceof ExpressionBean)
                txt.append(ep.evalString((ExpressionBean)o));
            else
                ep.error("Unknown print argument "+o);
        }
        boolean noNewline = (args.size() > 0) && (args.get(args.size() - 1) instanceof TokenBean) && (((TokenBean)args.get(args.size() - 1)).getType() == TokenBean.SEMICOLON);
        if (stream != null)
        {
            if (ep.rt.getStreams()[stream] instanceof BufferedWriter)
            {
                if (!noNewline)
                    txt.append(System.getProperty("line.separator"));
                try
                {
                    ((BufferedWriter)ep.rt.getStreams()[stream]).write(txt.toString());
                }
                catch (IOException e)
                {
                    ep.error(e);
                }
            }
            else
                ep.error("Stream "+stream+" not open for writing.");
        }
        else
        {
            if (!noNewline)
                txt.append("\r\n");
            ep.rt.getScreen().print(txt.toString());
        }
        ep.inc();
    }
    
    private static void executePSet(ExecutionPointer ep)
    {
        int x = ep.evalInt((ExpressionBean)ep.arg1());
        int y = ep.evalInt((ExpressionBean)ep.arg2());
        ep.rt.getScreen().pset(x, y);
        ep.inc();
    }
    
    private static void executeDrawLine(ExecutionPointer ep)
    {
        if (ep.arg1() instanceof List<?>)
        {
            @SuppressWarnings("unchecked")
            List<ExpressionBean> exprs = (List<ExpressionBean>)ep.arg1();
            int x1 = ep.evalInt(exprs.get(0));
            int y1 = ep.evalInt(exprs.get(1));
            int x2 = ep.evalInt(exprs.get(2));
            int y2 = ep.evalInt(exprs.get(3));
            if (exprs.size() > 4)
            {
                int color = ep.evalInt(exprs.get(4));
                ep.rt.getScreen().color(color);
            }
            ep.rt.getScreen().line(x1, y1, x2, y2);
        }
        else
        {
            int x = ep.evalInt((ExpressionBean)ep.arg1());
            int y = ep.evalInt((ExpressionBean)ep.arg2());
            ep.rt.getScreen().lineTo(x, y);
        }
        ep.inc();
    }
    
    private static void executeCircle(ExecutionPointer ep)
    {
        int x = ep.evalInt((ExpressionBean)ep.arg1());
        int y = ep.evalInt((ExpressionBean)ep.arg2());
        int r = ep.evalInt((ExpressionBean)ep.arg3());
        Integer color = null;
        if (ep.arg4() != null)
            color = ep.evalInt((ExpressionBean)ep.arg4());
        ep.rt.getScreen().circle(x, y, r, color);
        ep.inc();
    }
    
    private static void executePaint(ExecutionPointer ep)
    {
        int x = ep.evalInt((ExpressionBean)ep.arg1());
        int y = ep.evalInt((ExpressionBean)ep.arg2());
        int color = ep.evalInt((ExpressionBean)ep.arg3());
        ep.rt.getScreen().paint(x, y, color);
        ep.inc();
    }
    
    private static void executeGetImage(ExecutionPointer ep)
    {
        @SuppressWarnings("unchecked")
        List<ExpressionBean> ords = (List<ExpressionBean>)ep.arg1();
        VariableBean var = (VariableBean)ep.arg2();
        int x1 = ep.evalInt(ords.get(0));
        int y1 = ep.evalInt(ords.get(1));
        int x2 = ep.evalInt(ords.get(2));
        int y2 = ep.evalInt(ords.get(3));
        int[] data = ep.rt.getScreen().get(x1, y1, x2, y2);
        ep.put(var, data);
        ep.inc();
    }
    
    private static void executePutImage(ExecutionPointer ep)
    {
        @SuppressWarnings("unchecked")
        List<ExpressionBean> ords = (List<ExpressionBean>)ep.arg1();
        VariableBean var = (VariableBean)ep.arg2();
        int x = ep.evalInt(ords.get(0));
        int y = ep.evalInt(ords.get(1));
        int[] data = (int[])ep.get(var);
        ep.rt.getScreen().put(x, y, data);
        ep.inc();
    }

    @SuppressWarnings("resource")
    private static void executeOpen(ExecutionPointer ep)
    {
        try
        {
            String mode = (String)ep.arg1();
            ExpressionBean idxEx = (ExpressionBean)ep.arg2();
            int idx = ep.evalInt(idxEx);
            ExpressionBean file = (ExpressionBean)ep.arg3();
            String fname = ep.evalString(file);
            closeStream(ep, idx);
            File f = ep.makeFile(fname);
            ep.rt.getStreamFiles()[idx] = f;
            if ("i".equalsIgnoreCase(mode))
            {
                BufferedReader rdr = new BufferedReader(new FileReader(f));
                ep.rt.getStreams()[idx] = rdr;
            }
            else if ("o".equalsIgnoreCase(mode))
            {
                BufferedWriter wtr = new BufferedWriter(new FileWriter(f));
                ep.rt.getStreams()[idx] = wtr;
            }
            else if ("r".equalsIgnoreCase(mode))
            {
                ep.rt.getStreams()[idx] = f; // test for exists
            }
            else
                ep.error("Unrecognized mode "+mode);
            ep.inc();
        }
        catch (IOException e)
        {
            ep.error(e);
        }
    }
    
    private static void executeLineInput(ExecutionPointer ep)
    {
        try
        {
            if (ep.arg1() instanceof Integer)
            {
                int idx;
                if (ep.arg1() instanceof Integer)
                    idx = (Integer)ep.arg1();
                else
                {
                    VariableBean idxV = (VariableBean)ep.arg1();
                    idx = IntegerUtils.parseInt(ep.get(idxV));
                }
                VariableBean var = (VariableBean)ep.arg2();
                if (ep.rt.getStreams()[idx] instanceof BufferedReader)
                {
                    String val = ((BufferedReader)ep.rt.getStreams()[idx]).readLine();
                    ep.put(var, val);
                    //System.out.println("Input: "+val);
                }
                else
                    ep.error("Stream #"+idx+" not for input");
            }
            else
            {
                String prompt = (String)ep.arg1();
                VariableBean var = (VariableBean)ep.arg2();
                String val = ep.rt.getScreen().input(prompt);
                ep.put(var, val);
            }
            ep.inc();
        }
        catch (IOException e)
        {
            ep.error(e);
        }
    }
    
    private static void executeClose(ExecutionPointer ep)
    {
        if (ep.arg1() instanceof Integer)
            closeStream(ep, (Integer)ep.arg1());
        else if (ep.arg1() instanceof VariableBean)
        {
            VariableBean var = (VariableBean)ep.arg1();
            int idx = IntegerUtils.parseInt(ep.get(var));
            closeStream(ep, idx);
        }
        else
            for (int i = 0; i < ep.rt.getStreams().length; i++)
                closeStream(ep, i);
        ep.inc();
    }

    private static void closeStream(ExecutionPointer ep, int idx)
    {
        try
        {
            if (ep.rt.getStreams()[idx] instanceof Reader)
                ((Reader)ep.rt.getStreams()[idx]).close();
            else if (ep.rt.getStreams()[idx] instanceof Writer)
                ((Writer)ep.rt.getStreams()[idx]).close();
        }
        catch (IOException e)
        {
            ep.error(e);
        }
    }
    
    private static void executeGosub(ExecutionPointer ep) throws FunctionExitException
    {
        String var = (String)ep.arg1();
        int step = ep.rt.getProgram().getLabels().get(var);
        ep.inc();
        int returnPoint = ep.rt.getExecutionPoint();
        ep.rt.setExecutionPoint(step);
        executeSteps(ep, UNTIL_RETURN);
        ep.rt.setExecutionPoint(returnPoint);
    }
    
    private static void executeGoto(ExecutionPointer ep)
    {
        String var = (String)ep.arg1();
        int step = ep.rt.getProgram().getLabels().get(var);
        ep.rt.setExecutionPoint(step);
    }
    
    private static void executeIfGoto(ExecutionPointer ep)
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        Boolean test = BooleanUtils.parseBoolean(ep.eval(expr));
        if (test)
        {
            String var = (String)ep.arg2();
            int step = ep.rt.getProgram().getLabels().get(var);
            ep.rt.setExecutionPoint(step);
        }
        else
            ep.inc();
    }
    
    private static void executeIfGosub(ExecutionPointer ep) throws FunctionExitException
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        Boolean test = BooleanUtils.parseBoolean(ep.eval(expr));
        if (test)
        {
            String var = (String)ep.arg2();
            int step = ep.rt.getProgram().getLabels().get(var);
            ep.inc();
            int returnPoint = ep.rt.getExecutionPoint();
            ep.rt.setExecutionPoint(step);
            executeSteps(ep, UNTIL_RETURN);
            ep.rt.setExecutionPoint(returnPoint);
        }
        else
            ep.inc();
    }
    
    private static boolean executeIfReturn(ExecutionPointer ep)
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        Boolean test = BooleanUtils.parseBoolean(ep.eval(expr));
        ep.inc();
        return test;
    }
    
    private static void executeIfBlock(ExecutionPointer ep) throws FunctionExitException
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        ep.inc();
        Boolean test = BooleanUtils.parseBoolean(ep.eval(expr));
        if (!test)
        {
            for (;;)
            {
                skipToElse(ep);
                if (ep.command().getType() == SyntaxBean.ELSE)
                {
                    ep.inc();
                    break;
                }
                else if (ep.command().getType() == SyntaxBean.END_IF)
                {
                    ep.inc();
                    return; // no else clause
                }
                else // ELSE_IF
                {
                    expr = (ExpressionBean)ep.arg1();
                    ep.inc();
                    test = BooleanUtils.parseBoolean(ep.eval(expr));
                    if (test)
                        break;
                }
            }
        }
        executeSteps(ep, UNTIL_END_IF);
    }
    
    private static void executeIfCommand(ExecutionPointer ep) throws FunctionExitException
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        ep.inc();
        Boolean test = BooleanUtils.parseBoolean(ep.eval(expr));
        if (test)
        {
            executeSteps(ep, UNTIL_ONE);
            if (ep.command().getType() == SyntaxBean.ELSE)
            {
                ep.inc();
                ep.inc();
            }
        }
        else
        {
            ep.inc();
            if (ep.command().getType() == SyntaxBean.ELSE)
                ep.inc();
        }
    }
    
    private static void executeIfLet(ExecutionPointer ep)
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        VariableBean var = (VariableBean)ep.arg2();
        if (var == null)
            ep.error("Expected var on IF_LET");
        ExpressionBean val = (ExpressionBean)ep.arg3();
        if (val == null)
            ep.error("Expected val on IF_LET");
        ep.inc();
        Boolean test = BooleanUtils.parseBoolean(ep.eval(expr));
        if (test)
        {
            ep.put(var, ep.eval(val));
            if (ep.command().getType() == SyntaxBean.ELSE)
            {
                ep.inc();
                ep.inc();
            }
        }
        else
        {
            if (ep.command().getType() == SyntaxBean.ELSE)
                ep.inc();
        }
    }
    
    private static void skipToElse(ExecutionPointer ep)
    {
        int depth = 0;
        for (;;)
        {
            int type = ep.command().getType();
            if ((depth == 0) && ((type == SyntaxBean.ELSE) || (type == SyntaxBean.ELSEIF_BLOCK) || (type == SyntaxBean.END_IF)))
                break;
            else if (type == SyntaxBean.IF_BLOCK)
                depth++;
            else if (type == SyntaxBean.END_IF)
                depth--;
            ep.inc();
        }
    }
    
    private static void skipToEndIf(ExecutionPointer ep)
    {
        int depth = 0;
        for (;;)
        {
            int type = ep.command().getType();
            if ((depth == 0) && (type == SyntaxBean.END_IF))
                break;
            else if (type == SyntaxBean.IF_BLOCK)
                depth++;
            else if (type == SyntaxBean.END_IF)
                depth--;
            ep.inc();
        }
        ep.inc();
    }
    
    private static void executeRead(ExecutionPointer ep)
    {
        VariableBean var = (VariableBean)ep.arg1();
        ExpressionBean expr = ep.rt.getProgram().getData().get(ep.rt.getDataPointer());
        Object val = ep.eval(expr);
        ep.put(var, val);
        ep.rt.setDataPointer(ep.rt.getDataPointer() + 1);
        ep.inc();
    }
    
    private static void executeFor(ExecutionPointer ep) throws FunctionExitException
    {
        String var = (String)ep.arg1();
        ExpressionBean startExpr = (ExpressionBean)ep.arg2();
        ExpressionBean endExpr = (ExpressionBean)ep.arg3();
        ExpressionBean stepExpr = (ExpressionBean)ep.arg4();
        int start = ep.evalInt(startExpr);
        int end = ep.evalInt(endExpr);
        int step = (stepExpr != null) ? ep.evalInt(stepExpr) : 1;
        ep.inc();
        int loopTop = ep.rt.getExecutionPoint();
        for (int i = start; i <= end; i += step)
        {
            //System.out.println("for loop at "+i+" of "+end);
            ep.rt.setExecutionPoint(loopTop);
            ep.put(var, i);
            executeSteps(ep, UNTIL_NEXT);
        }
    }
    
    private static void executeDoUntil(ExecutionPointer ep) throws FunctionExitException
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        ep.inc();
        int loopTop = ep.rt.getExecutionPoint();
        for (;;)
        {
            ep.rt.setExecutionPoint(loopTop);
            Boolean cont = BooleanUtils.parseBoolean(ep.eval(expr));
            if (cont)
                break;
            executeSteps(ep, UNTIL_LOOP);
        }
        while (ep.command().getType() != SyntaxBean.LOOP)
            ep.inc();
        ep.inc();
    }
    
    private static void executeSelect(ExecutionPointer ep) throws FunctionExitException
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        int val = ep.evalInt(expr);
        ep.inc();
        boolean haveWeRun = false;
        for (;;)
        {
            if (ep.command().getType() == SyntaxBean.END_SELECT)
                break;
            if ((ep.command().getType() == SyntaxBean.CASE) && !haveWeRun)
            {
                boolean runThis = false;
                TokenBean arg1 = (TokenBean)ep.arg1();
                TokenBean arg2 = (TokenBean)ep.arg2();
                if (arg1.getType() == TokenBean.ELSE)
                    runThis = true;
                else if (arg1.getType() == TokenBean.NUMBER)
                {
                    int low = IntegerUtils.parseInt(arg1.getTokenText());
                    if (arg2 != null)
                    {
                        int high = IntegerUtils.parseInt(arg2.getTokenText());
                        runThis = (val >= low) && (val <= high);
                    }
                    else
                        runThis = (val == low);
                }
                else if (arg1.getType() == TokenBean.GREATERTHAN)
                {
                    int high = IntegerUtils.parseInt(arg2.getTokenText());
                    runThis = (val >= high);
                }
                else
                    ep.error("Unknown case");
                if (runThis)
                {
                    ep.inc();
                    executeSteps(ep, UNTIL_END_SELECT);                    
                    break;
                }
            }
            ep.inc();
        }
        while (ep.command().getType() != SyntaxBean.END_SELECT)
            ep.inc();
        ep.inc();
    }
    
    private static void executeLet(ExecutionPointer ep)
    {
        VariableBean var = (VariableBean)ep.arg1();
        ExpressionBean rval = (ExpressionBean)ep.arg2();
        Object rvalue = ep.eval(rval);
        ep.put(var, rvalue);
        ep.inc();
    }
    
    private static void executeSwap(ExecutionPointer ep)
    {
        VariableBean var1 = (VariableBean)ep.arg1();
        VariableBean var2 = (VariableBean)ep.arg2();
        Object val1 = ep.get(var1);
        Object val2 = ep.get(var2);
        ep.put(var1, val2);
        ep.put(var2, val1);
        ep.inc();
    }
    
    private static void executeDef(ExecutionPointer ep)
    {
        ep.inc();
        while (ep.command().getType() != SyntaxBean.END_DEF)
            ep.inc();
        ep.inc();
    }
    
    private static void executeRandomize(ExecutionPointer ep)
    {
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        long val = ep.evalLong(expr);
        ep.rt.getRND().setSeed(val);
        ep.inc();
    }
    
    private static void executeDim(ExecutionPointer ep)
    {
        String var = (String)ep.arg1();
        @SuppressWarnings("unchecked")
        List<Integer> dims = (List<Integer>)ep.arg2();
        if ((dims == null) || (dims.size() == 0))
            ep.put(var, null);
        else if (dims.size() == 1)
            ep.put(var, new Object[dims.get(0)+1]);
        else if (dims.size() == 2)
            ep.put(var, new Object[dims.get(0)+1][dims.get(1)+1]);
        else if (dims.size() == 3)
            ep.put(var, new Object[dims.get(0)+1][dims.get(1)+1][dims.get(2)+1]);
        else
        {
            System.err.println(ep.line().getText());
            ep.error("Can't handle #"+ep.command().getType());
        }
        ep.inc();
    }
    
    private static void executeOnErrorGoto(ExecutionPointer ep)
    {
        Integer label = (Integer)ep.arg1();
        ep.rt.setOnErrorLabel(label);
        ep.inc();
    }
    
    private static void executeDebug(ExecutionPointer ep)
    {
        ep.rt.getScreen().saveScreen();
        ExpressionBean expr = (ExpressionBean)ep.arg1();
        System.err.println(ep.eval(expr));
        ep.inc();
    }
}

