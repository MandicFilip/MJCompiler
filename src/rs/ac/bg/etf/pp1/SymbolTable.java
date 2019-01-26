package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

import static rs.etf.pp1.symboltable.Tab.currentScope;
import static rs.etf.pp1.symboltable.Tab.noType;

public class SymbolTable {
    public static final int BOOLEAN_TYPE = 5;

    public static final Struct boolType = new Struct(5);
    public static Obj printObj;

    public static void init() {
        currentScope.addToLocals(new Obj(2, "int", boolType));


        currentScope.addToLocals(printObj = new Obj(3, "print", Tab.noType, 0, 1));
        openScope();
        currentScope.addToLocals(new Obj(1, "arr", new Struct(3, noType), 0, 1));
        printObj.setLocals(currentScope.getLocals());
        closeScope();
    }

    public static void chainLocalSymbols(Obj outerScopeObj) {
        Tab.chainLocalSymbols(outerScopeObj);
    }

    public static void chainLocalSymbols(Struct innerClass) {
        Tab.chainLocalSymbols(innerClass);
    }

    public static void openScope() {
        Tab.openScope();
    }

    public static void closeScope() {
        Tab.closeScope();
    }

    public static Obj insert(int kind, String name, Struct type) {
        return Tab.insert(kind, name, type);
    }

    public static Obj find(String name) {
        return Tab.find(name);
    }

    public static Scope currentScope() {
        return Tab.currentScope();
    }

    public static void dump(SymbolTableVisitor stv) {
        Tab.dump();
    }

    public static void dump() {
        dump((SymbolTableVisitor)null);
    }

}
