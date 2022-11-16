package com.koolearn.klibrary.core.tree;

import com.koolearn.android.util.LogUtil;

import java.util.*;

// 树结构
public abstract class ZLTree<T extends ZLTree<T>> implements Iterable<T> {
	private int mySize = 1;
	public final T Parent;
	public final int Level;
	private volatile List<T> mySubtrees;

	protected ZLTree() {
		this(null);
	}

	protected ZLTree(T parent) {
		this(parent, -1);
	}

	protected ZLTree(T parent, int position) {
		LogUtil.i3("openBookText" + position);
		if (position == -1) {
			position = parent == null ? 0 : parent.subtrees().size();
		}
		if (parent != null && (position < 0 || position > parent.subtrees().size())) {
			throw new IndexOutOfBoundsException("`position` value equals " + position + " but must be in range [0; " + parent.subtrees().size() + "]");
		}
		Parent = parent;
		if (parent != null) {
			Level = parent.Level + 1;
			parent.addSubtree((T)this, position);
		} else {
			Level = 0;
		}
	}

	public final int getSize() {
		return mySize;
	}

	public final boolean hasChildren() {
		return mySubtrees != null && !mySubtrees.isEmpty();
	}

	public List<T> subtrees() {
		if (mySubtrees == null) {
			return Collections.emptyList();
		}
		synchronized (mySubtrees) {
			return new ArrayList<T>(mySubtrees);
		}
	}

	public synchronized final T getTreeByParagraphNumber(int index) {
		LogUtil.i3("getTreeByParagraphNumber:"+index);
		if (index < 0 || index >= mySize) {
			// TODO: throw an exception?
			return null;
		}
		if (index == 0) {
			return (T)this;
		}
		--index;
		if (mySubtrees != null) {
			synchronized (mySubtrees) {
				for (T subtree : mySubtrees) {
					if (((ZLTree<?>)subtree).mySize <= index) {
						index -= ((ZLTree<?>)subtree).mySize;
					} else {
						return (T)subtree.getTreeByParagraphNumber(index);
					}
				}
			}
		}
		throw new RuntimeException("That's impossible!!!");
	}

	synchronized final void addSubtree(T subtree, int position) {
		if (mySubtrees == null) {
			mySubtrees = Collections.synchronizedList(new ArrayList<T>());
		}
		final int subtreeSize = subtree.getSize();
		synchronized (mySubtrees) {
			final int thisSubtreesSize = mySubtrees.size();
			while (position < thisSubtreesSize) {
				subtree = mySubtrees.set(position++, subtree);
			}
			mySubtrees.add(subtree);
			for (ZLTree<?> parent = this; parent != null; parent = parent.Parent) {
				parent.mySize += subtreeSize;
			}
		}
	}

	synchronized public final void moveSubtree(T subtree, int index) {
		if (mySubtrees == null || !mySubtrees.contains(subtree)) {
			return;
		}
		if (index < 0 || index >= mySubtrees.size()) {
			return;
		}
		mySubtrees.remove(subtree);
		mySubtrees.add(index, subtree);
	}

	public void removeSelf() {
		final int subtreeSize = getSize();
		ZLTree<?> parent = Parent;
		if (parent != null) {
			parent.mySubtrees.remove(this);
			for (; parent != null; parent = parent.Parent) {
				parent.mySize -= subtreeSize;
			}
		}
	}

	public final void clear() {
		final int subtreesSize = mySize - 1;
		if (mySubtrees != null) {
			mySubtrees.clear();
		}
		mySize = 1;
		if (subtreesSize > 0) {
			for (ZLTree<?> parent = Parent; parent != null; parent = parent.Parent) {
				parent.mySize -= subtreesSize;
			}
		}
	}

	public final TreeIterator iterator() {
		return new TreeIterator(Integer.MAX_VALUE);
	}

	public final Iterable<T> allSubtrees(final int maxLevel) {
		return new Iterable<T>() {
			public TreeIterator iterator() {
				return new TreeIterator(maxLevel);
			}
		};
	}

	private class TreeIterator implements Iterator<T> {
		private T myCurrentElement = (T)ZLTree.this;
		private final LinkedList<Integer> myIndexStack = new LinkedList<Integer>();
		private final int myMaxLevel;

		TreeIterator(int maxLevel) {
			myMaxLevel = maxLevel;
		}

		public boolean hasNext() {
			return myCurrentElement != null;
		}

		public T next() {
			final T element = myCurrentElement;
			if (element.hasChildren() && element.Level < myMaxLevel) {
				myCurrentElement = (T)((ZLTree<?>)element).mySubtrees.get(0);
				myIndexStack.add(0);
			} else {
				ZLTree<T> parent = element;
				while (!myIndexStack.isEmpty()) {
					final int index = myIndexStack.removeLast() + 1;
					parent = parent.Parent;
					synchronized (parent.mySubtrees) {
						if (parent.mySubtrees.size() > index) {
							myCurrentElement = parent.mySubtrees.get(index);
							myIndexStack.add(index);
							break;
						}
					}
				}
				if (myIndexStack.isEmpty()) {
					myCurrentElement = null;
				}
			}
			return element;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
