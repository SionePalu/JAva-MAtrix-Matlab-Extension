/*
 * Copyright (c) 2006 Gregor Heinrich. All rights reserved. Redistribution and
 * use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: 1. Redistributions of source
 * code must retain the above copyright notice, this list of conditions and the
 * following disclaimer. 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
 * Created on 03.08.2006
 */
package jamaextension.jamax;

import java.util.ArrayList;
import java.util.Collections;

//import org.knowceans.util.Vectors;

/**
 * SparseMatrix is a sparse matrix with row-major format.
 * <p>
 * Conventions: except for the inherited methods and normalise(double),
 * operations leave <tt>this</tt> ummodified (immutable) if there is a return
 * value. Within operations, no pruning of values close to zero is done. Pruning
 * can be controlled via the prune() method.
 * 
 * Codes downloaded from 'http://www.arbylon.net'
 * 
 * @author gregor :: arbylon . net
 */
public class SparseMatrix extends ArrayList<SparseVector>
{

    private static final long serialVersionUID = 1L;
    private int maxVLength;

    /**
     * empty sparse matrix
     */
    public SparseMatrix()
    {
        super();
    }

    /**
     * empty sparse matrix with allocated number of rows
     * 
     * @param rows
     * @param cols
     */
    public SparseMatrix(int rows, int cols)
    {
        this();
        adjustMaxIndex(rows - 1, cols - 1);
    }

    /**
     * create sparse matrix from full matrix
     * 
     * @param x
     */
    public SparseMatrix(double[][] x)
    {
        this(x.length - 1, x[0].length - 1);
        for (int i = 0; i < x.length; i++)
        {
            SparseVector v = new SparseVector(x[i]);
            set(i, v);
        }
    }

    /**
     * copy contructor
     * 
     * @param matrix
     */
    public SparseMatrix(SparseMatrix matrix)
    {
        for (SparseVector s : matrix)
        {
            add(s.copy());
        }
    }

    /**
     * create dense representation
     * 
     * @return
     */
    public double[][] getDense()
    {
        double[][] aa = new double[size()][];
        for (int i = 0; i < size(); i++)
        {
            aa[i] = new double[maxVLength];
            for (int j : get(i).keySet())
            {
                aa[i][j] = get(i).get(j);
            }
        }
        return aa;
    }

    /**
     * set the sparse vector at index i.
     * 
     * @param i
     * @param x
     * @return the old value of the element
     */
    public SparseVector set(int i, SparseVector x)
    {
        adjustMaxIndex(i, x.getLength() - 1);
        return super.set(i, x);
    }

    /**
     * get number at index or 0. if not set. If index > size, returns 0.
     * 
     * @param i
     * @param j
     * @return
     */
    public double get(int i, int j)
    {
        if (i > size() - 1)
        {
            return 0.;
        }
        return get(i).get(j);
    }

    /**
     * set the value at the index i,j, returning the old value or 0. Increase
     * matrix size if index exceeds the dimension.
     * 
     * @param i
     * @param j
     * @param a
     * @return
     */
    public double set(int i, int j, double a)
    {
        adjustMaxIndex(i, j);
        double b = get(i).get(j);
        get(i).put(j, a);
        return b;
    }

    /**
     * adjusts the size of the matrix.
     * 
     * @param i
     *            index addressed
     * @param j
     *            index addressed
     */
    public void adjustMaxIndex(int i, int j)
    {
        if (i > size() - 1)
        {
            increase(i);
        }
        if (j >= maxVLength)
        {
            maxVLength = j + 1;
            for (int row = 0; row < size(); row++)
            {
                get(row).setLength(maxVLength);
            }
        }
    }

    /**
     * increase the size of the matrix with empty element SparseVectors.
     * 
     * @param i
     */
    private void increase(int i)
    {
        addAll(Collections.nCopies(i - size() + 1, new SparseVector()));
    }

    /**
     * get the size of the matrix
     * 
     * @return
     */
    public int[] getSize()
    {
        return new int[]
        {
                size(), maxVLength
        };

    }

    /**
     * adds a to the specified element, growing the matrix if necessary.
     * 
     * @param i
     * @param j
     * @param a
     * @return new value
     */
    public double add(int i, int j, double a)
    {
        adjustMaxIndex(i, j);
        double b = get(i, j);
        a += b;
        set(i, j, a);
        return a;
    }

    /**
     * normalise rows to rowsum
     * 
     * @param rowsum
     *            for each row
     * @return vector of old row sums
     */
    public SparseVector normalise(double rowsum)
    {
        SparseVector sums = new SparseVector();
        int i = 0;
        for (SparseVector vec : this)
        {
            sums.put(i, vec.normalise(rowsum));
            i++;
        }
        return sums;
    }

    /**
     * normalise by major dimension (rows)
     */
    public void normaliseRows()
    {
        for (SparseVector vec : this)
        {
            vec.normalise();
        }
    }

    /**
     * normalise by minor dimension (columns), expensive.
     */
    public void normaliseCols()
    {
        double[] sums = new double[maxVLength];
        for (int row = 0; row < size(); row++)
        {
            for (int col = 0; col < get(row).getLength(); col++)
            {
                sums[col] += get(row).get(col);
            }
        }
        for (int row = 0; row < size(); row++)
        {
            for (int col = 0; col < get(row).getLength(); col++)
            {
                get(row).mult(col, 1 / sums[col]);
            }
        }
    }

    /**
     * copy the matrix and its elements
     */
    public SparseMatrix copy()
    {
        return new SparseMatrix(this);
    }

    /**
     * immutable multiply this times the vector: A * x, i.e., rowwise.
     * 
     * @param v
     * @return
     */
    public SparseVector times(SparseVector v)
    {
        SparseVector w = new SparseVector();
        for (int i = 0; i < size(); i++)
        {
            w.add(i, get(i).times(v));
        }
        return w;
    }

    /**
     * immutable multiply the vector times this: x' * A, i.e., colwise.
     * 
     * @param v
     * @return
     */
    public SparseVector vectorTimes(SparseVector v)
    {
        SparseVector w = new SparseVector();
        // only the rows in A that v is nonzero
        for (int i : v.keySet())
        {
            SparseVector a = get(i).copy();
            a.factor(v.get(i));
            w.add(a);
        }
        return w;
    }

    /**
     * mutable multiply this matrix (A) with M : A * M'
     * 
     * @param m
     * @return modified this
     */
    public SparseMatrix timesTransposed(SparseMatrix m)
    {
        // A*M = ;( A(i,:) * M )
        for (int i = 0; i < size(); i++)
        {
            set(i, m.times(get(i)));
        }
        return this;
    }

    /**
     * immutable multiply this matrix (A) with M : A * M
     * 
     * @param m
     * @return matrix product
     */
    public SparseMatrix times(SparseMatrix m)
    {
        SparseMatrix s = new SparseMatrix();
        for (int i = 0; i < size(); i++)
        {
            for (int j = 0; j < m.size(); j++)
            {
                for (int k : get(i).keySet())
                {
                    double a = m.get(k, j);
                    if (a != 0.)
                    {
                        s.add(i, j, get(i, k) * a);
                    }
                }
            }
        }
        return s;
    }

    /**
     * immutable multiply matrix M with this (A) : M * A
     * 
     * @param m
     * @return
     */
    public SparseMatrix matrixTimes(SparseMatrix m)
    {
        return m.times(this);
    }

    /**
     * immutable transpose.
     * 
     * @return
     */
    public SparseMatrix transpose()
    {
        SparseMatrix s = new SparseMatrix();
        for (int i = 0; i < size(); i++)
        {
            s.set(i, getColum(i));
        }
        return s;
    }

    /**
     * get a column of the sparse matrix (expensive).
     * 
     * @return
     */
    public SparseVector getColum(int i)
    {
        SparseVector s = new SparseVector();
        for (int row = 0; row < size(); row++)
        {
            double v = get(row, i);
            if (v != 0.)
            {
                s.put(row, v);
            }
        }
        return s;
    }

    /**
     * mutable Hadamard product
     * 
     * @param m
     */
    public void hadamardProduct(SparseMatrix m)
    {
        for (int i = 0; i < size(); i++)
        {
            get(i).hadamardProduct(m.get(i));
        }
    }

    /**
     * mutable m2 = m .^ s
     * 
     * @param s
     * @return
     */
    public void hadamardPower(double s)
    {
        for (int i = 0; i < size(); i++)
        {
            get(i).hadamardPower(s);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++)
        {
            sb.append(i).append(" => ").append(get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * prints a dense representation
     * 
     * @return
     */
    /*
     * public String toStringDense() { return Vectors.print(getDense()); }
     */

    /**
     * prune all values whose magnitude is below threshold
     */
    public void prune(double threshold)
    {
        // for (SparseVector v : this) {
        for (int i = 0; i < size(); i++)
        {
            SparseVector a = get(i);
            a.prune(threshold);
        }

    }
}
