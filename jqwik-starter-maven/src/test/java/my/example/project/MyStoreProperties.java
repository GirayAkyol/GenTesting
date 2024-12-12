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
        return Arbitraries.integers().between(1, Integer.MAX_VALUE);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(1);
    }

    @Provide
    ActionChainArbitrary<MyStore<Integer, String>> storeActions() {
        return ActionChain.<MyStore<Integer, String>>startWith(MyStore::new)
                .withAction(3, new StoreAnyValue());
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


}
