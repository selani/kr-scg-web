package tt.lang.string;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tt.io.TData;

public class TStringBuilder {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static String nls = "";
	private static String ns = "\n";
	private static String ds = "  ";
	private static String cs = ", ";
	private static String cns = ",\n";
	private static String a1s = "[";
	private static String a2s = "]";
	private static String ads = ": ";
	private static String o1s = "{";
	private static String o2s = "}";
	private static String ods = ": ";

	private StringBuilder sb;
	private boolean appendSuperClassAtLast;

	public TStringBuilder() {
		this.sb = new StringBuilder();
	}

	public TStringBuilder(StringBuilder sb) {
		this.sb = sb;
	}

	public boolean isAppendSuperClassAtLast() {
		return appendSuperClassAtLast;
	}

	public void setAppendSuperClassAtLast(boolean appendSuperClassAtLast) {
		this.appendSuperClassAtLast = appendSuperClassAtLast;
	}

	public void write(Object o) {
		if (o == null)
			sb.append(nls);
		else
			write(o.getClass(), o, "");
	}

	public void write(Object o, String d) {
		if (o == null)
			sb.append(nls);
		else
			write(o.getClass(), o, d);
	}

	@SuppressWarnings({ "rawtypes" })
	public void write(Class c, Object o, String d) {
		try {
			if (o == null) {
				sb.append(nls);
				return;
			}
			if (c.isPrimitive()) {
				sb.append(o);
				return;
			}
			if (c.isArray()) {
				writeArray(c, o, d);
				return;
			}
			if (String.class.isAssignableFrom(c)) {
				sb.append("'").append(o).append("'");
				return;
			}
			if (Number.class.isAssignableFrom(c)) {
				sb.append(o);
				return;
			}
			if (Date.class.isAssignableFrom(c)) {
				sb.append(sdf.format((Date) o));
				return;
			}
			if (List.class.isAssignableFrom(c)) {
				writeList((List) o, d);
				return;
			}
			if (Iterable.class.isAssignableFrom(c)) {
				ArrayList<Object> l = new ArrayList<Object>();
				Iterable t = (Iterable) o;
				for (Object to : t)
					l.add(to);
				writeList(l, d);
				return;
			}
			if (Map.class.isAssignableFrom(c)) {
				writeMap((Map) o, d);
				return;
			}
			if (c != Object.class)
				writeFields(c, o, d);
		} catch (Exception e) {
			sb.append(String.format("ERR: %s[%s] %s", c, o, e.getMessage()));
		}
	}

	@SuppressWarnings("rawtypes")
	private void writeArray(Class c, Object o, String d) {
		Class cc = c.getComponentType();
		int n = Array.getLength(o);
		boolean b = n > 1;
		String nd = d + ds;
		String cd = b ? cns + nd : cs;
		sb.append(a1s);
		if (b)
			sb.append(ns).append(nd);
		for (int i = 0; i < n; i++) {
			if (i > 0)
				sb.append(cd);
			sb.append(i++).append(ads);
			write(cc, Array.get(o, i), nd);
		}
		sb.append(a2s);
	}

	@SuppressWarnings("rawtypes")
	private void writeList(List l, String d) {
		boolean b = l.size() > 1;
		String nd = d + ds;
		String cd = b ? cns + nd : cs;
		sb.append(a1s);
		if (b)
			sb.append(ns).append(nd);
		int i = 0;
		for (Object lo : l) {
			if (i > 0)
				sb.append(cd);
			sb.append(i++).append(ads);
			write(lo == null ? null : lo.getClass(), lo, nd);
		}
		sb.append(a2s);
	}

	@SuppressWarnings("rawtypes")
	private void writeMap(Map m, String d) {
		boolean b = m.size() > 1;
		String nd = d + ds;
		String cd = b ? cns + nd : cs;
		sb.append(o1s);
		if (b)
			sb.append(ns).append(nd);
		Set set = m.keySet();
		int i = 0;
		for (Object so : set) {
			if (i > 0)
				sb.append(cd);
			sb.append(i++).append(".").append(so).append(ods);
			write(so == null ? null : so.getClass(), m.get(so), nd);
		}
		sb.append(o2s);
	}

	@SuppressWarnings("rawtypes")
	private void writeFields(Class c, Object o, String d) {
		try {
			List<Field> l = new ArrayList<Field>();
			listFields(l, c);
			boolean b = l.size() > 1;
			String nd = d + ds;
			String cd = b ? cns + nd : cs;
			sb.append(o1s);
			if (b)
				sb.append(ns).append(nd);
			int i = 0;
			for (Field f : l) {
				if (i > 0)
					sb.append(cd);
				sb.append(i++).append(".").append(f.getName()).append(ods);
				write(f.getType(), f.get(o), nd);
			}
			sb.append(o2s);
			return;
		} catch (Exception e) {
			sb.append(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	private void listFields(List<Field> l, Class c) {
		Class sc = c.getSuperclass();
		if (appendSuperClassAtLast == false) {
			if (sc != null && !sc.equals(TData.class) && !sc.equals(Object.class))
				listFields(l, sc);
		}
		Field[] fa = c.getDeclaredFields();
		AccessibleObject.setAccessible(fa, true);
		for (Field f : fa)
			if (!Modifier.isStatic(f.getModifiers()))
				l.add(f);
		if (appendSuperClassAtLast) {
			if (sc != null && !sc.equals(TData.class) && !sc.isInterface())
				listFields(l, sc);
		}
	}

	public String buildString() {
		return sb == null ? "null" : sb.toString();
	}
}
