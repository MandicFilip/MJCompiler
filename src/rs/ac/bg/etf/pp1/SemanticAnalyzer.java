package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayList;
import java.util.List;

//SymbolTable class extends Tab and wraps its methods
//Use SymbolTable for methods and Boolean Const
public class SemanticAnalyzer extends VisitorAdaptor {

    private static Obj currentTypeObj = SymbolTable.noObj;
    private static Obj currentMethod = SymbolTable.noObj;

    private List<Integer> enumValues = new ArrayList<>();
    private static int nextEnumValue = 0;
    private static Obj currentEnum = SymbolTable.noObj;

    private static boolean hasReturnExpr = false;
    private static boolean isMainDefined = false;

    private boolean isSymbolDefined(String typeName) {
        Obj foundObj = SymbolTable.find(typeName);
        return foundObj != SymbolTable.noObj;
    }

    private boolean isDefinedInCurrentScope(String name) {
        return SymbolTable.currentScope().findSymbol(name) != null;
    }

    private boolean isMethodAndVarWithSameName(String varName) {
        if (currentMethod == SymbolTable.noObj) return false;
        return currentMethod.getName().equals(varName);
    }

    private boolean isEnumAndVarWithSameName(String varName) {
        if (currentEnum == SymbolTable.noObj) return false;
        return currentEnum.getName().equals(varName);
    }

    private boolean checkIfEnumValueIsUnique(int value) {
        for (int i = 0; i < enumValues.size(); i++) {
            if (value == enumValues.get(i)) return false;
        }
        return true;
    }


    private boolean isMainCorrect() {
        return currentMethod.getLevel() == 0 && currentMethod.getType() == SymbolTable.noType;
    }

    //--------------------VISIT METHODS----------------------------------------

    //--------------------PROGRAM----------------------------------------------

    public void visit(Program program) {
        SymbolTable.chainLocalSymbols(program.getProgName().obj);
        SymbolTable.closeScope();
    }

    public void visit(ProgName progName) {
        progName.obj = SymbolTable.insert(Obj.Prog, progName.getPName(), SymbolTable.noType);
        SymbolTable.openScope();
    }

    //-----------------------Type----------------------------------------------

    public void visit(Type type) {
        String typeName = type.getTName();

        if (isSymbolDefined(typeName)) {
            Obj foundObj = SymbolTable.find(typeName);
            if (foundObj.getKind() == Obj.Type) {
                currentTypeObj = foundObj;
            } else {
                //TODO report error - found ident in symbol table but it is not a type
            }
        } else {
            //TODO report error - no type in symbol table

            currentTypeObj = SymbolTable.noObj;
        }
    }

    //----------------------CONST----------------------------------------------

    public void visit(ConstDecl constDecl) {
        if (currentTypeObj == SymbolTable.noObj) {
            //TODO error in type already reported
            return;
        }

        String constName = constDecl.getConstName();
        Obj constObj = SymbolTable.find(constName);
        if (constObj == SymbolTable.noObj) {
            //TODO error symbol with that name already exists
            return;
        }

        ConstKind constKind = constDecl.getConstKind();

        int constValue = 0;
        Struct rightSide = SymbolTable.noType;

        if (constKind instanceof ConstNumber) {
            rightSide = SymbolTable.intType;
            constValue = ((ConstNumber) constKind).getNumValue();

        } else if (constKind instanceof ConstChar) {
            rightSide = SymbolTable.charType;
            constValue = ((ConstChar) constKind).getCharValue();

        } if (constKind instanceof ConstBool) {
            rightSide = SymbolTable.boolType;
            if (((ConstBool) constKind).getBoolValue()) {
                constValue = 1;
            } //no need for else - init to 0

        } else {
            //TODO report error - wrong const type
        }

        if (currentTypeObj.getType().assignableTo(rightSide)) {
            Obj newConst = SymbolTable.insert(Obj.Con, constName, rightSide);
            newConst.setAdr(constValue); //init const
        }

    }

    //-----------------------VARIABLE------------------------------------------

    public void visit(VarDecl varDecl) {

        if (currentTypeObj == SymbolTable.noObj) {
            //TODO report error type unknown - already reported in type
            return;
        }

        if (varDecl instanceof ErrorVarDecl) {
            return; //syntax error, no need for semantic processing
        }

        VarDeclaration varDeclaration = (VarDeclaration) varDecl;

        String varName = varDeclaration.getVarName();
        if (isDefinedInCurrentScope(varName) || isMethodAndVarWithSameName(varName)) {
            //TODO report error symbol with that name already exists
            return;
        }

        ArrOpt arrOpt = varDeclaration.getArrOpt();
        if (arrOpt instanceof ArrayOption) {
            SymbolTable.insert(Obj.Var, varName, new Struct(Struct.Array, currentTypeObj.getType()));
        }
        else {
            SymbolTable.insert(Obj.Var, varName, currentTypeObj.getType());
        }
    }


    //-----------------------ENUM----------------------------------------------

    public void visit(EnumDecl enumDecl) {
        SymbolTable.chainLocalSymbols(enumDecl.getEnumStart().obj);
        SymbolTable.closeScope();
    }

    public void visit(EnumStart enumStart) {
        SymbolTable.insert(Obj.Type, enumStart.getEName(), SymbolTable.enumType);
        SymbolTable.openScope();
        nextEnumValue = 0;
        enumValues.clear();
    }

    public void visit(EnumMember enumMember) {
        String enumName = enumMember.getEnumMemberName();
        if (isDefinedInCurrentScope(enumName) || isEnumAndVarWithSameName(enumName)) {
            //TODO report error symbol with that name already exists
            return;
        }

        int value = nextEnumValue++;
        EnumInit enumInit = enumMember.getEnumInit();
        if (enumInit instanceof SingleEnumInit) {
            value = ((SingleEnumInit)enumInit).getEnumMemberValue();
            nextEnumValue = value + 1;
        }

        if (checkIfEnumValueIsUnique(value)) {
            Obj newEnum = SymbolTable.insert(Obj.Con, enumName, SymbolTable.intType);
            newEnum.setAdr(value);
        } else {
            //TODO report error two same values in one enum
        }
    }

    //--------------------METHODS----------------------------------------------

    public void visit(MethodDecl methodDecl) {

        if (currentMethod == SymbolTable.noObj) {
            //TODO report error currentMethod not set
            return;
        }

        SymbolTable.chainLocalSymbols(currentMethod);

        String methodName = currentMethod.getName();
        if (methodName.equals("main") && !isMainCorrect()) {
            //TODO report error main has wrong signature
        }

        if (!hasReturnExpr && currentMethod.getType() != SymbolTable.noType) {
            //TODO report error function which returns value has no return expr
            hasReturnExpr = false;
        }

        currentMethod = SymbolTable.noObj;
        SymbolTable.closeScope();
    }

    public void visit(MethodStart methodStart) {
        String methodName = methodStart.getMethodName();

        if (isSymbolDefined(methodName)) {
            //TODO report error symbol already defined
            return;
        }

        RetType retType = methodStart.getRetType();
        Struct type;

        if (retType instanceof RetVoid) {
            type = SymbolTable.noType;
        } else {
            type = currentTypeObj.getType();
        }

        if (methodName.equals("main"))
            isMainDefined = true;

        currentMethod = SymbolTable.insert(Obj.Meth, methodName, type);
        SymbolTable.openScope();

        currentMethod.setLevel(0); //set number of parameters to 0 before parameters list
    }

    //this can't be done in method declaration because params are needed for recursion
    public void visit(FormParams formParams) {
        currentMethod.setLocals(SymbolTable.currentScope().getLocals());
    }

    public void visit(FormPara formPara) {
        String formParaName = formPara.getParamName();

        if (currentTypeObj == SymbolTable.noObj) {
            //TODO report error type unknown - already reported in type
            return;
        }

        if (isDefinedInCurrentScope(formParaName)) {
            //TODO report error form parameter already defined in this scope
            return;
        }

        currentMethod.setLevel(currentMethod.getLevel() + 1);
        ArrOpt arrOpt = formPara.getArrOpt();
        if (arrOpt instanceof ArrayOption) {
            SymbolTable.insert(Obj.Var, formParaName, new Struct(Struct.Array, currentTypeObj.getType()));
        }
        else {
            SymbolTable.insert(Obj.Var, formParaName, currentTypeObj.getType());
        }
    }

    //-----------------------STATEMENTS----------------------------------------

}
