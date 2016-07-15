package tt.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import tt.io.annotations.AtByte;
import tt.io.annotations.AtSize;
import tt.io.annotations.AtUnsignedInt;
import tt.io.annotations.AtUnuse;

public class TDataOutputStream extends DataOutputStream {

	private String charset = "UTF-8";

	public TDataOutputStream(OutputStream out) {
		super(out);
	}

	public void writeLength(int n) throws IOException {
		writeUnsignedShort(n);
	}

	public void writeCharBoolean(boolean b) throws IOException {
		out.write(b ? '1' : '0');
	}

	public void writeUnsignedShort(int n) throws IOException {
		out.write((int) (n >>> 0x08) & 255);
		out.write((int) (n >>> 0x00) & 255);
	}

	public void writeUnsignedInt(long n) throws IOException {
		out.write((int) (n >>> 0x18) & 255);
		out.write((int) (n >>> 0x10) & 255);
		out.write((int) (n >>> 0x08) & 255);
		out.write((int) (n >>> 0x00) & 255);
	}

	public void writeNString(int n, Object o) throws IOException {
		byte[] b = o == null ? null : o.toString().getBytes(charset);
		if (b.length < n) {
			out.write(b, 0, b.length);
			int z = n - b.length;
			while (z-- > 0)
				out.write(0);
		} else {
			out.write(b, 0, n);
		}
	}

	public void writeDate(Date d) throws IOException {
		writeUnsignedInt(d == null ? 0 : d.getTime());
	}

	public void writeObject(Class<?> c, Object o, Field f) throws Exception {
		AtUnuse atUnused = f == null ? null : f.getAnnotation(AtUnuse.class);
		if (atUnused != null)
			return;
		try {
			if (c.isPrimitive()) {
				if (o == null)
					o = c.newInstance();
				if (int.class.isAssignableFrom(c)) {
					AtByte atByte = f == null ? null : f.getAnnotation(AtByte.class);
					if (atByte == null) {
						writeInt(((Number) o).intValue());
						return;
					} else {
						out.write(((Number) o).intValue());
						return;
					}
				}
				if (float.class.isAssignableFrom(c)) {
					writeFloat(((Number) o).floatValue());
					return;
				}
				if (short.class.isAssignableFrom(c)) {
					writeShort(((Number) o).shortValue());
					return;
				}
				if (long.class.isAssignableFrom(c)) {
					AtUnsignedInt a1 = f == null ? null : f.getAnnotation(AtUnsignedInt.class);
					if (a1 != null) {
						writeUnsignedInt(((Number) o).longValue());
					} else {
						writeLong(((Number) o).longValue());
					}
					return;
				}
				if (double.class.isAssignableFrom(c)) {
					writeDouble(((Number) o).doubleValue());
					return;
				}
				if (char.class.isAssignableFrom(c)) {
					out.write(((Character) o).charValue());
					return;
				}
				if (byte.class.isAssignableFrom(c)) {
					out.write(((Byte) o).byteValue());
					return;
				}
				if (boolean.class.isAssignableFrom(c)) {
					writeCharBoolean(((Boolean) o).booleanValue());
				}
				throw new IOException(
						String.format("%s is unsupported primitive type%s.", c.getCanonicalName(), f == null ? ""
								: String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(), f.getName())));
			}
			if (c.isArray()) {
				AtSize a1 = f == null ? null : f.getAnnotation(AtSize.class);
				if (a1 == null)
					throw new IOException(String.format("%s must be annotated%s.", c.getCanonicalName(), f == null ? ""
							: String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(), f.getName())));
				Class<?> cs = c.getComponentType();
				if (cs.isPrimitive()) {
					int n = a1.value();
					if (byte[].class.isAssignableFrom(c)) {
						byte[] b = o == null ? new byte[n] : (byte[]) o;
						if (b.length != n)
							throw new IOException(String.format("%s must be %d size of array but %d%s.",
									c.getCanonicalName(), n, b.length,
									f == null ? ""
											: String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(),
													f.getName())));
						out.write(b, 0, b.length);
						return;
					}
					throw new IOException(
							String.format("%s is unsupported primitive array[] type%s.", c.getCanonicalName(),
									f == null ? ""
											: String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(),
													f.getName())));
				} else {
					int n = a1.value();
					if (o == null)
						o = Array.newInstance(cs, n);
					else if (Array.getLength(o) != n)
						throw new IOException(String.format("%s must be %d size of array but %d%s.",
								c.getCanonicalName(), n, Array.getLength(o),
								f == null ? ""
										: String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(),
												f.getName())));
					for (int i = 0; i < n; i++)
						writeObject(cs, Array.get(o, i), null);
					return;
				}
			}
			if (String.class.isAssignableFrom(c)) {
				AtSize a1 = f == null ? null : f.getAnnotation(AtSize.class);
				if (a1 != null) {
					writeNString(a1.value(), o);
				} else {
					throw new IOException(String.format("%s should be annotated%s.", c.getCanonicalName(), f == null
							? "" : String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(), f.getName())));
				}
				return;
			}
			if (Date.class.isAssignableFrom(c)) {
				writeDate((Date) o);
				return;
			}
			if (List.class.isAssignableFrom(c)) {
				if (f == null)
					throw new IOException(String.format("%s should be annotated%s.", c.getCanonicalName(), f == null
							? "" : String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(), f.getName())));
				Class<?> lc = (Class<?>) ((((ParameterizedType) f.getGenericType()).getActualTypeArguments())[0]);
				List<?> l = (List<?>) o;
				if (l == null || l.size() == 0) {
					writeLength(0);
				} else {
					writeLength(l.size());
					for (Object lo : l)
						writeObject(lc, lo, null);
				}
				return;
			}
			if (Number.class.isAssignableFrom(c) || c.isArray()) {
				if (Integer.class.isAssignableFrom(c)) {
					Integer n = (Integer) o;
					this.writeInt(n == null ? 0 : n.intValue());
				} else
					throw new IOException(String.format("%s is unsupported class type%s.", c.getCanonicalName(),
							f == null ? ""
									: String.format(" at %s.%s", f.getDeclaringClass().getCanonicalName(),
											f.getName())));
				return;
			}
			writeObjectFields(c, o);
		} catch (Exception e) {
			throw e;
		}
	}

	public void writeObjectFields(Class<?> c, Object o) throws Exception {
		if (o == null)
			o = c.newInstance();
		Field[] fa = c.getDeclaredFields();
		AccessibleObject.setAccessible(fa, true);
		for (Field f : fa)
			if (!Modifier.isStatic(f.getModifiers()))
				writeObject(f.getType(), f.get(o), f);
	}
}
