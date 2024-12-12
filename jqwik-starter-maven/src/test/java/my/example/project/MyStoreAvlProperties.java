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

    public static boolean checkBst(MyStoreAVL.Node root, int min, int max) {
        if (root == null) {
            return true;
        }
        if (root.key < min || root.key > max) {
            return false;
        }
        return checkBst(root.left, min, root.key) && checkBst(root.right, root.key, max);
    }

    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, 100);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(1).map(String::toLowerCase);
    }

    @Property(shrinking = ShrinkingMode.FULL)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MyStoreAVL<String>> storeChain) {
        storeChain.withInvariant(store -> {
            int balance = store.getBalance(store.root);
            assertThat(balance).isBetween(-1, 1);
        }).run();
    }

    @Provide
    ActionChainArbitrary<MyStoreAVL<String>> storeActions() {
        return ActionChain.<MyStoreAVL<String>>startWith(MyStoreAVL<String>::new)
                .withAction(3, new StoreNewValue())
                .withAction(1, new UpdateValue())
                .withAction(1, new RemoveValue()).withMaxTransformations(10);
    }

    static class StoreNewValue implements Action.Dependent<MyStoreAVL<String>> {
        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer(MyStoreAVL<String> state) {
            return Combinators.combine(keys().filter(key -> !state.keys().contains(key)), values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("store %s=%s", key, value),
                            store -> {
                                Assume.that(store.get(key) == null);
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
                                String oldvalue = store.get(key);
                                store.store(key, value);
                                assertThat(store.isEmpty()).isFalse();
                                assertThat(store.get(key)).isEqualTo(oldvalue);
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


}
