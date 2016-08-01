package jo.basic.logic;

import jo.basic.data.BasicRuntime;
import jo.basic.data.LineBean;
import jo.basic.data.ProgramBean;
import jo.basic.data.TokenBean;

public class TokenizeLogic
{
    public static void tokenize(BasicRuntime rt)
    {
        ProgramBean program = rt.getProgram();
        for (LineBean line : program.getLines())
            tokenize(program, line);
    }
    private static void tokenize(ProgramBean program, LineBean line)
    {
        int startTokens = program.getTokens().size();
        String text = line.getText();
        int lp = 0;
        //System.out.println("("+text.length()+")"+text);
        for (;;)
        {
            lp = skipWhitespace(lp, text);
            if (lp >= text.length())
                break;
            if (Character.isLetter(text.charAt(lp)))
            {
                int start = lp;
                while ((lp < text.length()) && Character.isLetterOrDigit(text.charAt(lp)))
                    lp++;
                if ((lp < text.length()) && ((text.charAt(lp) == '$') || (text.charAt(lp) == '%')))
                    lp++;
                int end = lp;
                String kw = text.substring(start, end);
                Integer type = TokenBean.KEYWORDS.get(kw.toUpperCase());
                if (type == null)
                    addToken(program, line, start, end, TokenBean.VARIABLE);
                else if (type == TokenBean.REM)
                    break;
                else
                    addToken(program, line, start, end, type);
            }
            else if (Character.isDigit(text.charAt(lp)) || text.charAt(lp) == '.')
                lp = parseDigit(program, line, text, lp);
            else if (text.charAt(lp) == '\"')
                lp = parseString(program, line, text, lp);
            else if (text.charAt(lp) == ':')
                if ((program.getTokens().size() > 0) && (program.getTokens().get(program.getTokens().size() - 1).getType() == TokenBean.CASE))
                    lp++;
                else
                    addToken(program, line, lp, ++lp, TokenBean.END_OF_COMMAND);
            else if ((text.charAt(lp) == '&') && (text.charAt(lp+1) == 'H'))
                lp = parseHexDigit(program, line, text, lp + 2);
            else
            {
                String op = text.substring(lp, lp + 1);
                Integer type = TokenBean.OPERATORS.get(op);
                if (type != null)
                    addToken(program, line, lp, ++lp, type);
                else
                    throw new IllegalStateException("Unexpected '"+text.charAt(lp)+"' at line "+line.getNumber()+" char "+lp);
            }
        }
        int endTokens = program.getTokens().size();
        if (endTokens > startTokens)
            addToken(program, line, lp, lp, TokenBean.END_OF_COMMAND);
    }
    private static int parseString(ProgramBean program, LineBean line,
            String text, int lp)
    {
        int start = ++lp;
        while ((lp < text.length()) && (text.charAt(lp) != '\"'))
            lp++;
        int end = lp++;
        addToken(program, line, start, end, TokenBean.STRING);
        return lp;
    }
    private static int parseDigit(ProgramBean program, LineBean line,
            String text, int lp)
    {
        boolean doneDecimal = false;
        int start = lp;
        while (lp < text.length())
        {
            if (Character.isDigit(text.charAt(lp)))
                lp++;
            else if (text.charAt(lp) == '.')
            {
                if (!doneDecimal)
                {
                    doneDecimal = true;
                    lp++;
                }
                else
                    throw new IllegalStateException("Unexpected '.' at line "+line.getNumber()+" char "+lp);
            }
            else
                break;
        }
        int end = lp;
        addToken(program, line, start, end, TokenBean.NUMBER);
        return lp;
    }
    private static int parseHexDigit(ProgramBean program, LineBean line,
            String text, int lp)
    {
        int start = lp;
        while (lp < text.length())
        {
            if (Character.isDigit(text.charAt(lp)))
                lp++;
            else if ((text.charAt(lp) >= 'A') && (text.charAt(lp) <= 'F'))
                lp++;
            else
                break;
        }
        int end = lp;
        addToken(program, line, start, end, TokenBean.HEXNUMBER);
        return lp;
    }
    
    private static void addToken(ProgramBean program, LineBean line, int start, int end, int type)
    {
        TokenBean t = new TokenBean();
        t.setLine(line);
        t.setCharStart(start);
        t.setCharEnd(end);
        t.setType(type);
        program.getTokens().add(t);
    }
/*    
    private static void tokenize(ProgramBean program, LineBean line)
    {
        line.getTokens().clear();
        String text = line.getText();
        int lp = 0;
        lp = skipWhitespace(lp, text);
        lp = parseLabel(line, lp);
        if (lp < 0)
            return;
        if (lp == text.length())
        {
            TokenBean tok = new TokenBean();
            tok.setType(TokenBean.REMARK);
            tok.setCharStart(0);
            tok.setCharEnd(text.length());
            line.getTokens().add(tok);
            return;
        }
        int tokStart = lp;
        lp = skipToWhitespace(lp, text);
        TokenBean main = new TokenBean();
        main.setLine(line);
        main.setCharStart(tokStart);
        main.setCharEnd(lp);
        String mainTok = text.substring(tokStart, lp);
        switch (mainTok.toUpperCase())
        {
            case "GOTO":
                main.setType(TokenBean.GOTO);
                line.getTokens().add(main);
                parseLineNum(line, text, lp);
                break;
            case "GOSUB":
                main.setType(TokenBean.GOSUB);
                line.getTokens().add(main);
                parseLineNum(line, text, lp);
                break;
            case "REM":
                main.setType(TokenBean.REMARK);
                line.getTokens().add(main);
                TokenBean cmnt = new TokenBean();
                cmnt.setLine(line);
                cmnt.setType(TokenBean.COMMENT);
                cmnt.setCharStart(lp);
                cmnt.setCharEnd(text.length());
                line.getTokens().add(cmnt);
                break;
            case "RETURN":
                main.setType(TokenBean.RETURN);
                line.getTokens().add(main);
                break;
            case "CLS":
                main.setType(TokenBean.CLS);
                line.getTokens().add(main);
                break;
            case "END":
                parseEnd(line, text, lp, main);
                break;
            default:
                main.setType(TokenBean.UNKNOWN);
                line.getTokens().add(main);
                System.err.println("Can't tokenize line #"+line.getNumber()+": "+text);
                program.setUnknownLines(program.getUnknownLines() + 1);
                break;
        }
    }

    private static void parseLineNum(LineBean line, String text, int lp)
    {
        lp = skipWhitespace(lp, text);
        int lineNumStart = lp;
        lp = skipToWhitespace(lp, text);
        int lineNumEnd = lp;
        TokenBean lineNum = new TokenBean();
        lineNum.setLine(line);
        lineNum.setType(TokenBean.LINENUM);
        lineNum.setCharStart(lineNumStart);
        lineNum.setCharEnd(lineNumEnd);
        line.getTokens().add(lineNum);
        if (lp != text.length())
            System.out.println("Unrecognized text at end of GOTO: "+text);
    }
    
    private static int parseExpr(LineBean line, String text, int lp)
    {
        while (lp < text.length())
            if (Character.isLetter(text.charAt(lp)))
            {
                int varStart = lp++;
                while ((lp < text.length()) && Character.isLetterOrDigit(text.charAt(lp)))
                    lp++;
                if ((lp < text.length()) && ((text.charAt(lp) == '$') || (text.charAt(lp) == '%')))
                    lp++;
                int varEnd = lp;
                String varText = text.substring(varStart, varEnd);
                TokenBean var = new TokenBean();
                var.setLine(line);
                var.setCharStart(varStart);
                var.setCharEnd(varEnd);
                if ("AND".equalsIgnoreCase(varText))
                {
                    var.setType(TokenBean.AND);
                    line.getTokens().add(var);
                }
                else
                {
                    var.setType(TokenBean.VARIABLE);
                    line.getTokens().add(var);
                }
            }
            else if (Character.isWhitespace(text.charAt(lp)))
                ;
            else
    }

    private static void parseEnd(LineBean line, String text, int lp,
            TokenBean main)
    {
        int tokStart;
        lp = skipWhitespace(lp, text);
        if (lp == text.length())
        {
            main.setType(TokenBean.END);
            line.getTokens().add(main);
        }
        else
        {
            tokStart = lp;
            lp = skipToWhitespace(lp, text);
            String secTok = text.substring(tokStart, lp);
            switch (secTok.toUpperCase())
            {
                case "IF":
                    main.setType(TokenBean.ENDIF);
                    main.setCharEnd(lp);
                    line.getTokens().add(main);
                    break;
                case "DEF":
                    main.setType(TokenBean.ENDDEF);
                    main.setCharEnd(lp);
                    line.getTokens().add(main);
                    break;
                case "SELECT":
                    main.setType(TokenBean.ENDSELECT);
                    main.setCharEnd(lp);
                    line.getTokens().add(main);
                    break;
                default:
                    System.err.println("Unrecognized text after END: "+text);
                    break;
            }
        }
    }

    private static int parseLabel(LineBean line, int lp)
    {
        String text = line.getText();
        if ((lp < text.length()) && Character.isDigit(text.charAt(lp)))
        {   // label
            int digitStart = lp;
            lp = skipToWhitespace(lp, text);
            int digitEnd = lp;
            line.setLabel(text.substring(digitStart, digitEnd));
            lp = skipWhitespace(lp, text);
        }
        return lp;
    }
    
    private static int skipToWhitespace(int lp, String txt)
    {
        while ((lp < txt.length()) && !Character.isWhitespace(txt.charAt(lp)))
            lp++;
        return lp;
    }

    private static void splitLines(BasicRuntime rt)
    {
        ProgramBean program = rt.getProgram();
        if (program.getLines().size() == 0)
            return;
        for (LineBean line = program.getLines().get(0); line != null; line = line.getNext())
        {
            String text = line.getText();
            int o = text.indexOf(':');
            if (o < 0)
                continue;
            LineBean line2 = new LineBean();
            line2.setNumber(line.getNumber());
            line2.setText(text.substring(o + 1).trim());
            line.setText(text.substring(0, o).trim());
            line2.setNext(line.getNext());
            line.setNext(line2);
            line2.getNext().setPrevious(line2);
            line2.setPrevious(line2);
        }
    }
*/
    
    private static int skipWhitespace(int lp, String txt)
    {
        while ((lp < txt.length()) && Character.isWhitespace(txt.charAt(lp)))
            lp++;
        return lp;
    }
}
