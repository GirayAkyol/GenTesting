package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.state.Action;
import net.jqwik.api.state.ActionChain;
import net.jqwik.api.state.ActionChainArbitrary;
import net.jqwik.api.state.Transformer;

import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 1000)
public class MyStoreAvlMbt {

    public static class MBT {
        public MyStoreAVL<String> system = new MyStoreAVL<>();
        public MyStore<Integer, String> model = new MyStore<>();

        // override to string
        @Override
        public String toString() {
            return String.format("\nSystem: %s\nModel: %s", system, model);
        }
    }

    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, 100);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(1).map(String::toLowerCase);
    }


    @Property(shrinking = ShrinkingMode.FULL, afterFailure = AfterFailureMode.PREVIOUS_SEED)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MBT> storeChain) {
        storeChain.withInvariant("samekeys", state -> {
            assertThat(state.system.keys()).containsExactlyInAnyOrderElementsOf(state.model.keys());
        }).run();
    }

    @Provide
    ActionChainArbitrary<MBT> storeActions() {
        return ActionChain.<MBT>startWith(MBT::new)
                .withAction(1, new StoreNewValue())
                .withAction(1, new UpdateValue())
                .withAction(1, new RemoveValue()).withMaxTransformations(10);
    }

    static class StoreNewValue implements Action.Independent<MBT> {
        @Override
        public Arbitrary<Transformer<MBT>> transformer() {
            return Combinators.combine(keys(), values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("store %s=%s", key, value),
                            state -> {
                                MyStoreAVL<String> system = state.system;
                                MyStore<Integer, String> model = state.model;

                                assertThat(system.isEmpty()).isFalse();
                                assertThat(model.isEmpty()).isFalse();
                            }
                    ));
        }
    }

    static class UpdateValue implements Action.Dependent<MBT> {
        @Override
        public boolean precondition(MBT store) {
            return !store.model.isEmpty() && !store.system.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MBT>> transformer(MBT state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.model.keys());
            return Combinators.combine(existingKeys, values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("update %s=%s", key, value),
                            store -> {
                                MyStoreAVL<String> system = state.system;
                                MyStore<Integer, String> model = state.model;

                                assertThat(system.isEmpty()).isFalse();
                                assertThat(model.isEmpty()).isFalse();
                            }
                    ));
        }
    }

    static class RemoveValue implements Action.Dependent<MBT> {
        @Override
        public boolean precondition(MBT store) {
            return !store.system.isEmpty() && !store.model.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MBT>> transformer(MBT state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.model.keys());
            return existingKeys.map(key -> Transformer.mutate(
                    String.format("remove %s", key),
                    store -> {
                        MyStoreAVL<String> system = state.system;
                        MyStore<Integer, String> model = state.model;

                    }
            ));
        }
    }


}
