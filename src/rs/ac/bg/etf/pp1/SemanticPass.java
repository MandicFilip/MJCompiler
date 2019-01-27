package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

//SymbolTable class extends Tab and wraps its methods
//Use SymbolTable for methods and Boolean Const
public class SemanticPass extends VisitorAdaptor {

    private static Obj currentTypeObj = Tab.noObj;
    private static Obj currentMethod = Tab.noObj;

    private static int nextEnumValue = 0;
    private static Obj currentEnum = Tab.noObj;

    private static boolean hasReturnExpr = false;
    private static boolean isMainDefined = false;

    private boolean isSymbolDefined(String typeName) {
        Obj foundObj = SymbolTable.find(typeName);
        return foundObj != Tab.noObj;
    }

    private boolean isDefinedInCurrentScope(String name) {
        return SymbolTable.currentScope().findSymbol(name) != null;
    }

    private boolean isMethodAndVarWithSameName(String varName) {
        if (currentMethod == Tab.noObj) return false;
        return currentMethod.getName().equals(varName);
    }

    private boolean isEnumAndVarWithSameName(String varName) {
        if (currentEnum == Tab.noObj) return false;
        return currentEnum.getName().equals(varName);
    }

    private boolean isMainCorrect() {
        return currentMethod.getLevel() == 0 && currentMethod.getType() == Tab.noType;
    }

    //--------------------VISIT METHODS----------------------------------------

    //--------------------PROGRAM----------------------------------------------

    public void visit(Program program) {
        SymbolTable.chainLocalSymbols(program.getProgName().obj);
        SymbolTable.closeScope();
    }

    public void visit(ProgName progName) {
        progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
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

            currentTypeObj = Tab.noObj;
        }
    }

    //----------------------CONST----------------------------------------------

    public void visit(ConstDecl constDecl) {
        if (currentTypeObj == Tab.noObj) {
            //TODO error in type already reported
            return;
        }

        String constName = constDecl.getConstName();
        Obj constObj = SymbolTable.find(constName);
        if (constObj == Tab.noObj) {
            //TODO error symbol with that name already exists
            return;
        }

        ConstKind constKind = constDecl.getConstKind();

        int constValue = 0;
        Struct rightSide = Tab.noType;

        if (constKind instanceof ConstNumber) {
            rightSide = Tab.intType;
            constValue = ((ConstNumber) constKind).getNumValue();

        } else if (constKind instanceof ConstChar) {
            rightSide = Tab.charType;
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

        if (currentTypeObj == Tab.noObj) {
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
        nextEnumValue = 0;
    }

    public void visit(EnumStart enumStart) {
        SymbolTable.insert(Obj.Type, enumStart.getEName(), Tab.intType); //TODO enum type
        SymbolTable.openScope();
        nextEnumValue = 0;
    }

    //paziti na enum{1, 2, 3, 2, 4, 5} !
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

        SymbolTable.insert(Obj.Con, enumName, Tab.intType);
    }

    //--------------------METHODS----------------------------------------------

    public void visit(MethodDecl methodDecl) {

        if (currentMethod == Tab.noObj) {
            //TODO report error currentMethod not set
            return;
        }

        SymbolTable.chainLocalSymbols(currentMethod);

        String methodName = currentMethod.getName();
        if (methodName.equals("main") && !isMainCorrect()) {
            //TODO report error main has wrong signature
        }

        if (!hasReturnExpr && currentMethod.getType() != Tab.noType) {
            //TODO report error function which returns value has no return expr
            hasReturnExpr = false;
        }

        currentMethod = Tab.noObj;
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
            type = Tab.noType;
        } else {
            type = currentTypeObj.getType();
        }

        if (methodName.equals("main"))
            isMainDefined = true;

        currentMethod = Tab.insert(Obj.Meth, methodName, type);
        SymbolTable.openScope();

        currentMethod.setLevel(0); //set number of parameters to 0 before parameters list
    }

    //this can't be done in method declaration because params are needed for recursion
    public void visit(FormParams formParams) {
        currentMethod.setLocals(SymbolTable.currentScope().getLocals());
    }

    public void visit(FormPara formPara) {
        String formParaName = formPara.getParamName();

        if (currentTypeObj == Tab.noObj) {
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
