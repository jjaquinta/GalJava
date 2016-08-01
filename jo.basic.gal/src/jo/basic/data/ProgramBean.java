package jo.basic.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramBean
{
    private File mSource;
    private List<LineBean>  mLines = new ArrayList<LineBean>();
    private List<TokenBean> mTokens = new ArrayList<>();
    private List<SyntaxBean> mSyntax = new ArrayList<>();
    private Map<String, Integer> mLabels = new HashMap<String, Integer>();
    private List<ExpressionBean> mData = new ArrayList<>();
    private Map<String, FunctionBean> mFunctions = new HashMap<String, FunctionBean>();

    public List<LineBean> getLines()
    {
        return mLines;
    }

    public void setLines(List<LineBean> lines)
    {
        mLines = lines;
    }

    public List<TokenBean> getTokens()
    {
        return mTokens;
    }

    public void setTokens(List<TokenBean> tokens)
    {
        mTokens = tokens;
    }

    public List<SyntaxBean> getSyntax()
    {
        return mSyntax;
    }

    public void setSyntax(List<SyntaxBean> syntax)
    {
        mSyntax = syntax;
    }

    public Map<String, Integer> getLabels()
    {
        return mLabels;
    }

    public void setLabels(Map<String, Integer> labels)
    {
        mLabels = labels;
    }

    public List<ExpressionBean> getData()
    {
        return mData;
    }

    public void setData(List<ExpressionBean> data)
    {
        mData = data;
    }

    public Map<String, FunctionBean> getFunctions()
    {
        return mFunctions;
    }

    public void setFunctions(Map<String, FunctionBean> functions)
    {
        mFunctions = functions;
    }

    public File getSource()
    {
        return mSource;
    }

    public void setSource(File source)
    {
        mSource = source;
    }
}
