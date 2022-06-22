package github._1p6.repair_pouches;

public final class Util {
	public static int saturatingAdd(int a, int b) {
		try {
			return Math.addExact(a, b);
		} catch(ArithmeticException e) {
			return Integer.MAX_VALUE;
		}
	}
	
	public static int saturatingMul(int a, int b) {
		try {
			return Math.multiplyExact(a, b);
		} catch(ArithmeticException e) {
			return Integer.MAX_VALUE;
		}
	}
}
