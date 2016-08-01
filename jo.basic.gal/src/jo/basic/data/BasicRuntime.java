package jo.basic.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jo.basic.logic.ui.ScreenManager;

public class BasicRuntime
{
    private File        mRoot;
    private ProgramBean mProgram;
    private String      mArgs;
    private int         mExecutionPoint;
    private int         mDataPointer;
    private Map<String,Object>  mVariables = new HashMap<>();
    private Integer     mOnErrorLabel;
    private Random      mRND = new Random();
    private Object[]    mStreams = new Object[8];
    private File[]      mStreamFiles = new File[8];
    private ScreenManager mScreen;
    
    public File getRoot()
    {
        return mRoot;
    }
    public void setRoot(File root)
    {
        mRoot = root;
    }
    public ProgramBean getProgram()
    {
        return mProgram;
    }
    public void setProgram(ProgramBean program)
    {
        mProgram = program;
    }
    public int getExecutionPoint()
    {
        return mExecutionPoint;
    }
    public void setExecutionPoint(int executionPoint)
    {
        mExecutionPoint = executionPoint;
    }
    public Map<String, Object> getVariables()
    {
        return mVariables;
    }
    public void setVariables(Map<String, Object> variables)
    {
        mVariables = variables;
    }
    public Integer getOnErrorLabel()
    {
        return mOnErrorLabel;
    }
    public void setOnErrorLabel(Integer onErrorLabel)
    {
        mOnErrorLabel = onErrorLabel;
    }
    public Random getRND()
    {
        return mRND;
    }
    public void setRND(Random rND)
    {
        mRND = rND;
    }
    public int getDataPointer()
    {
        return mDataPointer;
    }
    public void setDataPointer(int dataPointer)
    {
        mDataPointer = dataPointer;
    }
    public Object[] getStreams()
    {
        return mStreams;
    }
    public void setStreams(Object[] streams)
    {
        mStreams = streams;
    }
    public ScreenManager getScreen()
    {
        return mScreen;
    }
    public void setScreen(ScreenManager screen)
    {
        mScreen = screen;
    }
    public String getArgs()
    {
        return mArgs;
    }
    public void setArgs(String args)
    {
        mArgs = args;
    }
    public File[] getStreamFiles()
    {
        return mStreamFiles;
    }
    public void setStreamFiles(File[] streamFiles)
    {
        mStreamFiles = streamFiles;
    }
}
