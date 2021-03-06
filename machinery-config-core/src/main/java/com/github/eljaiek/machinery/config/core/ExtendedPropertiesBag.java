package com.github.eljaiek.machinery.config.core;

import static lombok.AccessLevel.PACKAGE;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

@AllArgsConstructor(access = PACKAGE)
final class ExtendedPropertiesBag implements PropertiesBag {

  private final PropertiesBag delegate;

  @Getter(PACKAGE)
  private final Set<String> transientPropertyKeys;

  private final Consumer<Set<Property>> saveBatch;

  public ExtendedPropertiesBag(
      @NonNull PropertiesBag delegate, @NonNull Consumer<Set<Property>> saveBatch) {
    this(delegate, UnifiedSet.newSet(), saveBatch);
  }

  boolean isTransient(String propertyKey) {
    return transientPropertyKeys.contains(propertyKey);
  }

  boolean hasTransientProperties() {
    return !transientPropertyKeys.isEmpty();
  }

  @Override
  public void put(Property property) {
    delegate.put(property);
    transientPropertyKeys.add(property.key());
  }

  @Override
  public Property put(String key, String value) {
    var property = delegate.put(key, value);
    transientPropertyKeys.add(key);
    return property;
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public void clear() {
    transientPropertyKeys.clear();
    delegate.clear();
  }

  @Override
  public <T> Optional<T> getValueAs(String key, @NonNull Function<String, T> as) {
    return delegate.getValueAs(key, as);
  }

  @Override
  public Set<Property> getAll(@NonNull Set<String> keys) {
    return delegate.getAll(keys);
  }

  @Override
  public Optional<String> getValue(String key) {
    return delegate.getValue(key);
  }

  @Override
  public String getValueAsText(String key) {
    return delegate.getValueAsText(key);
  }

  @Override
  public int getValueAsInt(String key) {
    return delegate.getValueAsInt(key);
  }

  @Override
  public float getValueAsFloat(String key) {
    return delegate.getValueAsFloat(key);
  }

  @Override
  public long getValueAsLong(String key) {
    return delegate.getValueAsLong(key);
  }

  @Override
  public List<String> getValueAsList(String key) {
    return delegate.getValueAsList(key);
  }

  @Override
  public List<String> getValueAsList(String key, @NonNull String splitRegex) {
    return delegate.getValueAsList(key, splitRegex);
  }

  @Override
  public <T> List<T> getValueAsList(String key, @NonNull Function<String, T> as) {
    return delegate.getValueAsList(key, as);
  }

  @Override
  public <T> List<T> getValueAsList(
      String key, Function<String, T> as, @NonNull String splitRegex) {
    return delegate.getValueAsList(key, as, splitRegex);
  }

  @Override
  public void flush() {
    saveBatch.accept(delegate.getAll(transientPropertyKeys));
    clear();
  }

  @Override
  public Stream<Property> stream() {
    return delegate.stream();
  }

  @Override
  public Set<String> keys() {
    return delegate.keys();
  }

  @Override
  public void forEach(Consumer<Property> consumer) {
    delegate.forEach(consumer);
  }

  @Override
  public Property get(String key) {
    return delegate.get(key);
  }

  @Override
  public Optional<Property> remove(String key) {
    transientPropertyKeys.removeIf(x -> x.equals(key));
    return delegate.remove(key);
  }
}
