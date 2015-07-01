package phibao37.ent.models;

import java.util.HashSet;

/**
 * Manager class that manage the object by their ID
 */
public class Manager<T extends Manager.Manageable> extends HashSet<T> {
	
	private static final long serialVersionUID = -272846306839543996L;
	
	/**
	 * Return the element that has the ID match the given one, or <i>null</i> if not found
	 * @param id the id to look for
	 */
	public T get(int id){
		for (T item: this)
			if (item.getId() == id)
				return item;
		return null;
	}
	
	/**
	 * Remove an object that has a given ID
	 * @param id the ID of the object to be remove
	 */
	public boolean remove(int id){
		T match = null;
		
		for (T item: this)
			if (item.getId() == id){
				match = item;
				break;
			}
		return this.remove(match);
	}
	
	/**
	 * Define a class that support manager by {@link Manager}
	 */
	public static abstract class Manageable{
		
		/***
		 * Get the distinct identify of the object 
		 */
		public abstract int getId();
		
		public int hashCode(){
			return getId();
		}
		
		public boolean equals(Object o){
			if (o == this) return true;
			if (o == null || o.getClass() != this.getClass()) return false;
			Manageable other = (Manageable)o;
			
			return getId() == other.getId();
		}
	}
}
