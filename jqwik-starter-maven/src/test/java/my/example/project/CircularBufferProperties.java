package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;
import my.example.project.CircularBufferActions.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class CircularBufferProperties {

    @Property
    void checkSequentialStateMachine(@ForAll("sequences") ActionSequence<Model> sequence) {

        Invariant<Model> sizeMustNotBeNegative = model ->
                assertThat(model.buffer.size())
                        .as("Size must not be negative")
                        .isGreaterThanOrEqualTo(0);

        Invariant<Model> sizeMustNotExceedCapacity = model ->
                assertThat(model.buffer.size())
                        .as("Size must not exceed capacity")
                        .isLessThanOrEqualTo(model.capacity);

        Invariant<Model> contentsMustMatch = model ->
        {
            int size = model.buffer.size();
            ArrayList<Object> expected = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Object x = model.buffer.get();
                expected.add(x);
                model.buffer.put(x);
            }
            assertThat(model.contents).as("Contents must match " + model.buffer)
                    .containsExactlyElementsOf(expected);
        };

        sequence
                .withInvariant(sizeMustNotBeNegative)
                .withInvariant(sizeMustNotExceedCapacity)
                .withInvariant(contentsMustMatch)
                .run(new Model());
    }

    @Provide
    Arbitrary<ActionSequence<Model>> sequences() {
        return Arbitraries.sequences(Arbitraries.oneOf(
                Arbitraries.integers().between(3, 100).map(NewAction::new),
                Arbitraries.integers().map(PutAction::new),
                Arbitraries.just(new GetAction()),
                Arbitraries.just(new SizeAction())
        ));
    }

}

