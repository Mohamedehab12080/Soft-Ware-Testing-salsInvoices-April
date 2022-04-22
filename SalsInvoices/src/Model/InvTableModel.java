
package Model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import view.InvFrame;

public class InvTableModel extends AbstractTableModel
{
    private ArrayList<InvoiceHeader> invoicesArray;
    private String [] columns ={"Invoice Num","Invoice Date","Customer Name","Invoice Total"};
    public InvTableModel(ArrayList<InvoiceHeader> invoicesArray) {
        this.invoicesArray = invoicesArray;
    }
    

    @Override
    public int getRowCount() 
    {
        return invoicesArray.size();
    }

    @Override
    public int getColumnCount() 
    {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        InvoiceHeader inv=invoicesArray.get(rowIndex);
        switch(columnIndex)
        {
            case 0:return inv.getNum();
            case 1:return InvFrame.dateFormat.format(inv.getInvDate());
            case 2:return inv.getCustomer();
            case 3:return inv.getInvoiceTotal();
            
        }
        return "";
    }
    
    @Override 
    public String getColumnName(int column)
    {
        return columns[column];
    }
            
}
