package jo.basic.logic.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.basic.data.TokenBean;
import jo.basic.logic.expr.func.ExprFunctionArray;
import jo.basic.logic.expr.func.ExprFunctionAsc;
import jo.basic.logic.expr.func.ExprFunctionChr;
import jo.basic.logic.expr.func.ExprFunctionDefFn;
import jo.basic.logic.expr.func.ExprFunctionEOF;
import jo.basic.logic.expr.func.ExprFunctionInStr;
import jo.basic.logic.expr.func.ExprFunctionInt;
import jo.basic.logic.expr.func.ExprFunctionLCase;
import jo.basic.logic.expr.func.ExprFunctionLOF;
import jo.basic.logic.expr.func.ExprFunctionLTrim;
import jo.basic.logic.expr.func.ExprFunctionLeft;
import jo.basic.logic.expr.func.ExprFunctionLen;
import jo.basic.logic.expr.func.ExprFunctionMid;
import jo.basic.logic.expr.func.ExprFunctionRTrim;
import jo.basic.logic.expr.func.ExprFunctionRight;
import jo.basic.logic.expr.func.ExprFunctionSpace;
import jo.basic.logic.expr.func.ExprFunctionString;
import jo.basic.logic.expr.func.ExprFunctionVal;
import jo.basic.logic.expr.ops.ExprOpAdd;
import jo.basic.logic.expr.ops.ExprOpAnd;
import jo.basic.logic.expr.ops.ExprOpDivide;
import jo.basic.logic.expr.ops.ExprOpEquals;
import jo.basic.logic.expr.ops.ExprOpExponent;
import jo.basic.logic.expr.ops.ExprOpGreaterThan;
import jo.basic.logic.expr.ops.ExprOpGreaterThanOrEqual;
import jo.basic.logic.expr.ops.ExprOpLessThan;
import jo.basic.logic.expr.ops.ExprOpLessThanOrEqual;
import jo.basic.logic.expr.ops.ExprOpMultiply;
import jo.basic.logic.expr.ops.ExprOpNotEquals;
import jo.basic.logic.expr.ops.ExprOpOr;
import jo.basic.logic.expr.ops.ExprOpSubtract;
import jo.util.utils.obj.BooleanUtils;


public class ExprUtils
{
    public static final List<ExprFunction> mFunctions = new ArrayList<>();
    static
    {
        addFunction(new ExprFunctionInt());
        addFunction(new ExprFunctionRight());
        addFunction(new ExprFunctionLen());
        addFunction(new ExprFunctionEOF());
        addFunction(new ExprFunctionRTrim());
        addFunction(new ExprFunctionLeft());
        addFunction(new ExprFunctionArray());
        addFunction(new ExprFunctionMid());
        addFunction(new ExprFunctionVal());
        addFunction(new ExprFunctionChr());
        addFunction(new ExprFunctionAsc());
        addFunction(new ExprFunctionInStr());
        addFunction(new ExprFunctionLCase());
        addFunction(new ExprFunctionLOF());
        addFunction(new ExprFunctionLTrim());
        addFunction(new ExprFunctionDefFn());
        addFunction(new ExprFunctionString());
        addFunction(new ExprFunctionSpace());
    }
    
    public static void addFunction(ExprFunction func)
    {
        mFunctions.add(func);
    }
    
    public static final List<ExprOperator> mOperators = new ArrayList<>();
    public static final Map<String, ExprOperator> mOperatorMap = new HashMap<>();
    public static final List<String> mOperatorList = new ArrayList<>();
    static
    {
        addOperator(new ExprOpAdd());
        addOperator(new ExprOpDivide());
        addOperator(new ExprOpEquals());
        addOperator(new ExprOpGreaterThan());
        addOperator(new ExprOpGreaterThanOrEqual());
        addOperator(new ExprOpLessThan());
        addOperator(new ExprOpLessThanOrEqual());
        addOperator(new ExprOpMultiply());
        addOperator(new ExprOpSubtract());
        addOperator(new ExprOpAnd());
        addOperator(new ExprOpOr());
        addOperator(new ExprOpNotEquals());
        addOperator(new ExprOpExponent());
    }
    
    public static void addOperator(ExprOperator op)
    {
        mOperators.add(op);
        for (String opName : op.getAliases())
            addOperator(opName, op);
    }
    
    private static void addOperator(String opName, ExprOperator op)
    {
        mOperatorMap.put(opName, op);
        for (int i = 0; i < mOperatorList.size(); i++)
            if (mOperatorList.get(i).length() <= opName.length())
            {
                mOperatorList.add(i, opName);
                return;
            }
        mOperatorList.add(opName);
    }

    public static Object evalObject(List<TokenBean> toks, IExprProps props) throws ExpressionEvaluationException
    {
        ExprUtils.preProcess(toks);
        ParseNode root = ParseUtils.parse(toks);
        Object ret = eval(root, props, true);
        //DebugUtils.info(expr + " = "+ret);
        return ret;
    }
    
    private static Object eval(ParseNode node, IExprProps props, boolean rvalue) throws ExpressionEvaluationException
    {
        Object ret = null;
        switch (node.getType())
        {
            case ParseNode.CODE_BLOCK:
                for (int i = 0; i < node.getChildren().size(); i++)
                    ret = eval(node.getChildren().get(i), props, true);
                break;
            case ParseNode.FUNCTION:
                boolean didIt = false;
                for (ExprFunction func : mFunctions)
                    if (func.isFunction(node.getToken().getTokenText(), node.getChildren().size()))
                    {
                        Object[] args = new Object[node.getChildren().size()];
                        for (int i = 0; i < node.getChildren().size(); i++)
                            args[i] = eval(node.getChildren().get(i), props, true);
                        ret = func.eval(node.getToken().getTokenText(), args);
                        didIt = true;
                        break;
                    }
                if (!didIt)
                    throw new ExpressionEvaluationException("Unknown function '"+node.getToken().getTokenText()+"'");
                break;
            case ParseNode.METHOD:
                Object[] args = new Object[node.getChildren().size() + 1];
                args[0] = node.getToken().getTokenText();
                for (int i = 0; i < node.getChildren().size(); i++)
                    args[i+1] = eval(node.getChildren().get(i), props, true);
                ret = args;
                break;
            case ParseNode.OPERATION:
                ExprOperator op = mOperatorMap.get(node.getToken().getTokenText());
                Object arg1 = eval(node.getChildren().get(0), props, op.getArg1Type());
                Object arg2 = eval(node.getChildren().get(1), props, op.getArg2Type());
                ret = op.eval(arg1, arg2, props);
                break;
            case ParseNode.VALUE:
                if (node.getToken().getType() == TokenBean.STRING)
                    ret = node.getToken().getTokenText();
                else if (node.getToken().getType() == TokenBean.NUMBER)
                    ret = Double.parseDouble(node.getToken().getTokenText());
                else if (node.getToken().getType() == TokenBean.HEXNUMBER)
                    ret = Integer.parseInt(node.getToken().getTokenText(), 16);
                else if (node.getToken().getType() == TokenBean.VARIABLE)
                    if (rvalue)
                        ret = props.get(node.getToken().getTokenText());
                    else
                        ret = node.getToken().getTokenText();
                break;
            case ParseNode.IF:
                Object cond = eval(node.getChildren().get(0), props, true);
                if (BooleanUtils.parseBoolean(cond))
                    ret = eval(node.getChildren().get(1), props, true);
                else if (node.getChildren().size() > 2)
                    ret = eval(node.getChildren().get(2), props, true);
                break;
           default:
               throw new ExpressionEvaluationException("Unknown node type: "+node.getType());
        }
        return ret;
    }
    
    private static void preProcess(List<TokenBean> tok)
    {
//        System.out.println("Before:");
//        for (TokenBean t : tok)
//            System.out.print(t.toString()+"#"+t.getType()+" ");
//        System.out.println();
        for (int i = 0; i < tok.size() - 1; i++)
            if ((tok.get(i).getType() == TokenBean.LESSTHAN) && (tok.get(i+1).getType() == TokenBean.GREATERTHAN))
            {
                TokenBean ne = new TokenBean();
                ne.setType(TokenBean.NOT_EQUAL);
                ne.setLine(tok.get(i).getLine());
                ne.setCharStart(tok.get(i).getCharStart());
                ne.setCharEnd(tok.get(i+1).getCharEnd());
                tok.remove(i);
                tok.remove(i);
                tok.add(i, ne);
                i--;
            }
            else if ((tok.get(i).getType() == TokenBean.GREATERTHAN) && (tok.get(i+1).getType() == TokenBean.EQUAL))
            {
                TokenBean ne = new TokenBean();
                ne.setType(TokenBean.GREATERTHAN_EQUAL);
                ne.setLine(tok.get(i).getLine());
                ne.setCharStart(tok.get(i).getCharStart());
                ne.setCharEnd(tok.get(i+1).getCharEnd());
                tok.remove(i);
                tok.remove(i);
                tok.add(i, ne);
                i--;
            }
            else if ((tok.get(i).getType() == TokenBean.LESSTHAN) && (tok.get(i+1).getType() == TokenBean.EQUAL))
            {
                TokenBean ne = new TokenBean();
                ne.setType(TokenBean.LESSTHAN_EQUAL);
                ne.setLine(tok.get(i).getLine());
                ne.setCharStart(tok.get(i).getCharStart());
                ne.setCharEnd(tok.get(i+1).getCharEnd());
                tok.remove(i);
                tok.remove(i);
                tok.add(i, ne);
                i--;
            }
            else if ((tok.get(i).getType() == TokenBean.NOT_EQUAL) && (tok.get(i+1).getType() == TokenBean.SUBTRACT) && (tok.get(i+2).getType() == TokenBean.NUMBER))
            {
                TokenBean ne = new TokenBean();
                ne.setType(TokenBean.NUMBER);
                ne.setLine(tok.get(i).getLine());
                ne.setCharStart(tok.get(i+1).getCharStart());
                ne.setCharEnd(tok.get(i+2).getCharEnd());
                tok.remove(i+1);
                tok.remove(i+1);
                tok.add(i+1, ne);
            }
            else if ((tok.get(i).getType() == TokenBean.EQUAL) && (tok.get(i+1).getType() == TokenBean.SUBTRACT) && (tok.get(i+2).getType() == TokenBean.NUMBER))
            {
                TokenBean ne = new TokenBean();
                ne.setType(TokenBean.NUMBER);
                ne.setLine(tok.get(i).getLine());
                ne.setCharStart(tok.get(i+1).getCharStart());
                ne.setCharEnd(tok.get(i+2).getCharEnd());
                tok.remove(i+1);
                tok.remove(i+1);
                tok.add(i+1, ne);
            }
//        System.out.println("After:");
//        for (TokenBean t : tok)
//            System.out.print(t.toString()+"#"+t.getType()+" ");
//        System.out.println();
    }
}

