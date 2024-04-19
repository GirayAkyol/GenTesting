package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.stateful.ActionSequence;

class MyStackProperties {

    @Property()
        //@Report(Reporting.GENERATED)
    void checkMyStackMachine(@ForAll("sequences") ActionSequence<MyStringStack> sequence) {
        sequence.run(new MyStringStack());
    }

    @Provide
    Arbitrary<ActionSequence<MyStringStack>> sequences() {
        return Arbitraries.sequences(MyStringStackActions.actions());
    }


}
