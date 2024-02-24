package my.example.project;

import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

class MyAdditionRand {

    @Example
    void commutative() {
        int x = (int) (Math.random() * 100);
        int y = (int) (Math.random() * 100);

        int result1 = new MyAddition().add(x, y);
        int result2 = new MyAddition().add(y, x);

        Assertions.assertThat(result1).isEqualTo(result2);
    }

}
