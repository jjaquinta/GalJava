package jo.basic.data;

import java.util.HashMap;
import java.util.Map;

public class TokenBean
{
    public static final int UNKNOWN = 0;
    public static final int END_OF_COMMAND = 1;
    public static final int VARIABLE = 2;
    public static final int NUMBER = 3;
    public static final int STRING = 4;
    public static final int HEXNUMBER = 5;
    // keywords
    public static final int DATA = 1002;
    public static final int READ = 1003;
    public static final int IF = 1004;
    public static final int THEN = 1005;
    public static final int ELSE = 1006;
    public static final int FOR = 1007; 
    public static final int TO = 1008;
    public static final int STEP = 1009; 
    public static final int NEXT = 1010;
    public static final int DO = 1013; 
    public static final int LOOP = 1014;
    public static final int GOTO = 1017;
    public static final int UNTIL = 1016;
    public static final int GOSUB = 1018;
    public static final int ON = 1019;
    public static final int DEF = 1020;
    public static final int REM = 1022;
    public static final int CASE = 1023;
    public static final int END = 1024; 
    public static final int SELECT = 1025;
    public static final int RETURN = 1026;
    public static final int ERROR = 1027;
    public static final int CLS = 1028;
    public static final int COLOR = 1029;
    //public static final int ALL = 1030;
    public static final int SCREEN = 1031;
    public static final int SOUND = 1032;
    public static final int PALETTE = 1033;
    public static final int RANDOMIZE = 1034;
    public static final int SHELL = 1035;
    public static final int PRINT = 1036;
    public static final int CLOSE = 1037;
    public static final int IS = 1038;
    public static final int AS = 1039;
    public static final int INTEGER = 1040;
    public static final int DIM = 1041;
    public static final int LINE = 1042;
    public static final int INPUT = 1043;
    public static final int OPEN = 1044;
    public static final int SWAP = 1045;
    public static final int ELSEIF = 1046;
    public static final int PSET = 1047;
    public static final int CIRCLE = 1048;
    public static final int PAINT = 1049;
    public static final int GET = 1050;
    public static final int PUT = 1051;
    public static final int DEBUG = 1052;
    public static final int DECLARE = 1053;
    public static final int SUB = 1054;
    public static final int SHARED = 1055;
    public static final int OUTPUT = 1056;
    public static final int FUNCTION = 1057;
    public static final int EXIT = 1058;
    /*
    public static final int WEND = 1011; 
    public static final int REPEAT = 1012; 
    public static final int WHILE = 1015;
    public static final int FN = 1021;
    */
    // operators
    public static final int LPAREN = 2001;
    public static final int RPAREN = 2002;
    public static final int ADD = 2003;
    public static final int SUBTRACT = 2004;
    public static final int MULTIPLY = 2005;
    public static final int DIVIDE = 2006;
    public static final int EQUAL = 2007;
    public static final int LESSTHAN = 2008;
    public static final int GREATERTHAN = 2009;
    public static final int COMMA = 2010;
    public static final int SEMICOLON = 2011;
    public static final int HASH = 2012;
    public static final int EXPONENT = 2013;
    public static final int LOCATE = 2014;
    public static final int OR = 2015;
    public static final int AND = 2016;
    public static final int MOD = 2017;
    // synthesized operators
    public static final int NOT_EQUAL = 3001;
    public static final int GREATERTHAN_EQUAL = 3002;
    public static final int LESSTHAN_EQUAL = 3003;

    // keyword table
    public static final Map<String, Integer> KEYWORDS = new HashMap<>();
    static
    {
        KEYWORDS.put("REM", TokenBean.REM);
        KEYWORDS.put("CASE", TokenBean.CASE);
        KEYWORDS.put("END", TokenBean.END);
        KEYWORDS.put("IF", TokenBean.IF);
        KEYWORDS.put("DEF", TokenBean.DEF);
        KEYWORDS.put("SELECT", TokenBean.SELECT);
        KEYWORDS.put("RETURN", TokenBean.RETURN);
        KEYWORDS.put("GOTO", TokenBean.GOTO);
        KEYWORDS.put("ON", TokenBean.ON);
        KEYWORDS.put("ERROR", TokenBean.ERROR);
        KEYWORDS.put("CLS", TokenBean.CLS);
        KEYWORDS.put("GOSUB", TokenBean.GOSUB);
        KEYWORDS.put("COLOR", TokenBean.COLOR);
        //KEYWORDS.put("ALL", TokenBean.ALL);
        KEYWORDS.put("SCREEN", TokenBean.SCREEN);
        KEYWORDS.put("SOUND", TokenBean.SOUND);
        KEYWORDS.put("DATA", TokenBean.DATA);
        KEYWORDS.put("PALETTE", TokenBean.PALETTE);
        KEYWORDS.put("LOCATE", TokenBean.LOCATE);
        KEYWORDS.put("FOR", TokenBean.FOR); 
        KEYWORDS.put("TO", TokenBean.TO);
        KEYWORDS.put("STEP", TokenBean.STEP); 
        KEYWORDS.put("RANDOMIZE", TokenBean.RANDOMIZE); 
        KEYWORDS.put("NEXT", TokenBean.NEXT); 
        KEYWORDS.put("SHELL", TokenBean.SHELL); 
        KEYWORDS.put("PRINT", TokenBean.PRINT); 
        KEYWORDS.put("CLOSE", TokenBean.CLOSE); 
        KEYWORDS.put("ELSE", TokenBean.ELSE);
        KEYWORDS.put("IS", TokenBean.IS);
        KEYWORDS.put("AS", TokenBean.AS);
        KEYWORDS.put("INTEGER", TokenBean.INTEGER);
        KEYWORDS.put("DIM", TokenBean.DIM);
        KEYWORDS.put("READ", TokenBean.READ);
        KEYWORDS.put("DO", TokenBean.DO); 
        KEYWORDS.put("LOOP", TokenBean.LOOP);
        KEYWORDS.put("UNTIL", TokenBean.UNTIL);
        KEYWORDS.put("LINE", TokenBean.LINE);
        KEYWORDS.put("INPUT", TokenBean.INPUT);
        KEYWORDS.put("OPEN", TokenBean.OPEN);
        KEYWORDS.put("THEN", TokenBean.THEN);
        KEYWORDS.put("SWAP", TokenBean.SWAP);
        KEYWORDS.put("ELSEIF", TokenBean.ELSEIF);
        KEYWORDS.put("PSET", TokenBean.PSET);
        KEYWORDS.put("CIRCLE", TokenBean.CIRCLE);
        KEYWORDS.put("PAINT", TokenBean.PAINT);
        KEYWORDS.put("GET", TokenBean.GET);
        KEYWORDS.put("PUT", TokenBean.PUT);
        KEYWORDS.put("OR", TokenBean.OR);
        KEYWORDS.put("AND", TokenBean.AND);
        KEYWORDS.put("MOD", TokenBean.MOD);
        KEYWORDS.put("<>", TokenBean.NOT_EQUAL);
        KEYWORDS.put(">=", TokenBean.GREATERTHAN_EQUAL);
        KEYWORDS.put("<=", TokenBean.LESSTHAN_EQUAL);
        KEYWORDS.put("DECLARE", TokenBean.DECLARE);
        KEYWORDS.put("SUB", TokenBean.SUB);
        KEYWORDS.put("SHARED", TokenBean.SHARED);
        KEYWORDS.put("OUTPUT", TokenBean.OUTPUT);
        KEYWORDS.put("FUNCTION", TokenBean.FUNCTION);
        KEYWORDS.put("EXIT", TokenBean.EXIT);
        KEYWORDS.put("DEBUG", TokenBean.DEBUG);
        /*
        KEYWORDS.put("NEXT", TokenBean.NEXT);
        KEYWORDS.put("WEND", TokenBean.WEND); 
        KEYWORDS.put("REPEAT", TokenBean.REPEAT); 
        KEYWORDS.put("WHILE", TokenBean.WHILE);
        KEYWORDS.put("FN", TokenBean.FN);
        */
    }

    // operator table
    public static final Map<String, Integer> OPERATORS = new HashMap<>();
    static
    {
        OPERATORS.put("(", TokenBean.LPAREN);
        OPERATORS.put(")", TokenBean.RPAREN);
        OPERATORS.put("+", TokenBean.ADD);
        OPERATORS.put("-", TokenBean.SUBTRACT);
        OPERATORS.put("*", TokenBean.MULTIPLY);
        OPERATORS.put("/", TokenBean.DIVIDE);
        OPERATORS.put("=", TokenBean.EQUAL);
        OPERATORS.put("<", TokenBean.LESSTHAN);
        OPERATORS.put(">", TokenBean.GREATERTHAN);
        OPERATORS.put(",", TokenBean.COMMA);
        OPERATORS.put(";", TokenBean.SEMICOLON);
        OPERATORS.put("#", TokenBean.HASH);
        OPERATORS.put("^", TokenBean.EXPONENT);
    }
    
    private int         mType;
    private LineBean    mLine;
    private int         mCharStart;
    private int         mCharEnd;

    // utilities
    @Override
    public String toString()
    {
        return getTokenText();
    }
    
    public String getTokenText()
    {
        return mLine.getText().substring(mCharStart, mCharEnd);
    }
    
    // getters and setters
    
    public int getType()
    {
        return mType;
    }
    public void setType(int type)
    {
        mType = type;
    }
    public LineBean getLine()
    {
        return mLine;
    }
    public void setLine(LineBean line)
    {
        mLine = line;
    }
    public int getCharStart()
    {
        return mCharStart;
    }
    public void setCharStart(int charStart)
    {
        mCharStart = charStart;
    }
    public int getCharEnd()
    {
        return mCharEnd;
    }
    public void setCharEnd(int charEnd)
    {
        mCharEnd = charEnd;
    }
}
