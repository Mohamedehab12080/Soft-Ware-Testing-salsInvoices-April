
package Model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mohamed
 */
public class InvLineTableModel  extends  AbstractTableModel 
{
    private ArrayList<InvoiceLine> LinesArray;
    private String []columns={"Item Name","Price","Count","Line Total"};

    public InvLineTableModel(ArrayList<InvoiceLine> LinesArray) {
        this.LinesArray = LinesArray;
    }

    @Override
    public int getRowCount() 
    {
        return LinesArray ==null ? 0: LinesArray.size();
    }

    @Override
    public int getColumnCount() 
    {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        if(LinesArray==null)
        {
            return "";
        }
        else{
     InvoiceLine line=LinesArray.get(rowIndex);
     switch (columnIndex)
     {
         case 0:return line.getItem();
         case 1:return line.getPrice();
         case 2:return line.getCount();
         case 3:return line.getLineTotal();
         default: return "";
     }
        }
    }
     
    @Override
    public String getColumnName(int Column)
    {
        return columns[Column];
    }
    
}
