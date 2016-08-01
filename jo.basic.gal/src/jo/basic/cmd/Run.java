package jo.basic.cmd;

import java.io.File;
import java.io.IOException;

import jo.basic.data.BasicRuntime;
import jo.basic.logic.IOLogic;
import jo.basic.logic.RuntimeLogic;
import jo.basic.logic.ui.ScreenManager;

public class Run
{
    private String[] mArgs;
    private File     mRoot;
    private String   mModule;
    
    public Run(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        parseArgs();
        BasicRuntime rt = new BasicRuntime();
        rt.setRoot(mRoot);
        rt.setScreen(new ScreenManager());
        try
        {
            IOLogic.load(rt, mModule);
            RuntimeLogic.execute(rt);
            System.exit(0);
        }
        catch (IOException e)
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
                else
                {
                    mRoot = f.getParentFile();
                    mModule = f.getName();
                }
            }
            else
                mModule = mArgs[i];
        }
        if (mRoot == null)
            mRoot = new File(".");
        if (mModule == null)
            for (File f : mRoot.listFiles())
                if (f.getName().toUpperCase().endsWith(".BAS"))
                {
                    mModule = f.getName();
                    break;
                }
    }

    public static void main(String[] args)
    {
        Run app = new Run(args);
        app.run();
    }

}
