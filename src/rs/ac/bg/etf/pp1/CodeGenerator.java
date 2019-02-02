package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import sun.security.krb5.internal.crypto.Des;

import java.util.Collection;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPC;

    public int getMainPC() {
        return mainPC;
    }

//    @Override
//    public void visit(ConstDecl constDecl) {
//        super.visit(constDecl);
//
//        Obj con = SymbolTable.find(constDecl.getConstName());
//        Code.load(con);
//    }

    private void loadDesignator(Designator designator) {
        if (designator instanceof EnumDesignator) {
            EnumDesignator enumDesignator = (EnumDesignator) designator;

            Obj enumObj = SymbolTable.find(enumDesignator.getDesignatorName());
            String enumMemberName = enumDesignator.getEnumMember();

            Collection<Obj> members = enumObj.getLocalSymbols();
            for (Obj member : members) {
                if (member.getName().equals(enumMemberName)) {
                    Code.load(member);
                    break;
                }
            }

        } else if (designator instanceof ArrayDesignator) {
            ArrayDesignator arrayDesignator = (ArrayDesignator) designator;

            Obj arrayObj = SymbolTable.find(arrayDesignator.getArrayName().getDesignatorName());
            Struct type = arrayObj.getType().getElemType();

            if (type == SymbolTable.intType) {
                Code.put(Code.aload);
            } else {
                Code.put(Code.baload);
            }
        } else if (designator instanceof SimpleDesignator) {
            SimpleDesignator simpleDesignator = (SimpleDesignator) designator;

            Obj designatorObj = SymbolTable.find(simpleDesignator.getDesignatorName());
            Code.load(designatorObj);
        }
    }

    private void storeDesignator(Designator designator) {
        if (designator instanceof ArrayDesignator) {
            ArrayDesignator arrayDesignator = (ArrayDesignator) designator;

            Obj arrayObj = SymbolTable.find(arrayDesignator.getArrayName().getDesignatorName());
            Struct type = arrayObj.getType().getElemType();

            if (type == SymbolTable.intType) {
                Code.put(Code.astore);
            } else {
                Code.put(Code.bastore);
            }
        } else if (designator instanceof SimpleDesignator) {
            SimpleDesignator simpleDesignator = (SimpleDesignator) designator;
            Obj designatorObj = SymbolTable.find(simpleDesignator.getDesignatorName());
            Code.store(designatorObj);
        }
    }

    public void visit(EnumDesignator enumDesignator) {

    }

    public void visit(ArrayName arrayName) {

    }

    public void visit(ArrayDesignator arrayDesignator) {

    }

    public void visit(SimpleDesignator simpleDesignator) {

    }

    public void visit(NumberFactor numberFactor) {
        int value = numberFactor.getValueNumber();
        Obj tmp = new Obj(Obj.Con, "", SymbolTable.intType, value, 0);
        Code.load(tmp);
    }

    public void visit(CharFactor charFactor) {
        int value = charFactor.getValueChar();
        Obj tmp = new Obj(Obj.Con, "", SymbolTable.charType, value, 0);
        Code.load(tmp);
    }

    public void visit(BoolFactor boolFactor) {
        int value = 0;
        if (boolFactor.getValBool()) {
            value = 1;
        }
        Obj tmp = new Obj(Obj.Con, "", SymbolTable.charType, value, 0);
        Code.load(tmp);
    }

    public void visit(NewTypeFactor newTypeFactor) {
        //TODO later
    }

    public void visit(FunCallFactor funCallFactor) {
        //TODO later
    }

    @Override
    public void visit(PrintStatement printStatement) {
        super.visit(printStatement);

        ExpressionToPrint ex = printStatement.getExpressionToPrint();
        Struct type;
        int printTimes = 1;
        int printWidth = 1;

        if (ex instanceof ExpressionAndConstToPrint) {
            printTimes = ((ExpressionAndConstToPrint) ex).getValue();
            type = ((ExpressionAndConstToPrint) ex).getExpr().struct;
        }
        else type = ((OnlyExpressionToPrint) ex).getExpr().struct;

        if (type == SymbolTable.intType) {
            printWidth = 5; //???
        }

        Code.loadConst(printWidth);
        Code.put(Code.print);
        //TODO generate code for multiple calls
    }
}
