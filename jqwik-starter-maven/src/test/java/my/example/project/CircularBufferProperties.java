package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;
import my.example.project.CircularBufferActions.*;

import static org.assertj.core.api.Assertions.*;

class CircularBufferProperties {

    @Property
        //@Report(Reporting.GENERATED)
    void checkSequentialStateMachine(@ForAll("sequences") ActionSequence<Model> sequence) {

        Invariant<Model> sizeMustNotBeNegative = model ->
                assertThat(model.buffer.size())
                        .as("Size must not be negative")
                        .isGreaterThanOrEqualTo(0);

        Invariant<Model> sizeMustNotExceedCapacity = model ->
                assertThat(model.buffer.size())
                        .as("Size must not exceed capacity")
                        .isLessThanOrEqualTo(model.capacity);

        sequence
                .withInvariant(sizeMustNotBeNegative)
                .withInvariant(sizeMustNotExceedCapacity)
                .run(new Model());
    }

    @Provide
    Arbitrary<ActionSequence<Model>> sequences() {
        return Arbitraries.sequences(Arbitraries.oneOf(
                Arbitraries.integers().between(1, 100).map(NewAction::new),
                Arbitraries.integers().map(Object::toString).map(PutAction::new),
                Arbitraries.just(new GetAction()),
                Arbitraries.just(new SizeAction())
        ));
    }

}

