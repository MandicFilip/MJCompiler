package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPC;

    private static final int trueValue = 1;
    private static final int falseValue = 0;

    private static final int TWO_JMP_OFFSET_SIZE = 6;
    private static final int TWO_JMP_OFFSET_CONST_SIZE = 7;
    private static final int JMP_OFFSET_CONST_SIZE =4;

    private static final int MAX_CODE_SIZE = 8192;

    private JumpAddressStack jumpAddressStack = new JumpAddressStack();

    private boolean isIncDecArrayElem = false;

    private Logger logger = Logger.getLogger(getClass());

    public int getMainPC() {
        return mainPC;
    }

    //IN SEMANTIC ANALYZER
    //for enum designators designator.obj is set enum.member obj
    //for array designators there is a dummy obj with same name, kind and element type
    //for simple designators there is obj from symbol table
    private void loadDesignator(Designator designator) {
        if (designator instanceof EnumDesignator) {
            Obj enumObj = designator.obj;
            Code.load(enumObj);

        } else if (designator instanceof ArrayDesignator) {
            Obj arrayObj = designator.obj;
            Struct type = arrayObj.getType();

            if (isIncDecArrayElem) {
                Code.put(Code.dup2);
            }

            if (type == SymbolTable.intType) {
                Code.put(Code.aload);
            } else {
                Code.put(Code.baload);
            }
        } else if (designator instanceof SimpleDesignator) {
            Obj designatorObj = designator.obj;
            Code.load(designatorObj);
        }
    }

    private void storeDesignator(Designator designator) {
        if (designator instanceof ArrayDesignator) {
            Obj arrayObj = designator.obj;
            Struct type = arrayObj.getType();

            if (type == SymbolTable.intType) {
                Code.put(Code.astore);
            } else {
                Code.put(Code.bastore);
            }
        } else if (designator instanceof SimpleDesignator) {
            Obj designatorObj = designator.obj;
            Code.store(designatorObj);
        }
        //no case for enum, enum members are const
    }

    private int getRelOpCode(RelOp relOp) {
        if (relOp instanceof Equal) return Code.eq;
        if (relOp instanceof Unequal) return Code.ne;
        if (relOp instanceof Greater) return Code.gt;
        if (relOp instanceof GreaterEqual) return Code.ge;
        if (relOp instanceof Smaller) return Code.lt;
        return Code.le;
    }

    private boolean checkIfConditionExists(OptCondition optCondition) {
        return optCondition instanceof Condition;
    }

    private boolean isSecondOptStatement(SecondOptDesignator secondOptDesignator) {
        return secondOptDesignator.getOptDesignatorStatement() instanceof SingleDesignatorStatement;
    }

    //-------------------------VISIT METHODS-----------------------------------

    //-------------------------PROGRAM-----------------------------------------

    @Override
    public void visit(ProgName progName) {
        //define global predefined methods
        defineOrdCall();
        defineChrCall();
        defineLenCall();
    }

    private void defineOrdCall() {
        Obj ordObj = SymbolTable.find("ord");
        ordObj.setAdr(Code.pc);

        Code.put(Code.return_);
    }

    private void defineChrCall() {
        Obj chrObj = SymbolTable.find("chr");
        chrObj.setAdr(Code.pc);

        Code.put(Code.return_);
    }

    private void defineLenCall() {
        Obj lenObj = SymbolTable.find("len");
        lenObj.setAdr(Code.pc);

        Code.put(Code.arraylength);
        Code.put(Code.return_);
    }

    @Override
    public void visit(Program Program) {
        super.visit(Program);

        if (Code.pc >= MAX_CODE_SIZE) {
            logger.error("Program is too big. It has to be less than 8192 bytes!");
        }
    }

    //-------------------------METHOD DECLARATION------------------------------

    @Override
    public void visit(MethodStart methodStart) {
        //takes input parameters from stack

        if (methodStart.obj.getName().equals("main")) {
            mainPC = Code.pc;
        }
        methodStart.obj.setAdr(Code.pc);

        int parametersNumber = methodStart.obj.getLevel();
        int localsAndParametersNumber = methodStart.obj.getLocalSymbols().size();

        Code.put(Code.enter);
        Code.put(parametersNumber);
        Code.put(localsAndParametersNumber);

        //leaves the stack without input parameters
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        //takes nothing from expression stack

        Code.put(Code.exit);
        Code.put(Code.return_);

        //leaves stack the same as it was
    }

    @Override
    public void visit(ReturnExpStatement returnExpStatement) {
        //takes nothing from expression stack

        Code.put(Code.exit);
        Code.put(Code.return_);

        //leaves stack the same as it was
    }

    @Override
    public void visit(ReturnNopStatement returnNopStatement) {
        //takes nothing from expression stack

        Code.put(Code.exit);
        Code.put(Code.return_);

        //leaves stack the same as it was
    }

    //-------------------------VISIT FACTOR------------------------------------

    @Override
    public void visit(ArrayName arrayName) {
        //takes nothing from stack

        Code.load(arrayName.obj);

        //leaves array address on stack
    }

    @Override
    public void visit(NumberFactor numberFactor) {
        //takes nothing from stack

        int value = numberFactor.getValueNumber();
        Obj tmp = new Obj(Obj.Con, "", SymbolTable.intType, value, 0);
        Code.load(tmp);

        //leaves const value on stack
    }

    @Override
    public void visit(CharFactor charFactor) {
        //takes nothing from stack

        int value = charFactor.getValueChar();
        Obj tmp = new Obj(Obj.Con, "", SymbolTable.charType, value, 0);
        Code.load(tmp);

        //leaves const value on stack
    }

    @Override
    public void visit(BoolFactor boolFactor) {
        //takes nothing from stack

        int value = 0;
        if (boolFactor.getValBool()) {
            value = 1;
        }
        Obj tmp = new Obj(Obj.Con, "", SymbolTable.charType, value, 0);
        Code.load(tmp);

        //leaves const value on stack
    }

    @Override
    public void visit(NewTypeFactor newTypeFactor) {
        //takes n from stack as expression result

        Struct arrayType = newTypeFactor.getType().struct;

        Code.put(Code.newarray);
        int wordArray = 1;
        if (arrayType == SymbolTable.charType) {
            wordArray = 0;
        }

        Code.loadConst(wordArray);

        //leaves address on stack
    }

    @Override
    public void visit(DesignatorFactor designatorFactor) {
        //may take adr and i from stack if designator is array element, otherwise nothing

        Designator designator = designatorFactor.getDesignator();
        loadDesignator(designator);

        //leaves designator value on stack, clears adr and i if they were on stack
    }

    @Override
    public void visit(FunCallFactor funCallFactor) {
        //nothing to do, return value is already on stack
    }

    @Override
    public void visit(MethodCall methodCall) {
        //takes nothing from stack

        int relativeAddress = methodCall.obj.getAdr() - Code.pc;

        Code.put(Code.call);
        Code.put2(relativeAddress); //call needs short

        //leaves nothing on stack
        //parameters are set in expression pass
    }

    //-------------------------EXPRESSION CALCULATOR---------------------------
    @Override
    public void visit(SingleFactorTerm singleFactorTerm) {
        //already have expression on stack
        //leave expression on stack
    }

    @Override
    public void visit(FactorListTerm factorListTerm) {
        //takes two operands from stack top

        MulOp mulOp = factorListTerm.getMulOp();
        if (mulOp instanceof MulOper) {
            Code.put(Code.mul);

        } else if (mulOp instanceof DivOp) {
            Code.put(Code.div);

        } else {
            Code.put(Code.rem);
        }

        //leaves result on stack
    }

    @Override
    public void visit(NoSignTerm noSignTerm) {
        //already have expression on stack
        //leave expression on stack
    }

    @Override
    public void visit(MinusTerm minusTerm) {
        //takes expression from stack

        Code.loadConst(-1);
        Code.put(Code.mul);

        //leaves result on stack
    }

    @Override
    public void visit(SingleTermExpr singleTermExpr) {
        //already have expression on stack
        //leave expression on stack
    }

    @Override
    public void visit(TermListExpr termListExpr) {
        //takes two operands from stack

        Addop addop = termListExpr.getAddop();
        if (addop instanceof PlusOp) {
            Code.put(Code.add);
        } else {
            Code.put(Code.sub);
        }

        //leaves result on stack
    }

    //-------------------------CONDITION EXPRESSION----------------------------

    @Override
    public void visit(TermListCondition termListCondition) {
        //takes two bool values from stack

        Code.put(Code.add);
        Code.loadConst(0); //value to compare with
        Code.put(Code.jcc + Code.gt);
        Code.put2(TWO_JMP_OFFSET_CONST_SIZE); //offset 8 = loadconst, put, put2 sizes
        Code.loadConst(falseValue);
        Code.put(Code.jmp);
        Code.put2(JMP_OFFSET_CONST_SIZE);
        Code.loadConst(trueValue);

        //leaves bool value on stack
    }

    @Override
    public void visit(SingleTermCondition singleTermCondition) {
        //takes nothing from stack
        //leaves nothing on stack
    }

    @Override
    public void visit(CondFactListTerm condFactListTerm) {
        //takes two bool values from stack

        Code.put(Code.mul);

        //leaves bool value on stack
    }

    @Override
    public void visit(SingleCondFactTerm singleCondFactTerm) {
        //takes nothing from stack
        //leaves nothing on stack
    }

    @Override
    public void visit(ConditionFactor conditionFactor) {
        //takes two expression values from stack
        int relOpCode = getRelOpCode(conditionFactor.getRelOp());

        Code.put(Code.jcc + relOpCode);
        Code.put2(TWO_JMP_OFFSET_CONST_SIZE);  //jmp offset
        Code.loadConst(falseValue);
        Code.put(Code.jmp);
        Code.put2(JMP_OFFSET_CONST_SIZE); //jmp offset
        Code.loadConst(trueValue);

        //leaves bool value on stack
    }

    @Override
    public void visit(SingleCondFact singleCondFact) {
        //takes nothing from stack
        //leaves nothing on stack
    }

    //-------------------------DESIGNATOR STATEMENTS---------------------------

    @Override
    public void visit(AssignDesignatorStatement assignDesignatorStatement) {
        //takes right side value from stack

        Designator designator = assignDesignatorStatement.getDesignator();
        storeDesignator(designator);

        //leaves nothing on stack
    }

    @Override
    public void visit(MethodCallStatement methodCallStatement) {
        //if function is not void, it may get ret value on stack

        Struct retType = methodCallStatement.getMethodCall().obj.getType();
        if (retType != SymbolTable.noType) {
            Code.put(Code.pop);
        }

        //leaves nothing on stack
    }

    public void visit(IncDesignatorStatement incDesignatorStatement) {
        //takes nothing from stack

        Designator designator = incDesignatorStatement.getDesignator();
        isIncDecArrayElem = true;
        loadDesignator(designator);
        isIncDecArrayElem = false;
        Code.loadConst(1);
        Code.put(Code.add);
        storeDesignator(designator);

        //leaves nothing on stack
    }

    @Override
    public void visit(DecDesignatorStatement decDesignatorStatement) {
        //takes nothing from stack

        Designator designator = decDesignatorStatement.getDesignator();
        isIncDecArrayElem = true;
        loadDesignator(designator);
        isIncDecArrayElem = false;
        Code.loadConst(-1);       //Code.put(Code.const_m1);
        Code.put(Code.add);
        storeDesignator(designator);

        //leaves nothing on stack
    }

    //-------------------------------STATEMENTS--------------------------------

    //---------------IF STATEMENTS-------------

    @Override
    public void visit(IfStart ifStart) {
        //takes condition value from stack

        Code.loadConst(trueValue);
        Code.put(Code.jcc + Code.eq);
        Code.put2(TWO_JMP_OFFSET_SIZE);
        Code.put(Code.jmp);

        int addressToPatch = Code.pc;
        jumpAddressStack.pushIfConditionAddressToPatch(addressToPatch);

        Code.put2(0); //needs patch - ELSE or IF end

        //leaves nothing on stack
    }

    @Override
    public void visit(IfStatement ifStatement) {
        //takes nothing from stack

        int currentAddress = Code.pc;
        int addressToPatch = jumpAddressStack.popIfConditionAddressToPatch(); //IF START DID PUSH

        if (addressToPatch == -1) {
            logger.error("RUNTIME ERROR - SHOULD PATCH IN IF-STATEMENT, BUT NO ADDRESS TO PATCH");
        }

        //add 1 for jmp instruction code
        Code.put2(addressToPatch, currentAddress - addressToPatch + 1);

        //leaves nothing on stack
    }

    @Override
    public void visit(IfElseStatement ifElseStatement) {
        //takes nothing from stack

        int currentAddress = Code.pc;
        int addressToPatch = jumpAddressStack.popIfConditionAddressToPatch(); //ELSE PART DID PUSH

        if (addressToPatch == -1) {
            logger.error("RUNTIME ERROR - SHOULD PATCH IN IF-ELSE-STATEMENT, BUT NO ADDRESS TO PATCH");
        }

        //add 1 for jmp instruction code
        Code.put2(addressToPatch, currentAddress - addressToPatch + 1);

        //leaves nothing on stack
    }

    @Override
    public void visit(ElsePart elsePart) {
        //insert jmp to jump over else
        Code.put(Code.jmp);

        int newAddressToPatch = Code.pc;

        Code.put2(0);  //needs PATCH - IF end

        int currentAddress = Code.pc;
        int addressToPatchOld = jumpAddressStack.popIfConditionAddressToPatch(); //IF START PART DID PUSH

        if (addressToPatchOld == -1) {
            logger.error("RUNTIME ERROR - SHOULD PATCH IN ELSE, BUT NO ADDRESS TO PATCH");

            //add 1 for jmp instruction code
        }  else Code.put2(addressToPatchOld, currentAddress - addressToPatchOld + 1);

        jumpAddressStack.pushIfConditionAddressToPatch(newAddressToPatch);

        //leaves nothing on stack
    }

    //---------------FOR STATEMENTS------------

    @Override
    public void visit(ForStatement forStatement) {
        //takes nothing from stack

        int iteratorStatementStart = jumpAddressStack.getIteratorStatementStart();

        Code.put(Code.jmp);
        Code.put2(iteratorStatementStart - Code.pc + 1);  //!

        ArrayList<Integer> list = jumpAddressStack.getForEndAddressesToPatch();
        for (Integer addressToPatch : list) {
            Code.put2(addressToPatch, Code.pc - addressToPatch + 1);
        }

        jumpAddressStack.popFor();

        //leaves nothing on stack
    }

    @Override
    public void visit(FirstOptDesignator firstOptDesignator) {
        //takes nothing from stack

        jumpAddressStack.pushFor();
        jumpAddressStack.setConditionStart(Code.pc);

        //leaves nothing on stack
    }

    @Override
    public void visit(ForOptCondition forOptCondition) {
        //takes condition result from stack

        boolean hasCondition = checkIfConditionExists(forOptCondition.getOptCondition());
        if (hasCondition) {
            Code.loadConst(trueValue);

            Code.put(Code.jcc + Code.ne);
            jumpAddressStack.addForEndAddressesToPatch(Code.pc);

            Code.put2(0);  //needs patch - END

            Code.put(Code.jmp);
            jumpAddressStack.setConditionToBodyStartAddressToPatch(Code.pc);
            Code.put2(0);  //needs patch - body start
        } else {
            Code.put(Code.jmp);
            jumpAddressStack.setConditionToBodyStartAddressToPatch(Code.pc);
            Code.put2(0); //needs patch - body start
        }

        jumpAddressStack.setIteratorStatementStart(Code.pc);

        //leaves nothing on stack
    }

    @Override
    public void visit(SecondOptDesignator secondOptDesignator) {
        //takes nothing from stack

        boolean hasSecondOptStatement = isSecondOptStatement(secondOptDesignator);

        //optimization
        if (hasSecondOptStatement) {

            int conditionStart = jumpAddressStack.getConditionStart();

            Code.put(Code.jmp);
            Code.put2(conditionStart - Code.pc + 1); //!

        } else {
            jumpAddressStack.setIteratorStatementStart(jumpAddressStack.getConditionStart());
        }

        int bodyStartAddress = Code.pc;
        jumpAddressStack.setBodyStart(bodyStartAddress);

        //fix body start jmp in condition
        int addressToPatch = jumpAddressStack.getConditionToBodyStartAddressToPatch();
        Code.put2(addressToPatch, bodyStartAddress - addressToPatch + 1);

        //leaves nothing on stack
    }

    @Override
    public void visit(BreakStatement breakStatement) {
        //takes nothing from stack

        Code.put(Code.jmp);

        int addressToPatch = Code.pc;

        Code.put2(0);  //needs patch

        jumpAddressStack.addForEndAddressesToPatch(addressToPatch);

        //leaves nothing on stack
    }

    @Override
    public void visit(ContinueStatement continueStatement) {
        //takes nothing from stack

        int iteratorStatementAddress = jumpAddressStack.getIteratorStatementStart();

        Code.put(Code.jmp);
        Code.put2(iteratorStatementAddress - Code.pc + 1);

        //leaves nothing on stack
    }

    //---------------IO STATEMENTS-------------

    @Override
    public void visit(ReadStatement readStatement) {
        //takes nothing from stack

        Designator designator = readStatement.getDesignator();
        if (designator.obj.getType() == SymbolTable.charType) {
            Code.put(Code.bread);
        } else {
            Code.put(Code.read);
        }

        storeDesignator(designator);

        //leaves nothing on stack
    }

    @Override
    public void visit(PrintStatement printStatement) {
        //takes value to print from stack

        super.visit(printStatement);

        ExpressionToPrint ex = printStatement.getExpressionToPrint();
        Struct type;
        int printTimes = 1;
        int printWidth = 5;
        int loopStart;
        int print = Code.print;

        if (ex instanceof ExpressionAndConstToPrint) {
            printTimes = ((ExpressionAndConstToPrint) ex).getValue();
            type = ((ExpressionAndConstToPrint) ex).getExpr().struct;
        }
        else type = ((OnlyExpressionToPrint) ex).getExpr().struct;

        if (type == SymbolTable.charType) {
            printWidth = 1; //char print size
            print = Code.bprint;
        }

        if (printTimes == 1) {
            Code.loadConst(printWidth);
            Code.put(print);

        } else if (printTimes > 1) {
            Code.loadConst(printTimes);

            loopStart = Code.pc;

            Code.put(Code.dup2);
            Code.put(Code.pop);
            Code.loadConst(printWidth);
            Code.put(print);

            Code.loadConst(1);
            Code.put(Code.sub);

            Code.put(Code.dup);
            Code.loadConst(0);
            Code.put(Code.jcc + Code.ne);
            Code.put2(loopStart - Code.pc + 1);

            Code.put(Code.pop);
            Code.put(Code.pop);
        }

        //leaves nothing on stack
    }
}
