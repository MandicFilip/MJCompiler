package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Compiler {
    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(MJParserTest.class);
        if (args.length < 1) {
            log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");
            return;
        }

        File sourceCode = new File(args[0]);
        if (!sourceCode.exists()) {
            log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
            return;
        }

        log.info("Compiling source file: " + sourceCode.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
            Yylex lexer = new Yylex(br);
            MJParser p = new MJParser(lexer);
            Symbol s = p.parse();  //pocetak parsiranja
            SyntaxNode prog = (SyntaxNode) (s.value);
            log.info(prog.toString());

            SemanticAnalyzer semanticCheck = new SemanticAnalyzer();
            prog.traverseBottomUp(semanticCheck);

            SymbolTable.dump();

            if (!p.errorDetected && !semanticCheck.isErrorsInCode()) {
                File objFile = new File(args[1]);
                log.info("Generating bytecode file: " + objFile.getAbsolutePath());
                if (objFile.exists())
                    objFile.delete();

                // Code generation...
                CodeGenerator codeGenerator = new CodeGenerator();
                prog.traverseBottomUp(codeGenerator);
                Code.dataSize = semanticCheck.getGlobal_variables_count();
                Code.mainPc = codeGenerator.getMainPC();
                Code.write(new FileOutputStream(objFile));
                log.info("Parsiranje uspesno zavrseno!");
            } else {
                log.error("Parsiranje NIJE uspesno zavrseno!");
            }
        }
    }
}
