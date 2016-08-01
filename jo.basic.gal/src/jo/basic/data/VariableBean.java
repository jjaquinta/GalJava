package jo.basic.data;

import java.util.ArrayList;
import java.util.List;

public class VariableBean
{
    private String  mName;
    private List<ExpressionBean> mIndicies = new ArrayList<>();
    
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }
    public List<ExpressionBean> getIndicies()
    {
        return mIndicies;
    }
    public void setIndicies(List<ExpressionBean> indicies)
    {
        mIndicies = indicies;
    }
}
