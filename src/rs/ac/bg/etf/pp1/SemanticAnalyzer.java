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

    private boolean isMethodValid;

    private static final int MAX_LOCAL_VARIABLES = 256;
    private static final int MAX_GLOBAL_VARIABLES = 65536;

    private static int local_variables_count = 0;
    private static int global_variables_count = 0;

    private Logger logger = Logger.getLogger(getClass());
    //----------------ERROR REPORT---------------------------------------------

    private void reportError(String message, SyntaxNode node) {
        errorsInCode = true;
        print_error(message, node);
    }

    public static boolean isErrorsInCode() {
        return errorsInCode;
    }

    //----------------ERROR REPORT---------------------------------------------

    private void print_error(String message, SyntaxNode node) {
        StringBuilder builder = new StringBuilder(message);
        int line = (node == null) ? 0 : node.getLine();
        if (line != 0) {
            builder.append(" on line ").append(line);
        }
        logger.error(builder.toString());
    }

    private void print_info(String message, SyntaxNode node) {
        StringBuilder builder = new StringBuilder(message);
        int line = (node == null) ? 0 : node.getLine();
        if (line != 0) {
            builder.append(" on line ").append(line);
        }
        logger.info(builder.toString());
    }

    //----------------CONSTRUCTOR----------------------------------------------

    public SemanticAnalyzer() {
        SymbolTable.init();
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
        for (Integer enumValue : enumValues) {
            if (value == enumValue) return false;
        }
        return true;
    }


    private boolean isMainCorrect() {
        return currentMethod.getLevel() == 0 && currentMethod.getType() == SymbolTable.noType;
    }

    private int getNumberConstValue(NumberConst numberConst) {
        if (numberConst instanceof JustNumber) return ((JustNumber) numberConst).getValue();
        else if (numberConst instanceof PositiveNumber) return ((PositiveNumber) numberConst).getValue();
        else return -1 * ((NegativeNumber) numberConst).getValue();
    }

    //--------------------VISIT METHODS----------------------------------------

    //--------------------PROGRAM----------------------------------------------

    @Override
    public void visit(Program program) {
        if (!isMainDefined) {
            reportError("main method is not defined", program);
        }

        if (ifLevel != 0) {
            reportError("If level is not 0 at the end of program", program);
        }

        if (forLevel != 0) {
            reportError("For level is not 0 at the end of program", program);
        }

        if (global_variables_count > MAX_GLOBAL_VARIABLES) {
            reportError("Program has more than 65535 variables", program);
        }

        SymbolTable.chainLocalSymbols(program.getProgName().obj);
        SymbolTable.closeScope();

        print_info("Program visit", program);
    }

    @Override
    public void visit(ProgName progName) {
        progName.obj = SymbolTable.insert(Obj.Prog, progName.getPName(), SymbolTable.noType);
        SymbolTable.openScope();

        print_info("ProgName visit", progName);
    }

    //-----------------------Type----------------------------------------------

    @Override
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
        type.struct = foundObj.getType();

        print_info("Type visit", type);
    }

    //----------------------CONST----------------------------------------------

    @Override
    public void visit(ConstDecl constDecl) {
        if (currentTypeObj == SymbolTable.noObj) {
            reportError("Const type is not correct", constDecl);

            return;
        }

        String constName = constDecl.getConstName();
        Obj constObj = SymbolTable.find(constName);
        if (constObj != SymbolTable.noObj) {
            reportError("Symbol used for const name is already defined", constDecl);
            return;
        }

        ConstKind constKind = constDecl.getConstKind();

        int constValue = 0;
        Struct rightSide;

        if (constKind instanceof ConstNumber) {
            rightSide = SymbolTable.intType;
            constValue = getNumberConstValue(((ConstNumber) constKind).getNumberConst());

        } else if (constKind instanceof ConstChar) {
            rightSide = SymbolTable.charType;
            constValue = ((ConstChar) constKind).getCharValue();

        } else if (constKind instanceof ConstBool) {
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
        } else {
            reportError("Const init value has wrong type", constDecl);
        }

        print_info("ConstDecl visit", constDecl);
    }

    //-----------------------VARIABLE------------------------------------------

    @Override
    public void visit(VarDeclaration varDecl) {

        if (currentTypeObj == SymbolTable.noObj) {
            reportError("Variable type is not correct", varDecl); //bag from type
            return;
        }

        String varName = varDecl.getVarName();
        if (isDefinedInCurrentScope(varName) || isMethodAndVarWithSameName(varName)) {
            reportError("Symbol used for variable name is already defined in this scope", varDecl);
            return;
        }

        ArrOpt arrOpt = varDecl.getArrOpt();
        if (arrOpt instanceof ArrayOption) {
            SymbolTable.insert(Obj.Var, varName, new Struct(Struct.Array, currentTypeObj.getType()));
        }
        else {
            SymbolTable.insert(Obj.Var, varName, currentTypeObj.getType());
        }

        if (currentMethod != SymbolTable.noObj) global_variables_count++;
        else local_variables_count++;

        print_info("VarDecl visit", varDecl);
    }


    //-----------------------ENUM----------------------------------------------

    @Override
    public void visit(EnumDecl enumDecl) {
        SymbolTable.chainLocalSymbols(enumDecl.getEnumStart().obj);
        SymbolTable.closeScope();

        print_info("EnumDecl visit", enumDecl);
    }

    @Override
    public void visit(EnumStart enumStart) {
        enumStart.obj = SymbolTable.insert(Obj.Type, enumStart.getEName(), SymbolTable.enumType);
        SymbolTable.openScope();
        nextEnumValue = 0;
        enumValues.clear();

        print_info("EnumStart visit", enumStart);
    }

    @Override
    public void visit(EnumMember enumMember) {
        String enumName = enumMember.getEnumMemberName();
        if (isDefinedInCurrentScope(enumName) || isEnumAndVarWithSameName(enumName)) {
            reportError("Symbol used for enum const name is already defined", enumMember);
            return;
        }

        int value = nextEnumValue++;
        EnumInit enumInit = enumMember.getEnumInit();
        if (enumInit instanceof SingleEnumInit) {
            value = getNumberConstValue(((SingleEnumInit)enumInit).getNumberConst());
            nextEnumValue = value + 1;
        }

        if (checkIfEnumValueIsUnique(value)) {
            Obj newEnum = SymbolTable.insert(Obj.Con, enumName, SymbolTable.intType);
            newEnum.setAdr(value);
            enumValues.add(value);
        } else {
            reportError("Two enum constants have same value", enumMember);
        }

        print_info("EnumMember visit", enumMember);
    }

    //--------------------METHODS----------------------------------------------

    @Override
    public void visit(MethodDecl methodDecl) {

        if (currentMethod == SymbolTable.noObj) {
            reportError("Method matched, but method name is not set", methodDecl);
            return;
        }

        if (isMethodValid) SymbolTable.chainLocalSymbols(currentMethod);

        String methodName = currentMethod.getName();
        if (methodName.equals("main") && !isMainCorrect()) {
            reportError("Method main has wrong signature", methodDecl);
        }

        if (!hasReturnExpr && (currentMethod.getType() != SymbolTable.noType)) {
            reportError("Non void function has no return statement or no expression in return statement", methodDecl);
        }

        currentMethod = SymbolTable.noObj;
        SymbolTable.closeScope();

        if (local_variables_count > MAX_LOCAL_VARIABLES) {
            reportError("Method have more than 256 variables", methodDecl);
        }
        local_variables_count = 0;

        print_info("MethodDecl visit", methodDecl);
    }

    @Override
    public void visit(MethodStart methodStart) {
        String methodName = methodStart.getMethodName();
        isMethodValid = true;

        if (isSymbolDefined(methodName)) {
            reportError("Symbol used for method name is already defined in this scope", methodStart);
            isMethodValid = false;
        }

        RetType retType = methodStart.getRetType();
        Struct type;

        if (retType instanceof RetVoid) {
            type = SymbolTable.noType;
        } else {
            type = currentTypeObj.getType();
        }

        if (methodName.equals("main")) {
            if (isMainDefined) {
                reportError("Multiple definitions of main method", methodStart);
                isMethodValid = false;
            }
            isMainDefined = true;
        }

        if (isMethodValid) {
            methodStart.obj = currentMethod = SymbolTable.insert(Obj.Meth, methodName, type);
        } else {
            methodStart.obj = currentMethod = new Obj(Obj.Meth, methodName, type);
        }
        SymbolTable.openScope();

        currentMethod.setLevel(0); //set number of parameters to 0 before parameters list
        hasReturnExpr = false;

        print_info("MethodStart visit", methodStart);
    }

    //this can't be done in method declaration because params are needed for recursion
    @Override
    public void visit(FormParams formParams) {
        currentMethod.setLocals(SymbolTable.currentScope().getLocals());

        print_info("FormParams visit", formParams);
    }

    @Override
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

        print_info("FormPara visit", formPara);
    }

    //-----------------------STATEMENTS----------------------------------------

    @Override
    public void visit(ContinueStatement continueStatement) {
        if (forLevel == 0) {
            reportError("Continue statement found outside of loop", continueStatement);
        }

        print_info("ContinueStatement visit", continueStatement);
    }

    @Override
    public void visit(BreakStatement breakStatement) {
        if (forLevel == 0) {
            reportError("Break statement found outside of loop", breakStatement);
        }

        print_info("BreakStatement visit", breakStatement);
    }

    @Override
    public void visit(ReturnExpStatement returnExpStatement) {
        if (currentMethod == SymbolTable.noObj) {
            reportError("Return statement found, but not in function", returnExpStatement);
            return;
        }

        hasReturnExpr = true;

        Struct retType = returnExpStatement.getExpr().struct;

        Struct methodType = currentMethod.getType();

        if (retType != methodType) {
            reportError("Return type is wrong", returnExpStatement);
        }

        print_info("ReturnExpStatement visit", returnExpStatement);
    }

    @Override
    public void visit(ReadStatement readStatement) {
        Obj designatorObj = readStatement.getDesignator().obj;
        Struct type = designatorObj.getType();

        if (type != SymbolTable.intType && type != SymbolTable.charType && type != SymbolTable.boolType) {
            reportError("Read parameter not a basic type", readStatement);
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Read parameter is not a variable", readStatement);
        }

        print_info("ReadStatement visit", readStatement);
    }

    @Override
    public void visit(PrintStatement printStatement) {
        ExpressionToPrint ex = printStatement.getExpressionToPrint();
        Struct type;

        if (ex instanceof ExpressionAndConstToPrint) {
            type = ((ExpressionAndConstToPrint) ex).getExpr().struct;
        }
        else type = ((OnlyExpressionToPrint) ex).getExpr().struct;

        if (type != SymbolTable.intType && type != SymbolTable.charType && type != SymbolTable.boolType) {
            reportError("Print expression is not a basic type", printStatement);
        }

        print_info("PrintStatement visit", printStatement);
    }

    @Override
    public void visit(IfStart ifStart) {
        ifLevel++;
        print_info("IfStart visit", ifStart);
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifLevel--;

        if (ifLevel < 0) {
            reportError("IfLevel error", ifStatement);
        }

        print_info("IfStatement visit", ifStatement);
    }

    @Override
    public void visit(IfElseStatement ifElseStatement) {
        ifLevel--;

        if (ifLevel < 0) {
            reportError("IfLevel error", ifElseStatement);
        }
        print_info("IfElseStatement visit", ifElseStatement);
    }

    @Override
    public void visit(ForStart forStart) {

        forLevel++;

        print_info("ForStart visit", forStart);
    }

    @Override
    public void visit(ForStatement forStatement) {
        forLevel--;

        if (forLevel < 0) {
            reportError("IfLevel error", forStatement);
        }

        print_info("ForStatement visit", forStatement);
    }

    //-----------------------DESIGNATOR STATEMENTS-----------------------------

    @Override
    public void visit(AssignDesignatorStatement assignDesignatorStatement) {
        Obj designatorObj = assignDesignatorStatement.getDesignator().obj;

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", assignDesignatorStatement);
            return;
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Left side is not a variable", assignDesignatorStatement);
        }

        Struct leftSideType = designatorObj.getType();
        Struct rightSideType = assignDesignatorStatement.getExpr().struct;

        if (!leftSideType.assignableTo(rightSideType)) {
            reportError("Right side can't be assigned to left", assignDesignatorStatement);
        }


        print_info("AssignDesignatorStatement visit", assignDesignatorStatement);
    }

    @Override
    public void visit(MethodCallStatement methodCallStatement) {
        //done in MethodCall visit
    }

    @Override
    public void visit(IncDesignatorStatement incDesignatorStatement) {
        Obj designatorObj = incDesignatorStatement.getDesignator().obj;

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", incDesignatorStatement);
            return;
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Increment operator used on non variable symbol", incDesignatorStatement);
        }

        Struct type = designatorObj.getType();

        if (!type.compatibleWith(SymbolTable.intType)) {
            reportError("Increment operator used on non integer type", incDesignatorStatement);
        }

        print_info("IncDesignatorStatement visit", incDesignatorStatement);
    }

    @Override
    public void visit(DecDesignatorStatement decDesignatorStatement) {
        Obj designatorObj = decDesignatorStatement.getDesignator().obj;

        if (designatorObj == SymbolTable.noObj) {
            reportError("Error with designator", decDesignatorStatement);
            return;
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Decrement operator used on non variable symbol", decDesignatorStatement);
        }

        Struct type = designatorObj.getType();

        if (!type.compatibleWith(SymbolTable.intType)) {
            reportError("Decrement operator used on non integer type", decDesignatorStatement);
        }

        print_info("DecDesignatorStatement visit", decDesignatorStatement);
    }

    //-----------------------DESIGNATORS---------------------------------------

    @Override
    public void visit(EnumDesignator enumDesignator) {
        String name = enumDesignator.getDesignatorName();

        Obj designatorObj = SymbolTable.find(name);

        if (designatorObj.getKind() != Obj.Type) {
            reportError("Symbol used for enum name is not enum type", enumDesignator);
            enumDesignator.obj = SymbolTable.noObj;
            return;
        }

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

        print_info("EnumDesignator visit", enumDesignator);
    }

    @Override
    public void visit(ArrayName arrayName) {
        String name = arrayName.getDesignatorName();
        arrayName.obj = SymbolTable.find(name);
    }

    @Override
    public void visit(ArrayDesignator arrayDesignator) {
        arrayDesignator.obj = SymbolTable.noObj;

        Obj designatorObj = arrayDesignator.getArrayName().obj;
        if (designatorObj == SymbolTable.noObj) {
            reportError("Symbol used but never defined", arrayDesignator);
            return;
        }

        if (designatorObj.getKind() != Obj.Var) {
            reportError("Symbol in array place is not variable", arrayDesignator);
            return;
        }

        if (!arrayDesignator.getExpr().struct.compatibleWith(SymbolTable.intType)) {
            reportError("Expression in brackets can not be converted to integer type", arrayDesignator);
            return;
        }

        if (designatorObj.getType().getKind() != Struct.Array) {
            reportError("Non array symbol used as array name", arrayDesignator);
            return;
        }

        arrayDesignator.obj = new Obj(Obj.Elem, designatorObj.getName(), designatorObj.getType().getElemType());

        print_info("ArrayDesignator visit", arrayDesignator);
    }

    @Override
    public void visit(SimpleDesignator simpleDesignator) {
        String name = simpleDesignator.getDesignatorName();

        simpleDesignator.obj = SymbolTable.find(name);
        if (simpleDesignator.obj == SymbolTable.noObj) {
            reportError("Symbol used but never defined", simpleDesignator);
            simpleDesignator.obj = SymbolTable.noObj;
        }
        print_info("SimpleDesignator", simpleDesignator);
    }

    //-----------------------ACTUAL PARAMETERS---------------------------------

    @Override
    public void visit(MethodCallStart methodCallStart) {
        methodCallStart.obj = methodCallStart.getDesignator().obj;

        actualParametersBuffer.createParametersList();

        print_info("MethodCallStart visit", methodCallStart);
    }

    @Override
    public void visit(ActualParametar actualParametar) {
        Expr expr = actualParametar.getExpr();
        Struct type = expr.struct;

        if (type == null) {
            reportError("Type of actual parameter is null", actualParametar);
            return;
        }

        if (!actualParametersBuffer.insertActualParameter(type)) {
            reportError("Actual Parameter Processing but method start not found", actualParametar);
        }

        print_info("ActualParametar", actualParametar);
    }

    @Override
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

        methodCall.obj = designatorObj;

        List<Struct> actualParametersList = actualParametersBuffer.getParameters();

        if (actualParametersList == null) {
            reportError("Method call processing but method start not found", methodCall);
            return;
        }

        int formalParsNumber = designatorObj.getLevel();

        if (actualParametersList.size() != formalParsNumber) {
            reportError("Wrong number of parameters", methodCall);
            return;
        }

        Collection<Obj> formalParameters = designatorObj.getLocalSymbols();
        int i = 0;
        for (Obj currentParameter: formalParameters) {
            if (i >= formalParsNumber) break;

            Struct type = currentParameter.getType();
            if (!actualParametersList.get(i).assignableTo(type)) {
                reportError("Actual parameter at position " + (i + 1) + " has wrong type", methodCall);
            }
            i++;
        }

        print_info("MethodCall visit", methodCall);
    }

    //---------------------CONDITIONS------------------------------------------

    @Override
    public void visit(ConditionFactor conditionFactor) {
        Struct type = conditionFactor.getExpr().struct;
        if (type.getKind() == Struct.Array) {
            RelOp relOp = conditionFactor.getRelOp();
            if (!(relOp instanceof Equal) && !(relOp instanceof Unequal)) {
                reportError("Illegal operand used on array in condition", conditionFactor);
                conditionFactor.struct = SymbolTable.boolType;
                return;
            }
        }
        if (!type.compatibleWith(conditionFactor.getCondFact().struct)) {
            reportError("Incompatible types in condition check", conditionFactor);
        }
        conditionFactor.struct = SymbolTable.boolType;

        print_info("ConditionFactor visit", conditionFactor);
    }

    @Override
    public void visit(SingleCondFact singleCondFact) {
        singleCondFact.struct = singleCondFact.getExpr().struct;

        print_info("SingleCondFact visit", singleCondFact);
    }

    @Override
    public void visit(SingleCondFactTerm singleCondFactTerm) {
        if (singleCondFactTerm.getCondFact().struct != SymbolTable.boolType) {
            reportError("Condition terminal is not boolean", singleCondFactTerm);
        }
        singleCondFactTerm.struct = SymbolTable.boolType;

        print_info("SingleCondFactTerm visit", singleCondFactTerm);
    }

    @Override
    public void visit(CondFactListTerm condFactListTerm) {
        Struct leftType = condFactListTerm.getCondTerm().struct;
        Struct rightType = condFactListTerm.getCondFact().struct;
        if ((leftType != SymbolTable.boolType) || (rightType != SymbolTable.boolType)) {
            reportError("One of the condition factors in AND operator is not bool type", condFactListTerm);
        }
        condFactListTerm.struct = SymbolTable.boolType;

        print_info("CondFactListTerm visit", condFactListTerm);
    }

    @Override
    public void visit(SingleTermCondition singleTermCondition) {
        singleTermCondition.struct = singleTermCondition.getCondTerm().struct;

        print_info("SingleTermCondition visit", singleTermCondition);
    }

    @Override
    public void visit(TermListCondition termListCondition) {
        Struct leftType = termListCondition.getConditionDecl().struct;
        Struct rightType = termListCondition.getCondTerm().struct;
        if ((leftType != SymbolTable.boolType) || (rightType != SymbolTable.boolType)) {
            reportError("One of the condition factors in OR operator is not bool type", termListCondition);
        }
        termListCondition.struct = SymbolTable.boolType;

        print_info("TermListCondition visit", termListCondition);
    }

    //---------------------EXPRESSIONS-----------------------------------------

    @Override
    public void visit(TermListExpr termListExpr) {
        Struct leftType = termListExpr.getExpr().struct;
        Struct rightType = termListExpr.getTerm().struct;

        if ((leftType != SymbolTable.intType) || (rightType != SymbolTable.intType)) {
            reportError("One of the condition factors in MulOp operator is not int type", termListExpr);
        }
        termListExpr.struct = SymbolTable.intType;

        print_info("TermListExpr visit", termListExpr);
    }

    @Override
    public void visit(SingleTermExpr singleTermExpr) {
        singleTermExpr.struct = singleTermExpr.getSignTerm().struct;

        print_info("SingleTermExpr visit", singleTermExpr);
    }

    @Override
    public void visit(MinusTerm minusTerm) {
        if (minusTerm.getTerm().struct != SymbolTable.intType) {
            reportError("Minus operator used on non int term", minusTerm);
        }
        minusTerm.struct = SymbolTable.intType;

        print_info("MinusTerm visit", minusTerm);
    }

    @Override
    public void visit(NoSignTerm noSignTerm) {
        noSignTerm.struct = noSignTerm.getTerm().struct;

        print_info("NoSignTerm visit", noSignTerm);
    }

    @Override
    public void visit(FactorListTerm factorListTerm) {
        Struct leftType = factorListTerm.getTerm().struct;
        Struct rightType = factorListTerm.getFactor().struct;

        if ((leftType != SymbolTable.intType) || (rightType != SymbolTable.intType)) {
            reportError("One of the condition factors in MulOp operator is not int type", factorListTerm);
        }
        factorListTerm.struct = SymbolTable.intType;

        print_info("FactorListTerm visit", factorListTerm);
    }

    @Override
    public void visit(SingleFactorTerm singleFactorTerm) {
        singleFactorTerm.struct = singleFactorTerm.getFactor().struct;

        print_info("SingleFactorTerm visit", singleFactorTerm);
    }

    @Override
    public void visit(ParenFactor parenFactor) {
        parenFactor.struct = parenFactor.getExpr().struct;

        print_info("ParenFactor visit", parenFactor);
    }

    @Override
    public void visit(NumberFactor numberFactor) {
        numberFactor.struct = SymbolTable.intType;

        print_info("NumberFactor visit", numberFactor);
    }

    @Override
    public void visit(CharFactor charFactor) {
        charFactor.struct = SymbolTable.charType;

        print_info("CharFactor visit", charFactor);
    }

    @Override
    public void visit(BoolFactor boolFactor) {
        boolFactor.struct = SymbolTable.boolType;

        print_info("BoolFactor visit", boolFactor);
    }

    @Override
    public void visit(NewTypeFactor newTypeFactor) {
        newTypeFactor.struct = new Struct(Struct.Array, newTypeFactor.getType().struct);
        if (newTypeFactor.getExpr().struct != SymbolTable.intType) {
            reportError("Expression in NEW statement is not int", newTypeFactor);
        }

        print_info("NewTypeFactor visit", newTypeFactor);
    }

    @Override
    public void visit(DesignatorFactor designatorFactor) {
        designatorFactor.struct = designatorFactor.getDesignator().obj.getType();

        print_info("DesignatorFactor visit", designatorFactor);
    }

    @Override
    public void visit(FunCallFactor funCallFactor) {
        funCallFactor.struct = funCallFactor.getMethodCall().getMethodCallStart().getDesignator().obj.getType();

        print_info("FunCallFactor", funCallFactor);
    }

}
