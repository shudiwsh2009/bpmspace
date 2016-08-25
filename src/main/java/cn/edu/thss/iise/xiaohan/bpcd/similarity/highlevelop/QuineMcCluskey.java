package cn.edu.thss.iise.xiaohan.bpcd.similarity.highlevelop;

/**
 * @author
 */

// exception class
class ExceptionQuine extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ExceptionQuine(String str) {
		super(str);
	}
}

// minterm class to hold a minterm
class MinTerm {

	// constants characters for logic 0, 1 and dont care
	public static final char NOT_CH = '0';
	public static final char SET_CH = '1';
	public static final char ANY_CH = 'X';

	// internal representation
	protected static final int NOT = 0;
	protected static final int SET = 1;
	protected static final int ANY = -1;

	// attributes
	// number of variables
	protected int count = 0;
	// the minterm
	protected int[] term;

	// constructor
	public MinTerm(String str) {
		// create a new array
		term = new int[str.length()];
		// loop on the input string and substitute
		// the character represenation with the internal one
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) {
			case NOT_CH:
				term[count++] = NOT;
				break;
			case SET_CH:
				term[count++] = SET;
				break;
			case ANY_CH:
				term[count++] = ANY;
				break;
			}
		}
	}

	// convert to string, opposite of constructor
	public String toString() {
		StringBuffer buf = new StringBuffer(count);
		// loop on internal representation and substitute
		// character representation
		for (int i = 0; i < count; i++) {
			switch (term[i]) {
			case NOT:
				buf.append(NOT_CH);
				break;
			case SET:
				buf.append(SET_CH);
				break;
			case ANY:
				buf.append(ANY_CH);
				break;
			}
		}
		return buf.toString();
	}

	// compares two minterms
	public boolean isSame(MinTerm a) throws ExceptionQuine {
		if (count != a.count)
			throw new ExceptionQuine("MinTerm::isSame()");
		for (int i = 0; i < count; i++) {
			if (term[i] != a.term[i])
				return false;
		}
		return true;
	}

	// returns the resolution count between two minterms
	// the int returned is the number of differences between
	// the two minterms
	public int resolutionCount(MinTerm a) throws ExceptionQuine {
		// check if they are equal in count
		if (count != a.count)
			throw new ExceptionQuine("MinTerm::resolutionCount()");
		// init resCount
		int resCount = 0;
		// loop on the bits and check differences
		for (int i = 0; i < count; i++)
			if (term[i] != a.term[i])
				resCount++;

		return resCount;
	}

	// checks if this minterm covers another minterm
	// used to check that a prime implicant contains another
	// minterm
	public boolean contains(MinTerm a) throws ExceptionQuine {
		// check if they are equal in count
		if (count != a.count)
			throw new ExceptionQuine("MinTerm::contains()");
		// loop on the bits and check differences
		// if the current bit is not dont care and it doesnt match with
		// the other bit
		for (int i = 0; i < count; i++)
			if ((term[i] != ANY) && (term[i] != a.term[i]))
				return false;

		return true;
	}

	/*
	 * //returns the resolution position between two minterms //the int returned
	 * is the first position of difference public int resolutionPos(MinTerm a)
	 * throws ExceptionQuine { //check for equal length if (count != a.count)
	 * throw new ExceptionQuine("MinTerm::resoutionPos()"); for (int i = 0; i <
	 * count; i++) if (term[i] != a.term[i]) return i;
	 * 
	 * //returns -1 if no differnece return -1; }
	 */

	// combines two minterms by replacing the positions that they
	// differ at with dont cares
	public static MinTerm combine(MinTerm a, MinTerm b) throws ExceptionQuine {
		// check equal count
		if (a.count != b.count)
			throw new ExceptionQuine("MinTerm::combine()");
		StringBuffer buf = new StringBuffer(a.count);
		// loop
		for (int i = 0; i < a.count; i++) {
			// if they are different -> put dont care
			if (a.term[i] != b.term[i])
				buf.append(ANY_CH);
			// else put the same value
			else
				buf.append(a.toString().charAt(i));
		}
		// return a new Minterm representing the combination
		return new MinTerm(buf.toString());
	}
}

// The main class that implements the QuineMcCluskey algorithm
class QuineMcCluskey {

	// constant representing the maximum number of
	// terms permitted: 255
	protected static final int MAX_TERMS = 0xff;

	// attributes
	// array to hold the currently existing terms
	private MinTerm[] terms = new MinTerm[MAX_TERMS];
	// array to hold the original set of input terms
	private MinTerm[] originalTerms = new MinTerm[MAX_TERMS];
	// number of currently existing terms
	public int count = 0;
	// number of original minterms
	private int originalCount = 0;

	// Add a new minterm into the array
	public void addTerm(String str) throws ExceptionQuine {
		// check if MAX_COUNT reached
		if (count == MAX_TERMS)
			throw new ExceptionQuine("Quine::addTerm()");
		// add the new term
		originalTerms[originalCount++] = new MinTerm(str);
		terms[count++] = new MinTerm(str);
	}

	// return a given term
	public String getTerm(int index) throws ExceptionQuine {
		// check exception
		if (index >= count)
			throw new ExceptionQuine("Quine::getTerm");
		return terms[index].toString();
	}

	/*
	 * //convert the object into a string public String toString() {
	 * StringBuffer buf = new StringBuffer(); for (int i = 0; i < count; i++) {
	 * buf.append(terms[i] + "\n"); } return buf.toString(); }
	 */

	// check if the object has the given term
	public boolean hasTerm(MinTerm a) throws ExceptionQuine {
		// loop on the terms and check
		for (int i = 0; i < count; i++) {
			if (a.isSame(terms[i]))
				return true;
		}
		return false;
	}

	// function to simplify the given terms together
	public void simplify() throws ExceptionQuine {
		// call reduce until the number of reduced terms are zero
		// i.e. no more terms to reduce
		while (reduce() > 0)
			;

		// recuce the result by finding only the
		// essential terms
		findEssentialImplicants();
	}

	// reduce the present minterms
	private int reduce() throws ExceptionQuine {

		// number of terms reduced
		int reducedCount = 0;
		// array of reduced minterms
		MinTerm[] reducedTerms = new MinTerm[MAX_TERMS];
		// boolean array to denote which minterms have been
		// used in the reduction process
		boolean[] used = new boolean[MAX_TERMS];

		// try to combine all minterms with all others
		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				// if the resolution count between the two minterms == 1
				// then we can combine these two minterms together
				if (terms[i].resolutionCount(terms[j]) == 1) {
					// add the combined minterm into the reducedTerms array
					reducedTerms[reducedCount++] = MinTerm.combine(terms[i],
							terms[j]);
					// mark the two minterms as used
					used[i] = true;
					used[j] = true;
				}
			}
		}

		// loop on all minterms and include those that
		// are not used in the reduction process
		int totalReduced = reducedCount;
		for (int i = 0; i < count; i++) {
			// if not used, then add to reducedTerms array
			if (used[i] == false)
				reducedTerms[totalReduced++] = terms[i];
		}

		// copy the reducedTerms array into the terms array
		// to prepare for the next interation
		count = 0;
		for (int i = 0; i < totalReduced; i++) {
			// check if not already present before addition
			if (!hasTerm(reducedTerms[i]))
				terms[count++] = reducedTerms[i];
		}
		// anzahl der reduzierungen liefern
		return reducedCount;
	}

	// function to find the essential implicants and remove others
	private void findEssentialImplicants() throws ExceptionQuine {

		int[] originalCovered = new int[originalCount];
		boolean[] finalAdded = new boolean[count];
		MinTerm[] finalTerms = new MinTerm[MAX_TERMS];
		int finalCount = 0;
		int lastCovered = -1;
		// loop on the original input minterms
		for (int i = 0; i < originalCount; i++) {
			// loop on the final terms
			for (int j = 0; j < count; j++) {
				// check if this minterm covers the original
				// minterm
				if (terms[j].contains(originalTerms[i])) {
					// save its index
					lastCovered = j;
					// increment covered entry
					originalCovered[i]++;
				}
			}
			// check if it was covered by only 1 minterm
			// then this minterm is an essential minterm
			if (originalCovered[i] == 1 && !finalAdded[lastCovered]) {
				// add to final array
				finalTerms[finalCount++] = new MinTerm(
						terms[lastCovered].toString());
				finalAdded[lastCovered] = true;
			}

			// reset original covered
			originalCovered[i] = 0;
		}

		// loop on the original terms and check which are not
		// covered and find out which terms are included
		for (int i = 0; i < originalCount && originalCovered[i] == 0; i++) {

			// check if this is covered by a final variable
			for (int j = 0; j < finalCount && originalCovered[i] == 0; j++)
				if (finalTerms[j].contains(originalTerms[i]))
					originalCovered[i] = 1;

			// loop on the other terms that are not final and check
			// if they can cover this original minterm
			for (int j = 0; originalCovered[i] == 0 && j < count; j++) {
				// check if this term can cover that original term
				if (!finalAdded[j] && terms[j].contains(originalTerms[i])) {
					// add this to final terms
					finalTerms[finalCount++] = new MinTerm(terms[j].toString());
					// mark it as final
					finalAdded[j] = true;
					// it is now covered
					originalCovered[i] = 1;
				}
			}
		}

		// copy finalTerms array into terms array
		for (count = 0; count < finalCount; count++)
			terms[count] = finalTerms[count];
	}
}
