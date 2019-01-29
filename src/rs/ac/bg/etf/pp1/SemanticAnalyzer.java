package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SemanticAnalyzer extends VisitorAdaptor {

    private static Obj currentTypeObj = SymbolTable.noObj;
    private static Obj currentMethod = SymbolTable.noObj;

    private List<Integer> enumValues = new ArrayList<>();
    private static int nextEnumValue = 0;
    private static Obj currentEnum = SymbolTable.noObj;

    private static boolean hasReturnExpr = false;
    private static boolean isMainDefined = false;
    private static boolean errorsInCode = false;

    private ActualParametersBuffer actualParametersBuffer = new ActualParametersBuffer();

    private static int ifLevel = 0;
    private static int forLevel = 0;

    private Logger logger = Logger.getLogger(getClass());
    //----------------ERROR REPORT---------------------------------------------

    void reportError(String message, SyntaxNode node) {
        errorsInCode = true;
        print_error(message, node);
    }

    public static boolean isErrorsInCode() {
        return errorsInCode;
    }

    //----------------ERROR REPORT---------------------------------------------

    void print_error(String message, SyntaxNode node) {
        StringBuilder builder = new StringBuilder(message);
        int line = (node == null) ? 0 : node.getLine();
        if (line != 0) {
            builder.append(" on line " + line);
        }
        logger.error(builder.toString());
    }

    void print_info(String message, SyntaxNode node) {
        StringBuilder builder = new StringBuilder(message);
        int line = (node == null) ? 0 : node.getLine();
        if (line != 0) {
            builder.append(" on line " + line);
        }
        logger.info(builder.toString());
    }

    //----------------HELP METHODS---------------------------------------------

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
        if (!isMainDefined) {
            reportError("main method is not defined", program);
        }

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

        if (!isSymbolDefined(typeName)) {
            reportError("Type not recognized", type);
            currentTypeObj = SymbolTable.noObj;
            return;
        }

        Obj foundObj = SymbolTable.find(typeName);
        if (foundObj.getKind() == Obj.Type) {
            currentTypeObj = foundObj;
        } else {
            reportError("Identifier is not a type", type);
            currentTypeObj = SymbolTable.noObj;
        }
    }

    //----------------------CONST----------------------------------------------

    public void visit(ConstDecl constDecl) {
        if (currentTypeObj == SymbolTable.noObj) {
            reportError("Const type is not correct", constDecl);

            return;
        }

        String constName = constDecl.getConstName();
        Obj constObj = SymbolTable.find(constName);
        if (constObj == SymbolTable.noObj) {
            reportError("Symbol used for const name is already defined", constDecl);
            return;
        }

        ConstKind constKind = constDecl.getConstKind();

        int constValue = 0;
        Struct rightSide;

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
            reportError("Const type is wrong", constDecl);
            return;
        }

        if (currentTypeObj.getType().assignableTo(rightSide)) {
            Obj newConst = SymbolTable.insert(Obj.Con, constName, rightSide);
            newConst.setAdr(constValue); //init const
        }

    }

    //-----------------------VARIABLE------------------------------------------

    public void visit(VarDecl varDecl) {

        if (currentTypeObj == SymbolTable.noObj) {
            reportError("Variable type is not correct", varDecl);
            return;
        }

        if (varDecl instanceof ErrorVarDecl) {
            return; //syntax error, no need for semantic processing
        }

        VarDeclaration varDeclaration = (VarDeclaration) varDecl;

        String varName = varDeclaration.getVarName();
        if (isDefinedInCurrentScope(varName) || isMethodAndVarWithSameName(varName)) {
            reportError("Symbol used for variable name is already defined in this scope", varDecl);
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
            reportError("Symbol used for enum const name is already defined", enumMember);
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
            reportError("Two enum constants have same value", enumMember);
        }
    }

    //--------------------METHODS----------------------------------------------

    public void visit(MethodDecl methodDecl) {

        if (currentMethod == SymbolTable.noObj) {
            reportError("Method matched, but method name is not set", methodDecl);
            return;
        }

        SymbolTable.chainLocalSymbols(currentMethod);

        String methodName = currentMethod.getName();
        if (methodName.equals("main") && !isMainCorrect()) {
            reportError("Method main has wrong signature", methodDecl);
        }

        if (!hasReturnExpr && currentMethod.getType() != SymbolTable.noType) {
            reportError("Non void function has no return statement or no expression in return statement", methodDecl);
        }

        currentMethod = SymbolTable.noObj;
        SymbolTable.closeScope();
    }

    public void visit(MethodStart methodStart) {
        String methodName = methodStart.getMethodName();

        if (isSymbolDefined(methodName)) {
            reportError("Symbol used for enum name is already defined", methodStart);
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
        hasReturnExpr = false;
    }

    //this can't be done in method declaration because params are needed for recursion
    public void visit(FormParams formParams) {
        currentMethod.setLocals(SymbolTable.currentScope().getLocals());
    }

    public void visit(FormPara formPara) {
        String formParaName = formPara.getParamName();

        if (currentTypeObj == SymbolTable.noObj) {
            reportError("Type used for formal argument is not correct", formPara);
            return;
        }

        if (isDefinedInCurrentScope(formParaName)) {
            reportError("Symbol used for formal argument name is already defined in current scope", formPara);
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

    public void visit(ContinueStatement continueStatement) {
        if (forLevel == 0) {
            reportError("Continue statement found outside of loop", continueStatement);
        }
    }

    public void visit(BreakStatement breakStatement) {
        if (forLevel == 0) {
            reportError("Break statement found outside of loop", breakStatement);
        }
    }

    public void visit(ReturnExpStatement returnExpStatement) {
        hasReturnExpr = true;

        //TODO check the type
    }

    public void visit(ReadStatement readStatement) {
        //has to be variable
    }

    public void printStatement(PrintStatement printStatement) {

    }

    //if and for should change in grammar

    //-----------------------DESIGNATOR STATEMENTS-----------------------------

    public void visit(AssignDesignatorStatement assignDesignatorStatement) {
        Obj designatorObj = assignDesignatorStatement.getDesignator().obj;

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", assignDesignatorStatement);
            return;
        }

        Struct leftSideType = designatorObj.getType();
        Struct rightSideType = assignDesignatorStatement.getExpr().struct;
        if (!leftSideType.assignableTo(rightSideType)) {
            reportError("Right side can't be assigned to left", assignDesignatorStatement);
        }

    }

    public void visit(MethodCallStatement methodCallStatement) {
        //done in MethodCall visit
    }

    public void visit(IncDesignatorStatement incDesignatorStatement) {
        Obj designatorObj = incDesignatorStatement.getDesignator().obj;

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", incDesignatorStatement);
            return;
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Increment operator used on non variable symbol", incDesignatorStatement);
        }

        if (designatorObj.getType().compatibleWith(SymbolTable.intType)) {
            reportError("Increment operator used on non integer type", incDesignatorStatement);
        }

    }

    public void visit(DecDesignatorStatement decDesignatorStatement) {
        Obj designatorObj = decDesignatorStatement.getDesignator().obj;

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", decDesignatorStatement);
            return;
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Decrement operator used on non variable symbol", decDesignatorStatement);
        }

        if (designatorObj.getType().compatibleWith(SymbolTable.intType)) {
            reportError("Decrement operator used on non integer type", decDesignatorStatement);
        }
    }

    //-----------------------DESIGNATORS---------------------------------------

    public void visit(EnumDesignator enumDesignator) {
        String name = enumDesignator.getDesignatorName();

        Obj designatorObj = SymbolTable.find(name);
        if (designatorObj == SymbolTable.noObj) {
            reportError("Symbol used but never defined", enumDesignator);
        }

        if (designatorObj.getKind() != Obj.Type) {
            reportError("Symbol matched as enum is not a type kind", enumDesignator);
        }

        String enumMemberName = enumDesignator.getEnumMember();
        Collection<Obj> enumMembers = designatorObj.getLocalSymbols();
        boolean isEnumMember = false;
        for (Obj currentMember: enumMembers) {
            if (enumMemberName.equals(currentMember.getName())) {
                isEnumMember = true;
                enumDesignator.obj = currentMember;
                break;
            }
        }

        if (!isEnumMember) {
            reportError("Enum does not have member searched for", enumDesignator);
            enumDesignator.obj = SymbolTable.noObj;
        }

    }

    public void visit(ArrayDesignator arrayDesignator) {
        String name = arrayDesignator.getDesignatorName();

        arrayDesignator.obj = SymbolTable.noObj;

        Obj designatorObj = SymbolTable.find(name);
        if (designatorObj == SymbolTable.noObj) {
            reportError("Symbol used but never defined", arrayDesignator);
            return;
        }

        if (designatorObj.getKind() != Obj.Var && designatorObj.getKind() != Obj.Con) {
            reportError("Symbol in array place is neither variable nor constant", arrayDesignator);
            return;
        }

        if (!arrayDesignator.getExpr().struct.compatibleWith(SymbolTable.intType)) {
            reportError("Expression in brackets can not be converted to integer type", arrayDesignator);
            return;
        }

        arrayDesignator.obj = designatorObj;
    }

    public void visit(SimpleDesignator simpleDesignator) {
        String name = simpleDesignator.getDesignatorName();

        simpleDesignator.obj = SymbolTable.find(name);
        if (simpleDesignator.obj == SymbolTable.noObj) {
            reportError("Symbol used but never defined", simpleDesignator);
            simpleDesignator.obj = SymbolTable.noObj;
        }
    }


    //-----------------------ACTUAL PARAMETERS---------------------------------

    public void visit(MethodCallStart methodCallStart) {
        methodCallStart.obj = methodCallStart.getDesignator().obj;

        actualParametersBuffer.createParametersList();
    }

    public void visit(ActualParametar actualParametar) {
        Expr expr = actualParametar.getExpr();
        Struct type = expr.struct;

        if (!actualParametersBuffer.insertActualParameter(type)) {
            reportError("Actual Parameter Processing but method start not found", actualParametar);
        }
    }

    public void visit(MethodCall methodCall) {
        Designator designator = methodCall.getMethodCallStart().getDesignator();
        Obj designatorObj = designator.obj;

        if (!(designator instanceof SimpleDesignator)) {
            reportError("Error method call on enum or array", methodCall);
            return;
        }

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", methodCall);
            return;
        }

        if (designatorObj.getKind() != Obj.Meth) {
            reportError("Designator is not declared as a method", methodCall);
            return;
        }

        List<Struct> actualParametersList = actualParametersBuffer.getParameters();

        if (actualParametersList == null) {
            reportError("Method call processing but method start not found", methodCall);
        }

        int formalParsNumber = designatorObj.getLevel();

        if (actualParametersList.size() != formalParsNumber) {
            reportError("Wrong number of parameters", methodCall);
        }

        Collection<Obj> formalParameters = designatorObj.getLocalSymbols();
        int i = 0;
        for (Obj currentParameter: formalParameters) {
            if (i >= formalParsNumber) break;

            Struct type = currentParameter.getType();
            if (!actualParametersList.get(i).assignableTo(type)) {
                reportError("Actual parameter at position " + i + " has wrong type", methodCall);
            }
            i++;
        }
    }

}
