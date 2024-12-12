package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.state.Action;
import net.jqwik.api.state.ActionChain;
import net.jqwik.api.state.ActionChainArbitrary;
import net.jqwik.api.state.Transformer;

import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 100)
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


    @Property(shrinking = ShrinkingMode.FULL, afterFailure = AfterFailureMode.PREVIOUS_SEED)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MBT> storeChain) {
        storeChain.run();
    }

    @Provide
    ActionChainArbitrary<MBT> storeActions() {
        return ActionChain.<MBT>startWith(MBT::new)
                .withAction(3, new StoreNewValue())
                .withAction(1, new UpdateValue())
                .withAction(1, new RemoveValue()).withMaxTransformations(10);
        //.improveShrinkingWith(StoreChangesDetector::new);
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
                                system.store(key, value);
                                model.store(key, value);
                                assertThat(system.isEmpty()).isFalse();
                                assertThat(model.isEmpty()).isFalse();
                                assertThat(system.get(key)).isEqualTo(model.get(key).get());
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
                                system.store(key, value);
                                model.store(key, value);
                                assertThat(system.isEmpty()).isFalse();
                                assertThat(model.isEmpty()).isFalse();
                                assertThat(system.get(key)).isEqualTo(model.get(key).get());
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
                        system.delete(key);
                        model.remove(key);
                        assertThat(system.get(key)).describedAs("value of key <%s>", key).isNull();
                        assertThat(model.get(key)).describedAs("value of key <%s>", key).isEmpty();
                    }
            ));
        }
    }

    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, 100);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(4).map(String::toLowerCase);
    }

}
