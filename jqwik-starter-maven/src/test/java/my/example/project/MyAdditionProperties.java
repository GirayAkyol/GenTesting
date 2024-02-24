package my.example.project;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.assertj.core.api.*;

import static java.util.Arrays.*;

class MyAdditionProperties {

	@Property
	boolean sumsOfSmallPositivesAreAlwaysPositive(@ForAll @IntRange(min = 1, max = 1000) int x, @ForAll @IntRange(min = 1, max = 1000) int y) {
		int result = new MyAddition().add(x, y);
		return result > 0;
	}

	@Property
	void addingZeroToAnyNumberResultsInNumber(@ForAll int aNumber) {
		int result = new MyAddition().add(aNumber, 0);
		Assertions.assertThat(result).isGreaterThanOrEqualTo(aNumber);
	}
}
