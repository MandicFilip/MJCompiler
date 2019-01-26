package rs.ac.bg.etf.pp1;


import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

//SymbolTable class extends Tab and wraps its methods
//Use SymbolTable for methods and Boolean Const
public class SemanticPass extends VisitorAdaptor {

    public static Obj currentTypeObj = Tab.noObj;
    public static Struct currentTypeStruct = Tab.noType;



    private boolean isSymbolDefined(String typeName) {
        Obj foundObj = SymbolTable.find(typeName);
        return foundObj != Tab.noObj;
    }


    //--------------------VISIT METHODS-------------------------------------

    public void visit(Program program) {
        SymbolTable.chainLocalSymbols(program.getProgName().obj);
        SymbolTable.closeScope();
    }

    public void visit(ProgName progName) {
        progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
        SymbolTable.openScope();
    }

    public void visit(Type type) {
        String typeName = type.getTName();

        if (isSymbolDefined(typeName)) {
            Obj foundObj = SymbolTable.find(typeName);
            if (foundObj.getKind() == Obj.Type) {
                currentTypeObj = foundObj;
            } else {
                // report error - found ident in symbol table but it is not a type
            }
        } else {
            //report error - no type in symbol table

            currentTypeObj = Tab.noObj;
        }
    }

    public void visit(ConstDecl constDecl) {
        if (currentTypeObj == Tab.noObj) {
            //error in type already reported
            return;
        }

//        String constName = constDecl
    }

    public void visit(VarDeclExpression varDeclExpression) {

    }

    public void visit(VarDecl varDecl) {
        //Tab.insert(Obj.Var, varDecl.get);
    }




}
