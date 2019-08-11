package info.kucharczyk.java.simple_people_base.dataloader;

import java.io.Closeable;
import java.util.Enumeration;

public interface Loader<T> extends Closeable, Enumeration<T> {
}
