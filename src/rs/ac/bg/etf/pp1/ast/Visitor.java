// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(OptCondition OptCondition);
    public void visit(ExpressionToPrint ExpressionToPrint);
    public void visit(ConstDeclExpression ConstDeclExpression);
    public void visit(VarDeclExpressionList VarDeclExpressionList);
    public void visit(ArrOpt ArrOpt);
    public void visit(AllDeclList AllDeclList);
    public void visit(SignTerm SignTerm);
    public void visit(Addop Addop);
    public void visit(DesignatorStmt DesignatorStmt);
    public void visit(NumberConst NumberConst);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(Designator Designator);
    public void visit(IfConditionForm IfConditionForm);
    public void visit(Term Term);
    public void visit(RetType RetType);
    public void visit(VarDeclExpression VarDeclExpression);
    public void visit(Statements Statements);
    public void visit(MulOp MulOp);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(FormParams FormParams);
    public void visit(EnumList EnumList);
    public void visit(RelOp RelOp);
    public void visit(AssignOp AssignOp);
    public void visit(ActualParamList ActualParamList);
    public void visit(OptDesignatorStatement OptDesignatorStatement);
    public void visit(ConditionDecl ConditionDecl);
    public void visit(VarsList VarsList);
    public void visit(Expr Expr);
    public void visit(ActualPars ActualPars);
    public void visit(ConstKind ConstKind);
    public void visit(Statement Statement);
    public void visit(VarDecl VarDecl);
    public void visit(CondFact CondFact);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(OptVars OptVars);
    public void visit(DeclType DeclType);
    public void visit(EnumInit EnumInit);
    public void visit(ModOp ModOp);
    public void visit(DivOp DivOp);
    public void visit(MulOper MulOper);
    public void visit(MinusOp MinusOp);
    public void visit(PlusOp PlusOp);
    public void visit(SmallerEqual SmallerEqual);
    public void visit(Smaller Smaller);
    public void visit(GreaterEqual GreaterEqual);
    public void visit(Greater Greater);
    public void visit(Unequal Unequal);
    public void visit(Equal Equal);
    public void visit(Assignment Assignment);
    public void visit(ArrayName ArrayName);
    public void visit(SimpleDesignator SimpleDesignator);
    public void visit(ArrayDesignator ArrayDesignator);
    public void visit(EnumDesignator EnumDesignator);
    public void visit(ActualParametar ActualParametar);
    public void visit(SingleActualParametar SingleActualParametar);
    public void visit(ParametarsList ParametarsList);
    public void visit(NoActualParametarsList NoActualParametarsList);
    public void visit(ActualParametarsList ActualParametarsList);
    public void visit(NumberFactor NumberFactor);
    public void visit(FunCallFactor FunCallFactor);
    public void visit(DesignatorFactor DesignatorFactor);
    public void visit(NewTypeFactor NewTypeFactor);
    public void visit(BoolFactor BoolFactor);
    public void visit(CharFactor CharFactor);
    public void visit(ParenFactor ParenFactor);
    public void visit(SingleFactorTerm SingleFactorTerm);
    public void visit(FactorListTerm FactorListTerm);
    public void visit(NoSignTerm NoSignTerm);
    public void visit(MinusTerm MinusTerm);
    public void visit(SingleTermExpr SingleTermExpr);
    public void visit(TermListExpr TermListExpr);
    public void visit(SingleCondFact SingleCondFact);
    public void visit(ConditionFactor ConditionFactor);
    public void visit(SingleCondFactTerm SingleCondFactTerm);
    public void visit(CondFactListTerm CondFactListTerm);
    public void visit(SingleTermCondition SingleTermCondition);
    public void visit(TermListCondition TermListCondition);
    public void visit(MethodCallStart MethodCallStart);
    public void visit(MethodCall MethodCall);
    public void visit(DecDesignatorStatement DecDesignatorStatement);
    public void visit(IncDesignatorStatement IncDesignatorStatement);
    public void visit(MethodCallStatement MethodCallStatement);
    public void visit(ErrAssignment ErrAssignment);
    public void visit(AssignDesignatorStatement AssignDesignatorStatement);
    public void visit(ExpressionAndConstToPrint ExpressionAndConstToPrint);
    public void visit(OnlyExpressionToPrint OnlyExpressionToPrint);
    public void visit(ConditionError ConditionError);
    public void visit(IfCondition IfCondition);
    public void visit(NoDesignatorStatement NoDesignatorStatement);
    public void visit(SingleDesignatorStatement SingleDesignatorStatement);
    public void visit(SecondOptDesignator SecondOptDesignator);
    public void visit(NoCondition NoCondition);
    public void visit(Condition Condition);
    public void visit(ForOptCondition ForOptCondition);
    public void visit(FirstOptDesignator FirstOptDesignator);
    public void visit(ForStart ForStart);
    public void visit(ElsePart ElsePart);
    public void visit(IfStart IfStart);
    public void visit(StatementBlock StatementBlock);
    public void visit(ReturnNopStatement ReturnNopStatement);
    public void visit(ReturnExpStatement ReturnExpStatement);
    public void visit(PrintStatement PrintStatement);
    public void visit(ReadStatement ReadStatement);
    public void visit(ContinueStatement ContinueStatement);
    public void visit(BreakStatement BreakStatement);
    public void visit(ForStatement ForStatement);
    public void visit(IfElseStatement IfElseStatement);
    public void visit(IfStatement IfStatement);
    public void visit(AssignmentError AssignmentError);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(NoStatementList NoStatementList);
    public void visit(StatementList StatementList);
    public void visit(FormPara FormPara);
    public void visit(FormParamsDerived1 FormParamsDerived1);
    public void visit(NoFormParamsDeclaration NoFormParamsDeclaration);
    public void visit(SingleFormParametarDeclaration SingleFormParametarDeclaration);
    public void visit(FormParamsDeclaration FormParamsDeclaration);
    public void visit(ReturnType ReturnType);
    public void visit(RetVoid RetVoid);
    public void visit(MethodStart MethodStart);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarationList MethodDeclarationList);
    public void visit(NoEnumInit NoEnumInit);
    public void visit(SingleEnumInit SingleEnumInit);
    public void visit(EnumMember EnumMember);
    public void visit(SingleEnumMember SingleEnumMember);
    public void visit(EnumMembersList EnumMembersList);
    public void visit(EnumStart EnumStart);
    public void visit(EnumDecl EnumDecl);
    public void visit(NoArrayOption NoArrayOption);
    public void visit(ArrayOption ArrayOption);
    public void visit(ErrorVarDecl ErrorVarDecl);
    public void visit(VarDeclaration VarDeclaration);
    public void visit(SigleVarDecl SigleVarDecl);
    public void visit(VariablesList VariablesList);
    public void visit(VarDeclarationWithError VarDeclarationWithError);
    public void visit(VarDeclarationExpression VarDeclarationExpression);
    public void visit(SingleOptVarDecl SingleOptVarDecl);
    public void visit(OptVarDeclExpressionList OptVarDeclExpressionList);
    public void visit(NoOptVarsList NoOptVarsList);
    public void visit(OptVarsList OptVarsList);
    public void visit(NegativeNumber NegativeNumber);
    public void visit(PositiveNumber PositiveNumber);
    public void visit(JustNumber JustNumber);
    public void visit(ConstBool ConstBool);
    public void visit(ConstChar ConstChar);
    public void visit(ConstNumber ConstNumber);
    public void visit(ConstDecl ConstDecl);
    public void visit(SingleConstDeclaration SingleConstDeclaration);
    public void visit(ConstDeclarationList ConstDeclarationList);
    public void visit(ConstDeclarationExpression ConstDeclarationExpression);
    public void visit(Type Type);
    public void visit(EnumTypeDeclaration EnumTypeDeclaration);
    public void visit(VarTypeDeclaration VarTypeDeclaration);
    public void visit(ConstTypeDeclaration ConstTypeDeclaration);
    public void visit(NoDeclaration NoDeclaration);
    public void visit(ALLDeclarationsList ALLDeclarationsList);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
