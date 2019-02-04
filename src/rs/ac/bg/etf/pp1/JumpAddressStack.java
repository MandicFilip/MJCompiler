package rs.ac.bg.etf.pp1;

import java.util.Stack;

public class JumpAddressStack {

    private class StackElem {
        private int address;
        private int nestLevel;

        public StackElem(int address, int nestLevel) {
            this.address = address;
            this.nestLevel = nestLevel;
        }

        public int getAddress() {
            return address;
        }

        public int getNestLevel() {
            return nestLevel;
        }
    }

    Stack<StackElem> jumpStack;
    int currentNestLevel = 0;


}
