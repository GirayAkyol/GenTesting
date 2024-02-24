package my.example.project;

import net.jqwik.api.*;
import org.assertj.core.api.*;

import static java.util.Arrays.*;

class MyAdditionExamples {

    @Example
    void oneAndThree() {
        int result = new MyAddition().add(1, 3);
        Assertions.assertThat(result).isEqualTo(4);
    }


}
