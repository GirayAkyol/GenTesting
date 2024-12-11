package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.NumericChars;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.api.stateful.ActionSequence;

class MyStackProperties {

    @Property
    boolean pushPopcheck(@ForAll @NumericChars @StringLength(1) String tobepushed) {
        MyStringStack stack = new MyStringStack();
        stack.push(tobepushed);
        return stack.pop().equals(tobepushed) && stack.isEmpty();
    }


}
