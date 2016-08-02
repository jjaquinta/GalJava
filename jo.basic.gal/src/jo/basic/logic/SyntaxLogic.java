package jo.basic.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jo.basic.data.BasicRuntime;
import jo.basic.data.ExpressionBean;
import jo.basic.data.FunctionBean;
import jo.basic.data.ProgramBean;
import jo.basic.data.SyntaxBean;
import jo.basic.data.TokenBean;
import jo.basic.data.VariableBean;

public class SyntaxLogic
{
    public static void syntax(BasicRuntime rt) throws IOException
    {
        ProgramBean program = rt.getProgram();
        TokenPointer tp = new TokenPointer(program);
        while (tp.moreTokens())
        {
            TokenBean tok = tp.token();
            switch (tok.getType())
            {
                case TokenBean.END_OF_COMMAND:
                    tp.inc();
                    break;
                case TokenBean.NUMBER:
                    program.getLabels().put(tok.getTokenText(), program.getSyntax().size());
                    tp.inc();
                    break;
                case TokenBean.ON:
                    parseOn(tp);
                    break;
                case TokenBean.LINE:
                    parseLine(tp);
                    break;
                case TokenBean.INPUT:
                    parseInput(tp);
                    break;
                case TokenBean.GOTO:
                    parseSingletonArg(tp, TokenBean.GOTO, TokenBean.NUMBER, SyntaxBean.GOTO);
                    break;
                case TokenBean.GOSUB:
                    parseSingletonArg(tp, TokenBean.GOSUB, TokenBean.NUMBER, SyntaxBean.GOSUB);
                    break;
                case TokenBean.READ:
                    parseRead(tp);
                    break;
                case TokenBean.CLOSE:
                    parseClose(tp);
                    break;
                case TokenBean.SCREEN:
                    parseSingletonArg(tp, TokenBean.SCREEN, TokenBean.NUMBER, SyntaxBean.SCREEN);
                    break;
                case TokenBean.RANDOMIZE:
                    parseSingletonArg(tp, TokenBean.RANDOMIZE, -1, SyntaxBean.RANDOMIZE);
                    break;
                case TokenBean.SHELL:
                    parseSingletonArg(tp, TokenBean.SHELL, -1, SyntaxBean.SHELL);
                    break;
                case TokenBean.DEBUG:
                    parseSingletonArg(tp, TokenBean.DEBUG, -1, SyntaxBean.DEBUG);
                    break;
                case TokenBean.NEXT:
                    parseSingletonArg(tp, TokenBean.NEXT, TokenBean.VARIABLE, SyntaxBean.NEXT);
                    break;
                case TokenBean.SOUND:
                    parseDoubleton(tp, TokenBean.SOUND, SyntaxBean.SOUND);
                    break;
                case TokenBean.LOCATE:
                    parseExprList(tp, SyntaxBean.LOCATE);
                    break;
                case TokenBean.OPEN:
                    parseOpen(tp);
                    break;
                case TokenBean.SWAP:
                    parseSwap(tp);
                    break;
                case TokenBean.PALETTE:
                    parseExprList(tp, SyntaxBean.PALETTE);
                    break;
                case TokenBean.COLOR:
                    parseColor(tp);
                    break;
                case TokenBean.IF:
                    parseIf(tp);
                    break;
                case TokenBean.ELSEIF:
                    parseElseIf(tp);
                    break;
                case TokenBean.PRINT:
                    parsePrint(tp);
                    break;
                case TokenBean.RETURN:
                    parseSingleton(tp, TokenBean.RETURN, SyntaxBean.RETURN);
                    break;
                case TokenBean.LOOP:
                    parseSingleton(tp, TokenBean.LOOP, SyntaxBean.LOOP);
                    break;
                case TokenBean.END:
                    parseEnd(tp);
                    break;
                case TokenBean.DEF:
                    parseDef(tp);
                    break;
                case TokenBean.DO:
                    parseDo(tp);
                    break;
                case TokenBean.DIM:
                    parseDim(tp);
                    break;
                case TokenBean.DATA:
                    parseData(tp);
                    break;
                case TokenBean.FOR:
                    parseFor(tp);
                    break;
                case TokenBean.SELECT:
                    parseSelect(tp);
                    break;
                case TokenBean.GET:
                    parseGet(tp);
                    break;
                case TokenBean.PUT:
                    parsePut(tp);
                    break;
                case TokenBean.CASE:
                    parseCase(tp);
                    break;
                case TokenBean.PSET:
                    parsePSet(tp);
                    break;
                case TokenBean.CIRCLE:
                    parseCircle(tp);
                    break;
                case TokenBean.PAINT:
                    parsePaint(tp);
                    break;
                case TokenBean.CLS:
                    parseSingleton(tp, TokenBean.CLS, SyntaxBean.CLS);
                    break;
                case TokenBean.ELSE:
                    parseElse(tp);
                    break;
                case TokenBean.VARIABLE:
                    parseLet(tp);
                    break;
                default:
                    System.err.println("Unexpected token ["+tok.getTokenText()+"|"+tok.getType()+"], "+tok.getLine().getText());
                    tp.printContext();
                    while (tp.moreTokens() && !tp.isType(TokenBean.END_OF_COMMAND))
                        tp.inc();
                    tp.inc();
                    break;
            }
        }
    }

    private static void parseElse(TokenPointer tp) throws IOException
    {
        if (tp.ifTokenMatch(TokenBean.ELSE, TokenBean.END_OF_COMMAND))
            parseSingleton(tp, TokenBean.ELSE, SyntaxBean.ELSE);
        else
        {
            tp.mark();
            tp.inc();
            tp.addSyntax(SyntaxBean.ELSE);
        }
    }

    private static void parseSwap(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        VariableBean var1 = tp.parseVariable();
        tp.assertType(TokenBean.COMMA);
        VariableBean var2 = tp.parseVariable();
        tp.addSyntax(SyntaxBean.SWAP, var1, var2);
    }

    private static void parseExprList(TokenPointer tp, int syntaxType) throws IOException
    {
        tp.mark();
        tp.inc();
        List<ExpressionBean> palette = new ArrayList<>();
        while (tp.moreTokens())
        {
            if (tp.isType(TokenBean.COMMA))
            {
                tp.inc();
                continue;
            }
            if (tp.isType(TokenBean.END_OF_COMMAND))
                break;
            palette.add(tp.parseExpression());
        }
        tp.addSyntax(syntaxType, palette);
    }

    private static void parseLet(TokenPointer tp) throws IOException
    {
        tp.mark();
        VariableBean arg1 = tp.parseVariable();
        tp.assertType(TokenBean.EQUAL);
        ExpressionBean arg2 = tp.parseExpression();
        tp.assertType(TokenBean.END_OF_COMMAND);
        tp.addSyntax(SyntaxBean.LET, arg1, arg2);
    }

    private static void parseRead(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        VariableBean arg1 = tp.parseVariable();
        tp.assertType(TokenBean.END_OF_COMMAND);
        tp.addSyntax(SyntaxBean.READ, arg1);
    }

    private static void parseIf(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        ExpressionBean arg1 = tp.parseExpression();
        if (tp.isType(TokenBean.GOTO))
        {
            tp.inc();
            String arg2 = tp.assertType(TokenBean.NUMBER).getTokenText();
            tp.addSyntax(SyntaxBean.IF_GOTO, arg1, arg2);
        }
        else if (tp.isType(TokenBean.GOSUB))
        {
            tp.inc();
            String arg2 = tp.assertType(TokenBean.NUMBER).getTokenText();
            tp.addSyntax(SyntaxBean.IF_GOSUB, arg1, arg2);
        }
        else if (tp.isType(TokenBean.THEN))
        {
            tp.inc();
            if (tp.isType(TokenBean.GOTO))
            {
                tp.inc();
                String arg2 = tp.assertType(TokenBean.NUMBER).getTokenText();
                tp.addSyntax(SyntaxBean.IF_GOTO, arg1, arg2);
            }
            else if (tp.isType(TokenBean.NUMBER))
            {
                String arg2 = tp.assertType(TokenBean.NUMBER).getTokenText();
                tp.addSyntax(SyntaxBean.IF_GOTO, arg1, arg2);
            }
            else if (tp.isType(TokenBean.GOSUB))
            {
                tp.inc();
                String arg2 = tp.assertType(TokenBean.NUMBER).getTokenText();
                tp.addSyntax(SyntaxBean.IF_GOSUB, arg1, arg2);
            }
            else if (tp.isType(TokenBean.RETURN))
            {
                tp.inc();
                tp.addSyntax(SyntaxBean.IF_RETURN, arg1);
            }
            else if (tp.isType(TokenBean.END_OF_COMMAND))
            {
                tp.addSyntax(SyntaxBean.IF_BLOCK, arg1);                
            }
            else if (tp.isStartOfExpression())
            {
                VariableBean arg2 = tp.parseVariable();
                tp.assertType(TokenBean.EQUAL);
                ExpressionBean arg3 = tp.parseExpression();
                tp.addSyntax(SyntaxBean.IF_LET, arg1, arg2, arg3);
            }
            else
                tp.addSyntax(SyntaxBean.IF_COMMAND, arg1);
        }
        else
            tp.syntaxError();
    }

    private static void parseElseIf(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        ExpressionBean arg1 = tp.parseExpression();
        if (tp.isType(TokenBean.THEN))
        {
            tp.inc();
            if (tp.isType(TokenBean.END_OF_COMMAND))
            {
                tp.addSyntax(SyntaxBean.ELSEIF_BLOCK, arg1);                
            }
            else
                tp.syntaxError();
        }
        else
            tp.syntaxError();
    }

    private static void parseFor(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        String var = tp.assertType(TokenBean.VARIABLE).getTokenText();
        tp.assertType(TokenBean.EQUAL);
        ExpressionBean expr1 = tp.parseExpression();
        tp.assertType(TokenBean.TO);
        ExpressionBean expr2 = tp.parseExpression();
        ExpressionBean expr3 = null;
        if (tp.isType(TokenBean.STEP))
        {
            tp.inc();
            expr3 = tp.parseExpression();
        }
        tp.addSyntax(SyntaxBean.FOR, var, expr1, expr2, expr3);
    }

    private static void parsePSet(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        tp.assertType(TokenBean.LPAREN);
        ExpressionBean expr1 = tp.parseExpression();
        tp.assertType(TokenBean.COMMA);
        ExpressionBean expr2 = tp.parseExpression();
        tp.assertType(TokenBean.RPAREN);
        Object expr3 = null;
        if (tp.isType(TokenBean.COMMA))
        {
            tp.inc();
            expr3 = tp.parseExpression();
        }
        tp.addSyntax(SyntaxBean.PSET, expr1, expr2, expr3);
    }

    private static void parsePaint(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        tp.assertType(TokenBean.LPAREN);
        ExpressionBean expr1 = tp.parseExpression();
        tp.assertType(TokenBean.COMMA);
        ExpressionBean expr2 = tp.parseExpression();
        tp.assertType(TokenBean.RPAREN);
        Object expr3 = null;
        if (tp.isType(TokenBean.COMMA))
        {
            tp.inc();
            expr3 = tp.parseExpression();
        }
        tp.addSyntax(SyntaxBean.PAINT, expr1, expr2, expr3);
    }

    private static void parseCircle(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        tp.assertType(TokenBean.LPAREN);
        ExpressionBean expr1 = tp.parseExpression();
        tp.assertType(TokenBean.COMMA);
        ExpressionBean expr2 = tp.parseExpression();
        tp.assertType(TokenBean.RPAREN);
        ExpressionBean expr3 = null;
        Object expr4 = null;
        if (tp.isType(TokenBean.COMMA))
        {
            tp.inc();
            expr3 = tp.parseExpression();
            if (tp.isType(TokenBean.COMMA))
            {
                tp.inc();
                expr4 = tp.parseExpression();
            }
        }
        tp.addSyntax(SyntaxBean.CIRCLE, expr1, expr2, expr3, expr4);
    }

    private static void parseDo(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        tp.assertType(TokenBean.UNTIL);
        ExpressionBean expr1 = tp.parseExpression();
        tp.addSyntax(SyntaxBean.DO_UNTIL, expr1);
    }

    private static void parseSelect(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        tp.assertType(TokenBean.CASE);
        ExpressionBean expr1 = tp.parseExpression();
        tp.addSyntax(SyntaxBean.SELECT, expr1);
    }

    private static void parseLine(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        if (tp.isType(TokenBean.INPUT) 
                && tp.isType(1, TokenBean.HASH) && tp.isType(2, TokenBean.NUMBER)
                && tp.isType(3, TokenBean.COMMA) && tp.isType(4, TokenBean.VARIABLE))
        {
            Integer arg1 = Integer.parseInt(tp.token(2).getTokenText());
            tp.inc(4);
            VariableBean arg2 = tp.parseVariable();
            tp.addSyntax(SyntaxBean.LINE_INPUT, arg1, arg2);
        }
        else if (tp.isType(TokenBean.LPAREN))
        {
            List<Object> expr = new ArrayList<>();
            tp.inc();
            expr.add(tp.parseExpression());
            tp.assertType(TokenBean.COMMA);
            expr.add(tp.parseExpression());
            tp.assertType(TokenBean.RPAREN);
            tp.assertType(TokenBean.SUBTRACT);
            tp.assertType(TokenBean.LPAREN);
            expr.add(tp.parseExpression());
            tp.assertType(TokenBean.COMMA);
            expr.add(tp.parseExpression());
            tp.assertType(TokenBean.RPAREN);
            while (tp.moreTokens())
            {
                if (tp.isType(TokenBean.COMMA))
                {
                    tp.inc();
                    continue;
                }
                if (tp.isType(TokenBean.END_OF_COMMAND))
                    break;
                expr.add(tp.parseExpression());
            }
            tp.addSyntax(SyntaxBean.DRAW_LINE, expr);
        }
        else if (tp.isType(TokenBean.SUBTRACT))
        {
            tp.inc();
            tp.assertType(TokenBean.STEP);
            tp.assertType(TokenBean.LPAREN);
            ExpressionBean arg1 = tp.parseExpression();
            tp.assertType(TokenBean.COMMA);
            ExpressionBean arg2 = tp.parseExpression();
            tp.assertType(TokenBean.RPAREN);
            ExpressionBean arg3 = null;
            if (tp.isType(TokenBean.COMMA))
            {
                tp.inc();
                arg3 = tp.parseExpression();
            }
            tp.addSyntax(SyntaxBean.DRAW_LINE, arg1, arg2, arg3);
        }
        else
            tp.syntaxError();
    }

    private static void parseGet(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        List<ExpressionBean> arg1 = new ArrayList<>();
        tp.assertType(TokenBean.LPAREN);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.COMMA);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.RPAREN);
        tp.assertType(TokenBean.SUBTRACT);
        tp.assertType(TokenBean.LPAREN);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.COMMA);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.RPAREN);
        tp.assertType(TokenBean.COMMA);
        VariableBean arg2 = tp.parseVariable();
        tp.addSyntax(SyntaxBean.GET_IMAGE, arg1, arg2);
    }

    private static void parsePut(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        List<ExpressionBean> arg1 = new ArrayList<>();
        tp.assertType(TokenBean.LPAREN);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.COMMA);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.RPAREN);
        tp.assertType(TokenBean.COMMA);
        arg1.add(tp.parseExpression());
        tp.assertType(TokenBean.COMMA);
        tp.assertType(TokenBean.PSET);
        tp.addSyntax(SyntaxBean.PUT_IMAGE, arg1);
    }

    private static void parseDef(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        FunctionBean func = new FunctionBean();
        func.setName(tp.assertType(TokenBean.VARIABLE).getTokenText());
        tp.assertType(TokenBean.LPAREN);
        for (;;)
        {
            if (tp.type() == TokenBean.RPAREN)
            {
                tp.inc();
                break;
            }
            if (tp.type() == TokenBean.COMMA)
            {
                tp.inc();
                continue;
            }
            func.getArgs().add(tp.parseVariable());
        }
        if (tp.type() == TokenBean.EQUAL)
            func.setCode(tp.parseExpression());
        tp.assertType(TokenBean.END_OF_COMMAND);
        tp.addSyntax(SyntaxBean.DEF, func);
        tp.program.getFunctions().put(func.getName().toUpperCase(), func);
        tp.program.getLabels().put(func.getName().toUpperCase(), tp.program.getSyntax().size());
    }

    private static void parseInput(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        if (tp.isType(TokenBean.HASH) && tp.isType(1, TokenBean.NUMBER)
                && tp.isType(2, TokenBean.COMMA))
        {
            Integer arg1 = Integer.parseInt(tp.token(1).getTokenText());
            tp.inc(3);
            VariableBean arg2 = tp.parseVariable();
            tp.addSyntax(SyntaxBean.LINE_INPUT, arg1, arg2);
        }
        else if (tp.isType(TokenBean.STRING)
                && tp.isType(1, TokenBean.COMMA))
        {
            String arg1 = tp.token().getTokenText();
            tp.inc(2);
            VariableBean arg2 = tp.parseVariable();
            tp.addSyntax(SyntaxBean.LINE_INPUT, arg1, arg2);
        }
        else
            tp.syntaxError();
    }

    private static void parseCase(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        Object arg1 = null;
        Object arg2 = null;
        if (tp.isType(TokenBean.ELSE))
        {
            arg1 = tp.token();
            tp.inc();
        }
        else if (tp.isType(TokenBean.NUMBER) && tp.isType(1, TokenBean.TO) && tp.isType(2, TokenBean.NUMBER))
        {
            arg1 = tp.token(0);
            arg2 = tp.token(2);
            tp.inc(3);
        }
        else if (tp.isType(TokenBean.NUMBER))
        {
            arg1 = tp.token();
            tp.inc();
        }
        else if (tp.isType(TokenBean.IS) && tp.isType(1, TokenBean.GREATERTHAN) && tp.isType(2, TokenBean.NUMBER))
        {
            arg1 = tp.token(1);
            arg2 = tp.token(2);
            tp.inc(3);
        }
        else
            tp.syntaxError();
        tp.addSyntax(SyntaxBean.CASE, arg1, arg2);
    }

    private static void parseDim(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        String arg1 = null;
        List<Integer> arg2 = new ArrayList<>();
        arg1 = tp.assertType(TokenBean.VARIABLE).getTokenText();
        tp.assertType(TokenBean.LPAREN);
        for (;;)
        {
            if (!tp.moreTokens())
                tp.syntaxError();
            TokenBean tok = tp.token();
            if (tok.getType() == TokenBean.RPAREN)
            {
                tp.inc();
                break;
            }
            if (tok.getType() == TokenBean.COMMA)
            {
                tp.inc();
                continue;
            }
            if (tok.getType() == TokenBean.NUMBER)
            {
                arg2.add(Integer.parseInt(tok.getTokenText()));
                tp.inc();
                continue;
            }
            tp.syntaxError();
        }        
        if (tp.isType(TokenBean.AS))
        {
            tp.inc();
            if (tp.isType(TokenBean.INTEGER))
                tp.inc();
        }
        tp.assertType(TokenBean.END_OF_COMMAND);
        tp.addSyntax(SyntaxBean.DIM, arg1, arg2);
    }

    private static void parseData(TokenPointer tp) throws IOException
    {
        tp.inc();
        while (tp.moreTokens())
        {
            if (tp.isType(TokenBean.COMMA))
            {
                tp.inc();
                continue;
            }
            if (tp.isType(TokenBean.END_OF_COMMAND))
                break;
            tp.program.getData().add(tp.parseExpression());
        }
    }

    private static void parsePrint(TokenPointer tp) throws IOException
    {
        tp.mark();;
        tp.inc();
        List<Object> args = new ArrayList<>();
        while (tp.moreTokens())
        {
            TokenBean tok = tp.token();
            if ((tok.getType() == TokenBean.COMMA) || (tok.getType() == TokenBean.SEMICOLON))
            {
                args.add(tok);
                tp.inc();
                continue;
            }
            if ((tok.getType() == TokenBean.HASH) && tp.isType(1, TokenBean.NUMBER))
            {
                args.add(tp.token(1));
                tp.inc(2);
                if (tp.isType(TokenBean.COMMA))
                    tp.inc();
                continue;
            }
            if (tok.getType() == TokenBean.END_OF_COMMAND)
                break;
            args.add(tp.parseExpression());
        }
        tp.addSyntax(SyntaxBean.PRINT, args);
    }

    private static void parseOn(TokenPointer tp) throws IOException
    {
        if (tp.ifTokenMatch(TokenBean.ON, TokenBean.ERROR, TokenBean.GOTO, TokenBean.NUMBER))
        {
            Integer arg1 = Integer.parseInt(tp.token(3).getTokenText());
            tp.inc(4);
            tp.addSyntax(SyntaxBean.ONERRORGOTO, arg1);
        }
        else
            tp.syntaxError();
    }

    private static void parseDoubleton(TokenPointer tp, int tokenType, int syntaxType) throws IOException
    {
        if (tp.ifTokenMatch(tokenType, TokenBean.NUMBER, TokenBean.COMMA, TokenBean.NUMBER))
        {
            tp.mark();
            String arg1 = tp.token(1).getTokenText();
            String arg2 = tp.token(3).getTokenText();
            tp.inc(4);
            tp.addSyntax(syntaxType, arg1, arg2);
        }
        else
        {
            tp.mark();
            tp.inc();
            ExpressionBean arg1 = tp.parseExpression();
            tp.inc(1);
            ExpressionBean arg2 = tp.parseExpression();
            tp.addSyntax(syntaxType, arg1, arg2);
        }
    }

    private static void parseSingleton(TokenPointer tp, int tokenType, int syntaxType) throws IOException
    {
        if (tp.ifTokenMatch(tokenType))
        {
            tp.mark();
            tp.inc();
            tp.addSyntax(syntaxType);
        }
        else
            tp.syntaxError();
    }

    private static void parseSingletonArg(TokenPointer tp, int tokenType, int argTokenType, int syntaxType) throws IOException
    {
        if (argTokenType < 0)
        {
            tp.mark();
            tp.inc();
            ExpressionBean arg1 = tp.parseExpression();
            tp.addSyntax(syntaxType, arg1);
        }
        else if (tp.ifTokenMatch(tokenType, argTokenType))
        {
            tp.mark();
            String arg1 = tp.token(1).getTokenText();
            tp.inc(2);
            tp.addSyntax(syntaxType, arg1);
        }
        else
            tp.syntaxError();
    }

    private static void parseColor(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        ExpressionBean arg1 = tp.parseExpression();
        tp.addSyntax(SyntaxBean.COLOR, arg1);
    }

    private static void parseOpen(TokenPointer tp) throws IOException
    {
        tp.mark();
        tp.inc();
        String arg1 = tp.assertType(TokenBean.STRING).getTokenText();
        tp.assertType(TokenBean.COMMA);
        int arg2 = Integer.parseInt(tp.assertType(TokenBean.NUMBER).getTokenText());
        tp.assertType(TokenBean.COMMA);
        ExpressionBean arg3 = tp.parseExpression();
        tp.addSyntax(SyntaxBean.OPEN, arg1, arg2, arg3);
    }

    private static void parseClose(TokenPointer tp) throws IOException
    {
        if (tp.ifTokenMatch(TokenBean.CLOSE, TokenBean.NUMBER))
        {
            tp.mark();
            tp.inc();
            int stream = Integer.parseInt(tp.token().getTokenText());
            tp.inc();
            tp.addSyntax(SyntaxBean.CLOSE, stream);
        }
        else if (tp.ifTokenMatch(TokenBean.CLOSE))
        {
            tp.mark();
            tp.inc();
            tp.addSyntax(SyntaxBean.CLOSE);
        }
        else
            tp.syntaxError();
    }

    private static void parseEnd(TokenPointer tp) throws IOException
    {
        if (tp.ifTokenMatch(TokenBean.END))
        {
            tp.mark();
            tp.inc();
            tp.addSyntax(SyntaxBean.END_PROGRAM);
        }
        else if (tp.ifTokenMatch(TokenBean.END, TokenBean.IF))
        {
            tp.mark();
            tp.inc(2);
            tp.addSyntax(SyntaxBean.END_IF);
        }
        else if (tp.ifTokenMatch(TokenBean.END, TokenBean.DEF))
        {
            tp.mark();
            tp.inc(2);
            tp.addSyntax(SyntaxBean.END_DEF);
        }
        else if (tp.ifTokenMatch(TokenBean.END, TokenBean.SELECT))
        {
            tp.mark();
            tp.inc(2);
            tp.addSyntax(SyntaxBean.END_SELECT);
        }
        else
            tp.syntaxError();
    }
}

class TokenPointer
{
    public ProgramBean program;
    public int tp;
    public int start;
    
    public TokenPointer(ProgramBean program)
    {
        this.program = program;
    }
    
    public VariableBean parseVariable() throws IOException
    {
        VariableBean var = new VariableBean();
        var.setName(assertType(TokenBean.VARIABLE).getTokenText());
        if (isType(TokenBean.LPAREN))
        {
            inc();
            for (;;)
            {
                if (isType(TokenBean.RPAREN))
                {
                    inc();
                    break;
                }
                if (isType(TokenBean.COMMA))
                {
                    inc();
                    continue;
                }
                var.getIndicies().add(parseExpression());
            }
        }
        return var;
    }

    public void addSyntax(int syntaxType, Object... args)
    {
        SyntaxBean s = new SyntaxBean();
        s.setType(syntaxType);
        s.setFirstToken(start);
        s.setLastToken(tp);
        if (args.length > 0)
            s.setArg1(args[0]);
        if (args.length > 1)
            s.setArg2(args[1]);
        if (args.length > 2)
            s.setArg3(args[2]);
        if (args.length > 3)
            s.setArg4(args[3]);
        program.getSyntax().add(s);
        //System.out.println("Adding "+syntaxType);
   }

    public boolean moreTokens()
    {
        return tp < program.getTokens().size();
    }
    
    public TokenBean token()
    {
        return program.getTokens().get(tp);
    }
    
    public int type()
    {
        return token().getType();
    }
    
    public TokenBean token(int incr)
    {
        return program.getTokens().get(tp + incr);
    }
    
    public int type(int incr)
    {
        return token(incr).getType();
    }
    
    public int inc()
    {
        tp++;
        return tp;
    }
    
    public int inc(int incr)
    {
        tp += incr;
        return tp;
    }
    
    public boolean isType(int tokenType)
    {
        return type() == tokenType;
    }
    
    public boolean isType(int incr, int tokenType)
    {
        return type(incr) == tokenType;
    }

    public ExpressionBean parseExpression() throws IOException
    {
        int start = tp;
        int inParen = 0;
        while (tp < program.getTokens().size())
        {
            TokenBean tok = token();
            if (tok.getType() == TokenBean.LPAREN)
                inParen++;
            else if (tok.getType() == TokenBean.RPAREN)
            {
                if (inParen == 0)
                    break;
                inParen--;
            }
            else if (tok.getType() == TokenBean.COMMA)
            {
                if (inParen <= 0)
                    break;
            }
            else if (!isExpressionPart(tok))
                break;
            inc();
        }
        int end = tp;
        if (start == end)
        {
            System.err.println("Didn't find expression, found "+token().getTokenText());
            syntaxError();
        }
        ExpressionBean expr = new ExpressionBean();
        expr.setFirstToken(start);
        expr.setLastToken(end);
        return expr;
    }

    public boolean isStartOfExpression()
    {
        return isExpressionPart(token());
    }
    
    private boolean isExpressionPart(TokenBean tok)
    {
        switch (tok.getType())
        {
            case TokenBean.NUMBER:
            case TokenBean.STRING:
            case TokenBean.HEXNUMBER:
            case TokenBean.VARIABLE:
            case TokenBean.LPAREN:
            case TokenBean.RPAREN:
            case TokenBean.ADD:
            case TokenBean.SUBTRACT:
            case TokenBean.MULTIPLY:
            case TokenBean.DIVIDE:
            case TokenBean.EQUAL:
            case TokenBean.LESSTHAN:
            case TokenBean.GREATERTHAN:
            case TokenBean.COMMA:
            case TokenBean.EXPONENT:
            case TokenBean.OR:
            case TokenBean.AND:
            case TokenBean.MOD:
            case TokenBean.NOT_EQUAL:
            case TokenBean.GREATERTHAN_EQUAL:
            case TokenBean.LESSTHAN_EQUAL:
               return true;
        }
        return false;
    }

    public void mark()
    {
        start = tp;
    }
 
    public void printContext()
    {
        int start = tp - 1;
        while ((start > 0) && program.getTokens().get(start).getType() != TokenBean.END_OF_COMMAND)
            start--;
        int end = tp + 1;
        while ((end < program.getTokens().size()) && program.getTokens().get(start).getType() != TokenBean.END_OF_COMMAND)
            end++;
        for (int i = start + 1; i <= end; i++)
        {
            TokenBean tok = program.getTokens().get(i);
            System.err.print((i == tp) ? "[" : "(");
            System.err.print(tok.getTokenText()+"|"+tok.getType());
            System.err.print((i == tp) ? "]" : ")");
        }
        System.err.println();
    }
    
    public void syntaxError() throws IOException
    {
        TokenBean tok = token();
        String msg = tok.getTokenText();
        System.err.println(tok.getLine().getText());
        printContext();
        throw new IOException(msg+" syntax error: "+tok.getLine().getText());
    }
    
    public TokenBean assertType(int tokenType) throws IOException
    {
        if (!isType(tokenType))
        {
            System.err.println("Expected "+tokenType+", actually "+token().getType());
            syntaxError();
        }
        TokenBean tok = token();
        inc();
        return tok;
    }

    public boolean ifTokenMatch(int... tokens)
    {
        for (int i = 1; i < tokens.length; i++)
            if (!isType(i, tokens[i]))
                return false;
        if (isType(tokens.length, TokenBean.END_OF_COMMAND))
            return true;
        return false;
    }
}