/**
 * 
 */
package com.sjsu.rollbits.intercluster.sync;

/**
 * @author nishantrathi
 *
 */
public interface ResultCollectable<T> {
	
	public void collectResult(T t);
	
	public T fetchResult();

}
