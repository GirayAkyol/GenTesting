package my.example.project;

import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

class MyAdditionRand {

    @Example
    void randAndRand() {
        // gen 2 random integers
        int x = (int) (Math.random() * 100);
        int y = (int) (Math.random() * 100);

        int result = new MyAddition().add(x, y);

        int expected = x + y;

        Assertions.assertThat(result).isEqualTo(expected);

        // do it 50 more times
        for (int i = 0; i < 50; i++) {
            x = (int) (Math.random() * 100);
            y = (int) (Math.random() * 100);
            result = new MyAddition().add(x, y);
            expected = x + y;
            Assertions.assertThat(result).isEqualTo(expected);
        }

    }

}
