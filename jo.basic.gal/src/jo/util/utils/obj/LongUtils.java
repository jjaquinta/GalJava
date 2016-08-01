/*
 * Created on Sep 25, 2005
 *
 */
package jo.util.utils.obj;

public class LongUtils
{
    public static long[] dup(long[] arr)
    {
        long[] ret = new long[arr.length];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        return ret;
    }

    public static long parseLong(Object o)
    {
        if (o == null)
            return 0L;
        if (o instanceof Number)
            return ((Number)o).longValue();
        else
            return parseLong(o.toString());
    }

    public static long parseLong(String str)
    {
        try
        {
            return Long.parseLong(str);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static Object[] toArray(long[] longArray)
    {
        if (longArray == null)
            return null;
        Long[] objArray = new Long[longArray.length];
        for (int i = 0; i < longArray.length; i++)
            objArray[i] = new Long(longArray[i]);
        return objArray;
    }
    
    public static long sgn(long l)
    {
        if (l < 0)
            return -l;
        else
            return l;
    }
}
