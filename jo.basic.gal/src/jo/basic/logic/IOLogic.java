package jo.basic.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jo.basic.data.BasicRuntime;
import jo.basic.data.LineBean;
import jo.basic.data.ProgramBean;

public class IOLogic
{
    public static void load(BasicRuntime rt, String module) throws IOException
    {
        ProgramBean program = rt.getProgram();
        if (program == null)
        {
            program = new ProgramBean();
            rt.setProgram(program);
        }
        program.getLines().clear();
        File modFile = new File(rt.getRoot(), module);
        BufferedReader rdr = new BufferedReader(new FileReader(modFile));
        for (int number = 1;; number++)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            LineBean line = new LineBean();
            line.setText(inbuf);
            line.setNumber(number);
            if (program.getLines().size() > 0)
            {
                LineBean lastLine = program.getLines().get(program.getLines().size() - 1);
                lastLine.setNext(line);
                line.setPrevious(lastLine);
            }
            program.getLines().add(line);
        }
        rdr.close();
        System.out.println(program.getLines().size()+" lines read");
        TokenizeLogic.tokenize(rt);
        System.out.println(program.getTokens().size()+" tokens");
        SyntaxLogic.syntax(rt);
        System.out.println(program.getSyntax().size()+" commands");
        System.out.println(program.getLabels().size()+" labels");
        System.out.println(program.getData().size()+" data");
        System.out.println(program.getFunctions().size()+" functions");
    }
}
