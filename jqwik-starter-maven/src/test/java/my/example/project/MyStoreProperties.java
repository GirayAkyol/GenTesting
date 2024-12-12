package my.example.project;

import java.util.*;
import java.util.stream.*;

import net.jqwik.api.*;
import net.jqwik.api.state.*;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.*;

@PropertyDefaults(tries = 100)
public class MyStoreProperties {

    /**
     * This property should detect a bug that will occur when a key is updated and then deleted afterwards
     */
    @Property(shrinking = ShrinkingMode.FULL, afterFailure = AfterFailureMode.RANDOM_SEED)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MyStore<Integer, String>> storeChain) {
        storeChain.run();
    }

    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, 30);
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

    static class StoreAnyValue implements Action.Independent<MyStore<Integer, String>> {
        @Override
        public Arbitrary<Transformer<MyStore<Integer, String>>> transformer() {
            return Combinators.combine(keys(), values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("store %s=%s", key, value),
                            store -> {
                                store.store(key, value);
                                assertThat(store.isEmpty()).isFalse();
                                assertThat(store.get(key)).isEqualTo(Optional.of(value));
                            }
                    ));
        }
    }

    static class UpdateValue implements Action.Independent<MyStore<Integer, String>> {

        @Override
        public Arbitrary<Transformer<MyStore<Integer, String>>> transformer() {
            Arbitrary<Integer> existingKeys = keys();
            return Combinators.combine(existingKeys, values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("update %s=%s", key, value),
                            store -> {
                                String oldValue = store.get(key).get();
                                store.store(key, value);
                                assertThat(store.isEmpty()).isFalse();
                                assertThat(store.get(key)).isEqualTo(Optional.of(oldValue));
                            }
                    ));
        }
    }

    static class RemoveValue implements Action.Independent<MyStore<Integer, String>> {

        @Override
        public Arbitrary<Transformer<MyStore<Integer, String>>> transformer() {
            Arbitrary<Integer> existingKeys = keys();
            return existingKeys.map(key -> Transformer.mutate(
                    String.format("remove %s", key),
                    store -> {
                        store.remove(key);
                        assertThat(store.get(key)).describedAs("value of key <%s>", key).isNotPresent();
                    }
            ));
        }
    }


}
