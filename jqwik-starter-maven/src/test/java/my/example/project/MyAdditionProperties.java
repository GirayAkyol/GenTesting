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
}
