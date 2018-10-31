/*
 * DifferenceInd.java
 *
 * Created on 27 November 2007, 11:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jamaextension.jamax.datafun;

import jamaextension.jamax.Indices;
import jamaextension.jamax.constants.Dimension;

/**
 * 
 * @author Feynman Perceptrons
 */
public class DifferenceInd extends Difference
{

    public DifferenceInd(Indices A)
    {
        this(A, 1);
    }

    /** Creates a new instance of DifferenceInd */
    public DifferenceInd(Indices A, int order)
    {
        this(A, order, null);
    }

    public DifferenceInd(Indices A, int order, Dimension dim)
    {
        if (A == null)
        {
            throw new IllegalArgumentException("DifferenceMat : Parameter \"A\" , must be non-null.");
        }
        if (order < 1)
        {
            throw new IllegalArgumentException("DifferenceMat : The parameter order (= " + order
                    + ") , must be at least one.");
        }
        this.order = order;
        if (dim == null)
        {
            if (A.isVector())
            {
                differenceVector(A);
            }
            else
            {
                differenceMatrix(A, Dimension.ROW);
            }
        }
        else
        {
            differenceMatrix(A, dim);
        }
    }

    private void differenceMatrix(Indices A, Dimension dim)
    {
        if (A.isVector())
        {
            if (A.isRowVector())
            {// row vector
                if (dim == Dimension.ROW)
                {
                    differenceObject = A.copy();
                }
                else
                {
                    differenceVector(A);
                }
            }
            else
            {// column vector
                if (dim == Dimension.COL)
                {
                    differenceObject = A.copy();
                }
                else
                {
                    differenceVector(A);
                }
            }
        }
        else
        {
            differenceMatrixAll(A, dim);
        }
    }

    private void differenceMatrixAll(Indices A, Dimension dim)
    {
        int row = A.getRowDimension();
        int col = A.getColumnDimension();
        int[][] temp1 = A.getArray();
        int[][] temp2 = null;
        // System.out.println("----- differenceMatrixAll -----");
        switch (dim)
        {
        case COL:
        {
            if (col == 1)
            {
                differenceObject = null;
                return;
            }

            int newColumn = 0;
            for (int k = 1; k <= order; k++)
            {
                newColumn = col - k;
                if (newColumn > 0)
                {
                    temp2 = new int[row][newColumn]; // System.out.println("Row----- temp2 -----");
                    for (int i = 0; i < row; i++)
                    {
                        for (int j = newColumn - 1; j >= 0; j--)
                        {
                            temp2[i][j] = temp1[i][j + 1] - temp1[i][j];
                        }
                    }
                    temp1 = temp2;
                }
                else
                {
                    differenceObject = null;
                    return;
                }
            }
            break;
        }
        case ROW:
        {
            if (row == 1)
            {
                differenceObject = null;
                return;
            }

            int newRow = 0;
            for (int k = 1; k <= order; k++)
            {
                newRow = row - k;
                if (newRow > 0)
                {
                    temp2 = new int[newRow][col]; // System.out.println("Col----- temp2 -----");
                    for (int j = 0; j < col; j++)
                    {
                        for (int i = newRow - 1; i >= 0; i--)
                        {
                            temp2[i][j] = temp1[i + 1][j] - temp1[i][j];
                        }
                    }
                    temp1 = temp2;
                }
                else
                {
                    differenceObject = null;
                    return;
                }
            }
            break;
        }
        default:
            throw new IllegalArgumentException("differenceMatrixAll : Dimension  " + dim.toString()
                    + " , not supported.");
        }// end switch
        differenceObject = new Indices(temp2);
    }

    private void differenceVector(Indices A)
    {
        if (A.isVector() == false)
        {
            throw new IllegalArgumentException(
                    " differenceVector :  Parameter \"A\" must be a vector and not a matrix.");
        }

        int c = A.length();
        if (c == 1)
        {// if only one element , then return.
            differenceObject = null;
            return;
        }

        int row = A.getRowDimension();
        int col = A.getColumnDimension();
        int[][] temp1 = A.getArray();
        int[][] temp2 = null;

        if (A.isRowVector())
        {
            int newColumn = 0;
            for (int k = 1; k <= order; k++)
            {
                newColumn = col - k;
                if (newColumn > 0)
                {
                    temp2 = new int[1][newColumn];
                    for (int j = newColumn - 1; j >= 0; j--)
                    {
                        temp2[0][j] = temp1[0][j + 1] - temp1[0][j];
                    }
                    temp1 = temp2;
                }
                else
                {
                    differenceObject = null;
                    return;
                }
            }// end for
        }
        else
        {
            int newRow = 0;
            for (int k = 1; k <= order; k++)
            {
                newRow = row - k;
                if (newRow > 0)
                {
                    temp2 = new int[newRow][1];
                    for (int i = newRow - 1; i >= 0; i--)
                    {
                        temp2[i][0] = temp1[i + 1][0] - temp1[i][0];
                    }
                    temp1 = temp2;
                }
                else
                {
                    differenceObject = null;
                    return;
                }
            }// end for
        }

        differenceObject = new Indices(temp2);
    }

    public static void main(String[] args)
    {
        Indices A = SampleMatrices.generateIndices();
        A = A.getRowAt(0);
        System.out.println("----- A -----");
        A.print(4, 0);

        DifferenceInd dm = new DifferenceInd(A.transpose());
        Indices diff = (Indices) dm.getDifferenceObject();

        if (diff != null)
        {
            System.out.println("----- dm -----");
            diff.print(4, 0);
        }
        else
        {
            System.out.println("----- NULL -----");
        }
        /*
         * Matrix row = A.getRowAt(2); Matrix col = A.getColumnAt(4); MaxMat M =
         * new MaxMat(col, Dimension.COL); Matrix Mr = (Matrix)M.getMaxObject();
         * System.out.println("----- Mr -----"); Mr.print(4,0); Indices Ir =
         * M.getIndices(); System.out.println("----- Ir -----");
         * Ir.plus(1).print(4,0);
         */
    }

}// --------------------------- End Class Definition
// ----------------------------

