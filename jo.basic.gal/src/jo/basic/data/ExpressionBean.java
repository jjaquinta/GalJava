package jo.basic.data;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBean
{
    private int mFirstToken;
    private int mLastToken;
    
    public List<TokenBean> tokens(ProgramBean program)
    {
        List<TokenBean> t = new ArrayList<>();
        for (int i = mFirstToken; i < mLastToken; i++)
            t.add(program.getTokens().get(i));
        return t;
    }
    
    public int getFirstToken()
    {
        return mFirstToken;
    }
    public void setFirstToken(int firstToken)
    {
        mFirstToken = firstToken;
    }
    public int getLastToken()
    {
        return mLastToken;
    }
    public void setLastToken(int lastToken)
    {
        mLastToken = lastToken;
    }
}
