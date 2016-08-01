package jo.basic.logic.expr;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jo.basic.data.TokenBean;


public class ParseUtils
{
    public static ParseNode parse(List<TokenBean> toks) throws ExpressionEvaluationException
    {
		//OutputLogic.println("Eval("+start+","+end+")");
		ParseNode ret = parseExpr(toks);
		if (ret == null)
		    return null;
		if (toks.size() == 0)
		    return ret;
		ParseNode block = new ParseNode();
		block.setType(ParseNode.CODE_BLOCK);
		block.getChildren().add(ret);
		while (toks.size() > 0)
		{
	        ret = parseExpr(toks);
	        if (ret != null)
	            block.getChildren().add(ret);
		}
		return block;
	}

    // Adapted from http://www.geeksforgeeks.org/expression-evaluation/
    public static ParseNode parseExpr(List<TokenBean> toks) throws ExpressionEvaluationException
    {
        Stack<ParseNode> values = new Stack<>();
        Stack<TokenBean> ops = new Stack<>();
        // 1. While there are still tokens to be read in,
        while (toks.size() > 0)
        {
            // 1.1 Get the next token.
            TokenBean tok = toks.get(0);
            toks.remove(0);
            //  1.2 If the token is:
            // 1.2.1 A number: push it onto the value stack.
            if ((tok.getType() == TokenBean.NUMBER) || (tok.getType() == TokenBean.HEXNUMBER) || (tok.getType() == TokenBean.STRING))
            {
                ParseNode node = parseValue(tok);
                values.push(node);
            }
            // 1.2.2 A variable: get its value, and push onto the value stack.
            else if (tok.getType() == TokenBean.VARIABLE)
            {
                if ((toks.size() > 0) && (toks.get(0).getType() == TokenBean.LPAREN))
                {   // function
                    ParseNode node = parseFunction(tok, toks);
                    values.push(node);
                }
                else
                {   // lookup
                    ParseNode node = parseValue(tok);
                    values.push(node);
                }
            }
            //  1.2.3 A left parenthesis: push it onto the operator stack
            else if (tok.getType() == TokenBean.LPAREN)
                ops.push(tok);
            // 1.2.4 A right parenthesis:
            else if (tok.getType() == TokenBean.RPAREN)
            {
                // 1 While the thing on top of the operator stack is not a left parenthesis,
                while (ops.peek().getType() != TokenBean.LPAREN)
                {
                    // 1 Pop the operator from the operator stack.
                    TokenBean op = ops.pop();
                    // 2 Pop the value stack twice, getting two operands.
                    ParseNode v2 = values.pop();
                    ParseNode v1 = values.pop();
                    // 3 Apply the operator to the operands, in the correct order.
                    ParseNode node = parseOperation(op, v1, v2);
                    // 4 Push the result onto the value stack.
                    values.push(node);
                }
                // 2 Pop the left parenthesis from the operator stack, and discard it
                ops.pop();
            }
            // 1.2.5 An operator (call it thisOp):
            else if (isOperator(tok.getType()))
            {
                // 1 While the operator stack is not empty, and the top thing on the
                // operator stack has the same or greater precedence as thisOp,
                while ((ops.size() > 0) && (getPrecedence(ops.peek().getTokenText()) >= getPrecedence(tok.getTokenText())))
                {
                    // 1 Pop the operator from the operator stack.
                    TokenBean op = ops.pop();
                    // 2 Pop the value stack twice, getting two operands.
                    ParseNode v2 = values.pop();
                    ParseNode v1 = values.pop();
                    // 3 Apply the operator to the operands, in the correct order.
                    ParseNode node = parseOperation(op, v1, v2);
                    // 4 Push the result onto the value stack.
                    values.push(node);
                }
                // 2 Push thisOp onto the operator stack.
                ops.push(tok);
            }
        }
        // 2. While the operator stack is not empty,
        while (ops.size() > 0)
        {
            // 1 Pop the operator from the operator stack.
            TokenBean op = ops.pop();
            // 2 Pop the value stack twice, getting two operands.
            ParseNode v2 = values.pop();
            ParseNode v1 = values.pop();
            // 3 Apply the operator to the operands, in the correct order.
            ParseNode node = parseOperation(op, v1, v2);
            // 4 Push the result onto the value stack.
            values.push(node);
        }
        if (values.size() == 0)
            return null;
        if (values.size() > 1)
            throw new ExpressionEvaluationException("Extra stuff on stack after parsing, "+values);
        return values.pop();
    }
    
    private static boolean isOperator(int type)
    {
        switch (type)
        {
            case TokenBean.ADD:
            case TokenBean.SUBTRACT:
            case TokenBean.MULTIPLY:
            case TokenBean.DIVIDE:
            case TokenBean.EQUAL:
            case TokenBean.LESSTHAN:
            case TokenBean.GREATERTHAN:
            case TokenBean.EXPONENT:
            case TokenBean.OR:
            case TokenBean.AND:
            case TokenBean.NOT_EQUAL:
            case TokenBean.GREATERTHAN_EQUAL:
            case TokenBean.LESSTHAN_EQUAL:
                return true;
        }
        return false;
    }

    private static int getPrecedence(String token)
    {
        if ("(".equals(token) || ")".equals(token))
            return 0;
        if (!ExprUtils.mOperatorMap.containsKey(token))
            System.err.println("No such operator '"+token+"'");
        int p = ExprUtils.mOperatorMap.get(token).getPrecedence();
        return p;
    }

    private static ParseNode parseOperation(TokenBean op, ParseNode v1,
            ParseNode v2)
    {
        ParseNode node = new ParseNode();
        node.setType(ParseNode.OPERATION);
        node.setToken(op);
        node.getChildren().add(v1);
        node.getChildren().add(v2);
        return node;
    }

    private static ParseNode parseFunction(TokenBean tok, List<TokenBean> toks)
            throws ExpressionEvaluationException
    {
        toks.remove(0);
        int end = findToken(toks, TokenBean.RPAREN);
        if (end < 0)
            throw new ExpressionEvaluationException("Unclosed function call.");
        ParseNode node = new ParseNode();
        node.setType(ParseNode.FUNCTION);
        node.setToken(tok);
        List<TokenBean> argList = sublist(toks, 0, end-1);
        remove(toks, 0, end + 1);
        while (argList.size() > 0)
        {
            int comma = findToken(argList, TokenBean.COMMA);
            if (comma < 0)
            {
                node.getChildren().add(parse(argList));
                break;
            }
            else
            {
                List<TokenBean> arg = sublist(argList, 0, comma-1);
                remove(argList, 0, comma + 1);
                node.getChildren().add(parse(arg));
            }
        }
        return node;
    }

    private static ParseNode parseValue(TokenBean tok)
    {
        ParseNode node = new ParseNode();
        node.setType(ParseNode.VALUE);
        node.setToken(tok);
        return node;
    }
	
	private static void remove(List<TokenBean> toks, int start, int end)
    {
        for (int i = end - 1; i >= start; i--)
            toks.remove(i);
    }

    private static List<TokenBean> sublist(List<TokenBean> toks, int start, int end)
    {
        List<TokenBean> sub = new ArrayList<>();
        for (int i = start; i <= end; i++)
            sub.add(toks.get(i));
        return sub;
    }
	
	private static int findToken(List<TokenBean> toks, int type)
	{
		int stack = 0;
		for (int i = 0; i < toks.size(); i++)
		{
			TokenBean tok = (TokenBean)toks.get(i);
			if (tok.getType() == TokenBean.LPAREN)
				stack++;
			else if ((tok.getType() == TokenBean.RPAREN) && (stack > 0))
				stack--;
			else if ((tok.getType() == type) && (stack == 0))
				return i;
		}
		return -1;
	}

    public static void print(String prefix, ParseNode node)
    {
        System.out.println(toString(prefix, node));
    }

    public static String toString(String prefix, ParseNode node)
    {
        if (node == null)
            return prefix="<null>";
        StringBuffer sb = new StringBuffer();
        switch (node.getType())
        {
            case ParseNode.CODE_BLOCK:
                for (ParseNode child : node.getChildren())
                    sb.append(toString(prefix+"  ", child));
                sb.append(prefix+";\n");
                break;
            case ParseNode.FUNCTION:
                sb.append(prefix+node.getToken()+"(\n");
                for (ParseNode child : node.getChildren())
                    sb.append(toString(prefix+"  ", child));
                sb.append(prefix+")\n");
                break;
            case ParseNode.METHOD:
                sb.append(prefix+"."+node.getToken()+"(\n");
                for (ParseNode child : node.getChildren())
                    sb.append(toString(prefix+"  ", child));
                sb.append(prefix+")\n");
                break;
            case ParseNode.OPERATION:
                sb.append(prefix+node.getToken()+"\n");
                for (ParseNode child : node.getChildren())
                    sb.append(toString(prefix+"  ", child));
                break;
            case ParseNode.VALUE:
                sb.append(prefix+node.getToken()+"\n");
                break;
            case ParseNode.IF:
                sb.append(prefix+"IF (\n");
                sb.append(toString(prefix+"  ", node.getChildren().get(0)));
                sb.append(prefix+")\n");
                sb.append(toString(prefix+"  ", node.getChildren().get(1)));
                if (node.getChildren().size() > 2)
                {
                    sb.append(prefix+"ELSE");
                    sb.append(toString(prefix+"  ", node.getChildren().get(2)));
                }
                break;
        }
        return sb.toString();
    }
}

