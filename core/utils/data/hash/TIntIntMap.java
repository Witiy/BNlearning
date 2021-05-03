package BNlearning.core.utils.data.hash;


import BNlearning.core.utils.data.common.TIntCollection;
import BNlearning.core.utils.data.set.TIntSet;

import java.util.Map;


/**
 * Interface for a primitive map of int keys and int values.
 */
interface TIntIntMap {

    /**
     * Returns the value that will be returned from {@link #get} or {@link #put} if no
     * entry exists for a given key. The default value is generally zero, but can be
     * changed during construction of the collection.
     *
     * @return the value that represents a null key in this collection.
     */
    int getNoEntryKey();

    /**
     * Returns the value that will be returned from {@link #get} or {@link #put} if no
     * entry exists for a given key. The default value is generally zero, but can be
     * changed during construction of the collection.
     *
     * @return the value that represents a null value in this collection.
     */
    int getNoEntryValue();

    /**
     * Inserts a key/value pair into the map.
     *
     * @param key   an <code>int</code> value
     * @param value an <code>int</code> value
     * @return the previous value associated with <tt>key</tt>, or the "no entry" value
     * if none was found (see {@link #getNoEntryValue}).
     */
    int put(int key, int value);

    /**
     * Inserts a key/value pair into the map if the specified key is not already
     * associated with a value.
     *
     * @param key   an <code>int</code> value
     * @param value an <code>int</code> value
     * @return the previous value associated with <tt>key</tt>, or the "no entry" value
     * if none was found (see {@link #getNoEntryValue}).
     */
    int putIfAbsent(int key, int value);

    /**
     * Put all the entries from the given Map into this map.
     *
     * @param map The Map from which entries will be obtained to put into this map.
     */
    void putAll(Map<? extends Integer, ? extends Integer> map);

    /**
     * Put all the entries from the given map into this map.
     *
     * @param map The map from which entries will be obtained to put into this map.
     */
    void putAll(TIntIntMap map);

    /**
     * Retrieves the value for <tt>key</tt>
     *
     * @param key an <code>int</code> value
     * @return the previous value associated with <tt>key</tt>, or the "no entry" value
     * if none was found (see {@link #getNoEntryValue}).
     */
    int get(int key);

    /**
     * Empties the map.
     */
    void clear();

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * Deletes a key/value pair from the map.
     *
     * @param key an <code>int</code> value
     * @return the previous value associated with <tt>key</tt>, or the "no entry" value
     * if none was found (see {@link #getNoEntryValue}).
     */
    int remove(int key);

    /**
     * Returns an <tt>int</tt> value that is the number of elements in the map.
     *
     * @return an <tt>int</tt> value that is the number of elements in the map.
     */
    int size();

    /**
     * Returns the keys of the map as a <tt>TIntSet</tt>
     *
     * @return the keys of the map as a <tt>TIntSet</tt>
     */
    TIntSet keySet();

    /**
     * Returns the keys of the map as an array of <tt>int</tt> values.
     *
     * @return the keys of the map as an array of <tt>int</tt> values.
     */
    int[] keys();

    /**
     * Returns the keys of the map.
     *
     * @param array the array into which the elements of the list are to be stored,
     *              if it is big enough; otherwise, a new array of the same type is
     *              allocated for this purpose.
     * @return the keys of the map as an array.
     */
    int[] keys(int[] array);

    /**
     * Returns the values of the map as a <tt>TIntCollection</tt>
     *
     * @return the values of the map as a <tt>TIntCollection</tt>
     */
    TIntCollection valueCollection();

    /**
     * Returns the values of the map as an array of <tt>#e#</tt> values.
     *
     * @return the values of the map as an array of <tt>#e#</tt> values.
     */
    int[] values();

    /**
     * Returns the values of the map using an existing array.
     *
     * @param array the array into which the elements of the list are to be stored,
     *              if it is big enough; otherwise, a new array of the same type is
     *              allocated for this purpose.
     * @return the values of the map as an array of <tt>#e#</tt> values.
     */
    int[] values(int[] array);

    /**
     * Checks for the presence of <tt>val</tt> in the values of the map.
     *
     * @param val an <code>int</code> value
     * @return a <code>boolean</code> value
     */
    boolean containsValue(int val);

    /**
     * Checks for the present of <tt>key</tt> in the keys of the map.
     *
     * @param key an <code>int</code> value
     * @return a <code>boolean</code> value
     */
    boolean containsKey(int key);

    /**
     * @return a TIntIntIterator with access to this map's keys and values
     */
    TIntIntIterator iterator();

    /**
     * Increments the primitive value mapped to key by 1
     *
     * @param key the key of the value to increment
     * @return true if a mapping was found and modified.
     */
    boolean increment(int key);

    /**
     * Adjusts the primitive value mapped to key.
     *
     * @param key    the key of the value to increment
     * @param amount the amount to adjust the value by.
     * @return true if a mapping was found and modified.
     */
    boolean adjustValue(int key, int amount);

    /**
     * Adjusts the primitive value mapped to the key if the key is present in the map.
     * Otherwise, the <tt>initial_value</tt> is put in the map.
     *
     * @param key           the key of the value to increment
     * @param adjust_amount the amount to adjust the value by
     * @param put_amount    the value put into the map if the key is not initial present
     * @return the value present in the map after the adjustment or put operation
     */
    int adjustOrPutValue(int key, int adjust_amount, int put_amount);
}
