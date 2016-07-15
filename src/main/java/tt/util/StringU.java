package tt.util;

import tt.lang.string.TStringBuilder;

public class StringU {

	protected StringU() {

	}

	public static String toString(Object o) {
		TStringBuilder sb = new TStringBuilder();
		sb.write(o);
		return sb.buildString();
	}
}
