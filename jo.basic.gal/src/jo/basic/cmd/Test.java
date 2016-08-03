package jo.basic.cmd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jo.basic.data.BasicRuntime;
import jo.basic.data.ExpressionBean;
import jo.basic.data.SyntaxBean;
import jo.basic.data.TokenBean;
import jo.basic.data.VariableBean;
import jo.basic.logic.IOLogic;
import jo.basic.logic.expr.ExprUtils;
import jo.basic.logic.expr.ExpressionEvaluationException;
import jo.basic.logic.expr.ParseNode;
import jo.basic.logic.expr.ParseUtils;

public class Test
{
    private String[] mArgs;
    private File     mRoot;

    public Test(String[] args)
    {
        mArgs = args;
    }

    public void run()
    {
        parseArgs();
        check(mRoot, "");
    }

    private void check(File dir, String prefix)
    {
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            if (f.isDirectory())
                check(f, prefix + f.getName() + "/");
            else if (f.getName().toUpperCase().endsWith(".BAS"))
            {
                BasicRuntime rt = new BasicRuntime();
                rt.setRoot(mRoot);
                try
                {
                    String module = prefix + f.getName();
                    System.out.println(f.toString());
                    IOLogic.load(rt, module);
                    checkExpressions(rt);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
    }
    
    private void checkExpressions(BasicRuntime rt)
    {
        for (SyntaxBean command : rt.getProgram().getSyntax())
        {
            if (command.getArg1() instanceof ExpressionBean)
                checkExpression(rt, (ExpressionBean)command.getArg1());
            else if (command.getArg1() instanceof VariableBean)
                checkExpression(rt, (VariableBean)command.getArg1());
        }
    }
    
    private void checkExpression(BasicRuntime rt, VariableBean var)
    {
        for (ExpressionBean expr : var.getIndicies())
            checkExpression(rt, expr);
    }
    
    private void checkExpression(BasicRuntime rt, ExpressionBean expr)
    {
        List<TokenBean> toks = expr.tokens(rt.getProgram());
        ExprUtils.preProcess(toks);
        try
        {
            ParseNode root = ParseUtils.parse(toks);
        }
        catch (ExpressionEvaluationException e)
        {
            e.printStackTrace();
        }
    }

    private void parseArgs()
    {
        for (int i = 0; i < mArgs.length; i++)
        {
            if (mRoot == null)
            {
                File f = new File(mArgs[i]);
                if (f.isDirectory())
                    mRoot = f;
            }
        }
        if (mRoot == null)
            mRoot = new File(".");
    }

    public static void main(String[] args)
    {
        Test app = new Test(args);
        app.run();
    }

}
