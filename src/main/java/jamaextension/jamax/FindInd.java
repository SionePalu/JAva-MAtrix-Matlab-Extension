/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamaextension.jamax;

/**
 * 
 * @author Sione
 */
public final class FindInd
{

    private int[] index;
    private Indices findEntries;
    private int[] findSize;
    private static boolean javaInd = false;

    private FindInd()
    {
    }

    public static FindInd create()
    {
        return new FindInd();
    }

    public Indices getIndexInd()
    {
        if (this.isNull())
        {
            return null;
        }
        int m = findSize[0];
        int n = findSize[1];
        boolean vector = (m == 1 || n == 1);
        Indices ind = null;
        if (vector)
        {
            if (m == 1)
            {
                ind = new Indices(index);
            }
            else
            {
                ind = new Indices(index).toColVector();
            }
        }
        else
        {
            ind = new Indices(index).toColVector();
        }
        return ind;
    }

    /**
     * @return the index
     */
    public int[] getIndex()
    {
        return index;
    }

    /**
     * @return the findEntries
     */
    public Indices getFindEntries()
    {
        return findEntries;
    }

    public Indices getI()
    {
        Indices I = null;
        if (this.isNull())
        {
            return I;
        }
        I = findEntries.getColumnAt(0);
        return I;
    }

    public Indices getJ()
    {
        Indices J = null;
        if (this.isNull())
        {
            return J;
        }
        J = findEntries.getColumnAt(1);
        return J;
    }

    public int[] getArrayI()
    {
        int[] I = null;
        if (this.isNull())
        {
            return I;
        }
        I = findEntries.getColumnAt(0).getRowPackedCopy();
        return I;
    }

    public int[] getArrayJ()
    {
        int[] J = null;
        if (this.isNull())
        {
            return J;
        }
        J = findEntries.getColumnAt(1).getRowPackedCopy();
        return J;
    }

    /**
     * @return the findSize
     */
    public int[] getFindSize()
    {
        return findSize;
    }

    public boolean equalSize(Indices siz)
    {
        boolean tf = false;
        if (siz == (Indices) null)
        {
            return tf;
        }
        if (!siz.isVector())
        {
            throw new ConditionalException("equalSize : Indices parameter \"siz\" must be a vector and not a matrix;");
        }
        if (siz.length() != 2)
        {
            throw new ConditionalException("equalSize : The length of Indices parameter \"siz\" must be 2 ;");
        }

        int[] sizArr = siz.getRowPackedCopy();
        tf = equalSize(sizArr);

        return tf;
    }

    public boolean equalSize(int[] siz)
    {
        boolean tf = false;
        if (siz == (int[]) null)
        {
            return tf;
        }
        if (this.findSize == (int[]) null)
        {
            return tf;
        }
        if (siz.length != 2)
        {
            throw new ConditionalException("equalSize : The length of array parameter \"siz\" must be 2 ;");
        }
        tf = (findSize[0] == siz[0]) && (findSize[1] == siz[1]);
        return tf;
    }

    public Indices getFindSizeInd()
    {
        if (findSize == null)
        {
            return null;
        }
        return new Indices(findSize);
    }

    public boolean isNull()
    {
        return index == null;
    }

    public int numel()
    {
        if (isNull())
        {
            return 0;
        }
        return this.index.length;
    }

    /**
     * @param index
     *            the index to set
     */
    protected void setIndex(int[] index)
    {
        this.index = index;
    }

    /**
     * @param findEntries
     *            the findEntries to set
     */
    protected void setFindEntries(Indices findEntries)
    {
        this.findEntries = findEntries;
    }

    /**
     * @param findSize
     *            the findSize to set
     */
    protected void setFindSize(int[] findSize)
    {
        if (findSize == null)
        {
            return;
        }
        if (findSize.length != 2)
        {
            throw new ConditionalException("setFindSize : The length of array parameter \"findSize\" must be 2 ;");
        }
        if (findSize[0] < 1)
        {
            throw new ConditionalException(
                    "setFindSize : First element of array parameter \"findSize\" must be at least 1 ;");
        }
        if (findSize[1] < 1)
        {
            throw new ConditionalException(
                    "setFindSize : Second element of array parameter \"findSize\" must be at least 1 ;");
        }
        this.findSize = findSize;
    }

    public void print(String nameObj)
    {
        int num = this.numel();
        System.out.println("\n------------------------- [" + nameObj + " - Length : " + num
                + "] -------------------------");
        if (this.isNull())
        {
            System.out.println(" NULL");
            return;
        }

        for (int i = 0; i < num; i++)
        {
            int II = this.findEntries.get(i, 0);
            int JJ = this.findEntries.get(i, 1);
            if (javaInd)
            {
                System.out.printf("\t(" + II + " , " + JJ + ")\n");
            }
            else
            {
                System.out.printf("\t(" + (II + 1) + " , " + (JJ + 1) + ")\n");
            }
        }
    }

    public static void setJavaInd(boolean jInd)
    {
        javaInd = jInd;
    }
}
