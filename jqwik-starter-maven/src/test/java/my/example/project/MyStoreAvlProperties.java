package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.state.*;
import org.assertj.core.api.Assert;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 1000)
public class MyStoreAvlProperties {


    private static Arbitrary<Integer> keys() {
        return Arbitraries.integers().between(1, 100);
    }

    private static Arbitrary<String> values() {
        return Arbitraries.strings().alpha().ofLength(1).map(String::toLowerCase);
    }

    public static boolean checkBST(MyStoreAVL.Node node, int min, int max) {
        if (node == null) {
            return true;
        }
        if (node.key < min || node.key > max) {
            return false;
        }
        return checkBST(node.left, min, node.key - 1) && checkBST(node.right, node.key + 1, max);
    }

    @Property(shrinking = ShrinkingMode.BOUNDED)
    void storeWorksAsExpected(@ForAll("storeActions") ActionChain<MyStoreAVL<String>> storeChain) {
        storeChain.withInvariant("BST property", store ->
                        assertThat(checkBST(store.root, Integer.MIN_VALUE, Integer.MAX_VALUE)).isTrue())
                .withInvariant("balanced", store -> {
                    int rootbal = store.getBalance(store.root);
                    assertThat(rootbal).isBetween(-1, 1);
                })
                .run();
    }

    @Provide
    ActionChainArbitrary<MyStoreAVL<String>> storeActions() {
        return ActionChain.<MyStoreAVL<String>>startWith(MyStoreAVL<String>::new)
                .withAction(1, new StoreNewValue())
                .withAction(1, new UpdateValue())
                .withAction(1, new RemoveValue())
                .withMaxTransformations(10);
    }

    static class StoreNewValue implements Action.Dependent<MyStoreAVL<String>> {
        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer(MyStoreAVL<String> state) {
            return Combinators.combine(keys().filter(
                            key -> !state.keys().contains(key)
                    ), values())
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


    static class RemoveValue implements Action.Dependent<MyStoreAVL<String>> {

        @Override
        public boolean precondition(MyStoreAVL<String> state) {
            return !state.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer(MyStoreAVL<String> state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.keys());
            return existingKeys.map(key -> Transformer.mutate(
                    String.format("remove %s", key),
                    store -> {
                        store.delete(key);
                        assertThat(store.get(key)).isNull();
                    }
            ));
        }
    }


    private class UpdateValue implements Action.Dependent<MyStoreAVL<String>> {

        @Override
        public boolean precondition(MyStoreAVL<String> state) {
            return !state.isEmpty();
        }

        @Override
        public Arbitrary<Transformer<MyStoreAVL<String>>> transformer(MyStoreAVL<String> state) {
            Arbitrary<Integer> existingKeys = Arbitraries.of(state.keys());
            return Combinators.combine(existingKeys, values())
                    .as((key, value) -> Transformer.mutate(
                            String.format("update %s=%s", key, value),
                            store -> {
                                Assume.that(store.get(key) != null);
                                String oldValue = store.get(key);
                                store.store(key, value);
                                assertThat(store.get(key)).isEqualTo(oldValue);
                            }
                    ));
        }
    }
}
