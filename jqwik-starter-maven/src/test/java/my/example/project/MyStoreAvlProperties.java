package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.state.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 100)
public class MyStoreAvlProperties {

    /**
     * This property should detect a bug that will occur when a key is updated and then deleted afterwards
     */
    @Property(shrinking = ShrinkingMode.FULL, afterFailure = AfterFailureMode.RANDOM_SEED)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MyStoreAVL<String>> storeChain) {
        storeChain.run();
    }

    @Provide
    ActionChainArbitrary<MyStoreAVL<String>> storeActions() {
        return ActionChain.<MyStoreAVL<String>>startWith(MyStoreAVL<String>::new)
                .withAction(3, new StoreAnyValue())
                .withAction(1, new UpdateValue())
                .withAction(1, new RemoveValue());
        //.improveShrinkingWith(StoreChangesDetector::new);
    }

    static class StoreAnyValue implements Action.Independent<MyStoreAVL<String>> {
        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer() {
            return Combinators.combine(keys(), values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("store %s=%s", key, value),
                            store -> {
                                store.store(key, value);
                                assertThat(store.isEmpty()).isFalse();
                                assertThat(store.get(key)).isEqualTo(value);
                            }
                    ));
        }
    }

    static class UpdateValue implements Action.Dependent<MyStoreAVL<String>> {
        @Override
        public boolean precondition(MyStoreAVL<String> store) {
            return !store.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer(MyStoreAVL<String> state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.keys());
            return Combinators.combine(existingKeys, values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("update %s=%s", key, value),
                            store -> {
                                store.store(key, value);
                                assertThat(store.isEmpty()).isFalse();
                                assertThat(store.get(key)).isEqualTo(value);
                            }
                    ));
        }
    }

    static class RemoveValue implements Action.Dependent<MyStoreAVL<String>> {
        @Override
        public boolean precondition(MyStoreAVL<String> store) {
            return !store.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer(MyStoreAVL<String> state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.keys());
            return existingKeys.map(key -> Transformer.mutate(
                    String.format("remove %s", key),
                    store -> {
                        store.delete(key);
                        assertThat(store.get(key)).describedAs("value of key <%s>", key).isNull();
                    }
            ));
        }
    }

    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, Integer.MAX_VALUE);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(4).map(String::toLowerCase);
    }

}
