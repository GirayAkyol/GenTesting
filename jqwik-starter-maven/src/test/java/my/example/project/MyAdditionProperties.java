package my.example.project;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.assertj.core.api.*;

import static java.util.Arrays.*;

class MyAdditionProperties {

	@Property
	boolean commutative(@ForAll @IntRange(min = 0, max = 100) int x,
						@ForAll @IntRange(min = 0, max = 100) int y) {
		int result1 = new MyAddition().add(x, y);
		int result2 = new MyAddition().add(y, x);
		return result1 == result2;
	}

	@Property
	boolean add1Add1AndAdd2(@ForAll @IntRange(min = 0, max = 100) int x) {
		int intermediate = new MyAddition().add(x, 1);
		int result1 = new MyAddition().add(intermediate, 1);
		int result2 = new MyAddition().add(x, 2);
		return result1 == result2;
	}

	@Property
	void associative(@ForAll @IntRange(min = 0, max = 100) int x,
	  				 @ForAll @IntRange(min = 0, max = 100) int y,
					 @ForAll @IntRange(min = 0, max = 100) int z) {
		int result1 = new MyAddition().add(x, new MyAddition().add(y, z));
		int result2 = new MyAddition().add(new MyAddition().add(x, y), z);
		Assertions.assertThat(result1).isEqualTo(result2);
	}
}
