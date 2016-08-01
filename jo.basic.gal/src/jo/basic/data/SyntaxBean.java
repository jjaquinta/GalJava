package jo.basic.data;

public class SyntaxBean
{
    public static final int END_PROGRAM = 1000; 
    public static final int END_IF = 1001; 
    public static final int END_DEF = 1002; 
    public static final int END_SELECT = 1003; 
    public static final int RETURN = 1004; 
    public static final int GOTO = 1005; 
    public static final int ONERRORGOTO = 1006; 
    public static final int CLS = 1007; 
    public static final int GOSUB = 1008; 
    public static final int COLOR = 1009; 
    public static final int SCREEN = 1010; 
    public static final int SOUND = 1011; 
    public static final int PALETTE = 1012; 
    public static final int LOCATE = 1013; 
    public static final int FOR = 1014; 
    public static final int RANDOMIZE = 1015; 
    public static final int NEXT = 1016; 
    public static final int SHELL = 1017; 
    public static final int PRINT = 1018; 
    public static final int CLOSE = 1019; 
    public static final int SELECT = 1020; 
    public static final int CASE = 1021; 
    public static final int DIM = 1022; 
    public static final int READ = 1023; 
    public static final int DO_UNTIL = 1024; 
    public static final int LOOP = 1025; 
    public static final int LINE_INPUT = 1026; 
    public static final int DRAW_LINE = 1027; 
    public static final int OPEN = 1028; 
    public static final int IF_GOTO = 1029; 
    public static final int IF_GOSUB = 1030; 
    public static final int IF_LET = 1031; 
    public static final int IF_BLOCK = 1032; 
    public static final int IF_COMMAND = 1033; 
    public static final int SWAP = 1034; 
    public static final int ELSEIF_BLOCK = 1035; 
    public static final int ELSE = 1036; 
    public static final int PSET = 1037; 
    public static final int CIRCLE = 1038; 
    public static final int PAINT = 1039; 
    public static final int GET_IMAGE = 1040; 
    public static final int PUT_IMAGE = 1041; 
    public static final int DEF = 1042; 
    public static final int LET = 1043; 
    public static final int DEBUG = 1044; 
    public static final int IF_RETURN = 1045; 

    private int mType;
    private int mFirstToken;
    private int mLastToken;
    private Object mArg1;
    private Object mArg2;
    private Object mArg3;
    private Object mArg4;
    
    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
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

    public Object getArg1()
    {
        return mArg1;
    }

    public void setArg1(Object arg1)
    {
        mArg1 = arg1;
    }

    public Object getArg2()
    {
        return mArg2;
    }

    public void setArg2(Object arg2)
    {
        mArg2 = arg2;
    }

    public Object getArg3()
    {
        return mArg3;
    }

    public void setArg3(Object arg3)
    {
        mArg3 = arg3;
    }

    public Object getArg4()
    {
        return mArg4;
    }

    public void setArg4(Object arg4)
    {
        mArg4 = arg4;
    }
    
}
