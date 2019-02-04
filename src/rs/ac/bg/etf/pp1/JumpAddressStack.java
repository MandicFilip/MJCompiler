package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Stack;

public class JumpAddressStack {

    //------------------------IF CONDITION STACK-------------------------------

    private static class IfConditionStackElement {
        private int address;

        public IfConditionStackElement(int address) {
            this.address = address;
        }

        public int getAddress() {
            return address;
        }
    }

    Stack<IfConditionStackElement> jumpStack;

    public JumpAddressStack() {
        jumpStack = new Stack<>();
        forLoopStack = new Stack<>();
    }

    public int popIfConditionAddressToPatch() {
        if (jumpStack.size() == 0) return -1;

        IfConditionStackElement stackElem = jumpStack.pop();
        return stackElem.getAddress();
    }

    public void pushIfConditionAddressToPatch(int address) {
        IfConditionStackElement stackElem = new IfConditionStackElement(address);
        jumpStack.push(stackElem);
    }

    //-----------------------------FOR STACK-----------------------------------

    private static class ForConditionElement {
        int conditionStart;
        int iteratorStatementStart;
        int bodyStart;
        boolean noCondition = true;

        ArrayList<Integer> breakAddressesToFix = new ArrayList<>();
        ArrayList<Integer> continueAddressesToFix = new ArrayList<>();
    }

    private Stack<ForConditionElement> forLoopStack;

    public void pushFor() {
        forLoopStack.push(new ForConditionElement());
    }

    public void popFor() {
        forLoopStack.pop();
    }

    public void setConditionStart(int conditionStart) {
        ForConditionElement tmp = forLoopStack.peek();
        tmp.conditionStart = conditionStart;
    }

    public void setIteratorStatementStart(int iteratorStatementStart) {
        ForConditionElement tmp = forLoopStack.peek();
        tmp.iteratorStatementStart = iteratorStatementStart;
    }

    public void setBodyStart(int bodyStart) {
        ForConditionElement tmp = forLoopStack.peek();
        tmp.bodyStart = bodyStart;
    }

    public void setNoCondition(boolean noCondition) {
        ForConditionElement tmp = forLoopStack.peek();
        tmp.noCondition = noCondition;
    }

    public int getConditionStart() {
        ForConditionElement tmp = forLoopStack.peek();
        return tmp.conditionStart;
    }

    public int getIteratorStatementStart() {
        ForConditionElement tmp = forLoopStack.peek();
        return tmp.iteratorStatementStart;
    }

    public int getBodyStart() {
        ForConditionElement tmp = forLoopStack.peek();
        return tmp.bodyStart;
    }

    public boolean isNoCondition() {
        ForConditionElement tmp = forLoopStack.peek();
        return tmp.noCondition;
    }

    public void setBreakAddress(int address) {
        ForConditionElement tmp = forLoopStack.peek();
        tmp.breakAddressesToFix.add(address);
    }

    public void setContinueAddress(int address) {
        ForConditionElement tmp = forLoopStack.peek();
        tmp.continueAddressesToFix.add(address);
    }

    public ArrayList<Integer> getBreakAddresses() {
        ForConditionElement tmp = forLoopStack.peek();
        return tmp.breakAddressesToFix;
    }

    public ArrayList<Integer> getContinueAddresses() {
        ForConditionElement tmp = forLoopStack.peek();
        return tmp.continueAddressesToFix;
    }

}