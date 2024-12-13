package my.example.project;

import java.util.*;
import java.util.stream.*;

import net.jqwik.api.*;
import net.jqwik.api.state.*;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.*;

@PropertyDefaults(tries = 1000)
public class MyStoreProperties {

    /**
     * This property should detect a bug that will occur when a key is updated and then deleted afterwards
     */
    @Property(shrinking = ShrinkingMode.BOUNDED, afterFailure = AfterFailureMode.RANDOM_SEED)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MyStore<Integer, String>> storeChain) {
        storeChain.run();
    }

    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, Integer.MAX_VALUE);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(1);
    }

    @Provide
    ActionChainArbitrary<MyStore<Integer, String>> storeActions() {
        return ActionChain.<MyStore<Integer, String>>startWith(MyStore::new)
                .withAction(1, new StoreAnyValue())
                .withAction(1, new UpdateValue())
                .withAction(1, new RemoveValue());
    }

    static class StoreAnyValue implements Action.Dependent<MyStore<Integer, String>> {
        @Override
        public Arbitrary<Transformer<MyStore<Integer, String>>> transformer(MyStore<Integer, String> state) {
            return Combinators.combine(keys().filter(
                            key -> !state.keys().contains(key)
                    ), values())
                    .as((key, newValue) -> Transformer.mutate(
                            String.format("store %s=%s", key, newValue),
                            store -> {
                                Assume.that(!store.keys().contains(key));
                                store.store(key, newValue);
                                assertThat(store.isEmpty()).isFalse();
                                assertThat(store.get(key)).isEqualTo(Optional.of(newValue));
                            }
                    ));
        }
    }


    static class RemoveValue implements Action.Dependent<MyStore<Integer, String>> {

        @Override
        public boolean precondition(MyStore<Integer, String> state) {
            return !state.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MyStore<Integer, String>>> transformer(MyStore<Integer, String> state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.keys());
            return existingKeys.map(key -> Transformer.mutate(
                    String.format("remove %s", key),
                    store -> {
                        Assume.that(store.get(key).isPresent());
                        store.remove(key);
                        assertThat(store.get(key)).describedAs("value of key <%s>", key).isNotPresent();
                    }
            ));
        }
    }


    private class UpdateValue implements Action.Dependent<MyStore<Integer, String>> {

        @Override
        public boolean precondition(MyStore<Integer, String> state) {
            return !state.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MyStore<Integer, String>>> transformer(MyStore<Integer, String> state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.keys());
            Arbitrary<String> newValue = values();
            return Combinators.combine(existingKeys, newValue).as
                    ((key, value) -> Transformer.mutate(
                            String.format("update %s=%s", key, value),
                            store -> {
                                Assume.that(store.get(key).isPresent());
                                String oldValue = store.get(key).get();
                                store.store(key, value);
                                assertThat(store.get(key)).isEqualTo(Optional.of(oldValue));
                            }
                    ));
        }
    }
}
